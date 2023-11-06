package com.ksv.ktrccrm.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ksv.ktrccrm.dao.LeaveTrackerDao;
import com.ksv.ktrccrm.db1.entities.EmpPersonalDetails;
import com.ksv.ktrccrm.db1.entities.LeaveTracker;
import com.ksv.ktrccrm.service.LeaveTrackerService;

@Service
public class LeaveTrackerServiceImpl implements LeaveTrackerService {
	private static final Logger LOGGER = LogManager.getLogger(LeaveTrackerServiceImpl.class);

	@Autowired
	private LeaveTrackerDao leaveTrackerDao;

	@Override
	public List<LeaveTracker> getEmpDetails(String empId) throws Exception {
		List<LeaveTracker> leaveTracker = new ArrayList<>();
		try {
			leaveTracker = leaveTrackerDao.getEmpDetails(empId);
		} catch (Exception e) {
			LOGGER.error(" Error occur while find emp details ");
		}
		return leaveTracker;
	}

	/* Matenity Leave for Married Employee Only */
	@Override
	public EmpPersonalDetails getMarriedOnly(String empId) throws Exception {
		EmpPersonalDetails empPersonalDetails = null;
		try {
			empPersonalDetails = leaveTrackerDao.getMarriedOnly(empId);
		} catch (Exception e) {
			LOGGER.error(" Error occur while find married employee ");
		}
		return empPersonalDetails;
	}

	/* Update Employee Leave Apply Time */
	@Override
	public LeaveTracker updateLeave(LeaveTracker leaveTracker) throws Exception {
		try {
			if (Objects.nonNull(leaveTracker)) {
				return leaveTrackerDao.updateLeave(leaveTracker);
			}
		} catch (Exception e) {
			LOGGER.error(" Error occur while update leave ");
		}
		return leaveTracker;
	}

	/* Find Employee leave details by employee id */
	@Override
	public Optional<LeaveTracker> findByEmpId(String empId) throws Exception {
		Optional<LeaveTracker> optional = leaveTrackerDao.findByEmpId(empId);
		try {
			if (optional.isPresent()) {
				return optional;
			}
		} catch (Exception e) {
			LOGGER.error(" Error occur while find leave details by employee id ");
		}
		return optional;
	}

	@Override
	public List<LeaveTracker> getRecordList(String isActive, String tenantId) throws Exception {
		List<LeaveTracker> leaveTracker = leaveTrackerDao.getRecordList(isActive, tenantId);
		try {
			if (!leaveTracker.isEmpty()) {
				return leaveTracker;
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while display active list" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}
	@Override
	public LeaveTracker cancelAddLeave(LeaveTracker leaveTracker) throws Exception {
		try {
			leaveTrackerDao.cancelAddLeave(leaveTracker);
		} catch (Exception e) {
			LOGGER.error("Error occur while cancel add leave" + ExceptionUtils.getStackTrace(e));
		}
		return leaveTracker;
	}

	@Override
	public LeaveTracker save(LeaveTracker leaveTracker) throws Exception {
		try {
			if (Objects.nonNull(leaveTracker)) {
				return leaveTrackerDao.save(leaveTracker);
			}
		} catch (Exception e) {
			LOGGER.error(" Error occur while save & update leave ");
		}
		return leaveTracker;
	}

	@Override
	public Optional<LeaveTracker> getEmpDetailsById(Long id) throws Exception {
		Optional<LeaveTracker> optional = leaveTrackerDao.getEmpDetailsById(id);
		try {
			if (optional.isPresent()) {
				return optional;
			}
		} catch (Exception e) {
			LOGGER.error(" Error occur while find all employee details by id ");
		}
		return optional;
	}

	@Override
	public List<String> getAllEmp() throws Exception {
		List<String> getAllEmp = leaveTrackerDao.getAllEmp();
		try {
			if (Objects.nonNull(getAllEmp)) {
				return getAllEmp;
			}
		} catch (Exception e) {
			LOGGER.error(" Error occur while getAllEmp ");
		}
		return new ArrayList<>();

	}
	
	@Override
	public List<Float> getAllEmpTotalLeave() throws Exception {
		List<Float> getAllEmpTotalLeave = leaveTrackerDao.getAllEmpTotalLeave();
		try {
			if (Objects.nonNull(getAllEmpTotalLeave)) {
				return getAllEmpTotalLeave;
			}
		} catch (Exception e) {
			LOGGER.error(" Error occur while getAllEmpTotalLeave  ");
		}
		return new ArrayList<>();
	}

	@Override
	public void updateTotalLeave(float totalleave, String empId) throws Exception {
		try {
			leaveTrackerDao.updateTotalLeave(totalleave, empId);
		} catch (Exception e) {
			LOGGER.error(" Error occur while updateTotalLeave ");
		}
	}

	@Override
	public List<Float> getAllEmpPaidLeave() throws Exception {
		List<Float> getAllEmpPaidLeave = leaveTrackerDao.getAllEmpPaidLeave();
		try {
			if (Objects.nonNull(getAllEmpPaidLeave)) {
				return getAllEmpPaidLeave;
			}
		} catch (Exception e) {
			LOGGER.error(" Error occur while getAllEmpPaidLeave ");
		}
		return new ArrayList<>();
	}

	@Override
	public void updatePaidLeave(float paidleave, String empId) throws Exception {
		try {
			leaveTrackerDao.updatePaidLeave(paidleave, empId);
		} catch (Exception e) {
			LOGGER.error(" Error occur while updatePaidLeave ");
		}
	}

	@Override
	public List<Float> getAllEmpMaternityLeave() throws Exception {
		List<Float> getAllEmpMaternityLeave = leaveTrackerDao.getAllEmpMaternityLeave();
		try {
			if (Objects.nonNull(getAllEmpMaternityLeave)) {
				return getAllEmpMaternityLeave;
			}
		} catch (Exception e) {
			LOGGER.error(" Error occur while getAllEmpMaternityLeave ");
		}
		return new ArrayList<>();
	}

	@Override
	public void updateMaternityLeave(float maternityLeave, String empId) throws Exception {
		try {
			leaveTrackerDao.updateMaternityLeave(maternityLeave, empId);
		} catch (Exception e) {
			LOGGER.error(" Error occur while updateMaternityLeave ");
		}
	}

	@Override
	public List<String> getMarriedEmployee() throws Exception {
		List<String> getMarriedEmployee = leaveTrackerDao.getMarriedEmployee();
		try {
			if (Objects.nonNull(getMarriedEmployee)) {
				return getMarriedEmployee;
			}
		} catch (Exception e) {
			LOGGER.error(" Error occur while getMarriedEmployee ");
		}
		return new ArrayList<>();
	}

	@Override
	public LeaveTracker getCurrentUser(String empId) throws Exception {
		LeaveTracker leaveTracker = null;
		try {
			leaveTracker = leaveTrackerDao.getCurrentUser(empId);
		} catch (Exception e) {
			LOGGER.error("Error occur while get current employee data " + ExceptionUtils.getStackTrace(e));
		}
		return leaveTracker;
	}
}