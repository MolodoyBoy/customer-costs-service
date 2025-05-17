package com.oleg.customer.costs.user_management.service;

import com.oleg.customer.costs.user_management.source.UserRegistrationService;
import com.oleg.customer.costs.user_management.source.UserManagementSource;
import com.oleg.customer.costs.user_management.source.UserLoginService;
import com.oleg.customer.costs.user_management.value_object.RegisterUserCommand;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
class UserRegistrationServiceImpl implements UserRegistrationService {

    private static final int MIN_CREDENTIALS_LENGTH = 10;
    private static final int MAX_CREDENTIALS_LENGTH = 30;

    private final PasswordEncoder passwordEncoder;
    private final UserLoginService userLoginService;
    private final UserManagementSource userManagementSource;

    UserRegistrationServiceImpl(PasswordEncoder passwordEncoder,
                                UserLoginService userLoginService,
                                UserManagementSource userManagementSource) {
        this.passwordEncoder = passwordEncoder;
        this.userLoginService = userLoginService;
        this.userManagementSource = userManagementSource;
    }

    @Override
    public void register(RegisterUserCommand command) {
        checkLength(command.email(), "email");
        checkLength(command.username(), "username");
        checkLength(command.password(), "password");

        RegisterUserCommand encryptedCommand = new RegisterUserCommand(
            command.email(),
            command.username(),
            passwordEncoder.encode(command.password())
        );

        Integer id = userManagementSource.save(encryptedCommand);
        if (id == null) {
            throw new IllegalStateException("User already existed!");
        }

        userLoginService.login(command.toLoginUserCommand());
    }

    private void checkLength(String credential, String credentialName) {
        if (credential.length() < MIN_CREDENTIALS_LENGTH || credential.length() > MAX_CREDENTIALS_LENGTH) {
            throw new IllegalStateException(
                String.format(
                    "Invalid %s length. Must be between %d and %d characters.",
                    credentialName,
                    MIN_CREDENTIALS_LENGTH,
                    MAX_CREDENTIALS_LENGTH
                )
            );
        }
    }
}