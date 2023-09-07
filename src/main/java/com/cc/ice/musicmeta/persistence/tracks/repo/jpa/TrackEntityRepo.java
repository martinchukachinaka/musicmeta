package com.cc.ice.musicmeta.persistence.tracks.repo.jpa;

import com.cc.ice.musicmeta.persistence.tracks.model.TrackEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TrackEntityRepo extends JpaRepository<TrackEntity, UUID> {

	boolean existsByTrackNameAndArtistId(String trackName, UUID artistId);

	Page<TrackEntity> findByArtistId(@Param("artistId") UUID artistId, Pageable page);
}
