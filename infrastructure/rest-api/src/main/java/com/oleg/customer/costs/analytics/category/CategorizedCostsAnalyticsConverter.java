package com.oleg.customer.costs.analytics.category;

import com.oleg.customer.costs.analytics.categorized_costs.snapshot.CategorizedCostsAnalyticsSnapshot;
import com.oleg.customer.costs.analytics.categorized_costs.value_object.CategorizedCostsAnalyticsWithCosts;
import com.oleg.customer.costs.analytics.customer_costs.query.CategoryCustomerCostsQuery;
import com.oleg.customer.costs.model.CategorizedCostsAnalyticsDto;
import com.oleg.customer.costs.model.CategorizedCostsAnalyticsResponseDto;
import com.oleg.customer.costs.model.CustomerCostsDto;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.List;

@Component
public class CategorizedCostsAnalyticsConverter {

    public CategorizedCostsAnalyticsResponseDto convert(CategorizedCostsAnalyticsWithCosts entity) {
        return new CategorizedCostsAnalyticsResponseDto()
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
            .amount(customerCostsQuery.amount().doubleValue())
            .createdAt(customerCostsQuery.createdAt().atOffset(ZoneOffset.UTC));
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
