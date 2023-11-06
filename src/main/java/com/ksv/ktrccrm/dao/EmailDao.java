package com.ksv.ktrccrm.dao;

import java.util.List;

import com.ksv.ktrccrm.db1.entities.EmailTemplate;

public interface EmailDao {

	public List<EmailTemplate> getRecordList(String isActive, String tenantId) throws Exception;
	
	public EmailTemplate getById(Long id)throws Exception;

	public EmailTemplate save(EmailTemplate emailTemplate) throws Exception;

	public EmailTemplate getEmailTemplate(String isActive, String templateName) throws Exception;

}
