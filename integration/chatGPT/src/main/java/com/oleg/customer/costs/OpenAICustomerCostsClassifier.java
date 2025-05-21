package com.oleg.customer.costs;

import com.oleg.customer.costs.costs.source.CustomerCostsClassifier;
import com.oleg.customer.costs.costs.query.CreateCustomerCostsCmd;
import com.openai.client.OpenAIClient;
import com.openai.models.embeddings.CreateEmbeddingResponse;
import com.openai.models.embeddings.Embedding;
import com.openai.models.embeddings.EmbeddingCreateParams;
import com.openai.services.blocking.EmbeddingService;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.openai.models.embeddings.EmbeddingCreateParams.builder;
import static com.openai.models.embeddings.EmbeddingCreateParams.Input.ofString;

@Component
public class OpenAICustomerCostsClassifier implements CustomerCostsClassifier {

    private final EmbeddingService embeddingService;

    public OpenAICustomerCostsClassifier(OpenAIClient openAIClient) {
        this.embeddingService = openAIClient.embeddings();
    }

    @Override
    public Double[] classify(CreateCustomerCostsCmd cmd) {
        EmbeddingCreateParams params = builder()
            .model("text-embedding-3-small")
            .input(ofString(cmd.description()))
            .build();

        CreateEmbeddingResponse response = embeddingService.create(params);

        List<Embedding> dataList = response.data();
        return dataList.getFirst().embedding().toArray(Double[]::new);
    }
}
