package com.adel.fraud;

import com.adel.clients.fraud.FraudCheckResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/fraud-check")
public class FraudController {

    private final FraudCheckService service;

    @GetMapping(path = "{customerId}")
    public FraudCheckResponse isFraudster(@PathVariable("customerId") Integer customerId){
        boolean isFraudCustomer = service.isFraudulentCustomer(customerId);
        log.info("Fraud check customer id: {}", customerId);
        return new FraudCheckResponse(isFraudCustomer);
    }
}
