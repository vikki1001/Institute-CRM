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

import com.ksv.ktrccrm.dao.LeaveDao;
import com.ksv.ktrccrm.db1.entities.LeaveMst;
import com.ksv.ktrccrm.service.LeaveService;

@Service
public class LeaveServiceImpl implements LeaveService {
	private static final Logger LOGGER = LogManager.getLogger(LeaveServiceImpl.class);

	@Autowired
	private LeaveDao leaveDao;

	/* Save & Update Leave */
	@Override
	public LeaveMst saveOrUpdateLeave(LeaveMst leaveMst) throws Exception {
		try {
			leaveDao.saveOrUpdateLeave(leaveMst);
		} catch (Exception e) {
			LOGGER.error("Error occur while save & update leave " + ExceptionUtils.getStackTrace(e));
		}
		return leaveMst;
	}

	/* List Of Leaves */
	@Override
	public List<LeaveMst> getLeaveList() throws Exception {
		List<LeaveMst> leaveMst = new ArrayList<>();
		try {
			leaveMst = leaveDao.getLeaveList();
		} catch (Exception e) {
			LOGGER.error("Error occur while display leave list" + ExceptionUtils.getStackTrace(e));
		}
		return leaveMst;
	}

	/* Active List Of Leaves */
	@Override
	public List<LeaveMst> getRecordList(String empId, String isActive, String tenantId) throws Exception {
		List<LeaveMst> leaveMst = new ArrayList<>();
		try {
			leaveMst = leaveDao.getRecordList(empId,isActive, tenantId);
		} catch (Exception e) {
			LOGGER.error("Error occur while current emp leave list" + ExceptionUtils.getStackTrace(e));
		}
		return leaveMst;
	}

	/* Cancel Leave by ID */
	@Override
	public LeaveMst cancelLeave(LeaveMst leaveMst) throws Exception {
		try {
			leaveMst = leaveDao.cancelLeave(leaveMst);
		} catch (Exception e) {
			LOGGER.error("Error while cancel leave " + ExceptionUtils.getStackTrace(e));
		}
		return leaveMst;
	}

	/* Leave Apply Mail Send Project Manager & HR */
	@Override
	public List<LeaveMst> getLeaveApply(String empId) throws Exception {
		List<LeaveMst> leaveMst = new ArrayList<>();
		try {
			leaveMst = leaveDao.getLeaveApply(empId);
		} catch (Exception e) {
			LOGGER.error("Error occur while send mail to leave apply " + ExceptionUtils.getStackTrace(e));
		}
		return leaveMst;
	}

	/* Active Leave List for acceptLeave */
	@Override
	public List<LeaveMst> getRejectLeave(String empId) throws Exception {
		List<LeaveMst> leaveMst = new ArrayList<>();
		try {
			leaveMst = leaveDao.getRejectLeave(empId);
		} catch (Exception e) {
			LOGGER.error("Error occur while get reject leave " + ExceptionUtils.getStackTrace(e));
		}
		return leaveMst;
	}

	/* Active Leave by Id List for acceptLeave */
	@Override
	public List<LeaveMst> getAcceptLeaveById(Long id) throws Exception {
		List<LeaveMst> leaveMst = new ArrayList<>();
		try {
			leaveMst = leaveDao.getAcceptLeaveById(id);
		} catch (Exception e) {
			LOGGER.error("Error occur while get accept leave by id  " + ExceptionUtils.getStackTrace(e));
		}
		return leaveMst;
	}

	/* acceptLeave by manager */
	@Override
	public void acceptStatus(String status, String flag, Long id) throws Exception {
		try {
			leaveDao.acceptStatus(status, flag, id);
		} catch (Exception e) {
			LOGGER.error("Error occur while accept Leave " + ExceptionUtils.getStackTrace(e));
		}
	}

	/* Get Leave List By Leave Type */
	@Override
	public List<LeaveMst> getLeaveByLeaveType(String leaveType) throws Exception {
		List<LeaveMst> leaveMst = new ArrayList<>();
		try {
			leaveMst = leaveDao.getLeaveByLeaveType(leaveType);
		} catch (Exception e) {
			LOGGER.error("Error occur while get leave by leave type " + ExceptionUtils.getStackTrace(e));
		}
		return leaveMst;
	}

	/* Get Leave List of Emp with Manager */
	@Override
	public List<LeaveMst> getEmpWithManger(String empId) throws Exception {
		List<LeaveMst> leaveMst = new ArrayList<>();
		try {
			leaveMst = leaveDao.getEmpWithManger(empId);
		} catch (Exception e) {
			LOGGER.error("Error occur while display list if emp  with managerId" + ExceptionUtils.getStackTrace(e));
		}
		return leaveMst;
	}

	/* Display Employee Leave request in Manager Dashboard */
	@Override
	public List<LeaveMst> getLeavePending(String userId, String tenantId) throws Exception {
		List<LeaveMst> leaveMst = new ArrayList<>();
		try {
			leaveMst = leaveDao.getLeavePending(userId,tenantId);
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while display pending leave  " + ExceptionUtils.getStackTrace(e));
		}
		return leaveMst;
	}

	@Override
	public List<LeaveMst> onLeaveToday(String tenantId) throws Exception {
		List<LeaveMst> leaveMst = leaveDao.onLeaveToday(tenantId);
		try {
			if (Objects.nonNull(leaveMst)) {
				return leaveMst;
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while get leave today  " + ExceptionUtils.getStackTrace(e));
		}
		return leaveMst;
	}

	/* Employee leave Find by id */
	@Override
	public Optional<LeaveMst> findLeaveById(Long id) throws Exception {
		Optional<LeaveMst> optional = leaveDao.findLeaveById(id);
		try {
			if (optional.isPresent()) {
				return optional;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display find employee leave by id " + ExceptionUtils.getStackTrace(e));
		}
		return optional;
	}

	@Override
	public List<LeaveMst> getTotalLeave() throws Exception {
		List<LeaveMst> leaveMsts = leaveDao.getTotalLeave();
		try {
			if (Objects.nonNull(leaveMsts)) {
				return leaveMsts;
			} else {
				System.out.println("Null Data Get :::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get list of total leave " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<LeaveMst> getTotalPendingLeave() throws Exception {
		List<LeaveMst> leaveMsts = leaveDao.getTotalPendingLeave();
		try {
			if (Objects.nonNull(leaveMsts)) {
				return leaveMsts;
			} else {
				System.out.println("Null Data Get :::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get list of total pending leave " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public Long getLeaveDays(String userId) throws Exception {
		try {
			return leaveDao.getLeaveDays(userId);
		} catch (Exception e) {
			LOGGER.error("Error while get leave count(days) of employee in current month "
					+ ExceptionUtils.getStackTrace(e));
		}
		return (long) 0;
	}

	@Override
	public Long getAllLeaves(String tenantId) throws Exception {
		try {
			return leaveDao.getAllLeaves(tenantId);
		} catch (Exception e) {
			LOGGER.error(
					"Error while get all leave days of employee in current month " + ExceptionUtils.getStackTrace(e));
		}
		return (long) 0;
	}

	@Override
	public Long getAllPendingLeaves(String tenantId) throws Exception {
		try {
			return leaveDao.getAllPendingLeaves(tenantId);
		} catch (Exception e) {
			LOGGER.error("Error while get all pending leave days of employee in current month "
					+ ExceptionUtils.getStackTrace(e));
		}
		return (long) 0;
	}

	@Override
	public LeaveMst notificationRead(Long id) throws Exception {
		try {
			leaveDao.notificationRead(id);
		} catch (Exception e) {
			LOGGER.error("Error occur while read notification /n" + ExceptionUtils.getStackTrace(e));
		}
		return new LeaveMst();
	}

	@Override
	public List<LeaveMst> leaveMstsNotificationBell(String userId) throws Exception {
		List<LeaveMst> leaveMsts = leaveDao.leaveMstsNotificationBell(userId);
		try {
			if (leaveMsts != null) {
				return leaveMsts;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display list of notification " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<LeaveMst> unreadNotification(String userId) throws Exception {
		List<LeaveMst> leaveMsts = leaveDao.unreadNotification(userId);
		try {
			if (leaveMsts != null) {
				return leaveMsts;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display list of unread notification " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<LeaveMst> getEmpMonthLeavesList(String empId, String status, String isActive) throws Exception {
		List<LeaveMst> leaveMstList = leaveDao.getEmpMonthLeavesList(empId,status,isActive);
		try {
			if(Objects.nonNull(leaveMstList)) {
				return leaveMstList;
			}
			
		} catch (Exception e) {
			LOGGER.error("Error occur while get list of emp month leave " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<> ();
	}
}