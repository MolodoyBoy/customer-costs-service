package com.oleg.customer.costs.costs.source;

import com.oleg.customer.costs.costs.query.CreateCustomerCostsCmd;

public interface CustomerCostsClassifier {

    Double[] classify(CreateCustomerCostsCmd cmd);
}
