package com.cc.ice.musicmeta.application.tracks.request;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;


@Builder
@Getter
public class TracksPageRequest {

	private UUID artistId;

	private Integer pageNumber;

	private Integer pageSize;
}
