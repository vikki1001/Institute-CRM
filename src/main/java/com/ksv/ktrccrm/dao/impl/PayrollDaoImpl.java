package com.ksv.ktrccrm.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.dao.PayrollDao;
import com.ksv.ktrccrm.db1.entities.PayrollMst;
import com.ksv.ktrccrm.db1.repository.PayrollRepository;

@Repository
@Transactional
public class PayrollDaoImpl implements PayrollDao {
	private static final Logger LOGGER = LogManager.getLogger(PayrollDaoImpl.class);

	@Autowired
	private PayrollRepository payrollRepository;

	/* For Save/Upload Payroll Excel File */
	@Override
	public void saveAll(List<PayrollMst> payrollMst) throws Exception {
		try {
			if (Objects.nonNull(payrollMst)) {
				this.payrollRepository.saveAll(payrollMst);
			}
		} catch (Exception e) {
			LOGGER.error("Error Occuring While Save payroll Data " + ExceptionUtils.getStackTrace(e));
		}
	}

	/* Current Month Payroll Display for Employee */
	@Override
	public List<PayrollMst> getPayrollSlipByMonthAndYear(Integer month, Integer year, String empId,String tenantId, String isActive) throws Exception {
		List<PayrollMst> payrollMst = payrollRepository.getPayrollSlipByMonthAndYear(month, year, empId, tenantId, isActive);
		try {
			if (Objects.nonNull(payrollMst)) {
				System.out.println("Inside daoImpl......");
				return payrollMst;
			} else {
				System.out.println("List is empty");
			}
		} catch (Exception e) {
			LOGGER.error("Error Occuring While Display Payroll List by month and year" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList();
	}

	/* Get all Records into DB */
	@Override
	public List<PayrollMst> findAll() throws Exception {
		List<PayrollMst> payrollMsts = new ArrayList<>();
		try {
			payrollMsts = payrollRepository.findAll();
		} catch (Exception e) {
			LOGGER.error("Error occur while display payroll list " + ExceptionUtils.getStackTrace(e));
		}
		return payrollMsts;
	}
	
	@Override
	public List<PayrollMst> getRecordList(String isActive, String tenantId) throws Exception {
		List<PayrollMst> payrollMsts = payrollRepository.getRecordList(isActive , tenantId);
		try {
			if (Objects.nonNull(payrollMsts)) {
				return payrollMsts;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display active emp payroll" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}
}