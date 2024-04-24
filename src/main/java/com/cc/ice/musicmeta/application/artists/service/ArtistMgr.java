package com.cc.ice.musicmeta.application.artists.service;

import com.cc.ice.musicmeta.application.artists.request.ArtistRequest;
import com.cc.ice.musicmeta.application.artists.response.ArtistInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ArtistMgr {

	List<UUID> saveArtists(List<ArtistRequest> requests);

	Optional<ArtistInfo> findArtistInfo(UUID id);

	boolean artistsExist();

	List<ArtistInfo> findArtistInfos();
}
