package com.oleg.customer.costs.costs.categorizer;

import com.oleg.customer.costs.costs.query.CreateCustomerCostsCmd;
import com.oleg.customer.costs.costs.value_object.CustomerCosts;
import com.oleg.customer.costs.costs.source.CustomerCostsClassifier;
import com.oleg.customer.costs.costs.source.GetCostsCategorySource;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CustomerCostsCategorizer {

    private static final double THRESHOLD = 0.75;
    private static final int OTHER_CATEGORY_ID = 11;

    private final GetCostsCategorySource getCostsCategorySource;
    private final CustomerCostsClassifier customerCostsClassifier;

    public CustomerCostsCategorizer(GetCostsCategorySource getCostsCategorySource, CustomerCostsClassifier customerCostsClassifier) {
        this.getCostsCategorySource = getCostsCategorySource;
        this.customerCostsClassifier = customerCostsClassifier;
    }

    public CustomerCosts categorize(int userId, CreateCustomerCostsCmd cmd) {
        Double[] costsEmbeddings = customerCostsClassifier.classify(cmd);
        Map<Integer, Double[]> categoryEmbeddings = getCostsCategorySource.getAllEmbeddings();

        double bestScore = -1.0;
        int bestCategoryId = OTHER_CATEGORY_ID;

        for (Map.Entry<Integer, Double[]> categoryEmbedding : categoryEmbeddings.entrySet()) {
            double score = cosineSimilarity(costsEmbeddings, categoryEmbedding.getValue());
            if (score > bestScore) {
                bestScore = score;
                bestCategoryId = categoryEmbedding.getKey();
            }
        }

        if (bestScore < THRESHOLD) {
            bestCategoryId = OTHER_CATEGORY_ID;
        }

        return new CustomerCosts(
            -1,
            userId,
            cmd.bankId(),
            bestCategoryId,
            cmd.amount(),
            cmd.description(),
            cmd.createdAt()
        );
    }

    double cosineSimilarity(Double[] a, Double[] b) {
        double dot = 0, normA = 0, normB = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
