package com.ksv.ktrccrm.dao;

import com.ksv.ktrccrm.db1.entities.TenantMst;

public interface TenantMstDao {

	public TenantMst getTenantDetails(String tenantId,String isActive) throws Exception;

}
