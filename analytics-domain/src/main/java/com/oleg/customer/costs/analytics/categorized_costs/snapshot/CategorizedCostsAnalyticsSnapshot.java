package com.oleg.customer.costs.analytics.categorized_costs.snapshot;

import java.math.BigDecimal;

public record CategorizedCostsAnalyticsSnapshot(
        int id,
        BigDecimal amount,
        BigDecimal percent,
        Integer transactionsCount,
        String categoryDescription
) {}