package com.ksv.ktrccrm.db1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ksv.ktrccrm.db1.entities.CustomerContact;
import com.ksv.ktrccrm.db1.entities.OrganizationContact;

public interface OrganizationContactRepository extends JpaRepository<OrganizationContact, String> {

	@Query("SELECT e FROM OrganizationContact e WHERE e.isActive = :isActive")
	public List<OrganizationContact> activeListOfOrganizationContact(String isActive);

	@Query("SELECT e FROM OrganizationContact e WHERE e.isActive = :inActive")
	public List<OrganizationContact> inActiveListOfOrganizationContact(String inActive);

	@Query("SELECT organizationId FROM OrganizationContact")
	public List<String> getListOfOrgId();

	@Query("SELECT u FROM OrganizationContact u WHERE u.mobileNumber IN :mobileNo")
	public List<OrganizationContact> checkDuplicateOrgContactMobileNo(String mobileNo);

	//@Query("SELECT u FROM OrganizationContact u WHERE u.organizationId = :subCategory OR u.customerContact.customerId = :subCategory OR u.customerContact.branch = :subCategory OR u.organizationEmployeeContact.orgEmpContactID = :subCategory")
	@Query("SELECT u FROM OrganizationContact u WHERE u.organizationId = :subCategory")
	public List<OrganizationContact> getDataBySubCategory(String subCategory);

	@Query("SELECT count(u) FROM OrganizationContact u WHERE u.isActive = :isActive")
	public long getOrganizationCount(String isActive);
	
	@Query("SELECT e FROM OrganizationContact e WHERE e.organizationId = :organizationId")
	public OrganizationContact getByOrganizationId(String organizationId);
	
	@Query("SELECT e FROM OrganizationContact e WHERE e.organizationId = :organizationId")
	Optional<OrganizationContact> findByOrganizationId(String organizationId);

}
