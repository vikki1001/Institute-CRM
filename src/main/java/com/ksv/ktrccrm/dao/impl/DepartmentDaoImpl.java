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
import com.ksv.ktrccrm.dao.DepartmentDao;
import com.ksv.ktrccrm.db1.entities.DepartmentMst;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.repository.DepartmentRepository;
import com.ksv.ktrccrm.db1.repository.EmpBasicRepository;
import com.ksv.ktrccrm.db1.repository.RoleRepository;

@Repository
public class DepartmentDaoImpl implements DepartmentDao {
	private static final Logger LOGGER = LogManager.getLogger(DepartmentDaoImpl.class);

	@Autowired
	public DepartmentRepository departmentRepository;
	@Autowired
	public RoleRepository roleRepository;
	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;
	@Autowired
	private EmpBasicRepository empBasicRepository;
	
	@Override
	public List<DepartmentMst> getRecordList(String isActive, String tenantId) throws Exception {
		List<DepartmentMst> departmentMst = departmentRepository.getRecordList(isActive, tenantId);
		try {
			if (Objects.nonNull(departmentMst)) {
				return departmentMst;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display department list " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}
	@Override
	public DepartmentMst saveDepartment(DepartmentMst departmentMst) throws Exception {
		String loginId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			loginId = authentication.getName();
			EmpBasicDetails userDetails = empBasicRepository.getCurrentUser(loginId,ProdConstant.TRUE);
		System.out.println("TenantId:::::::::"+ userDetails.getTenantId() + "************" + departmentMst.getDepartmentId());
			String tenantId = userDetails.getTenantId();
			if(Objects.nonNull(tenantId)) {
			Optional<DepartmentMst> departments = departmentRepository
					.findByDepartmentId(departmentMst.getDepartmentId());
			if (departments.isPresent()) {
				System.out.println("Inside if....................");
				DepartmentMst newdepartment = departments.get();
				newdepartment.setDepartmentId(departmentMst.getDepartmentId());
				newdepartment.setDepartmentName(departmentMst.getDepartmentName());
				newdepartment.setRoleId(departmentMst.getRoleId());
				newdepartment.setDepartmentDesc(departmentMst.getDepartmentDesc());
				newdepartment.setLastModifiedDate(checkOutDaoImpl.getDateTime());
				newdepartment.setLastModifiedBy(loginId);
				newdepartment.setTenantId(tenantId);
				departmentRepository.save(newdepartment);
			} else {
				System.out.println("Inside else....................");
				departmentMst.setIsActive(ProdConstant.TRUE);
				departmentMst.setIsAdminFlag(0);
				departmentMst.setVersion(0);
				departmentMst.setCreatedDate(checkOutDaoImpl.getDateTime());
				departmentMst.setCreatedBy(loginId);
				departmentMst.setTenantId(tenantId);
				this.departmentRepository.save(departmentMst);
			}
		}else {
			System.out.println("TenantId is null  ");
		}
		}
		catch (Exception e) {
			LOGGER.error("Error occur while save & update department " + ExceptionUtils.getStackTrace(e));
		}
		return departmentMst;
	}

	@Override
	public DepartmentMst findByDepartmentId(Long departmentId) throws Exception {
		Optional<DepartmentMst> optional = departmentRepository.findByDepartmentId(departmentId);
		DepartmentMst departmentMst = null;
		try {
			if (optional.isPresent()) {
				departmentMst = optional.get();
			}
		} catch (Exception e) {
			LOGGER.error("Error occur department not found by id " + ExceptionUtils.getStackTrace(e));
		}
		return departmentMst;
	}

	@Override
	public Optional<DepartmentMst> findDeptByDeptName(String departmentName) throws Exception {
		Optional<DepartmentMst> departmentMst = departmentRepository.findDeptByDeptName(departmentName);
		try {
			if (Objects.nonNull(departmentMst)) {
				return departmentMst;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while find duplicate department name" + ExceptionUtils.getStackTrace(e));
		}
		return Optional.empty();
	}

	@Override
	public boolean departmentExists(String departmentName) throws Exception {
		try {
			return findDeptByDeptName(departmentName).isPresent();
		} catch (Exception e) {
			LOGGER.error("Error occur while find department code " + ExceptionUtils.getStackTrace(e));
		}
		return false;

	}


	@Override
	public DepartmentMst getDepartmentDetails(Long departmentId) throws Exception {
		DepartmentMst departmentDetails = null;
		try {
			departmentDetails = departmentRepository.getDepartmentDetails(departmentId);
		} catch (Exception e) {
			LOGGER.error("Error occur while get department details " + ExceptionUtils.getStackTrace(e));
		}
		return departmentDetails;
	}
}
