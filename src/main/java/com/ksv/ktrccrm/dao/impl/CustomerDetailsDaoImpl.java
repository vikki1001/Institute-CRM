package com.ksv.ktrccrm.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.dao.CustomerDetailsDao;
import com.ksv.ktrccrm.db1.entities.CustomerDetails;
import com.ksv.ktrccrm.db1.repository.CustomerDetailsRepository;

@Repository
public class CustomerDetailsDaoImpl implements CustomerDetailsDao {
	private static final Logger LOGGER = LogManager.getLogger(CustomerDetailsDaoImpl.class);

	@Autowired
	private CustomerDetailsRepository customerDetailsRepository;

	@Override
	public List<CustomerDetails> activeCustomerList(Integer isactive) throws Exception {
		List<CustomerDetails> customerDetails = customerDetailsRepository.activeCustomerList(isactive);
		try {
			if (customerDetails != null && !customerDetails.isEmpty()) {
				return customerDetails;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display active customer list... " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<CustomerDetails> inActiveCustomerList(Integer inactive) throws Exception {
		List<CustomerDetails> customerDetails = customerDetailsRepository.inActiveCustomerList(inactive);
		try {
			if (customerDetails != null && !customerDetails.isEmpty()) {
				return customerDetails;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display inactive customer list... " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public CustomerDetails saveCustomer(CustomerDetails customerDetails) throws Exception {
		try {
			if (customerDetails != null) {				
				customerDetailsRepository.save(customerDetails);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while insert customer record ... " + ExceptionUtils.getStackTrace(e));
		}
		return customerDetails;
	}

	@Override
	public Optional<CustomerDetails> findCustomerById(Long id) throws Exception {
		Optional<CustomerDetails> optional = customerDetailsRepository.findById(id);
		try {
			if (optional.isPresent()) {
				return optional;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get customer record by id ... " + ExceptionUtils.getStackTrace(e));
		}
		return Optional.empty();
	}

	@Override
	public CustomerDetails activeCustomer(Long id) throws Exception {
		try {
			customerDetailsRepository.findById(id);
		} catch (Exception e) {
			LOGGER.error("Error occur while active user " + ExceptionUtils.getStackTrace(e));
		}
		return new CustomerDetails();
	}

	@Override
	public CustomerDetails deactiveCustomer(Long id) throws Exception {
		try {
			customerDetailsRepository.findById(id);
		} catch (Exception e) {
			LOGGER.error("Error occur while inactive user " + ExceptionUtils.getStackTrace(e));
		}
		return new CustomerDetails();
	}
	
	
}
