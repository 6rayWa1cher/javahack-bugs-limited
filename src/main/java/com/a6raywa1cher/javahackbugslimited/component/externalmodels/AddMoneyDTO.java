package com.a6raywa1cher.javahackbugslimited.component.externalmodels;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddMoneyDTO {
	private BigDecimal money;
	private Boolean safeTransfer;
}
