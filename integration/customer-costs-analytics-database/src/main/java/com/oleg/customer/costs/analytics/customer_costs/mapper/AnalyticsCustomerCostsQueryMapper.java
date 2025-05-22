package com.oleg.customer.costs.analytics.customer_costs.mapper;

import com.oleg.customer.costs.analytics.customer_costs.query.CustomerCostsQuery;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import static com.oleg.fund.customer.costs.analytics.tables.CustomerCosts.CUSTOMER_COSTS;

@Component
public class AnalyticsCustomerCostsQueryMapper implements RecordMapper<Record, CustomerCostsQuery> {

    @Override
    public CustomerCostsQuery map(Record rc) {
        return new CustomerCostsQuery(
            rc.get(CUSTOMER_COSTS.AMOUNT),
            rc.get(CUSTOMER_COSTS.DESCRIPTION),
            rc.get(CUSTOMER_COSTS.CREATED_AT)
        );
    }
}