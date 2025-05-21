package com.oleg.customer.costs.out_box;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Component
@ConditionalOnProperty(value = "oleg.outbox.enable", havingValue = "true")
public class EventPullingScheduledTask {

    private final CustomerCostsEventSource customerCostsEventSource;
    private final CustomerCostsEventProcessor customerCostsEventProcessor;

    public EventPullingScheduledTask(CustomerCostsEventSource customerCostsEventSource,
                                     CustomerCostsEventProcessor customerCostsEventProcessor) {
        this.customerCostsEventSource = customerCostsEventSource;
        this.customerCostsEventProcessor = customerCostsEventProcessor;
    }

    @Scheduled(
        fixedDelay = 200,
        initialDelay = 3000,
        timeUnit = MILLISECONDS
    )
    public void pullEvents() {
        Map<Long, Integer> events = customerCostsEventSource.getEvents(600);
        customerCostsEventProcessor.processEvents(events);
    }
}