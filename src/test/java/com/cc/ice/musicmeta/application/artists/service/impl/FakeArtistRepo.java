package com.cc.ice.musicmeta.application.artists.service.impl;

import com.cc.ice.musicmeta.application.artists.repo.ArtistRepo;
import com.cc.ice.musicmeta.domain.artists.Artist;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class FakeArtistRepo implements ArtistRepo {

	List<Artist> database = new ArrayList<>();

	@Override
	public List<UUID> saveArtists(List<Artist> artists) {
		this.database = artists;
		return artists.stream().map(Artist::getId).toList();
	}

	@Override
	public Optional<Artist> findArtistById(UUID id) {
		return database.stream().filter(artist -> Objects.equals(artist.getId(), id)).findAny();
	}
}
