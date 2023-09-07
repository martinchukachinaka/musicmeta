package com.cc.ice.musicmeta.application.artists.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;


@Data
@Builder
@Getter
public class ArtistInfo {

	private UUID id;

	private String artistName;

	private String biography;

	private String avatarRef;

	private LocalDateTime createdAt;

	private LocalDateTime lastUpdatedAt;

	private Set<String> aliases;
}
