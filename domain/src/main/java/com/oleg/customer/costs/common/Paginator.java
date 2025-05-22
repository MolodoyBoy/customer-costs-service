package com.oleg.customer.costs.common;

import static java.lang.String.format;

public class Paginator {

    private final int page;
    private final int limit;

    public Paginator(int page, int pageSize) {
        if (page < 1) {
            throw new IllegalArgumentException(format("Not valid page. {page = %d}", page));
        }

        this.page = page;
        this.limit = pageSize;
    }

    public int skip() {
        return (page - 1) * limit;
    }

    public int limit() {
        return limit;
    }
}
