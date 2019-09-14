package com.a6raywa1cher.javahackbugslimited.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@Component
@ConfigurationProperties(prefix = "app")
@Data
public class AppConfigProperties {
	@NotBlank
	private String baseUrl;

	@NotBlank
	private String myUrl;

	@NotBlank
	private String jwtTokenKey;
}
