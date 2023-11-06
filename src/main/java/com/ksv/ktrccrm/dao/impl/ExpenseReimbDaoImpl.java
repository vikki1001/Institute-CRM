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
import com.ksv.ktrccrm.dao.ExpenseReimbDao;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.ExpenseReimb;
import com.ksv.ktrccrm.db1.repository.EmpBasicRepository;
import com.ksv.ktrccrm.db1.repository.ExpenseReimbRepository;

@Repository
public class ExpenseReimbDaoImpl implements ExpenseReimbDao {
	private static final Logger LOGGER = LogManager.getLogger(ExpenseReimbDaoImpl.class);

	@Autowired
	private ExpenseReimbRepository expenseReimbRepository;
	@Autowired
	private EmpBasicRepository empBasicRepository;
	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;
	/* List of Active Expense Reimbursement */
	@Override
	public List<ExpenseReimb> getRecordList(String empId, String isActive, String tenantId) throws Exception {
		List<ExpenseReimb> expenseList = expenseReimbRepository.getRecordList(empId,isActive,tenantId);
		try {
			if (!expenseList.isEmpty()) {
				return expenseList;
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while display expense list" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	/* List of Cancel Expense Reimbursement */
	@Override
	public ExpenseReimb getCancelById(ExpenseReimb expenseReimb) throws Exception {
		try {
			Optional<ExpenseReimb> optional = expenseReimbRepository.getCancelById(expenseReimb.getId(),ProdConstant.TRUE);
			if (optional.isPresent()) {
				ExpenseReimb newExpenseReimb = optional.get();
				newExpenseReimb.setIsActive(expenseReimb.getIsActive());
				newExpenseReimb.setIsActive("0");
				expenseReimbRepository.save(newExpenseReimb);
				return newExpenseReimb;
			} else {
				return expenseReimb;
			}
		} catch (Exception e) {
			LOGGER.error("------Error occur while cancel expense by id------" + ExceptionUtils.getStackTrace(e));
		}
		return expenseReimb;
	}

	/* Save Expense Reimbursement Data */
	@Override
	public ExpenseReimb saveExpenseReimb(ExpenseReimb expenseReimb) throws Exception {
		String empId;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		empId = authentication.getName();
		EmpBasicDetails empBasicDetails = null;
		try {
			if (Objects.nonNull(empId)) {
				empBasicDetails = empBasicRepository.getCurrentUser(empId,ProdConstant.TRUE);
				System.out.println("GET TENANTID      " + empBasicDetails.getTenantId());
				if (Objects.isNull(expenseReimb.getId()) && Objects.nonNull(empBasicDetails)) {
					expenseReimb.setIsActive("1");
					expenseReimb.setEmpId(empId);
					expenseReimb.setStatus("Pending");
					expenseReimb.setNotification("Unread");
					expenseReimb.setCreatedDate(checkOutDaoImpl.getDateTime());
					expenseReimb.setCreatedBy(empId);
					expenseReimb.setSaveDraft("N");
					expenseReimb.setTenantId(empBasicDetails.getTenantId());
					expenseReimbRepository.save(expenseReimb);
				} else {
					Optional<ExpenseReimb> optional=expenseReimbRepository.findById(expenseReimb.getId());
					if(optional.isPresent()) {
						ExpenseReimb expenseUpdate = optional.get();
						expenseUpdate.setAttachment(expenseReimb.getAttachment());
						expenseUpdate.setCurrency(expenseReimb.getCurrency());
						expenseUpdate.setExpenseAmount(expenseReimb.getExpenseAmount());
						expenseUpdate.setLocation(expenseReimb.getLocation());
						expenseUpdate.setReasonsForExpense(expenseReimb.getReasonsForExpense());
						expenseUpdate.setNotification("Unread");
						expenseUpdate.setFlag(expenseReimb.getFlag());
						expenseUpdate.setDate(expenseReimb.getDate());
						expenseUpdate.setReimbursementType(expenseReimb.getReimbursementType());
						expenseUpdate.setLastModifiedBy(empId);
						expenseUpdate.setLastModifiedDate(checkOutDaoImpl.getDateTime());
						expenseUpdate.setStatus("Pending");
						expenseUpdate.setSaveDraft("N");
						expenseReimbRepository.save(expenseUpdate);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("------Error occur while save & update employee Exit Activity------"
					+ ExceptionUtils.getStackTrace(e));
		}
		return expenseReimb;
	}
	
	/* Save Draft Expense Reimbursement Data */
	@Override
	public ExpenseReimb saveDraftExpenseReimb(ExpenseReimb expenseReimb) throws Exception {
		String empId;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		empId = authentication.getName();
		EmpBasicDetails empBasicDetails = null;
		try {
			if (Objects.nonNull(empId)) {
				empBasicDetails = empBasicRepository.getCurrentUser(empId,ProdConstant.TRUE);
				if (Objects.isNull(expenseReimb.getId()) && Objects.nonNull(empBasicDetails)) {
					expenseReimb.setIsActive("1");
					expenseReimb.setEmpId(empId);
					expenseReimb.setStatus("Draft");
					expenseReimb.setNotification("Unread");
					expenseReimb.setCreatedDate(checkOutDaoImpl.getDateTime());
					expenseReimb.setCreatedBy(empId);
					expenseReimb.setSaveDraft("Y");
					expenseReimb.setTenantId(empBasicDetails.getTenantId() );
					expenseReimbRepository.save(expenseReimb);
				} else {
					Optional<ExpenseReimb> optional = expenseReimbRepository.findById(expenseReimb.getId());
					if(optional.isPresent()) {
						ExpenseReimb expenseUpdate = optional.get();
						expenseUpdate.setAttachment(expenseReimb.getAttachment());
						expenseUpdate.setCurrency(expenseReimb.getCurrency());
						expenseUpdate.setExpenseAmount(expenseReimb.getExpenseAmount());
						expenseUpdate.setDate(expenseReimb.getDate());
						expenseUpdate.setReasonsForExpense(expenseReimb.getReimbursementType());
						expenseUpdate.setReimbursementType(expenseReimb.getReimbursementType());
						expenseUpdate.setStatus("Draft");
						expenseUpdate.setNotification(expenseReimb.getNotification());
						expenseUpdate.setLastModifiedBy(empId);
						expenseUpdate.setLastModifiedDate(checkOutDaoImpl.getDateTime());
						expenseReimbRepository.save(expenseUpdate);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while save draft & update employee Exit Activity"
					+ ExceptionUtils.getStackTrace(e));
		}
		return expenseReimb;
	}
	

	/* Expense Reimbursement Apply Mail Send Project Manager, HR & Employee */
	@Override
	public List<ExpenseReimb> getExpenseReimbByEmpId(String empId) throws Exception {
		try {
			return expenseReimbRepository.getExpenseReimbByEmpId(empId,ProdConstant.TRUE);
		} catch (Exception e) {
			LOGGER.error("Error occur while get expense reimbursement by empId "
					+ ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}
	
	/* Accept/Reject Expense Reimbursement by Manager */
	@Override
	public List<ExpenseReimb> acceptExpenseReimbById(Long id) throws Exception {
		try {
			return expenseReimbRepository.acceptExpenseReimbById(id,ProdConstant.TRUE);
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while get expense reimbursement by Id " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	/* Add Some Data in Registered Expense Reimbursement by Approved/Reject Request by Manager */
	@Override
	public void acceptStatus(String status, String flag, Long id) throws Exception {
		try {
			expenseReimbRepository.acceptStatus(status, flag, id);
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while update expense reimbursement to admin approved/Reject request "
							+ ExceptionUtils.getStackTrace(e));
		}
	}

	/* Display Attachment(Image) */
	@Override
	public ExpenseReimb getAttachment(Long id) throws Exception {
		ExpenseReimb expenseReimb = null;
		try {
			return expenseReimbRepository.getAttachment(id);
		} catch (Exception e) {
			LOGGER.error("------ Error occur while display attachment (Image) -----" + ExceptionUtils.getStackTrace(e));
		}
		return expenseReimb;
	}
	/* Display List of Expense Reimbursement to Manager with Pending Status */
	@Override
	public List<ExpenseReimb> getEmpWithMangerWithPending(String tenantId) throws Exception {
		try {
			return expenseReimbRepository.getEmpWithMangerWithPending(ProdConstant.ADMIN, ProdConstant.TRUE, ProdConstant.PENDING, tenantId);
		} catch (Exception e) {
			LOGGER.error("------ Error occur while display expense reimbursement to admin with pending status -----"
					+ ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}
	
	@Override
	public ExpenseReimb notificationRead(Long id) throws Exception {
		try {			
			Optional<ExpenseReimb> optional = expenseReimbRepository.findById(id);
			if (optional.isPresent()) {
				ExpenseReimb reimb = optional.get();
				reimb.setNotification("Read");
				
				expenseReimbRepository.save(reimb);
				return reimb;
			} else {

			}
		} catch (Exception e) {
			LOGGER.error("Error occur while read notification /n" + ExceptionUtils.getStackTrace(e));
		}
		return new ExpenseReimb();
	}
	
	@Override
	public List<ExpenseReimb> getExpenseReimbByEmpId() throws Exception {
		List<ExpenseReimb> expenseReimbs = expenseReimbRepository.getExpenseReimbByEmpId(ProdConstant.ADMIN, ProdConstant.TRUE);
		try {
			if (expenseReimbs != null) {
				return expenseReimbs;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get expense by empId  " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<ExpenseReimb> unreadNotification() throws Exception {
		List<ExpenseReimb> expenseReimbs = expenseReimbRepository.unreadNotification(ProdConstant.TRUE, ProdConstant.UNREAD);
		try {
			if (expenseReimbs != null) {
				return expenseReimbs;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display list of unread notification " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<ExpenseReimb> getEmpDetails(String empId) throws Exception {
		List<ExpenseReimb> expenseReimbs = expenseReimbRepository.getEmpDetails(empId, ProdConstant.TRUE, ProdConstant.FLAG_N);
		try {
			if (expenseReimbs != null) {
				return expenseReimbs;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get list of employee details" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public ExpenseReimb getExpenseById(Long id) throws Exception {
		Optional<ExpenseReimb> optional = expenseReimbRepository.findById(id);
		ExpenseReimb expenseReimb = optional.get();
		try {
			if(optional.isPresent()) {
				return expenseReimb;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get expense by id" + ExceptionUtils.getStackTrace(e));
		}
		
		return expenseReimb;
	}

}