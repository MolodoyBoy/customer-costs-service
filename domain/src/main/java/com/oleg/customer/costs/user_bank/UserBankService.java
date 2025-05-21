package com.oleg.customer.costs.user_bank;

import com.oleg.customer.costs.bank.Bank;
import com.oleg.customer.costs.costs.source.BankLoader;
import com.oleg.customer.costs.exception.NotFoundException;
import com.oleg.customer.costs.user_management.UserContext;
import com.oleg.customer.costs.user_management.UserTokenSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserBankService {

    private final UserContext userContext;
    private final UserBankSource userBankSource;
    private final BankLoader bankLoader;
    private final UserTokenSource userTokenSource;

    public UserBankService(UserContext userContext,
                           UserBankSource userBankSource,
                           BankLoader bankLoader,
                           UserTokenSource userTokenSource) {
        this.userContext = userContext;
        this.userBankSource = userBankSource;
        this.bankLoader = bankLoader;
        this.userTokenSource = userTokenSource;
    }

    public List<Bank> getUserBanks() {
        int userId = userContext.id();
        List<Bank> userBanks = userBankSource.getUserBanks(userId);
        if (userBanks.isEmpty()) {
            throw new NotFoundException("You don't have any banks yet!");
        }

        return userBanks;
    }

    @Transactional
    public void addUserBank(int bankId, String token) {
        int userId = userContext.id();
        AddBankStatus status = userBankSource.addUserBank(userId, bankId);
        if (!status.isSuccess()) {
            throw new IllegalArgumentException(status.getDescription());
        }

        userTokenSource.addToken(userId, bankId, token);
        bankLoader.loadBank(userId, bankId);
    }

    public void deleteUserBank(int bankId) {
        int userId = userContext.id();
        userBankSource.deleteUserBank(userId, bankId);
    }
}