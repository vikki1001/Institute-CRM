package com.ksv.ktrccrm.db1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ksv.ktrccrm.db1.entities.SMSTemplate;

public interface SMSRepository extends JpaRepository<SMSTemplate, Long> {

	@Query("SELECT e FROM SMSTemplate e WHERE e.id = :id")
	public Optional<SMSTemplate> findByid(Long id);

	@Query("SELECT u FROM SMSTemplate u WHERE u.isActive=:isActive AND u.tenantId=:tenantId")
	public List<SMSTemplate> getRecordList(String isActive,  String tenantId);

	@Query("SELECT u FROM SMSTemplate u WHERE u.id=:id")
	public SMSTemplate getDataById(Long id);

}
