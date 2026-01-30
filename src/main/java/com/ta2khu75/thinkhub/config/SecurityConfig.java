package com.ta2khu75.thinkhub.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import com.ta2khu75.thinkhub.authn.internal.config.JwtProviderFactory;
import com.ta2khu75.thinkhub.authn.internal.config.TokenType;
import com.ta2khu75.thinkhub.authn.internal.service.OAuth2LoginSuccessHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
	private final AuthorizationManager<RequestAuthorizationContext> authorizationManager;
	private final OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService;
	private final OAuth2LoginSuccessHandler oauth2LoginSuccessHandler;
	private final AuthenticationEntryPoint authenticationEntryPoint;
	private final AccessDeniedHandler accessDeniedHandler;
	private final JwtProviderFactory jwtProviderFactory;
	private final UserDetailsService userDetailsService;
	private final OidcUserService oidcUserService;
	private final PasswordEncoder passwordEncoder;
	@Value("${springdoc.swagger-ui.path}")
	private String swaggerUiPath;
	@Value("${springdoc.api-docs.path}")
	private String swaggerApiPath;

	private String[] swaggerWhitelist() {
		return new String[] { swaggerUiPath, swaggerUiPath + "/**", // tất cả asset swagger-ui
				swaggerApiPath, swaggerApiPath + "/**", // bao gồm /swagger-config
		};
	}

	@Bean
	SecurityFilterChain securityFilter(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).cors(Customizer.withDefaults())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
				.exceptionHandling(exception -> exception.accessDeniedHandler(accessDeniedHandler))
				.authorizeHttpRequests(authz -> authz.requestMatchers(HttpMethod.GET, swaggerWhitelist()).permitAll()
						.anyRequest().access(authorizationManager))
				.oauth2Login(oauth2 -> oauth2
						.userInfoEndpoint(user -> user.oidcUserService(oidcUserService).userService(oauth2UserService))
						.successHandler(oauth2LoginSuccessHandler))
				.oauth2ResourceServer(oauth2 -> oauth2.bearerTokenResolver(new CookieTokenResolver("access_token"))
						.jwt(jwt -> jwt.decoder(jwtProviderFactory.getDecoder(TokenType.ACCESS)))
						.authenticationEntryPoint(authenticationEntryPoint))
				.formLogin(login -> login.disable());
		return http.build();
	}

	@Bean
	AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
		return authenticationManagerBuilder.build();
	}

	// config get auth on jwt
	@Bean
	JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
		return jwtAuthenticationConverter;
	}

}
