package com.ksv.ktrccrm.service;

import java.util.List;

import com.ksv.ktrccrm.db1.entities.ExpenseReimb;

public interface ExpenseReimbService {

	public ExpenseReimb getCancelById(ExpenseReimb expenseReimb) throws Exception;

	public ExpenseReimb saveExpenseReimb(ExpenseReimb expenseReimb) throws Exception;

	public List<ExpenseReimb> getRecordList(String empId, String isActive, String tenantId) throws Exception;

	public List<ExpenseReimb> getExpenseReimbByEmpId(String empId) throws Exception;

	public List<ExpenseReimb> acceptExpenseReimbById(Long id) throws Exception;

	public void acceptStatus(String status, String flag, Long id) throws Exception;

	public ExpenseReimb getAttachment(Long id) throws Exception;

	public List<ExpenseReimb> getEmpWithMangerWithPending(String tenantId) throws Exception;

	public ExpenseReimb notificationRead(Long id) throws Exception;

	public List<ExpenseReimb> expenseReimbNotificationBell() throws Exception;

	public List<ExpenseReimb> unreadNotification() throws Exception;

	public List<ExpenseReimb> getEmpDetails(String empId)throws Exception;

	public void saveDraftExpenseReimb(ExpenseReimb expenseReimb)throws Exception;

	public ExpenseReimb getExpenseById(Long id);

	
}