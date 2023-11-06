package com.ksv.ktrccrm.service;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ksv.ktrccrm.db1.entities.CustomerContact;

public interface CustomerContactService {

	public List<CustomerContact> activeListOfCustomerContact(String isActive) throws Exception;

	public List<CustomerContact> inActiveListOfCustomerContact(String inActive) throws Exception;
	
	public CustomerContact saveCustomerContact(CustomerContact customerContact) throws Exception;
	
	public CustomerContact activateCustomerContact(String id) throws Exception;
	
	public CustomerContact deactivateCustomerContact(String id) throws Exception;

	public CustomerContact getDataByCustomerContact(String id) throws Exception;
	
	public void saveAll(MultipartFile files)  throws Exception;
	
	public List<CustomerContact> checkDuplicateCustomerContact(String customerId) throws Exception;

	public boolean checkDuplicateException(String customerId) throws Exception;
	
	public List<CustomerContact> findAll() throws Exception;

	public ByteArrayInputStream exportCustomerContactExcel(List<CustomerContact> customerContacts) throws Exception;

	public long getStudentsCount(String isActive) throws Exception;
	
}
