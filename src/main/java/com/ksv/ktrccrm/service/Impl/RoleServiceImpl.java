package com.ksv.ktrccrm.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.RoleDao;
import com.ksv.ktrccrm.db1.entities.RoleMst;
import com.ksv.ktrccrm.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
	private static final Logger LOGGER = LogManager.getLogger(RoleServiceImpl.class);

	@Autowired
	private RoleDao roleDao;
	
	/* List Of IsActive Roles */
	public List<RoleMst> getRecordList(String isActive, String tenantId) throws Exception {
		List<RoleMst> roleList = roleDao.getRecordList(isActive, tenantId);
		try {
			if (Objects.nonNull(roleList)) {
				return roleList;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display role list " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}


	/* For Save & Update Role */
	public void saveRole(RoleMst role) throws Exception {
		try {
			if (Objects.nonNull(role)) {
				this.roleDao.saveRole(role);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while save & update role " + ExceptionUtils.getStackTrace(e));
		}
	}

	/* Find Role By Id */
	public RoleMst getRoleById(Long roleId) throws Exception {
		RoleMst roleMst = roleDao.getRoleById(roleId);
		try {
			if (Objects.nonNull(roleMst)) {
				return roleMst;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while role not found for id " + ExceptionUtils.getStackTrace(e));
		}
		return new RoleMst();
	}

	/* For Duplicate Role Code Validation */
	@Override
	public Optional<RoleMst> findRoleByRoleCode(String roleCode) throws Exception {
		Optional<RoleMst> optional = roleDao.findRoleByRoleCode(roleCode);
		try {
			if (optional.isPresent()) {
				return optional;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while find duplicate role " + ExceptionUtils.getStackTrace(e));
		}
		return Optional.empty();
	} 

	@Override
	public boolean roleExists(String roleCode) throws Exception {
		try {
			return findRoleByRoleCode(roleCode).isPresent();
		} catch (Exception e) {
			LOGGER.error("Error occur while find role code " + ExceptionUtils.getStackTrace(e));
		}
		return false;

	}
	public RoleMst roleEnableAndDisable(Long roleId) {
		try {
			RoleMst roleDetails = roleDao.getRoleById(roleId);
			if(Objects.nonNull(roleDetails)){
				String checkActiveOrInActiveData = roleDetails.getIsActive();
				
				if(checkActiveOrInActiveData.equals(ProdConstant.TRUE)) {
					roleDetails.setIsActive(ProdConstant.FALSE);
					roleDao.saveRole(roleDetails);
				}else if(checkActiveOrInActiveData.equals(ProdConstant.FALSE)) {
					roleDetails.setIsActive(ProdConstant.TRUE);
					roleDao.saveRole(roleDetails);
				}
			}else {
			  System.out.println("RoleDetails object is null");	
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while activate and inactivate role code " + ExceptionUtils.getStackTrace(e));
		}
		
		return new RoleMst();
		
	}

}
