package com.oleg.customer.costs.common;

import static java.lang.String.format;

public class Paginator {

    private final int page;
    private final int limit = 25;

    public Paginator(int page) {
        if (page < 1) {
            throw new IllegalArgumentException(format("Not valid page. {page = %d}", page));
        }

        this.page = page;
    }

    public int skip() {
        return (page - 1) * limit;
    }

    public int limit() {
        return limit;
    }
}
