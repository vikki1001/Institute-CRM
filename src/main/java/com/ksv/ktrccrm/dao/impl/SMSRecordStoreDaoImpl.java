package com.ksv.ktrccrm.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.dao.SMSRecordStoreDao;
import com.ksv.ktrccrm.db1.entities.SMSRecordStore;
import com.ksv.ktrccrm.db1.repository.SMSRecordStoreRepository;

@Repository
public class SMSRecordStoreDaoImpl implements SMSRecordStoreDao {
private static final Logger LOGGER = LogManager.getLogger(SMSRecordStoreDaoImpl.class);
	
	@Autowired
	private  SMSRecordStoreRepository smsRecordStoreRepository; 
	
	@Override
	public void saveAllSMSRecords(List<SMSRecordStore> smsRecordStores) throws Exception {
		try {
			smsRecordStoreRepository.saveAll(smsRecordStores);
		} catch (Exception e) {
			LOGGER.error("Error occur while save list of SMS record " + ExceptionUtils.getStackTrace(e));
		}
	}
	
	@Override
	public List<SMSRecordStore> getPendingRecords(String pending) throws Exception {
		List<SMSRecordStore> smsRecordStores = smsRecordStoreRepository.getPendingRecords(pending);
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
		SMSRecordStore smsRecordStores = smsRecordStoreRepository.getById(id);
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
