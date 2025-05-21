package com.oleg.customer.costs.costs;

import com.oleg.customer.costs.category.ManageCostsCategorySource;
import com.oleg.customer.costs.costs.mapper.CostsCategoryMapper;
import com.oleg.customer.costs.costs.source.GetCostsCategorySource;
import com.oleg.customer.costs.costs.value_object.CostsCategory;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static com.oleg.fund.customer.costs.analytics.tables.CostsCategory.COSTS_CATEGORY;
import static com.oleg.fund.customer.costs.analytics.tables.CostsCategoryEmbedding.COSTS_CATEGORY_EMBEDDING;

@Repository
public class DbCostsCategorySource implements GetCostsCategorySource, ManageCostsCategorySource {

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

    @Override
    public Map<Integer, Double[]> getAllEmbeddings() {
        return dslContext.select()
            .from(COSTS_CATEGORY_EMBEDDING)
            .fetchMap(COSTS_CATEGORY_EMBEDDING.CATEGORY_ID, COSTS_CATEGORY_EMBEDDING.EMBEDDING);
    }

    @Override
    public void saveEmbeddings(Map<Integer, Double[]> embeddings) {
        dslContext.insertInto(COSTS_CATEGORY_EMBEDDING)
            .set(toRecords(embeddings))
            .onConflict(COSTS_CATEGORY_EMBEDDING.CATEGORY_ID)
            .doUpdate()
            .setAllToExcluded()
            .execute();
    }

    private List<Record> toRecords(Map<Integer, Double[]> embeddings) {
        return embeddings.entrySet()
            .stream()
            .map(this::toRecord)
            .toList();
    }

    private Record toRecord(Map.Entry<Integer, Double[]> entry) {
        Record rc = dslContext.newRecord(COSTS_CATEGORY_EMBEDDING);
        rc.set(COSTS_CATEGORY_EMBEDDING.CATEGORY_ID, entry.getKey());
        rc.set(COSTS_CATEGORY_EMBEDDING.EMBEDDING, entry.getValue());

        return rc;
    }
}