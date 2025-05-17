package com.oleg.customer.costs.user_bank;

public enum AddBankStatus {

    SUCCESS("Bank added successfully!"),
    BANK_NOT_EXISTS("Bank does not exists!"),
    BANK_ALREADY_ADDED("Bank already added!"),;

    private final String description;

    AddBankStatus(String description) {
        this.description = description;
    }

    public boolean isSuccess() {
        return this == SUCCESS;
    }

    public String getDescription() {
        return description;
    }
}