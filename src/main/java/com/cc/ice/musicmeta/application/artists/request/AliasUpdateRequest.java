package com.cc.ice.musicmeta.application.artists.request;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AliasUpdateRequest {

	@NotNull
	private UUID artistId;

	@NotNull
	private String newAlias;

	@NotNull
	private String oldAlias;

	@NotNull
	private boolean preferred;
}
