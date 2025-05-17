package com.oleg.customer.costs.publisher;

import com.oleg.customer.costs.costs.value_object.CostsCategory;
import com.oleg.customer.costs.data.CostCategoryData;
import com.oleg.customer.costs.source.CostsCategoryPublisher;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Component
class KafkaCostsCategoryPublisher implements CostsCategoryPublisher {

    private static final Logger logger = getLogger(KafkaCostsCategoryPublisher.class);

    private final String topic;
    private final KafkaTemplate<Integer, CostCategoryData> kafkaTemplate;

    KafkaCostsCategoryPublisher(KafkaTemplate<Integer, CostCategoryData> kafkaTemplate,
                                @Value("${oleg.kafka.costs-category.topic}") String topic) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(List<CostsCategory> costsCategories) {
        logger.info("Costs category publishing started.");

        costsCategories.forEach(costsCategory ->
            kafkaTemplate.send(topic, costsCategory.id(), convert(costsCategory))
        );

        logger.info("Costs category publishing finished.");
    }

    private CostCategoryData convert(CostsCategory costsCategory) {
        return new CostCategoryData(
            costsCategory.id(),
            costsCategory.description()
        );
    }
}