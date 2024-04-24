package com.cc.ice.musicmeta.persistence.artists.repo;

import com.cc.ice.musicmeta.application.artists.repo.ArtistRepo;
import com.cc.ice.musicmeta.domain.artists.Artist;
import com.cc.ice.musicmeta.persistence.artists.model.ArtistEntity;
import com.cc.ice.musicmeta.persistence.artists.repo.jpa.ArtistEntityRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class ArtistRepoImpl implements ArtistRepo {

	private final ArtistEntityRepo artistEntityRepo;

	@Transactional
	@Override
	public List<UUID> saveArtists(List<Artist> artists) {
		List<ArtistEntity> artistEntityList =
				artistEntityRepo.saveAll(artists.stream().map(ArtistEntity::new).toList());
		return artistEntityList.stream().map(ArtistEntity::getId).toList();
	}

	@Override
	public Optional<Artist> findArtistById(UUID id) {
		return artistEntityRepo.findById(id).map(ArtistEntity::createArtist);
	}

	@Transactional
	@Override
	public void saveArtistAlias(Artist artist) {
		ArtistEntity artistEntity = artistEntityRepo.findById(artist.getId()).orElseThrow();
		artistEntity.setArtistName(artist.getArtistName());
		artistEntity.setAliases(artist.getAliases());
		//TODO: Remove not needed
		artistEntityRepo.save(artistEntity);
	}

	@Override
	public boolean artistsExists() {
		return artistEntityRepo.count() > 0;
	}

	@Override
	public Optional<Artist> findNextArtistByCreationDate(UUID artistRef, LocalDateTime artistCreationDate) {
		return artistEntityRepo.findNextArtistByCreationDate(artistRef, artistCreationDate)
		                       .map(ArtistEntity::createArtist);
	}

	@Override
	public Optional<Artist> findFirstCreatedArtist() {
		return artistEntityRepo.findFirstByOrderByCreatedAtAsc().map(ArtistEntity::createArtist);
	}

	@Override
	public List<Artist> findArtists() {
		return artistEntityRepo.findAll().stream().map(ArtistEntity::createArtist).toList();
	}
}
