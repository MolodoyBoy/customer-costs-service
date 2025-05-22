package com.oleg.customer.costs.analytics.period_costs.entity;

import com.oleg.customer.costs.analytics.customer_costs.entity.CustomerCosts;
import com.oleg.customer.costs.analytics.period_costs.snapshot.PeriodCostsAnalyticsSnapshot;
import com.oleg.customer.costs.analytics.period_costs.value_object.Period;

import java.math.BigDecimal;

import static com.oleg.customer.costs.analytics.period_costs.value_object.Period.of;

public class PeriodCostsAnalytics {

    private final int id;
    private final Period period;

    private BigDecimal amount;
    private BigDecimal differenceFromPrevious;

    public PeriodCostsAnalytics(int id,
                                Integer period,
                                BigDecimal amount,
                                BigDecimal differenceFromPrevious) {
        this.id = id;
        this.period = of(period);
        this.amount = amount;
        this.differenceFromPrevious = differenceFromPrevious;
    }

    public int id() {
        return id;
    }

    public void addCustomerCosts(CustomerCosts customerCosts) {
        if (customerCosts == null) return;

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

        customerCosts.linkToPeriodCostsAnalyticsId(id);
    }

    public void addPrevious(PeriodCostsAnalytics previousAnalytic) {
        if (previousAnalytic == null) return;
        if (amount == null) {
            differenceFromPrevious = previousAnalytic.amount;
        }
    }

    public PeriodCostsAnalyticsSnapshot toSnapshot() {
        return new PeriodCostsAnalyticsSnapshot(id, period, amount, differenceFromPrevious);
    }
}