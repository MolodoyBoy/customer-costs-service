package com.oleg.customer.costs.analytics.categorized_costs.snapshot;

import java.math.BigDecimal;

public record CategorizedCostsAnalyticsSnapshot(
        int id,
        int categoryId,
        BigDecimal amount,
        BigDecimal percent,
        Integer transactionsCount,
        String categoryDescription
) {}