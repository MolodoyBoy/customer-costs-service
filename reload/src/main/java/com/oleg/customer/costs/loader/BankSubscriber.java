package com.oleg.customer.costs.loader;

public interface BankSubscriber {

    int bankId();

    void subscribe(int userId);
}
