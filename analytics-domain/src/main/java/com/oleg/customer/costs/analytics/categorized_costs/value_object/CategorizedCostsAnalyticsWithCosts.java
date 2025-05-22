package com.oleg.customer.costs.analytics.categorized_costs.value_object;

import com.oleg.customer.costs.analytics.categorized_costs.snapshot.CategorizedCostsAnalyticsSnapshot;
import com.oleg.customer.costs.analytics.customer_costs.query.CategoryCustomerCostsQuery;
import com.oleg.customer.costs.analytics.customer_costs.query.PeriodCustomerCostsQuery;

import java.util.List;

public record CategorizedCostsAnalyticsWithCosts(
    List<CategoryCustomerCostsQuery> customerCosts,
    CategorizedCostsAnalyticsSnapshot categorizedCostsAnalytics
) {}