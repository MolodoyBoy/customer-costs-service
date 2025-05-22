package com.oleg.customer.costs.analytics.customer_costs.query;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CustomerCostsQuery(
    BigDecimal amount,
    String description,
    LocalDateTime createdAt
) {
}
