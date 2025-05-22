package com.oleg.customer.costs.analytics.customer_costs.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class CustomerCosts {

    private final int id;
    private final int userId;
    private final int categoryId;
    private final BigDecimal amount;
    private final String description;
    private final LocalDateTime createdAt;

    private Integer periodCostsAnalyticsId;
    private Integer categorizedCostsAnalyticsId;

    public CustomerCosts(int id,
                         int userId,
                         int categoryId,
                         BigDecimal amount,
                         String description,
                         LocalDateTime createdAt) {
        this.id = id;
        this.amount = amount;
        this.userId = userId;
        this.createdAt = createdAt;
        this.categoryId = categoryId;
        this.description = description;
    }

    public int id() {
        return id;
    }

    public int userId() {
        return userId;
    }

    public int categoryId() {
        return categoryId;
    }

    public BigDecimal amount() {
        return amount;
    }

    public String description() {
        return description;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public Integer periodCostsAnalyticsId() {
        return periodCostsAnalyticsId;
    }

    public Integer categorizedCostsAnalyticsId() {
        return categorizedCostsAnalyticsId;
    }

    public void linkToPeriodCostsAnalyticsId(int periodCostsAnalyticsId) {
        this.periodCostsAnalyticsId = periodCostsAnalyticsId;
    }

    public void linkToCategoryCostsAnalytics(int categorizedCostsId) {
        this.categorizedCostsAnalyticsId = categorizedCostsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerCosts that = (CustomerCosts) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}