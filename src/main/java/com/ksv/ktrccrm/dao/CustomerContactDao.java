package com.ksv.ktrccrm.dao;

import java.util.List;
import java.util.Optional;

import com.ksv.ktrccrm.db1.entities.CustomerContact;

public interface CustomerContactDao {

	public List<CustomerContact> activeListOfCustomerContact(String isActive) throws Exception;

	public List<CustomerContact> inActiveListOfCustomerContact(String inActive) throws Exception;

	public CustomerContact saveCustomerContact(CustomerContact customerContact) throws Exception;

	public CustomerContact activateCustomerContact(String id) throws Exception;

	public CustomerContact deactivateCustomerContact(String id) throws Exception;

	public Optional<CustomerContact> getCustomerContactById(String id) throws Exception;

	public CustomerContact getDataByCustomerContact(String id) throws Exception;

	public List<String> getListOfCustomerId() throws Exception;
	
	public void saveAll(List<CustomerContact> customerContacts)  throws Exception;
	
	public List<CustomerContact> checkDuplicateCustomerContact(String customerId) throws Exception;

	public boolean checkDuplicateException(String customerId) throws Exception;
	
	public List<CustomerContact> findAll() throws Exception;

	public long getStudentsCount(String isActive) throws Exception;

}
