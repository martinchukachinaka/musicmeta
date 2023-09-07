package com.cc.ice.musicmeta.application.artists.service.impl;

import com.cc.ice.musicmeta.application.artists.repo.ArtistRepo;
import com.cc.ice.musicmeta.application.artists.request.AliasAddRequest;
import com.cc.ice.musicmeta.application.artists.request.AliasRemoveRequest;
import com.cc.ice.musicmeta.application.artists.request.AliasUpdateRequest;
import com.cc.ice.musicmeta.domain.artists.Artist;
import com.cc.ice.musicmeta.infrastructure.errors.DuplicateICERecordException;
import com.cc.ice.musicmeta.infrastructure.errors.NoICERecordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.cc.ice.musicmeta.infrastructure.AppConstants.DUPLICATE_RECORD;
import static com.cc.ice.musicmeta.infrastructure.AppConstants.NO_RECORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Given an artist")
class ArtistAliasMgrImplTest {

	public static final UUID ARTIST_ID = UUID.fromString("66a61c3b-0f94-48c9-9d96-4629da5dcbb7");


	ArtistAliasMgrImpl aliasMgr;

	ArtistRepo artistRepo;

	@BeforeEach
	void setup() {
		artistRepo = mock(ArtistRepo.class);
		aliasMgr = new ArtistAliasMgrImpl(artistRepo, 3);
	}

	@Nested
	@DisplayName("when an alias is being added")
	class AddAlias {
		@Test
		void shouldRejectIfArtistDoesNotExist() {
			AliasAddRequest request = createAliasRequest();

			NoICERecordException noICERecordException =
					assertThrows(NoICERecordException.class, () -> aliasMgr.addAlias(request));

			assertEquals(NO_RECORD, noICERecordException.getMessage());
		}

		@Test
		void shouldRejectIfAliasAlreadyExists() {
			AliasAddRequest request = createAliasRequest();
			given(artistRepo.findArtistById(any())).willReturn(Optional.of(createArtist(request)));

			DuplicateICERecordException dupeException =
					assertThrows(DuplicateICERecordException.class, () -> aliasMgr.addAlias(request));
			assertEquals(DUPLICATE_RECORD, dupeException.getMessage());
		}


		@Test
		void shouldRejectAddIfMaxAliasSizeIsReached() {
			AliasAddRequest request = createAliasRequest();
			given(artistRepo.findArtistById(any())).willReturn(Optional.of(createArtist(request)));

			DuplicateICERecordException dupeException =
					assertThrows(DuplicateICERecordException.class, () -> aliasMgr.addAlias(request));
			assertEquals(DUPLICATE_RECORD, dupeException.getMessage());
		}

		@Test
		void shouldAddAliasSuccessfully() {
			Artist artist = createArtist(List.of("old-name-1", "old-name-2"));
			given(artistRepo.findArtistById(any())).willReturn(Optional.of(artist));

			aliasMgr.addAlias(createAliasRequest());

			verify(artistRepo, times(1)).saveArtistAlias(artist);
		}
	}

	@Nested
	@DisplayName("when an alias is being updated")
	class UpdateAlias {
		@Test
		void shouldRejectIfArtistDoesNotExist() {
			NoICERecordException noICERecordException =
					assertThrows(NoICERecordException.class, () -> aliasMgr.updateAlias(createUpdateAliasRequest()));

			assertEquals(NO_RECORD, noICERecordException.getMessage());
		}

		@Test
		void shouldUpdateAliasSuccessfully() {
			Artist artist = createArtist(List.of("alias1", "alias2"));
			given(artistRepo.findArtistById(any())).willReturn(Optional.of(artist));

			aliasMgr.updateAlias(createUpdateAliasRequest());

			assertThat(artist.getAliases()).doesNotContain("alias1");
			assertThat(artist.getAliases()).contains("aliasNew", "alias2");
			verify(artistRepo, times(1)).saveArtistAlias(any());
		}

		private AliasUpdateRequest createUpdateAliasRequest() {
			AliasUpdateRequest request = new AliasUpdateRequest();
			request.setPreferred(true);
			request.setOldAlias("alias1");
			request.setNewAlias("aliasNew");
			request.setArtistId(ARTIST_ID);
			return request;
		}
	}


	@Nested
	@DisplayName("when an alias is being removed")
	class RemoveAlias {

		@Test
		void shouldRejectIfOneOrLessAliasesExist() {
			Artist artist = createArtist(List.of("alias1", "alias2"));
			given(artistRepo.findArtistById(any())).willReturn(Optional.of(artist));

			aliasMgr.removeAlias(createAliasRemoveRequest());
		}

		@Test
		void shouldRemoveIfExists() {
			Artist artist = createArtist(List.of("alias1", "alias2"));
			given(artistRepo.findArtistById(any())).willReturn(Optional.of(artist));

			aliasMgr.removeAlias(createAliasRemoveRequest());
			assertThat(artist.getAliases()).hasSize(1)
			                               .contains("alias2");
			verify(artistRepo, times(1)).saveArtistAlias(artist);
		}

		private static AliasRemoveRequest createAliasRemoveRequest() {
			AliasRemoveRequest request = new AliasRemoveRequest();
			request.setAlias("alias1");
			request.setArtistId(ARTIST_ID);
			return request;
		}
	}


	private static AliasAddRequest createAliasRequest() {
		AliasAddRequest request = new AliasAddRequest();
		request.setAlias("alias1");
		request.setArtistId(ARTIST_ID);
		return request;
	}


	private static Artist createArtist(AliasAddRequest request) {
		Artist artist = new Artist();
		artist.setArtistName("a-name");
		artist.setAliases(Set.of(request.getAlias()));
		return artist;
	}

	private static Artist createArtist(List<String> aliases) {
		Artist artist = new Artist();
		artist.setArtistName(aliases.get(0));
		artist.setAliases(new HashSet<>());
		for (String alias : aliases) {
			artist.getAliases().add(alias);
		}
		return artist;
	}
}