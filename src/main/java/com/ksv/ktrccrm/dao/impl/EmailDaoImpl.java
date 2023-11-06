package com.ksv.ktrccrm.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.EmailDao;
import com.ksv.ktrccrm.db1.entities.EmailTemplate;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.repository.EmailRepository;
import com.ksv.ktrccrm.db1.repository.EmpBasicRepository;

@Repository
public class EmailDaoImpl implements EmailDao {
	private static final Logger LOGGER = LogManager.getLogger(EmailDaoImpl.class);

	@Autowired
	public EmailRepository emailRepository;
	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;
	@Autowired
	private EmpBasicRepository empBasicRepository;

	@Override
	public EmailTemplate save(EmailTemplate emailTemplate) throws Exception {
		String loginId = null;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			EmpBasicDetails userDetails = empBasicRepository.getCurrentUser(loginId,ProdConstant.TRUE);
			System.out.println("TenantId:::::::::"+ userDetails.getTenantId());
			loginId = authentication.getName();
			if (emailTemplate.getId() == null) {
				emailTemplate.setIsActive(ProdConstant.TRUE);
				emailTemplate.setTenantId(userDetails.getTenantId());
				emailTemplate.setEmailFlag(0);
				emailTemplate.setVersion(0);
				emailTemplate.setCreatedDate(checkOutDaoImpl.getDateTime());
				emailTemplate.setCreatedBy(loginId);
				System.out.println("Inside save method DAO 1....");
				this.emailRepository.save(emailTemplate);
			} else {
				Optional<EmailTemplate> emailTemplates = emailRepository.findByid(emailTemplate.getId());
				if (emailTemplates.isPresent()) {
					EmailTemplate newEmailTemplate = emailTemplates.get();
					newEmailTemplate.setTemplateName(emailTemplate.getTemplateName());
					newEmailTemplate.setTemplateDesc(emailTemplate.getTemplateDesc());
					newEmailTemplate.setEmailTo(emailTemplate.getEmailTo());
					newEmailTemplate.setEmailCc(emailTemplate.getEmailCc());
					newEmailTemplate.setEmailBcc(emailTemplate.getEmailBcc());
					newEmailTemplate.setEmailSub(emailTemplate.getEmailSub());
					newEmailTemplate.setEmailMsg(emailTemplate.getEmailMsg());
					newEmailTemplate.setLastModifiedDate(checkOutDaoImpl.getDateTime());
					newEmailTemplate.setLastModifiedBy(loginId);
					emailRepository.save(newEmailTemplate);
				} else {
					return emailTemplate;
				}
			}
		} catch (Exception e) {
			LOGGER.error("------Error occur while save & update role------" + ExceptionUtils.getStackTrace(e));
		}
		return emailTemplate;
	}

	@Override
	public List<EmailTemplate> getRecordList(String isActive, String tenantId) throws Exception {
		List<EmailTemplate> emailTemplate = emailRepository.getRecordList(isActive, tenantId);
		try {
			if (!emailTemplate.isEmpty()) {
				return emailTemplate;
			}
		} catch (Exception e) {
			LOGGER.error("------Error occur while display role list------" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public EmailTemplate getById(Long id) throws Exception {
		Optional<EmailTemplate> optional = emailRepository.findByid(id);
		EmailTemplate emailTemplate = null;
		try {
			if (optional.isPresent()) {
				emailTemplate = optional.get();
			}
		} catch (Exception e) {
			LOGGER.error("Error while gmail template by id " + ExceptionUtils.getStackTrace(e));
		}
		return emailTemplate;
	}
	@Override
	public EmailTemplate getEmailTemplate(String isActive, String templateName) throws Exception {
		EmailTemplate emailTemplate = new EmailTemplate();
		try {
			emailTemplate = emailRepository.getEmailTemplate(isActive,templateName);
		} catch (Exception e) {
			LOGGER.error("Error occur while get email template" + ExceptionUtils.getStackTrace(e));
		}
		return emailTemplate;
	}
}


