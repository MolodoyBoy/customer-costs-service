package com.oleg.customer.costs.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.proc.SecurityContext;
import com.oleg.customer.costs.analytics.user.AnalyticsUserContext;
import com.oleg.customer.costs.user_management.UserContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Configuration
public class SecurityConfig {

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        return new JwtAuthenticationConverter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @RequestScope
    public UserContext userContext() {
        var auth = getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            return (UserContext) jwtAuth.getDetails();
        } else {
            throw new BadCredentialsException("Token not found!");
        }
    }

    @Bean
    @RequestScope
    public AnalyticsUserContext analyticsUserContext() {
        var auth = getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            UserContext details = (UserContext) jwtAuth.getDetails();
            return new AnalyticsUserContext(details.id(), details.username());
        } else {
            throw new BadCredentialsException("Token not found!");
        }
    }

    @Bean
    public SecretKey secretKey(@Value("${spring.security.oauth2.resourceserver.jwt.secret}") String jwtSecret,
                               @Value("${spring.security.oauth2.resourceserver.jwt.jws-algorithms}") String jwtAlgorithm) {
        return new SecretKeySpec(jwtSecret.getBytes(), jwtAlgorithm);
    }

    @Bean
    public JwtDecoder jwtDecoder(SecretKey secretKey) {
        return NimbusJwtDecoder.withSecretKey(secretKey)
            .macAlgorithm(MacAlgorithm.HS256)
            .build();
    }

    @Bean
    public JwtEncoder jwtEncoder(SecretKey secretKey) {
        OctetSequenceKey jwk = new OctetSequenceKey.Builder(secretKey)
            .algorithm(JWSAlgorithm.HS256)
            .keyID("hs256-key")
            .build();

        ImmutableJWKSet<SecurityContext> jwkSet = new ImmutableJWKSet<>(new JWKSet(jwk));

        return new NimbusJwtEncoder(jwkSet);
    }

    @Bean
    @SuppressWarnings("java:S4502")
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           JwtDecoder jwtDecoder,
                                           JwtAuthenticationConverter jwtAuthenticationConverter) throws Exception {
        return http.cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(sm -> sm.sessionCreationPolicy(STATELESS))
            .authorizeHttpRequests(auth ->
                auth.requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/monobank/**",
                        "/swagger-ui.html/**"
                    ).permitAll()
                    .requestMatchers("/user/**").permitAll()
                    .anyRequest().authenticated()
            ).oauth2ResourceServer(r ->
                r.jwt(jwtConfigurer -> jwtConfigurer
                    .decoder(jwtDecoder)
                    .jwtAuthenticationConverter(jwtAuthenticationConverter)
                )
            ).build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOriginPatterns(List.of("*"));
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(List.of("*"));
        corsConfig.addExposedHeader("Authorization");
        corsConfig.setAllowCredentials(false);
        corsConfig.setMaxAge(3600L);

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}