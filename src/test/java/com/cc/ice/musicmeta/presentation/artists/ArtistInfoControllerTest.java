package com.cc.ice.musicmeta.presentation.artists;

import com.cc.ice.musicmeta.application.artists.response.ArtistInfo;
import com.cc.ice.musicmeta.application.artists.service.ArtistMgr;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.cc.ice.musicmeta.infrastructure.AppConstants.BAD_PAYLOAD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ArtistInfoController.class)
class ArtistInfoControllerTest {

	@MockBean
	ArtistMgr artistMgr;

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	public static final String SAMPLE_UUID = "a1878edb-1865-45e3-9f62-0bcf756b6891";

	@Test
	void shouldFailToRetrieveNonExistingArtist() throws Exception {
		mockMvc.perform(get("/artists/{id}", SAMPLE_UUID))
		       .andExpect(status().isNotFound())
		       .andExpect(jsonPath("$").isNotEmpty())
		       .andExpect(jsonPath("$.message", is("artist not found")));
	}

	@Test
	void shouldRejectBadArtistInfoRequests() throws Exception {
		mockMvc.perform(get("/artists/{id}", "bad_id"))
		       .andExpect(status().isBadRequest())
		       .andExpect(jsonPath("$.message", is(BAD_PAYLOAD)));
	}

	@Test
	void shouldRetrieveArtistIfAvailable() throws Exception {
		UUID id = UUID.fromString(SAMPLE_UUID);
		ArtistInfo artist = ArtistInfo.builder()
		                              .id(id)
		                              .artistName("john")
		                              .biography("hails from")
		                              .aliases(Set.of("john"))
		                              .build();
		given(artistMgr.findArtistInfo(id)).willReturn(Optional.of(artist));

		MvcResult result = mockMvc.perform(get("/artists/{id}", SAMPLE_UUID))
		                          .andExpect(status().isOk())
		                          .andExpect(jsonPath("$.artistName", is("john")))
		                          .andReturn();
		String resultAsString = result.getResponse().getContentAsString();
		assertThat(resultAsString)
				.isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(artist));
	}
}