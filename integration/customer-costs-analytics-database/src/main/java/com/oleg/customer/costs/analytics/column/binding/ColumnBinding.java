package com.oleg.customer.costs.analytics.column.binding;

import com.oleg.customer.costs.analytics.common.column.Column;
import org.jooq.Field;

import java.util.Collection;
import java.util.List;

public interface ColumnBinding {

    Class<?> entity();

    Field<?> getField(Column column);

    List<Field<?>> getFields(Collection<? extends Column> columns);
}