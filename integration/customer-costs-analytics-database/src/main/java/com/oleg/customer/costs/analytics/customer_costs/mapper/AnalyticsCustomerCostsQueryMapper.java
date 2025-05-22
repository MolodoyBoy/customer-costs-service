package com.oleg.customer.costs.analytics.customer_costs.mapper;

import com.oleg.customer.costs.analytics.customer_costs.query.PeriodCustomerCostsQuery;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import static com.oleg.customer.costs.analytics.customer_costs.CustomerCostsFields.*;

@Component
public class AnalyticsCustomerCostsQueryMapper implements RecordMapper<Record, PeriodCustomerCostsQuery> {

    @Override
    public PeriodCustomerCostsQuery map(Record rc) {
        return new PeriodCustomerCostsQuery(
            rc.get(sum()).abs(),
            rc.get(createdAt())
        );
    }
}