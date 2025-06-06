/*
 * This file is generated by jOOQ.
 */
package com.oleg.fund.customer.costs.analytics.tables;


import com.oleg.fund.customer.costs.analytics.Public;

import java.math.BigDecimal;
import java.util.Collection;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.SQL;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Stringly;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class PeriodCostsAnalytics extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.period_costs_analytics</code>
     */
    public static final PeriodCostsAnalytics PERIOD_COSTS_ANALYTICS = new PeriodCostsAnalytics();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>public.period_costs_analytics.id</code>.
     */
    public final TableField<Record, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.period_costs_analytics.amount</code>.
     */
    public final TableField<Record, BigDecimal> AMOUNT = createField(DSL.name("amount"), SQLDataType.NUMERIC, this, "");

    /**
     * The column <code>public.period_costs_analytics.period</code>.
     */
    public final TableField<Record, Integer> PERIOD = createField(DSL.name("period"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.period_costs_analytics.user_id</code>.
     */
    public final TableField<Record, Integer> USER_ID = createField(DSL.name("user_id"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column
     * <code>public.period_costs_analytics.difference_from_previous_month</code>.
     */
    public final TableField<Record, BigDecimal> DIFFERENCE_FROM_PREVIOUS_MONTH = createField(DSL.name("difference_from_previous_month"), SQLDataType.NUMERIC, this, "");

    /**
     * The column <code>public.period_costs_analytics.average</code>.
     */
    public final TableField<Record, BigDecimal> AVERAGE = createField(DSL.name("average"), SQLDataType.NUMERIC, this, "");

    /**
     * The column <code>public.period_costs_analytics.total_transactions</code>.
     */
    public final TableField<Record, Integer> TOTAL_TRANSACTIONS = createField(DSL.name("total_transactions"), SQLDataType.INTEGER, this, "");

    private PeriodCostsAnalytics(Name alias, Table<Record> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private PeriodCostsAnalytics(Name alias, Table<Record> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>public.period_costs_analytics</code> table
     * reference
     */
    public PeriodCostsAnalytics(String alias) {
        this(DSL.name(alias), PERIOD_COSTS_ANALYTICS);
    }

    /**
     * Create an aliased <code>public.period_costs_analytics</code> table
     * reference
     */
    public PeriodCostsAnalytics(Name alias) {
        this(alias, PERIOD_COSTS_ANALYTICS);
    }

    /**
     * Create a <code>public.period_costs_analytics</code> table reference
     */
    public PeriodCostsAnalytics() {
        this(DSL.name("period_costs_analytics"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<Record, Integer> getIdentity() {
        return (Identity<Record, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<Record> getPrimaryKey() {
        return Internal.createUniqueKey(PeriodCostsAnalytics.PERIOD_COSTS_ANALYTICS, DSL.name("period_costs_analytics_pkey"), new TableField[] { PeriodCostsAnalytics.PERIOD_COSTS_ANALYTICS.ID }, true);
    }

    @Override
    public PeriodCostsAnalytics as(String alias) {
        return new PeriodCostsAnalytics(DSL.name(alias), this);
    }

    @Override
    public PeriodCostsAnalytics as(Name alias) {
        return new PeriodCostsAnalytics(alias, this);
    }

    @Override
    public PeriodCostsAnalytics as(Table<?> alias) {
        return new PeriodCostsAnalytics(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public PeriodCostsAnalytics rename(String name) {
        return new PeriodCostsAnalytics(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public PeriodCostsAnalytics rename(Name name) {
        return new PeriodCostsAnalytics(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public PeriodCostsAnalytics rename(Table<?> name) {
        return new PeriodCostsAnalytics(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public PeriodCostsAnalytics where(Condition condition) {
        return new PeriodCostsAnalytics(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public PeriodCostsAnalytics where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public PeriodCostsAnalytics where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public PeriodCostsAnalytics where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public PeriodCostsAnalytics where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public PeriodCostsAnalytics where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public PeriodCostsAnalytics where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public PeriodCostsAnalytics where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public PeriodCostsAnalytics whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public PeriodCostsAnalytics whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
