package com.cc.ice.musicmeta.application.tracks.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackRequest {

	@NotBlank
	private String trackName;

	@NotBlank
	private String artistAlias;

	private int duration;

	private boolean explicitContent;

	@JsonIgnore
	private UUID artistId;
}
