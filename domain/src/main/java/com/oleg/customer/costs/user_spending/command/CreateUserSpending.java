package com.oleg.customer.costs.user_spending.command;

import java.math.BigDecimal;

public record CreateUserSpending(
    int userId,
    int bankId,
    BigDecimal maxAmount,
    BigDecimal currentAmount) {
}
