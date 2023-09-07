package com.cc.ice.musicmeta.application.artists.service.impl;

import com.cc.ice.musicmeta.application.artists.repo.ArtistRepo;
import com.cc.ice.musicmeta.application.artists.request.AliasAddRequest;
import com.cc.ice.musicmeta.application.artists.request.AliasRemoveRequest;
import com.cc.ice.musicmeta.application.artists.request.AliasUpdateRequest;
import com.cc.ice.musicmeta.application.artists.service.ArtistAliasMgr;
import com.cc.ice.musicmeta.domain.artists.Artist;
import com.cc.ice.musicmeta.infrastructure.errors.NoICERecordException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.cc.ice.musicmeta.infrastructure.AppConstants.NO_RECORD;


@Component
public class ArtistAliasMgrImpl implements ArtistAliasMgr {

	private final ArtistRepo artistRepo;

	private final Integer maxAllowedAliases;

	public ArtistAliasMgrImpl(ArtistRepo artistRepo, @Value("${maxAllowedAliases:5}") Integer maxAllowedAliases) {
		this.artistRepo = artistRepo;
		this.maxAllowedAliases = maxAllowedAliases;
	}

	@Override
	public void addAlias(AliasAddRequest request) {
		Artist artist = artistRepo.findArtistById(request.getArtistId())
		                          .orElseThrow(() -> new NoICERecordException(NO_RECORD));
		artist.addAlias(request, maxAllowedAliases);
		artistRepo.saveArtistAlias(artist);
	}

	@Override
	public void updateAlias(AliasUpdateRequest request) {
		Artist artist = artistRepo.findArtistById(request.getArtistId())
		                          .orElseThrow(() -> new NoICERecordException(NO_RECORD));
		artist.updateAlias(request);
		artistRepo.saveArtistAlias(artist);
	}

	@Override
	public void removeAlias(AliasRemoveRequest request) {
		Artist artist = artistRepo.findArtistById(request.getArtistId())
		                          .orElseThrow(() -> new NoICERecordException(NO_RECORD));
		artist.removeAlias(request);
		artistRepo.saveArtistAlias(artist);
	}
}
