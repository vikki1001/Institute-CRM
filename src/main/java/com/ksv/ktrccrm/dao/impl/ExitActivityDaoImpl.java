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
import com.ksv.ktrccrm.dao.ExitActivityDao;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.ExitActivity;
import com.ksv.ktrccrm.db1.repository.EmpBasicRepository;
import com.ksv.ktrccrm.db1.repository.ExitActivityRepository;

@Repository
public class ExitActivityDaoImpl implements ExitActivityDao {
	private static final Logger LOGGER = LogManager.getLogger(ExitActivityDaoImpl.class);

	@Autowired
	private ExitActivityRepository exitActivityRepository;
	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;
	@Autowired
	private EmpBasicRepository empBasicRepository;
	
	/* List of IsActive Activity */
	@Override
	public List<ExitActivity> getRecordList(String empId,String isActive, String tenantId) throws Exception {
		List<ExitActivity> exitList = exitActivityRepository.getRecordList(empId,isActive,tenantId);
		try {
			if (!exitList.isEmpty()) {
				return exitList;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display  exit list" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}
	@Override
	public List<ExitActivity> getAllEmp() throws Exception {
		List<ExitActivity> exitActivity = exitActivityRepository.findAll();
		try {
			if (!exitActivity.isEmpty()) {
				return exitActivity;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get Employee list" + ExceptionUtils.getStackTrace(e));
		}

		return exitActivity;
	}

	/* For Save Exit Activity */
	@Override
	public ExitActivity saveExitActivity(ExitActivity exitActivity) throws Exception {
		String empId;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		empId = authentication.getName();
		try {
			if (Objects.nonNull(empId)) {
			 EmpBasicDetails	empBasicDetails = empBasicRepository.getCurrentUser(empId,ProdConstant.TRUE);
				System.out.println("GET TENANTID      " + empBasicDetails.getTenantId());
			if (Objects.isNull(exitActivity.getId())) {
				exitActivity.setIsActive(ProdConstant.TRUE);
				exitActivity.setManagerId(exitActivity.getEmpId().getEmpWorkDetails().getReportingManager());
				exitActivity.setStatus(ProdConstant.PENDING);
				exitActivity.setNotification(ProdConstant.UNREAD);
				exitActivity.setCreatedDate(checkOutDaoImpl.getDateTime());
				exitActivity.setCreatedBy(empId);
				exitActivity.setRegDate(exitActivity.getRegDate()); // set timezone
				exitActivity.setTenantId(empBasicDetails.getTenantId());
				exitActivityRepository.save(exitActivity);
			} else {
				return exitActivity;
			}
			}
	
		} catch (Exception e) {
			LOGGER.error(" Error occur while save Exit Activity " + ExceptionUtils.getStackTrace(e));
		}
		return exitActivity;
	}

	/* Deactive Registered ExitActivity */
	@Override
	public ExitActivity cancelById(ExitActivity exitActivity) throws Exception {
		try {
			Optional<ExitActivity> optional = exitActivityRepository.cancelById(exitActivity.getId(),
					ProdConstant.TRUE);
			if (optional.isPresent()) {
				ExitActivity newExitActivity = optional.get();
				newExitActivity.setIsActive(exitActivity.getIsActive());
				newExitActivity.setIsActive(ProdConstant.FALSE);

				exitActivityRepository.save(newExitActivity);
				return newExitActivity;
			} else {
				return exitActivity;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while deactivate registered exit activity " + ExceptionUtils.getStackTrace(e));
		}
		return exitActivity;
	}

	@Override
	public List<ExitActivity> getExitActivityById(String empId) throws Exception {
		try {
			return exitActivityRepository.getExitActivityById(empId, ProdConstant.TRUE);
		} catch (Exception e) {
			LOGGER.error("Error occur while get exit activity by empId " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<ExitActivity> getEmpWithManger(String empId, String tenantId) throws Exception {
		try {
			return exitActivityRepository.getEmpWithManger(empId, ProdConstant.TRUE, tenantId);
		} catch (Exception e) {
			LOGGER.error(" Error occur while display exit activity to manager " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public void acceptStatus(String status, String flag, Long id) throws Exception {
		try {
			exitActivityRepository.acceptStatus(status, flag, id);
		} catch (Exception e) {
			LOGGER.error(" Error occur while update exit activity to manager approved/Reject request "
					+ ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public List<ExitActivity> acceptExitActivityById(Long id) throws Exception {
		try {
			return exitActivityRepository.acceptExitActivityById(id, ProdConstant.TRUE);
		} catch (Exception e) {
			LOGGER.error(" Error occur while get exit activity by Id " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<ExitActivity> getEmpWithMangerWithPending(String userId, String tenantId) throws Exception {
		try {
			return exitActivityRepository.getEmpWithMangerWithPending(userId, ProdConstant.PENDING, ProdConstant.TRUE, tenantId);
		} catch (Exception e) {
			LOGGER.error(" Error occur while display exit activity to manager with pending status "
					+ ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<ExitActivity> getTotalExitActivity() throws Exception {
		List<ExitActivity> exitActivities = exitActivityRepository.getTotalExitActivity(ProdConstant.APPROVED,
				ProdConstant.TRUE);
		try {
			if (Objects.nonNull(exitActivities)) {
				return exitActivities;
			} else {
				System.out.println("Null Data Get :::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get list of total exit activity " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public Long getAllExitActivity(String tenantId) throws Exception {
		try {
			return exitActivityRepository.getAllExitActivity(ProdConstant.PENDING, ProdConstant.TRUE, tenantId);
		} catch (Exception e) {
			LOGGER.error("Error occuring while get all exit activity of employees in current month "
					+ ExceptionUtils.getStackTrace(e));
		}
		return (long) 0;
	}

	@Override
	public ExitActivity notificationRead(Long id) throws Exception {
		try {
			Optional<ExitActivity> optional = exitActivityRepository.findById(id);
			if (optional.isPresent()) {
				ExitActivity activity = optional.get();
				activity.setNotification(ProdConstant.READ);

				exitActivityRepository.save(activity);
				return activity;
			} else {

			}
		} catch (Exception e) {
			LOGGER.error("Error occur while save notification /n" + ExceptionUtils.getStackTrace(e));
		}
		return new ExitActivity();
	}

	@Override
	public List<ExitActivity> exitActivityNotificationBell(String userId) throws Exception {
		List<ExitActivity> exitActivities = exitActivityRepository.exitActivityNotificationBell(userId,
				ProdConstant.TRUE);
		try {
			if (exitActivities != null) {
				return exitActivities;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display list of notification " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<ExitActivity> unreadNotification(String userId) throws Exception {
		List<ExitActivity> exitActivities = exitActivityRepository.unreadNotification(userId, ProdConstant.UNREAD,
				ProdConstant.TRUE);
		try {
			if (exitActivities != null) {
				return exitActivities;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display list of unread notification " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}
}
