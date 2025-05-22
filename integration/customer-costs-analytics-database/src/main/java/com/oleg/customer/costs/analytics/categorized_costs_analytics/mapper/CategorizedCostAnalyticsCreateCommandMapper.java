package com.oleg.customer.costs.analytics.categorized_costs_analytics.mapper;

import com.oleg.customer.costs.analytics.categorized_costs.command.CategorizedCostAnalyticsCreateCommand;
import com.oleg.customer.costs.analytics.common.mapper.ToRecordMapper;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import static com.oleg.fund.customer.costs.analytics.tables.CategorizedCostsAnalytics.CATEGORIZED_COSTS_ANALYTICS;

@Component
public class CategorizedCostAnalyticsCreateCommandMapper implements RecordMapper<Record, CategorizedCostAnalyticsCreateCommand>, ToRecordMapper<CategorizedCostAnalyticsCreateCommand> {

    private final DSLContext dslContext;

    public CategorizedCostAnalyticsCreateCommandMapper(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public Record toRecord(CategorizedCostAnalyticsCreateCommand value) {
        Record rc = dslContext.newRecord(CATEGORIZED_COSTS_ANALYTICS);
        rc.set(CATEGORIZED_COSTS_ANALYTICS.CATEGORY_ID, value.categoryId());
        rc.set(CATEGORIZED_COSTS_ANALYTICS.PERIOD_COST_ANALYTICS_ID, value.periodCostsAnalyticsId());

        return rc;
    }

    @Override
    public CategorizedCostAnalyticsCreateCommand map(Record rc) {
        return new CategorizedCostAnalyticsCreateCommand(
            rc.get(CATEGORIZED_COSTS_ANALYTICS.CATEGORY_ID),
            rc.get(CATEGORIZED_COSTS_ANALYTICS.PERIOD_COST_ANALYTICS_ID)
        );
    }
}
