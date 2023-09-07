package com.cc.ice.musicmeta.application.tracks.repo;

import com.cc.ice.musicmeta.application.tracks.request.TracksPageRequest;
import com.cc.ice.musicmeta.domain.tracks.Track;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface TrackRepo {

	UUID addNewTrack(Track track);

	boolean trackExistsForArtist(String trackName, UUID artistRef);

	Page<Track> findTracksForArtist(TracksPageRequest request);
}
