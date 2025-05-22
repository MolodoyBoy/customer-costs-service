package com.oleg.customer.costs.analytics.categorized_costs_analytics.condition;

import com.oleg.customer.costs.analytics.categorized_costs.command.CategorizedCostAnalyticsCreateCommand;
import com.oleg.customer.costs.analytics.common.condition.ConditionBuilder;
import org.jooq.Condition;
import org.springframework.stereotype.Component;

import static com.oleg.fund.customer.costs.analytics.tables.CategorizedCostsAnalytics.CATEGORIZED_COSTS_ANALYTICS;

@Component
public class CategorizedCostAnalyticsCreateCommandConditionBuilder implements ConditionBuilder<CategorizedCostAnalyticsCreateCommand> {

    @Override
    public Condition buildCondition(CategorizedCostAnalyticsCreateCommand value) {
        return CATEGORIZED_COSTS_ANALYTICS.CATEGORY_ID.eq(value.categoryId())
            .and(CATEGORIZED_COSTS_ANALYTICS.PERIOD_COST_ANALYTICS_ID.eq(value.periodCostsAnalyticsId()));
    }
}