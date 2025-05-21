package com.oleg.customer.costs.costs.source;

import com.oleg.customer.costs.costs.value_object.CostsCategory;

import java.util.List;
import java.util.Map;

public interface GetCostsCategorySource {

    List<CostsCategory> getAll();

    Map<Integer, Double[]> getAllEmbeddings();
}
