package com.ksv.ktrccrm.db1.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;

public interface EmpBasicRepository extends JpaRepository<EmpBasicDetails, String> {

	@Query("SELECT e FROM EmpBasicDetails e WHERE e.coEmailId = :coEmailId")
	Optional<EmpBasicDetails> findEmpByEmail(String coEmailId);

	@Query("SELECT u FROM EmpBasicDetails u WHERE u.isActive=:isActive AND u.tenantId =:tenantId")
	public List<EmpBasicDetails> getRecordList(String isActive, String tenantId);

	@Query("SELECT u FROM EmpBasicDetails u WHERE u.empId = :empId and u.isActive = :isActive")
	public EmpBasicDetails getCurrentUser(String empId,String isActive);
	
	@Query("SELECT u FROM EmpBasicDetails u WHERE u.empId = :empId")
	public EmpBasicDetails getDataByEmpId(String empId);

	/* Employee Summary Report */
	@Query("SELECT e FROM EmpBasicDetails e WHERE e.empId LIKE %:empId% AND e.fullName LIKE %:fullName% AND e.departName LIKE %:departName% AND e.grade LIKE %:grade% AND e.isActive = :isActive")
	public List<EmpBasicDetails> getBySearch(@Param("empId") String empId, @Param("fullName") String fullName,
			@Param("departName") String departName, @Param("grade") String grade, String isActive);

	@Query("SELECT u FROM EmpBasicDetails u WHERE u.empId = :empId and u.isActive=:isActive")
	public List<EmpBasicDetails> listOfCurrentUser(String empId,String isActive);

	/* Search Box in DashBoard */
	@Query("SELECT e FROM EmpBasicDetails e WHERE e.fullName LIKE %:value% AND e.isActive=:isActive")
	public List<EmpBasicDetails> getSearchEmployeeByFullName(String value, String isActive);

	@Query("SELECT u FROM EmpBasicDetails u WHERE u.tenantId = :tenantId")
	public EmpBasicDetails getEmployeeId(String tenantId);

	/* Escalation Manager */
	@Query("SELECT e FROM EmpBasicDetails e WHERE e.empId LIKE %:value% AND e.tenantId = :tenantId AND e.isActive = :isActive OR e.fullName LIKE %:value% AND e.tenantId = :tenantId AND e.isActive = :isActive")
	public List<EmpBasicDetails> getSearchEmpByIdAndFullName(String value, String tenantId, String isActive);
	
	@Query("SELECT DISTINCT(u.tenantId) FROM EmpBasicDetails u WHERE u.isActive=:isActive")
	public List<String> getUniqueTenantId(String isActive);

	@Query("SELECT u.empId FROM EmpBasicDetails u WHERE u.isActive = :isActive")
	public List<String> getEmpIdWithIsActive(String isActive);	
	
	@Query("SELECT u.tenantId FROM EmpBasicDetails u WHERE u.empId = :userId")
	public String getTenantIdWithEmpId(String userId);
	
	/* DashBoard Count for HR */
	@Query("SELECT count(u) FROM EmpBasicDetails u WHERE u.isActive = :isActive")
	public Long getAllEmployees(String isActive);
	
	@Transactional
	@Modifying
	@Query("UPDATE EmpBasicDetails e SET e.file = ?1 WHERE e.empId = ?2")
	public void uploadImage(byte[] imageData, String empId);
		
	@Query("SELECT u FROM EmpBasicDetails u WHERE u.departName = :depId AND u.empId = :empId AND u.isActive = :isActive ORDER BY u.createdDate DESC")
	public EmpBasicDetails getKRAWithDepIdAndEmpId(String depId, String empId,String isActive);

	@Query("SELECT DISTINCT(u.departName) FROM EmpBasicDetails u WHERE u.isActive=:isActive ORDER BY u.departName ASC")
	public List<String> getUniqueDepId(String isActive);
	
	@Query("SELECT u FROM EmpBasicDetails u WHERE u.empWorkDetails.reportingManager = :empId OR u.empWorkDetails.reportingManager IN :empIds AND u.departName = :depId")
	public List<EmpBasicDetails> getEmpWithMangerIdOrReportingManager(String empId, String depId, List<String> empIds);
	
	@Query("SELECT u.coEmailId FROM EmpBasicDetails u WHERE u.isActive =:isActive AND coEmailId IS NOT NULL")
	public String[] getAllEmpMailIdsForBirthDay(String isActive);
	
	@Query("SELECT u.coEmailId FROM EmpBasicDetails u WHERE u.empId IN :empId")
	public String[] getBirthdayMailId(List<String> empId);

	@Query("SELECT u.coEmailId FROM EmpBasicDetails u WHERE u.departName = :departName AND u.grade = :grade") 
	public String[] getMailIdForSendMailByAdmin(String departName, String grade); 
	
	@Query("SELECT u FROM EmpBasicDetails u WHERE u.coEmailId IN :getMailIds")
	public List<EmpBasicDetails> getAllEmpDataAccordingToMailIds(String[] getMailIds);
	
	@Query("SELECT u FROM EmpBasicDetails u WHERE u.coEmailId =:coEmailId")
	public EmpBasicDetails getEmpData(String coEmailId);

	@Query("SELECT u.coEmailId FROM EmpBasicDetails u WHERE u.isActive =:isActive")
    public String[] findAll(String isActive);
	
	@Query("SELECT u.coEmailId FROM EmpBasicDetails u WHERE u.departName =:departName")
	public String[] findAll1(String departName);
	
	@Query("SELECT u.coEmailId FROM EmpBasicDetails u WHERE u.grade =:grade")
	public String[] findAll2(String grade); 
	
}