package com.ksv.ktrccrm.db1.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ksv.ktrccrm.db1.entities.CheckInCheckOut;

public interface CheckInCheckOutRepository extends JpaRepository<CheckInCheckOut, Long> {

	@Query("SELECT u FROM CheckInCheckOut u WHERE u.checkinDateTime between :from AND :to OR u.checkoutDateTime between :from AND :to ORDER BY u.date DESC")
	public List<CheckInCheckOut> findAllEmp(@Param("from") String from, @Param("to") String to);

	@Query("SELECT u FROM CheckInCheckOut u WHERE u.checkinDateTime between :from AND :to OR u.checkoutDateTime between :from AND :to AND u.userId = :userId AND u.tenantId = :tenantId ORDER BY u.date DESC")
	public List<CheckInCheckOut> findByDateorUserIdorTenantId(@Param("from") String from, @Param("to") String to, @Param("userId") String userId, @Param("tenantId") String tenantId);

	@Query("SELECT u FROM CheckInCheckOut u WHERE u.checkinDateTime between :from AND :to OR u.checkoutDateTime between :from AND :to AND u.userId = :userId ORDER BY u.date DESC")
	public List<CheckInCheckOut> findByDateorUserId(@Param("from") String from, @Param("to") String to, @Param("userId") String userId);

	@Query("SELECT u FROM CheckInCheckOut u WHERE u.checkinDateTime between :from AND :to OR u.checkoutDateTime between :from AND :to AND u.tenantId = :tenantId ORDER BY u.date DESC")
	public List<CheckInCheckOut> findByDateorTenantId(@Param("from") String from, @Param("to") String to, @Param("tenantId") String tenantId);

	@Query("SELECT e FROM CheckInCheckOut e WHERE e.userId = :userId AND e.isActive = :isActive ORDER BY e.date DESC")
	public List<CheckInCheckOut> getCurrentUser(String userId, String isActive);

	@Query("SELECT u FROM CheckInCheckOut u WHERE u.date between :from AND :to AND u.userId = :userId ORDER BY u.date")
	public List<CheckInCheckOut> findByDayOfTheWeek(@Param("from") String from, @Param("to") String to, String userId);

	@Query("SELECT u FROM CheckInCheckOut u WHERE u.date = CURDATE() AND u.userId = :userId")
	public Optional<CheckInCheckOut> findByDateAndCurrentUser(@Param("userId") String userId);

	@Query("SELECT u FROM CheckInCheckOut u WHERE u.date = CURDATE() AND u.userId = :userId")
	public CheckInCheckOut findByDateAndCurrentUser2(@Param("userId") String userId);

	@Query("SELECT u FROM CheckInCheckOut u WHERE u.YEARWEEK(date) = YEARWEEK(NOW()) AND u.userId = :userId")
	public List<CheckInCheckOut> findByCurrentDay(@Param("userId") String userId);

	@Query("SELECT u FROM CheckInCheckOut u WHERE u.date = CURDATE() AND u.userId = :userId AND u.isActive=:isActive")
	public CheckInCheckOut findByEmpId(@Param("userId") String userId, String isActive);

	@Query("SELECT e FROM CheckInCheckOut e WHERE e.userId = :userId AND e.date = :date")
	public Optional<CheckInCheckOut> getByUserId(String userId, String date);

	@Query("SELECT u FROM CheckInCheckOut u WHERE u.managerId = :userId AND u.addNewReq = :addNewReq AND u.tenantId=:tenantId ORDER BY u.date DESC")
	public List<CheckInCheckOut> getEmpWithManger(String userId, String addNewReq, String tenantId);

	@Query("SELECT u FROM CheckInCheckOut u WHERE u.managerId = :userId AND u.addNewReq = :addNewReq AND u.isActive = :isActive AND u.approvalReq = :approvalReq AND u.tenantId=:tenantId")
	public List<CheckInCheckOut> getAttendancePending(String userId, String addNewReq, String isActive, String approvalReq, String tenantId);

	/* For Approved/Reject Request */
	@Transactional
	@Modifying
	@Query("update CheckInCheckOut set approvalReq = :approvalReq, status = :status, isActive = :isActive, flag = :flag where id = :id")
	void acceptStatus(String approvalReq, String status, String isActive, String flag, Long id);

	@Query("SELECT u FROM CheckInCheckOut u WHERE u.id = :id")
	public List<CheckInCheckOut> getAcceptLeaveById(Long id);

	@Query("SELECT u FROM CheckInCheckOut u WHERE u.date = CURDATE() AND u.userId = :userId")
	public CheckInCheckOut sendMail(String userId);

	@Query("SELECT e FROM CheckInCheckOut e WHERE e.userId = :userId AND e.addNewReq = :addNewReq AND e.isActive = :isActive ORDER BY e.date DESC")
	public List<CheckInCheckOut> getNewReqList(String userId, String addNewReq, String isActive);

	@Query("SELECT e FROM CheckInCheckOut e WHERE e.userId = :userId AND e.addNewReq = :addNewReq AND e.isActive=:isActive ORDER BY e.date DESC")
	public List<CheckInCheckOut> cancelReqList(String userId, String addNewReq, String isActive);

	@Transactional                           
	@Modifying
	@Query(value = "INSERT INTO CheckInCheckOut (userId, createdDate, date, dayOfTheWeek, tenantId, isActive, checkindttm, checkoutdttm, timeDuration, status) VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9,?10)", nativeQuery = true)
	void insertEmployee(String userId, Date createdDate, String date, String dayOfTheWeek, String tenantId, String isActive, String checkinDateTime, String checkoutDateTime,String timeDuration, String status);
	
	@Query("SELECT u.userId FROM CheckInCheckOut u WHERE u.date = CURDATE() AND u.isActive = :isActive")
	public List<String> getDateAndUserId(String isActive);

	@Query("SELECT u FROM CheckInCheckOut u WHERE u.date = :date AND u.userId = :userId")
	public Optional<CheckInCheckOut> getByUserIdAndDate(String date, String userId);
	
	@Query("SELECT u.userId FROM CheckInCheckOut u WHERE u.date = CURDATE() AND u.timeDuration <= :timeDuration OR u.date = CURDATE() AND u.timeDuration is NULL")
	public List<String> getNineHourNotComplete(String timeDuration);
	
	@Query("SELECT u FROM CheckInCheckOut u WHERE u.date = CURDATE() AND u.userId IN :userId")
	public List<CheckInCheckOut> getByUserIdAndCurentDate(List<String> userId);
	
	/* DAYS OVERVIEW THIS MONTH */
	@Query("SELECT count(u) FROM CheckInCheckOut u WHERE MONTH(u.date) = MONTH(CURRENT_DATE()) AND u.userId = :userId AND u.isActive = :isActive")
	public Long getPresentDays(String userId, String isActive);

	@Query("SELECT count(u) FROM CheckInCheckOut u WHERE MONTH(u.date) = MONTH(CURRENT_DATE()) AND DATE_FORMAT(u.checkinDateTime, '%H:%i:%s') > :checkindttm AND u.userId = :userId")
	public Long getLateDays(String userId, String checkindttm );

	@Query("SELECT count(u) FROM CheckInCheckOut u WHERE MONTH(u.date) = MONTH(CURRENT_DATE()) AND u.timeDuration < :timeDuration AND u.userId = :userId")
	public Long getHalfDays(String userId, String timeDuration);

	@Query("SELECT count(u) FROM CheckInCheckOut u WHERE MONTH(u.date) = MONTH(CURRENT_DATE()) AND u.userId = :userId AND u.isActive = :isActive")
	public Long getAbsentDays(String userId, String isActive);
	
	/* DashBoard Count for HR */
	@Query("SELECT count(u) FROM CheckInCheckOut u WHERE MONTH(u.date) = MONTH(CURRENT_DATE()) AND u.status = :status AND u.isActive = :isActive AND u.tenantId=:tenantId")
	public Long getAllAttendance(String status, String isActive, String tenantId);
	
	@Query("SELECT count(u) FROM CheckInCheckOut u WHERE MONTH(u.date) = MONTH(CURRENT_DATE()) AND u.isActive = :isActive AND u.tenantId=:tenantId")
	public Long getAllEmpAbsent(String isActive, String tenantId);
	
	@Query("SELECT count(u) FROM CheckInCheckOut u WHERE MONTH(u.date) = MONTH(CURRENT_DATE()) AND u.approvalReq = :approvalReq AND u.addNewReq = :addNewReq AND u.isActive = :isActive AND u.tenantId=:tenantId")
	public Long getAllNewReq(String approvalReq, String addNewReq, String isActive, String tenantId);
	
	/* Link */
	@Query("SELECT u FROM CheckInCheckOut u WHERE MONTH(u.date) = MONTH(CURRENT_DATE()) AND u.status = :status AND u.isActive = :isActive AND u.tenantId=:tenantId ORDER BY u.date DESC")
	public List<CheckInCheckOut> getTotalAbsentEmp(String status, String isActive, String tenantId);
	
	@Query("SELECT u FROM CheckInCheckOut u WHERE MONTH(u.date) = MONTH(CURRENT_DATE()) AND u.status = :status AND u.isActive = :isActive AND u.tenantId=:tenantId ORDER BY u.date DESC")
	public List<CheckInCheckOut> getTotalAttendance(String status, String isActive, String tenantId);
	
	@Query("SELECT u FROM CheckInCheckOut u WHERE MONTH(u.date) = MONTH(CURRENT_DATE()) AND u.approvalReq = :approvalReq AND u.addNewReq = :addNewReq AND u.isActive = :isActive ORDER BY u.date DESC")
	public List<CheckInCheckOut> getTotalNewReq(String approvalReq, String addNewReq, String isActive);
	
	@Query("SELECT u FROM CheckInCheckOut u WHERE u.date = curdate() AND u.userId = :userId AND u.isActive = :isActive")
	public CheckInCheckOut getCheckInDateTime(String userId, String isActive);

	@Query("SELECT u FROM CheckInCheckOut u WHERE u.notification = :notification AND u.isActive = :isActive AND u.managerId = :userId")
	public List<CheckInCheckOut> unreadNotification(String userId, String notification, String isActive);
		
	@Query("SELECT u FROM CheckInCheckOut u WHERE u.managerId = :userId AND u.isActive = :isActive AND u.addNewReq=:addNewReq ORDER BY u.date DESC")
	public List<CheckInCheckOut> addNewReqNotificationBell(String userId, String isActive, String addNewReq);

	@Query("SELECT e FROM CheckInCheckOut e WHERE e.userId = :userId AND e.id = :id AND e.isActive = :isActive")
	public CheckInCheckOut getCurrentUserWithId(String userId, Long id, String isActive);

	/* Weekly Data Display Previous or Next Button */
	
	@Query("SELECT u FROM CheckInCheckOut u WHERE u.date between :from AND :to AND u.userId = :userId AND u.isActive = :isActive")
	public List<CheckInCheckOut> currentWeekData(@Param("from") String from, @Param("to") String to, String userId,String isActive);
}