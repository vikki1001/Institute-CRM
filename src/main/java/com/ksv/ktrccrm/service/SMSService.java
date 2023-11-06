package com.ksv.ktrccrm.service;

import java.util.List;

import com.ksv.ktrccrm.db1.entities.SMSTemplate;

public interface SMSService {

	public List<SMSTemplate> getRecordList(String isActive, String tenantId ) throws Exception;

	public SMSTemplate getById(Long id) throws Exception;

	public SMSTemplate save(SMSTemplate smsTemplate) throws Exception;

	public SMSTemplate SMSEnableAndDisable(Long id) throws Exception;
}
