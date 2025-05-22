package com.oleg.customer.costs.analytics.categorized_costs.usecase;

import com.oleg.customer.costs.analytics.categorized_costs.source.GetCategorizedCostsAnalyticsSource;
import com.oleg.customer.costs.analytics.categorized_costs.value_object.CategorizedCostsAnalyticsWithCosts;
import com.oleg.customer.costs.analytics.common.exception.NotFoundException;
import com.oleg.customer.costs.analytics.common.value_object.Paginator;
import com.oleg.customer.costs.analytics.customer_costs.source.GetCustomerCosts;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.oleg.customer.costs.analytics.categorized_costs.colum.CategorizedCostsAnalyticsColumn.AMOUNT;
import static com.oleg.customer.costs.analytics.categorized_costs.colum.CategorizedCostsAnalyticsColumn.CATEGORY_DESCRIPTION;
import static com.oleg.customer.costs.analytics.categorized_costs.colum.CategorizedCostsAnalyticsColumn.ID;
import static com.oleg.customer.costs.analytics.categorized_costs.colum.CategorizedCostsAnalyticsColumn.PERCENT;
import static com.oleg.customer.costs.analytics.categorized_costs.colum.CategorizedCostsAnalyticsColumn.TRANSACTIONS_COUNT;

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
        var columns = Set.of(ID, AMOUNT, CATEGORY_DESCRIPTION, PERCENT, TRANSACTIONS_COUNT);

        if (paginator == null) {
            paginator = new Paginator(CUSTOMER_COSTS_LIMIT);
        }

        var categorizedCostsAnalytics = getCategorizedCostsAnalyticsSource.get(id, columns);
        if (categorizedCostsAnalytics == null) {
            throw new NotFoundException("Categorized costs analytics not found for id " + id);
        }

        var customerCosts = getCustomerCosts.getForCategory(paginator, id);

        return new CategorizedCostsAnalyticsWithCosts(customerCosts, categorizedCostsAnalytics);
    }
}