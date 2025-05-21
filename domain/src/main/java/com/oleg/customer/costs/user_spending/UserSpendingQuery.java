package com.oleg.customer.costs.user_spending;

import java.math.BigDecimal;

public record UserSpendingQuery(BigDecimal maxAmount,
                                BigDecimal currentAmount) {
}
