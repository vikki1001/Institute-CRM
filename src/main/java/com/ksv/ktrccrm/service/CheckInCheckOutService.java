package com.ksv.ktrccrm.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mobile.device.Device;

import com.ksv.ktrccrm.db1.entities.CheckInCheckOut;
import com.ksv.ktrccrm.db1.entities.EmpPersonalDetails;

public interface CheckInCheckOutService {

	public List<CheckInCheckOut> getcheckInOutList() throws Exception;

	public List<CheckInCheckOut> findByDateorUserIdorTenantId(String from, String to, String userId, String tenantId)
			throws Exception;

	public CheckInCheckOut saveCheckIn(CheckInCheckOut checkInCheckOut, Device device, HttpServletRequest request) throws Exception;

	public CheckInCheckOut saveCheckOut(CheckInCheckOut checkInCheckOut, Device device, HttpServletRequest request) throws Exception;

	public List<CheckInCheckOut> getTotalTime(String userId) throws Exception;

	public List<CheckInCheckOut> findByDayOfTheWeek(String from, String to, String userId) throws Exception;

	public List<String> getNineHourNotComplete() throws Exception;

	public List<EmpPersonalDetails> getUpcomingEmpBirthday(String tenantId) throws Exception;

	public CheckInCheckOut updateNewRequest(CheckInCheckOut checkInCheckOut, Device device, HttpServletRequest request) throws Exception;

	public CheckInCheckOut cancelAddReq(CheckInCheckOut checkInCheckOut) throws Exception;

	public List<CheckInCheckOut> getAttendancePending(String userId, String tenantId) throws Exception;

	public List<String> getDateAndUserId() throws Exception;

	public void insertEmployee(String userId, Date createdDate, String date, String dayOfTheWeek, String tenantId, String isActive,
			String checkinDateTime, String checkoutDateTime, String timeDuration, String status) throws Exception;

	public CheckInCheckOut findByEmpId(String userId) throws Exception;

	public Long getPresentDays(String userId) throws Exception;

	public Long getLateDays(String userId) throws Exception;

	public Long getHalfDays(String userId) throws Exception;

	public Long getAbsentDays(String userId) throws Exception;

	public List<CheckInCheckOut> getNewReqList(String empId) throws Exception;

	public List<CheckInCheckOut> cancelReqList(String empId) throws Exception;

	public CheckInCheckOut sendMail(String userId) throws Exception;

	public List<CheckInCheckOut> getEmpWithManger(String empId, String tenantId) throws Exception;

	public List<CheckInCheckOut> getAcceptLeaveById(Long id) throws Exception;

	public void acceptStatus(String approvalReq, String status, String isActive, String flag, Long id) throws Exception;

	public List<CheckInCheckOut> getByUserIdAndCurentDate(List<String> userId) throws Exception;

	public List<CheckInCheckOut> getTotalAbsentEmp(String tenantId) throws Exception;
	
	public List<CheckInCheckOut> getTotalAttendance(String tenantId) throws Exception;
	
	public List<CheckInCheckOut> getTotalNewReq() throws Exception;

	public Long getAllAttendance(String tenantId) throws Exception;

	public Long getAllEmpAbsent(String tenantId) throws Exception;

	public Long getAllNewReq(String tenantId) throws Exception;

	public CheckInCheckOut findByDateAndCurrentUser2(String userId) throws Exception;

	public CheckInCheckOut notificationRead(Long id) throws Exception;

	public List<CheckInCheckOut> addNewReqNotificationBell(String userId) throws Exception;

	public List<CheckInCheckOut> unreadNotification(String userId) throws Exception;

	public CheckInCheckOut getCurrentUserWithId(String userId, Long id) throws Exception;

	public List<CheckInCheckOut> currentWeekData(String from, String to, String userId, String isActive) throws Exception;

}