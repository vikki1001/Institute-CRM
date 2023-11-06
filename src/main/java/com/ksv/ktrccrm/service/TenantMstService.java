package com.ksv.ktrccrm.service;

import com.ksv.ktrccrm.db1.entities.TenantMst;

public interface TenantMstService {

	public TenantMst getTenantDetails(String tenantId,String isActive) throws Exception;

}
