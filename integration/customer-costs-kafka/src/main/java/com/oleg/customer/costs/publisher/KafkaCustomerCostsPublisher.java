package com.oleg.customer.costs.publisher;

import com.oleg.customer.costs.costs.value_object.CustomerCosts;
import com.oleg.customer.costs.data.CustomerCostsData;
import com.oleg.customer.costs.out_box.CustomerCostsPublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.slf4j.LoggerFactory.getLogger;
import static java.util.concurrent.CompletableFuture.allOf;

@Component
class KafkaCustomerCostsPublisher implements CustomerCostsPublisher {

    private static final Logger logger = getLogger(KafkaCustomerCostsPublisher.class);

    private final String topic;
    private final KafkaTemplate<Integer, CustomerCostsData> kafkaTemplate;

    KafkaCustomerCostsPublisher(KafkaTemplate<Integer, CustomerCostsData> kafkaTemplate,
                                @Value("${oleg.kafka.customer-costs.topic}") String topic) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(List<CustomerCosts> customerCosts, Runnable onCompleteAction) {
        logger.info("Customer costs publishing started.");

        int counter = 0;
        CompletableFuture<?>[] all = new CompletableFuture<?>[customerCosts.size()];

        for (CustomerCosts customerCost : customerCosts) {

            all[counter++] = kafkaTemplate.send(topic, customerCost.userId(), convert(customerCost));
        }

        postAction(onCompleteAction, allOf(all));

        logger.info("Customer costs publishing finished.");
    }

    private CustomerCostsData convert(CustomerCosts customerCosts) {
        return new CustomerCostsData(
            customerCosts.id(),
            customerCosts.userId(),
            customerCosts.categoryId(),
            customerCosts.amount(),
            customerCosts.description(),
            customerCosts.createdAt()
        );
    }

    private void postAction(Runnable action, CompletableFuture<Void> result) {
        result.thenRun(action)
            .exceptionally(ex -> {
                logger.error(ex.getMessage(), ex);
                return null;
            });
    }
}