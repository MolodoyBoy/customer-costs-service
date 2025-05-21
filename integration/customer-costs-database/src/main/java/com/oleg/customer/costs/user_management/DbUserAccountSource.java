package com.oleg.customer.costs.user_management;

import com.oleg.customer.costs.monobank.UserAccountsSource;
import org.jooq.DSLContext;
import org.jooq.Record;
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
        var records = accountNumbers.stream()
            .map(accountNumber -> toRecord(userId, accountNumber))
            .toList();

        dslContext.insertInto(MONOBANK_USER_ACCOUNTS)
            .set(records)
            .execute();
    }

    private Record toRecord(int userId, String accountNumber) {
        Record newRecord = dslContext.newRecord(MONOBANK_USER_ACCOUNTS);
        newRecord.set(MONOBANK_USER_ACCOUNTS.USER_ID, userId);
        newRecord.set(MONOBANK_USER_ACCOUNTS.ACCOUNT_ID, accountNumber);

        return newRecord;
    }

    @Override
    public Integer getUserIdByAccountNumber(String accountNumber) {
        return dslContext.select(MONOBANK_USER_ACCOUNTS.USER_ID)
            .from(MONOBANK_USER_ACCOUNTS)
            .where(MONOBANK_USER_ACCOUNTS.ACCOUNT_ID.eq(accountNumber))
            .fetchOne(MONOBANK_USER_ACCOUNTS.USER_ID);
    }
}
