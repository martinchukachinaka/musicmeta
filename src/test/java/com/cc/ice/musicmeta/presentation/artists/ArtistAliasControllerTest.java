package com.cc.ice.musicmeta.presentation.artists;

import com.cc.ice.musicmeta.application.artists.request.AliasAddRequest;
import com.cc.ice.musicmeta.application.artists.request.AliasRemoveRequest;
import com.cc.ice.musicmeta.application.artists.request.AliasUpdateRequest;
import com.cc.ice.musicmeta.application.artists.service.ArtistAliasMgr;
import com.cc.ice.musicmeta.infrastructure.errors.DuplicateICERecordException;
import com.cc.ice.musicmeta.infrastructure.errors.NoICERecordException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.UUID;

import static com.cc.ice.musicmeta.infrastructure.AppConstants.BAD_PAYLOAD;
import static com.cc.ice.musicmeta.infrastructure.AppConstants.DUPLICATE_RECORD;
import static com.cc.ice.musicmeta.infrastructure.AppConstants.NO_RECORD;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArtistAliasController.class)
@DisplayName("Given an artist")
class ArtistAliasControllerTest {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	MockMvc mockMvc;

	@MockBean
	ArtistAliasMgr artistAliasMgr;

	public static final String SAMPLE_UUID = "a1878edb-1865-45e3-9f62-0bcf756b6891";

	public static final String OTHER_UUID = "66a61c3b-0f94-48c9-9d96-4629da5dcbb7";

	@DisplayName("When an attempt is made to add a new alias")
	@Nested
	class ArtistAliasAdd {
		@Test
		void shouldRejectIfInvalidRequest() throws Exception {
			mockMvc.perform(createCommonAddRequest(getAliasAddRequest(null)))
			       .andExpect(status().isBadRequest())
			       .andExpect(jsonPath("$.message", is(BAD_PAYLOAD)));

			AliasAddRequest request2 = getAliasAddRequest();
			request2.setAlias(null);
			mockMvc.perform(createCommonAddRequest(request2))
			       .andExpect(status().isBadRequest())
			       .andExpect(jsonPath("$.message", is(BAD_PAYLOAD)));
		}

		@Test
		void shouldRejectIfUUIDInUrlAndPayloadDoNotMatch() throws Exception {
			AliasAddRequest request = getAliasAddRequest(OTHER_UUID);

			mockMvc.perform(createCommonAddRequest(request))
			       .andExpect(status().isBadRequest())
			       .andExpect(jsonPath("$.message", is("mismatched artist ids")));
		}

		@Test
		void shouldRejectIfNonExistentArtist() throws Exception {
			doThrow(new NoICERecordException(NO_RECORD))
					.when(artistAliasMgr).addAlias(any());

			mockMvc.perform(createCommonAddRequest(getAliasAddRequest()))
			       .andExpect(status().isNotFound())
			       .andExpect(jsonPath("$.message", is(NO_RECORD)));
		}

		@Test
		void shouldRejectIfAlreadyExistingName() throws Exception {
			doThrow(new DuplicateICERecordException(DUPLICATE_RECORD))
					.when(artistAliasMgr).addAlias(any());

			mockMvc.perform(createCommonAddRequest(getAliasAddRequest()))
			       .andExpect(status().isConflict())
			       .andExpect(jsonPath("$.message", is(DUPLICATE_RECORD)));
		}

		@Test
		void shouldAllowIfValidInput() throws Exception {
			mockMvc.perform(createCommonAddRequest(getAliasAddRequest()))
			       .andExpect(status().isCreated());
		}

		private MockHttpServletRequestBuilder createCommonAddRequest(AliasAddRequest request) throws JsonProcessingException {
			return post("/artists/{id}/aliases", SAMPLE_UUID)
					.contentType("application/json")
					.content(objectMapper.writeValueAsBytes(request));
		}

		private static AliasAddRequest getAliasAddRequest(String id) {
			AliasAddRequest request = new AliasAddRequest();
			if (id != null) {
				request.setArtistId(UUID.fromString(id));
			}
			request.setAlias("new name");
			request.setPreferred(false);
			return request;
		}

		private static AliasAddRequest getAliasAddRequest() {
			return getAliasAddRequest(SAMPLE_UUID);
		}
	}


	@DisplayName("When an attempt is made to update an existing")
	@Nested
	class ArtistAliasUpdate {

		@Test
		void shouldRejectIfInvalidRequest() throws Exception {
			mockMvc.perform(put("/artists/{id}/aliases", SAMPLE_UUID)
					                .contentType("application/json")
					                .content(objectMapper.writeValueAsBytes(new AliasUpdateRequest())))
			       .andExpect(status().isBadRequest())
			       .andExpect(jsonPath("$.message", is(BAD_PAYLOAD)));
		}

		@Test
		void shouldRejectIfNonExistentArtist() throws Exception {
			AliasUpdateRequest request = new AliasUpdateRequest();
			request.setArtistId(UUID.fromString(SAMPLE_UUID));
			request.setOldAlias("alias1");
			request.setNewAlias("alias2");

			doThrow(new NoICERecordException(NO_RECORD))
					.when(artistAliasMgr).updateAlias(request);

			mockMvc.perform(put("/artists/{id}/aliases", SAMPLE_UUID)
					                .contentType("application/json")
					                .content(objectMapper.writeValueAsBytes(request)))
			       .andExpect(status().isNotFound())
			       .andExpect(jsonPath("$.message", is(NO_RECORD)));
		}

		@Test
		void shouldAllowIfValidInput() throws Exception {
			AliasUpdateRequest request = new AliasUpdateRequest();
			request.setArtistId(UUID.fromString(SAMPLE_UUID));
			request.setOldAlias("alias1");
			request.setNewAlias("alias2");

			mockMvc.perform(put("/artists/{id}/aliases", SAMPLE_UUID)
					                .contentType("application/json")
					                .content(objectMapper.writeValueAsBytes(request)))
			       .andExpect(status().isOk());
		}
	}

	@DisplayName("When an attempt is made to remove a alias")
	@Nested
	class ArtistAliasRemove {

		@Test
		void shouldRejectIfInvalidRequest() throws Exception {
			mockMvc.perform(delete("/artists/{id}/aliases", SAMPLE_UUID)
					                .contentType("application/json")
					                .content(objectMapper.writeValueAsBytes(new AliasRemoveRequest())))
			       .andExpect(status().isBadRequest())
			       .andExpect(jsonPath("$.message", is(BAD_PAYLOAD)));
		}

		@Test
		void shouldAllowIfValidInput() throws Exception {
			AliasRemoveRequest request = new AliasRemoveRequest();
			request.setArtistId(UUID.fromString(SAMPLE_UUID));
			request.setAlias("alias1");

			mockMvc.perform(delete("/artists/{id}/aliases", SAMPLE_UUID)
					                .contentType("application/json")
					                .content(objectMapper.writeValueAsBytes(request)))
			       .andExpect(status().isOk());
		}
	}
}