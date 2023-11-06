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

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.EmpRegistartionDao;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.EmpPersonalDetails;
import com.ksv.ktrccrm.db1.entities.EmpWorkDetails;
import com.ksv.ktrccrm.service.EmpRegistartionService;

@Service
public class EmpRegistartionServiceImpl implements EmpRegistartionService {
	private static final Logger LOG = LogManager.getLogger(EmpRegistartionServiceImpl.class);

	@Autowired
	private EmpRegistartionDao empRegistartionDao;
	
	@Override
	public List<EmpBasicDetails> getFindAllEmpRegList() throws Exception {
		List<EmpBasicDetails> empBasicDetails = new ArrayList<>();
		try {
			empBasicDetails = empRegistartionDao.getFindAllEmpRegList();
		} catch (Exception e) {
			LOG.error("Error occur while display findAll employee registartion list"
					+ ExceptionUtils.getStackTrace(e));
		}
		return empBasicDetails;
	}

	/* Display List of IsActive Employee Registration list */
	@Override
	public List<EmpBasicDetails> getRecordList(String isActive, String tenantId) throws Exception {
		List<EmpBasicDetails> empBasicDetails = new ArrayList<>();
		try {
			empBasicDetails = empRegistartionDao.getRecordList(isActive, tenantId);
		} catch (Exception e) {
			LOG.error("Error occur while display employee registartion list"
					+ ExceptionUtils.getStackTrace(e));
		}
		return empBasicDetails;
	}


	/* Find Emp By Id */
	@Override
	public EmpBasicDetails getEmpById(String empId) throws Exception {
		EmpBasicDetails empBasicDetails = new EmpBasicDetails();
		try {
			empBasicDetails = empRegistartionDao.getEmpById(empId);
		} catch (Exception e) {
			LOG.error("Error while get employee by id " + ExceptionUtils.getStackTrace(e));
		}
		return empBasicDetails;
	}

	@Override
	public Optional<EmpBasicDetails> findEmpByEmail(String coEmailId) throws Exception {
		return empRegistartionDao.findEmpByEmail(coEmailId);
	}

	@Override
	public boolean userExists(String coEmailId) throws Exception {
		return empRegistartionDao.userExists(coEmailId);
	}

	/* Save Record */
	@Override
	public EmpBasicDetails saveBasicDetails(EmpBasicDetails empBasicDetails) throws Exception {
		try {
			empRegistartionDao.saveBasicDetails(empBasicDetails);
		} catch (Exception e) {
			LOG.error("Error occur while save basic record" + ExceptionUtils.getStackTrace(e));
		}
		return empBasicDetails;
	}

	@Override
	public Optional<EmpBasicDetails> getImageById(String empId) throws Exception {
		return empRegistartionDao.findEmpByEmail(empId);
	}
	/* Get Current Active Employee Data by empId */
	@Override
	public EmpBasicDetails getCurrentUser(String empId) throws Exception {
		EmpBasicDetails basicDetails = null;
		try {
			basicDetails =  this.empRegistartionDao.getCurrentUser(empId);
		} catch (Exception e) {
			LOG.error("Error occur while get current employee data" + ExceptionUtils.getStackTrace(e));
		}
		return basicDetails;
	}

	/* Get Employee Image by empId */
	@Override
	public EmpBasicDetails getDataByEmpId(String empId) throws Exception {
		EmpBasicDetails basicDetails = null;
		try {
			basicDetails =  this.empRegistartionDao.getDataByEmpId(empId);
		} catch (Exception e) {
			LOG.error("Error occur while display employee Image" + ExceptionUtils.getStackTrace(e));
		}
		return basicDetails;
	}

	@Override
	public List<EmpWorkDetails> getEmpWithManger(String userId, String tenantId) throws Exception {
		List<EmpWorkDetails> empWorkDetails = empRegistartionDao.getEmpWithManger(userId,tenantId);
		try {
			if (Objects.nonNull(empWorkDetails)) {
				return empWorkDetails;
			}
		} catch (Exception e) {
			LOG.error("Error occur while display records to manager" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<String> getEmpIdWithIsActive() throws Exception {
		List<String> listOfEmp = empRegistartionDao.getEmpIdWithIsActive();
		try {
			if (Objects.nonNull(listOfEmp)) {
				return listOfEmp;
			}
		} catch (Exception e) {
			LOG.error("Error occur while get list of active employee " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public Long getAllEmployees() throws Exception {
		try {
			return empRegistartionDao.getAllEmployees();
		} catch (Exception e) {
			LOG.error("Error occuring while get all employee " + ExceptionUtils.getStackTrace(e));
		}
		return (long) 0;
	}

	@Override
	public List<String> getUniqueTenantId() throws Exception {
		List<String> listOfUniqueTenantId = empRegistartionDao.getUniqueTenantId();
		try {
			if (Objects.nonNull(listOfUniqueTenantId)) {
				return listOfUniqueTenantId;
			} else {
				System.out.println("list Of Unique Tenant Id is null");
			}
		} catch (Exception e) {
			LOG.error("Error occuring while get Unique Tenant Id  " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<EmpBasicDetails> listOfCurrentUser(String empId) throws Exception {
		List<EmpBasicDetails> empBasicDetails = empRegistartionDao.listOfCurrentUser(empId);
		try {
			if (!empBasicDetails.isEmpty()) {
				return empBasicDetails;
			}
		} catch (Exception e) {
			LOG.error("Error occur while display list of current user" + ExceptionUtils.getStackTrace(e));
		}
		return empBasicDetails;
	}

	@Override
	public void uploadImage(byte[] imageData, String empId) throws Exception {
		try {
			empRegistartionDao.uploadImage(imageData, empId);
		} catch (Exception e) {
			LOG.error("Error occur while upload image " + ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public List<String> getUniqueDepId() throws Exception {
			List<String> empsList = empRegistartionDao.getUniqueDepId();
			try {
				if (Objects.nonNull(empsList)) {
					return empsList;
				}
		} catch (Exception e) {
			LOG.error("Error occur while get unique depId " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<String> getAllEmpId(String empId) throws Exception {
		List<String> empsList = empRegistartionDao.getAllEmpId(empId);
		try {
			if (Objects.nonNull(empsList)) {
				return empsList;
			}
		} catch (Exception e) {
			LOG.error("Error occur while get All EmpId " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<EmpWorkDetails> getAllEmpJoiningDate(List<String> empIds) throws Exception {
		List<EmpWorkDetails> empsList = empRegistartionDao.getAllEmpJoiningDate(empIds);
		try {
			if (Objects.nonNull(empsList)) {
				return empsList;
			}
		} catch (Exception e) {
			LOG.error("Error occur while get All Emp JoiningDate " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public EmpBasicDetails getKRAWithDepIdAndEmpId(String depId, String reportingManager) throws Exception {
		EmpBasicDetails empBasicDetails = empRegistartionDao.getKRAWithDepIdAndEmpId(depId, reportingManager);
		try {
			if (Objects.nonNull(empBasicDetails)) {
				return empBasicDetails;
			}
		} catch (Exception e) {
			LOG.error("Error occur while get KRA With DepId And EmpId " + ExceptionUtils.getStackTrace(e));
		}
		return new EmpBasicDetails();
	}

	@Override
	public Optional<EmpBasicDetails> findById(String empId) throws Exception {
		Optional<EmpBasicDetails> optional = empRegistartionDao.findById(empId);
		try {
			if (optional.isPresent()) {
				return optional;
			}
		} catch (Exception e) {
			LOG.error("Error occur while get find by id " + ExceptionUtils.getStackTrace(e));
		}
		return Optional.empty();
	}

	@Override
	public List<EmpPersonalDetails> getEmpBirthdayToday(String tenantId) throws Exception {
		List<EmpPersonalDetails> empPersonalDetails = empRegistartionDao.getEmpBirthdayToday(tenantId);
		try {
			if (Objects.nonNull(empPersonalDetails)) {
				return empPersonalDetails;
			}
		} catch (Exception e) {
			LOG.error("Error occur while get All Emp birthday's" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}
	
	@Override
	public List<EmpBasicDetails> getEmpWithMangerIdOrReportingManager(String empId, String depId, List<String> empIds) {
		try {
			return empRegistartionDao.getEmpWithMangerIdOrReportingManager(empId, depId, empIds);
		} catch (Exception e) {
			LOG.error("Error occuring while get Emp With MangerId Or ReportingManager ... " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}
@Override
	public String[] getAllEmpMailIdsForBirthDay() {
		String [] empBasicDetails = null ;
		try {
			empBasicDetails = empRegistartionDao.getAllEmpMailIdsForBirthday();
		} catch (Exception e) {
			LOG.error("Error occuring while get all employees email ids for birthday ... " + ExceptionUtils.getStackTrace(e));

		}
		return empBasicDetails;
	}

	@Override
	public String[] getBirthdayMailId(List<String> empId) throws Exception {
	  String [] empBasicDetails = null;
	  try {
		empBasicDetails = empRegistartionDao.getBirthdayMailId(empId);
	} catch (Exception e) {
		LOG.error("Error occuring while get employee email ids for birthday... " + ExceptionUtils.getStackTrace(e));
	}
		return empBasicDetails;
	}

	@Override
	public String[] getMailIdForSendMailByAdmin(String departmentName,String grade) throws Exception {
		String [] empBasicDetails = null;
		try {
			empBasicDetails = empRegistartionDao.getMailIdForSendMailByAdmin(departmentName,grade);
		} catch (Exception e) {
			LOG.error("Error occuring while get employee email ids for send mail by admin... " + ExceptionUtils.getStackTrace(e));
		}
		return empBasicDetails;
	}

	@Override
	public String getTenantIdWithEmpId(String id) throws Exception {
		String empBasicDetails = empRegistartionDao.getTenantIdWithEmpId(id);
		try {
			if(Objects.nonNull(empBasicDetails)) {
				return empBasicDetails;
			}
		} catch (Exception e) {
			LOG.error("Error occuring while get tenantId according to employee id  " + ExceptionUtils.getStackTrace(e));
		}
		return empBasicDetails;
	}

	@Override
	public List<EmpBasicDetails> getAllEmpDataAccordingToMailIds(String[] getMailIds) throws Exception {
		List<EmpBasicDetails> empBasicDetails = empRegistartionDao.getAllEmpDataAccordingToMailIds(getMailIds);
		try {
			if(Objects.nonNull(empBasicDetails)) {
				return empBasicDetails;
			}
		} catch (Exception e) {
			LOG.error("Error occuring while get employee data according to mail ids  " + ExceptionUtils.getStackTrace(e));
		}
		return empBasicDetails;
	}

	@Override
	public EmpBasicDetails getEmpData(String emailId) throws Exception {
		EmpBasicDetails empBasicDetails = empRegistartionDao.getEmpData(emailId);
		try {
			if(Objects.nonNull(empBasicDetails)) {
				return empBasicDetails;
			}
		} catch (Exception e) {
			LOG.error("Error occuring while get employee data according to mail id  " + ExceptionUtils.getStackTrace(e));
		}
		return empBasicDetails;
	}

	@Override
	public List<EmpPersonalDetails> getEmpBirthdayTodayForMail() throws Exception {
		List<EmpPersonalDetails> empBasicDetails = empRegistartionDao.getEmpBirthdayTodayForMail();
		try {
			if(Objects.nonNull(empBasicDetails)) {
				return empBasicDetails;
			}
		} catch (Exception e) {
			LOG.error("Error occuring while get employee birthday today for mail   " + ExceptionUtils.getStackTrace(e));
		}
		return empBasicDetails;
	}
/* employee Enable And Disable */
	public void empEnableAndDisable(String empId)throws Exception{
		try {
			EmpBasicDetails empDetails = empRegistartionDao.getDataByEmpId(empId);
			if(Objects.nonNull(empDetails)){
				String checkActiveOrInActiveData = empDetails.getIsActive();
				
	System.out.println("active = ::::::::::::::"+checkActiveOrInActiveData);
	
				if(checkActiveOrInActiveData.equals(ProdConstant.TRUE)) {
		System.out.println("if......."+ checkActiveOrInActiveData);
					empDetails.setIsActive(ProdConstant.FALSE);
		System.out.println("if......."+ empDetails.getIsActive());
					empRegistartionDao.saveBasicDetails(empDetails);
					
				}else if(checkActiveOrInActiveData.equals(ProdConstant.FALSE)) {
		System.out.println("else.......");
					empDetails.setIsActive(ProdConstant.TRUE);
					empRegistartionDao.saveBasicDetails(empDetails);
				}
			}else {   
			  System.out.println("empDetails object is null");	
			}
		} catch (Exception e) {
			LOG.error("Error occur while activate and deactivate employee  " + ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public List<String> getUserIdWhoseBirthdayToday()throws Exception{
		List<String> empBasicDetails = empRegistartionDao.getUserIdWhoseBirthdayToday();
		try {
			if(Objects.nonNull(empBasicDetails)) {
				return empBasicDetails;
			}
		} catch (Exception e) {
			LOG.error("Error occuring while get employee birthday today for mail   " + ExceptionUtils.getStackTrace(e));
		}
		return empBasicDetails;
	}

}