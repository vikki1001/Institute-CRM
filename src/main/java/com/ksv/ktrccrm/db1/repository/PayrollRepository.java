package com.ksv.ktrccrm.db1.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ksv.ktrccrm.db1.entities.PayrollMst;

@Transactional
public interface PayrollRepository extends JpaRepository<PayrollMst, Long> {
	
	@Query("SELECT u FROM PayrollMst u WHERE MONTH(u.date) = :month AND YEAR(u.date) = :year AND u.empId = :empId AND u.tenantId=:tenantId AND u.isActive=:isActive")
	public List<PayrollMst> getPayrollSlipByMonthAndYear(Integer month, Integer year, String empId,String tenantId, String isActive);
	
	@Query("SELECT u FROM PayrollMst u WHERE u.isActive = :isActive AND u.tenantId = :tenantId ORDER BY u.date DESC")
	public List<PayrollMst> getRecordList(String isActive, String tenantId);
	
	
}
