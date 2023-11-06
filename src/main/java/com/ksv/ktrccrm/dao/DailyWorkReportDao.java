package com.ksv.ktrccrm.dao;

import java.util.List;

import com.ksv.ktrccrm.db1.entities.DailyWorkReport;

public interface DailyWorkReportDao {
	
	public List<DailyWorkReport> getRecordList(String empId,String isActive) throws Exception;
	
	public DailyWorkReport findByFullName(String fullName) throws Exception;

	public DailyWorkReport saveWorkReport(DailyWorkReport dailyWorkReport) throws Exception;

	public DailyWorkReport cancelWorkReport(DailyWorkReport dailyWorkReport) throws Exception;

	public DailyWorkReport findByWorkReportId(Long id) throws Exception;
	
	public List<DailyWorkReport> getTotalWorkReport() throws Exception;
	
	public Long getAllWorkReport(String tenantId) throws Exception;
	
}
