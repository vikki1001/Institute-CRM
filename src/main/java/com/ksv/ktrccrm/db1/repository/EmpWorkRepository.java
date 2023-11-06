package com.ksv.ktrccrm.db1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ksv.ktrccrm.db1.entities.EmpWorkDetails;

public interface EmpWorkRepository extends JpaRepository<EmpWorkDetails, String> {

	@Query("SELECT u FROM EmpWorkDetails u WHERE u.empBasicDetails.isActive = :isActive AND u.reportingManager = :empId AND u.empBasicDetails.tenantId=:tenantId")
	public List<EmpWorkDetails> getEmpWithManger(String empId, String isActive, String tenantId);

	@Query("SELECT u FROM EmpWorkDetails u WHERE u.reportingManager = :empId AND u.empBasicDetails.empId IN :empId2")
	public EmpWorkDetails getEmpWithManger1(String empId, List<String> empId2);

	@Query("SELECT u.empBasicDetails.empId FROM EmpWorkDetails u WHERE u.reportingManager = :empId")
	public List<String> getEmpWithManger3(String empId);

	@Query("SELECT u.empBasicDetails.empId FROM EmpWorkDetails u WHERE u.reportingManager = :empId")
	public List<String> getAllEmpId(String empId);

	@Query("SELECT u FROM EmpWorkDetails u WHERE u.empBasicDetails.empId IN :empIds")
	public List<EmpWorkDetails> getAllEmpJoiningDate(List<String> empIds);

	@Query("SELECT u.empBasicDetails.empId FROM EmpWorkDetails u WHERE u.reportingManager = :empId AND  u.empBasicDetails.empId IN (SELECT u.empId FROM EmpBasicDetails u WHERE u.departName = :depId AND u.isActive = :isActive)")
	public List<String> getTeammetsTeamMangerId2(String empId, String depId,String isActive);

}