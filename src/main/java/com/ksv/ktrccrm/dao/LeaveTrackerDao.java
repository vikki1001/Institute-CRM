package com.ksv.ktrccrm.dao;

import java.util.List;
import java.util.Optional;

import com.ksv.ktrccrm.db1.entities.EmpPersonalDetails;
import com.ksv.ktrccrm.db1.entities.LeaveTracker;

public interface LeaveTrackerDao {

	public List<LeaveTracker> getEmpDetails(String empId) throws Exception;

	public EmpPersonalDetails getMarriedOnly(String empId) throws Exception;

	public LeaveTracker updateLeave(LeaveTracker leaveTracker) throws Exception;

	public Optional<LeaveTracker> findByEmpId(String empId) throws Exception;

	public LeaveTracker save(LeaveTracker leaveTracker) throws Exception;

	public List<LeaveTracker> getRecordList(String isActive, String tenantId) throws Exception;

	public LeaveTracker cancelAddLeave(LeaveTracker leaveTracker) throws Exception;

	public Optional<LeaveTracker> getEmpDetailsById(Long id) throws Exception;

	public List<String> getAllEmp() throws Exception;

	public List<Float> getAllEmpTotalLeave() throws Exception;

	public void updateTotalLeave(float totalleave, String empId) throws Exception;

	public List<Float> getAllEmpPaidLeave() throws Exception;

	public void updatePaidLeave(float paidleave, String empId) throws Exception;

	public List<Float> getAllEmpMaternityLeave() throws Exception;

	public void updateMaternityLeave(float maternityLeave, String empId) throws Exception;

	public List<String> getMarriedEmployee() throws Exception;

	public LeaveTracker getCurrentUser(String empId) throws Exception;
}
