package com.ksv.ktrccrm.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.dao.CustomerContactDao;
import com.ksv.ktrccrm.db1.entities.CustomerContact;
import com.ksv.ktrccrm.db1.repository.CustomerContactRepository;

@Repository
public class CustomerContactDaoImpl implements CustomerContactDao {
	private static final Logger LOGGER = LogManager.getLogger(CustomerContactDaoImpl.class);

	@Autowired
	private CustomerContactRepository customerContactRepository;
	
	@Override
	public List<CustomerContact> activeListOfCustomerContact(String isActive) throws Exception {
		List<CustomerContact> customerContactsList = new ArrayList<>();
		try {
			customerContactsList = customerContactRepository.activeListOfCustomerContact(isActive);
		} catch (Exception e) {
			LOGGER.error("Error occur while display active list of customer contact " + ExceptionUtils.getStackTrace(e));
		}
		return customerContactsList;
	}

	@Override
	public List<CustomerContact> inActiveListOfCustomerContact(String inActive) throws Exception {
		List<CustomerContact> customerContactsList = new ArrayList<>();
		try {
			customerContactsList = customerContactRepository.inActiveListOfCustomerContact(inActive);
		} catch (Exception e) {
			LOGGER.error("Error occur while display inactive list of customer contact " + ExceptionUtils.getStackTrace(e));
		}
		return customerContactsList;
	}

	@Override
	public CustomerContact saveCustomerContact(CustomerContact customerContact) throws Exception {
		try {
			if (Objects.nonNull(customerContact)) {
				customerContactRepository.save(customerContact);
			} else {
				System.out.println("CUSTOMER CONTACT RECORD NOT SAVE DAO ::::::::: ");
			}			
		} catch (Exception e) {
			LOGGER.error("Error occur while save customer contact record " + ExceptionUtils.getStackTrace(e));
		}
		return customerContact;
	}

	@Override
	public CustomerContact activateCustomerContact(String id) throws Exception {
		CustomerContact customerContact = customerContactRepository.getById(id);
		try {
			if (Objects.nonNull(customerContact)) {
				return customerContact;
			} else {
				System.out.println("CUSTOMER CONTACT NOT ACTIVATE DAO ::::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while active customer contact " + ExceptionUtils.getStackTrace(e));
		}
		return new CustomerContact();
	}

	@Override
	public CustomerContact deactivateCustomerContact(String id) throws Exception {
		CustomerContact customerContact = customerContactRepository.getById(id);
		try {
			if (Objects.nonNull(customerContact)) {
				return customerContact;
			} else {
				System.out.println("CUSTOMER CONTACT NOT DEACTIVATE DAO ::::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while deactive customer contact " + ExceptionUtils.getStackTrace(e));
		}
		return new CustomerContact();
	}

	@Override
	public Optional<CustomerContact> getCustomerContactById(String id) throws Exception {
		Optional<CustomerContact> optional = customerContactRepository.findByCustomerId(id);
		try {
			if (optional.isPresent()) {
				return optional;
			} else {
				System.out.println("NOT GET CUSTOMER CONTACT BY ID DAO ::::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get customer contact by id " + ExceptionUtils.getStackTrace(e));
		}
		return Optional.empty();
	}

	@Override
	public CustomerContact getDataByCustomerContact(String id) throws Exception {
		CustomerContact customerContact = customerContactRepository.getByCustomerId(id);
		try {
			if (Objects.nonNull(customerContact)) {
				return customerContact;
			} else {
				System.out.println("CUSTOMER CONTACT DATA GET BY ID DAO::::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get data by id customer contact " + ExceptionUtils.getStackTrace(e));
		}
		return new CustomerContact();
	}

	@Override
	public List<String> getListOfCustomerId() throws Exception {
		List<String> list = customerContactRepository.getListOfCustomerId();
		try {
			if (Objects.nonNull(list)) {
				return list;
			} else {
				System.out.println("LIST OF ID NOT GET DAO ::::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while get list of id's of customer contact " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}
	
	@Override
	public void saveAll(List<CustomerContact> customerContacts) throws Exception {
		try {
			if (Objects.nonNull(customerContacts)) {
				this.customerContactRepository.saveAll(customerContacts);	
			}			
		} catch (Exception e) {
			LOGGER.error("Error occur while save customer contact excel file data "
					+ ExceptionUtils.getStackTrace(e));
		}

	}

	@Override
	public List<CustomerContact> checkDuplicateCustomerContact(String customerId) throws Exception {
		List<CustomerContact> customerContacts = customerContactRepository.checkDuplicateCustomerContact(customerId);
		try {
			if (Objects.nonNull(customerContacts)) {
				return customerContacts;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while check duplicate customer contact code"
					+ ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public boolean checkDuplicateException(String customerId) throws Exception {
		try {
			List<CustomerContact> customerContacts = customerContactRepository.checkDuplicateCustomerContact(customerId);
			if (Objects.nonNull(customerContacts)) {
				return true;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while find duplicate customer contact excel"
					+ ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public List<CustomerContact> findAll() throws Exception {
		try {
			List<CustomerContact> customerContacts = customerContactRepository.findAll();
			if (Objects.nonNull(customerContacts)) {
				return customerContacts;
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while display all customer contact" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public long getStudentsCount(String isActive) throws Exception {
		try {
			long count  = customerContactRepository.getStudentsCount(isActive);
			 if (Objects.nonNull(count)) {
				 return count;	
			}	
		} catch (Exception e) {
			LOGGER.error("Error occur while count record " + ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}
}
