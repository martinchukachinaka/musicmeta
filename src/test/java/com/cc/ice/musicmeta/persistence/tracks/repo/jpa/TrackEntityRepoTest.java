package com.cc.ice.musicmeta.persistence.tracks.repo.jpa;

import com.cc.ice.musicmeta.persistence.artists.model.ArtistEntity;
import com.cc.ice.musicmeta.persistence.tracks.model.TrackEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
class TrackEntityRepoTest {

	@Autowired
	TrackEntityRepo trackEntityRepo;

	@Autowired
	TestEntityManager entityManager;

	public static final UUID TEST_UUID = UUID.fromString("66a61c3b-0f94-48c9-9d96-4629da5dcbb7");

	@Test
	void shouldFindExistsByTrackNameAndArtistId() {
		createTestTrack();
		assertTrue(trackEntityRepo.existsByTrackNameAndArtistId("ukwu", TEST_UUID));
	}

	@Test
	void shouldAddTrackSuccessfully() {
		TrackEntity trackEntity = new TrackEntity();
		trackEntity.setId(UUID.randomUUID());
		trackEntity.setTrackName("ukwu");
		trackEntity.setArtist(createArtist());

		trackEntityRepo.save(trackEntity);
		assertThat(trackEntityRepo.findById(trackEntity.getId())).isPresent();
	}

	private void createTestTrack() {
		ArtistEntity artist = createArtist();

		TrackEntity trackEntity = new TrackEntity();
		trackEntity.setId(UUID.randomUUID());
		trackEntity.setArtist(artist);
		trackEntity.setTrackName("ukwu");

		entityManager.persist(trackEntity);
	}

	private ArtistEntity createArtist() {
		ArtistEntity artist = new ArtistEntity();
		artist.setId(TEST_UUID);
		entityManager.persist(artist);
		return artist;
	}
}