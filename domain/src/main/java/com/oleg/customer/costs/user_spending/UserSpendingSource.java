package com.oleg.customer.costs.user_spending;

import com.oleg.customer.costs.user_spending.command.CreateUserSpending;
import com.oleg.customer.costs.user_spending.command.UpdateUserSpending;

import java.math.BigDecimal;

public interface UserSpendingSource {

    UserSpendingQuery getUserSpending(int userId, int bankId);

    BigDecimal getPreviousMaxSpending(int userId, int bankId);

    void updateUserMaxSpending(UpdateUserSpending updateUserSpending);

    void createUserSpending(CreateUserSpending createUserSpending);
}
