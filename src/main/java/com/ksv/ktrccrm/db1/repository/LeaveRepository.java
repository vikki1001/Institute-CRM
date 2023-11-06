package com.ksv.ktrccrm.db1.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ksv.ktrccrm.db1.entities.LeaveMst;

public interface LeaveRepository extends JpaRepository<LeaveMst, Long> {

	@Query("SELECT u FROM LeaveMst u WHERE u.managerId = :empId AND u.isActive = :isActive ORDER BY date DESC")
	public List<LeaveMst> getEmpWithManger(String empId, String isActive);
	
	@Query("SELECT u FROM LeaveMst u WHERE u.managerId = :empId AND u.isActive = :isActive AND status = :status AND u.tenantId=:tenantId")
	public List<LeaveMst> getLeavePending(String empId, String isActive,  String status, String tenantId);
	
	@Query("SELECT u FROM LeaveMst u WHERE u.empId.empId = :empId AND u.isActive = :isActive AND u.tenantId =:tenantId ORDER BY date DESC")
	public List<LeaveMst> getRecordList(String empId, String isActive, String tenantId);

	@Query("SELECT u FROM LeaveMst u WHERE u.empId.empId = :empId AND u.isActive = :isActive")
	public List<LeaveMst> getLeaveApply(String empId, String isActive);

	@Query("SELECT u FROM LeaveMst u WHERE u.date = CURDATE() AND u.id = :id AND u.isActive = :isActive")
	public List<LeaveMst> getAcceptLeaveById(Long id, String isActive);

	@Transactional
	@Modifying
	@Query("update LeaveMst set status = :status, flag = :flag where id = :id")
	void acceptStatus(String status, String flag, Long id);

	@Query("SELECT u FROM LeaveMst u WHERE u.leaveType = :leaveType AND u.isActive = :isActive")
	public List<LeaveMst> getLeaveByLeaveType(@Param("leaveType") String leaveType, String isActive);

	@Query("SELECT u FROM LeaveMst u WHERE u.status = :status AND u.isActive = :isActive ORDER BY date DESC")
	public List<LeaveMst> getApprovedLeave(String status, String isActive);
	
	@Query("SELECT u FROM LeaveMst u WHERE u.status = :status AND u.leaveType = :leaveType AND u.isActive = :isActive")
	public List<LeaveMst> getApprovedAndLeaveType(String status, @Param("leaveType") String leaveType, String isActive);
	
	@Query("SELECT u FROM LeaveMst u WHERE u.isActive = :isActive ORDER BY date DESC")
	public List<LeaveMst> activeLeave(String isActive);
	
	@Query("SELECT u FROM LeaveMst u WHERE u.isActive = :isActive AND u.status = :status AND DATE(u.fromDate) <= CURRENT_DATE() AND DATE(u.toDate) >= CURRENT_DATE() AND u.tenantId=:tenantId ORDER BY date DESC")
	public List<LeaveMst> onLeaveToday(String isActive, String status, String tenantId);
	
	/* DAYS OVERVIEW THIS MONTH */
	@Query("SELECT count(*) FROM LeaveMst WHERE MONTH(date) = MONTH(CURRENT_DATE()) AND empId.empId = :userId AND isActive = :isActive")
	public Long getLeaveDays(String userId, String isActive);
	
	/* DashBoard Count for HR */
	@Query("SELECT count(u) FROM LeaveMst u WHERE MONTH(u.date) = MONTH(CURRENT_DATE()) AND u.status = :status AND u.isActive = :isActive AND u.tenantId=:tenantId")
	public Long getAllLeaves(String status, String isActive, String tenantId);
	
	@Query("SELECT count(u) FROM LeaveMst u WHERE MONTH(u.date) = MONTH(CURRENT_DATE()) AND u.status = :status AND u.isActive = :isActive AND u.tenantId=:tenantId")
	public Long getAllPendingLeaves(String status, String isActive, String tenantId);
	
	/* Link */
	@Query("SELECT u FROM LeaveMst u WHERE MONTH(u.date) = MONTH(CURRENT_DATE()) AND u.status = :status AND u.isActive = :isActive ORDER BY date DESC")
	public List<LeaveMst> getTotalLeave(String status, String isActive);
	
	@Query("SELECT u FROM LeaveMst u WHERE MONTH(u.date) = MONTH(CURRENT_DATE()) AND u.status = :status AND u.isActive = :isActive ORDER BY date DESC")
	public List<LeaveMst> getTotalPendingLeave( String status, String isActive);

	@Query("SELECT u FROM LeaveMst u WHERE u.notification = :notification AND u.isActive = :isActive AND u.managerId = :userId")
	public List<LeaveMst> unreadNotification(String userId,String notification,String isActive);
	
	@Query("SELECT u FROM LeaveMst u WHERE u.managerId = :userId AND u.isActive = :isActive ORDER BY createdDate DESC")
	public List<LeaveMst> leaveMstsNotificationBell(String userId, String isActive);

	@Query("SELECT u FROM LeaveMst u WHERE u.empId.empId = :empId AND u.status=:status AND MONTH(u.date) = Month(current_date()) AND u.isActive = :isActive ORDER BY createdDate DESC")
	public List<LeaveMst> getEmpMonthLeavesList(String empId, String status, String isActive);

}