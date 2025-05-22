package com.oleg.customer.costs.analytics.categorized_costs_analytics.mapper;

import com.oleg.customer.costs.analytics.categorized_costs.snapshot.CategorizedCostsAnalyticsSnapshot;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import static com.oleg.customer.costs.analytics.common.mapper.UtilMapper.getNullableField;
import static com.oleg.fund.customer.costs.analytics.tables.CategorizedCostsAnalytics.CATEGORIZED_COSTS_ANALYTICS;
import static com.oleg.fund.customer.costs.analytics.tables.CostsCategory.COSTS_CATEGORY;

@Component
public class CategorizedCostsAnalyticsSnapshotMapper implements RecordMapper<Record, CategorizedCostsAnalyticsSnapshot> {

    @Override
    public CategorizedCostsAnalyticsSnapshot map(Record rc) {
        return new CategorizedCostsAnalyticsSnapshot(
            rc.get(CATEGORIZED_COSTS_ANALYTICS.ID),
            rc.get(CATEGORIZED_COSTS_ANALYTICS.CATEGORY_ID),
            getNullableField(rc, CATEGORIZED_COSTS_ANALYTICS.AMOUNT),
            getNullableField(rc, CATEGORIZED_COSTS_ANALYTICS.PERCENT),
            getNullableField(rc, CATEGORIZED_COSTS_ANALYTICS.TRANSACTIONS_COUNT),
            getNullableField(rc, COSTS_CATEGORY.DESCRIPTION)
        );
    }
}
