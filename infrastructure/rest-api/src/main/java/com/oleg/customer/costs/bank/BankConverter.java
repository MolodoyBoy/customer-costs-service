package com.oleg.customer.costs.bank;

import com.oleg.customer.costs.model.BankDto;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class BankConverter {

    public List<BankDto> convert(Collection<Bank> banks) {
        return banks.stream()
            .map(this::convert)
            .toList();
    }

    private BankDto convert(Bank bank) {
        return new BankDto(bank.id(), bank.name());
    }

    public Bank convert(BankDto bankDto) {
        return new Bank(bankDto.getId(), bankDto.getDescription());
    }
}