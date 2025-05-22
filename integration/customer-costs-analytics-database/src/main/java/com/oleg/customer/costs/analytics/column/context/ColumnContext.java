package com.oleg.customer.costs.analytics.column.context;

import com.oleg.customer.costs.analytics.common.column.Column;
import org.jooq.Field;

import java.util.Collection;
import java.util.List;

public interface ColumnContext {

    List<Field<?>> getFields(Collection<? extends Column> columns);
}
