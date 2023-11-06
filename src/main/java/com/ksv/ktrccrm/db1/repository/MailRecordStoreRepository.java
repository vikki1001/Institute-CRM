package com.ksv.ktrccrm.db1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ksv.ktrccrm.db1.entities.MailRecordStore;

public interface MailRecordStoreRepository extends JpaRepository<MailRecordStore, Long> {

	@Query("SELECT u FROM MailRecordStore u WHERE u.status = :status")
	public List<MailRecordStore> getPendingRecords(String status);

}
