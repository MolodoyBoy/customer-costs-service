package com.oleg.customer.costs.user_management.service;

import com.oleg.customer.costs.user_management.entity.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Component;

import static com.nimbusds.jose.JOSEObjectType.JWT;
import static org.springframework.security.oauth2.jose.jws.MacAlgorithm.HS256;
import static org.springframework.security.oauth2.jwt.JwtEncoderParameters.from;

@Component
class JwtTokenGenerator {

    private final JwtEncoder jwtEncoder;

    JwtTokenGenerator(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(Authentication authentication) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        JwsHeader header = JwsHeader.with(HS256)
            .type(JWT.getType())
            .build();

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .subject(user.getUsername())
            .claim("userId", user.id())
            .claim("username", user.getUsername())
            .build();

        return jwtEncoder.encode(from(header, claims)).getTokenValue();
    }
}