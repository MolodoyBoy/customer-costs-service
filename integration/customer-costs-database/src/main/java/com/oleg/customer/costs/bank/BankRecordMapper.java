package com.oleg.customer.costs.bank;

import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import static com.oleg.fund.customer.costs.analytics.tables.Bank.BANK;

@Component
public class BankRecordMapper implements RecordMapper<Record, Bank> {

    @Override
    public Bank map(Record rc) {
        return new Bank(rc.get(BANK.ID), rc.get(BANK.DESCRIPTION));
    }
}