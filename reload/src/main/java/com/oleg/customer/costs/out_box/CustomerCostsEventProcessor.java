package com.oleg.customer.costs.out_box;

import com.oleg.customer.costs.costs.source.GetCustomerCostsSource;
import com.oleg.customer.costs.costs.value_object.CustomerCosts;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.HashSet;

import static java.lang.Thread.currentThread;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class CustomerCostsEventProcessor {

    private static final Logger logger = getLogger(CustomerCostsEventProcessor.class);

    private final GetCustomerCostsSource customerCostsSource;
    private final CustomerCostsPublisher customerCostsPublisher;
    private final CustomerCostsEventSource customerCostsEventSource;

    public CustomerCostsEventProcessor(GetCustomerCostsSource customerCostsSource,
                                       CustomerCostsPublisher customerCostsPublisher,
                                       CustomerCostsEventSource customerCostsEventSource) {
        this.customerCostsSource = customerCostsSource;
        this.customerCostsPublisher = customerCostsPublisher;
        this.customerCostsEventSource = customerCostsEventSource;
    }

    public void processEvents(Map<Long, Integer> events) {
        if (events.isEmpty()) return;

        logger.info("Customer costs event processing started...");

        if (currentThread().isInterrupted()) return;

        Set<Integer> ids = new HashSet<>(events.values());
        List<CustomerCosts> customerCosts = customerCostsSource.getCustomerCosts(ids);

        Runnable onCompleteAction = () -> customerCostsEventSource.delete(events.keySet());

        customerCostsPublisher.publish(customerCosts, onCompleteAction);

        logger.info("Customer costs event processing finished.");
    }
}