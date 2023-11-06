package com.ksv.ktrccrm.db1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ksv.ktrccrm.db1.entities.EmailTemplate;

public interface EmailRepository extends JpaRepository<EmailTemplate, Long> {

	@Query("SELECT e FROM EmailTemplate e WHERE e.id = :id")
	public Optional<EmailTemplate> findByid(Long id);

	@Query("SELECT u FROM EmailTemplate u WHERE u.isActive=:isActive AND u.tenantId=:tenantId")
	public List<EmailTemplate> getRecordList(String isActive, String tenantId);
	
	@Query("SELECT u FROM EmailTemplate u WHERE u.isActive = :isActive  AND u.templateName = :templateName")
	public EmailTemplate getEmailTemplate(String isActive, String templateName);

	@Query("SELECT u FROM EmailTemplate u WHERE u.id=:id")
	public EmailTemplate getDataById(Long id);

}	