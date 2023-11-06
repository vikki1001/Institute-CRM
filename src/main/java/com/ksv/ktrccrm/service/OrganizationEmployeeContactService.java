package com.ksv.ktrccrm.service;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ksv.ktrccrm.db1.entities.OrganizationEmployeeContact;

public interface OrganizationEmployeeContactService {

	public List<OrganizationEmployeeContact> activeListOfOEC(String isActive) throws Exception;

	public List<OrganizationEmployeeContact> inActiveListOfOEC(String inActive) throws Exception;

	public OrganizationEmployeeContact saveOEC(OrganizationEmployeeContact oec) throws Exception;

	public OrganizationEmployeeContact activateOEC(String orgEmpContactID) throws Exception;

	public OrganizationEmployeeContact deactivateOEC(String orgEmpContactID) throws Exception;

	public OrganizationEmployeeContact getOECByOrgEmpContactID(String orgEmpContactID) throws Exception;

	public void saveAll(MultipartFile files) throws Exception;
	
	public List<OrganizationEmployeeContact> checkDuplicateOEC(String mobileNo) throws Exception;

	public boolean checkDuplicateException(String orgEmpContactID) throws Exception;

	public List<OrganizationEmployeeContact> findAll() throws Exception;

	public ByteArrayInputStream exportOECExcel(List<OrganizationEmployeeContact> oec) throws Exception;

	public long getOrganizationEmployeeCount(String isActive) throws Exception;

}
