package com.ksv.ktrccrm.db1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ksv.ktrccrm.db1.entities.SequenceMst;

public interface SequenceMstRepository extends JpaRepository<SequenceMst, Long> {

	/* User/Edit Registration */
	@Query("SELECT u FROM SequenceMst u WHERE u.isActive = :isActive")
	public List<SequenceMst> getTenantId(String isActive);

	@Query("SELECT u FROM SequenceMst u WHERE u.tenantId = :tenantId")
	public Optional<SequenceMst> getSequenceKey(String tenantId);

}
