package com.ksv.ktrccrm.dao.impl;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.TenantMstDao;
import com.ksv.ktrccrm.db1.entities.TenantMst;
import com.ksv.ktrccrm.db1.repository.TenantMstRepository;

@Repository
public class TenantMstDaoImpl implements TenantMstDao{
	private static final Logger LOGGER = LogManager.getLogger(TenantMstDaoImpl.class);

	@Autowired
	TenantMstRepository tenantMstRepository;

	@Override
	public TenantMst getTenantDetails(String tenantId,String isActive) throws Exception {
		TenantMst tenantMst = null;
		try {
			tenantMst = tenantMstRepository.getTenantDetails(tenantId,  isActive);
		} catch (Exception e) {
			LOGGER.error("Error occur while disply tenantId " + ExceptionUtils.getStackTrace(e));
		}
		return tenantMst;
	}

}
