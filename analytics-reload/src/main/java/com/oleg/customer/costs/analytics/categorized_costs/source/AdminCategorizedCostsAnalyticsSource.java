package com.oleg.customer.costs.analytics.categorized_costs.source;

import com.oleg.customer.costs.analytics.categorized_costs.command.CategorizedCostAnalyticsCreateCommand;
import com.oleg.customer.costs.analytics.categorized_costs.entity.CategorizedCostsAnalytics;

import java.util.Collection;
import java.util.Map;

public interface AdminCategorizedCostsAnalyticsSource {

    int update(Collection<CategorizedCostsAnalytics> categorizedCostsAnalytics);

    Map<CategorizedCostAnalyticsCreateCommand, CategorizedCostsAnalytics> get(Collection<CategorizedCostAnalyticsCreateCommand> keys);

    Map<CategorizedCostAnalyticsCreateCommand, CategorizedCostsAnalytics> create(Collection<CategorizedCostAnalyticsCreateCommand> keys);
}