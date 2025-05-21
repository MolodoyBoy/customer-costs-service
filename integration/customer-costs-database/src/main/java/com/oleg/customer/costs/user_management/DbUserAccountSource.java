package com.oleg.customer.costs.user_management;

import com.oleg.customer.costs.monobank.UserAccountsSource;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.oleg.fund.customer.costs.analytics.tables.MonobankUserAccounts.*;

@Repository
public class DbUserAccountSource implements UserAccountsSource {

    private final DSLContext dslContext;

    public DbUserAccountSource(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public void addAccount(int userId, List<String> accountNumbers) {
        var queries = accountNumbers.stream()
            .map(accountNumber ->
                dslContext.insertInto(MONOBANK_USER_ACCOUNTS)
                    .values(MONOBANK_USER_ACCOUNTS.ACCOUNT_ID, accountNumber)
                    .values(MONOBANK_USER_ACCOUNTS.USER_ID, userId)
                    .onConflict(MONOBANK_USER_ACCOUNTS.ACCOUNT_ID)
                    .doNothing()
            )
            .toList();

        dslContext.batch(queries).execute();
    }

    @Override
    public Integer getUserIdByAccountNumber(String accountNumber) {
        return dslContext.select(MONOBANK_USER_ACCOUNTS.USER_ID)
            .from(MONOBANK_USER_ACCOUNTS)
            .where(MONOBANK_USER_ACCOUNTS.ACCOUNT_ID.eq(accountNumber))
            .fetchOne(MONOBANK_USER_ACCOUNTS.USER_ID);
    }
}
