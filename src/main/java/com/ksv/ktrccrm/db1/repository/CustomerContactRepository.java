package com.ksv.ktrccrm.db1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ksv.ktrccrm.db1.entities.CustomerContact;
import com.ksv.ktrccrm.db1.entities.OrganizationEmployeeContact;

public interface CustomerContactRepository extends JpaRepository<CustomerContact, String> {

	@Query("SELECT e FROM CustomerContact e WHERE e.isActive = :isActive")
	List<CustomerContact> activeListOfCustomerContact(String isActive);

	@Query("SELECT e FROM CustomerContact e WHERE e.isActive = :inActive")
	List<CustomerContact> inActiveListOfCustomerContact(String inActive);

	@Query("SELECT customerId FROM CustomerContact")
	List<String> getListOfCustomerId();

	@Query("SELECT u FROM CustomerContact u WHERE u.customerId IN :customerId")
	List<CustomerContact> checkDuplicateCustomerContact(String customerId);

	@Query("SELECT count(u) FROM CustomerContact u WHERE u.isActive = :isActive")	
	public long getStudentsCount(String isActive);

	@Query("SELECT u FROM CustomerContact u WHERE u.customerId = :customerId")
	CustomerContact getByCustomerId(String customerId);
	
	@Query("SELECT u FROM CustomerContact u WHERE u.customerId = :customerId")
	Optional<CustomerContact> findByCustomerId(String customerId);
	
	@Query("SELECT u FROM CustomerContact u WHERE u.customerId = :subCategory")
	public List<CustomerContact> getDataBySubCategory(String subCategory);


}
