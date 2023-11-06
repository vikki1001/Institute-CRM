package com.ksv.ktrccrm.dao;

import java.util.List;

import com.ksv.ktrccrm.db1.entities.PayrollMst;

public interface PayrollDao {

	public void saveAll(List<PayrollMst> payrollMst) throws Exception;
	
	public List<PayrollMst> getPayrollSlipByMonthAndYear(Integer month, Integer year, String empId, String tenantId, String isActive) throws Exception;
	
	public List<PayrollMst> findAll() throws Exception;
	
	public List<PayrollMst> getRecordList(String isActive, String tenantId) throws Exception;
}
