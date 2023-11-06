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
import com.ksv.ktrccrm.dao.DepartmentDao;
import com.ksv.ktrccrm.db1.entities.DepartmentMst;
import com.ksv.ktrccrm.service.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService {
	private static final Logger LOGGER = LogManager.getLogger(DepartmentServiceImpl.class);

	@Autowired
	private DepartmentDao departmentDao;
	
	@Override
	public void saveDepartment(DepartmentMst departmentMst) throws Exception {
		try {
			if (Objects.nonNull(departmentMst)) {
				departmentDao.saveDepartment(departmentMst);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while save & update role " + ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public List<DepartmentMst> getRecordList(String isActive, String tenantId) throws Exception {
		List<DepartmentMst> departmentMst = departmentDao.getRecordList(isActive, tenantId);
		try {
			if (Objects.nonNull(departmentMst)) {
				return departmentMst;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display role list " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}
	@Override
	public DepartmentMst getByDepartmentId(Long departmentId) throws Exception {
		DepartmentMst departmentMst = departmentDao.findByDepartmentId(departmentId);
		try {
			if (Objects.nonNull(departmentMst)) {
				return departmentMst;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while role not found by id " + ExceptionUtils.getStackTrace(e));
		}
		return new DepartmentMst();
	}

	@Override
	public Optional<DepartmentMst> findDeptByDeptName(String departmentName) throws Exception {
		Optional<DepartmentMst> optional = departmentDao.findDeptByDeptName(departmentName);
		try {
			if (optional.isPresent()) {
				return optional;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while find duplicate department name " + ExceptionUtils.getStackTrace(e));
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
		DepartmentMst departments = null;
		try {
		 departments = departmentDao.getDepartmentDetails(departmentId);
		} catch (Exception e) {
			LOGGER.error("Error occur while get department details" + ExceptionUtils.getStackTrace(e));
		}
		return departments;
	}

	public DepartmentMst deptEnableAndDisable(Long departmentId){
	try {
		DepartmentMst deptDetails = departmentDao.getDepartmentDetails(departmentId);
		if(Objects.nonNull(deptDetails)){
			String checkActiveOrInActiveData = deptDetails.getIsActive();
			if(checkActiveOrInActiveData.equals(ProdConstant.TRUE)) {
				deptDetails.setIsActive(ProdConstant.FALSE);
				departmentDao.saveDepartment(deptDetails);
			}else if(checkActiveOrInActiveData.equals(ProdConstant.FALSE)) {
				deptDetails.setIsActive(ProdConstant.TRUE);
				departmentDao.saveDepartment(deptDetails);
			}
		}else {   
		  System.out.println("deptDetails object is null");	
		}
	} catch (Exception e) {
		LOGGER.error("Error occur while activate and deactivate department  " + ExceptionUtils.getStackTrace(e));
	}
	return new DepartmentMst();
   }
}
