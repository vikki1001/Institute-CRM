package com.ksv.ktrccrm.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ksv.ktrccrm.dao.SMSRecordStoreDao;
import com.ksv.ktrccrm.db1.entities.SMSRecordStore;
import com.ksv.ktrccrm.service.SMSRecordStoreService;

@Service
public class SMSRRecordStoreServiceImpl implements SMSRecordStoreService {
	private static final Logger LOGGER = LogManager.getLogger(SMSRRecordStoreServiceImpl.class);
	
	@Autowired
	private  SMSRecordStoreDao smsRecordStoreDao; 
	
	@Override
	public void saveAllSMSRecords(List<SMSRecordStore> smsRecordStores) throws Exception {
		try {
			smsRecordStoreDao.saveAllSMSRecords(smsRecordStores);
		} catch (Exception e) {
			LOGGER.error("Error occur while save list of SMS record " + ExceptionUtils.getStackTrace(e));
		}

	}

	@Override
	public List<SMSRecordStore> getPendingRecords(String pending) throws Exception {
		List<SMSRecordStore> smsRecordStores = smsRecordStoreDao.getPendingRecords(pending);
		try {
			if (Objects.nonNull(smsRecordStores)) {
				return smsRecordStores;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get list of pending SMS record " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public SMSRecordStore getById(Long id) throws Exception {
		SMSRecordStore smsRecordStores = smsRecordStoreDao.getById(id);
		try {
			if (Objects.nonNull(smsRecordStores)) {
				return smsRecordStores;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get record by id " + ExceptionUtils.getStackTrace(e));
		}
		return new SMSRecordStore();
	}

}
