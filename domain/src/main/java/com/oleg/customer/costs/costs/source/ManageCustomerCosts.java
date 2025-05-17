package com.oleg.customer.costs.costs.source;

import com.oleg.customer.costs.costs.value_object.CustomerCosts;

import java.util.Collection;

public interface ManageCustomerCosts {

    void save(Collection<CustomerCosts> customerCosts);
}