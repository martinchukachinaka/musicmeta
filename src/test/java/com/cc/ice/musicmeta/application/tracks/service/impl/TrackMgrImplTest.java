package com.cc.ice.musicmeta.application.tracks.service.impl;

import com.cc.ice.musicmeta.application.tracks.repo.TrackRepo;
import com.cc.ice.musicmeta.application.tracks.request.TrackRequest;
import com.cc.ice.musicmeta.infrastructure.errors.DuplicateICERecordException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.cc.ice.musicmeta.application.tracks.service.impl.TrackMgrImpl.INVALID_TRACK;
import static com.cc.ice.musicmeta.infrastructure.AppConstants.DUPLICATE_RECORD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class TrackMgrImplTest {

	public static final UUID TEST_UUID = UUID.fromString("66a61c3b-0f94-48c9-9d96-4629da5dcbb7");

	TrackMgrImpl trackMgr;

	TrackRepo trackRepo;

	@BeforeEach
	void setup() {
		trackRepo = mock(TrackRepo.class);
		trackMgr = new TrackMgrImpl(trackRepo);
	}

	@Test
	void shouldRejectAdditionIfNullTrackProvided() {
		NullPointerException exception =
				assertThrows(NullPointerException.class, () -> trackMgr.addNewTrack(null));
		assertEquals(INVALID_TRACK, exception.getMessage());
	}

	@Test
	void shouldRejectAdditionIfInvalidTrackName() {
		TrackRequest request = TrackRequest.builder().artistAlias("MJ").artistId(TEST_UUID).build();
		IllegalArgumentException exception =
				assertThrows(IllegalArgumentException.class, () -> trackMgr.addNewTrack(request));
		assertEquals("invalid track name", exception.getMessage());
	}

	@Test
	void shouldRejectAdditionIfInvalidArtistSupplied() {
		TrackRequest request1 = TrackRequest.builder().trackName("track1").artistAlias("MJ").build();
		TrackRequest request2 = TrackRequest.builder().trackName("track1").artistId(TEST_UUID).build();

		SoftAssertions softly = new SoftAssertions();
		softly.assertThatThrownBy(() -> trackMgr.addNewTrack(request1)).isInstanceOf(NullPointerException.class)
		      .hasMessageContaining("invalid artist ref");
		softly.assertThatThrownBy(() -> trackMgr.addNewTrack(request2)).isInstanceOf(IllegalArgumentException.class)
		      .hasMessageContaining("invalid artist alias");

		softly.assertAll();
	}

	@Test
	void shouldRejectAdditionIfTrackAlreadyExists() {
		given(trackRepo.trackExistsForArtist(any(), any())).willReturn(true);
		TrackRequest request = createValidRequest();

		DuplicateICERecordException exception =
				assertThrows(DuplicateICERecordException.class, () -> trackMgr.addNewTrack(request));
		assertEquals(DUPLICATE_RECORD, exception.getMessage());
	}

	@Test
	void shouldSuccessAddATrack() {
		TrackRequest request = createValidRequest();
		UUID uuid = trackMgr.addNewTrack(request);

		assertNotNull(uuid);
		verify(trackRepo, times(1)).addNewTrack(any());
	}

	private static TrackRequest createValidRequest() {
		return TrackRequest.builder()
		                   .trackName("track1")
		                   .artistAlias("MJ")
		                   .artistId(TEST_UUID)
		                   .build();
	}
}