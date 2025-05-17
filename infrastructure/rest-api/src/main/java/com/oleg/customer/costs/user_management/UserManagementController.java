package com.oleg.customer.costs.user_management;

import com.oleg.customer.costs.api.UserManagementApi;
import com.oleg.customer.costs.model.LoginUserDto;
import com.oleg.customer.costs.model.UserRegistrationDto;
import com.oleg.customer.costs.user_management.source.UserLoginService;
import com.oleg.customer.costs.user_management.source.UserRegistrationService;
import com.oleg.customer.costs.user_management.value_object.LoginUserCommand;
import com.oleg.customer.costs.user_management.value_object.RegisterUserCommand;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserManagementController implements UserManagementApi {

    private final UserLoginService userLoginService;
    private final UserRegistrationService userRegistrationService;

    public UserManagementController(UserLoginService userLoginService,
                                    UserRegistrationService userRegistrationService) {
        this.userLoginService = userLoginService;
        this.userRegistrationService = userRegistrationService;
    }

    @Override
    public void login(LoginUserDto dto) {
        LoginUserCommand command = convert(dto);
        userLoginService.login(command);
    }

    @Override
    public void registration(UserRegistrationDto dto) {
        RegisterUserCommand command = convert(dto);
        userRegistrationService.register(command);
    }

    private LoginUserCommand convert(LoginUserDto dto) {
        return new LoginUserCommand(dto.getUsername(), dto.getPassword());
    }

    private RegisterUserCommand convert(UserRegistrationDto dto) {
        return new RegisterUserCommand(dto.getEmail(), dto.getUsername(), dto.getPassword());
    }
}