package com.oleg.customer.costs.bank;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankService {

    private final BankSource bankSource;

    public BankService(BankSource bankSource) {
        this.bankSource = bankSource;
    }

    public List<Bank> getSupportedBanks() {
        return bankSource.getSupportedBanks();
    }
}