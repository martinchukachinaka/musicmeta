package com.cc.ice.musicmeta.presentation.artists;


import com.cc.ice.musicmeta.application.artists.request.AliasAddRequest;
import com.cc.ice.musicmeta.application.artists.request.AliasRemoveRequest;
import com.cc.ice.musicmeta.application.artists.request.AliasUpdateRequest;
import com.cc.ice.musicmeta.application.artists.service.ArtistAliasMgr;
import com.cc.ice.musicmeta.infrastructure.errors.ApiError;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/artists/{id}/aliases")
@RequiredArgsConstructor
public class ArtistAliasController {

	private final ArtistAliasMgr artistAliasMgr;

	public static final String MISMATCHED_ARTIST_IDS = "mismatched artist ids";

	@PostMapping
	public ResponseEntity<?> addArtistName(@PathVariable UUID id, @Valid @RequestBody AliasAddRequest request) {
		if (!Objects.equals(id, request.getArtistId())) {
			return ResponseEntity.badRequest().body(new ApiError(MISMATCHED_ARTIST_IDS));
		}
		artistAliasMgr.addAlias(request);
		return ResponseEntity.status(CREATED).build();
	}

	@PutMapping
	public ResponseEntity<?> updateArtistName(@PathVariable UUID id, @Valid @RequestBody AliasUpdateRequest request) {
		if (!Objects.equals(id, request.getArtistId())) {
			return ResponseEntity.badRequest().body(new ApiError(MISMATCHED_ARTIST_IDS));
		}
		artistAliasMgr.updateAlias(request);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping
	public ResponseEntity<?> removeArtistName(@PathVariable UUID id, @Valid @RequestBody AliasRemoveRequest request) {
		if (!Objects.equals(id, request.getArtistId())) {
			return ResponseEntity.badRequest().body(new ApiError(MISMATCHED_ARTIST_IDS));
		}
		artistAliasMgr.removeAlias(request);
		return ResponseEntity.ok().build();
	}
}
