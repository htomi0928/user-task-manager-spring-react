package com.bdo.digital.user_task_service;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    private String country;
    private int postCode;
    private String city;
    private String street;
    private int houseNumber;
}
