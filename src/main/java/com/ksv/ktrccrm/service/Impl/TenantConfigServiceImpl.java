package com.ksv.ktrccrm.service.Impl;

import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ksv.ktrccrm.dao.TenantConfigDao;
import com.ksv.ktrccrm.db1.entities.TenantConfig;
import com.ksv.ktrccrm.service.TenantConfigService;

@Service
public class TenantConfigServiceImpl implements TenantConfigService {
	private static final Logger LOGGER = LogManager.getLogger(TenantConfigServiceImpl.class);

	@Autowired
	private TenantConfigDao tenantConfigDao;

	/* For Save TenantConfig */
	@Override
	public void saveTenantConfig(TenantConfig tenantConfig) throws Exception {
		try {
			if (Objects.nonNull(tenantConfig)) {
				this.tenantConfigDao.saveTenantConfig(tenantConfig);
			}
		} catch (Exception e) {
			LOGGER.error("Error while Tenant id not found for id " + ExceptionUtils.getStackTrace(e));
		}
	}

}
