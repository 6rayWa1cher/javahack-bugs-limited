package com.a6raywa1cher.javahackbugslimited.component;

import com.a6raywa1cher.javahackbugslimited.component.externalmodels.AccountCreationDTO;
import com.a6raywa1cher.javahackbugslimited.component.externalmodels.AccountMirror;
import com.a6raywa1cher.javahackbugslimited.component.externalmodels.TransferMoneyDTO;
import com.a6raywa1cher.javahackbugslimited.component.externalmodels.UserMirror;
import com.a6raywa1cher.javahackbugslimited.config.AppConfigProperties;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

public class RaiffEmulatorExternalRest implements ExternalRest {
	private RestTemplate restTemplate;
	private AppConfigProperties appConfigProperties;

	public RaiffEmulatorExternalRest(RestTemplate restTemplate, AppConfigProperties appConfigProperties) {
		this.restTemplate = restTemplate;
		this.appConfigProperties = appConfigProperties;
	}

	@Override
	public UserMirror getUser(Integer userId) {
		return restTemplate.getForEntity(URI.create(appConfigProperties.getBaseUrl()).resolve("user/" + userId),
				UserMirror.class)
				.getBody();
	}

	@Override
	public UserMirror getUserByLoginAndPassword(String login, String password) {
		return null;
	}

	@Override
	public List<AccountMirror> getAccountsByUser(Integer userId) {
		return null;
	}

	@Override
	public AccountMirror addMoney(BigDecimal money, boolean isSafeTransfer) {
		return null;
	}

	@Override
	public AccountMirror regAccount(Integer userId, boolean isSafeTransfer) {
		AccountCreationDTO dto = new AccountCreationDTO();
		dto.setUserId(userId);
		dto.setFrozenBySafeTransfer(isSafeTransfer);
		return restTemplate.postForEntity(URI.create(appConfigProperties.getBaseUrl()).resolve("account"),
				dto, AccountMirror.class).getBody();
	}

	@Override
	public AccountMirror getById(Integer id) {
		return restTemplate.getForEntity(URI.create(appConfigProperties.getBaseUrl()).resolve("account/" + id),
				AccountMirror.class).getBody();
	}

	@Override
	public void transferMoney(Integer from, Integer to, BigDecimal amount) {
		TransferMoneyDTO dto = new TransferMoneyDTO();
		dto.setFromId(from);
		dto.setToId(to);
		dto.setAmount(amount);
		restTemplate.postForLocation(URI.create(appConfigProperties.getBaseUrl()).resolve("account/transfer"),
				dto);
	}
}
