package com.oleg.customer.costs.analytics.customer_costs.source;

import com.oleg.customer.costs.analytics.customer_costs.entity.CustomerCosts;

import java.util.List;

public interface AdminCustomerCostsSource {

    void bindCustomerCostsByPeriod(List<CustomerCosts> customerCosts);

    void bindCustomerCostsByCategory(List<CustomerCosts> customerCosts);
}