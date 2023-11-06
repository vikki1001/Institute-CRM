package com.ksv.ktrccrm.db1.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ksv.ktrccrm.db1.entities.EmpKRA;

@Transactional
public interface EmpKRARepository extends JpaRepository<EmpKRA, Long> {

	@Query("SELECT u FROM EmpKRA u WHERE u.empId = :empId AND u.isActive = :isActive")
	public List<EmpKRA> getCurrentEmpAppraisal(String empId,String isActive);

	@Query("SELECT u FROM EmpKRA u WHERE u.empId = :empId AND u.selfAppStatus = :selfAppStatus ORDER BY date DESC")
	public List<EmpKRA> appraisalCycleList(String empId,String selfAppStatus);

	@Query("SELECT u FROM EmpKRA u WHERE u.empId = :empId")
	public Optional<EmpKRA> findByempId(String empId);

	@Query("SELECT u FROM EmpKRA u WHERE EXISTS (SELECT u FROM EmpWorkDetails u WHERE u.reportingManager = :managerId) AND u.empId = :empId")
	public EmpKRA getManagerIdWithMangerId(String managerId, String empId);

	@Query("SELECT u FROM EmpKRA u WHERE u.depId = :depId AND u.empId = :empId AND u.isActive = :isActive ORDER BY createdDate DESC")
	public EmpKRA getKRAWithDepIdAndEmpId(String depId, String empId, String isActive);

	@Query("SELECT u FROM EmpKRA u WHERE u.managerId = :managerId AND u.depId = :depId AND u.empId = :empId")
	public EmpKRA getManagerIdWithMangerIdWithDepId(String managerId, String depId, String empId);

	@Query("SELECT u FROM EmpKRA u WHERE u.managerId = :empId AND u.depId = :depId OR u.managerId IN :empIds")
	public List<EmpKRA> getEmpWithMangerIdOrReportingManager(String empId, String depId, List<String> empIds);

	@Query("SELECT u FROM EmpKRA u WHERE u.managerId = :empId AND u.depId = :depId")
	public List<EmpKRA> getTeammetsTeamMangerId(String empId, String depId);

	@Query("SELECT u FROM EmpKRA u WHERE u.depId = :depId AND u.empId = :empId AND u.isActive = :isActive AND u.date = :date")
	public EmpKRA getKRAWithDepIdAndEmpIdAndDate(String depId, String empId, String isActive, Date date);

	@Query("SELECT u FROM EmpKRA u WHERE u.empId = :empId AND u.isActive = :isActive")
	public List<EmpKRA> findEmpByEmpId(@Param("empId") String empId, String isActive);

	@Query("SELECT u FROM EmpKRA u WHERE u.depId = :depId AND u.isActive = :isActive")
	public List<EmpKRA> findEmpByDepId(@Param("depId") String depId,String isActive);
	
	@Query("SELECT u FROM EmpKRA u WHERE u.isActive = :isActive")
	public List<EmpKRA> findEmpByIsactive(String isActive);

	@Query("SELECT u FROM EmpKRA u WHERE u.empId = :empId AND u.depId = :depId AND u.isActive = :isActive")
	public List<EmpKRA> findEmpByEmpIdAndDepId(@Param("empId") String empId, @Param("depId") String depId, String isActive);

	/* For Duplicate KRA Check */
//	@Query("SELECT u FROM EmpKRA u WHERE u.empId = :empId AND u.kraI = :kraI, u.kraII = :kraI, u.kraIII = :kraI, u.kraIV = :kraI, u.kraV = :kraI, u.kraVI = :kraI, u.kraVII = :kraI, u.kraVIII = :kraI, u.kraIX = :kraI, u.kraX = :kraI")
//	public Optional<EmpKRA> duplicateEmpKRACheck(String empId ,String kraI);

}