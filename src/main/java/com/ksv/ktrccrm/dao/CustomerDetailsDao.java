package com.ksv.ktrccrm.dao;

import java.util.List;
import java.util.Optional;

import com.ksv.ktrccrm.db1.entities.CustomerDetails;

public interface CustomerDetailsDao {

	List<CustomerDetails> activeCustomerList(Integer isactive) throws Exception;

	List<CustomerDetails> inActiveCustomerList(Integer inactive) throws Exception;

	CustomerDetails saveCustomer(CustomerDetails customerDetails) throws Exception;

	Optional<CustomerDetails> findCustomerById(Long id) throws Exception;

	CustomerDetails activeCustomer(Long id) throws Exception;

	CustomerDetails deactiveCustomer(Long id) throws Exception;
}
