package com.oleg.customer.costs.job;

import com.oleg.customer.costs.source.CostsCategoryPublisher;
import com.oleg.customer.costs.costs.source.GetCostsCategorySource;
import com.oleg.customer.costs.costs.value_object.CostsCategory;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CostsCategoryJob {

    private final CostsCategoryPublisher costsCategoryPublisher;
    private final GetCostsCategorySource getCostsCategorySource;

    public CostsCategoryJob(CostsCategoryPublisher costsCategoryPublisher, GetCostsCategorySource getCostsCategorySource) {
        this.costsCategoryPublisher = costsCategoryPublisher;
        this.getCostsCategorySource = getCostsCategorySource;
    }

    @PostConstruct
    void runJob() {
        List<CostsCategory> allCostsCategory = getCostsCategorySource.getAll();
        costsCategoryPublisher.publish(allCostsCategory);
    }
}