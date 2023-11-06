package com.ksv.ktrccrm.service;

import java.util.List;

import com.ksv.ktrccrm.db1.entities.EmailTemplate;

public interface EmailService {

	public List<EmailTemplate> getRecordList(String isActive, String tenantId) throws Exception;

	public EmailTemplate getById(Long id) throws Exception;

	public void save(EmailTemplate emailTemplate) throws Exception;

	public EmailTemplate getEmailTemplate(String isActive, String templateName) throws Exception;

	public EmailTemplate emailEnableAndDisable(Long id);

}
