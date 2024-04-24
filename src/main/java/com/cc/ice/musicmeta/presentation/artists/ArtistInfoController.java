package com.cc.ice.musicmeta.presentation.artists;


import com.cc.ice.musicmeta.application.artists.request.ArtistRequest;
import com.cc.ice.musicmeta.application.artists.response.ArtistInfo;
import com.cc.ice.musicmeta.application.artists.service.ArtistMgr;
import com.cc.ice.musicmeta.infrastructure.errors.ApiError;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/artists")
@RequiredArgsConstructor
public class ArtistInfoController {

	public static final String ARTIST_NOT_FOUND = "artist not found";

	private final ArtistMgr artistMgr;

	@GetMapping("{id}")
	public ResponseEntity<?> findArtistInfo(@PathVariable UUID id) {
		Optional<ArtistInfo> artistInfo = artistMgr.findArtistInfo(id);

		if (artistInfo.isPresent()) {
			return ResponseEntity.ok(artistInfo);
		}
		return ResponseEntity.status(NOT_FOUND).body(new ApiError(ARTIST_NOT_FOUND));
	}

	@PostMapping("init")
	public ResponseEntity<Map<String, Object>> initArtists(@Valid @RequestBody List<ArtistRequest> requests) {
		List<UUID> artistIds = artistMgr.saveArtists(requests);
		return ResponseEntity.ok(Map.of(
				"status", "artists created",
				"artistIds", artistIds
		));
	}

	@GetMapping
	public ResponseEntity<List<ArtistInfo>> loadArtists() {
		List<ArtistInfo> artists = artistMgr.findArtistInfos();
		return ResponseEntity.ok(artists);
	}
}
