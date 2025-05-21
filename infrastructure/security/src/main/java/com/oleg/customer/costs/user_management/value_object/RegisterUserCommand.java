package com.oleg.customer.costs.user_management.value_object;

public record RegisterUserCommand(String email, String username, String password) {

    public LoginUserCommand toLoginUserCommand() {
        return new LoginUserCommand(username, password);
    }
}
