package com.oleg.customer.costs.analytics.categorized_costs.entity;

import com.oleg.customer.costs.analytics.categorized_costs.snapshot.CategorizedCostsAnalyticsSnapshot;
import com.oleg.customer.costs.analytics.customer_costs.entity.CustomerCosts;
import com.oleg.customer.costs.analytics.period_costs.entity.PeriodCostsAnalytics;
import com.oleg.customer.costs.analytics.period_costs.snapshot.PeriodCostsAnalyticsSnapshot;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;

import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;

public class CategorizedCostsAnalytics {

    private static final BigDecimal HUNDRED_PERCENT = valueOf(100);

    private final int id;
    private final int categoryId;
    private final String categoryDescription;

    private BigDecimal amount;
    private BigDecimal percent;
    private int transactionsCount;

    public CategorizedCostsAnalytics(int id,
                                     int categoryId,
                                     BigDecimal amount,
                                     BigDecimal percent,
                                     Integer transactionsCount,
                                     String categoryDescription) {
        this.id = id;
        this.amount = amount;
        this.percent = percent;
        this.categoryId = categoryId;
        this.categoryDescription = categoryDescription;
        this.transactionsCount = transactionsCount != null ? transactionsCount : 0;
    }

    public int id() {
        return id;
    }

    public void addCustomerCosts(CustomerCosts customerCosts) {
        if (customerCosts == null || categoryId != customerCosts.categoryId()) return;

        transactionsCount += 1;
        if (this.amount == null) {
            this.amount = customerCosts.amount();
        } else {
            this.amount = this.amount.add(customerCosts.amount());
        }

        customerCosts.linkToCategoryCostsAnalytics(id);
    }

    public void addPeriodCostsAnalytics(PeriodCostsAnalytics periodCostsAnalytics) {
        if (periodCostsAnalytics == null) return;

        PeriodCostsAnalyticsSnapshot snapshot = periodCostsAnalytics.toSnapshot();
        if (amount == null) {
            percent = ZERO;
        } else {
            BigDecimal totalAmount = snapshot.amount();
            if (totalAmount == null || totalAmount.equals(ZERO)) {
                throw new IllegalArgumentException("Total cant be null or zero!");
            }
            BigDecimal delimiter = totalAmount.divide(HUNDRED_PERCENT, MathContext.DECIMAL64);
            percent = amount.divide(delimiter, MathContext.DECIMAL64);
        }
    }

    public CategorizedCostsAnalyticsSnapshot toSnapshot() {
        return new CategorizedCostsAnalyticsSnapshot(id, categoryId, amount, percent, transactionsCount, categoryDescription);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategorizedCostsAnalytics that = (CategorizedCostsAnalytics) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}