package com.cc.ice.musicmeta.application.tracks.response;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
public class TrackResponse {

	private UUID id;

	private LocalDateTime createdAt;

	private LocalDateTime lastUpdatedAt;

	private String trackName;

	private int duration;

	private boolean explicitContent;

	private String artistAlias;

	private UUID artistRef;
}
