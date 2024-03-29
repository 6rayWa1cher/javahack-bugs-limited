package com.a6raywa1cher.javahackbugslimited.rest;

import com.a6raywa1cher.javahackbugslimited.component.EmailService;
import com.a6raywa1cher.javahackbugslimited.component.ExternalRest;
import com.a6raywa1cher.javahackbugslimited.component.JwtComponent;
import com.a6raywa1cher.javahackbugslimited.component.externalmodels.AccountMirror;
import com.a6raywa1cher.javahackbugslimited.config.AppConfigProperties;
import com.a6raywa1cher.javahackbugslimited.dao.services.SafeTransferService;
import com.a6raywa1cher.javahackbugslimited.models.SafeTransfer;
import com.a6raywa1cher.javahackbugslimited.models.enumerations.SafeTransferStatus;
import com.a6raywa1cher.javahackbugslimited.rest.dto.CreateSafeTransferDTO;
import com.a6raywa1cher.javahackbugslimited.rest.dto.CreateSafeTransferResponse;
import com.a6raywa1cher.javahackbugslimited.rest.dto.IsMoneyArrivedResponse;
import com.a6raywa1cher.javahackbugslimited.rest.dto.PutEmailDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("")
public class SafeTransferController {
	private SafeTransferService safeTransferService;
	private ExternalRest externalRest;
	private JwtComponent jwtComponent;
	private EmailService emailService;
	private AppConfigProperties properties;

	@Autowired
	public SafeTransferController(SafeTransferService safeTransferService, ExternalRest externalRest,
	                              JwtComponent jwtComponent, EmailService emailService, AppConfigProperties properties) {
		this.safeTransferService = safeTransferService;
		this.externalRest = externalRest;
		this.jwtComponent = jwtComponent;
		this.emailService = emailService;
		this.properties = properties;
	}

	@PostMapping("/create")
	public ResponseEntity<CreateSafeTransferResponse> createSafeTransfer(@RequestBody @Valid CreateSafeTransferDTO dto) {
		SafeTransfer safeTransfer = new SafeTransfer();
		safeTransfer.setMemo(dto.getMemo());
		safeTransfer.setReceiverUserId(dto.getUserId());
		safeTransfer.setExternalDestinationAccount(externalRest.getUser(dto.getUserId()).getFavoriteAccount().getId());
		safeTransfer.setExternalFrozenAccount(externalRest.regAccount(dto.getUserId(), true).getId());
		safeTransfer.setStatus(SafeTransferStatus.CREATED);
		safeTransfer.setTargetPrice(dto.getTargetPrice());
		SafeTransfer saved = safeTransferService.save(safeTransfer);

		ObjectMapper objectMapper = new ObjectMapper();
		String payloadOfLink = objectMapper.createObjectNode()
				.put("transferId", saved.getId())
				.toString();
		String token = jwtComponent.create("cst", payloadOfLink);
		CreateSafeTransferResponse response = new CreateSafeTransferResponse();
		response.setSafeTransfer(saved);
		response.setLinkForSecondUser(properties.getMyUrl() + "/customer/fill_email?token=" +
				token + "&accountId=" + safeTransfer.getExternalFrozenAccount());
		return ResponseEntity.ok(response);
	}

	@PostMapping("/{transferId}/new_email_link")
	public ResponseEntity<CreateSafeTransferResponse> createNewEmailLink(@PathVariable Integer transferId) {
		Optional<SafeTransfer> optionalSafeTransfer = safeTransferService.getById(transferId);
		if (optionalSafeTransfer.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		SafeTransfer safeTransfer = optionalSafeTransfer.get();
		ObjectMapper objectMapper = new ObjectMapper();
		String payloadOfLink = objectMapper.createObjectNode()
				.put("transferId", safeTransfer.getId())
				.toString();
		String token = jwtComponent.create("cst", payloadOfLink);
		CreateSafeTransferResponse response = new CreateSafeTransferResponse();
		response.setSafeTransfer(safeTransfer);
		response.setLinkForSecondUser(properties.getMyUrl() + "/customer/fill_email?token=" +
				token + "&accountId=" + safeTransfer.getExternalFrozenAccount());
		return ResponseEntity.ok(response);
	}

	@GetMapping("/get_by_id/{transferId}")
	public ResponseEntity<SafeTransfer> getById(@PathVariable Integer transferId) {
		Optional<SafeTransfer> optionalSafeTransfer = safeTransferService.getById(transferId);
		if (optionalSafeTransfer.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(optionalSafeTransfer.get());
	}

	@GetMapping("/get_by_user/{userId}")
	public ResponseEntity<List<SafeTransfer>> getAllTransfers(@PathVariable Integer userId) {
		List<SafeTransfer> safeTransfers = safeTransferService.findAllByUserId(userId);
		return ResponseEntity.ok(safeTransfers);
	}

	@PostMapping("/put_email")
	public ResponseEntity<SafeTransfer> putEmail(@RequestBody @Valid PutEmailDTO dto) throws IOException {
		Optional<String> additionalInfo = jwtComponent.decode("cst", dto.getToken());
		if (additionalInfo.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		int transferId = new ObjectMapper().readTree(additionalInfo.get())
				.get("transferId").asInt();
		SafeTransfer transfer = safeTransferService.getById(transferId).orElseThrow();
		transfer.setEmail(dto.getEmail());
		return ResponseEntity.ok(safeTransferService.save(transfer));
	}

	@PostMapping("/{transferId}/check")
	public ResponseEntity<?> setCheck(@RequestParam(name = "value", defaultValue = "true") boolean val,
	                                  @PathVariable Integer transferId) {
		Optional<SafeTransfer> optionalSafeTransfer = safeTransferService.getById(transferId);
		if (optionalSafeTransfer.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		SafeTransfer safeTransfer = safeTransferService.getById(transferId).orElseThrow();
		AccountMirror accountMirror = externalRest.getById(safeTransfer.getExternalFrozenAccount());
		if (safeTransfer.getStatus() == SafeTransferStatus.FINALIZED || accountMirror.getMoney()
				.compareTo(safeTransfer.getTargetPrice()) < 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.body("{}");
		}
		if (val) {
			ObjectMapper objectMapper = new ObjectMapper();
			String payloadOfLink = objectMapper.createObjectNode()
					.put("transferId", safeTransfer.getId())
					.toString();
			safeTransfer.setStatus(SafeTransferStatus.CHECKED);
			emailService.sendSimpleMessage(safeTransfer.getEmail(), "Подтвердите оказание услуги",
					"Нажмите на эту ссылку: " + properties.getMyUrl() + "/customer/finalize?token=" +
							jwtComponent.create("flz", payloadOfLink));
		} else {
			safeTransfer.setStatus(SafeTransferStatus.CREATED);
		}
		return ResponseEntity.ok(safeTransferService.save(safeTransfer));
	}

	@GetMapping("/finalize")
	public ResponseEntity<SafeTransfer> finalizeTransfer(@RequestParam(name = "token") String token) throws IOException {
		Optional<String> additionalInfo = jwtComponent.decode("flz", token);
		if (additionalInfo.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		int transferId = new ObjectMapper().readTree(additionalInfo.get())
				.get("transferId").asInt();
		SafeTransfer transfer = safeTransferService.getById(transferId).orElseThrow();
		externalRest.transferMoney(transfer.getExternalFrozenAccount(), transfer.getExternalDestinationAccount(),
				transfer.getTargetPrice());
		transfer.setStatus(SafeTransferStatus.FINALIZED);
		return ResponseEntity.ok(safeTransferService.save(transfer));
	}

	@GetMapping("/money_status/{transferId}")
	public ResponseEntity<IsMoneyArrivedResponse> isMoneyArrived(@PathVariable Integer transferId) {
		Optional<SafeTransfer> optionalSafeTransfer = safeTransferService.getById(transferId);
		if (optionalSafeTransfer.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		SafeTransfer safeTransfer = optionalSafeTransfer.get();
		AccountMirror accountMirror = externalRest.getById(safeTransfer.getExternalFrozenAccount());
		IsMoneyArrivedResponse response = new IsMoneyArrivedResponse();
		response.setIsArrived(accountMirror.getMoney()
				.compareTo(safeTransfer.getTargetPrice()) >= 0);
		return ResponseEntity.ok(response);
	}
}
