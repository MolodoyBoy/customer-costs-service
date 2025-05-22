package com.oleg.customer.costs.analytics.period_costs_analytics.condition;

import com.oleg.customer.costs.analytics.common.condition.ConditionBuilder;
import com.oleg.customer.costs.analytics.period_costs.command.PeriodCostAnalyticsCreateCommand;
import org.jooq.Condition;
import org.springframework.stereotype.Component;

import static com.oleg.fund.customer.costs.analytics.tables.PeriodCostsAnalytics.PERIOD_COSTS_ANALYTICS;

@Component
public class PeriodCostAnalyticsCreateCommandConditionBuilder implements ConditionBuilder<PeriodCostAnalyticsCreateCommand> {

    @Override
    public Condition buildCondition(PeriodCostAnalyticsCreateCommand value) {
        return PERIOD_COSTS_ANALYTICS.USER_ID.eq(value.userId())
            .and(PERIOD_COSTS_ANALYTICS.PERIOD.eq(value.periodIndex()));
    }
}
