package com.ksv.ktrccrm.db1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ksv.ktrccrm.db1.entities.OrganizationContact;
import com.ksv.ktrccrm.db1.entities.OrganizationEmployeeContact;

public interface OrganizationEmployeeContactRepository extends JpaRepository<OrganizationEmployeeContact, String> {

	@Query("SELECT e FROM OrganizationEmployeeContact e WHERE e.isActive = :isActive")
	List<OrganizationEmployeeContact> activeListOfOEC(String isActive);

	@Query("SELECT e FROM OrganizationEmployeeContact e WHERE e.isActive = :inActive")
	List<OrganizationEmployeeContact> inActiveListOfOEC(String inActive);
	
	@Query("SELECT orgEmpContactID FROM OrganizationEmployeeContact")
	public List<String> getListOfOECId();

	@Query("SELECT u FROM OrganizationEmployeeContact u WHERE u.mobileNumber IN :mobileNo")
	public List<OrganizationEmployeeContact> checkDuplicateOrgEmpContactOrgEmpId(String mobileNo);

	@Query("SELECT count(u) FROM OrganizationEmployeeContact u WHERE u.isActive = :isActive")
	long getOrganizationEmployeeCount(String isActive); 
	
	@Query("SELECT e FROM OrganizationEmployeeContact e WHERE orgEmpContactID = :orgEmpContactID")
	OrganizationEmployeeContact getByOrgEmpContactID(String orgEmpContactID);
	
	@Query("SELECT e FROM OrganizationEmployeeContact e WHERE orgEmpContactID = :orgEmpContactID")
	Optional<OrganizationEmployeeContact> findByOrgEmpContactID(String orgEmpContactID);

	@Query("SELECT u FROM OrganizationEmployeeContact u WHERE u.orgEmpContactID = :subCategory")
	public List<OrganizationEmployeeContact> getDataBySubCategory(String subCategory);

}
