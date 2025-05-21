package com.oleg.customer.costs.config;

import com.oleg.customer.costs.user_management.UserContext;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;

class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        long userId = jwt.getClaim("userId");
        UserContext userContext = new UserContext(
            (int) userId,
            jwt.getClaim("username")
        );

        JwtAuthenticationToken token = new JwtAuthenticationToken(jwt, List.of(), userContext.username());
        token.setDetails(userContext);

        return token;
    }
}