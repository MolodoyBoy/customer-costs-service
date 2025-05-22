package com.oleg.customer.costs.analytics.period_costs.source;

import com.oleg.customer.costs.analytics.period_costs.command.PeriodCostAnalyticsCreateCommand;
import com.oleg.customer.costs.analytics.period_costs.entity.PeriodCostsAnalytics;

import java.util.Collection;
import java.util.Map;

public interface AdminPeriodCostsAnalyticsSource {

    int update(Collection<PeriodCostsAnalytics> periodCostsAnalytics);

    Map<PeriodCostAnalyticsCreateCommand, PeriodCostsAnalytics> get(Collection<PeriodCostAnalyticsCreateCommand> keys);

    Map<PeriodCostAnalyticsCreateCommand, PeriodCostsAnalytics> create(Collection<PeriodCostAnalyticsCreateCommand> keys);
}