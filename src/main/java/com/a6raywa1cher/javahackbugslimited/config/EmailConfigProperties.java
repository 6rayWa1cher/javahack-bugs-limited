package com.a6raywa1cher.javahackbugslimited.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.email")
@Data
public class EmailConfigProperties {
	private String host;
	private Integer port;
	private String fullEmail;
	private String username;
	private String password;
}
