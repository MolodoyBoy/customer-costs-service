package com.oleg.customer.costs.listener;

import com.oleg.customer.costs.analytics.core.Publisher;
import com.oleg.customer.costs.analytics.customer_costs.entity.CustomerCosts;
import com.oleg.customer.costs.analytics.period_costs.message.PeriodCostsAnalyticsMessage;
import com.oleg.customer.costs.data.CustomerCostsData;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
class CustomerCostsListener {

    private final Publisher publisher;

    CustomerCostsListener(Publisher publisher) {
        this.publisher = publisher;
    }

    @KafkaListener(
        clientIdPrefix = "${oleg.kafka.customer-costs.id}",
        groupId = "${oleg.kafka.customer-costs.consumer.group.id}",
        topics = "${oleg.kafka.customer-costs.topic}",
        errorHandler = "kafkaListenerErrorHandler",
        batch = "true",
        containerFactory = "customerCostsListenerFactory"
    )
    public void listenCustomerCosts(@Payload ConsumerRecords<Integer, CustomerCostsData> consumerRecords) {
        List<CustomerCosts> customerCosts = new ArrayList<>(consumerRecords.count());

        consumerRecords.forEach(c -> {
            CustomerCostsData data = c.value();
            customerCosts.add(convert(data));
        });

        publisher.publishMessage(new PeriodCostsAnalyticsMessage(customerCosts));
    }

    private CustomerCosts convert(CustomerCostsData data) {
        return new CustomerCosts(
            data.id(),
            data.userId(),
            data.categoryId(),
            data.amount(),
            data.description(),
            data.createdAt()
        );
    }
}