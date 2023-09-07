package com.cc.ice.musicmeta.application.topartist.service;

import com.cc.ice.musicmeta.application.topartist.response.TopArtistResponse;
import com.cc.ice.musicmeta.domain.artists.Artist;
import com.cc.ice.musicmeta.domain.topartist.TopArtist;

import java.time.LocalDate;

public interface TopArtistMgr {

	TopArtistResponse findTopArtist();

	TopArtistResponse findNextTopArtist();

	TopArtist createTopArtist(Artist artist);

	default TopArtistResponse findTopAristByDate(LocalDate date) {
		throw new IllegalStateException("Improvement Idea. Not yet Implemented");
	}
}
