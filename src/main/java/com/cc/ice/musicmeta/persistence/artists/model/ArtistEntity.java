package com.cc.ice.musicmeta.persistence.artists.model;

import com.cc.ice.musicmeta.domain.artists.Artist;
import com.cc.ice.musicmeta.persistence.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "artists")
@Getter
@Setter
@NoArgsConstructor
public class ArtistEntity extends BaseEntity {

	private String artistName;

	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> aliases = new HashSet<>();

	@Column(columnDefinition = "TEXT")
	private String biography;

	private String avatarRef;

	public ArtistEntity(Artist artist) {
		setId(artist.getId());
		setArtistName(artist.getArtistName());
		setBiography(artist.getBiography());
		setAvatarRef(artist.getAvatarRef());
		setAliases(artist.getAliases());
		setCreatedAt(artist.getCreatedAt());
		setLastUpdatedAt(artist.getLastUpdatedAt());
	}

	public Artist createArtist() {
		Artist artist = new Artist();
		artist.setId(id);
		artist.setArtistName(artistName);
		artist.setAvatarRef(avatarRef);
		artist.setBiography(biography);
		artist.setLastUpdatedAt(lastUpdatedAt);
		artist.setCreatedAt(createdAt);
		artist.setAliases(aliases);
		return artist;
	}
}
