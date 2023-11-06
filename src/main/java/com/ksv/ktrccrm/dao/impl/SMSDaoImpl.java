package com.ksv.ktrccrm.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
import com.ksv.ktrccrm.dao.SMSDao;
import com.ksv.ktrccrm.db1.entities.SMSTemplate;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.db1.repository.SMSRepository;
import com.ksv.ktrccrm.service.UserService;

@Repository
public class SMSDaoImpl implements SMSDao {
	private static final Logger LOGGER = LogManager.getLogger(SMSDaoImpl.class);

	@Autowired
	public SMSRepository smsRepository;
	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;
	@Autowired
	private UserService userService;
	
	@Override
	public SMSTemplate getById(Long id) throws Exception {
		Optional<SMSTemplate> optional = smsRepository.findByid(id);
		SMSTemplate smsTemplate = null;
		try {
			if (optional.isPresent()) {
				smsTemplate = optional.get();
			}
		} catch (Exception e) {
			LOGGER.error("Error Email sms not found for id " + ExceptionUtils.getStackTrace(e));
		}
		return smsTemplate;
	}

	@Override
	public List<SMSTemplate> getRecordList(String isActive,  String tenantId) throws Exception {
		List<SMSTemplate> smsTemplates = smsRepository.getRecordList(isActive, tenantId);
		try {
			if (!smsTemplates.isEmpty()) {
				return smsTemplates;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display sms list " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}


	@Override
	public SMSTemplate save(SMSTemplate smsTemplate) throws Exception {
		try {
		String loginId;
		String empId = null;
		String tenantId = null;
		UserMst user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		empId = authentication.getName();
		if(Objects.nonNull(empId)) {
			user = userService.getUserById(empId);
			tenantId = user.getTenantId();
			if(Objects.nonNull(tenantId)) {
			loginId = authentication.getName();
			if (smsTemplate.getId() == null) {
				smsTemplate.setIsActive(ProdConstant.TRUE);
				smsTemplate.setTenantId(tenantId);
				smsTemplate.setSmsFlag(0);
				smsTemplate.setVersion(0);
				smsTemplate.setCreatedDate(checkOutDaoImpl.getDateTime());
				smsTemplate.setCreatedBy(loginId);
				this.smsRepository.save(smsTemplate);
			} else {
				Optional<SMSTemplate> smsTemplates = smsRepository.findByid(smsTemplate.getId());
				if (smsTemplates.isPresent()) {
					SMSTemplate newsmsTemplate = smsTemplates.get();
					newsmsTemplate.setAdditionalNo(smsTemplate.getAdditionalNo());
					newsmsTemplate.setSmsMsg(smsTemplate.getSmsMsg());
					newsmsTemplate.setLastModifiedDate(checkOutDaoImpl.getDateTime());
					newsmsTemplate.setLastModifiedBy(loginId);
					smsRepository.save(newsmsTemplate);
				} else {
					return smsTemplate;
				}
			}
			}
			else{
				System.out.println("tenantId is null ");
			}
			} 
		}catch (Exception e) {
			LOGGER.error("Error occur while save & update sms " + ExceptionUtils.getStackTrace(e));
		}
		return smsTemplate;
	}
}