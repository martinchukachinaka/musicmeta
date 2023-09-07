package com.cc.ice.musicmeta.presentation.topartist;


import com.cc.ice.musicmeta.application.topartist.service.TopArtistMgr;
import com.cc.ice.musicmeta.infrastructure.errors.ApiError;
import com.cc.ice.musicmeta.infrastructure.errors.NoICERecordException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cc.ice.musicmeta.application.topartist.response.TopArtistResponse.NO_ARTISTS;

@RestController
@RequestMapping("/top-artist")
@RequiredArgsConstructor
public class TopArtistController {

	private final TopArtistMgr topArtistMgr;

	@GetMapping
	public ResponseEntity<?> getArtistOfTheDay() {
		try {
			return ResponseEntity.ok(topArtistMgr.findTopArtist());
		} catch (NoICERecordException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
			                     .body(new ApiError(NO_ARTISTS));
		}
	}

	@GetMapping(params = "force-next")
	public ResponseEntity<?> getNextTopArtist() {
		try {
			return ResponseEntity.ok(topArtistMgr.findNextTopArtist());
		} catch (NoICERecordException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
			                     .body(new ApiError(NO_ARTISTS));
		}
	}
}
