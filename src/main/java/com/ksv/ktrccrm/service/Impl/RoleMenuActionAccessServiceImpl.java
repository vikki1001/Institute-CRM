package com.ksv.ktrccrm.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ksv.ktrccrm.dao.RoleMenuActionAccessDao;
import com.ksv.ktrccrm.db1.entities.RoleMenuActionAccess;
import com.ksv.ktrccrm.service.RoleMenuActionAccessService;

@Service
public class RoleMenuActionAccessServiceImpl implements RoleMenuActionAccessService {
	private static final Logger LOGGER = LogManager.getLogger(RoleMenuActionAccessServiceImpl.class);

	@Autowired
	private RoleMenuActionAccessDao roleMenuActionAccessDao;

	@Override
	public List<RoleMenuActionAccess> getMenuListByModuleRoleMap(String TenantId, String roleCode, String isActive)
			throws Exception {
		try {
			List<RoleMenuActionAccess> roleMenuActionAccesses = roleMenuActionAccessDao.getMenuListByModuleRoleMap(TenantId, roleCode, isActive);
			if (Objects.nonNull(roleMenuActionAccesses)) {
				return roleMenuActionAccesses;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display menu list " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<RoleMenuActionAccess> getAllMenusByRoleCode(String roleCode) throws Exception {
		try {
			List<RoleMenuActionAccess> roleMenuActionAccesses =  roleMenuActionAccessDao.getAllMenusByRoleCode(roleCode);
			if (Objects.nonNull(roleMenuActionAccesses)) {
				return roleMenuActionAccesses;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display menu by role code " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	public List<RoleMenuActionAccess> getRoleMenuActionAccessListByRoleCode(String roleCode) throws Exception {
		try {
			List<RoleMenuActionAccess> roleMenuActionAccesses = roleMenuActionAccessDao.getRoleMenuActionAccessListByRoleCode(roleCode);
			if (Objects.nonNull(roleMenuActionAccesses)) {
				return roleMenuActionAccesses;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display isactive role menu action access list " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	public void saveMenus(List<RoleMenuActionAccess> roleMenuActionAccess) throws Exception {
		try {
			if (Objects.nonNull(roleMenuActionAccess)) {
				this.roleMenuActionAccessDao.saveMenus(roleMenuActionAccess);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while save menus " + ExceptionUtils.getStackTrace(e));
		}
	}
}
