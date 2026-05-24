package com.ta2khu75.thinkhub.modules.authz.permission;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.modules.authz.api.dto.PermissionSummary;
import com.ta2khu75.thinkhub.modules.authz.api.dto.response.PermissionResponse;
import com.ta2khu75.thinkhub.shared.exception.NotFoundException;
import com.ta2khu75.thinkhub.shared.service.BaseService;

@Service
class PermissionService extends BaseService<Permission, Long, PermissionRepository>
		implements PermissionApi {

	protected PermissionService(PermissionRepository repository, PermissionMapper mapper) {
		super(repository);
		this.mapper = mapper;
	}

	private final PermissionMapper mapper;

	@Override
	public PermissionResponse read(Long id) {
		Permission permission = this.readEntity(id);
		return mapper.convert(permission);
	}

	@Override
	public Set<PermissionSummary> readAllSummaryByCodes(Collection<String> codes) {
		if (validateCollection(codes).isEmpty()) {
			return Collections.emptySet();
		}
		Set<String> uniqueCodes = new HashSet<>(codes);
		List<Permission> entities = repository.findAllByCodeIn(uniqueCodes);
		return entities.stream().map(mapper::toSummary).collect(Collectors.toSet());
	}

	private <T> Collection<T> validateCollection(Collection<T> items) {
		if (items == null || items.isEmpty()) {
			return Collections.emptyList();
		}
		return items.stream().filter(Objects::nonNull).collect(Collectors.toUnmodifiableList());
	}

	@Override
	public PermissionResponse readByCode(String code) {
		return mapper.convert(repository.findByCode(code)
				.orElseThrow(() -> new NotFoundException(PermissionErrorCode.NOT_FOUND,
						"Could not find " + Permission.class.getSimpleName() + " with code: " + code)));
	}

	@Override
	public List<PermissionSummary> saveAll(Collection<PermissionSummary> permissions) {
		if (validateCollection(permissions).isEmpty()) {
			return Collections.emptyList();
		}
		return repository.saveAll(permissions.stream().map(mapper::toEntity).collect(Collectors.toSet())).stream()
				.map(mapper::toSummary).toList();
	}
}
