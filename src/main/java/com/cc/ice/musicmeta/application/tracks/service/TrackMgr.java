package com.cc.ice.musicmeta.application.tracks.service;

import com.cc.ice.musicmeta.application.tracks.request.TrackRequest;
import com.cc.ice.musicmeta.application.tracks.request.TracksPageRequest;
import com.cc.ice.musicmeta.application.tracks.response.TrackResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface TrackMgr {

	UUID addNewTrack(TrackRequest trackRequest);

	Page<TrackResponse> findTracksForArtist(TracksPageRequest build);
}
