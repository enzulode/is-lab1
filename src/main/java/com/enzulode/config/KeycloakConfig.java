package com.enzulode.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

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
}
