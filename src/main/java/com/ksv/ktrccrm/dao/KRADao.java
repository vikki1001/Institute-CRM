package com.ksv.ktrccrm.dao;

import java.util.List;

import com.ksv.ktrccrm.db1.entities.KRA;

public interface KRADao {
	
	public List<KRA> getRecordList(String isActive, String tenantId) throws Exception;
	
	public KRA saveOrUpdate(KRA kra) throws Exception;
	
	public KRA getById(Long id) throws Exception;

	public List<KRA> getKRAWithDepIdAndEmpId(String depId) throws Exception;

	public void deleteById(Long id) throws Exception;
}
