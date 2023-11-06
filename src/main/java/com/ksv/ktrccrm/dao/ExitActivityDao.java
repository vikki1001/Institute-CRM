package com.ksv.ktrccrm.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.db1.entities.ExitActivity;

@Repository
public interface ExitActivityDao {

	public List<ExitActivity> getAllEmp() throws Exception;

	public ExitActivity saveExitActivity(ExitActivity exitActivity) throws Exception;

	public ExitActivity cancelById(ExitActivity exitActivity) throws Exception;

	public List<ExitActivity> getRecordList(String empId,String isActive, String tenantId) throws Exception;

	public List<ExitActivity> getExitActivityById(String empId) throws Exception;

	public List<ExitActivity> getEmpWithManger(String empId, String tenantId) throws Exception;

	public void acceptStatus(String status, String flag, Long id) throws Exception;

	public List<ExitActivity> acceptExitActivityById(Long id) throws Exception;
	
	public List<ExitActivity> getEmpWithMangerWithPending(String userId, String tenantId) throws Exception;

	public List<ExitActivity> getTotalExitActivity() throws Exception;
	
	public Long getAllExitActivity(String tenantId) throws Exception;

	public ExitActivity notificationRead(Long id) throws Exception;

	public List<ExitActivity> exitActivityNotificationBell(String userId) throws Exception;

	public List<ExitActivity> unreadNotification(String userId) throws Exception;
}
