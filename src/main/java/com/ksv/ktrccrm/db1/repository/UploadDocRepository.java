package com.ksv.ktrccrm.db1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ksv.ktrccrm.db1.entities.UploadDocument;

public interface UploadDocRepository extends JpaRepository<UploadDocument, Long> {

	@Query("SELECT u FROM UploadDocument u WHERE u.id = :id")
	public UploadDocument getId(String id);

	@Query("SELECT u FROM UploadDocument u WHERE u.isActive = :isActive AND u.tenantId = :tenantId")
	public List<UploadDocument> getRecordList(String isActive, String tenantId);

	@Query("SELECT u FROM UploadDocument u WHERE u.fileDepartment = :fileDepartment AND u.isActive = :isActive AND u.tenantId=:tenantId ORDER BY createdDate DESC")
	public List<UploadDocument> getEmpDepartment(String fileDepartment, String isActive, String tenantId);

	@Query("SELECT u FROM UploadDocument u WHERE u.fileDepartment = :fileDepartment AND u.isActive = :isActive AND u.tenantId=:tenantId ORDER BY createdDate DESC")
	public List<UploadDocument> getOrganizationDepart(String fileDepartment, String isActive, String tenantId);

	@Query("SELECT u FROM UploadDocument u WHERE u.id =:id")
	public UploadDocument getDataById(Long id);

}
