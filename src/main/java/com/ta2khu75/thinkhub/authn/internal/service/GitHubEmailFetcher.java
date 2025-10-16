package com.ta2khu75.thinkhub.authn.internal.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

public class GitHubEmailFetcher {
	private static final String BASE_URL = "https://api.github.com";

	private final RestClient restClient;

	public GitHubEmailFetcher(String accessToken) {
		this.restClient = RestClient.builder().baseUrl(BASE_URL)
				.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
				.defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json").build();
	}

	public String fetchPrimaryEmail() {
		try {
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> emails = restClient.get().uri("/user/emails").retrieve().body(List.class);

			if (emails == null || emails.isEmpty()) {
				throw new IllegalStateException("No email addresses returned from GitHub");
			}

			return emails.stream().filter(e -> Boolean.TRUE.equals(e.get("primary"))).map(e -> (String) e.get("email"))
					.findFirst().orElse((String) emails.get(0).get("email")); // fallback
		} catch (RestClientResponseException ex) {
			throw new RuntimeException("Failed to fetch email: " + ex.getResponseBodyAsString(), ex);
		}
	}
}
