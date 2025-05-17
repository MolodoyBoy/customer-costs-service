package com.oleg.customer.costs.source;

import com.oleg.customer.costs.costs.value_object.CustomerCosts;

import java.util.List;

public interface CustomerCostsBankSource {

    List<CustomerCosts> getCustomerCosts(int userId, int bankId);
}
