package com.ksv.ktrccrm.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ksv.ktrccrm.dao.DailyWorkReportDao;
import com.ksv.ktrccrm.db1.entities.DailyWorkReport;
import com.ksv.ktrccrm.service.DailyWorkReportService;

@Service
public class DailyWorkReportServiceImpl implements DailyWorkReportService{
	private static final Logger LOGGER = LogManager.getLogger(DailyWorkReportServiceImpl.class);

	@Autowired
	private DailyWorkReportDao dailyWorkReportDao;
	
	@Override
	public List<DailyWorkReport> getRecordList(String empId,String isActive) throws Exception {
		List<DailyWorkReport> dailyWorkreport = dailyWorkReportDao.getRecordList(empId,isActive);
		try {
			if(!dailyWorkreport.isEmpty()) {
				return dailyWorkreport;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display daily work report list  " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public DailyWorkReport findByFullName(String fullName) throws Exception {
		DailyWorkReport dailyWorkReport = new DailyWorkReport();
		try {
			dailyWorkReport = dailyWorkReportDao.findByFullName(fullName);
		} catch (Exception e) {
			LOGGER.error("Error while found the name  " + ExceptionUtils.getStackTrace(e));
		}
		return dailyWorkReport;
	}
	
	@Override
	public DailyWorkReport saveWorkReport(DailyWorkReport dailyWorkReport) throws Exception {
		try {
			this.dailyWorkReportDao.saveWorkReport(dailyWorkReport);
		} catch (Exception e) {
			LOGGER.error("Error occur while save daily work report " + ExceptionUtils.getStackTrace(e));
		}
		return dailyWorkReport;
	}

	@Override
	public DailyWorkReport cancelWorkReport(DailyWorkReport dailyWorkReport) throws Exception {
		try {
			dailyWorkReportDao.cancelWorkReport(dailyWorkReport);
		} catch (Exception e) {
			LOGGER.error("Error occur while cancel work report " + ExceptionUtils.getStackTrace(e));
		}
		return dailyWorkReport;
	}

	@Override
	public DailyWorkReport getWorkReportById(Long id) throws Exception {
		DailyWorkReport dailyWorkReport = new DailyWorkReport();
		try {
			dailyWorkReport = dailyWorkReportDao.findByWorkReportId(id);
		} catch (Exception e) {
			LOGGER.error("Error while daily work report not found for id " + ExceptionUtils.getStackTrace(e));
		}
		return dailyWorkReport;
	}

	@Override
	public List<DailyWorkReport> getTotalWorkReport() throws Exception {
		List<DailyWorkReport> dailyWorkReports = dailyWorkReportDao.getTotalWorkReport();
		try {
			if (Objects.nonNull(dailyWorkReports)) {
				return dailyWorkReports;
			} else {
				System.out.println("Null Data Get :::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get list of daily work report " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public Long getAllWorkReport(String tenantId) throws Exception {
		try {
			return dailyWorkReportDao.getAllWorkReport(tenantId);
		} catch (Exception e) {
			LOGGER.error("Error occuring while get all daily work report of employees in current month " + ExceptionUtils.getStackTrace(e));
		}
		return (long) 0;
	}
}