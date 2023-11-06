package com.ksv.ktrccrm.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.CustomerDetailsDao;
import com.ksv.ktrccrm.dao.impl.CheckInCheckOutDaoImpl;
import com.ksv.ktrccrm.db1.entities.CustomerDetails;
import com.ksv.ktrccrm.service.CustomerDetailsService;

@Service
public class CustomerDetailsServiceImpl implements CustomerDetailsService {
	private static final Logger LOGGER = LogManager.getLogger(CustomerDetailsServiceImpl.class);

	@Autowired
	private CustomerDetailsDao customerDetailsDao;
	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;

	@Override
	public List<CustomerDetails> activeCustomerList(Integer isactive) throws Exception {
		List<CustomerDetails> customerDetails = customerDetailsDao.activeCustomerList(isactive);
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
		List<CustomerDetails> customerDetails = customerDetailsDao.inActiveCustomerList(inactive);
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
		String loginId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			loginId = authentication.getName();
			Optional<CustomerDetails> customerDetails2 = customerDetailsDao.findCustomerById(customerDetails.getId());
			if (customerDetails2.isPresent()) {
				CustomerDetails newCustomerDetails = customerDetails2.get();
				newCustomerDetails.setEmailId(customerDetails.getEmailId());
				newCustomerDetails.setMobileNo(customerDetails.getMobileNo());
				newCustomerDetails.setDob(customerDetails.getDob());
				newCustomerDetails.setTenantId(customerDetails.getTenantId());
				newCustomerDetails.setMaritalStatus(customerDetails.getMaritalStatus());
				newCustomerDetails.setAnniversaryDate(customerDetails.getAnniversaryDate());
				newCustomerDetails.setFirstName(customerDetails.getFirstName());
				newCustomerDetails.setMiddleName(customerDetails.getMiddleName());
				newCustomerDetails.setLastName(customerDetails.getLastName());
				newCustomerDetails.setAddress1(customerDetails.getAddress1());
				newCustomerDetails.setAddress2(customerDetails.getAddress2());
				newCustomerDetails.setAddress3(customerDetails.getAddress3());

				newCustomerDetails.setLastmodifiedBy(loginId);
				newCustomerDetails.setLastmodifiedDate(checkOutDaoImpl.getDateTime());
				customerDetailsDao.saveCustomer(newCustomerDetails);
			} else {
				customerDetails.setCreatedBy(loginId);
				customerDetails.setCreatedDate(checkOutDaoImpl.getDateTime());
				customerDetailsDao.saveCustomer(customerDetails);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while insert customer record ... " + ExceptionUtils.getStackTrace(e));
		}
		return customerDetails;
	}

	@Override
	public Optional<CustomerDetails> findCustomerById(Long id) throws Exception {
		Optional<CustomerDetails> optional = customerDetailsDao.findCustomerById(id);
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
			Optional<CustomerDetails> customerDetails = customerDetailsDao.findCustomerById(id);
			if (customerDetails.isPresent()) {
				CustomerDetails newCustomerDetails = customerDetails.get();
				newCustomerDetails.setIsActive(ProdConstant.ISACTIVE);
				customerDetailsDao.saveCustomer(newCustomerDetails);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while active user " + ExceptionUtils.getStackTrace(e));
		}
		return new CustomerDetails();
	}

	@Override
	public CustomerDetails deactiveCustomer(Long id) throws Exception {
		try {
			Optional<CustomerDetails> customerDetails = customerDetailsDao.findCustomerById(id);
			if (customerDetails.isPresent()) {
				CustomerDetails newCustomerDetails = customerDetails.get();
				newCustomerDetails.setIsActive(ProdConstant.INACTIVE);
				customerDetailsDao.saveCustomer(newCustomerDetails);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while inactive user " + ExceptionUtils.getStackTrace(e));
		}
		return new CustomerDetails();
	}
}
