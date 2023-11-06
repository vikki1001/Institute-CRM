package com.ksv.ktrccrm.dao;

import java.util.List;
import java.util.Optional;

import com.ksv.ktrccrm.db1.entities.RoleMst;

public interface RoleDao {

	public List<RoleMst> getRecordList(String isActive, String tenantId) throws Exception;

	public RoleMst saveRole(RoleMst role) throws Exception;

	public RoleMst getRoleById(Long roleId) throws Exception;

	public Optional<RoleMst> findRoleByRoleCode(String roleCode) throws Exception;

	/* For Duplicate Role Code Validation */
	public boolean roleExists(String roleCode) throws Exception;

}
