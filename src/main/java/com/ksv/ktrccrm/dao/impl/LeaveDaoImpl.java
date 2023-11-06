package com.ksv.ktrccrm.dao.impl;

import java.text.SimpleDateFormat;
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
import com.ksv.ktrccrm.dao.LeaveDao;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.LeaveMst;
import com.ksv.ktrccrm.db1.repository.EmpBasicRepository;
import com.ksv.ktrccrm.db1.repository.LeaveRepository;

@Repository
public class LeaveDaoImpl implements LeaveDao {
	private static final Logger LOGGER = LogManager.getLogger(LeaveDaoImpl.class);

	@Autowired
	private LeaveRepository leaveRepository;
	@Autowired
	private EmpBasicRepository empBasicRepository;
	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;

	/* Save & Update Leaves */
	@Override
	public LeaveMst saveOrUpdateLeave(LeaveMst leaveMst) throws Exception {
		String loginId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			loginId = authentication.getName();
			EmpBasicDetails basicDetails = empBasicRepository.getById(loginId);
			if (leaveMst.getId() == null) {
				leaveMst.setTenantId(basicDetails.getTenantId());
				leaveMst.setIsActive(ProdConstant.TRUE);
				leaveMst.setDate(new SimpleDateFormat().format(checkOutDaoImpl.getDateTime()));
				leaveMst.setCreatedDate(checkOutDaoImpl.getDateTime());
				leaveMst.setCreatedBy(loginId);
				leaveMst.setNotification(ProdConstant.UNREAD);
				leaveRepository.save(leaveMst);
			} else {
				System.out.println("Leave Id is already available");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while save & update leaves" + ExceptionUtils.getStackTrace(e));
		}
		return leaveMst;
	}

	/* List Of Leaves */
	@Override
	public List<LeaveMst> getLeaveList() throws Exception {
		List<LeaveMst> leaveList = leaveRepository.findAll();
		try {
			if (!leaveList.isEmpty()) {
				return leaveList;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display leave list" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	/* Active List Of Leaves */
	@Override
	public List<LeaveMst> getRecordList(String empId, String isActive, String tenantId) throws Exception {
		List<LeaveMst> leaveMst = new ArrayList<>();
		try {
			leaveMst = leaveRepository.getRecordList(empId,isActive,tenantId);
		} catch (Exception e) {
			LOGGER.error("Error occur while current emp leave list" + ExceptionUtils.getStackTrace(e));
		}
		return leaveMst;
	}
	/* Cancel Leave by ID */
	@Override
	public LeaveMst cancelLeave(LeaveMst leaveMst) throws Exception {
		try {
			Optional<LeaveMst> leaveMst2 = leaveRepository.findById(leaveMst.getId());
			if (leaveMst2.isPresent()) {
				LeaveMst newLeave = leaveMst2.get();
				newLeave.setIsActive(leaveMst.getIsActive());
				newLeave.setIsActive(ProdConstant.FALSE);

				leaveRepository.save(newLeave);
			} else {
				return leaveMst;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while cancel leave " + ExceptionUtils.getStackTrace(e));
		}
		return leaveMst;
	}

	/* Leave Apply Mail Send Project Manager & HR */
	@Override
	public List<LeaveMst> getLeaveApply(String empId) throws Exception {
		List<LeaveMst> leaveMst = new ArrayList<>();
		try {
			leaveMst = leaveRepository.getLeaveApply(empId, ProdConstant.TRUE);
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
			leaveMst = leaveRepository.getLeaveApply(empId, ProdConstant.TRUE);
		} catch (Exception e) {
			LOGGER.error("Error occur while display reject leave " + ExceptionUtils.getStackTrace(e));
		}
		return leaveMst;
	}

	/* Active Leave by id List for acceptLeave */
	@Override
	public List<LeaveMst> getAcceptLeaveById(Long id) throws Exception {
		List<LeaveMst> leaveMst = new ArrayList<>();
		try {
			leaveMst = leaveRepository.getAcceptLeaveById(id, ProdConstant.TRUE);
		} catch (Exception e) {
			LOGGER.error("Error occur while display accept leave " + ExceptionUtils.getStackTrace(e));
		}
		return leaveMst;
	}

	/* acceptLeave by manager */
	@Override
	public void acceptStatus(String status, String flag, Long id) throws Exception {
		try {
			leaveRepository.acceptStatus(status, flag, id);
		} catch (Exception e) {
			LOGGER.error("Error occur while acceptLeave " + ExceptionUtils.getStackTrace(e));
		}
	}

	/* Get Leave List By Leave Type */
	@Override
	public List<LeaveMst> getLeaveByLeaveType(String leaveType) throws Exception {
		List<LeaveMst> leaveMst = new ArrayList<>();
		try {
			leaveMst = leaveRepository.getLeaveByLeaveType(leaveType, ProdConstant.TRUE);
		} catch (Exception e) {
			LOGGER.error("Error occur while get Apply leave list" + ExceptionUtils.getStackTrace(e));
		}
		return leaveMst;
	}

	/* Get Leave List of Emp with Manager */
	@Override
	public List<LeaveMst> getEmpWithManger(String empId) throws Exception {
		List<LeaveMst> leaveMst = new ArrayList<>();
		try {
			leaveMst = leaveRepository.getEmpWithManger(empId, ProdConstant.TRUE);
		} catch (Exception e) {
			LOGGER.error("Error occur while display  list if emp  with managerId" + ExceptionUtils.getStackTrace(e));
		}
		return leaveMst;
	}

	/* Display Employee Leave request in Manager Dashboard */
	@Override
	public List<LeaveMst> getLeavePending(String userId, String tenantId) throws Exception {
		List<LeaveMst> leaveMst = new ArrayList<>();
		try {
			leaveMst = leaveRepository.getLeavePending(userId, ProdConstant.TRUE, ProdConstant.PENDING, tenantId);
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while display leave request in manager dashboard " + ExceptionUtils.getStackTrace(e));
		}
		return leaveMst;
	}

	@Override
	public List<LeaveMst> onLeaveToday(String tenantId) throws Exception {
		List<LeaveMst> leaveMst = leaveRepository.onLeaveToday(ProdConstant.TRUE, ProdConstant.APPROVED, tenantId);
		try {
			if (Objects.nonNull(leaveMst)) {
				return leaveMst;
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while display employee today leave on dashboard " + ExceptionUtils.getStackTrace(e));
		}
		return leaveMst;
	}

	/* Employee leave Find by id */
	@Override
	public Optional<LeaveMst> findLeaveById(Long id) throws Exception {
		Optional<LeaveMst> optional = leaveRepository.findById(id);
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
		List<LeaveMst> leaveMsts = leaveRepository.getTotalLeave(ProdConstant.APPROVED, ProdConstant.TRUE);
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
		List<LeaveMst> leaveMsts = leaveRepository.getTotalPendingLeave(ProdConstant.PENDING, ProdConstant.TRUE);
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
			return leaveRepository.getLeaveDays(userId, ProdConstant.TRUE);
		} catch (Exception e) {
			LOGGER.error("Error while get leave count(days) of employee in current month "
					+ ExceptionUtils.getStackTrace(e));
		}
		return (long) 0;
	}

	@Override
	public Long getAllLeaves(String tenantId) throws Exception {
		try {
			return leaveRepository.getAllLeaves(ProdConstant.APPROVED, ProdConstant.TRUE, tenantId);
		} catch (Exception e) {
			LOGGER.error(
					"Error while get all leave days of employee in current month " + ExceptionUtils.getStackTrace(e));
		}
		return (long) 0;
	}

	@Override
	public Long getAllPendingLeaves(String tenantId) throws Exception {
		try {
			return leaveRepository.getAllPendingLeaves(ProdConstant.PENDING, ProdConstant.TRUE, tenantId);
		} catch (Exception e) {
			LOGGER.error("Error while get all pending leave days of employee in current month "
					+ ExceptionUtils.getStackTrace(e));
		}
		return (long) 0;
	}

	@Override
	public LeaveMst notificationRead(Long id) throws Exception {
		try {
			Optional<LeaveMst> optional = leaveRepository.findById(id);
			if (optional.isPresent()) {
				LeaveMst leaveMst2 = optional.get();
				leaveMst2.setNotification(ProdConstant.READ);

				leaveRepository.save(leaveMst2);
				return leaveMst2;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while read notification /n" + ExceptionUtils.getStackTrace(e));
		}
		return new LeaveMst();
	}

	@Override
	public List<LeaveMst> leaveMstsNotificationBell(String userId) throws Exception {
		List<LeaveMst> leaveMsts = leaveRepository.leaveMstsNotificationBell(userId, ProdConstant.TRUE);
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
		List<LeaveMst> leaveMsts = leaveRepository.unreadNotification(userId, ProdConstant.UNREAD, ProdConstant.TRUE);
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
		List<LeaveMst> leaveMstList = leaveRepository.getEmpMonthLeavesList(empId,status,isActive);
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