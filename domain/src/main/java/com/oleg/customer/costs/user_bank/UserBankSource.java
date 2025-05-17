package com.oleg.customer.costs.user_bank;

import com.oleg.customer.costs.bank.Bank;

import java.util.List;

public interface UserBankSource {

    List<Bank> getUserBanks(int userId);

    void deleteUserBank(int userId, int bankId);

    AddBankStatus addUserBank(int userId, int bankId);
}
