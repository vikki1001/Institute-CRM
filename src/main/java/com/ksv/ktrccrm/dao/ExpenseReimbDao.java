package com.ksv.ktrccrm.dao;

import java.util.List;

import com.ksv.ktrccrm.db1.entities.ExpenseReimb;


public interface ExpenseReimbDao {
	
	public ExpenseReimb getCancelById(ExpenseReimb expenseReimb) throws Exception;

	public ExpenseReimb saveExpenseReimb(ExpenseReimb expenseReimb) throws Exception;

	public List<ExpenseReimb> getRecordList(String empId, String isActive, String tenantId) throws Exception;
	
	public List<ExpenseReimb> getExpenseReimbByEmpId(String empId) throws Exception;
	
	public List<ExpenseReimb> acceptExpenseReimbById(Long id) throws Exception;
	
	public void acceptStatus(String status, String flag, Long id) throws Exception;
	
	public ExpenseReimb getAttachment(Long id) throws Exception;
	
	public List<ExpenseReimb> getEmpWithMangerWithPending(String tenantId) throws Exception;
	
	public ExpenseReimb notificationRead(Long id) throws Exception;

	public List<ExpenseReimb> getExpenseReimbByEmpId() throws Exception;

	public List<ExpenseReimb> unreadNotification() throws Exception;

	public List<ExpenseReimb> getEmpDetails(String empId)throws Exception;

	public ExpenseReimb saveDraftExpenseReimb(ExpenseReimb expenseReimb)throws Exception;

	public ExpenseReimb getExpenseById(Long id)throws Exception;
}
