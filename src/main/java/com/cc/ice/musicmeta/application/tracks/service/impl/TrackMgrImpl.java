package com.cc.ice.musicmeta.application.tracks.service.impl;

import com.cc.ice.musicmeta.application.tracks.repo.TrackRepo;
import com.cc.ice.musicmeta.application.tracks.request.TrackRequest;
import com.cc.ice.musicmeta.application.tracks.request.TracksPageRequest;
import com.cc.ice.musicmeta.application.tracks.response.TrackResponse;
import com.cc.ice.musicmeta.application.tracks.service.TrackMgr;
import com.cc.ice.musicmeta.domain.tracks.Track;
import com.cc.ice.musicmeta.domain.tracks.TrackArtist;
import com.cc.ice.musicmeta.infrastructure.errors.DuplicateICERecordException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.UUID;

import static com.cc.ice.musicmeta.infrastructure.AppConstants.DUPLICATE_RECORD;


@Component
@RequiredArgsConstructor
public class TrackMgrImpl implements TrackMgr {

	public static final String INVALID_TRACK = "invalid track";

	public static final String INVALID_TRACK_NAME = "invalid track name";

	public static final String INVALID_ARTIST_ALIAS = "invalid artist alias";

	public static final String INVALID_ARTIST_REF = "invalid artist ref";

	private final TrackRepo trackRepo;

	@Override
	public UUID addNewTrack(TrackRequest trackRequest) {
		validateTrackRequest(trackRequest);
		Track track = Track.builder()
		                   .trackName(trackRequest.getTrackName())
		                   .duration(trackRequest.getDuration())
		                   .explicitContent(trackRequest.isExplicitContent())
		                   .artist(new TrackArtist(trackRequest.getArtistAlias(), trackRequest.getArtistId()))
		                   .build();
		trackRepo.addNewTrack(track);
		return track.getId();
	}

	@Override
	public Page<TrackResponse> findTracksForArtist(TracksPageRequest request) {
		return trackRepo.findTracksForArtist(request)
		                .map(track -> TrackResponse.builder()
		                                           .id(track.getId())
		                                           .artistRef(track.getArtist().getArtistRef())
		                                           .artistAlias(track.getArtist().getArtistAlias())
		                                           .createdAt(track.getCreatedAt())
		                                           .lastUpdatedAt(track.getLastUpdatedAt())
		                                           .duration(track.getDuration())
		                                           .explicitContent(track.isExplicitContent())
		                                           .trackName(track.getTrackName())
		                                           .build());
	}

	private void validateTrackRequest(TrackRequest trackRequest) {
		Objects.requireNonNull(trackRequest, INVALID_TRACK);
		Objects.requireNonNull(trackRequest.getArtistId(), INVALID_ARTIST_REF);
		if (!StringUtils.hasText(trackRequest.getTrackName())) {
			throw new IllegalArgumentException(INVALID_TRACK_NAME);
		}
		if (!StringUtils.hasText(trackRequest.getArtistAlias())) {
			throw new IllegalArgumentException(INVALID_ARTIST_ALIAS);
		}
		if (trackRepo.trackExistsForArtist(trackRequest.getTrackName(), trackRequest.getArtistId())) {
			throw new DuplicateICERecordException(DUPLICATE_RECORD);
		}
	}
}
