package com.cc.ice.musicmeta.presentation.tracks;


import com.cc.ice.musicmeta.application.tracks.request.TrackRequest;
import com.cc.ice.musicmeta.application.tracks.request.TracksPageRequest;
import com.cc.ice.musicmeta.application.tracks.response.TrackResponse;
import com.cc.ice.musicmeta.application.tracks.service.TrackMgr;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/artists/{artistId}/tracks")
public class TrackController {

	private final TrackMgr trackMgr;

	@PostMapping
	public ResponseEntity<UUID> addNewTrack(@PathVariable UUID artistId,
	                                        @Valid @RequestBody TrackRequest trackRequest) {
		trackRequest.setArtistId(artistId);
		UUID trackId = trackMgr.addNewTrack(trackRequest);
		return ResponseEntity.ok(trackId);
	}

	@GetMapping
	public ResponseEntity<Page<TrackResponse>> findArtistTracks(@PathVariable UUID artistId,
	                                                            @RequestParam(required = false, defaultValue = "0") Integer page,
	                                                            @RequestParam(required = false, defaultValue = "100") Integer size) {
		return ResponseEntity.ok(
				trackMgr.findTracksForArtist(TracksPageRequest.builder()
				                                              .artistId(artistId)
				                                              .pageNumber(page)
				                                              .pageSize(size)
				                                              .build()));
	}
}
