package com.oleg.customer.costs;

import com.oleg.customer.costs.costs.source.CustomerCostsCategoryClassifier;
import com.oleg.customer.costs.costs.query.CreateCustomerCostsCmd;
import com.oleg.customer.costs.costs.source.GetCostsCategorySource;
import com.oleg.customer.costs.costs.value_object.CostsCategory;
import com.openai.client.OpenAIClient;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.services.blocking.chat.ChatCompletionService;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.openai.models.ChatModel.*;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class OpenAICustomerCostsCategoryClassifier implements CustomerCostsCategoryClassifier {

    private static final Logger logger = getLogger(OpenAICustomerCostsCategoryClassifier.class);

    private final List<CostsCategory> costsCategories;
    private final ChatCompletionService completionService;

    public OpenAICustomerCostsCategoryClassifier(OpenAIClient openAIClient,
                                                 GetCostsCategorySource getCostsCategorySource) {
        this.completionService = openAIClient.chat().completions();
        this.costsCategories = getCostsCategorySource.getAll();
    }

    @Override
    public Integer classify(CreateCustomerCostsCmd cmd) {
        StringBuilder stringBuilder = new StringBuilder("У мене є картка категорій у форматі индентіфкатор, опис: ");
        costsCategories.forEach(costsCategory ->
                stringBuilder.append(costsCategory.id())
                    .append(", ")
                    .append(costsCategory.description())
                    .append("; ")
        );

        stringBuilder.append("Вибери з усіх вказаних описів найбільш підходящим для опису транзакції - ");
        stringBuilder.append(cmd.description());
        stringBuilder.append("І поверни лише її индентіфкатор без слів");

        ChatCompletionCreateParams request = ChatCompletionCreateParams.builder()
            .model(GPT_4O_MINI)
            .addDeveloperMessage("Ти помічник-класифікатор транзакцій.")
            .addUserMessage(stringBuilder.toString())
            .build();

        return completionService.create(request)
            .choices()
            .stream()
            .flatMap(choice -> choice.message()
                .content()
                .stream()
            )
            .map(choice -> {
                try {
                    return Integer.parseInt(choice);
                } catch (Exception e) {
                    logger.error("Can't parse choice: {}", choice);
                    return 11;
                }
            })
            .findFirst()
            .orElse(11);
    }
}
