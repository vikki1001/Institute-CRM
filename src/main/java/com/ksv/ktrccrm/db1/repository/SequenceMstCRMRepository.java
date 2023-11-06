package com.ksv.ktrccrm.db1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ksv.ktrccrm.db1.entities.SequenceMstCRM;


public interface SequenceMstCRMRepository extends JpaRepository<SequenceMstCRM, Long> {

	@Query("SELECT u FROM SequenceMstCRM u WHERE u.isActive = :isActive")
	public List<SequenceMstCRM> getTenantId(String isActive);

//	@Query("SELECT u FROM SequenceMstCRM u WHERE u.tenantId = :tenantId AND u.sequenceKey = :sequenceKey")
//	public Optional<SequenceMstCRM> getSequenceKey(String tenantId, String sequenceKey);
	
	@Query("SELECT u FROM SequenceMstCRM u WHERE u.sequenceKey = :sequenceKey")
	public Optional<SequenceMstCRM> getSequenceKey(String sequenceKey);
	
}
