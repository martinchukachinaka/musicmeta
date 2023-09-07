package com.cc.ice.musicmeta.persistence.topartist.repo.jpa;

import com.cc.ice.musicmeta.persistence.topartist.model.TopArtistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TopArtistEntityRepo extends JpaRepository<TopArtistEntity, Long> {

	Optional<TopArtistEntity> findTop1ByCreatedAtOrderByIdDesc(LocalDate createdAt);

	Optional<TopArtistEntity> findFirstByOrderByIdDesc();
}
