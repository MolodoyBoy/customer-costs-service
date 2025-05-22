package com.oleg.customer.costs.analytics.period_costs_analytics.mapper;

import com.oleg.customer.costs.analytics.period_costs.snapshot.PeriodCostsAnalyticsSnapshot;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import static com.oleg.customer.costs.analytics.common.mapper.UtilMapper.getNullableField;
import static com.oleg.fund.customer.costs.analytics.tables.PeriodCostsAnalytics.PERIOD_COSTS_ANALYTICS;

@Component
public class PeriodCostsAnalyticsSnapshotMapper implements RecordMapper<Record, PeriodCostsAnalyticsSnapshot> {

    @Override
    public PeriodCostsAnalyticsSnapshot map(Record rc) {
        return new PeriodCostsAnalyticsSnapshot(
            rc.get(PERIOD_COSTS_ANALYTICS.ID),
            getNullableField(rc, PERIOD_COSTS_ANALYTICS.PERIOD),
            getNullableField(rc, PERIOD_COSTS_ANALYTICS.AMOUNT),
            getNullableField(rc, PERIOD_COSTS_ANALYTICS.DIFFERENCE_FROM_PREVIOUS_MONTH)
        );
    }
}
