package com.ksv.ktrccrm.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.dao.RoleMenuActionAccessDao;
import com.ksv.ktrccrm.db1.entities.RoleMenuActionAccess;
import com.ksv.ktrccrm.db1.repository.RoleMenuActionAccessRepository;

@Repository
public class RoleMenuActionAccessDaoImpl implements RoleMenuActionAccessDao {
	private static final Logger LOGGER = LogManager.getLogger(RoleMenuActionAccessDaoImpl.class);

	@Autowired
	private RoleMenuActionAccessRepository roleMenuActionAccessRepository;

	public List<RoleMenuActionAccess> getMenuListByModuleRoleMap(String tenantId, String roleCode, String isActive)
			throws Exception {
		List<RoleMenuActionAccess> roleMenuActionAccess = new ArrayList<>();
		try {
			roleMenuActionAccess = roleMenuActionAccessRepository.getMenuListByRoleMap(tenantId, roleCode, isActive);
		} catch (Exception e) {
			LOGGER.error("Error occur while find menu list " + ExceptionUtils.getStackTrace(e));
		}
		return roleMenuActionAccess;
	}

	public List<RoleMenuActionAccess> getAllMenusByRoleCode(String roleCode) throws Exception {
		List<RoleMenuActionAccess> roleMenuActionAccess = roleMenuActionAccessRepository.getMenuByRoleCode(roleCode);
		try {
			if (Objects.nonNull(roleMenuActionAccess)) {
				return roleMenuActionAccess;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while find menus by role code " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	/* List Of IsActive RoleMenuActionAccess */
	public List<RoleMenuActionAccess> getRoleMenuActionAccessListByRoleCode(String roleCode) throws Exception {
		List<RoleMenuActionAccess> roleMenuActionAccessList = roleMenuActionAccessRepository.getRoleMenuActionAccessListByRoleCode(roleCode);
		try {
			if (Objects.nonNull(roleMenuActionAccessList)) {
				return roleMenuActionAccessList;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display isactive role menu action access list" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	public void saveMenus(List<RoleMenuActionAccess> roleMenuActionAccess) throws Exception {
		try {
			if (Objects.nonNull(roleMenuActionAccess)) {
				this.roleMenuActionAccessRepository.saveAll(roleMenuActionAccess);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while save menus" + ExceptionUtils.getStackTrace(e));
		}
	}
}
