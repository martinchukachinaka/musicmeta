package com.cc.ice.musicmeta.application.artists.request;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AliasAddRequest {

	@NotNull
	private UUID artistId;

	@NotNull
	private String alias;

	private boolean preferred;
}
