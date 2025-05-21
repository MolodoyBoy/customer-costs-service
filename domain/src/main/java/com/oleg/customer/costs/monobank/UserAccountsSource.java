package com.oleg.customer.costs.monobank;

import java.util.List;

public interface UserAccountsSource {

    Integer getUserIdByAccountNumber(String accountNumber);

    void addAccount(int userId, List<String> accountNumber);
}
