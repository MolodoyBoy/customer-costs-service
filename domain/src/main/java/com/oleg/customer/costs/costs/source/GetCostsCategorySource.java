package com.oleg.customer.costs.costs.source;

import com.oleg.customer.costs.costs.value_object.CostsCategory;

import java.util.List;

public interface GetCostsCategorySource {

    List<CostsCategory> getAll();
}
