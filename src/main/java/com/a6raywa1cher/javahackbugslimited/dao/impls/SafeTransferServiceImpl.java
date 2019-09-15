package com.a6raywa1cher.javahackbugslimited.dao.impls;

import com.a6raywa1cher.javahackbugslimited.dao.repository.SafeTransferRepository;
import com.a6raywa1cher.javahackbugslimited.dao.services.SafeTransferService;
import com.a6raywa1cher.javahackbugslimited.models.SafeTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SafeTransferServiceImpl implements SafeTransferService {
	private SafeTransferRepository repository;

	@Autowired
	public SafeTransferServiceImpl(SafeTransferRepository repository) {
		this.repository = repository;
	}

	@Override
	public SafeTransfer save(SafeTransfer safeTransfer) {
		return repository.save(safeTransfer);
	}

	@Override
	public Optional<SafeTransfer> getById(Integer id) {
		return repository.findById(id);
	}

	@Override
	public List<SafeTransfer> findAllByUserId(Integer userId) {
		return repository.findAllByReceiverUserId(userId);
	}
}
