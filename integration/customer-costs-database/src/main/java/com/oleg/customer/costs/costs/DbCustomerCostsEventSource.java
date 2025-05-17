package com.oleg.customer.costs.costs;

import com.oleg.customer.costs.source.CustomerCostsEventSource;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static com.oleg.fund.customer.costs.analytics.tables.CustomerCostsEvents.CUSTOMER_COSTS_EVENTS;

@Repository
public class DbCustomerCostsEventSource implements CustomerCostsEventSource {

    private final DSLContext dslContext;

    public DbCustomerCostsEventSource(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public void delete(Collection<Long> ids) {
        dslContext.deleteFrom(CUSTOMER_COSTS_EVENTS)
            .where(CUSTOMER_COSTS_EVENTS.ID.in(ids))
            .execute();
    }

    @Override
    public void insert(Collection<Integer> customerCostsIds) {
        var records = customerCostsIds.stream()
            .map(this::toRecord)
            .toList();

        dslContext.insertInto(CUSTOMER_COSTS_EVENTS)
            .set(records)
            .execute();
    }

    private Record toRecord(Integer customerCostsId) {
        Record newRecord = dslContext.newRecord(CUSTOMER_COSTS_EVENTS);
        newRecord.set(CUSTOMER_COSTS_EVENTS.CUSTOMER_COSTS_ID, customerCostsId);

        return newRecord;
    }

    @Override
    @Transactional
    public Map<Long, Integer> getEvents(int limit) {
        Map<Long, Integer> events = dslContext.select()
            .from(CUSTOMER_COSTS_EVENTS)
            .where(CUSTOMER_COSTS_EVENTS.PROCESSED.eq(false))
            .orderBy(CUSTOMER_COSTS_EVENTS.ID)
            .limit(limit)
            .forUpdate()
            .skipLocked()
            .fetchMap(CUSTOMER_COSTS_EVENTS.ID, rc -> rc.get(CUSTOMER_COSTS_EVENTS.CUSTOMER_COSTS_ID));

        markFundBenchmarkEventProcessed(events.keySet());

        return events;
    }

    private void markFundBenchmarkEventProcessed(Set<Long> ids) {
        dslContext.update(CUSTOMER_COSTS_EVENTS)
            .set(CUSTOMER_COSTS_EVENTS.PROCESSED, true)
            .where(CUSTOMER_COSTS_EVENTS.ID.in(ids))
            .execute();
    }
}
