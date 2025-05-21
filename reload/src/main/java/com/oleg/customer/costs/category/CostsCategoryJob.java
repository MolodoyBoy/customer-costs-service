package com.oleg.customer.costs.category;

import com.oleg.customer.costs.costs.source.CostCategoryClassifier;
import com.oleg.customer.costs.costs.source.GetCostsCategorySource;
import com.oleg.customer.costs.source.CostsCategoryPublisher;
import com.oleg.customer.costs.costs.value_object.CostsCategory;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.List;

@Component
public class CostsCategoryJob {

    private final CostCategoryClassifier costCategoryClassifier;
    private final CostsCategoryPublisher costsCategoryPublisher;
    private final GetCostsCategorySource getCostsCategorySource;
    private final ManageCostsCategorySource manageCostsCategorySource;

    public CostsCategoryJob(CostCategoryClassifier costCategoryClassifier,
                            CostsCategoryPublisher costsCategoryPublisher,
                            GetCostsCategorySource getCostsCategorySource,
                            ManageCostsCategorySource manageCostsCategorySource) {
        this.costCategoryClassifier = costCategoryClassifier;
        this.costsCategoryPublisher = costsCategoryPublisher;
        this.getCostsCategorySource = getCostsCategorySource;
        this.manageCostsCategorySource = manageCostsCategorySource;
    }

    @PostConstruct
    void runJob() {
        List<CostsCategory> allCostsCategory = getCostsCategorySource.getAll();
        Map<Integer, Double[]> allEmbeddings = getCostsCategorySource.getAllEmbeddings();

        List<CostsCategory> newCategories = allCostsCategory.stream()
            .filter(c -> !allEmbeddings.containsKey(c.id()))
            .toList();

        if (!newCategories.isEmpty()) {
            Map<Integer, Double[]> newEmbeddings = costCategoryClassifier.classify(newCategories);
            manageCostsCategorySource.saveEmbeddings(newEmbeddings);
        }

        costsCategoryPublisher.publish(allCostsCategory);
    }
}