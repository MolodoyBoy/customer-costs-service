package com.oleg.customer.costs.analytics.period;

import com.oleg.customer.costs.analytics.categorized_costs.snapshot.CategorizedCostsAnalyticsSnapshot;
import com.oleg.customer.costs.analytics.customer_costs.query.PeriodCustomerCostsQuery;
import com.oleg.customer.costs.analytics.period_costs.snapshot.PeriodCostsAnalyticsSnapshot;
import com.oleg.customer.costs.analytics.period_costs.value_object.PeriodCostsAnalyticsWithCategories;
import com.oleg.customer.costs.model.CategorizedCostsAnalyticsDto;
import com.oleg.customer.costs.model.PeriodCostsAnalyticsDto;
import com.oleg.customer.costs.model.PeriodCostsAnalyticsResponseDto;
import com.oleg.customer.costs.model.PeriodCustomerCostsDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class PeriodCostsAnalyticsConverter {

    public PeriodCostsAnalyticsResponseDto convert(PeriodCostsAnalyticsWithCategories entity) {
        return new PeriodCostsAnalyticsResponseDto()
            .customerCosts(convertCustomerCosts(entity.customerCosts()))
            .periodCostsAnalytics(convert(entity.periodCostsAnalytics()))
            .categorizedCostsAnalytics(convert(entity.categorizedCostsAnalytics()));
    }

    private List<PeriodCustomerCostsDto> convertCustomerCosts(List<PeriodCustomerCostsQuery> customerCostsQueries) {
        return customerCostsQueries.stream()
            .map(this::convert)
            .toList();
    }

    private PeriodCustomerCostsDto convert(PeriodCustomerCostsQuery periodCustomerCostsQuery) {
        return new PeriodCustomerCostsDto()
            .amount(periodCustomerCostsQuery.amount().doubleValue())
            .createdAt(periodCustomerCostsQuery.createdAt());
    }

    private PeriodCostsAnalyticsDto convert(PeriodCostsAnalyticsSnapshot entity) {
        return new PeriodCostsAnalyticsDto()
            .id(entity.id())
            .amount(toDouble(entity.amount()))
            .period(entity.period().toLocalDate())
            .average(toDouble(entity.average()))
            .differenceFromPrevious(toDouble(entity.differenceFromPrevious()));
    }

    private Double toDouble(BigDecimal value) {
        if (value == null) return 0.0;
        return value.doubleValue();
    }

    private List<CategorizedCostsAnalyticsDto> convert(List<CategorizedCostsAnalyticsSnapshot> entities) {
        return entities.stream()
            .map(this::convert)
            .toList();
    }

    private CategorizedCostsAnalyticsDto convert(CategorizedCostsAnalyticsSnapshot entity) {
        return new CategorizedCostsAnalyticsDto()
            .id(entity.id())
            .amount(entity.amount().doubleValue())
            .percent(entity.percent().doubleValue())
            .transactionsCount(entity.transactionsCount())
            .categoryDescription(entity.categoryDescription());
    }
}
