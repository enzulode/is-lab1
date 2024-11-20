package com.enzulode.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class KeycloakConfig {

  @Bean
  public SecurityFilterChain securityFilterChainConfig(
      HttpSecurity http, Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter)
      throws Exception {
    // formatter:off
    http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            httpRequests -> httpRequests
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/api/v1/ws").permitAll()
                .anyRequest().authenticated()
        )
        .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(STATELESS))
        .oauth2ResourceServer(
            resourceServer -> resourceServer.jwt(
                jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter)
            )
        );
    // formatter:on

    return http.build();
  }

  @Bean
  public Keycloak keycloakAdminClient(
      @Value("${oauth2.serverBaseUrl}") String serverUrl,
      @Value("${oauth2.realm}") String realm,
      @Value("${oauth2.adminClientId}") String adminClientId,
      @Value("${oauth2.adminClientSecret}") String adminClientSecret) {
    return KeycloakBuilder.builder()
        .serverUrl(serverUrl)
        .realm(realm)
        .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
        .clientId(adminClientId)
        .clientSecret(adminClientSecret)
        .build();
  }
}
