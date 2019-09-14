package com.a6raywa1cher.javahackbugslimited.models;

import com.a6raywa1cher.javahackbugslimited.models.enumerations.SafeTransferStatus;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class SafeTransfer {
	@Id
	@GeneratedValue
	private Integer id;

	@Column
	private BigDecimal targetPrice;

	@Column
	private String memo;

	@Column
	@Enumerated(EnumType.STRING)
	private SafeTransferStatus status;

	@Column
	private Integer receiverUserId;

	@Column
	private String email;

	@Column
	private Integer externalFrozenAccount;

	@Column
	private Integer externalDestinationAccount;
}
