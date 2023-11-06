package com.ksv.ktrccrm.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.LeaveTrackerDao;
import com.ksv.ktrccrm.db1.entities.EmpPersonalDetails;
import com.ksv.ktrccrm.db1.entities.LeaveTracker;
import com.ksv.ktrccrm.db1.repository.EmpPersonalRepository;
import com.ksv.ktrccrm.db1.repository.LeaveTrackerRepository;

@Repository
public class LeaveTrackerDaoImpl implements LeaveTrackerDao {
	private static final Logger LOGGER = LogManager.getLogger(LeaveTrackerDaoImpl.class);

	@Autowired
	private LeaveTrackerRepository leaveTrackerRepository;
	@Autowired
	private EmpPersonalRepository empPersonalRepository;
	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;

	@Override
	public List<LeaveTracker> getEmpDetails(String empId) throws Exception {
		List<LeaveTracker> leaveTracker = new ArrayList<>();
		try {
			leaveTracker = leaveTrackerRepository.getEmpDetails(empId,ProdConstant.TRUE);
		} catch (Exception e) {
			LOGGER.error("Error occur while find emp details ");
		}
		return leaveTracker;
	}

	/* Matenity Leave for Married Employee Only */
	@Override
	public EmpPersonalDetails getMarriedOnly(String empId) throws Exception {
		EmpPersonalDetails empPersonalDetails = null;
		try {
			empPersonalDetails = empPersonalRepository.getMarriedOnly(ProdConstant.MARRIED ,empId);
		} catch (Exception e) {
			LOGGER.error("Error occur while find married employee ");
		}
		return empPersonalDetails;
	}

	/* Update Employee Leave Apply Time */
	@Override
	public LeaveTracker updateLeave(LeaveTracker leaveTracker) throws Exception {
		try {
			if (Objects.nonNull(leaveTracker)) {
				return leaveTrackerRepository.save(leaveTracker);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while update leave ");
		}
		return leaveTracker;
	}

	/* Find Employee leave details by employee id */
	@Override
	public Optional<LeaveTracker> findByEmpId(String empId) throws Exception {
		Optional<LeaveTracker> optional = leaveTrackerRepository.findByEmpId(empId, ProdConstant.TRUE);
		try {
			if (optional.isPresent()) {
				return optional;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while find employee leave details by emp id ");
		}
		return optional;
	}

	@Override
	public List<LeaveTracker> getRecordList(String isActive, String tenantId) throws Exception {
		List<LeaveTracker> leaveTracker = leaveTrackerRepository.getRecordList(isActive, tenantId);
		try {
			if (!leaveTracker.isEmpty()) {
				return leaveTracker;
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while display list" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}
	@Override
	public LeaveTracker save(LeaveTracker leaveTracker) throws Exception {
		String empId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();
			if (Objects.isNull(leaveTracker.getId())) {
				leaveTracker.setBookedTotalLeave((float) 0.0);
				leaveTracker.setBookedPaidLeave((float) 0.0);
				leaveTracker.setBookedMaternityLeave(((float) 0.0));
				leaveTracker.setIsActive(ProdConstant.TRUE);
				leaveTracker.setTenantId(null);
				leaveTracker.setCreatedDate(checkOutDaoImpl.getDateTime());
				leaveTracker.setCreatedBy(empId);
				leaveTrackerRepository.save(leaveTracker);
			}
//			else {
//				Optional<LeaveTracker> leaveTrackers = leaveTrackerRepository.findById(leaveTracker.getId());
//				if (leaveTrackers.isPresent()) {
//					LeaveTracker tracker = leaveTrackers.get();
//					tracker.setTotalLeave(leaveTracker.getTotalLeave());
//					tracker.setPaidLeave(leaveTracker.getPaidLeave());
//					tracker.setMaternityLeave(leaveTracker.getMaternityLeave());
//					tracker.setAddLeave(leaveTracker.getAddLeave());
//					tracker.setLeaveType(leaveTracker.getLeaveType());
//					tracker.setLastModifiedBy(empId);
//					tracker.setLastModifiedDate(checkOutDaoImpl.getDateTime());
//					leaveTrackerRepository.save(tracker);
//				} else {
//					return leaveTracker;
//				}
//			}
		} catch (Exception e) {
			LOGGER.error("Error occur while save Add Leave " + ExceptionUtils.getStackTrace(e));
		}
		return leaveTracker;
	}

	@Override
	public LeaveTracker cancelAddLeave(LeaveTracker leaveTracker) throws Exception {
		try {
			Optional<LeaveTracker> optional = leaveTrackerRepository.findById(leaveTracker.getId());
			if (optional.isPresent()) {
				LeaveTracker tracker = optional.get();
				tracker.setIsActive(leaveTracker.getIsActive());
				tracker.setIsActive(ProdConstant.FALSE);

				leaveTrackerRepository.save(leaveTracker);
			} else {
				return leaveTracker;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while in cancel work report" + ExceptionUtils.getStackTrace(e));
		}
		return leaveTracker;
	}

	@Override
	public Optional<LeaveTracker> getEmpDetailsById(Long id) throws Exception {
		Optional<LeaveTracker> optional = leaveTrackerRepository.getEmpDetailsById(id,ProdConstant.TRUE);
		try {
			if (optional.isPresent()) {
				return optional;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while find employee details by id  ");
		}
		return optional;
	}

	@Override
	public List<String> getAllEmp() throws Exception {
		List<String> getAllEmp = leaveTrackerRepository.getAllEmp(ProdConstant.TRUE);
		try {
			if (Objects.nonNull(getAllEmp)) {
				return getAllEmp;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while getAllEmp ");
		}
		return new ArrayList<>();

	}

	@Override
	public List<Float> getAllEmpTotalLeave() throws Exception {
		List<Float> getAllEmpTotalLeave = leaveTrackerRepository.getAllEmpTotalLeave(ProdConstant.TRUE);
		try {
			if (Objects.nonNull(getAllEmpTotalLeave)) {
				return getAllEmpTotalLeave;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while getAllEmpTotalLeave  ");
		}
		return new ArrayList<>();
	}

	@Override
	public void updateTotalLeave(float totalleave, String empId) throws Exception {
		try {
			leaveTrackerRepository.updateTotalLeave(totalleave, empId, ProdConstant.TRUE);
		} catch (Exception e) {
			LOGGER.error("Error occur while updateTotalLeave ");
		}
	}

	@Override
	public List<Float> getAllEmpPaidLeave() throws Exception {
		List<Float> getAllEmpPaidLeave = leaveTrackerRepository.getAllEmpPaidLeave(ProdConstant.TRUE);
		try {
			if (Objects.nonNull(getAllEmpPaidLeave)) {
				return getAllEmpPaidLeave;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while getAllEmpPaidLeave ");
		}
		return new ArrayList<>();
	}

	@Override
	public void updatePaidLeave(float paidleave, String empId) throws Exception {
		try {
			leaveTrackerRepository.updatePaidLeave(paidleave, empId, ProdConstant.TRUE);
		} catch (Exception e) {
			LOGGER.error("Error occur while updatePaidLeave ");
		}
	}

	@Override
	public List<Float> getAllEmpMaternityLeave() throws Exception {
		List<Float> getAllEmpMaternityLeave = leaveTrackerRepository.getAllEmpMaternityLeave(ProdConstant.TRUE);
		try {
			if (Objects.nonNull(getAllEmpMaternityLeave)) {
				return getAllEmpMaternityLeave;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while getAllEmpMaternityLeave ");
		}
		return new ArrayList<>();
	}

	@Override
	public void updateMaternityLeave(float maternityLeave, String empId) throws Exception {
		try {
			leaveTrackerRepository.updateMaternityLeave(maternityLeave, empId, ProdConstant.TRUE);
		} catch (Exception e) {
			LOGGER.error("Error occur while updateMaternityLeave ");
		}
	}

	@Override
	public List<String> getMarriedEmployee() throws Exception {
		List<String> getMarriedEmployee = empPersonalRepository.getMarriedEmployee(ProdConstant.MARRIED);
		try {
			if (Objects.nonNull(getMarriedEmployee)) {
				return getMarriedEmployee;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while getMarriedEmployee ");
		}
		return new ArrayList<>();
	}
	
	@Override
	public LeaveTracker getCurrentUser(String empId) throws Exception {
		LeaveTracker leaveTracker = null;
		try {
			leaveTracker = leaveTrackerRepository.getCurrentUser(empId, ProdConstant.TRUE);
		} catch (Exception e) {
			LOGGER.error("Error occur while get current employee data " + ExceptionUtils.getStackTrace(e));
		}
		return leaveTracker;
	}
}