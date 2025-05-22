package com.oleg.customer.costs.analytics.period_costs_analytics.mapper;

import com.oleg.customer.costs.analytics.period_costs.query.AnalyticPeriodQuery;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import static com.oleg.fund.customer.costs.analytics.tables.PeriodCostsAnalytics.PERIOD_COSTS_ANALYTICS;

@Component
public class AnalyticPeriodMapper implements RecordMapper<Record, AnalyticPeriodQuery> {

    @Override
    public AnalyticPeriodQuery map(Record rc) {
        return new AnalyticPeriodQuery(
            rc.get(PERIOD_COSTS_ANALYTICS.PERIOD),
            rc.get(PERIOD_COSTS_ANALYTICS.ID)
        );
    }
}