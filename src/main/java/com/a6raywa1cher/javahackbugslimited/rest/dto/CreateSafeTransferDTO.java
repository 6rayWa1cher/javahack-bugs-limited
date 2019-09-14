package com.a6raywa1cher.javahackbugslimited.rest.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class CreateSafeTransferDTO {
	private String memo;

	@NotNull
	private BigDecimal targetPrice;

	@NotNull
	private Integer userId;
}
