package com.oleg.customer.costs.bank;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.oleg.fund.customer.costs.analytics.tables.Bank.BANK;

@Repository
public class DbBankSource implements BankSource {

    private final DSLContext dslContext;
    private final BankRecordMapper bankRecordMapper;

    public DbBankSource(DSLContext dslContext,
                        BankRecordMapper bankRecordMapper) {
        this.dslContext = dslContext;
        this.bankRecordMapper = bankRecordMapper;
    }

    @Override
    public List<Bank> getSupportedBanks() {
        return dslContext.select()
            .from(BANK)
            .fetch(bankRecordMapper);
    }
}