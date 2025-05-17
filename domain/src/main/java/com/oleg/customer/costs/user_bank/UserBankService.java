package com.oleg.customer.costs.user_bank;

import com.oleg.customer.costs.bank.Bank;
import com.oleg.customer.costs.costs.source.CustomerCostsLoader;
import com.oleg.customer.costs.exception.NotFoundException;
import com.oleg.customer.costs.user_management.UserContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserBankService {

    private final UserContext userContext;
    private final UserBankSource userBankSource;
    private final CustomerCostsLoader customerCostsLoader;

    public UserBankService(UserContext userContext,
                           UserBankSource userBankSource,
                           CustomerCostsLoader customerCostsLoader) {
        this.userContext = userContext;
        this.userBankSource = userBankSource;
        this.customerCostsLoader = customerCostsLoader;
    }

    public List<Bank> getUserBanks() {
        int userId = userContext.id();
        List<Bank> userBanks = userBankSource.getUserBanks(userId);
        if (userBanks.isEmpty()) {
            throw new NotFoundException("You don't have any banks yet!");
        }

        return userBanks;
    }

    public void addUserBank(int bankId) {
        int userId = userContext.id();
        AddBankStatus status = userBankSource.addUserBank(userId, bankId);
        if (!status.isSuccess()) {
            throw new IllegalStateException(status.getDescription());
        }

        customerCostsLoader.loadCustomerCosts(userId, bankId);
    }

    public void deleteUserBank(int bankId) {
        int userId = userContext.id();
        userBankSource.deleteUserBank(userId, bankId);
    }
}