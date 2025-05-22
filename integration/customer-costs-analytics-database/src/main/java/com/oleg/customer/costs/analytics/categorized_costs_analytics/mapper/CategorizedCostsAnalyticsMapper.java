package com.oleg.customer.costs.analytics.categorized_costs_analytics.mapper;

import com.oleg.customer.costs.analytics.categorized_costs.entity.CategorizedCostsAnalytics;
import com.oleg.customer.costs.analytics.categorized_costs.snapshot.CategorizedCostsAnalyticsSnapshot;
import com.oleg.customer.costs.analytics.common.mapper.ToRecordMapper;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import static com.oleg.customer.costs.analytics.common.mapper.UtilMapper.getNullableField;
import static com.oleg.fund.customer.costs.analytics.tables.CategorizedCostsAnalytics.CATEGORIZED_COSTS_ANALYTICS;
import static com.oleg.fund.customer.costs.analytics.tables.CostsCategory.COSTS_CATEGORY;

@Component
public class CategorizedCostsAnalyticsMapper implements ToRecordMapper<CategorizedCostsAnalytics>, RecordMapper<Record, CategorizedCostsAnalytics> {

    private final DSLContext dslContext;

    public CategorizedCostsAnalyticsMapper(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public CategorizedCostsAnalytics map(Record rc) {
        return new CategorizedCostsAnalytics(
            rc.get(CATEGORIZED_COSTS_ANALYTICS.ID),
            rc.get(CATEGORIZED_COSTS_ANALYTICS.CATEGORY_ID),
            getNullableField(rc, CATEGORIZED_COSTS_ANALYTICS.AMOUNT),
            getNullableField(rc, CATEGORIZED_COSTS_ANALYTICS.PERCENT),
            getNullableField(rc, CATEGORIZED_COSTS_ANALYTICS.TRANSACTIONS_COUNT),
            getNullableField(rc, COSTS_CATEGORY.DESCRIPTION)
        );
    }

    @Override
    public Record toRecord(CategorizedCostsAnalytics value) {
        CategorizedCostsAnalyticsSnapshot snapshot = value.toSnapshot();

        Record rc = dslContext.newRecord(CATEGORIZED_COSTS_ANALYTICS);
        rc.set(CATEGORIZED_COSTS_ANALYTICS.ID, snapshot.id());
        rc.set(CATEGORIZED_COSTS_ANALYTICS.AMOUNT, snapshot.amount());
        rc.set(CATEGORIZED_COSTS_ANALYTICS.PERCENT, snapshot.percent());
        rc.set(CATEGORIZED_COSTS_ANALYTICS.CATEGORY_ID, snapshot.categoryId());
        rc.set(CATEGORIZED_COSTS_ANALYTICS.TRANSACTIONS_COUNT, snapshot.transactionsCount());

        return rc;
    }
}