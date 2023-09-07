package com.cc.ice.musicmeta.presentation.tracks;

import com.cc.ice.musicmeta.application.tracks.request.TrackRequest;
import com.cc.ice.musicmeta.application.tracks.response.TrackResponse;
import com.cc.ice.musicmeta.application.tracks.service.TrackMgr;
import com.cc.ice.musicmeta.infrastructure.errors.DuplicateICERecordException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static com.cc.ice.musicmeta.infrastructure.AppConstants.BAD_PAYLOAD;
import static com.cc.ice.musicmeta.infrastructure.AppConstants.DUPLICATE_RECORD;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TrackController.class)
class TrackControllerTest {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	MockMvc mockMvc;

	@MockBean
	TrackMgr trackMgr;

	public static final UUID ARTIST_ID = UUID.fromString("66a61c3b-0f94-48c9-9d96-4629da5dcbb7");

	@Test
	void shouldRejectAddIfInvalidInput() throws Exception {
		TrackRequest request = new TrackRequest();

		mockMvc.perform(post("/artists/{id}/tracks", ARTIST_ID)
				                .contentType("application/json")
				                .content(objectMapper.writeValueAsBytes(request)))
		       .andExpect(status().isBadRequest())
		       .andExpect(jsonPath("$.message", is(BAD_PAYLOAD)));
	}

	@Test
	void shouldRejectAddIfAlreadyExistingTrack() throws Exception {
		given(trackMgr.addNewTrack(any())).willThrow(new DuplicateICERecordException(DUPLICATE_RECORD));

		TrackRequest request = new TrackRequest();
		request.setTrackName("track1");
		request.setArtistAlias("alias1");

		mockMvc.perform(post("/artists/{id}/tracks", ARTIST_ID)
				                .contentType("application/json")
				                .content(objectMapper.writeValueAsBytes(request)))
		       .andExpect(status().isConflict())
		       .andExpect(jsonPath("$.message", is(DUPLICATE_RECORD)));
	}

	@Test
	void shouldCreateTrackSuccessfully() throws Exception {
		given(trackMgr.addNewTrack(any())).willReturn(UUID.randomUUID());
		TrackRequest request = new TrackRequest();
		request.setTrackName("track1");
		request.setArtistAlias("alias1");

		mockMvc.perform(post("/artists/{id}/tracks", ARTIST_ID)
				                .contentType("application/json")
				                .content(objectMapper.writeValueAsBytes(request)))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$").isNotEmpty());
	}

	@Test
	void shouldReturnEmptyListIfNoArtist() throws Exception {
		given(trackMgr.findTracksForArtist(any())).willReturn(Page.empty());

		mockMvc.perform(get("/artists/{id}/tracks", ARTIST_ID))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.content").isEmpty());
	}

	@Test
	void shouldReturnTracksOfArtist() throws Exception {
		given(trackMgr.findTracksForArtist(any()))
				.willReturn(new PageImpl<>(
						List.of(TrackResponse.builder()
						                     .trackName("track1")
						                     .artistAlias("artist1")
						                     .artistRef(ARTIST_ID)
						                     .build())));

		mockMvc.perform(get("/artists/{id}/tracks", ARTIST_ID))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.content", hasSize(1)))
		       .andExpect(jsonPath("$.content[0].trackName", is("track1")))
		       .andExpect(jsonPath("$.content[0].artistRef", is(ARTIST_ID.toString())));
	}
}