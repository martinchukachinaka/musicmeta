package com.cc.ice.musicmeta.application.artists.service.impl;

import com.cc.ice.musicmeta.application.artists.repo.ArtistRepo;
import com.cc.ice.musicmeta.application.artists.request.ArtistRequest;
import com.cc.ice.musicmeta.application.artists.response.ArtistInfo;
import com.cc.ice.musicmeta.application.artists.service.ArtistMgr;
import com.cc.ice.musicmeta.domain.artists.Artist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ArtistMgrImpl implements ArtistMgr {

	private final ArtistRepo artistRepo;

	@Override
	public List<UUID> saveArtists(List<ArtistRequest> requests) {
		List<Artist> artists = requests.stream().map(Artist::new).toList();
		return artistRepo.saveArtists(artists);
	}

	@Override
	public Optional<ArtistInfo> findArtistInfo(UUID id) {
		return artistRepo.findArtistById(id)
		                 .map(artist -> ArtistInfo.builder()
		                                          .id(artist.getId())
		                                          .artistName(artist.getArtistName())
		                                          .aliases(artist.getAliases())
		                                          .createdAt(artist.getCreatedAt())
		                                          .lastUpdatedAt(artist.getLastUpdatedAt())
		                                          .biography(artist.getBiography())
		                                          .avatarRef(artist.getAvatarRef())
		                                          .build());
	}

	@Override
	public boolean artistsExist() {
		return artistRepo.artistsExists();
	}
}
