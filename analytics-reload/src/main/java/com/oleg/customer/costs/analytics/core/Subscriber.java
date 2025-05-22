package com.oleg.customer.costs.analytics.core;

public interface Subscriber {

    void onMessage(Message message);

    Class<? extends Message> getSupportedMessage();
}
