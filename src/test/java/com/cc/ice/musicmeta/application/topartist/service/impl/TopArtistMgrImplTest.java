package com.cc.ice.musicmeta.application.topartist.service.impl;

import com.cc.ice.musicmeta.application.artists.repo.ArtistRepo;
import com.cc.ice.musicmeta.application.topartist.repo.TopArtistRepo;
import com.cc.ice.musicmeta.application.topartist.response.TopArtistResponse;
import com.cc.ice.musicmeta.domain.artists.Artist;
import com.cc.ice.musicmeta.domain.topartist.TopArtist;
import com.cc.ice.musicmeta.infrastructure.errors.NoICERecordException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TopArtistMgrImplTest {

	@InjectMocks
	TopArtistMgrImpl topArtistMgr;

	@Mock
	ArtistRepo artistRepo;

	@Mock
	TopArtistRepo topArtistRepo;

	public static final UUID TEST_UUID = UUID.fromString("a1878edb-1865-45e3-9f62-0bcf756b6891");


	@Test
	void shouldRejectIfThereAreNoArtists() {
		NoICERecordException exception =
				assertThrows(NoICERecordException.class, () -> topArtistMgr.findNextTopArtist());
		assertEquals("no artists available", exception.getMessage());
	}

	@Test
	void shouldReturnArtistOfTheDayIfAlreadyComputed() {
		LocalDate creationDate = LocalDate.now();
		TopArtist artist = createNewTopArtist();
		artist.setCreatedAt(creationDate);

		given(artistRepo.artistsExists()).willReturn(true);
		given(topArtistRepo.findTopArtistByDate(any())).willReturn(Optional.of(artist));


		TopArtistResponse topArtist = topArtistMgr.findTopArtist();
		assertThat(topArtist).isNotNull();
		assertThat(topArtist.getCreatedAt()).isEqualTo(creationDate);
	}


	@Test
	void shouldCreateArtistOfTheDayIfNotComputed() {
		Artist artist = new Artist();
		artist.setArtistName("mj");
		artist.setBiography("thriller");
		artist.setId(TEST_UUID);
		given(artistRepo.artistsExists()).willReturn(true);
		given(artistRepo.findFirstCreatedArtist()).willReturn(Optional.of(artist));
		given(topArtistRepo.findTopArtistByDate(any())).willReturn(Optional.empty());
		
		TopArtistResponse topArtist = topArtistMgr.findTopArtist();

		verify(topArtistRepo, times(1)).saveTopArtist(any());
		assertThat(topArtist).isNotNull();
	}

	private static TopArtist createNewTopArtist() {
		TopArtist topArtist = new TopArtist();
		topArtist.setArtistName("mj");
		topArtist.setBiography("you are not alone");
		return topArtist;
	}
}