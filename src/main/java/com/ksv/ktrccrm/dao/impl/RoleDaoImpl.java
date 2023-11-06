package com.ksv.ktrccrm.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.RoleDao;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.RoleMst;
import com.ksv.ktrccrm.db1.repository.EmpBasicRepository;
import com.ksv.ktrccrm.db1.repository.RoleRepository;

@Repository
public class RoleDaoImpl implements RoleDao {
	private static final Logger LOGGER = LogManager.getLogger(RoleDaoImpl.class);

	@Autowired
	private RoleRepository roleRepository;
	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;
	@Autowired
	private EmpBasicRepository empBasicRepository;

	/* List Of IsActive Roles */
	public List<RoleMst> getRecordList(String isActive, String tenantId) throws Exception {
		List<RoleMst> roleList = roleRepository.getRecordList(isActive, tenantId);
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
	public RoleMst saveRole(RoleMst roleMst) throws Exception {
		String loginId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			loginId = authentication.getName();
			if (Objects.nonNull(loginId)) {
				EmpBasicDetails userDetails = empBasicRepository.getCurrentUser(loginId,ProdConstant.TRUE);
				System.out.println("TenantId:::::::::"+ userDetails.getTenantId());
				String tenantId = userDetails.getTenantId();
				if(Objects.nonNull(tenantId)) {
			
			if (roleMst.getRoleId() == null) {
				roleMst.setRoleCode(roleMst.getRoleCode().toUpperCase());
				roleMst.setIsActive(ProdConstant.TRUE);
				roleMst.setIsAdminFlag(0);
				roleMst.setVersion(0);
				roleMst.setCreatedDate(checkOutDaoImpl.getDateTime());
				roleMst.setCreatedBy(loginId);
				roleMst.setTenantId(tenantId);
				roleMst = roleRepository.save(roleMst);
			} else {
				Optional<RoleMst> role = roleRepository.findById(roleMst.getRoleId());
				if (role.isPresent()) {
					RoleMst newRole = role.get();
					newRole.setRoleName(roleMst.getRoleName());
					newRole.setLevel(roleMst.getLevel());
					newRole.setIsHeadRoleYn(roleMst.getIsHeadRoleYn());
					newRole.setLastModifiedDate(checkOutDaoImpl.getDateTime());
					newRole.setLastModifiedBy(loginId);
					roleRepository.save(newRole);
				} else {
					return roleMst;
				}
			}
		}else {
			System.out.println("tenantId is null ");
		}
			}
			else {
				System.out.println("loginId is null ");
			}
		}catch (Exception e) {
			LOGGER.error("Error occur while save & update role" + ExceptionUtils.getStackTrace(e));
		}
		return roleMst;
	}

	/* Find Role By Id */
	public RoleMst getRoleById(Long roleId) throws Exception {
		Optional<RoleMst> optional = roleRepository.findById(roleId);
		RoleMst roleMst = null;
		try {
			if (optional.isPresent()) {
				roleMst = optional.get();
			}
		} catch (Exception e) {
			LOGGER.error("Error while Role not found for id " + ExceptionUtils.getStackTrace(e));
		}
		return roleMst;
	}
	/* For Duplicate Role Code Validation */
	@Override
	public Optional<RoleMst> findRoleByRoleCode(String roleCode) throws Exception {
		Optional<RoleMst> optional = roleRepository.findRoleByRoleCode(roleCode);
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
}
