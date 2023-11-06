package com.ksv.ktrccrm.db1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ksv.ktrccrm.db1.entities.CustomerDetails;

public interface CustomerDetailsRepository extends JpaRepository<CustomerDetails, Long> {

	@Query("SELECT u FROM CustomerDetails u WHERE u.isActive = :isactive")
	public List<CustomerDetails> activeCustomerList(Integer isactive);

	@Query("SELECT u FROM CustomerDetails u WHERE u.isActive = :inactive")
	public List<CustomerDetails> inActiveCustomerList(Integer inactive);

}
