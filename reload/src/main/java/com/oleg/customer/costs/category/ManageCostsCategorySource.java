package com.oleg.customer.costs.category;

import java.util.Map;

public interface ManageCostsCategorySource {

    void saveEmbeddings(Map<Integer, Double[]> embeddings);
}
