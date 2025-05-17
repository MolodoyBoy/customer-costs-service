package com.oleg.customer.costs.generator;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DescriptionMappings {

    private final List<String> descriptionTemplates;

    public DescriptionMappings() {
        this.descriptionTemplates = List.of(
            "Payment for %s",
            "Purchase at %s",
            "Order from %s",
            "Subscription: %s",
            "Service fee: %s"
        );
    }

    public int size() {
        return descriptionTemplates.size();
    }

    public String getDescription(int index) {
        return descriptionTemplates.get(index);
    }
}
