package com.ksv.ktrccrm.db1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ksv.ktrccrm.db1.entities.DailyWorkReport;

public interface DailyWorkReportRepository extends JpaRepository<DailyWorkReport, Long> {

	@Query("SELECT u FROM DailyWorkReport u WHERE u.empId = :empId AND u.isActive=:isActive ORDER BY date DESC")
	public List<DailyWorkReport> getRecordList(String empId,String isActive);

	@Query("SELECT e FROM DailyWorkReport e WHERE e.id = :id")
	public Optional<DailyWorkReport> findByWorkReportId(Long id);
	
	@Query("SELECT e FROM DailyWorkReport e WHERE e.fullName = :fullName and e.isActive=:isActive")
	public Optional<DailyWorkReport> findByFullName(String fullName,String isActive);
	
	/* DashBoard Count for HR */
	@Query("SELECT count(u) FROM  DailyWorkReport u WHERE MONTH(u.date) = MONTH(CURRENT_DATE()) AND u.isActive = :isActive AND u.tenantId=:tenantId")
	public Long getAllWorkReport(String isActive, String tenantId);
	
	/* Link */
	@Query("SELECT u FROM DailyWorkReport u WHERE MONTH(u.date) = MONTH(CURRENT_DATE()) AND u.isActive = :isActive ORDER BY date DESC")
	public List<DailyWorkReport> getTotalWorkReport(String isActive);	

}
