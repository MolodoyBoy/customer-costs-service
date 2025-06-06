/*
 * This file is generated by jOOQ.
 */
package com.oleg.fund.customer.costs.analytics;


import com.oleg.fund.customer.costs.analytics.tables.Bank;
import com.oleg.fund.customer.costs.analytics.tables.CategorizedCostsAnalytics;
import com.oleg.fund.customer.costs.analytics.tables.CostsCategory;
import com.oleg.fund.customer.costs.analytics.tables.CustomerCosts;
import com.oleg.fund.customer.costs.analytics.tables.CustomerCostsByCategory;
import com.oleg.fund.customer.costs.analytics.tables.CustomerCostsByPeriod;
import com.oleg.fund.customer.costs.analytics.tables.CustomerCostsEvents;
import com.oleg.fund.customer.costs.analytics.tables.MonobankUserAccounts;
import com.oleg.fund.customer.costs.analytics.tables.PeriodCostsAnalytics;
import com.oleg.fund.customer.costs.analytics.tables.UserBank;
import com.oleg.fund.customer.costs.analytics.tables.UserDetails;
import com.oleg.fund.customer.costs.analytics.tables.UserSpending;
import com.oleg.fund.customer.costs.analytics.tables.UserTokens;

import java.util.Arrays;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Public extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public</code>
     */
    public static final Public PUBLIC = new Public();

    /**
     * The table <code>public.bank</code>.
     */
    public final Bank BANK = Bank.BANK;

    /**
     * The table <code>public.categorized_costs_analytics</code>.
     */
    public final CategorizedCostsAnalytics CATEGORIZED_COSTS_ANALYTICS = CategorizedCostsAnalytics.CATEGORIZED_COSTS_ANALYTICS;

    /**
     * The table <code>public.costs_category</code>.
     */
    public final CostsCategory COSTS_CATEGORY = CostsCategory.COSTS_CATEGORY;

    /**
     * The table <code>public.customer_costs</code>.
     */
    public final CustomerCosts CUSTOMER_COSTS = CustomerCosts.CUSTOMER_COSTS;

    /**
     * The table <code>public.customer_costs_by_category</code>.
     */
    public final CustomerCostsByCategory CUSTOMER_COSTS_BY_CATEGORY = CustomerCostsByCategory.CUSTOMER_COSTS_BY_CATEGORY;

    /**
     * The table <code>public.customer_costs_by_period</code>.
     */
    public final CustomerCostsByPeriod CUSTOMER_COSTS_BY_PERIOD = CustomerCostsByPeriod.CUSTOMER_COSTS_BY_PERIOD;

    /**
     * The table <code>public.customer_costs_events</code>.
     */
    public final CustomerCostsEvents CUSTOMER_COSTS_EVENTS = CustomerCostsEvents.CUSTOMER_COSTS_EVENTS;

    /**
     * The table <code>public.monobank_user_accounts</code>.
     */
    public final MonobankUserAccounts MONOBANK_USER_ACCOUNTS = MonobankUserAccounts.MONOBANK_USER_ACCOUNTS;

    /**
     * The table <code>public.period_costs_analytics</code>.
     */
    public final PeriodCostsAnalytics PERIOD_COSTS_ANALYTICS = PeriodCostsAnalytics.PERIOD_COSTS_ANALYTICS;

    /**
     * The table <code>public.user_bank</code>.
     */
    public final UserBank USER_BANK = UserBank.USER_BANK;

    /**
     * The table <code>public.user_details</code>.
     */
    public final UserDetails USER_DETAILS = UserDetails.USER_DETAILS;

    /**
     * The table <code>public.user_spending</code>.
     */
    public final UserSpending USER_SPENDING = UserSpending.USER_SPENDING;

    /**
     * The table <code>public.user_tokens</code>.
     */
    public final UserTokens USER_TOKENS = UserTokens.USER_TOKENS;

    /**
     * No further instances allowed
     */
    private Public() {
        super("public", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.asList(
            Bank.BANK,
            CategorizedCostsAnalytics.CATEGORIZED_COSTS_ANALYTICS,
            CostsCategory.COSTS_CATEGORY,
            CustomerCosts.CUSTOMER_COSTS,
            CustomerCostsByCategory.CUSTOMER_COSTS_BY_CATEGORY,
            CustomerCostsByPeriod.CUSTOMER_COSTS_BY_PERIOD,
            CustomerCostsEvents.CUSTOMER_COSTS_EVENTS,
            MonobankUserAccounts.MONOBANK_USER_ACCOUNTS,
            PeriodCostsAnalytics.PERIOD_COSTS_ANALYTICS,
            UserBank.USER_BANK,
            UserDetails.USER_DETAILS,
            UserSpending.USER_SPENDING,
            UserTokens.USER_TOKENS
        );
    }
}
