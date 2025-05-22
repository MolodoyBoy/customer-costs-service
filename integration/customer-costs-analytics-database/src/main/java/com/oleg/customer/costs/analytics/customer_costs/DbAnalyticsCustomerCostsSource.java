package com.oleg.customer.costs.analytics.customer_costs;

import com.oleg.customer.costs.analytics.common.value_object.Paginator;
import com.oleg.customer.costs.analytics.customer_costs.entity.CustomerCosts;
import com.oleg.customer.costs.analytics.customer_costs.mapper.CustomerCostsByCategoryMapper;
import com.oleg.customer.costs.analytics.customer_costs.mapper.CustomerCostsByPeriodMapper;
import com.oleg.customer.costs.analytics.customer_costs.mapper.AnalyticsCustomerCostsQueryMapper;
import com.oleg.customer.costs.analytics.customer_costs.query.CategoryCustomerCostsQuery;
import com.oleg.customer.costs.analytics.customer_costs.query.PeriodCustomerCostsQuery;
import com.oleg.customer.costs.analytics.customer_costs.source.AdminCustomerCostsSource;
import com.oleg.customer.costs.analytics.customer_costs.source.GetCustomerCosts;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.oleg.customer.costs.analytics.customer_costs.CustomerCostsFields.*;
import static com.oleg.fund.customer.costs.analytics.tables.CustomerCosts.CUSTOMER_COSTS;
import static com.oleg.fund.customer.costs.analytics.tables.CustomerCostsByCategory.CUSTOMER_COSTS_BY_CATEGORY;
import static com.oleg.fund.customer.costs.analytics.tables.CustomerCostsByPeriod.CUSTOMER_COSTS_BY_PERIOD;

@Repository
class DbAnalyticsCustomerCostsSource implements GetCustomerCosts, AdminCustomerCostsSource {

    private final DSLContext dslContext;
    private final AnalyticsCustomerCostsQueryMapper analyticsCustomerCostsQueryMapper;
    private final CustomerCostsByPeriodMapper customerCostsByPeriodMapper;
    private final CustomerCostsByCategoryMapper customerCostsByCategoryMapper;

    public DbAnalyticsCustomerCostsSource(DSLContext dslContext,
                                          AnalyticsCustomerCostsQueryMapper analyticsCustomerCostsQueryMapper,
                                          CustomerCostsByPeriodMapper customerCostsByPeriodMapper,
                                          CustomerCostsByCategoryMapper customerCostsByCategoryMapper) {
        this.dslContext = dslContext;
        this.analyticsCustomerCostsQueryMapper = analyticsCustomerCostsQueryMapper;
        this.customerCostsByPeriodMapper = customerCostsByPeriodMapper;
        this.customerCostsByCategoryMapper = customerCostsByCategoryMapper;
    }

    @Override
    public void bindCustomerCostsByPeriod(List<CustomerCosts> customerCosts) {
        if (customerCosts.isEmpty()) return;
        dslContext.insertInto(CUSTOMER_COSTS_BY_PERIOD)
            .set(customerCostsByPeriodMapper.toRecords(customerCosts))
            .execute();
    }

    @Override
    public void bindCustomerCostsByCategory(List<CustomerCosts> customerCosts) {
        if (customerCosts.isEmpty()) return;

        dslContext.insertInto(CUSTOMER_COSTS_BY_CATEGORY)
            .set(customerCostsByCategoryMapper.toRecords(customerCosts))
            .execute();
    }

    @Override
    public List<PeriodCustomerCostsQuery> getForPeriod(int periodCostsAnalyticsId) {
        return dslContext.select(sum(), createdAt())
            .from(CUSTOMER_COSTS)
            .innerJoin(CUSTOMER_COSTS_BY_PERIOD).on(CUSTOMER_COSTS_BY_PERIOD.CUSTOMER_COSTS_ID.eq(CUSTOMER_COSTS.ID))
            .where(CUSTOMER_COSTS_BY_PERIOD.PERIOD_COSTS_ANALYTICS_ID.eq(periodCostsAnalyticsId))
            .groupBy(createdAt())
            .orderBy(createdAt())
            .fetch(analyticsCustomerCostsQueryMapper);
    }

    @Override
    public List<CategoryCustomerCostsQuery> getForCategory(Paginator paginator, int categoryCostsAnalyticsId) {
        return dslContext.select(
                CUSTOMER_COSTS.AMOUNT,
                CUSTOMER_COSTS.CREATED_AT,
                CUSTOMER_COSTS.DESCRIPTION
            )
            .from(CUSTOMER_COSTS)
            .innerJoin(CUSTOMER_COSTS_BY_CATEGORY).on(CUSTOMER_COSTS_BY_CATEGORY.CUSTOMER_COSTS_ID.eq(CUSTOMER_COSTS.ID))
            .where(CUSTOMER_COSTS_BY_CATEGORY.CATEGORIZED_COSTS_ANALYTICS_ID.eq(categoryCostsAnalyticsId))
            .orderBy(CUSTOMER_COSTS.CREATED_AT.desc())
            .offset(paginator.skip())
            .limit(paginator.limit())
            .fetch(this::map);
    }

    private CategoryCustomerCostsQuery map(Record rc) {
        return new CategoryCustomerCostsQuery(
            rc.get(CUSTOMER_COSTS.AMOUNT),
            rc.get(CUSTOMER_COSTS.DESCRIPTION),
            rc.get(CUSTOMER_COSTS.CREATED_AT)
        );
    }
}