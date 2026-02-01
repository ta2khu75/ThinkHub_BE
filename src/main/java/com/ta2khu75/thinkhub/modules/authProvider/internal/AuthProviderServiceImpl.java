package com.ta2khu75.thinkhub.modules.authProvider.internal;

import org.springframework.stereotype.Service;

import com.ta2khu75.thinkhub.modules.authProvider.api.AuthProviderApi;
import com.ta2khu75.thinkhub.modules.authProvider.api.dto.AuthProviderLocal;
import com.ta2khu75.thinkhub.modules.authProvider.api.dto.AuthProviderOAuth2;
import com.ta2khu75.thinkhub.modules.authProvider.api.dto.AuthProviderSummary;
import com.ta2khu75.thinkhub.modules.authProvider.internal.entity.AuthProvider;
import com.ta2khu75.thinkhub.modules.authProvider.internal.entity.ProviderType;
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
class AuthProviderServiceImpl extends BaseService<AuthProvider, Long, AuthProviderRepository>
		implements AuthProviderApi, IdDecodable {

	protected AuthProviderServiceImpl(AuthProviderRepository repository, AuthProviderMapper mapper) {
		super(repository);
		this.mapper = mapper;
	}

	private final AuthProviderMapper mapper;

	@Override
	public IdConfig getIdConfig() {
		return IdConfig.USER;
	}

	@Override
	public AuthProviderSummary create(AuthProviderLocal local) {
		AuthProvider authProvider = mapper.toEntity(local);
		authProvider.setType(ProviderType.LOCAL);
		authProvider.setUserId(decodeId(local.userId()));
		return mapper.convert(repository.save(authProvider));
	}

	@Override
	public AuthProviderSummary create(AuthProviderOAuth2 oAuth2) {
		AuthProvider authProvider = mapper.toEntity(oAuth2);
		authProvider.setType(ProviderType.LOCAL);
		authProvider.setUserId(decodeId(oAuth2.userId()));
		return mapper.convert(repository.save(authProvider));
	}

	@Override
	public AuthProviderSummary readByEmailAndProvider(String email, ProviderType type) {
		AuthProvider authProvider = repository.findByEmailAndType(email, type)
				.orElseThrow(() -> new NotFoundException(AuthProviderErrorCode.NOT_FOUND,
						"Could not find " + AuthProvider.class.getSimpleName() + " with email: " + email
								+ " and type: " + type.name()));
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
	public AuthProviderSummary readByUserIdAndProvider(String userId, ProviderType type) {
		Long userIdLong = decodeId(userId);
		AuthProvider authProvider = repository.findByUserIdAndType(userIdLong, type)
				.orElseThrow(() -> new NotFoundException(AuthProviderErrorCode.NOT_FOUND,
						"Could not find " + AuthProvider.class.getSimpleName() + " with userId: " + userIdLong
								+ " and type: " + type.name()));
		return mapper.convert(authProvider);
	}

}
