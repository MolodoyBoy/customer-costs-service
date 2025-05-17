package com.oleg.customer.costs.user_bank;

import com.oleg.customer.costs.bank.Bank;
import com.oleg.customer.costs.bank.BankRecordMapper;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.postgresql.util.PSQLException;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.oleg.customer.costs.user_bank.AddBankStatus.*;
import static com.oleg.fund.customer.costs.analytics.tables.Bank.BANK;
import static com.oleg.fund.customer.costs.analytics.tables.UserBank.USER_BANK;

@Repository
public class DbUserBankSource implements UserBankSource {

    private static final String FOREIGN_KEY_VIOLATION = "23503";

    private final DSLContext dslContext;
    private final BankRecordMapper bankRecordMapper;

    public DbUserBankSource(DSLContext dslContext, BankRecordMapper bankRecordMapper) {
        this.dslContext = dslContext;
        this.bankRecordMapper = bankRecordMapper;
    }

    @Override
    public List<Bank> getUserBanks(int userId) {
        return dslContext.select(BANK.ID, BANK.DESCRIPTION)
            .from(USER_BANK)
            .innerJoin(BANK).on(BANK.ID.eq(USER_BANK.BANK_ID))
            .where(USER_BANK.USER_ID.eq(userId))
            .fetch(bankRecordMapper);
    }

    @Override
    public void deleteUserBank(int userId, int bankId) {
        dslContext.deleteFrom(USER_BANK)
            .where(USER_BANK.USER_ID.eq(userId))
            .and(USER_BANK.BANK_ID.eq(bankId))
            .execute();
    }

    @Override
    public AddBankStatus addUserBank(int userId, int bankId) {
        try {
            int execute = dslContext.insertInto(USER_BANK)
                .set(USER_BANK.USER_ID, userId)
                .set(USER_BANK.BANK_ID, bankId)
                .onConflict(USER_BANK.USER_ID, USER_BANK.BANK_ID)
                .doNothing()
                .execute();

            if (execute == 1) {
                return SUCCESS;
            } else {
                return BANK_ALREADY_ADDED;
            }
        } catch (DataAccessException e) {
            AddBankStatus notExistsStatus = getNotExistsStatus(e.getCause());
            if (notExistsStatus != null) {
                return notExistsStatus;
            }

            throw e;
        }
    }

    private AddBankStatus getNotExistsStatus(Throwable cause) {
        if (cause instanceof PSQLException psql) {
            if (FOREIGN_KEY_VIOLATION.equals(psql.getSQLState())) {
                return BANK_NOT_EXISTS;
            }
        }

        return null;
    }
}