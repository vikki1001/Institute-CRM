package com.ksv.ktrccrm.service;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ksv.ktrccrm.db1.entities.PayrollMst;

public interface PayrollService {

public void saveAll(MultipartFile files) throws Exception;
	
	public List<PayrollMst> getPayrollSlipByMonthAndYear(Integer month, Integer year, String empId,String tenantId, String isActive) throws Exception;

	public List<PayrollMst> findAll() throws Exception;

	public ByteArrayInputStream exportPayroll(List<PayrollMst> payrollMst) throws Exception;

	public List<PayrollMst> getRecordList(String isActive, String tenantId) throws Exception;

}
