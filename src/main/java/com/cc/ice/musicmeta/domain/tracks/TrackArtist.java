package com.cc.ice.musicmeta.domain.tracks;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class TrackArtist {

	private String artistAlias;

	private UUID artistRef;
}
