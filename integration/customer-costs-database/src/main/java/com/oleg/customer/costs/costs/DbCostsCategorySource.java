package com.oleg.customer.costs.costs;

import com.oleg.customer.costs.costs.mapper.CostsCategoryMapper;
import com.oleg.customer.costs.costs.source.GetCostsCategorySource;
import com.oleg.customer.costs.costs.value_object.CostsCategory;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.oleg.fund.customer.costs.analytics.tables.CostsCategory.COSTS_CATEGORY;

@Repository
public class DbCostsCategorySource implements GetCostsCategorySource {

    private final DSLContext dslContext;
    private final CostsCategoryMapper costsCategoryMapper;

    public DbCostsCategorySource(DSLContext dslContext,
                                 CostsCategoryMapper costsCategoryMapper) {
        this.dslContext = dslContext;
        this.costsCategoryMapper = costsCategoryMapper;
    }

    @Override
    public List<CostsCategory> getAll() {
        return dslContext.select()
            .from(COSTS_CATEGORY)
            .fetch(costsCategoryMapper);
    }
}