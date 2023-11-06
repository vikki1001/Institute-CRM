package com.ksv.ktrccrm.service;

import java.util.List;

import com.ksv.ktrccrm.db1.entities.DailyWorkReport;

public interface DailyWorkReportService {

	public List<DailyWorkReport> getRecordList(String empId,String isActive) throws Exception;
	
	public DailyWorkReport findByFullName(String fullName) throws Exception;
	
	public DailyWorkReport saveWorkReport(DailyWorkReport dailyWorkReport) throws Exception;

	public DailyWorkReport cancelWorkReport(DailyWorkReport dailyWorkReport) throws Exception;

	public DailyWorkReport getWorkReportById(Long id) throws Exception;

	public List<DailyWorkReport> getTotalWorkReport() throws Exception;

	public Long getAllWorkReport(String tenantId) throws Exception;

}
