package com.oleg.customer.costs.analytics.customer_costs;

import org.jooq.Field;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.oleg.fund.customer.costs.analytics.tables.CustomerCosts.CUSTOMER_COSTS;

public class CustomerCostsFields {

    private CustomerCostsFields() {}

    private static final Field<BigDecimal> SUM = DSL.sum(CUSTOMER_COSTS.AMOUNT);
    private static final Field<LocalDate> CREATED_AT = CUSTOMER_COSTS.CREATED_AT.cast(SQLDataType.LOCALDATE);

    public static Field<BigDecimal> sum() {
        return SUM;
    }

    public static Field<LocalDate> createdAt() {
        return CREATED_AT;
    }
}
