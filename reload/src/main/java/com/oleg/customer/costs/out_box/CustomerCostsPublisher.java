package com.oleg.customer.costs.out_box;

import com.oleg.customer.costs.costs.value_object.CustomerCosts;

import java.util.List;

public interface CustomerCostsPublisher {

    void publish(List<CustomerCosts> customerCosts, Runnable onCompleteAction);
}