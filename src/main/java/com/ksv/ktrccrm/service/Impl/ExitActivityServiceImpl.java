package com.ksv.ktrccrm.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ksv.ktrccrm.dao.ExitActivityDao;
import com.ksv.ktrccrm.db1.entities.ExitActivity;
import com.ksv.ktrccrm.service.ExitActivityService;

@Service
public class ExitActivityServiceImpl implements ExitActivityService {
	private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

	@Autowired
	private ExitActivityDao exitActivityDao;

	@Override
	public List<ExitActivity> getRecordList(String empId,String isActive, String tenantId) throws Exception {
		List<ExitActivity> exitList = new ArrayList<>();
		try {
			exitList = exitActivityDao.getRecordList(empId,isActive, tenantId);
		} catch (Exception e) {
			LOGGER.error("Error occur while display isactive exit list" + ExceptionUtils.getStackTrace(e));
		}
		return exitList;
	}
	@Override
	public List<ExitActivity> getAllEmp() throws Exception {
		List<ExitActivity> exitActivity = new ArrayList<>();
		try {
			exitActivity = exitActivityDao.getAllEmp();
		} catch (Exception e) {
			LOGGER.error("Error occur while get Employee list" + ExceptionUtils.getStackTrace(e));
		}
		return exitActivity;
	}

	/* For Save ExitActivity */
	@Override
	public ExitActivity saveExitActivity(ExitActivity exitActivity) throws Exception {
		try {
			exitActivityDao.saveExitActivity(exitActivity);
		} catch (Exception e) {
			LOGGER.error("Error occur while save Exit Activity" + ExceptionUtils.getStackTrace(e));
		}
		return exitActivity;
	}

	/* Deactive Registered ExitActivity */
	@Override
	public ExitActivity cancelById(ExitActivity exitActivity) throws Exception {
		try {
			exitActivity = exitActivityDao.cancelById(exitActivity);
		} catch (Exception e) {
			LOGGER.error("Error occur while deactivate registered exit activity " + ExceptionUtils.getStackTrace(e));
		}
		return exitActivity;
	}

	@Override
	public List<ExitActivity> getExitActivityById(String empId) throws Exception {
		try {
			return exitActivityDao.getExitActivityById(empId);
		} catch (Exception e) {
			LOGGER.error("Error occur while get exit activity by empId " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<ExitActivity> getEmpWithManger(String empId, String tenantId) throws Exception {
		try {
			return exitActivityDao.getEmpWithManger(empId, tenantId);
		} catch (Exception e) {
			LOGGER.error("Error occur while display exit activity to manager " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public void acceptStatus(String status, String flag, Long id) throws Exception {
		try {
			exitActivityDao.acceptStatus(status, flag, id);
		} catch (Exception e) {
			LOGGER.error("Error occur while update exit activity to manager approved/Reject request "
					+ ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public List<ExitActivity> acceptExitActivityById(Long id) throws Exception {
		try {
			return exitActivityDao.acceptExitActivityById(id);
		} catch (Exception e) {
			LOGGER.error("Error occur while get exit activity by Id " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<ExitActivity> getEmpWithMangerWithPending(String userId, String tenantId) throws Exception {
		try {
			return exitActivityDao.getEmpWithMangerWithPending(userId, tenantId);
		} catch (Exception e) {
			LOGGER.error("Error occur while display exit activity to manager with pending status "
					+ ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<ExitActivity> getTotalExitActivity() throws Exception {
		List<ExitActivity> exitActivities = exitActivityDao.getTotalExitActivity();
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
			return exitActivityDao.getAllExitActivity(tenantId);
		} catch (Exception e) {
			LOGGER.error("Error occuring while get all exit activity of employees in current month "
					+ ExceptionUtils.getStackTrace(e));
		}
		return (long) 0;
	}

	@Override
	public ExitActivity notificationRead(Long id) throws Exception {
		try {
			exitActivityDao.notificationRead(id);
		} catch (Exception e) {
			LOGGER.error("Error occur while save notification /n" + ExceptionUtils.getStackTrace(e));
		}
		return new ExitActivity();
	}

	@Override
	public List<ExitActivity> exitActivityNotificationBell(String userId) throws Exception {
		List<ExitActivity> exitActivities = exitActivityDao.exitActivityNotificationBell(userId);
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
		List<ExitActivity> exitActivities = exitActivityDao.unreadNotification(userId);
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