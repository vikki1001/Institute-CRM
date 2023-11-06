package com.ksv.ktrccrm.dao;

import java.util.List;
import java.util.Optional;

import com.ksv.ktrccrm.db1.entities.PasswordPolicy;

public interface PasswordPolicyDao {

	public List<PasswordPolicy> findAll() throws Exception;

	public Optional<PasswordPolicy> findById(Long id) throws Exception;

	public PasswordPolicy savePasswordPolicy(PasswordPolicy newPasswordPolicy) throws Exception;
	
	public PasswordPolicy getDataBytenantId(String tenantId) throws Exception;
	
	
	public PasswordPolicy findByTenantIdAndIsactive(String tenantId, String isActive);

	public List<PasswordPolicy> getRecordList(String isActive, String tenantId) throws Exception;

}
