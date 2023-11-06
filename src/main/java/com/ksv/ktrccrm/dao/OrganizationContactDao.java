package com.ksv.ktrccrm.dao;

import java.util.List;
import java.util.Optional;

import com.ksv.ktrccrm.db1.entities.CustomerContact;
import com.ksv.ktrccrm.db1.entities.OrganizationContact;

public interface OrganizationContactDao {

	public List<OrganizationContact> activeListOfOrganizationContact(String isActive) throws Exception;

	public List<OrganizationContact> inActiveListOfOrganizationContact(String inActive) throws Exception;

	public OrganizationContact saveOrganizationContact(OrganizationContact organizationContact) throws Exception;

	public OrganizationContact activateOrganizationContact(String id) throws Exception;

	public OrganizationContact deactivateOrganizationContact(String id) throws Exception;

	public Optional<OrganizationContact> getOrganizationContactById(String id) throws Exception;

	public List<String> getListOfOrgId() throws Exception;

	public OrganizationContact getOrgContactById(String organizationId) throws Exception;

	public void saveAll(List<OrganizationContact> organizationContact) throws Exception;
	
	public List<OrganizationContact> checkDuplicateOrgContact(String mobileNo) throws Exception;

	public boolean checkDuplicateException(String mobileNo) throws Exception;

	public List<OrganizationContact> sendMailToCustomer(String category, String subCategory) throws Exception;

	public long getOrganizationCount(String isActive) throws Exception;

}
