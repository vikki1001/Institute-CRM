package com.ksv.ktrccrm.db1.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ksv.ktrccrm.db1.entities.AuditRecord;
@Transactional
public interface AuditRecordRepository extends JpaRepository<AuditRecord, Long> {

	@Query("SELECT u FROM AuditRecord u WHERE DATE(u.createdDate) between DATE(:from) AND DATE(:to) AND u.roleCode = :roleCode ORDER BY u.createdDate DESC")
	public List<AuditRecord> findByFromToDateBetweenOrRoleCode(@Param("from") Date from, @Param("to") Date to,
			@Param("roleCode") String roleCode);

	@Query("SELECT u FROM AuditRecord u WHERE DATE(u.createdDate) between DATE(:from) AND DATE(:to) AND u.loginId = :loginId ORDER BY u.createdDate DESC")
	public List<AuditRecord> findByFromToDateBetweenOrLoginId(@Param("from") Date from, @Param("to") Date to,
			@Param("loginId") String loginId);

	@Query("SELECT u FROM AuditRecord u WHERE DATE(u.createdDate) between DATE(:from) AND DATE(:to) AND u.loginId = :loginId AND u.branchCode =:branchName ORDER BY u.createdDate DESC")
	public List<AuditRecord> findByFromToDateBetweenOrLoginIdOrBranchName(@Param("from") Date from,
			@Param("to") Date to, @Param("loginId") String loginId, @Param("branchName") String branchName);

	@Query("SELECT u FROM AuditRecord u WHERE DATE(u.createdDate) between DATE(:from) AND DATE(:to) ORDER BY u.createdDate DESC")
	public List<AuditRecord> findAll(@Param("from") Date from, @Param("to") Date to);
	
	@Query("SELECT u FROM AuditRecord u WHERE DATE(u.createdDate) between DATE(:from) AND DATE(:to) AND u.branchCode = :branchName ORDER BY u.createdDate DESC")
	public List<AuditRecord> findByFromToDateBetweenOrBranchCode(@Param("from") Date from, @Param("to") Date to,
			@Param("branchName") String branchName);


}