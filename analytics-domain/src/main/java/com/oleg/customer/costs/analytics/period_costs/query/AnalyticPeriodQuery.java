package com.oleg.customer.costs.analytics.period_costs.query;

import com.oleg.customer.costs.analytics.period_costs.value_object.Period;

import static com.oleg.customer.costs.analytics.period_costs.value_object.Period.of;

public record AnalyticPeriodQuery(Period period, int periodCostsAnalyticId) {

    public AnalyticPeriodQuery(int period, int periodCostsAnalyticId) {
        this(of(period), periodCostsAnalyticId);
    }
}