package com.oleg.customer.costs.analytics.customer_costs.source;

import com.oleg.customer.costs.analytics.common.value_object.Paginator;
import com.oleg.customer.costs.analytics.customer_costs.query.CategoryCustomerCostsQuery;
import com.oleg.customer.costs.analytics.customer_costs.query.PeriodCustomerCostsQuery;

import java.util.List;

public interface GetCustomerCosts {

    List<PeriodCustomerCostsQuery> getForPeriod(int periodCostsAnalyticsId);

    List<CategoryCustomerCostsQuery> getForCategory(Paginator paginator, int categoryCostsAnalyticsId);
}
