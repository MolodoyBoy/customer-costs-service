package com.oleg.customer.costs.analytics.period_costs_analytics.mapper;

import com.oleg.customer.costs.analytics.period_costs.entity.PeriodCostsAnalytics;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import static com.oleg.customer.costs.analytics.common.mapper.UtilMapper.getNullableField;
import static com.oleg.fund.customer.costs.analytics.tables.PeriodCostsAnalytics.PERIOD_COSTS_ANALYTICS;

@Component
public class PeriodCostsAnalyticsMapper implements RecordMapper<Record, PeriodCostsAnalytics> {

    @Override
    public PeriodCostsAnalytics map(Record rc) {
        return new PeriodCostsAnalytics(
            rc.get(PERIOD_COSTS_ANALYTICS.ID),
            getNullableField(rc, PERIOD_COSTS_ANALYTICS.PERIOD),
            getNullableField(rc, PERIOD_COSTS_ANALYTICS.AMOUNT),
            getNullableField(rc, PERIOD_COSTS_ANALYTICS.DIFFERENCE_FROM_PREVIOUS_MONTH)
        );
    }
}