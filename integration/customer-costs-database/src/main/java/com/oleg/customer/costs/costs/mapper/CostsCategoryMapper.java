package com.oleg.customer.costs.costs.mapper;

import com.oleg.customer.costs.costs.value_object.CostsCategory;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import static com.oleg.fund.customer.costs.analytics.tables.CostsCategory.COSTS_CATEGORY;

@Component
public class CostsCategoryMapper implements RecordMapper<Record, CostsCategory> {

    @Override
    public CostsCategory map(Record rc) {
        return new CostsCategory(
            rc.get(COSTS_CATEGORY.ID),
            rc.get(COSTS_CATEGORY.DESCRIPTION)
        );
    }
}
