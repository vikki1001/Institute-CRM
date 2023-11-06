package com.ksv.ktrccrm.dao;

import java.util.List;
import java.util.Optional;

import com.ksv.ktrccrm.db1.entities.OrganizationEmployeeContact;

public interface OrganizationEmployeeContactDao {
	
	public List<OrganizationEmployeeContact> activeListOfOEC(String isActive) throws Exception;

	public List<OrganizationEmployeeContact> inActiveListOfOEC(String inActive) throws Exception;

	public OrganizationEmployeeContact saveOEC(OrganizationEmployeeContact oec) throws Exception;

	public OrganizationEmployeeContact activateOEC(String orgEmpContactID) throws Exception;

	public OrganizationEmployeeContact deactivateOEC(String orgEmpContactID) throws Exception;
	
	public Optional<OrganizationEmployeeContact> getOECById(String id) throws Exception;
	
	public List<String> getListOfOECId() throws Exception;

	public OrganizationEmployeeContact getOECByOrgEmpContactID(String orgEmpContactID) throws Exception;
	
	public void saveAll(List<OrganizationEmployeeContact> organizationContact) throws Exception;
	
	public List<OrganizationEmployeeContact> checkDuplicateOEC(String mobileNo) throws Exception;

	public boolean checkDuplicateException(String orgEmpContactID) throws Exception;
	
	public List<OrganizationEmployeeContact> findAll() throws Exception;

	public long getOrganizationEmployeeCount(String isActive) throws Exception;

}
