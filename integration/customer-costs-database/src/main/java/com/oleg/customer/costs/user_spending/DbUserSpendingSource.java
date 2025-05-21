package com.oleg.customer.costs.user_spending;

import com.oleg.customer.costs.user_spending.command.CreateUserSpending;
import com.oleg.customer.costs.user_spending.command.UpdateUserSpending;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.YearMonth;

import static com.oleg.fund.customer.costs.analytics.tables.UserSpending.USER_SPENDING;

@Repository
public class DbUserSpendingSource implements UserSpendingSource {

    private final DSLContext dslContext;

    public DbUserSpendingSource(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public UserSpendingQuery getUserSpending(int userId, int bankId) {
        return dslContext.select(USER_SPENDING.CURRENT_AMOUNT, USER_SPENDING.MAX_AMOUNT)
            .from(USER_SPENDING)
            .where(USER_SPENDING.USER_ID.eq(userId))
            .and(USER_SPENDING.BANK_ID.eq(bankId))
            .and(USER_SPENDING.PERIOD.eq(nowPeriodIndex()))
            .fetch(this::map)
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public BigDecimal getPreviousMaxSpending(int userId, int bankId) {
        return dslContext.select(USER_SPENDING.MAX_AMOUNT)
            .from(USER_SPENDING)
            .where(USER_SPENDING.USER_ID.eq(userId))
            .and(USER_SPENDING.BANK_ID.eq(bankId))
            .and(USER_SPENDING.PERIOD.eq(previousPeriodIndex()))
            .fetch(USER_SPENDING.MAX_AMOUNT)
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public void updateUserMaxSpending(UpdateUserSpending updateUserSpending) {
        dslContext.update(USER_SPENDING)
            .set(USER_SPENDING.MAX_AMOUNT, updateUserSpending.maxAmount())
            .where(USER_SPENDING.USER_ID.eq(updateUserSpending.userId()))
            .and(USER_SPENDING.BANK_ID.eq(updateUserSpending.bankId()))
            .and(USER_SPENDING.PERIOD.eq(nowPeriodIndex()))
            .execute();
    }

    private UserSpendingQuery map(Record rc) {
        return new UserSpendingQuery(
            rc.get(USER_SPENDING.MAX_AMOUNT),
            rc.get(USER_SPENDING.CURRENT_AMOUNT)
        );
    }

    @Override
    public void createUserSpending(CreateUserSpending createUserSpending) {
        dslContext.insertInto(USER_SPENDING)
            .set(toRecord(createUserSpending))
            .onConflict(USER_SPENDING.USER_ID, USER_SPENDING.BANK_ID, USER_SPENDING.PERIOD)
            .doUpdate()
            .set(USER_SPENDING.CURRENT_AMOUNT, USER_SPENDING.CURRENT_AMOUNT.plus(createUserSpending.currentAmount()))
            .execute();
    }

    private Record toRecord(CreateUserSpending createUserSpending) {
        Record rc = dslContext.newRecord(USER_SPENDING);

        rc.set(USER_SPENDING.PERIOD, nowPeriodIndex());
        rc.set(USER_SPENDING.USER_ID, createUserSpending.userId());
        rc.set(USER_SPENDING.BANK_ID, createUserSpending.bankId());
        rc.set(USER_SPENDING.MAX_AMOUNT, createUserSpending.maxAmount());
        rc.set(USER_SPENDING.CURRENT_AMOUNT, createUserSpending.currentAmount());

        return rc;
    }

    private int nowPeriodIndex() {
        return Period.of(YearMonth.now()).periodIndex();
    }

    private int previousPeriodIndex() {
        return Period.of(YearMonth.now().minusMonths(1)).periodIndex();
    }
}