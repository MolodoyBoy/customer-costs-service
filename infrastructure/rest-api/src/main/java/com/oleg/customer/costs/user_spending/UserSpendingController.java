package com.oleg.customer.costs.user_spending;

import com.oleg.customer.costs.api.UserSpendingApi;
import com.oleg.customer.costs.model.UpdateSpendingDto;
import com.oleg.customer.costs.model.UserSpendingDto;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@CrossOrigin(
    origins = "http://localhost:3000",
    exposedHeaders = "Authorization"
)
public class UserSpendingController implements UserSpendingApi {

    private final UserSpendingService userSpendingService;

    public UserSpendingController(UserSpendingService userSpendingService) {
        this.userSpendingService = userSpendingService;
    }

    @Override
    public UserSpendingDto getUserSpending(Integer bankId) {
        UserSpendingQuery userSpending = userSpendingService.getUserSpending(bankId);
        return new UserSpendingDto()
            .maxAmount(convert(userSpending.maxAmount()))
            .currentAmount(convert(userSpending.currentAmount()));
    }

    @Override
    public void updateUserSpending(Integer bankId, UpdateSpendingDto updateSpendingDto) {
        Double amount = updateSpendingDto.getMaxAmount();
        BigDecimal maxAmount = amount == null ? null : BigDecimal.valueOf(amount);

        userSpendingService.updateUserSpending(bankId, maxAmount);
    }

    private Double convert(BigDecimal amount) {
        if (amount == null) return null;
        return amount.doubleValue();
    }
}