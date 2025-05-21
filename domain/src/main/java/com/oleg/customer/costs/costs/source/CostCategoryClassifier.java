package com.oleg.customer.costs.costs.source;

import com.oleg.customer.costs.costs.value_object.CostsCategory;

import java.util.Map;
import java.util.Collection;

public interface CostCategoryClassifier {

    Map<Integer, Double[]> classify(Collection<CostsCategory> costsCategories);
}