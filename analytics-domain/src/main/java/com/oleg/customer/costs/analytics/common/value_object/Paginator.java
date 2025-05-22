package com.oleg.customer.costs.analytics.common.value_object;

import static java.lang.String.format;

public class Paginator {

    private final int page;
    private final int limit;

    public Paginator(int limit) {
        this(0, limit);
    }

    public Paginator(int page, int limit) {
        if (page < 1 || limit < 1) {
            throw new IllegalArgumentException(format("Not valid page. {page = %d, pageSize = %d}", page, limit));
        }

        this.page = page;
        this.limit = limit;
    }

    public int skip() {
        return (page - 1) * limit;
    }

    public int limit() {
        return limit;
    }
}