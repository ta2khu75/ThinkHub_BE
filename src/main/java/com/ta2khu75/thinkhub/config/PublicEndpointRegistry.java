package com.ta2khu75.thinkhub.config;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
public class PublicEndpointRegistry {
	@Value("${app.api-prefix}")
	private String apiPrefix;
	private final Set<String> PUBLIC_POST_ENDPOINT = Set.of("/authn/login", "/authn/register", "/authn/google","/authn/refresh-token");
	private final Set<String> PUBLIC_GET_ENDPOINT = Set.of();
	private final Set<String> PUBLIC_PUT_ENDPOINT = Set.of();
	private final Set<String> PUBLIC_PATCH_ENDPOINT = Set.of();
	private final Set<String> PUBLIC_DELETE_ENDPOINT = Set.of();

	public Set<String> getPublicEndpoint(RequestMethod requestMethod) {
		switch (requestMethod) {
		case POST: {
			return getPrefixedEndpoints(PUBLIC_POST_ENDPOINT);
		}
		case GET: {
			return getPrefixedEndpoints(PUBLIC_GET_ENDPOINT);
		}
		case PUT: {
			return getPrefixedEndpoints(PUBLIC_PUT_ENDPOINT);
		}
		case DELETE: {
			return getPrefixedEndpoints(PUBLIC_DELETE_ENDPOINT);
		}
		case PATCH: {
			return getPrefixedEndpoints(PUBLIC_PATCH_ENDPOINT);
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + requestMethod);
		}
	}

	private Set<String> getPrefixedEndpoints(Set<String> endpoints) {
		return endpoints.stream().map(endpoint -> apiPrefix + endpoint).collect(Collectors.toSet());
	}
}
