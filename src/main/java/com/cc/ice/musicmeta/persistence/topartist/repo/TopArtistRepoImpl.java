package com.cc.ice.musicmeta.persistence.topartist.repo;

import com.cc.ice.musicmeta.application.topartist.repo.TopArtistRepo;
import com.cc.ice.musicmeta.domain.topartist.TopArtist;
import com.cc.ice.musicmeta.persistence.topartist.model.TopArtistEntity;
import com.cc.ice.musicmeta.persistence.topartist.repo.jpa.TopArtistEntityRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;


@Component
@Transactional
@RequiredArgsConstructor
public class TopArtistRepoImpl implements TopArtistRepo {

	private final TopArtistEntityRepo topArtistEntityRepo;

	@Override
	public Optional<TopArtist> findTopArtistByDate(LocalDate creationDate) {
		return topArtistEntityRepo.findTop1ByCreatedAtOrderByIdDesc(creationDate).map(TopArtistEntity::createTopArtist);
	}

	@Override
	public Optional<TopArtist> getLatestTopArtist() {
		return topArtistEntityRepo.findFirstByOrderByIdDesc().map(TopArtistEntity::createTopArtist);
	}

	@Override
	public void saveTopArtist(TopArtist topArtist) {
		topArtistEntityRepo.save(new TopArtistEntity(topArtist));
	}
}
