package com.cc.ice.musicmeta.persistence.topartist.model;


import com.cc.ice.musicmeta.domain.topartist.TopArtist;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "top_artist")
@Getter
@Setter
@NoArgsConstructor
public class TopArtistEntity {

	@Id
	@GeneratedValue
	private Long id;

	private UUID artistRef;

	private String artistName;

	private LocalDateTime artistCreationDate;

	private String biography;

	private LocalDate createdAt;


	public TopArtistEntity(TopArtist artist) {
		artistName = artist.getArtistName();
		artistRef = artist.getArtistRef();
		artistCreationDate = artist.getArtistCreationDate();
		biography = artist.getBiography();
		createdAt = artist.getCreatedAt();
	}


	public TopArtist createTopArtist() {
		TopArtist artist = new TopArtist();
		artist.setArtistName(artistName);
		artist.setCreatedAt(createdAt);
		artist.setArtistCreationDate(artistCreationDate);
		artist.setBiography(biography);
		artist.setArtistRef(artistRef);
		return artist;
	}
}
