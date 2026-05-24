package com.ta2khu75.thinkhub.modules.authProvider.internal;

import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.modules.authProvider.api.AuthProviderApi;
import com.ta2khu75.thinkhub.modules.authProvider.api.dto.AuthProviderLocal;
import com.ta2khu75.thinkhub.modules.authProvider.api.dto.AuthProviderOAuth2;
import com.ta2khu75.thinkhub.modules.authProvider.api.dto.AuthProviderSummary;
import com.ta2khu75.thinkhub.modules.authProvider.api.model.ProviderType;
import com.ta2khu75.thinkhub.modules.authProvider.internal.domain.AuthProvider;
import com.ta2khu75.thinkhub.modules.authProvider.internal.mapper.AuthProviderMapper;
import com.ta2khu75.thinkhub.modules.authProvider.internal.repository.AuthProviderRepository;
import com.ta2khu75.thinkhub.modules.authProvider.internal.validator.AuthProviderErrorCode;
import com.ta2khu75.thinkhub.shared.common.infra.id.IdConfig;
import com.ta2khu75.thinkhub.shared.exception.NotFoundException;
import com.ta2khu75.thinkhub.shared.service.BaseService;
import com.ta2khu75.thinkhub.shared.service.IdDecodable;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class AuthProviderService extends BaseService<AuthProvider, Long, AuthProviderRepository> implements AuthProviderApi {

	protected AuthProviderService(AuthProviderRepository repository, AuthProviderMapper mapper) {
		super(repository);
		this.mapper = mapper;
	}

	private final AuthProviderMapper mapper;

	@Override
	public AuthProviderSummary create(AuthProviderLocal local) {
		AuthProvider authProvider = mapper.toEntity(local);
		authProvider.setType(ProviderType.LOCAL);
		authProvider.setUserId(local.userId());
		return mapper.convert(repository.save(authProvider));
	}

	@Override
	public AuthProviderSummary create(AuthProviderOAuth2 oAuth2) {
		AuthProvider authProvider = mapper.toEntity(oAuth2);
		authProvider.setType(ProviderType.LOCAL);
		authProvider.setUserId(oAuth2.userId());
		return mapper.convert(repository.save(authProvider));
	}

	@Override
	public AuthProviderSummary readByEmailAndProvider(String email, ProviderType type) {
		AuthProvider authProvider = repository.findByEmailAndType(email, type)
				.orElseThrow(() -> new NotFoundException(AuthProviderErrorCode.NOT_FOUND, "Could not find "
						+ AuthProvider.class.getSimpleName() + " with email: " + email + " and type: " + type.name()));
		return mapper.convert(authProvider);
	}

	@Override
	public AuthProviderSummary updatePassword(Long id, String password) {
		AuthProvider authProvider = this.readEntity(id);
		authProvider.setPassword(password);
		authProvider = repository.save(authProvider);
		return mapper.convert(authProvider);
	}

	@Override
	public AuthProviderSummary readByUserIdAndProvider(Long userId, ProviderType type) {
		AuthProvider authProvider = repository.findByUserIdAndType(userId, type)
				.orElseThrow(() -> new NotFoundException(AuthProviderErrorCode.NOT_FOUND,
						"Could not find " + AuthProvider.class.getSimpleName() + " with userId: " + userId
								+ " and type: " + type.name()));
		return mapper.convert(authProvider);
	}

}
