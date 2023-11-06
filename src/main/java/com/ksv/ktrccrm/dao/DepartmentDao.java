package com.ksv.ktrccrm.dao;

import java.util.List;
import java.util.Optional;

import com.ksv.ktrccrm.db1.entities.DepartmentMst;

public interface DepartmentDao {
 	  
	  public List<DepartmentMst> getRecordList(String isActive, String tenantId) throws Exception;
	  
	  public DepartmentMst findByDepartmentId(Long departmentId)throws Exception;
	  	 
	  public DepartmentMst saveDepartment(DepartmentMst department) throws Exception;
	  
	  public Optional<DepartmentMst> findDeptByDeptName(String departmentName) throws Exception;
	  
	  /* For Duplicate Role Code Validation */
	  public boolean departmentExists(String departmentName) throws Exception;

	public DepartmentMst getDepartmentDetails(Long departmentId) throws Exception;
}
