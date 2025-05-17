package com.oleg.customer.costs.bank;

import com.oleg.customer.costs.costs.value_object.CustomerCosts;
import com.oleg.customer.costs.model.UserBankCostsDto;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.time.ZoneOffset.UTC;

@Component
public class CustomerCostsConverter {

    public List<UserBankCostsDto> convert(List<CustomerCosts> customerCosts) {
        return customerCosts.stream()
            .map(this::convert)
            .toList();
    }

    private UserBankCostsDto convert(CustomerCosts customerCosts) {
        return new UserBankCostsDto()
            .id(customerCosts.id())
            .amount(customerCosts.amount())
            .createdAt(customerCosts.createdAt().atOffset(UTC))
            .description(customerCosts.description());
    }
}