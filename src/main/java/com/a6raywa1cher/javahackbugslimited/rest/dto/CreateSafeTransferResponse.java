package com.a6raywa1cher.javahackbugslimited.rest.dto;

import com.a6raywa1cher.javahackbugslimited.models.SafeTransfer;
import lombok.Data;

@Data
public class CreateSafeTransferResponse {
	private SafeTransfer safeTransfer;
	private String linkForSecondUser;

}
