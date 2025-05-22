package com.oleg.customer.costs.analytics.categorized_costs_analytics;

import com.oleg.customer.costs.analytics.categorized_costs.colum.CategorizedCostsAnalyticsColumn;
import com.oleg.customer.costs.analytics.categorized_costs.command.CategorizedCostAnalyticsCreateCommand;
import com.oleg.customer.costs.analytics.categorized_costs.entity.CategorizedCostsAnalytics;
import com.oleg.customer.costs.analytics.categorized_costs.snapshot.CategorizedCostsAnalyticsSnapshot;
import com.oleg.customer.costs.analytics.categorized_costs.source.AdminCategorizedCostsAnalyticsSource;
import com.oleg.customer.costs.analytics.categorized_costs.source.GetCategorizedCostsAnalyticsSource;
import com.oleg.customer.costs.analytics.categorized_costs_analytics.condition.CategorizedCostAnalyticsCreateCommandConditionBuilder;
import com.oleg.customer.costs.analytics.categorized_costs_analytics.mapper.CategorizedCostAnalyticsCreateCommandMapper;
import com.oleg.customer.costs.analytics.categorized_costs_analytics.mapper.CategorizedCostsAnalyticsMapper;
import com.oleg.customer.costs.analytics.categorized_costs_analytics.mapper.CategorizedCostsAnalyticsSnapshotMapper;
import com.oleg.customer.costs.analytics.column.context.ColumnContext;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static com.oleg.fund.customer.costs.analytics.tables.CostsCategory.COSTS_CATEGORY;
import static com.oleg.fund.customer.costs.analytics.tables.CategorizedCostsAnalytics.CATEGORIZED_COSTS_ANALYTICS;

@Repository
class DbCategorizedCostsAnalyticsSource implements AdminCategorizedCostsAnalyticsSource, GetCategorizedCostsAnalyticsSource {

    private final DSLContext dslContext;
    private final ColumnContext columnContext;
    private final CategorizedCostsAnalyticsMapper categorizedCostsAnalyticsMapper;
    private final CategorizedCostsAnalyticsSnapshotMapper categorizedCostsAnalyticsSnapshotMapper;
    private final CategorizedCostAnalyticsCreateCommandMapper categorizedCostAnalyticsCreateCommandMapper;
    private final CategorizedCostAnalyticsCreateCommandConditionBuilder categorizedCostAnalyticsCreateCommandConditionBuilder;

    DbCategorizedCostsAnalyticsSource(DSLContext dslContext,
                                      ColumnContext columnContext,
                                      CategorizedCostsAnalyticsMapper categorizedCostsAnalyticsMapper,
                                      CategorizedCostsAnalyticsSnapshotMapper categorizedCostsAnalyticsSnapshotMapper,
                                      CategorizedCostAnalyticsCreateCommandMapper categorizedCostAnalyticsCreateCommandMapper,
                                      CategorizedCostAnalyticsCreateCommandConditionBuilder categorizedCostAnalyticsCreateCommandConditionBuilder) {
        this.dslContext = dslContext;
        this.columnContext = columnContext;
        this.categorizedCostsAnalyticsMapper = categorizedCostsAnalyticsMapper;
        this.categorizedCostsAnalyticsSnapshotMapper = categorizedCostsAnalyticsSnapshotMapper;
        this.categorizedCostAnalyticsCreateCommandMapper = categorizedCostAnalyticsCreateCommandMapper;
        this.categorizedCostAnalyticsCreateCommandConditionBuilder = categorizedCostAnalyticsCreateCommandConditionBuilder;
    }

    @Override
    public CategorizedCostsAnalyticsSnapshot get(int id, Set<CategorizedCostsAnalyticsColumn> columns) {
        Condition condition = CATEGORIZED_COSTS_ANALYTICS.ID.eq(id);
        return get(null, condition, columns).findFirst()
            .orElse(null);
    }

    @Override
    public List<CategorizedCostsAnalyticsSnapshot> getForPeriod(Integer limit, int periodCostsAnalyticsId, Set<CategorizedCostsAnalyticsColumn> columns) {
        Condition condition = CATEGORIZED_COSTS_ANALYTICS.PERIOD_COST_ANALYTICS_ID.eq(periodCostsAnalyticsId);
        return get(limit, condition, columns).toList();
    }

    private Stream<CategorizedCostsAnalyticsSnapshot> get(Integer limit,
                                                          Condition condition,
                                                          Set<CategorizedCostsAnalyticsColumn> columns) {
        List<Field<?>> fields = columnContext.getFields(columns);
        var orderByStep = dslContext.select(fields)
            .from(CATEGORIZED_COSTS_ANALYTICS)
            .innerJoin(COSTS_CATEGORY).on(COSTS_CATEGORY.ID.eq(CATEGORIZED_COSTS_ANALYTICS.CATEGORY_ID))
            .where(condition)
            .orderBy(CATEGORIZED_COSTS_ANALYTICS.AMOUNT.desc());


        if (limit != null) {
            return orderByStep.limit(limit)
                .fetchStream()
                .map(categorizedCostsAnalyticsSnapshotMapper);
        }

        return orderByStep
            .fetchStream()
            .map(categorizedCostsAnalyticsSnapshotMapper);
    }

    @Override
    public int update(Collection<CategorizedCostsAnalytics> categorizedCostsAnalytics) {
        if (categorizedCostsAnalytics.isEmpty()) return 0;
        var queries = categorizedCostsAnalytics.stream()
            .map(ca -> {
                CategorizedCostsAnalyticsSnapshot snapshot = ca.toSnapshot();
                return dslContext.update(CATEGORIZED_COSTS_ANALYTICS)
                    .set(CATEGORIZED_COSTS_ANALYTICS.AMOUNT, snapshot.amount())
                    .set(CATEGORIZED_COSTS_ANALYTICS.PERCENT, snapshot.percent())
                    .set(CATEGORIZED_COSTS_ANALYTICS.TRANSACTIONS_COUNT, snapshot.transactionsCount())
                    .where(CATEGORIZED_COSTS_ANALYTICS.ID.eq(snapshot.id()));
            })
            .toList();

        return Arrays.stream(dslContext.batch(queries).execute()).sum();
    }

    @Override
    public Map<CategorizedCostAnalyticsCreateCommand, CategorizedCostsAnalytics> get(Collection<CategorizedCostAnalyticsCreateCommand> keys) {
        if (keys.isEmpty()) return Map.of();
        return dslContext.select(
                COSTS_CATEGORY.DESCRIPTION,
                CATEGORIZED_COSTS_ANALYTICS.ID,
                CATEGORIZED_COSTS_ANALYTICS.AMOUNT,
                CATEGORIZED_COSTS_ANALYTICS.PERCENT,
                CATEGORIZED_COSTS_ANALYTICS.CATEGORY_ID,
                CATEGORIZED_COSTS_ANALYTICS.TRANSACTIONS_COUNT,
                CATEGORIZED_COSTS_ANALYTICS.PERIOD_COST_ANALYTICS_ID
            )
            .from(CATEGORIZED_COSTS_ANALYTICS)
            .innerJoin(COSTS_CATEGORY).on(COSTS_CATEGORY.ID.eq(CATEGORIZED_COSTS_ANALYTICS.CATEGORY_ID))
            .where(categorizedCostAnalyticsCreateCommandConditionBuilder.buildCondition(keys))
            .fetchMap(categorizedCostAnalyticsCreateCommandMapper, categorizedCostsAnalyticsMapper);
    }

    @Override
    public Map<CategorizedCostAnalyticsCreateCommand, CategorizedCostsAnalytics> create(Collection<CategorizedCostAnalyticsCreateCommand> keys) {
        if (keys.isEmpty()) return Map.of();
        return dslContext.insertInto(CATEGORIZED_COSTS_ANALYTICS)
            .set(categorizedCostAnalyticsCreateCommandMapper.toRecords(keys))
            .returning(
                CATEGORIZED_COSTS_ANALYTICS.ID,
                CATEGORIZED_COSTS_ANALYTICS.CATEGORY_ID,
                CATEGORIZED_COSTS_ANALYTICS.PERIOD_COST_ANALYTICS_ID
            )
            .fetchMap(categorizedCostAnalyticsCreateCommandMapper, categorizedCostsAnalyticsMapper);
    }
}