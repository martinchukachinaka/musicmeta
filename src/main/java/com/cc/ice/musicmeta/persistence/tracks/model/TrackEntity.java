package com.cc.ice.musicmeta.persistence.tracks.model;

import com.cc.ice.musicmeta.domain.tracks.Track;
import com.cc.ice.musicmeta.domain.tracks.TrackArtist;
import com.cc.ice.musicmeta.persistence.artists.model.ArtistEntity;
import com.cc.ice.musicmeta.persistence.common.BaseEntity;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tracks", uniqueConstraints = {
		@UniqueConstraint(name = "ice_track_per_artist", columnNames = {"artist_id", "trackName"})
})
@Getter
@Setter
@NoArgsConstructor
public class TrackEntity extends BaseEntity {


	private String trackName;

	private int duration;

	private boolean explicitContent;

	@ManyToOne
	private ArtistEntity artist;

	private String artistAlias;

	@ElementCollection
	private Set<String> songWriters = new HashSet<>();

	private UUID album;

	private String trackImageRef;


	public TrackEntity(Track track) {
		setId(track.getId());
		setCreatedAt(track.getCreatedAt());
		setLastUpdatedAt(track.getLastUpdatedAt());
		setTrackName(track.getTrackName());
		setDuration(track.getDuration());
		setExplicitContent(track.isExplicitContent());
		setSongWriters(track.getSongWriters());
		setAlbum(track.getAlbum());
		setTrackImageRef(track.getTrackImageRef());

		//add artist
		setArtistAlias(track.getArtist().getArtistAlias());
		ArtistEntity artist = new ArtistEntity();
		artist.setId(track.getArtist().getArtistRef());

		setArtist(artist);
	}

	public Track buildTrack() {
		Track track = new Track();
		track.setId(id);
		track.setCreatedAt(createdAt);
		track.setLastUpdatedAt(lastUpdatedAt);
		track.setTrackName(trackName);
		track.setDuration(duration);
		track.setExplicitContent(explicitContent);
		track.setAlbum(album);
		track.setTrackImageRef(trackImageRef);
		track.setArtist(new TrackArtist(artist.getArtistName(), artist.getId()));
		return track;
	}
}
