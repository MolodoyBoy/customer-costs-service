package com.oleg.customer.costs.analytics.period_costs.snapshot;

import com.oleg.customer.costs.analytics.period_costs.value_object.Period;

import java.math.BigDecimal;

import static com.oleg.customer.costs.analytics.period_costs.value_object.Period.of;

public record PeriodCostsAnalyticsSnapshot(
    int id,
    Period period,
    BigDecimal amount,
    BigDecimal differenceFromPrevious
) {

    public PeriodCostsAnalyticsSnapshot(int id, Integer period, BigDecimal amount, BigDecimal differenceFromPrevious) {
        this(id, of(period), amount, differenceFromPrevious);
    }
}