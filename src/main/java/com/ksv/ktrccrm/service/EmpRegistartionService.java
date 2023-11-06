package com.ksv.ktrccrm.service;

import java.util.List;
import java.util.Optional;

import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.EmpPersonalDetails;
import com.ksv.ktrccrm.db1.entities.EmpWorkDetails;

public interface EmpRegistartionService {

	public EmpBasicDetails getEmpById(String empId) throws Exception;

	public Optional<EmpBasicDetails> findEmpByEmail(String coEmailId) throws Exception;

	public boolean userExists(String coEmailId) throws Exception;

	public EmpBasicDetails saveBasicDetails(EmpBasicDetails empBasicDetails) throws Exception;

	public Optional<EmpBasicDetails> getImageById(String empId) throws Exception;

	public List<EmpBasicDetails> getFindAllEmpRegList() throws Exception;

	public List<EmpBasicDetails> getRecordList(String isActive, String tenantId) throws Exception;

	public EmpBasicDetails getCurrentUser(String empId) throws Exception;

	public EmpBasicDetails getDataByEmpId(String empId) throws Exception;

	public List<EmpWorkDetails> getEmpWithManger(String userId, String tenantId) throws Exception;

	public List<String> getEmpIdWithIsActive() throws Exception;

	public Long getAllEmployees() throws Exception;

	public List<String> getUniqueTenantId() throws Exception;

	public List<EmpBasicDetails> listOfCurrentUser(String empId) throws Exception;

	public void uploadImage(byte[] imageData, String empId) throws Exception;

	public List<String> getUniqueDepId() throws Exception;

	public List<String> getAllEmpId(String empId) throws Exception;

	public List<EmpWorkDetails> getAllEmpJoiningDate(List<String> empIds) throws Exception;

	public EmpBasicDetails getKRAWithDepIdAndEmpId(String depId, String reportingManager) throws Exception;

	public Optional<EmpBasicDetails> findById(String empId) throws Exception;

	public List<EmpPersonalDetails> getEmpBirthdayToday(String tenantId) throws Exception;

	public List<EmpBasicDetails> getEmpWithMangerIdOrReportingManager(String empId, String depId, List<String> empIds) throws Exception;
	
	public String[] getAllEmpMailIdsForBirthDay() throws Exception;
	
	public String[] getBirthdayMailId(List<String> empId) throws Exception;
 
	public String [] getMailIdForSendMailByAdmin(String departmentName,String grade) throws Exception;

	public String getTenantIdWithEmpId(String id) throws Exception;

	public List<EmpBasicDetails> getAllEmpDataAccordingToMailIds(String[] getMailIds)throws Exception;

	public EmpBasicDetails getEmpData(String emailId)throws Exception;

	public List<EmpPersonalDetails> getEmpBirthdayTodayForMail()throws Exception;
	
	public void empEnableAndDisable(String empId) throws Exception;

	public List<String> getUserIdWhoseBirthdayToday() throws Exception;

	
}
