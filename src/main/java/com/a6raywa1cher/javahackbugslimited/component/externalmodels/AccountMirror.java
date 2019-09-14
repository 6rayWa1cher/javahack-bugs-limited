package com.a6raywa1cher.javahackbugslimited.component.externalmodels;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountMirror {
	private Integer id;

	private BigDecimal money;

	private Boolean frozenBySafeTransfer;

	private UserMirror user;
}
