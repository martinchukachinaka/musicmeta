package com.cc.ice.musicmeta.persistence.common;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

	@Id
	protected UUID id;

	protected LocalDateTime createdAt;

	protected LocalDateTime lastUpdatedAt;
}
