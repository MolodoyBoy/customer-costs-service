package com.oleg.customer.costs.analytics.common.mapper;

import org.jooq.Field;
import org.jooq.Record;

public class UtilMapper {

    public static <T> T getNullableField(Record rc, Field<T> field) {
        if (rc.field(field) == null) return null;
        return rc.get(field);
    }
}