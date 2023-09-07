package com.cc.ice.musicmeta.application.artists.request;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistRequest {

	@NotNull
	@Max(200)
	private String biography;

	private String avatarRef;

	@NotEmpty(message = "artist must have at least one alias")
	private List<String> aliases;
}
