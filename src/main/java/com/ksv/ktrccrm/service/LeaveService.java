package com.ksv.ktrccrm.service;

import java.util.List;
import java.util.Optional;

import com.ksv.ktrccrm.db1.entities.LeaveMst;

public interface LeaveService {

	public LeaveMst saveOrUpdateLeave(LeaveMst leaveMst) throws Exception;

	public List<LeaveMst> getLeaveList() throws Exception;

	public List<LeaveMst> getRecordList(String empId, String isActive, String tenantId) throws Exception;

	public LeaveMst cancelLeave(LeaveMst leaveMst) throws Exception;

	public List<LeaveMst> getLeaveApply(String empId)  throws Exception;

	public List<LeaveMst> getRejectLeave(String empId) throws Exception;

	public void acceptStatus(String status, String flag, Long id) throws Exception;

	public List<LeaveMst> getAcceptLeaveById(Long id) throws Exception;

	public List<LeaveMst> getLeaveByLeaveType(String leaveType) throws Exception;

	public List<LeaveMst> getEmpWithManger(String empId) throws Exception;

	public List<LeaveMst> getLeavePending(String userId, String tenantId) throws Exception;

	public List<LeaveMst> onLeaveToday(String tenantId) throws Exception;

	public Optional<LeaveMst> findLeaveById(Long id) throws Exception;
	
	public List<LeaveMst> getTotalLeave() throws Exception;
	
	public List<LeaveMst> getTotalPendingLeave() throws Exception;

	public Long getLeaveDays(String userId) throws Exception;

	public Long getAllLeaves(String tenantId) throws Exception;

	public Long getAllPendingLeaves(String tenantId) throws Exception;

	public LeaveMst notificationRead(Long id) throws Exception;

	public List<LeaveMst> leaveMstsNotificationBell(String userId) throws Exception;

	public List<LeaveMst> unreadNotification(String userId) throws Exception;

	public List<LeaveMst> getEmpMonthLeavesList(String empId, String approved, String true1) throws Exception;
}
