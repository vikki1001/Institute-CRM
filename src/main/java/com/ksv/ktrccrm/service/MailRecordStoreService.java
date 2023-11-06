package com.ksv.ktrccrm.service;

import java.util.List;

import com.ksv.ktrccrm.db1.entities.MailRecordStore;

public interface MailRecordStoreService {

	public void saveAllRecords(List<MailRecordStore> ccmrList) throws Exception;

	public void saveRecords(MailRecordStore mailRecord) throws Exception;

	public MailRecordStore getById(Long id) throws Exception;

	public List<MailRecordStore> getPendingRecords(String pending) throws Exception;

}
