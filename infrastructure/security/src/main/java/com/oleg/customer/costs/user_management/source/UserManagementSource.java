package com.oleg.customer.costs.user_management.source;

import com.oleg.customer.costs.user_management.value_object.IdentifiedUser;
import com.oleg.customer.costs.user_management.value_object.RegisterUserCommand;

public interface UserManagementSource {

    Integer save(RegisterUserCommand command);

    IdentifiedUser loadUserByUsername(String username);
}