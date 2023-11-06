package com.ksv.ktrccrm.dao;

import java.util.List;

import com.ksv.ktrccrm.db1.entities.SMSRecordStore;

public interface SMSRecordStoreDao {

	public void saveAllSMSRecords(List<SMSRecordStore> smsRecordStores) throws Exception;
	
	public List<SMSRecordStore> getPendingRecords(String pending) throws Exception;

	public SMSRecordStore getById(Long id) throws Exception;
}
