package com.oleg.customer.costs.user_management.source;

import com.oleg.customer.costs.user_management.value_object.RegisterUserCommand;

public interface UserRegistrationService {

    void register(RegisterUserCommand token);
}
