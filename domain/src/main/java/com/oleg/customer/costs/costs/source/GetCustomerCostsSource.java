package com.oleg.customer.costs.costs.source;

import com.oleg.customer.costs.common.Paginator;
import com.oleg.customer.costs.costs.query.CustomerCostsQuery;
import com.oleg.customer.costs.costs.value_object.CustomerCosts;

import java.util.Set;
import java.util.List;

public interface GetCustomerCostsSource {

    int getCustomerCostsCount(int userId, int bankId);

    List<CustomerCosts> getCustomerCosts(Set<Integer> ids);

    List<CustomerCostsQuery> getCustomerCosts(int userId, int bankId, Paginator paginator);
}