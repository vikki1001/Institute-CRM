package com.ksv.ktrccrm.service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.ksv.ktrccrm.db1.entities.OrganizationContact;

public interface OrganizationContactService {
	
	public List<OrganizationContact> activeListOfOrganizationContact(String isActive) throws Exception;

	public List<OrganizationContact> inActiveListOfOrganizationContact(String inActive) throws Exception;

	public OrganizationContact saveOrganizationContact(OrganizationContact organizationContact) throws Exception;

	public OrganizationContact activateOrganizationContact(String id) throws Exception;

	public OrganizationContact deactivateOrganizationContact(String id) throws Exception;

	public OrganizationContact getOrgContactById(String organizationId) throws Exception;

	public void saveAll(MultipartFile files)  throws Exception;
	
	public List<OrganizationContact> checkDuplicateOrgContact(String organizationId) throws Exception;

	public boolean checkDuplicateException(String organizationId) throws Exception;
	
	public ByteArrayInputStream exportOrgContact(List<OrganizationContact> organizationContacts) throws Exception;

	public List<OrganizationContact> sendMailToCustomer(String category, String subCategory) throws Exception;

	public long getOrganizationCount(String isActive) throws Exception;
}
