package com.oleg.customer.costs.costs.query;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateCustomerCostsCmd(
    String id,
    int bankId,
    String accountId,
    BigDecimal amount,
    String description,
    LocalDateTime createdAt,
    BigDecimal commissionRate
) {}