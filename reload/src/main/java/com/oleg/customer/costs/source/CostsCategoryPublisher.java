package com.oleg.customer.costs.source;

import com.oleg.customer.costs.costs.value_object.CostsCategory;

import java.util.List;

public interface CostsCategoryPublisher {

    void publish(List<CostsCategory> costsCategories);
}
