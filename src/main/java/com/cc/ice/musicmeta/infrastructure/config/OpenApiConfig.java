package com.cc.ice.musicmeta.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI iceMusicOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("ICE Music Metadata Service")
				                .description("ICE Music Metadata Service for Artists!")
				                .version("1.0"));
	}
}