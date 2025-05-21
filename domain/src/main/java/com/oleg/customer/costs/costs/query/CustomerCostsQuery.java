package com.oleg.customer.costs.costs.query;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CustomerCostsQuery(
    int id,
    BigDecimal amount,
    String description,
    LocalDateTime createdAt,
    String categoryDescription
) {
}
