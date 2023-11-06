package com.ksv.ktrccrm.db1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ksv.ktrccrm.db1.entities.SMSRecordStore;

public interface SMSRecordStoreRepository extends JpaRepository<SMSRecordStore, Long> {

	@Query("SELECT u FROM SMSRecordStore u WHERE u.status = :status")
	public List<SMSRecordStore> getPendingRecords(String status);
}
