package com.ta2khu75.thinkhub.modules.authz.internal.group;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ta2khu75.thinkhub.modules.authz.api.dto.request.PermissionGroupSummary;
import com.ta2khu75.thinkhub.modules.authz.api.dto.response.PermissionGroupResponse;
import com.ta2khu75.thinkhub.shared.service.BaseService;

@Service
public class PermissionGroupServiceImpl extends BaseService<PermissionGroup, Integer, PermissionGroupRepository>
		implements PermissionGroupService {

	public PermissionGroupServiceImpl(PermissionGroupRepository repository, PermissionGroupMapper mapper) {
		super(repository);
		this.mapper = mapper;
	}

	private final PermissionGroupMapper mapper;

	@Override
	@Transactional
	public List<PermissionGroupResponse> readAll() {
		return repository.findAll().stream().map(mapper::convert).toList();
	}

	@Override
	public List<PermissionGroupSummary> saveAll(Collection<PermissionGroupSummary> requests) {
		return repository.saveAll(requests.stream().map(mapper::toEntity).collect(Collectors.toSet())).stream()
				.map(mapper::toSummary).toList();
	}

	@Override
	public Set<PermissionGroupSummary> readAllSummaryByCodes(Collection<String> codes) {
		return repository.findAllByCodeIn(codes).stream().map(mapper::toSummary).collect(Collectors.toSet());
	}
}
