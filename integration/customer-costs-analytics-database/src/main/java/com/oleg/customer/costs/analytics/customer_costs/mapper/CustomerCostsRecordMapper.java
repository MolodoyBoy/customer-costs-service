package com.oleg.customer.costs.analytics.customer_costs.mapper;

import com.oleg.customer.costs.analytics.customer_costs.entity.CustomerCosts;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import static com.oleg.fund.customer.costs.analytics.tables.CustomerCosts.CUSTOMER_COSTS;

@Component
public class CustomerCostsRecordMapper implements RecordMapper<Record, CustomerCosts> {

    @Override
    public CustomerCosts map(Record rc) {
        return new CustomerCosts(
            rc.get(CUSTOMER_COSTS.ID),
            rc.get(CUSTOMER_COSTS.USER_ID),
            rc.get(CUSTOMER_COSTS.CATEGORY_ID),
            rc.get(CUSTOMER_COSTS.AMOUNT),
            rc.get(CUSTOMER_COSTS.DESCRIPTION),
            rc.get(CUSTOMER_COSTS.CREATED_AT)
        );
    }
}