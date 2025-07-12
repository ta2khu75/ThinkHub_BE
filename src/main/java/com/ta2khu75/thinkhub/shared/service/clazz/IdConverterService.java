package com.ta2khu75.thinkhub.shared.service.clazz;

import java.util.List;

import org.springframework.stereotype.Service;
import org.sqids.Sqids;
import com.ta2khu75.thinkhub.config.IdProperties;
import com.ta2khu75.thinkhub.config.IdProperties.IdConfig;
import com.ta2khu75.thinkhub.config.IdProperties.IdType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IdConverterService {
	private final IdProperties idProperties;

	private Sqids getSqids(String alphabet) {
		return Sqids.builder().minLength(7).alphabet(alphabet).build();
	}

	public String encode(Long id, IdType idType) {
		if (id == null || idType == null) {
			throw new IllegalArgumentException("id and saltedType must not be null");
		}
		IdConfig idConfig = idProperties.getConfigByType(idType);

		String result = encode(this.getSqids(idConfig.alphabet()), id * idConfig.salt() + idConfig.offset());
		return result;
	}

	public long decode(String encodedStr, IdType idType) {
		if (encodedStr == null || encodedStr.isBlank() || idType == null) {
			throw new IllegalArgumentException("encodedStr and saltedType must not be null or blank");
		}
		IdConfig idConfig = idProperties.getConfigByType(idType);
		long id = decode(this.getSqids(idConfig.alphabet()), encodedStr);
		return (id - idConfig.offset()) / idConfig.salt();
	}

	private String encode(Sqids sqids, long id) {
		if (id > Long.MAX_VALUE || id < 0) {
			throw new IllegalArgumentException("ID must be between 0 and " + Long.MAX_VALUE);
		}
		return sqids.encode(List.of(id));
	}

	// Decode string về id long
	private long decode(Sqids sqids, String code) {
		List<Long> result = sqids.decode(code);
		if (result.size() == 0) {
			throw new IllegalArgumentException("Invalid code: " + code);
		}
		return result.get(0);
	}
}
