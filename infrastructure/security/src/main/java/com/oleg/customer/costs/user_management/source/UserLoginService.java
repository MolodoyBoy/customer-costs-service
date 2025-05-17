package com.oleg.customer.costs.user_management.source;

import com.oleg.customer.costs.user_management.value_object.LoginUserCommand;

public interface UserLoginService {

    void login(LoginUserCommand command);
}
