package com.ksv.ktrccrm.service;

import java.util.List;
import java.util.Optional;

import com.ksv.ktrccrm.db1.entities.DepartmentMst;

public interface DepartmentService {

	public List<DepartmentMst> getRecordList(String isActive, String tenantId) throws Exception;

	public DepartmentMst getByDepartmentId(Long departmentId) throws Exception;

	public void saveDepartment(DepartmentMst department) throws Exception;

	public Optional<DepartmentMst> findDeptByDeptName(String departmentName) throws Exception;

	/* For Duplicate Role Code Validation */
	public boolean departmentExists(String departmentCode) throws Exception;

	public DepartmentMst getDepartmentDetails(Long departmentId) throws Exception;

	public DepartmentMst deptEnableAndDisable(Long departmentId) throws Exception;

}
