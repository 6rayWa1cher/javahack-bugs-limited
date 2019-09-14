package com.a6raywa1cher.javahackbugslimited.component.externalmodels;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AccountCreationDTO {
	@NotNull
	private Integer userId;

	private boolean isFrozenBySafeTransfer;
}
