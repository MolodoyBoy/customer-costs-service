package com.oleg.customer.costs.user_spending;

import com.oleg.customer.costs.exception.NotFoundException;
import com.oleg.customer.costs.user_management.UserContext;
import com.oleg.customer.costs.user_spending.command.UpdateUserSpending;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class UserSpendingService {

    private final UserContext userContext;
    private final UserSpendingSource userSpendingSource;

    public UserSpendingService(UserContext userContext,
                               UserSpendingSource userSpendingSource) {
        this.userContext = userContext;
        this.userSpendingSource = userSpendingSource;
    }

    public UserSpendingQuery getUserSpending(int bankId) {
        int userId = userContext.id();
        UserSpendingQuery userSpending = userSpendingSource.getUserSpending(userId, bankId);
        if (userSpending == null) {
            throw new NotFoundException("There is no spending");
        }

        return userSpending;
    }

    public void updateUserSpending(int bankId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be more than zero!");
        }

        int userId = userContext.id();
        userSpendingSource.updateUserMaxSpending(new UpdateUserSpending(userId, bankId, amount));
    }
}