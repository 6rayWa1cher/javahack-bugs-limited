package com.a6raywa1cher.javahackbugslimited.component.externalmodels;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserMirror {
	private Integer id;

	private String login;

	private List<AccountMirror> accounts;

	private AccountMirror favoriteAccount;

	public UserMirror(Integer id) {
		this.id = id;
	}
}
