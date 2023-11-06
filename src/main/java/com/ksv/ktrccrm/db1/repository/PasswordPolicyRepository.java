package com.ksv.ktrccrm.db1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ksv.ktrccrm.db1.entities.PasswordPolicy;

public interface PasswordPolicyRepository extends JpaRepository<PasswordPolicy, Long> {
   
	
	@Query("SELECT u FROM PasswordPolicy u WHERE u.isActive = :isActive AND u.tenantId = :tenantId")
	public PasswordPolicy getDataBytenantId(String isActive, String tenantId);

	@Query("SELECT u FROM PasswordPolicy u WHERE u.tenantId = :tenantId AND u.isActive = :isActive")
	public PasswordPolicy findByTenantIdAndIsactive(String tenantId, String isActive);

	@Query("SELECT u FROM PasswordPolicy u WHERE u.isActive = :isActive AND u.tenantId = :tenantId")
	public List<PasswordPolicy> getRecordList(String isActive, String tenantId);
}
