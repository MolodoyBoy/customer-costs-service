package com.oleg.customer.costs.analytics.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Component
class MessagePublisher implements Publisher {

    private static final Logger logger = LoggerFactory.getLogger(MessagePublisher.class);

    private final Map<Class<? extends Message>, List<Subscriber>> subscribers;

    MessagePublisher(List<Subscriber> subscribers) {
        this.subscribers = subscribers.stream()
            .collect(groupingBy(Subscriber::getSupportedMessage));
    }

    @Override
    public void publishMessage(Message message) {
        if (message == null) return;

        try {
            notifySubscribers(message);
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    private void notifySubscribers(Message message) {
        if (message == null) return;

        subscribers.get(message.getClass())
            .forEach(subscriber -> subscriber.onMessage(message));
    }
}