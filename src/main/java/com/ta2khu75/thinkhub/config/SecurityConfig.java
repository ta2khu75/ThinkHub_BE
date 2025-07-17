package com.ta2khu75.thinkhub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

import com.ta2khu75.thinkhub.account.AccountService;
import com.ta2khu75.thinkhub.account.dto.AccountDto;
import com.ta2khu75.thinkhub.auth.model.Auth;
import com.ta2khu75.thinkhub.authority.RoleService;
import com.ta2khu75.thinkhub.authority.dto.RoleDto;
import com.ta2khu75.thinkhub.config.JwtProperties.TokenType;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
	private final AccessDeniedHandler accessDeniedHandler;
	private final AuthorizationManager<HttpServletRequest> authorizationManager;
	private final AuthenticationEntryPoint authenticationEntryPoint;
	private final JwtProviderFactory jwtProviderFactory;
	private final PasswordEncoder passwordEncoder;
	private final AccountService accountService;
	private final RoleService roleService;

	@Bean
	UserDetailsService userDetailsService() {
		return identifier -> {
			try {
				AccountDto account;
				if (identifier.contains("@")) {
					account = accountService.readDtoByEmail(identifier);
				} else {
					account = accountService.readDtoByUsername(identifier);
				}
				RoleDto role = roleService.readDto(account.status().roleId());
				return new Auth(account, role);
			} catch (Exception e) {
				throw new UsernameNotFoundException("Bad credentials");
			}
		};
	}

	@Bean
	SecurityFilterChain securityFilter(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).cors(Customizer.withDefaults())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(new AuthorizationFilter(authorizationManager), AuthorizationFilter.class)
				.exceptionHandling(exception -> exception.accessDeniedHandler(accessDeniedHandler))
				.authorizeHttpRequests(authz -> authz.anyRequest().permitAll())
				.oauth2ResourceServer(
						oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtProviderFactory.getDecoder(TokenType.ACCESS)))
								.authenticationEntryPoint(authenticationEntryPoint))
				.formLogin(login -> login.disable());
		return http.build();
	}

	@Bean
	AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder);
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
