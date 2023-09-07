package com.cc.ice.musicmeta.domain.topartist;

import com.cc.ice.musicmeta.application.topartist.response.TopArtistResponse;
import com.cc.ice.musicmeta.domain.artists.Artist;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class TopArtist {

	private UUID artistRef;

	private String artistName;

	private LocalDateTime artistCreationDate;

	private String biography;

	private LocalDate createdAt;

	public TopArtist(Artist artist) {
		setArtistRef(artist.getId());
		setArtistName(artist.getArtistName());
		setArtistCreationDate(artist.getCreatedAt());
		setCreatedAt(LocalDate.now());
		setBiography(artist.getBiography());
	}

	public TopArtistResponse buildTopArtistResponse() {
		return TopArtistResponse.builder()
		                        .artistRef(artistRef)
		                        .createdAt(createdAt)
		                        .biography(biography)
		                        .artistName(artistName)
		                        .build();
	}
}
