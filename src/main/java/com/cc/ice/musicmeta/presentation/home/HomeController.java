package com.cc.ice.musicmeta.presentation.home;

import com.cc.ice.musicmeta.application.artists.request.ArtistRequest;
import com.cc.ice.musicmeta.application.artists.service.ArtistMgr;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.cc.ice.musicmeta.presentation.home.HomeConstant.INIT_ARTISTS;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

	private final ArtistMgr artistMgr;

	private final ObjectMapper mapper;

	Set<UUID> tempStore = new HashSet<>();

	@GetMapping
	public ResponseEntity<?> initArtists() throws Exception {
		List<ArtistRequest> requests =
				mapper.readValue(INIT_ARTISTS, new TypeReference<>() {});

		if (!artistMgr.artistsExist()) {
			tempStore.addAll(artistMgr.saveArtists(requests));
			return ResponseEntity.ok(Map.of("artist-ids", tempStore));
		}
		return ResponseEntity.ok(Map.of("message", "Artists already exists. Find IDs here. Have fun!",
		                                "artist-ids", tempStore));
	}
}
