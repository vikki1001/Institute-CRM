package com.ksv.ktrccrm.service.Impl;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ksv.ktrccrm.dao.TenantMstDao;
import com.ksv.ktrccrm.db1.entities.TenantMst;
import com.ksv.ktrccrm.service.TenantMstService;

@Service
public class TenantMstServiceImpl implements TenantMstService {
	private static final Logger LOGGER = LogManager.getLogger(TenantMstServiceImpl.class);

	@Autowired
	TenantMstDao tenantMstDao;

	@Override
	public TenantMst getTenantDetails(String tenantId,String isActive) throws Exception {
		TenantMst tenantMst = null;
		try {
			tenantMst = tenantMstDao.getTenantDetails(tenantId, isActive);
		} catch (Exception e) {
			LOGGER.error("------Error occur while get tenantId ------" + ExceptionUtils.getStackTrace(e));
		}
		return tenantMst;
	}

}
