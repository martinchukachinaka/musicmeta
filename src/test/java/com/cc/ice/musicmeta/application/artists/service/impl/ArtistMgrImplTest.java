package com.cc.ice.musicmeta.application.artists.service.impl;

import com.cc.ice.musicmeta.application.artists.request.ArtistRequest;
import com.cc.ice.musicmeta.application.artists.response.ArtistInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArtistMgrImplTest {
	ArtistMgrImpl artistMgr;

	public static final String BAD_UUID = "a1878edb-1865-45e3-9f62-0bcf756b6891";


	private static final List<ArtistRequest> ARTIST_REQUESTS = createArtistRequests();

	@BeforeEach
	void setup() {
		artistMgr = new ArtistMgrImpl(new FakeArtistRepo());
	}

	@Test
	void shouldReturnNoArtistIfInvalidId() {
		Optional<ArtistInfo> artist = artistMgr.findArtistInfo(UUID.fromString(BAD_UUID));
		assertTrue(artist.isEmpty());
	}

	@Test
	void shouldReturnNoArtistIfNullId() {
		Optional<ArtistInfo> artist = artistMgr.findArtistInfo(null);
		assertTrue(artist.isEmpty());
	}

	@Test
	void shouldFindAnArtistGivenAnValidID() {
		// create artists and add pre-checks
		List<UUID> artistIds = artistMgr.saveArtists(ARTIST_REQUESTS);
		assertThat(artistIds).hasSize(2);

		assertThat(artistMgr.findArtistInfo(artistIds.get(0)))
				.isPresent()
				.hasValueSatisfying(artist -> assertThat(artist.getArtistName()).isEqualTo("maxwell1"));
	}

	@Test
	void shouldFindArtistAndIncludeAliasesIfAvailable() {
		// create artists and add pre-checks
		List<UUID> artistIds = artistMgr.saveArtists(ARTIST_REQUESTS);
		assertThat(artistIds).hasSize(2);

		assertThat(artistMgr.findArtistInfo(artistIds.get(0)))
				.isPresent()
				.hasValueSatisfying(artist -> {
					assertThat(artist.getArtistName()).isEqualTo("maxwell1");
					assertThat(artist.getAliases()).hasSize(3)
					                               .contains("maxwell1", "maxwell2", "maxwell3");
				});
	}


	private static List<ArtistRequest> createArtistRequests() {
		return List.of(
				ArtistRequest.builder().aliases(List.of("maxwell1", "maxwell2", "maxwell3"))
				             .biography("biography here")
				             .build(),
				ArtistRequest.builder().aliases(List.of("Lady G", "L Gaga", "lady-gaga"))
				             .biography("biography here")
				             .build()
		);
	}
}