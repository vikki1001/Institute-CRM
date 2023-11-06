package com.ksv.ktrccrm.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.dao.MailRecordStoreDao;
import com.ksv.ktrccrm.db1.entities.MailRecordStore;
import com.ksv.ktrccrm.db1.repository.MailRecordStoreRepository;

@Repository
public class MailRecordStoreDaoImpl implements MailRecordStoreDao {
private static final Logger LOGGER = LogManager.getLogger(MailRecordStoreDaoImpl.class);
	
	@Autowired
	private  MailRecordStoreRepository mailRecordStoreRepository; 
	
	@Override
	public void saveAllRecords(List<MailRecordStore> ccmrList) throws Exception {
		try {
			mailRecordStoreRepository.saveAll(ccmrList);
		} catch (Exception e) {
			LOGGER.error("Error occur while save list of mail record " + ExceptionUtils.getStackTrace(e));
		}

	}

	@Override
	public void saveRecords(MailRecordStore mailRecord) throws Exception {
		try {
			
			mailRecordStoreRepository.save(mailRecord);
		} catch (Exception e) {
			LOGGER.error("Error occur while save mail record " + ExceptionUtils.getStackTrace(e));
		}
		
	}

	@Override
	public MailRecordStore getById(Long id) throws Exception {
		MailRecordStore mailRecordStore = mailRecordStoreRepository.getById(id);
		try {
			if (Objects.nonNull(mailRecordStore)) {
				return mailRecordStore;
			}	
		} catch (Exception e) {
			LOGGER.error("Error occur while get record by id " + ExceptionUtils.getStackTrace(e));
		}
		return new MailRecordStore();
	}

	@Override
	public List<MailRecordStore> getPendingRecords(String pending) throws Exception {
		List<MailRecordStore> mailRecordStores =  mailRecordStoreRepository.getPendingRecords(pending);
		try {
			if (Objects.nonNull(mailRecordStores)) {
				return mailRecordStores;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get list of pending record " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}
}
