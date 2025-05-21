package com.oleg.customer.costs.user_management.service;

import com.oleg.customer.costs.user_management.source.UserLoginService;
import com.oleg.customer.costs.user_management.value_object.LoginUserCommand;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
class UserLoginServiceImpl implements UserLoginService {

    private static final String TOKEN_TYPE = "Bearer ";

    private final HttpServletResponse response;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final AuthenticationManager authenticationManager;

    UserLoginServiceImpl(HttpServletResponse response,
                                JwtTokenGenerator jwtTokenGenerator,
                                AuthenticationManager authenticationManager) {
        this.response = response;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void login(LoginUserCommand command) {
        try {
            Authentication authentication = getAuthentication(command);
            Authentication authenticate = authenticationManager.authenticate(authentication);
            String jwtToken = jwtTokenGenerator.generateToken(authenticate);

            response.addHeader(AUTHORIZATION, TOKEN_TYPE + jwtToken);
        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException("Invalid username or password!");
        }
    }

    private Authentication getAuthentication(LoginUserCommand command) {
        return new UsernamePasswordAuthenticationToken(command.username(), command.password());
    }
}