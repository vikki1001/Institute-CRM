package com.ksv.ktrccrm.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.EmailDao;
import com.ksv.ktrccrm.db1.entities.EmailTemplate;
import com.ksv.ktrccrm.service.EmailService;

@Service
@Transactional
public class EmailServiceImpl implements EmailService {
	private static final Logger LOGGER = LogManager.getLogger(EmailServiceImpl.class);

	@Autowired
	private EmailDao emailDao;
	
	@Override
	public void save(EmailTemplate emailTemplate) throws Exception {
		try {
			this.emailDao.save(emailTemplate);
		} catch (Exception e) {
			LOGGER.error("Error occur while save & update role" + ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public List<EmailTemplate> getRecordList(String isActive, String tenantId) throws Exception {
		List<EmailTemplate> emailTemplate = new ArrayList<>();
		try {
			emailTemplate = emailDao.getRecordList(isActive, tenantId);
		} catch (Exception e) {
			LOGGER.error("Error occur while display role list" + ExceptionUtils.getStackTrace(e));
		}
		return emailTemplate;
	}
	

	@Override
	public EmailTemplate getById(Long id) throws Exception {
		EmailTemplate emailTemplate = new EmailTemplate();
		try {
			emailTemplate = emailDao.getById(id);
		} catch (Exception e) {
			LOGGER.error("Error while gmail template by id" + ExceptionUtils.getStackTrace(e));
		}
		return emailTemplate;
	}
	@Override
	public EmailTemplate getEmailTemplate(String isActive, String templateName) throws Exception {
		EmailTemplate emailTemplate = new EmailTemplate();
		try {
			emailTemplate = emailDao.getEmailTemplate(isActive,templateName);
		} catch (Exception e) {
			LOGGER.error("Error occur while get gmail template" + ExceptionUtils.getStackTrace(e));
		}
		return emailTemplate;
	}

	public EmailTemplate emailEnableAndDisable(Long id) {
		try {
			EmailTemplate emmailDetails = emailDao.getById(id);
			if(Objects.nonNull(emmailDetails)){
				String checkActiveOrInActiveData = emmailDetails.getIsActive();
				
				if(checkActiveOrInActiveData.equals(ProdConstant.TRUE)) {
					emmailDetails.setIsActive(ProdConstant.FALSE);
					emailDao.save(emmailDetails);
				}else if(checkActiveOrInActiveData.equals(ProdConstant.FALSE)) {
					emmailDetails.setIsActive(ProdConstant.TRUE);
					emailDao.save(emmailDetails);
				}
			}else {   
			  System.out.println("empDetails object is null");	
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while activate and deactivate employee  " + ExceptionUtils.getStackTrace(e));
		}
		return new EmailTemplate();
	}
}
