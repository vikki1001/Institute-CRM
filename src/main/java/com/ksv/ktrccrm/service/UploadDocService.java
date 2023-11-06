package com.ksv.ktrccrm.service;

import java.util.List;

import com.ksv.ktrccrm.db1.entities.UploadDocument;

public interface UploadDocService {

	public UploadDocument saveUploadDoc(UploadDocument uploadDoc) throws Exception;

	public UploadDocument findDocById(Long id) throws Exception;

	public List<UploadDocument> getRecordList(String isActive, String tenantId) throws Exception;

	public List<UploadDocument> getEmpDepartment(String fileDepartment, String isActive, String tenantId) throws Exception;

	public List<UploadDocument> getOrganizationDepart(String fileDepartment, String isActive, String tenantId) throws Exception;

	public UploadDocument docEnableAndDisable(Long id) throws Exception;  
}
