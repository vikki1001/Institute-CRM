package com.ksv.ktrccrm.db1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ksv.ktrccrm.db1.entities.TenantMst;

public interface TenantMstRepository extends JpaRepository<TenantMst, Long> {

	@Query("SELECT u FROM TenantMst u WHERE u.tenantId = :tenantId AND u.isActive=:isActive")
	public TenantMst getTenantDetails(String tenantId,String isActive);
	
}
