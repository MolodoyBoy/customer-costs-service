package com.oleg.customer.costs.analytics.categorized_costs.command;

import com.oleg.customer.costs.analytics.customer_costs.entity.CustomerCosts;

public record CategorizedCostAnalyticsCreateCommand(int categoryId, int periodCostsAnalyticsId) {

    public CategorizedCostAnalyticsCreateCommand(CustomerCosts customerCosts) {
        this(customerCosts.categoryId(), customerCosts.periodCostsAnalyticsId());
    }
}