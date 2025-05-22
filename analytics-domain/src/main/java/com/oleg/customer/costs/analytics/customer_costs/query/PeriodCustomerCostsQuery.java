package com.oleg.customer.costs.analytics.customer_costs.query;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PeriodCustomerCostsQuery(
    BigDecimal amount,
    LocalDate createdAt
) {
}
