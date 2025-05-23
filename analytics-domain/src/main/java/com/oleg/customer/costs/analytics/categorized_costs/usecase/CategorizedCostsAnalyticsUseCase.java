package com.oleg.customer.costs.analytics.categorized_costs.usecase;

import com.oleg.customer.costs.analytics.categorized_costs.source.GetCategorizedCostsAnalyticsSource;
import com.oleg.customer.costs.analytics.categorized_costs.value_object.CategorizedCostsAnalyticsWithCosts;
import com.oleg.customer.costs.analytics.common.exception.NotFoundException;
import com.oleg.customer.costs.analytics.common.value_object.Paginator;
import com.oleg.customer.costs.analytics.customer_costs.query.CategoryCustomerCostsQuery;
import com.oleg.customer.costs.analytics.customer_costs.source.GetCustomerCosts;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.oleg.customer.costs.analytics.categorized_costs.colum.CategorizedCostsAnalyticsColumn.AMOUNT;
import static com.oleg.customer.costs.analytics.categorized_costs.colum.CategorizedCostsAnalyticsColumn.AVERAGE;
import static com.oleg.customer.costs.analytics.categorized_costs.colum.CategorizedCostsAnalyticsColumn.CATEGORY_DESCRIPTION;
import static com.oleg.customer.costs.analytics.categorized_costs.colum.CategorizedCostsAnalyticsColumn.CATEGORY_ID;
import static com.oleg.customer.costs.analytics.categorized_costs.colum.CategorizedCostsAnalyticsColumn.ID;
import static com.oleg.customer.costs.analytics.categorized_costs.colum.CategorizedCostsAnalyticsColumn.PERCENT;
import static com.oleg.customer.costs.analytics.categorized_costs.colum.CategorizedCostsAnalyticsColumn.TRANSACTIONS_COUNT;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Service
public class CategorizedCostsAnalyticsUseCase {

    private static final int CUSTOMER_COSTS_LIMIT = 30;

    private final GetCustomerCosts getCustomerCosts;
    private final GetCategorizedCostsAnalyticsSource getCategorizedCostsAnalyticsSource;

    public CategorizedCostsAnalyticsUseCase(GetCustomerCosts getCustomerCosts,
                                            GetCategorizedCostsAnalyticsSource getCategorizedCostsAnalyticsSource) {
        this.getCustomerCosts = getCustomerCosts;
        this.getCategorizedCostsAnalyticsSource = getCategorizedCostsAnalyticsSource;
    }

    public CategorizedCostsAnalyticsWithCosts get(int id, Paginator paginator) {
        var columns = Set.of(ID, CATEGORY_ID, AMOUNT, CATEGORY_DESCRIPTION, PERCENT, TRANSACTIONS_COUNT, AVERAGE);

        if (paginator == null) {
            paginator = new Paginator(CUSTOMER_COSTS_LIMIT);
        }

        var categorizedCostsAnalytics = getCategorizedCostsAnalyticsSource.get(id, columns);
        if (categorizedCostsAnalytics == null) {
            throw new NotFoundException("Categorized costs analytics not found for id " + id);
        }

        var customerCosts = getCustomerCosts.getForCategory(paginator, id);

        List<CategoryCustomerCostsQuery> extrapolate = extrapolate(customerCosts);
        return new CategorizedCostsAnalyticsWithCosts(extrapolate, customerCosts, categorizedCostsAnalytics);
    }

    public List<CategoryCustomerCostsQuery> extrapolate(List<CategoryCustomerCostsQuery> original) {
        Map<LocalDate, CategoryCustomerCostsQuery> existingByDate = original.stream()
            .collect(toMap(this::mapKey, identity(), this::merge));

        YearMonth yearMonth = getYearMonth(existingByDate.keySet());
        if (yearMonth == null) return List.of();

        List<CategoryCustomerCostsQuery> result = new ArrayList<>(yearMonth.lengthOfMonth());

        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate date = yearMonth.atDay(day);
            CategoryCustomerCostsQuery record = existingByDate.get(date);

            if (record == null) {
                record = new CategoryCustomerCostsQuery(BigDecimal.ZERO, null, date.atStartOfDay());
            }

            result.add(record);
        }

        return result;
    }

    private LocalDate mapKey(CategoryCustomerCostsQuery query) {
        return query.createdAt().toLocalDate();
    }

    private CategoryCustomerCostsQuery merge(CategoryCustomerCostsQuery a, CategoryCustomerCostsQuery b) {
        return new CategoryCustomerCostsQuery(a.amount().add(b.amount()), a.description(), a.createdAt());
    }

    private YearMonth getYearMonth(Set<LocalDate> availableDates) {
        return availableDates.stream()
            .findFirst()
            .map(YearMonth::from)
            .orElse(null);
    }
}