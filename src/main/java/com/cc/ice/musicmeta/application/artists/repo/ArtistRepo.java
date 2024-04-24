package com.cc.ice.musicmeta.application.artists.repo;

import com.cc.ice.musicmeta.domain.artists.Artist;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ArtistRepo {

	List<UUID> saveArtists(List<Artist> artists);

	Optional<Artist> findArtistById(UUID id);

	default void saveArtistAlias(Artist artist) {
		throw new IllegalStateException("Not Yet Implemented");
	}

	default boolean artistsExists() {
		return false;
	}

	default Optional<Artist> findNextArtistByCreationDate(UUID artistRef, LocalDateTime artistCreationDate) {
		return Optional.empty();
	}

	default Optional<Artist> findFirstCreatedArtist() {
		return Optional.empty();
	}

	default List<Artist> findArtists() {
		return Collections.emptyList();
	}
}
