package com.oleg.customer.costs.analytics.categorized_costs.value_object;

import com.oleg.customer.costs.analytics.categorized_costs.snapshot.CategorizedCostsAnalyticsSnapshot;
import com.oleg.customer.costs.analytics.customer_costs.query.CustomerCostsQuery;

import java.util.List;

public record CategorizedCostsAnalyticsWithCosts(
    List<CustomerCostsQuery> customerCosts,
    CategorizedCostsAnalyticsSnapshot categorizedCostsAnalytics
) {}