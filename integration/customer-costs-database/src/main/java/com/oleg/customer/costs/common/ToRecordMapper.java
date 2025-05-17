package com.oleg.customer.costs.common;

import org.jooq.Record;

import java.util.Collection;
import java.util.List;

public interface ToRecordMapper<V> {

    Record toRecord(V value);

    default List<Record> toRecords(Collection<V> values) {
        return values.stream()
            .map(this::toRecord)
            .toList();
    }
}