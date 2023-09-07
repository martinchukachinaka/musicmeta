package com.cc.ice.musicmeta.persistence.tracks.repo;

import com.cc.ice.musicmeta.application.tracks.repo.TrackRepo;
import com.cc.ice.musicmeta.application.tracks.request.TracksPageRequest;
import com.cc.ice.musicmeta.domain.tracks.Track;
import com.cc.ice.musicmeta.persistence.tracks.model.TrackEntity;
import com.cc.ice.musicmeta.persistence.tracks.repo.jpa.TrackEntityRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Transactional
@Component
@RequiredArgsConstructor
public class TrackRepoImpl implements TrackRepo {

	private final TrackEntityRepo trackEntityRepo;

	@Override
	public UUID addNewTrack(Track track) {
		TrackEntity trackEntity = trackEntityRepo.save(new TrackEntity(track));
		return trackEntity.getId();
	}

	@Override
	public boolean trackExistsForArtist(String trackName, UUID artistRef) {
		return trackEntityRepo.existsByTrackNameAndArtistId(trackName, artistRef);
	}

	@Override
	public Page<Track> findTracksForArtist(TracksPageRequest request) {
		return trackEntityRepo.findByArtistId(request.getArtistId(),
		                                      PageRequest.of(request.getPageNumber(), request.getPageSize()))
		                      .map(TrackEntity::buildTrack);
	}
}
