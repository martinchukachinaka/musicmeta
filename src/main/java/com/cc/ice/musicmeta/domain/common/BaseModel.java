package com.cc.ice.musicmeta.domain.common;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.time.LocalDateTime.now;

@Getter
@Setter
public class BaseModel {

	private UUID id;

	private LocalDateTime createdAt;

	private LocalDateTime lastUpdatedAt;

	public BaseModel() {
		id = UUID.randomUUID();
		lastUpdatedAt = createdAt = now();
	}
}
