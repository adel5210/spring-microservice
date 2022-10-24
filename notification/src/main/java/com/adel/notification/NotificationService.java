package com.adel.notification;

import com.adel.clients.notification.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public void send(NotificationRequest request){
        notificationRepository.save(
                Notification.builder()
                        .toCutomerId(request.toCustomerId())
                        .toCustomerEmail(request.toCustomerName())
                        .sender("adel")
                        .message(request.message())
                        .sendAt(LocalDateTime.now())
                        .build()
        );
    }
}
