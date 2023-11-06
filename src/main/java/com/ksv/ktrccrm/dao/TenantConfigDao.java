package com.ksv.ktrccrm.dao;

import com.ksv.ktrccrm.db1.entities.TenantConfig;

public interface TenantConfigDao {

	public void saveTenantConfig(TenantConfig tenantConfig) throws Exception;
}
