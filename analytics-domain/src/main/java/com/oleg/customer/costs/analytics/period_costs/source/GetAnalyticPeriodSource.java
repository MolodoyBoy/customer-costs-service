package com.oleg.customer.costs.analytics.period_costs.source;

import com.oleg.customer.costs.analytics.period_costs.query.AnalyticPeriodQuery;

import java.util.List;

public interface GetAnalyticPeriodSource {

    List<AnalyticPeriodQuery> getAnalyticPeriods(int userId);
}