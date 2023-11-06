package com.ksv.ktrccrm.db1.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ksv.ktrccrm.db1.entities.ExitActivity;

public interface ExitActivityRepository extends JpaRepository<ExitActivity, Long> {

	@Query("SELECT u FROM ExitActivity u WHERE u.empId.empId = :empId AND u.isActive = :isActive AND u.tenantId=:tenantId ORDER BY u.createdDate DESC")
	public List<ExitActivity> getRecordList(String empId,String isActive, String tenantId);

	@Query("SELECT u FROM ExitActivity u WHERE u.id = :id AND u.isActive = :isActive")
	public Optional<ExitActivity> cancelById(Long id, String isActive);

	@Query("SELECT u FROM ExitActivity u WHERE u.empId.empId = :empId AND u.isActive = :isActive")
	public List<ExitActivity> getExitActivityById(String empId, String isActive);

	@Query("SELECT u FROM ExitActivity u WHERE u.managerId = :empId AND u.isActive = :isActive AND u.tenantId=:tenantId ORDER BY u.createdDate DESC")
	public List<ExitActivity> getEmpWithManger(String empId, String isActive, String tenantId);

	@Query("SELECT u FROM ExitActivity u WHERE u.managerId = :empId AND u.isActive = :isActive AND u.status = :status AND u.tenantId=:tenantId ORDER BY u.createdDate DESC")
	public List<ExitActivity> getEmpWithMangerWithPending(String empId,String isActive, String status, String tenantId);

	@Transactional
	@Modifying
	@Query("update ExitActivity set status = :status, flag = :flag  where id = :id")
	public void acceptStatus(String status, String flag, Long id);

	@Query("SELECT u FROM ExitActivity u WHERE u.id = :id AND u.isActive = :isActive")
	public List<ExitActivity> acceptExitActivityById(Long id, String isActive);

	/* DashBoard Count for HR */
	@Query("SELECT count(*) FROM ExitActivity u WHERE MONTH(u.createdDate) = MONTH(CURRENT_DATE()) AND u.status = :status AND u.isActive = :isActive AND u.tenantId=:tenantId")
	public Long getAllExitActivity(String status,String isActive, String tenantId);
	
	/* Link */
	@Query("SELECT u FROM ExitActivity u WHERE MONTH(u.createdDate) = MONTH(CURRENT_DATE()) AND u.status = :status AND u.isActive = :isActive ORDER BY u.createdDate DESC")
	public List<ExitActivity> getTotalExitActivity(String status, String isActive);

	@Query("SELECT u FROM ExitActivity u WHERE u.notification = :notification AND u.isActive = :isActive AND u.managerId = :userId")
	public List<ExitActivity> unreadNotification(String userId, String notification,String isActive);

	@Query("SELECT u FROM ExitActivity u WHERE u.managerId = :empId AND u.isActive = :isActive ORDER BY u.createdDate DESC")
	public List<ExitActivity> exitActivityNotificationBell(String empId,String isActive);
}