package com.a6raywa1cher.javahackbugslimited.component;


import com.a6raywa1cher.javahackbugslimited.component.externalmodels.AccountMirror;
import com.a6raywa1cher.javahackbugslimited.component.externalmodels.UserMirror;

import java.math.BigDecimal;
import java.util.List;

public interface ExternalRest {
	UserMirror getUser(Integer userId);

	UserMirror getUserByLoginAndPassword(String login, String password);

	List<AccountMirror> getAccountsByUser(Integer userId);

	AccountMirror addMoney(BigDecimal money, boolean isSafeTransfer);

	AccountMirror regAccount(Integer userId, boolean isSafeTransfer);

	AccountMirror getById(Integer id);

	void transferMoney(Integer from, Integer to, BigDecimal amount);
}
