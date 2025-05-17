package com.oleg.customer.costs.user_management;

public class UserContext {

    private final int id;
    private final String username;

    public UserContext(int id, String username) {
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
