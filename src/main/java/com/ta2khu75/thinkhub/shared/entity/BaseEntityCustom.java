package com.ta2khu75.thinkhub.shared.entity;

import java.io.Serializable;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@MappedSuperclass
@EqualsAndHashCode(callSuper = false)
public class BaseEntityCustom<T extends Serializable> extends BaseEntity<T> {
	@Id
	@EmbeddedId
	T id;
}
