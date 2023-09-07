package com.cc.ice.musicmeta.presentation.topartist;

import com.cc.ice.musicmeta.application.topartist.response.TopArtistResponse;
import com.cc.ice.musicmeta.application.topartist.service.TopArtistMgr;
import com.cc.ice.musicmeta.infrastructure.errors.NoICERecordException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.cc.ice.musicmeta.application.topartist.response.TopArtistResponse.NO_ARTISTS;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TopArtistController.class)
class TopArtistControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	TopArtistMgr topArtistMgr;

	@Test
	void shouldFailIfThereAreNoArtists() throws Exception {
		given(topArtistMgr.findTopArtist()).willThrow(new NoICERecordException(NO_ARTISTS));

		mockMvc.perform(get("/top-artist"))
		       .andExpect(status().isNotFound())
		       .andExpect(jsonPath("$.message", Matchers.is(NO_ARTISTS)));
	}

	@Test
	void shouldGetTheArtistOfTheDay() throws Exception {
		given(topArtistMgr.findTopArtist()).willReturn(TopArtistResponse.builder().artistName("artist1").build());

		mockMvc.perform(get("/top-artist"))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.artistName", Matchers.is("artist1")));
	}


	@Test
	void shouldFailToGetNextIfThereAreNoArtists() throws Exception {
		given(topArtistMgr.findNextTopArtist()).willThrow(new NoICERecordException(NO_ARTISTS));

		mockMvc.perform(get("/top-artist?force-next"))
		       .andExpect(status().isNotFound())
		       .andExpect(jsonPath("$.message", Matchers.is(NO_ARTISTS)));
	}

	@Test
	void shouldGetNextArtistIfAvailable() throws Exception {
		given(topArtistMgr.findNextTopArtist()).willReturn(TopArtistResponse.builder().artistName("artist1").build());

		mockMvc.perform(get("/top-artist?force-next"))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.artistName", Matchers.is("artist1")));
	}
}