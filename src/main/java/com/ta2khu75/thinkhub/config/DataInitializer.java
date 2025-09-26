package com.ta2khu75.thinkhub.config;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ta2khu75.thinkhub.authn.api.AuthnApi;
import com.ta2khu75.thinkhub.authn.api.dto.CreateAuthProviderRequest;
import com.ta2khu75.thinkhub.authz.api.AuthzApi;
import com.ta2khu75.thinkhub.shared.enums.RoleDefault;
import com.ta2khu75.thinkhub.user.api.dto.CreateUserRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserStatusRequest;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DataInitializer implements ApplicationRunner {

	AuthzApi authorizationApi;
	AuthnApi authenticationApi;

	private void createAuthProviderAdmin() {
		UserStatusRequest status = new UserStatusRequest(true, true, authorizationApi.initDefaultRoles());
		CreateUserRequest user = new CreateUserRequest("admin", "admin", "admin@g.com", LocalDate.now(), "", status);
		authenticationApi.create(new CreateAuthProviderRequest("admin@g.com", "123456", "123456", user));
	}

	@Transactional
	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (authenticationApi.count() == 0) {
			createAuthProviderAdmin();
		}
		Set<Long> permissionSet = authorizationApi.initPermissionsFromEndpoints();
		authorizationApi.assignPermissionsToRole(RoleDefault.ANONYMOUS.name(), permissionSet);
	}

}
