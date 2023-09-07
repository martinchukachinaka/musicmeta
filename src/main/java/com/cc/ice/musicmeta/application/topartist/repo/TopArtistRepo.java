package com.cc.ice.musicmeta.application.topartist.repo;

import com.cc.ice.musicmeta.domain.topartist.TopArtist;

import java.time.LocalDate;
import java.util.Optional;

public interface TopArtistRepo {

	Optional<TopArtist> findTopArtistByDate(LocalDate now);

	Optional<TopArtist> getLatestTopArtist();

	void saveTopArtist(TopArtist topArtist);
}
