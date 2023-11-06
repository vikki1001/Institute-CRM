package com.ksv.ktrccrm.db1.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ksv.ktrccrm.db1.entities.KRA;

@Transactional
public interface KRARepository extends JpaRepository<KRA, Long> {

	@Query("SELECT u FROM KRA u WHERE u.isActive=:isActive AND u.tenantId=:tenantId ORDER BY createdDate DESC")
	public List<KRA> getRecordList(String isActive, String tenantId);

	@Query("SELECT u FROM KRA u WHERE u.department = :depId AND u.isActive = :isActive ORDER BY createdDate DESC")
	public List<KRA> getKRAWithDepIdAndEmpId(String depId, String isActive);
	
	@Query("SELECT u FROM KRA u WHERE u.kraName = :kra")
	public KRA getKRA(String kra);
}
