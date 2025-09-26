package com.ta2khu75.thinkhub.authz.internal.permission;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ta2khu75.thinkhub.authz.api.dto.request.PermissionRequest;
import com.ta2khu75.thinkhub.authz.api.dto.response.PermissionResponse;
import com.ta2khu75.thinkhub.shared.exception.NotFoundException;
import com.ta2khu75.thinkhub.shared.service.BaseService;

import jakarta.validation.Valid;

@Service
public class PermissionServiceImpl extends BaseService<Permission, Long, PermissionRepository, PermissionMapper>
		implements PermissionService {

	protected PermissionServiceImpl(PermissionRepository repository, PermissionMapper mapper) {
		super(repository, mapper);
	}

	@Override
	public PermissionResponse create(@Valid PermissionRequest request) {
		Permission permission = mapper.toEntity(request);
		permission = repository.save(permission);
		return mapper.convert(permission);
	}

	@Override
	public PermissionResponse update(Long id, @Valid PermissionRequest request) {
		Permission permission = this.readEntity(id);
		mapper.update(request, permission);
		permission = repository.save(permission);
		return mapper.convert(permission);
	}

	@Override
	public PermissionResponse read(Long id) {
		Permission permission = this.readEntity(id);
		return mapper.convert(permission);
	}

	@Override
	public void delete(Long id) {
		repository.deleteById(id);
	}

	@Override
	public Set<PermissionResponse> readAllBySummary(Set<String> summaries) {
		return repository.findAllBySummaryIn(summaries).stream().map(mapper::convert).collect(Collectors.toSet());
	}

	@Override
	public PermissionResponse readBySummary(String summary) {
		return mapper.convert(repository.findBySummary(summary)
				.orElseThrow(() -> new NotFoundException("Could not find Permission with summary: " + summary)));
	}

	@Override
	public PermissionResponse readByPatternAndMethod(String pattern, RequestMethod method) {
		return repository.findByPatternAndMethod(pattern, method).map(mapper::convert).orElseThrow(()->new NotFoundException("Could not find Permission with pattern: " + pattern + " and method: " + method));
	}

	@Override
	public Set<PermissionResponse> saveAll(Set<PermissionRequest> requests) {
		return repository.saveAll(requests.stream().map(mapper::toEntity).collect(Collectors.toSet())).stream()
				.map(mapper::convert).collect(Collectors.toSet());
	}

}
