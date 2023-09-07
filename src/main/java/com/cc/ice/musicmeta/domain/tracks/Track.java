package com.cc.ice.musicmeta.domain.tracks;

import com.cc.ice.musicmeta.domain.common.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Track extends BaseModel {

	private String trackName;

	private int duration;

	private boolean explicitContent;

	private TrackArtist artist;

	private Set<String> songWriters;

	private UUID album;

	private String trackImageRef;
}
