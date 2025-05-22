package com.oleg.customer.costs.analytics.user;

public class AnalyticsUserContext {

    private final int id;
    private final String username;

    public AnalyticsUserContext(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int id() {
        return id;
    }

    public String username() {
        return username;
    }
}
