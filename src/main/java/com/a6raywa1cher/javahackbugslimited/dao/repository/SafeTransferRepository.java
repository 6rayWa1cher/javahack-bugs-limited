package com.a6raywa1cher.javahackbugslimited.dao.repository;

import com.a6raywa1cher.javahackbugslimited.models.SafeTransfer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SafeTransferRepository extends CrudRepository<SafeTransfer, Integer> {
	List<SafeTransfer> findAllByReceiverUserId(Integer receiverUserId);
}
