package com.oleg.customer.costs.costs.mapper;

import com.oleg.customer.costs.costs.query.CustomerCostsQuery;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import static com.oleg.fund.customer.costs.analytics.tables.CostsCategory.COSTS_CATEGORY;
import static com.oleg.fund.customer.costs.analytics.tables.CustomerCosts.CUSTOMER_COSTS;

@Component
public class CustomerCostsQueryMapper implements RecordMapper<Record, CustomerCostsQuery> {

    @Override
    public CustomerCostsQuery map(Record rc) {
        return new CustomerCostsQuery(
            rc.get(CUSTOMER_COSTS.ID),
            rc.get(CUSTOMER_COSTS.AMOUNT),
            rc.get(CUSTOMER_COSTS.DESCRIPTION),
            rc.get(CUSTOMER_COSTS.CREATED_AT),
            rc.get(COSTS_CATEGORY.DESCRIPTION)
        );
    }
}
