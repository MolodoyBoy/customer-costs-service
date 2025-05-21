package com.oleg.customer.costs;

import com.oleg.customer.costs.costs.value_object.CostsCategory;
import com.oleg.customer.costs.costs.source.CostCategoryClassifier;
import com.openai.client.OpenAIClient;
import com.openai.models.embeddings.CreateEmbeddingResponse;
import com.openai.models.embeddings.Embedding;
import com.openai.models.embeddings.EmbeddingCreateParams;
import com.openai.services.blocking.EmbeddingService;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.Collection;

import static java.util.Comparator.comparing;
import static com.openai.models.embeddings.EmbeddingCreateParams.*;
import static com.openai.models.embeddings.EmbeddingCreateParams.Input.*;

@Component
public class OpenAICostCategoryClassifier implements CostCategoryClassifier {

    private final EmbeddingService embeddingService;

    public OpenAICostCategoryClassifier(OpenAIClient openAIClient) {
        this.embeddingService = openAIClient.embeddings();
    }

    @Override
    public Map<Integer, Double[]> classify(Collection<CostsCategory> costsCategories) {
        List<String> inputs = costsCategories.stream()
            .sorted(comparing(CostsCategory::id))
            .map(CostsCategory::description)
            .toList();

        EmbeddingCreateParams params = builder()
            .model("text-embedding-3-small")
            .input(ofArrayOfStrings(inputs))
            .build();

        CreateEmbeddingResponse response = embeddingService.create(params);

        Map<Integer, Double[]> result = new LinkedHashMap<>();
        List<Embedding> dataList = response.data();
        int idx = 0;

        for (CostsCategory key : costsCategories) {
            result.put(key.id(), dataList.get(idx++).embedding().toArray(Double[]::new));
        }

        return result;
    }
}