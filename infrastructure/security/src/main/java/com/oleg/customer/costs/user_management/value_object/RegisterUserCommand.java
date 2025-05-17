package com.oleg.customer.costs.user_management.value_object;

public record RegisterUserCommand(String username, String password, String email) {

    public LoginUserCommand toLoginUserCommand() {
        return new LoginUserCommand(username, password);
    }
}
