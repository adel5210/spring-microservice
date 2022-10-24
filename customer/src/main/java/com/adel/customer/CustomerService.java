package com.adel.customer;

import com.adel.amqp.RabbitMQMessageProducer;
import com.adel.clients.fraud.FraudCheckResponse;
import com.adel.clients.fraud.FraudClient;
import com.adel.clients.notification.NotificationClient;
import com.adel.clients.notification.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;
//    private final RestTemplate restTemplate;
    private final FraudClient fraudClient;
//    private final NotificationClient notificationClient;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;

    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .email(request.email())
                .build();
        repository.saveAndFlush(customer);
//        FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
//                "http://localhost:8081/api/v1/fraud-check/{customerId}",
//                FraudCheckResponse.class,
//                customer.getId()
//        );
//        FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
//                "http://FRAUD/api/v1/fraud-check/{customerId}",
//                FraudCheckResponse.class,
//                customer.getId()
//        );
        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());
        if(fraudCheckResponse.isFraudster()){
            throw new IllegalStateException("fraudster");
        }

        final NotificationRequest notificationRequest = new NotificationRequest(customer.getId(),
                customer.getEmail(),
                String.format("Hi %s, welcome", customer.getFirstname()));
//        notificationClient.sendNotification(
//                notificationRequest
//        );
        rabbitMQMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );

    }
}
