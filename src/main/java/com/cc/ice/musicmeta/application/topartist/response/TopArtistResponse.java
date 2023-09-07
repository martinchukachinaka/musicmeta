package com.cc.ice.musicmeta.application.topartist.response;

import com.cc.ice.musicmeta.domain.topartist.TopArtist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopArtistResponse {

	public TopArtistResponse(TopArtist top) {
		artistRef = top.getArtistRef();
		artistName = top.getArtistName();
		biography = top.getBiography();
		createdAt = top.getCreatedAt();
	}

	public static final String NO_ARTISTS = "no artists available";

	private UUID artistRef;

	private String artistName;

	private String biography;

	private LocalDate createdAt;
}
