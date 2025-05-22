package com.oleg.customer.costs.analytics.period_costs.source;

import com.oleg.customer.costs.analytics.period_costs.snapshot.PeriodCostsAnalyticsSnapshot;

public interface GetPeriodCostsAnalyticsSource {

    PeriodCostsAnalyticsSnapshot get(int id);
}