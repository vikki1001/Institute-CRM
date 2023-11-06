package com.ksv.ktrccrm.dao.impl;

import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.dao.TenantConfigDao;
import com.ksv.ktrccrm.db1.entities.TenantConfig;
import com.ksv.ktrccrm.db1.repository.TenantConfigRepository;

@Repository
public class TenantConfigDaoImpl implements TenantConfigDao {
	private static final Logger LOGGER = LogManager.getLogger(TenantConfigDaoImpl.class);

	@Autowired
	TenantConfigRepository tenantConfigRepository;

	@Override
	public void saveTenantConfig(TenantConfig tenantConfig) throws Exception {
		try {
			if (Objects.nonNull(tenantConfig)) {
				System.out.println("Inside Dao..");
				this.tenantConfigRepository.save(tenantConfig);
			}
		} catch (Exception e) {
			LOGGER.error("Error while Tenant id not found for id " + ExceptionUtils.getStackTrace(e));
		}
	}

}
