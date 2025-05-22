package com.oleg.customer.costs.analytics.period_costs.value_object;

import com.oleg.customer.costs.analytics.categorized_costs.snapshot.CategorizedCostsAnalyticsSnapshot;
import com.oleg.customer.costs.analytics.customer_costs.query.PeriodCustomerCostsQuery;
import com.oleg.customer.costs.analytics.period_costs.snapshot.PeriodCostsAnalyticsSnapshot;

import java.util.List;

public record PeriodCostsAnalyticsWithCategories(
    List<PeriodCustomerCostsQuery> customerCosts,
    PeriodCostsAnalyticsSnapshot periodCostsAnalytics,
    List<CategorizedCostsAnalyticsSnapshot> categorizedCostsAnalytics
) {
}
