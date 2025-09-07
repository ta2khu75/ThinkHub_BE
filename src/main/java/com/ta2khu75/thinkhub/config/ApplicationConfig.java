package com.ta2khu75.thinkhub.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(@NonNull CorsRegistry registry) {
		registry.addMapping("/api/**").allowedOrigins("http://localhost:3000").allowedMethods("*").allowedHeaders("*")
				.exposedHeaders("www-authenticate").allowCredentials(true);
	}

	@Bean
	AuditorAware<String> auditorProvider() {
		return new AuditorAwareImpl();
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new SnakeCaseModelAttributeResolver());
	}
}
