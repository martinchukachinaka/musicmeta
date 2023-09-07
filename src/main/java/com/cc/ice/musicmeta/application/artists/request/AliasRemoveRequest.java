package com.cc.ice.musicmeta.application.artists.request;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AliasRemoveRequest {

	@NotNull
	private UUID artistId;

	@NotNull
	private String alias;
}
