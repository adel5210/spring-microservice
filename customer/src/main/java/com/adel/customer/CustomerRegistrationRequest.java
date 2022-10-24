package com.adel.customer;

public record CustomerRegistrationRequest(
        String firstname,
        String lastname,
        String email) {
}
