package com.ksv.ktrccrm.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ksv.ktrccrm.dao.ExpenseReimbDao;
import com.ksv.ktrccrm.db1.entities.ExpenseReimb;
import com.ksv.ktrccrm.service.ExpenseReimbService;

@Service
public class ExpenseReimbServiceImpl implements ExpenseReimbService {
	private static final Logger LOGGER = LogManager.getLogger(ExpenseReimbServiceImpl.class);

	@Autowired
	private ExpenseReimbDao expenseReimbDao;

	/* List of Active Expense Reimbursement */
	@Override
	public List<ExpenseReimb> getRecordList(String empId, String isActive, String tenantId) throws Exception {
		List<ExpenseReimb> expenseList = new ArrayList<>();
		try {
			expenseList = expenseReimbDao.getRecordList(empId,isActive,tenantId);
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while display Expense list " + ExceptionUtils.getStackTrace(e));
		}
		return expenseList;
	}
	/* List of Cancel Expense Reimbursement */
	@Override
	public ExpenseReimb getCancelById(ExpenseReimb expenseReimb) throws Exception {
		try {
			expenseReimb = expenseReimbDao.getCancelById(expenseReimb);
		} catch (Exception e) {
			LOGGER.error("Error occur  while cancel Expense by id  " + ExceptionUtils.getStackTrace(e));
		}
		return expenseReimb;
	}

	/* Save Expense Reimbursement Data */
	@Override
	public ExpenseReimb saveExpenseReimb(ExpenseReimb expenseReimb) throws Exception {
		try {
			expenseReimbDao.saveExpenseReimb(expenseReimb);
		} catch (Exception e) {
			LOGGER.error("Error occur while save & update Expense Reimbursement "
					+ ExceptionUtils.getStackTrace(e));
		}
		return expenseReimb;
	}

	/* Expense Reimbursement Apply Mail Send Project Manager, HR & Employee */
	@Override
	public List<ExpenseReimb> getExpenseReimbByEmpId(String empId) throws Exception {
		try {
			return expenseReimbDao.getExpenseReimbByEmpId(empId);
		} catch (Exception e) {
			LOGGER.error("Error occur while get expense reimbursement by empId  "
					+ ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}


	/* Accept/Reject Expense Reimbursement by Manager */
	@Override
	public List<ExpenseReimb> acceptExpenseReimbById(Long id) throws Exception {
		try {
			return expenseReimbDao.acceptExpenseReimbById(id);
		} catch (Exception e) {
			LOGGER.error(" Error occur while accept expense reimbursement by Id -----"
					+ ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();		
	}

	/* Add Some Data in Registered Expense Reimbursement by Approved/Reject Request by Manager */
	@Override
	public void acceptStatus(String status, String flag, Long id) throws Exception {
		try {
			 expenseReimbDao.acceptStatus(status, flag, id);
		} catch (Exception e) {
			LOGGER.error("Error occur while Accept Expense Reimbursement by admin --"
					+ ExceptionUtils.getStackTrace(e));
		}		
	}

	/* Display Attachment(Image) */
	@Override
	public ExpenseReimb getAttachment(Long id) throws Exception {
		ExpenseReimb expenseReimb = null;
		try {
			expenseReimb =  expenseReimbDao.getAttachment(id);
		} catch (Exception e) {
			LOGGER.error("Error occur while display attachment (Image)"
					+ ExceptionUtils.getStackTrace(e));
		}
		return expenseReimb;
	}

	/* Display List of Expense Reimbursement to Manager with Pending Status */
	@Override
	public List<ExpenseReimb> getEmpWithMangerWithPending(String tenantId) throws Exception {
		try {
			return expenseReimbDao.getEmpWithMangerWithPending(tenantId);
		} catch (Exception e) {
			LOGGER.error("Error occur while display expense reimbursement to manager with pending status-----"
					+ ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}
	
	@Override
	public ExpenseReimb notificationRead(Long id) throws Exception {
		try {
			expenseReimbDao.notificationRead(id);
		} catch (Exception e) {
			LOGGER.error("Error occur while read notification /n" + ExceptionUtils.getStackTrace(e));
		}
		return new ExpenseReimb();
	}

	@Override
	public List<ExpenseReimb> expenseReimbNotificationBell() throws Exception {
		List<ExpenseReimb> expenseReimbs = expenseReimbDao.getExpenseReimbByEmpId();
		try {
			if (expenseReimbs != null) {
				return expenseReimbs;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display list of notification " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<ExpenseReimb> unreadNotification() throws Exception {
		List<ExpenseReimb> expenseReimbs = expenseReimbDao.unreadNotification();
		try {
			if (expenseReimbs != null) {
				return expenseReimbs;
			}
		} catch (Exception e) {
			LOGGER.error("------Error occur while display list of unread notification ------" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<ExpenseReimb> getEmpDetails(String empId) throws Exception {
		
		List<ExpenseReimb> expenseReimbs = expenseReimbDao.getEmpDetails(empId);
		try {
			if (expenseReimbs != null) {
				return expenseReimbs;
			}
		} catch (Exception e) {
			LOGGER.error("------Error occur while get list of employee details ------" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public void saveDraftExpenseReimb(ExpenseReimb expenseReimb) throws Exception {
		try {
			expenseReimbDao.saveDraftExpenseReimb(expenseReimb);
		} catch (Exception e) {
			LOGGER.error("Error occur while save draft Expense ------" + ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public ExpenseReimb getExpenseById(Long id) {
		ExpenseReimb expenseReimbs = null;
		try {
			expenseReimbs = expenseReimbDao.getExpenseById(id);
			if (expenseReimbs != null) {
				return expenseReimbs;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get expense by id " + ExceptionUtils.getStackTrace(e));
		}
		return expenseReimbs;
	}

	
}