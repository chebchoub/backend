package com.example.clientservice.model;


public enum ContractType {
    STANDARD,
    PREMIUM;

    public enum PremiumType {
        SILVER,
        GOLD,
        PLATINIUM
    }
}
