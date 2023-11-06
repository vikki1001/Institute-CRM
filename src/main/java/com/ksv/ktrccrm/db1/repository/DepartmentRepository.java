package com.ksv.ktrccrm.db1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ksv.ktrccrm.db1.entities.DepartmentMst;

public interface DepartmentRepository extends JpaRepository<DepartmentMst, Long> {
	
	@Query("SELECT e FROM DepartmentMst e WHERE e.departmentId = :departmentId")
	public Optional<DepartmentMst> findByDepartmentId(Long departmentId);

	@Query("SELECT u FROM DepartmentMst u WHERE u.isActive=:isActive AND u.tenantId=:tenantId")
	public List<DepartmentMst> getRecordList(String isActive, String tenantId);
	
	@Query("SELECT e FROM DepartmentMst e WHERE e.departmentName = :departmentName")
	public Optional<DepartmentMst> findDeptByDeptName(String departmentName);
	
	@Query("SELECT e FROM DepartmentMst e WHERE e.departmentId = :departmentId")
	public DepartmentMst getDepartmentDetails(Long departmentId);

}
