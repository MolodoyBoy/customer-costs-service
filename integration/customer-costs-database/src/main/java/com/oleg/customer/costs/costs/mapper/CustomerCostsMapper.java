package com.oleg.customer.costs.costs.mapper;

import com.oleg.customer.costs.common.ToRecordMapper;
import com.oleg.customer.costs.costs.value_object.CustomerCosts;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import static com.oleg.fund.customer.costs.analytics.tables.CustomerCosts.*;

@Component
public class CustomerCostsMapper implements RecordMapper<Record, CustomerCosts>, ToRecordMapper<CustomerCosts> {

    private final DSLContext dslContext;

    public CustomerCostsMapper(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public CustomerCosts map(Record rc) {
        return new CustomerCosts(
            rc.get(CUSTOMER_COSTS.ID),
            rc.get(CUSTOMER_COSTS.USER_ID),
            rc.get(CUSTOMER_COSTS.BANK_ID),
            rc.get(CUSTOMER_COSTS.CATEGORY_ID),
            rc.get(CUSTOMER_COSTS.AMOUNT),
            rc.get(CUSTOMER_COSTS.DESCRIPTION),
            rc.get(CUSTOMER_COSTS.CREATED_AT),
            rc.get(CUSTOMER_COSTS.COMMISSION_RATE)
        );
    }

    @Override
    public Record toRecord(CustomerCosts value) {
        Record rc = dslContext.newRecord(CUSTOMER_COSTS);
        rc.set(CUSTOMER_COSTS.AMOUNT, value.amount());
        rc.set(CUSTOMER_COSTS.USER_ID, value.userId());
        rc.set(CUSTOMER_COSTS.BANK_ID, value.bankId());
        rc.set(CUSTOMER_COSTS.CREATED_AT, value.createdAt());
        rc.set(CUSTOMER_COSTS.CATEGORY_ID, value.categoryId());
        rc.set(CUSTOMER_COSTS.DESCRIPTION, value.description());
        rc.set(CUSTOMER_COSTS.COMMISSION_RATE, value.commissionRate());

        return rc;
    }
}