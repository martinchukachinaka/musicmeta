package com.cc.ice.musicmeta.persistence.artists.repo.jpa;

import com.cc.ice.musicmeta.persistence.artists.model.ArtistEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface ArtistEntityRepo extends JpaRepository<ArtistEntity, UUID> {

	@Query("select a from ArtistEntity a where a.id <> :artistRef and a.createdAt >= :createdAt order by a.createdAt asc")
	List<ArtistEntity> findNextArtistByCreationDate(@Param("artistRef") UUID artistRef,
	                                                @Param("createdAt") LocalDateTime artistCreationDate,
	                                                Pageable page);


	default Optional<ArtistEntity> findNextArtistByCreationDate(UUID artistRef, LocalDateTime artistCreationDate) {
		return Optional.ofNullable(findNextArtistByCreationDate(artistRef, artistCreationDate, PageRequest.of(0, 1)))
		               .map(rec -> rec.isEmpty() ? null : rec.get(0));
	}

	Optional<ArtistEntity> findFirstByOrderByCreatedAtAsc();
}
