package com.oleg.customer.costs.analytics.period_costs.command;

import com.oleg.customer.costs.analytics.customer_costs.entity.CustomerCosts;
import com.oleg.customer.costs.analytics.period_costs.value_object.Period;

import java.time.LocalDate;

import static com.oleg.customer.costs.analytics.period_costs.value_object.Period.of;

public record PeriodCostAnalyticsCreateCommand(int userId, Period period) {

    public PeriodCostAnalyticsCreateCommand(CustomerCosts customerCosts) {
        this(customerCosts.userId(), of(customerCosts.createdAt()));
    }

    public int periodIndex() {
        return period.periodIndex();
    }

    public PeriodCostAnalyticsCreateCommand shiftMonthBack() {
        LocalDate localDate = period.toLocalDate();
        LocalDate monthBack = localDate.minusMonths(1);

        return new PeriodCostAnalyticsCreateCommand(userId, of(monthBack));
    }
}