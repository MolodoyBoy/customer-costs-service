package com.oleg.customer.costs.analytics.categorized_costs_analytics.column_binding;

import com.oleg.customer.costs.analytics.categorized_costs.colum.CategorizedCostsAnalyticsColumn;
import com.oleg.customer.costs.analytics.column.binding.ColumnBinding;
import com.oleg.customer.costs.analytics.common.column.Column;
import org.jooq.Field;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.oleg.customer.costs.analytics.categorized_costs.colum.CategorizedCostsAnalyticsColumn.*;
import static com.oleg.fund.customer.costs.analytics.tables.CategorizedCostsAnalytics.CATEGORIZED_COSTS_ANALYTICS;
import static com.oleg.fund.customer.costs.analytics.tables.CostsCategory.COSTS_CATEGORY;

@Component
class CategorizedCostsAnalyticsColumnBinding implements ColumnBinding {

    private final Class<?> entity;
    private final Map<Column, Field<?>> fieldBindings;

    CategorizedCostsAnalyticsColumnBinding() {
        fieldBindings = new HashMap<>();

        fieldBindings.put(ID, CATEGORIZED_COSTS_ANALYTICS.ID);
        fieldBindings.put(AMOUNT, CATEGORIZED_COSTS_ANALYTICS.AMOUNT);
        fieldBindings.put(PERCENT, CATEGORIZED_COSTS_ANALYTICS.PERCENT);
        fieldBindings.put(CATEGORY_DESCRIPTION, COSTS_CATEGORY.DESCRIPTION);
        fieldBindings.put(TRANSACTIONS_COUNT, CATEGORIZED_COSTS_ANALYTICS.TRANSACTIONS_COUNT);

        validate();

        this.entity = fieldBindings.keySet()
            .stream()
            .map(Column::entity)
            .distinct()
            .reduce((c1, c2) -> {
                if (!c1.equals(c2)) {
                    throw new IllegalArgumentException(
                        String.format("Columns not belong to same entity! %s %s", c1.getSimpleName(), c2.getSimpleName())
                    );
                } else {
                    return c1;
                }
            })
            .orElseThrow();
    }

    private void validate() {
        for (CategorizedCostsAnalyticsColumn column : values()) {
            if (!fieldBindings.containsKey(column)) {
                throw new IllegalArgumentException(String.format("Db column not set for column : %s", column.name()));
            }
        }
    }

    @Override
    public Class<?> entity() {
        return entity;
    }

    @Override
    public Field<?> getField(Column column) {
        return fieldBindings.get(column);
    }

    @Override
    public List<Field<?>> getFields(Collection<? extends Column> columns) {
        List<Field<?>> fields = new ArrayList<>();

        columns.stream()
            .map(this::getField)
            .forEach(fields::add);

        return fields;
    }
}