package com.cc.ice.musicmeta.domain.artists;

import com.cc.ice.musicmeta.application.artists.request.AliasAddRequest;
import com.cc.ice.musicmeta.application.artists.request.AliasRemoveRequest;
import com.cc.ice.musicmeta.application.artists.request.AliasUpdateRequest;
import com.cc.ice.musicmeta.application.artists.request.ArtistRequest;
import com.cc.ice.musicmeta.domain.common.BaseModel;
import com.cc.ice.musicmeta.infrastructure.errors.DuplicateICERecordException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

import static com.cc.ice.musicmeta.infrastructure.AppConstants.AT_LEAST_ONE_ALIAS;
import static com.cc.ice.musicmeta.infrastructure.AppConstants.DUPLICATE_RECORD;
import static com.cc.ice.musicmeta.infrastructure.AppConstants.MAX_ALIAS_REACHED;
import static com.cc.ice.musicmeta.infrastructure.AppConstants.NOT_AVAILABLE;
import static java.time.LocalDateTime.now;


@Getter
@Setter
@NoArgsConstructor
public class Artist extends BaseModel {

	private String artistName;

	private Set<String> aliases = new HashSet<>();

	private String biography;

	private String avatarRef;

	public Artist(ArtistRequest request) {
		super();
		if (CollectionUtils.isEmpty(request.getAliases())) {
			throw new IllegalArgumentException("No Artist name provided");
		}
		aliases.addAll(request.getAliases());
		artistName = request.getAliases().get(0);

		avatarRef = StringUtils.hasText(request.getAvatarRef()) ? request.getAvatarRef() : NOT_AVAILABLE;
		biography = StringUtils.hasText(request.getBiography()) ? request.getBiography() : NOT_AVAILABLE;
	}

	public void addAlias(AliasAddRequest request, int maxAllowedAliases) {
		if (aliases.size() >= maxAllowedAliases) {
			throw new IllegalStateException(MAX_ALIAS_REACHED);
		}
		if (aliases.contains(request.getAlias())) {
			throw new DuplicateICERecordException(DUPLICATE_RECORD);
		}
		aliases.add(request.getAlias());
		if (request.isPreferred()) {
			artistName = request.getAlias();
		}
		setLastUpdatedAt(now());
	}

	public void removeAlias(AliasRemoveRequest request) {
		if (aliases.size() <= 1) {
			throw new IllegalStateException(AT_LEAST_ONE_ALIAS);
		}
		aliases.remove(request.getAlias());
		if (request.getAlias().equals(artistName)) {
			aliases.stream().findFirst().ifPresent(alias -> artistName = alias);
		}
		setLastUpdatedAt(now());
	}

	public void updateAlias(AliasUpdateRequest request) {
		if (!aliases.contains(request.getOldAlias())) {
			return;
		}
		aliases.remove(request.getOldAlias());
		aliases.add(request.getNewAlias());
		if (artistName.equals(request.getOldAlias())) {
			artistName = request.getNewAlias();
		}
		if (request.isPreferred()) {
			artistName = request.getNewAlias();
		}
		setLastUpdatedAt(now());
	}
}
