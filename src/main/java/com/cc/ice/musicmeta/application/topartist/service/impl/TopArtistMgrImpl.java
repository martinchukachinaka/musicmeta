package com.cc.ice.musicmeta.application.topartist.service.impl;

import com.cc.ice.musicmeta.application.artists.repo.ArtistRepo;
import com.cc.ice.musicmeta.application.topartist.repo.TopArtistRepo;
import com.cc.ice.musicmeta.application.topartist.response.TopArtistResponse;
import com.cc.ice.musicmeta.application.topartist.service.TopArtistMgr;
import com.cc.ice.musicmeta.domain.artists.Artist;
import com.cc.ice.musicmeta.domain.topartist.TopArtist;
import com.cc.ice.musicmeta.infrastructure.errors.NoICERecordException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.cc.ice.musicmeta.application.topartist.response.TopArtistResponse.NO_ARTISTS;


@Slf4j
@Component
@RequiredArgsConstructor
public class TopArtistMgrImpl implements TopArtistMgr {

	private final ArtistRepo artistRepo;

	private final TopArtistRepo topArtistRepo;

	@Cacheable("top-artist")
	@Override
	public TopArtistResponse findTopArtist() {
		log.debug("cache not found, retrieving top-artist");
		verifyArtistsExistence();
		return topArtistRepo.findTopArtistByDate(LocalDate.now())
		                    .map(TopArtistResponse::new)
		                    .orElseGet(this::findNextTopArtist);
	}

	@Override
	@CacheEvict("top-artist")
	public TopArtistResponse findNextTopArtist() {
		verifyArtistsExistence();

		Artist artist = topArtistRepo.getLatestTopArtist()
		                             .map(this::findNextOrFirstArtist)
		                             .orElseGet(this::findFirstArtist);
		TopArtist topArtist = createTopArtist(artist);
		return topArtist.buildTopArtistResponse();
	}

	@Override
	public TopArtist createTopArtist(Artist artist) {
		TopArtist topArtist = new TopArtist(artist);
		topArtistRepo.saveTopArtist(topArtist);
		return topArtist;
	}

	private void verifyArtistsExistence() {
		if (!artistRepo.artistsExists()) {
			throw new NoICERecordException(NO_ARTISTS);
		}
	}

	private Artist findNextOrFirstArtist(TopArtist latestTopArtist) {
		return artistRepo.findNextArtistByCreationDate(
				latestTopArtist.getArtistRef(),
				latestTopArtist.getArtistCreationDate()
		).orElseGet(this::findFirstArtist);
	}

	private Artist findFirstArtist() {
		return artistRepo.findFirstCreatedArtist().orElseThrow();
	}
}
