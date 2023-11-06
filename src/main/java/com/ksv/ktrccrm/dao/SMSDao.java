package com.ksv.ktrccrm.dao;

import java.util.List;

import com.ksv.ktrccrm.db1.entities.SMSTemplate;

public interface SMSDao {

	public List<SMSTemplate> getRecordList(String isActive, String tenantId) throws Exception;

	public SMSTemplate getById(Long id) throws Exception;

	public SMSTemplate save(SMSTemplate smsTemplate) throws Exception;
}
