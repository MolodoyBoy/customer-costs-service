package com.oleg.customer.costs.analytics.period_costs.usecase;

import com.oleg.customer.costs.analytics.categorized_costs.source.GetCategorizedCostsAnalyticsSource;
import com.oleg.customer.costs.analytics.common.exception.NotFoundException;
import com.oleg.customer.costs.analytics.customer_costs.query.PeriodCustomerCostsQuery;
import com.oleg.customer.costs.analytics.customer_costs.source.GetCustomerCosts;
import com.oleg.customer.costs.analytics.period_costs.query.AnalyticPeriodQuery;
import com.oleg.customer.costs.analytics.period_costs.source.GetAnalyticPeriodSource;
import com.oleg.customer.costs.analytics.period_costs.source.GetPeriodCostsAnalyticsSource;
import com.oleg.customer.costs.analytics.period_costs.value_object.PeriodCostsAnalyticsWithCategories;
import com.oleg.customer.costs.analytics.user.AnalyticsUserContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.oleg.customer.costs.analytics.categorized_costs.colum.CategorizedCostsAnalyticsColumn.AMOUNT;
import static com.oleg.customer.costs.analytics.categorized_costs.colum.CategorizedCostsAnalyticsColumn.CATEGORY_DESCRIPTION;
import static com.oleg.customer.costs.analytics.categorized_costs.colum.CategorizedCostsAnalyticsColumn.ID;
import static com.oleg.customer.costs.analytics.categorized_costs.colum.CategorizedCostsAnalyticsColumn.PERCENT;
import static com.oleg.customer.costs.analytics.categorized_costs.colum.CategorizedCostsAnalyticsColumn.TRANSACTIONS_COUNT;
import static java.util.function.Function.*;
import static java.util.stream.Collectors.*;

@Service
public class PeriodCostsAnalyticsUseCase {

    private static final int CATEGORIZED_COSTS_ANALYTICS_LIMIT = 5;

    private final AnalyticsUserContext analyticsUserContext;
    private final GetCustomerCosts getCustomerCosts;
    private final GetAnalyticPeriodSource getAnalyticPeriodSource;
    private final GetPeriodCostsAnalyticsSource getPeriodCostsAnalyticsSource;
    private final GetCategorizedCostsAnalyticsSource getCategorizedCostsAnalyticsSource;

    public PeriodCostsAnalyticsUseCase(AnalyticsUserContext analyticsUserContext,
                                       GetCustomerCosts getCustomerCosts,
                                       GetAnalyticPeriodSource getAnalyticPeriodSource,
                                       GetPeriodCostsAnalyticsSource getPeriodCostsAnalyticsSource,
                                       GetCategorizedCostsAnalyticsSource getCategorizedCostsAnalyticsSource) {
        this.analyticsUserContext = analyticsUserContext;
        this.getCustomerCosts = getCustomerCosts;
        this.getAnalyticPeriodSource = getAnalyticPeriodSource;
        this.getPeriodCostsAnalyticsSource = getPeriodCostsAnalyticsSource;
        this.getCategorizedCostsAnalyticsSource = getCategorizedCostsAnalyticsSource;
    }

    public List<AnalyticPeriodQuery> get() {
        int userId = analyticsUserContext.id();
        List<AnalyticPeriodQuery> analyticPeriods = getAnalyticPeriodSource.getAnalyticPeriods(userId);
        if (analyticPeriods.isEmpty()) {
            throw new NotFoundException("No analytic periods found for user.");
        }

        return analyticPeriods;
    }

    public PeriodCostsAnalyticsWithCategories get(int id, Integer limit) {
        var periodCostsAnalytics = getPeriodCostsAnalyticsSource.get(id);
        if (periodCostsAnalytics == null) {
            throw new NotFoundException("No period costs analytics found for id: " + id);
        }

        if (limit == null) {
            limit = CATEGORIZED_COSTS_ANALYTICS_LIMIT;
        }

        var forPeriod = getCustomerCosts.getForPeriod(id);
        var columns = Set.of(ID, AMOUNT, PERCENT, TRANSACTIONS_COUNT, CATEGORY_DESCRIPTION);
        var categorizedCostsAnalytics = getCategorizedCostsAnalyticsSource.getForPeriod(limit, id, columns);

        var extrapolated = extrapolate(periodCostsAnalytics.period().toYearMonth(), forPeriod);
        return new PeriodCostsAnalyticsWithCategories(extrapolated, periodCostsAnalytics, categorizedCostsAnalytics);
    }

    public static List<PeriodCustomerCostsQuery> extrapolate(YearMonth month, List<PeriodCustomerCostsQuery> original) {
        Map<LocalDate, PeriodCustomerCostsQuery> existingByDate = original.stream()
            .collect(toMap(PeriodCustomerCostsQuery::createdAt, identity()));

        List<PeriodCustomerCostsQuery> result = new ArrayList<>(month.lengthOfMonth());

        for (int day = 1; day <= month.lengthOfMonth(); day++) {
            LocalDate date = month.atDay(day);
            PeriodCustomerCostsQuery record = existingByDate.get(date);

            if (record == null) {
                record = new PeriodCustomerCostsQuery(BigDecimal.ZERO, date);
            }
            result.add(record);
        }

        return result;
    }
}