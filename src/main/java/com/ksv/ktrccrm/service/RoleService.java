package com.ksv.ktrccrm.service;

import java.util.List;
import java.util.Optional;

import com.ksv.ktrccrm.db1.entities.RoleMst;

public interface RoleService {

	public List<RoleMst> getRecordList(String isActive, String tenantId) throws Exception;

	public void saveRole(RoleMst role) throws Exception;

	public RoleMst getRoleById(Long roleId) throws Exception;
	
	/* For Duplicate Role Code Validation */
	public Optional<RoleMst> findRoleByRoleCode(String roleCode) throws Exception;

	public boolean roleExists(String roleCode) throws Exception;

	public RoleMst roleEnableAndDisable(Long roleId) throws Exception;

}
