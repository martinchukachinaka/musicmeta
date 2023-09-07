package com.cc.ice.musicmeta.persistence.artists.dao;

import com.cc.ice.musicmeta.domain.artists.Artist;
import com.cc.ice.musicmeta.persistence.artists.repo.ArtistRepoImpl;
import com.cc.ice.musicmeta.persistence.artists.repo.jpa.ArtistEntityRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class ArtistEntityRepoImplTest {

	@Autowired
	ArtistRepoImpl artistRepoImpl;

	@BeforeEach
	void clearDB(@Autowired ArtistEntityRepo entityRepo) {
		entityRepo.deleteAll();
	}

	@Test
	void shouldSaveAndFindArtistsGivenValidInputs() {
		List<Artist> artists = createArtists();
		List<UUID> uuids = artistRepoImpl.saveArtists(artists);

		assertThat(uuids).isNotEmpty();
		assertThat(artistRepoImpl.findArtistById(uuids.get(0))).isNotEmpty();
	}

	@Test
	void shouldReportTheExistenceOfArtistsCorrectly() {
		List<Artist> artists = createArtists();
		artistRepoImpl.saveArtists(artists);

		assertTrue(artistRepoImpl.artistsExists());
	}

	@Test
	void shouldFindNextArtistByCreationDate() {
		Artist artist1 = createAnArtist();
		artist1.setArtistName("marco");

		Artist artist2 = createAnArtist();
		artist2.setArtistName("polo");
		artistRepoImpl.saveArtists(List.of(artist1, artist2));

		Optional<Artist> nextArtist =
				artistRepoImpl.findNextArtistByCreationDate(artist1.getId(), artist1.getCreatedAt());

		assertThat(nextArtist).isPresent()
		                      .hasValueSatisfying(artist -> {
			                      assertThat(artist.getArtistName())
					                      .isEqualTo("polo");
		                      });
	}


	@Test
	void shouldFindFirstCreatedArtist() {
		Artist artist1 = createAnArtist();
		artist1.setArtistName("marco");

		Artist artist2 = createAnArtist();
		artist2.setArtistName("polo");
		artistRepoImpl.saveArtists(List.of(artist1, artist2));

		Optional<Artist> result = artistRepoImpl.findFirstCreatedArtist();
		assertThat(result).isPresent()
		                  .hasValueSatisfying(artist -> {
			                  System.out.println("artist: " + artist);
			                  assertThat(artist.getArtistName()).isEqualTo("marco");
		                  });
	}

	private static List<Artist> createArtists() {
		return List.of(createAnArtist());
	}

	private static Artist createAnArtist() {
		Artist artist = new Artist();
		artist.setBiography("biography here");
		artist.setAliases(Set.of("mac1", "mac2"));
		return artist;
	}
}