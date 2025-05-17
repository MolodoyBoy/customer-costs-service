package com.oleg.customer.costs.source;

import java.util.Collection;
import java.util.Map;

public interface CustomerCostsEventSource {

    void delete(Collection<Long> ids);

    Map<Long, Integer> getEvents(int limit);

    void insert(Collection<Integer> customerCostsIds);
}
