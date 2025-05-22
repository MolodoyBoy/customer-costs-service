package com.oleg.customer.costs.analytics.period_costs_analytics.mapper;

import com.oleg.customer.costs.analytics.common.mapper.ToRecordMapper;
import com.oleg.customer.costs.analytics.period_costs.command.PeriodCostAnalyticsCreateCommand;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import static com.oleg.customer.costs.analytics.period_costs.value_object.Period.of;
import static com.oleg.fund.customer.costs.analytics.tables.PeriodCostsAnalytics.PERIOD_COSTS_ANALYTICS;

@Component
public class PeriodCostAnalyticsCreateCommandMapper implements RecordMapper<Record, PeriodCostAnalyticsCreateCommand>, ToRecordMapper<PeriodCostAnalyticsCreateCommand> {

    private final DSLContext dslContext;

    public PeriodCostAnalyticsCreateCommandMapper(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public Record toRecord(PeriodCostAnalyticsCreateCommand value) {
        Record rc = dslContext.newRecord(PERIOD_COSTS_ANALYTICS);
        rc.set(PERIOD_COSTS_ANALYTICS.USER_ID, value.userId());
        rc.set(PERIOD_COSTS_ANALYTICS.PERIOD, value.periodIndex());

        return rc;
    }

    @Override
    public PeriodCostAnalyticsCreateCommand map(Record rc) {
        return new PeriodCostAnalyticsCreateCommand(
            rc.get(PERIOD_COSTS_ANALYTICS.USER_ID),
            of(rc.get(PERIOD_COSTS_ANALYTICS.PERIOD))
        );
    }
}