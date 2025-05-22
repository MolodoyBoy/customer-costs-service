package com.oleg.customer.costs.analytics.common.condition;

import org.jooq.Condition;

import java.util.Collection;

import static org.jooq.impl.DSL.noCondition;

public interface ConditionBuilder<V> {

    Condition buildCondition(V value);

    default Condition buildCondition(Collection<V> values) {
        return values.stream()
                .map(this::buildCondition)
                .reduce(noCondition(), Condition::or);
    }
}