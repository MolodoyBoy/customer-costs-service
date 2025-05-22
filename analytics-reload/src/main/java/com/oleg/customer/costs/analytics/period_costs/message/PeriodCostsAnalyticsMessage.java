package com.oleg.customer.costs.analytics.period_costs.message;

import com.oleg.customer.costs.analytics.core.Message;
import com.oleg.customer.costs.analytics.customer_costs.entity.CustomerCosts;

import java.util.List;

public record PeriodCostsAnalyticsMessage(List<CustomerCosts> customerCosts) implements Message {}