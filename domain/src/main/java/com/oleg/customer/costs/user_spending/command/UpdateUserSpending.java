package com.oleg.customer.costs.user_spending.command;

import java.math.BigDecimal;

public record UpdateUserSpending(int userId, int bankId, BigDecimal maxAmount) {
}
