package com.oleg.customer.costs.analytics.customer_costs.source;

import com.oleg.customer.costs.analytics.common.value_object.Paginator;
import com.oleg.customer.costs.analytics.customer_costs.query.CustomerCostsQuery;

import java.util.List;

public interface GetCustomerCosts {

    List<CustomerCostsQuery> getForPeriod(int periodCostsAnalyticsId);

    List<CustomerCostsQuery> getForCategory(Paginator paginator, int categoryCostsAnalyticsId);
}
