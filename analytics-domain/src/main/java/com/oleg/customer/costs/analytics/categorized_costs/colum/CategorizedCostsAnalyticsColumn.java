package com.oleg.customer.costs.analytics.categorized_costs.colum;

import com.oleg.customer.costs.analytics.categorized_costs.entity.CategorizedCostsAnalytics;
import com.oleg.customer.costs.analytics.common.column.Column;

public enum CategorizedCostsAnalyticsColumn implements Column {

    ID,
    AMOUNT,
    PERCENT,
    TRANSACTIONS_COUNT,
    CATEGORY_DESCRIPTION;

    @Override
    public Class<?> entity() {
        return CategorizedCostsAnalytics.class;
    }
}