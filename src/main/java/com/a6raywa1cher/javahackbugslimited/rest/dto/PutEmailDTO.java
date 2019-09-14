package com.a6raywa1cher.javahackbugslimited.rest.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PutEmailDTO {
	@NotBlank
	private String email;
	@NotBlank
	private String token;
}
