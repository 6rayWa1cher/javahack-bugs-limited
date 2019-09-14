package com.a6raywa1cher.javahackbugslimited.dao.services;

import com.a6raywa1cher.javahackbugslimited.models.SafeTransfer;

import java.util.Optional;

public interface SafeTransferService {
	SafeTransfer save(SafeTransfer safeTransfer);

	Optional<SafeTransfer> getById(Integer id);
}
