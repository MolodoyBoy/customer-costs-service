package com.oleg.customer.costs.analytics.category;

import com.oleg.customer.costs.analytics.categorized_costs.snapshot.CategorizedCostsAnalyticsSnapshot;
import com.oleg.customer.costs.analytics.categorized_costs.value_object.CategorizedCostsAnalyticsWithCosts;
import com.oleg.customer.costs.analytics.customer_costs.query.CategoryCustomerCostsQuery;
import com.oleg.customer.costs.model.CategorizedCostsAnalyticsDto;
import com.oleg.customer.costs.model.CategorizedCostsAnalyticsResponseDto;
import com.oleg.customer.costs.model.CustomerCostsDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneOffset;
import java.util.List;

@Component
public class CategorizedCostsAnalyticsConverter {

    public CategorizedCostsAnalyticsResponseDto convert(CategorizedCostsAnalyticsWithCosts entity) {
        return new CategorizedCostsAnalyticsResponseDto()
            .extrapolated(convert(entity.extrapolated()))
            .customerCosts(convert(entity.customerCosts()))
            .categorizedCostsAnalytics(convert(entity.categorizedCostsAnalytics()));
    }

    private List<CustomerCostsDto> convert(List<CategoryCustomerCostsQuery> customerCosts) {
        return customerCosts.stream()
            .map(this::convert)
            .toList();
    }

    private CustomerCostsDto convert(CategoryCustomerCostsQuery customerCostsQuery) {
        return new CustomerCostsDto()
            .description(customerCostsQuery.description())
            .amount(customerCostsQuery.amount().abs().doubleValue())
            .createdAt(customerCostsQuery.createdAt().atOffset(ZoneOffset.UTC));
    }

    private CategorizedCostsAnalyticsDto convert(CategorizedCostsAnalyticsSnapshot entity) {
        return new CategorizedCostsAnalyticsDto()
            .id(entity.id())
            .amount(toDouble(entity.amount()))
            .percent(toDouble(entity.percent()))
            .average(toDouble(entity.average()))
            .transactionsCount(entity.transactionsCount())
            .categoryDescription(entity.categoryDescription());
    }

    private Double toDouble(BigDecimal value) {
        if (value == null) return 0.0;

        return value.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
