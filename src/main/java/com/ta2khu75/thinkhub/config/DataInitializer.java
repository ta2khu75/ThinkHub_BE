package com.ta2khu75.thinkhub.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ta2khu75.thinkhub.authn.api.AuthnApi;
import com.ta2khu75.thinkhub.authz.api.AuthzApi;
import com.ta2khu75.thinkhub.authz.api.dto.PermissionSummary;
import com.ta2khu75.thinkhub.authz.api.dto.request.PermissionGroupSummary;
import com.ta2khu75.thinkhub.authz.api.dto.request.RoleRequest;
import com.ta2khu75.thinkhub.authz.api.dto.response.RoleResponse;
import com.ta2khu75.thinkhub.shared.enums.RoleDefault;
import com.ta2khu75.thinkhub.shared.exception.NotFoundException;
import com.ta2khu75.thinkhub.shared.service.ApiScanner;
import com.ta2khu75.thinkhub.user.api.UserApi;
import com.ta2khu75.thinkhub.user.api.dto.UserCreateRequest;
import com.ta2khu75.thinkhub.user.api.dto.UserStatusRequest;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Lazy(false)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DataInitializer implements ApplicationRunner {
	AuthnApi authnApi;
	AuthzApi authzApi;
	UserApi userApi;
	ApiScanner apiScanner;
	PublicEndpointRegistry publicEndpointRegistry;

	private void createUserAdmin() {
		UserStatusRequest status = new UserStatusRequest(true, true, this.initDefaultRoles());
		UserCreateRequest user = new UserCreateRequest("loading@g.com", "loading", "loading", "loading", status);
		authnApi.create(user, "123456");
	}

//	private Map<String, List<PermissionSummary>> initPermissions() {
//		Map<String, Map<String, PermissionSummary>> permissionMap = apiScanner.getPermissionMap();
//		Map<String, Set<PermissionSummary>> permissionExistingMap = permissionMap.entrySet().stream()
//				.collect(Collectors.toMap(Map.Entry::getKey, // key của map mới
//						entry -> {
//							Set<String> codes = entry.getValue().keySet();
//							return authzApi.readAllPermissionSummaryByCodes(codes);
//						}));
//		// update permission
//		permissionExistingMap.forEach((k, v) -> {
//			v.forEach(permission -> {
//				PermissionSummary permissionSummary = permissionMap.get(k).get(permission.getCode());
//				permission.setId(permission.getId());
//				permissionMap.get(k).put(permission.getCode(), permissionSummary);
//			});
//		});
//		return permissionMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, // key của map mới
//				entry -> {
//					Collection<PermissionSummary> codes = entry.getValue().values();
//					return authzApi.saveAllPermisisons(codes);
//				}));
//	}
	private Map<String, List<PermissionSummary>> initPermissions() {
		Map<String, Map<String, PermissionSummary>> permissionMap = apiScanner.getPermissionMap();
		if (permissionMap == null || permissionMap.isEmpty()) {
			return Collections.emptyMap();
		}
		// 1️⃣ Lấy tất cả codes trong hệ thống
		Map<String, Set<String>> codesByGroup = permissionMap.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, entry -> new HashSet<>(entry.getValue().keySet())));

		// 2️⃣ Truy vấn DB để lấy Permission hiện có theo từng group
		Map<String, Set<PermissionSummary>> existingByGroup = codesByGroup.entrySet().stream().collect(Collectors
				.toMap(Map.Entry::getKey, entry -> authzApi.readAllPermissionSummaryByCodes(entry.getValue())));

		// 3️⃣ Merge dữ liệu cũ với mới (gắn id nếu tồn tại)
		existingByGroup.forEach((group, existingPermissions) -> {
			Map<String, PermissionSummary> targetMap = permissionMap.get(group);
			existingPermissions.forEach(existing -> {
				PermissionSummary newPerm = targetMap.get(existing.getCode());
				if (newPerm != null) {
					newPerm.setId(existing.getId());
				}
			});
		});

		// 4️⃣ Save tất cả group và trả kết quả
		return permissionMap.entrySet().stream().collect(
				Collectors.toMap(Map.Entry::getKey, entry -> authzApi.saveAllPermisisons(entry.getValue().values())));
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (userApi.count() == 0) {
			createUserAdmin();
		}
		Map<String, List<PermissionSummary>> permissionMap = this.initPermissions();
		this.initPermissionGroup(permissionMap);
		Set<Long> publicId = getPermissionPublicIds(permissionMap);
		authzApi.assignPermissionsToRole(RoleDefault.ANONYMOUS.name(), publicId);
	}

	private void initPermissionGroup(Map<String, List<PermissionSummary>> permissionMap) {
		if (permissionMap == null || permissionMap.isEmpty())
			return;

		// 1️⃣ Map nhóm → tập ID permission
		Map<String, Set<Long>> permissionIdMap = permissionMap.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream()
						.map(PermissionSummary::getId).filter(Objects::nonNull).collect(Collectors.toSet())));

		// 2️⃣ Lấy metadata group từ scanner
		Map<String, PermissionGroupSummary> permissionGroupMap = apiScanner.getPermissionGroupMap();
		if (permissionGroupMap == null || permissionGroupMap.isEmpty())
			return;

		// 3️⃣ Gắn permissionIds vào từng group
		permissionGroupMap.values()
				.forEach(group -> group.setPermissionIds(permissionIdMap.getOrDefault(group.getCode(), Set.of())));

		// 4️⃣ Đọc các group hiện có trong DB
		Set<String> groupCodes = permissionGroupMap.keySet();
		Set<PermissionGroupSummary> existingGroups = authzApi.readAllGroupSummaryByCodes(groupCodes);

		// 5️⃣ Gắn ID lại cho group đã tồn tại
		existingGroups.forEach(existing -> {
			PermissionGroupSummary current = permissionGroupMap.get(existing.getCode());
			if (current != null) {
				current.setId(existing.getId());
			}
		});

		// 6️⃣ Save tất cả group
		authzApi.saveAllGroups(permissionGroupMap.values());
	}

//	private void initPermissionGroup(Map<String, List<PermissionSummary>> permissionDtoMap) {
//		Map<String, Set<Long>> permissionIdMap = permissionDtoMap.entrySet().stream().collect(Collectors.toMap(
//				Map.Entry::getKey, entry -> entry.getValue().stream().map(m -> m.getId()).collect(Collectors.toSet())));
//		Map<String, PermissionGroupSummary> permissionGroupMap = apiScanner.getPermissionGroupMap();
//		Map<String, PermissionGroupSummary> updatePermissionGroupRequestMap = permissionGroupMap.entrySet().stream()
//				.collect(Collectors.toMap(Map.Entry::getKey, entry -> {
//					PermissionGroupSummary group = entry.getValue();
//					group.setPermissionIds(permissionIdMap.get(group.getCode()));
//					return group;
//				}));
//		Set<PermissionGroupSummary> permissionGroupResponse = authzApi
//				.readAllGroupSummaryByCodes(permissionGroupMap.keySet());
//		// update
//		permissionGroupResponse.forEach(permissionGroup -> {
//			PermissionGroupSummary group = updatePermissionGroupRequestMap.get(permissionGroup.getCode());
//			group.setId(group.getId());
//			updatePermissionGroupRequestMap.put(group.getCode(), group);
//		});
//		authzApi.saveAllGroups(updatePermissionGroupRequestMap.values());
//	}

	private Long initDefaultRoles() {
		List<RoleResponse> roles = Arrays.stream(RoleDefault.values())
				.map(role -> authzApi.createRole(new RoleRequest(role.name(), null, new HashSet<>()))).toList();
		return roles.stream().filter(role -> role.name().equals(RoleDefault.ADMIN.name())).findFirst()
				.orElseThrow(() -> new NotFoundException("Not found role name ADMIN")).id();
	}

	private Set<Long> getPermissionPublicIds(Map<String, List<PermissionSummary>> permissionMap) {
		Map<RequestMethod, Set<String>> publicEndpoints = Map.of(RequestMethod.GET,
				publicEndpointRegistry.getPublicEndpoint(RequestMethod.GET), RequestMethod.POST,
				publicEndpointRegistry.getPublicEndpoint(RequestMethod.POST), RequestMethod.PUT,
				publicEndpointRegistry.getPublicEndpoint(RequestMethod.PUT), RequestMethod.PATCH,
				publicEndpointRegistry.getPublicEndpoint(RequestMethod.PATCH), RequestMethod.DELETE,
				publicEndpointRegistry.getPublicEndpoint(RequestMethod.DELETE));
		Set<Long> publicId = new HashSet<>();
		permissionMap.values().forEach(permissionSet -> {
			permissionSet.forEach(permission -> {
				if (publicEndpoints.getOrDefault(permission.getMethod(), Set.of()).contains(permission.getPattern())) {
					publicId.add(permission.getId());
				}
			});
		});
		return publicId;
	}
}
