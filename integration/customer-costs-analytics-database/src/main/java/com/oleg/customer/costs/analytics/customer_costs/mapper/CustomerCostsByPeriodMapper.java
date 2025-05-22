package com.oleg.customer.costs.analytics.customer_costs.mapper;

import com.oleg.customer.costs.analytics.common.mapper.ToRecordMapper;
import com.oleg.customer.costs.analytics.customer_costs.entity.CustomerCosts;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Component;

import static com.oleg.fund.customer.costs.analytics.tables.CustomerCostsByPeriod.CUSTOMER_COSTS_BY_PERIOD;

@Component
public class CustomerCostsByPeriodMapper implements ToRecordMapper<CustomerCosts> {

    private final DSLContext dslContext;

    public CustomerCostsByPeriodMapper(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public Record toRecord(CustomerCosts value) {
        Record rc = dslContext.newRecord(CUSTOMER_COSTS_BY_PERIOD);
        rc.set(CUSTOMER_COSTS_BY_PERIOD.CUSTOMER_COSTS_ID, value.id());
        rc.set(CUSTOMER_COSTS_BY_PERIOD.PERIOD_COSTS_ANALYTICS_ID, value.periodCostsAnalyticsId());

        return rc;
    }
}
