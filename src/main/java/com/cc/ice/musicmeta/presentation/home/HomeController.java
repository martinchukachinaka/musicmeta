package com.cc.ice.musicmeta.presentation.home;

import com.cc.ice.musicmeta.application.artists.request.ArtistRequest;
import com.cc.ice.musicmeta.application.artists.response.ArtistInfo;
import com.cc.ice.musicmeta.application.artists.service.ArtistMgr;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
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
	@Operation(description = "init and/or fetch artists", summary = "initial app with artists and/or fetch available artist ids")
	public ResponseEntity<?> initArtists() throws Exception {
		List<ArtistRequest> requests =
				mapper.readValue(INIT_ARTISTS, new TypeReference<>() {});

		tempStore.addAll(artistMgr.findArtistInfos().stream().map(ArtistInfo::getId).toList());
		if (CollectionUtils.isEmpty(tempStore)) {
			tempStore.addAll(artistMgr.saveArtists(requests));
			return ResponseEntity.ok(Map.of("artist-ids", tempStore));
		}
		return ResponseEntity.ok(Map.of("message", "Artists already exists. Find IDs here. Have fun!",
		                                "artist-ids", tempStore));
	}
}
