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
import com.ksv.ktrccrm.dao.SMSDao;
import com.ksv.ktrccrm.db1.entities.SMSTemplate;
import com.ksv.ktrccrm.service.SMSService;

@Service
@Transactional
public class SMSServiceImpl implements SMSService {
	private static final Logger LOGGER = LogManager.getLogger(SMSServiceImpl.class);

	@Autowired
	private SMSDao smsDao;

	@Override
	public SMSTemplate getById(Long id) throws Exception {
		SMSTemplate smsTemplate = new SMSTemplate();
		try {
			smsTemplate = smsDao.getById(id);
		} catch (Exception e) {
			LOGGER.error("Error while sms not found for id " + ExceptionUtils.getStackTrace(e));
		}
		return smsTemplate;
	}

	@Override
	public SMSTemplate save(SMSTemplate smsTemplate) throws Exception {
		try {
			this.smsDao.save(smsTemplate);
		} catch (Exception e) {
			LOGGER.error("Error occur while save & update sms " + ExceptionUtils.getStackTrace(e));
		}
		return smsTemplate;
	}

	@Override
	public List<SMSTemplate> getRecordList(String isActive,  String tenantId) throws Exception {
		List<SMSTemplate> smsTemplates = new ArrayList<>();
		try {
			smsTemplates = smsDao.getRecordList(isActive, tenantId);
		} catch (Exception e) {
			LOGGER.error("Error occur while display sms list " + ExceptionUtils.getStackTrace(e));
		}
		return smsTemplates;
	}

	public SMSTemplate SMSEnableAndDisable(Long id) {
		try {
			SMSTemplate smsDetails = smsDao.getById(id);
			if (Objects.nonNull(smsDetails)) {
				String checkActiveOrInActiveData = smsDetails.getIsActive();

				if (checkActiveOrInActiveData.equals(ProdConstant.TRUE)) {
					smsDetails.setIsActive(ProdConstant.FALSE);
					smsDao.save(smsDetails);
				} else if (checkActiveOrInActiveData.equals(ProdConstant.FALSE)) {
					smsDetails.setIsActive(ProdConstant.TRUE);
					smsDao.save(smsDetails);
				}
			} else {
				System.out.println("docDetails object is null");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while activate and deactivate doc  " + ExceptionUtils.getStackTrace(e));
		}

		return new SMSTemplate();

	}
}