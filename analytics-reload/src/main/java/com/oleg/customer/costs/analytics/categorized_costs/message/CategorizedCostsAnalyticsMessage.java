package com.oleg.customer.costs.analytics.categorized_costs.message;

import com.oleg.customer.costs.analytics.core.Message;
import com.oleg.customer.costs.analytics.customer_costs.entity.CustomerCosts;
import com.oleg.customer.costs.analytics.period_costs.entity.PeriodCostsAnalytics;

import java.util.List;
import java.util.Map;

public record CategorizedCostsAnalyticsMessage(
    List<CustomerCosts> customerCosts,
    Map<Integer, PeriodCostsAnalytics> periodCostsAnalyticsById
) implements Message {}