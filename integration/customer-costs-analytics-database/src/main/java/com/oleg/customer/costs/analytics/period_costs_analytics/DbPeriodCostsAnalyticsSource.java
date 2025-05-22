package com.oleg.customer.costs.analytics.period_costs_analytics;

import com.oleg.customer.costs.analytics.period_costs.command.PeriodCostAnalyticsCreateCommand;
import com.oleg.customer.costs.analytics.period_costs.entity.PeriodCostsAnalytics;
import com.oleg.customer.costs.analytics.period_costs.query.AnalyticPeriodQuery;
import com.oleg.customer.costs.analytics.period_costs.snapshot.PeriodCostsAnalyticsSnapshot;
import com.oleg.customer.costs.analytics.period_costs.source.AdminPeriodCostsAnalyticsSource;
import com.oleg.customer.costs.analytics.period_costs.source.GetAnalyticPeriodSource;
import com.oleg.customer.costs.analytics.period_costs.source.GetPeriodCostsAnalyticsSource;
import com.oleg.customer.costs.analytics.period_costs_analytics.condition.PeriodCostAnalyticsCreateCommandConditionBuilder;
import com.oleg.customer.costs.analytics.period_costs_analytics.mapper.AnalyticPeriodMapper;
import com.oleg.customer.costs.analytics.period_costs_analytics.mapper.PeriodCostAnalyticsCreateCommandMapper;
import com.oleg.customer.costs.analytics.period_costs_analytics.mapper.PeriodCostsAnalyticsMapper;
import com.oleg.customer.costs.analytics.period_costs_analytics.mapper.PeriodCostsAnalyticsSnapshotMapper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.oleg.fund.customer.costs.analytics.tables.PeriodCostsAnalytics.PERIOD_COSTS_ANALYTICS;

@Repository
class DbPeriodCostsAnalyticsSource implements AdminPeriodCostsAnalyticsSource, GetPeriodCostsAnalyticsSource, GetAnalyticPeriodSource {

    private final DSLContext dslContext;
    private final AnalyticPeriodMapper analyticPeriodMapper;
    private final PeriodCostsAnalyticsMapper periodCostsAnalyticsMapper;
    private final PeriodCostsAnalyticsSnapshotMapper periodCostsAnalyticsSnapshotMapper;
    private final PeriodCostAnalyticsCreateCommandMapper periodCostAnalyticsCreateCommandMapper;
    private final PeriodCostAnalyticsCreateCommandConditionBuilder periodCostAnalyticsCreateCommandConditionBuilder;

    DbPeriodCostsAnalyticsSource(DSLContext dslContext,
                                 AnalyticPeriodMapper analyticPeriodMapper,
                                 PeriodCostsAnalyticsMapper periodCostsAnalyticsMapper,
                                 PeriodCostsAnalyticsSnapshotMapper periodCostsAnalyticsSnapshotMapper,
                                 PeriodCostAnalyticsCreateCommandMapper periodCostAnalyticsCreateCommandMapper,
                                 PeriodCostAnalyticsCreateCommandConditionBuilder periodCostAnalyticsCreateCommandConditionBuilder) {
        this.dslContext = dslContext;
        this.analyticPeriodMapper = analyticPeriodMapper;
        this.periodCostsAnalyticsMapper = periodCostsAnalyticsMapper;
        this.periodCostsAnalyticsSnapshotMapper = periodCostsAnalyticsSnapshotMapper;
        this.periodCostAnalyticsCreateCommandMapper = periodCostAnalyticsCreateCommandMapper;
        this.periodCostAnalyticsCreateCommandConditionBuilder = periodCostAnalyticsCreateCommandConditionBuilder;
    }

    @Override
    public List<AnalyticPeriodQuery> getAnalyticPeriods(int userId) {
        return dslContext.select(PERIOD_COSTS_ANALYTICS.ID, PERIOD_COSTS_ANALYTICS.PERIOD)
            .from(PERIOD_COSTS_ANALYTICS)
            .where(PERIOD_COSTS_ANALYTICS.USER_ID.eq(userId))
            .orderBy(PERIOD_COSTS_ANALYTICS.PERIOD)
            .fetch(analyticPeriodMapper);
    }

    @Override
    public PeriodCostsAnalyticsSnapshot get(int id) {
        Condition condition = PERIOD_COSTS_ANALYTICS.ID.eq(id);
        return get(condition);
    }

    private PeriodCostsAnalyticsSnapshot get(Condition condition) {
        return dslContext.select(
                PERIOD_COSTS_ANALYTICS.ID,
                PERIOD_COSTS_ANALYTICS.AMOUNT,
                PERIOD_COSTS_ANALYTICS.PERIOD,
                PERIOD_COSTS_ANALYTICS.DIFFERENCE_FROM_PREVIOUS_MONTH)
            .from(PERIOD_COSTS_ANALYTICS)
            .where(condition)
            .fetchOne(periodCostsAnalyticsSnapshotMapper);
    }

    @Override
    public int update(Collection<PeriodCostsAnalytics> periodCostsAnalytics) {
        if (periodCostsAnalytics.isEmpty()) return 0;
        var queries = periodCostsAnalytics.stream()
            .map(ca -> {
                PeriodCostsAnalyticsSnapshot snapshot = ca.toSnapshot();
                return dslContext.update(PERIOD_COSTS_ANALYTICS)
                    .set(PERIOD_COSTS_ANALYTICS.AMOUNT, snapshot.amount())
                    .set(PERIOD_COSTS_ANALYTICS.DIFFERENCE_FROM_PREVIOUS_MONTH, snapshot.differenceFromPrevious())
                    .where(PERIOD_COSTS_ANALYTICS.ID.eq(snapshot.id()));
            })
            .toList();

        return Arrays.stream(dslContext.batch(queries).execute()).sum();
    }

    @Override
    public Map<PeriodCostAnalyticsCreateCommand, PeriodCostsAnalytics> get(Collection<PeriodCostAnalyticsCreateCommand> keys) {
        if (keys.isEmpty()) return Map.of();
        return dslContext.select()
            .from(PERIOD_COSTS_ANALYTICS)
            .where(periodCostAnalyticsCreateCommandConditionBuilder.buildCondition(keys))
            .fetchMap(periodCostAnalyticsCreateCommandMapper, periodCostsAnalyticsMapper);
    }

    @Override
    public Map<PeriodCostAnalyticsCreateCommand, PeriodCostsAnalytics> create(Collection<PeriodCostAnalyticsCreateCommand> keys) {
        if (keys.isEmpty()) return Map.of();
        return dslContext.insertInto(PERIOD_COSTS_ANALYTICS)
            .set(periodCostAnalyticsCreateCommandMapper.toRecords(keys))
            .returning(
                PERIOD_COSTS_ANALYTICS.ID,
                PERIOD_COSTS_ANALYTICS.PERIOD,
                PERIOD_COSTS_ANALYTICS.USER_ID
            )
            .fetchMap(periodCostAnalyticsCreateCommandMapper, periodCostsAnalyticsMapper);
    }
}