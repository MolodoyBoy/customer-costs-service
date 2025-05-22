package com.oleg.customer.costs.analytics.period_costs.entity;

import com.oleg.customer.costs.analytics.customer_costs.entity.CustomerCosts;
import com.oleg.customer.costs.analytics.period_costs.snapshot.PeriodCostsAnalyticsSnapshot;
import com.oleg.customer.costs.analytics.period_costs.value_object.Period;

import java.math.BigDecimal;

import static java.math.RoundingMode.*;
import static com.oleg.customer.costs.analytics.period_costs.value_object.Period.of;

public class PeriodCostsAnalytics {

    private final int id;
    private final Period period;

    private BigDecimal amount;
    private BigDecimal average;
    private Integer totalTransactions;
    private BigDecimal differenceFromPrevious;

    public PeriodCostsAnalytics(int id,
                                Integer period,
                                BigDecimal amount,
                                BigDecimal average,
                                Integer totalTransactions,
                                BigDecimal differenceFromPrevious) {
        this.id = id;
        this.period = of(period);
        this.amount = amount;
        this.average = average;
        this.totalTransactions = totalTransactions;
        this.differenceFromPrevious = differenceFromPrevious;
    }

    public int id() {
        return id;
    }

    public void addCustomerCosts(CustomerCosts customerCosts) {
        if (customerCosts == null) return;

        totalTransactions += 1;
        BigDecimal amount = customerCosts.amount();
        if (this.amount == null) {
            this.amount = amount;
        } else {
            this.amount = this.amount.add(amount);
        }

        if (differenceFromPrevious == null) {
            differenceFromPrevious = amount;
        } else {
            differenceFromPrevious = differenceFromPrevious.add(amount);
        }

        average = amount.divide(BigDecimal.valueOf(totalTransactions), 2, HALF_UP);
        customerCosts.linkToPeriodCostsAnalyticsId(id);
    }

    public void addPrevious(PeriodCostsAnalytics previousAnalytic) {
        if (previousAnalytic == null) return;
        if (amount == null) {
            differenceFromPrevious = previousAnalytic.amount;
        }
    }

    public PeriodCostsAnalyticsSnapshot toSnapshot() {
        return new PeriodCostsAnalyticsSnapshot(id, period, amount, average, totalTransactions, differenceFromPrevious);
    }
}