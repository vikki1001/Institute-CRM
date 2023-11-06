package com.ksv.ktrccrm.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.UploadDocDao;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.UploadDocument;
import com.ksv.ktrccrm.db1.repository.EmpBasicRepository;
import com.ksv.ktrccrm.db1.repository.UploadDocRepository;

@Repository
public class UploadDocDaoImpl implements UploadDocDao {
	private static final Logger LOGGER = LogManager.getLogger(UploadDocDaoImpl.class);

	@Autowired
	private UploadDocRepository uploadDocRepository;
	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;
	@Autowired
	private EmpBasicRepository empBasicRepository;

	/* Save & Update Upload Document */
	@Override
	public UploadDocument saveUploadDoc(UploadDocument uploadDoc) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
		String loginId = authentication.getName();
		if (Objects.nonNull(loginId)) {
			EmpBasicDetails userDetails = empBasicRepository.getCurrentUser(loginId,ProdConstant.TRUE);
			System.out.println("TenantId:::::::::"+ userDetails.getTenantId());
			String fileType = uploadDoc.getFileType();
			if (uploadDoc.getId() == null) {
				uploadDoc.setIsActive(ProdConstant.TRUE);
				uploadDoc.setCreatedDate(checkOutDaoImpl.getDateTime());
				uploadDoc.setCreatedBy(loginId);
				uploadDoc.setTenantId(userDetails.getTenantId());
				uploadDocRepository.save(uploadDoc);
			} 
			else {
				Optional<UploadDocument> uploadDoc2 = uploadDocRepository.findById(uploadDoc.getId());
				if (uploadDoc2.isPresent()) {
					UploadDocument newUploadDoc = uploadDoc2.get();
					newUploadDoc.setTemplateName(uploadDoc.getTemplateName());
					newUploadDoc.setOriginalName(uploadDoc.getOriginalName());
					newUploadDoc.setFile(uploadDoc.getFile());
					newUploadDoc.setFileType(uploadDoc.getFileType());
					newUploadDoc.setSize(uploadDoc.getSize());
					newUploadDoc.setExtension(uploadDoc.getExtension());
					newUploadDoc.setFileDepartment(uploadDoc.getFileDepartment());
					newUploadDoc.setLastModifiedDate(checkOutDaoImpl.getDateTime());
					newUploadDoc.setLastModifiedBy(loginId);
					uploadDocRepository.save(newUploadDoc);
				} else {
					return uploadDoc;
				}
			}
		}else {
			LOGGER.error("Error occur to get login id");
		}
		} catch (Exception e) {
			LOGGER.error("Error occur while save & update upload doc " + ExceptionUtils.getStackTrace(e));
		}
		return uploadDoc;
	}

	/* Find Doc By Id */
	@Override
	public UploadDocument findDocById(Long id) throws Exception {
		UploadDocument document = null;
		try {
			document = uploadDocRepository.getById(id);
		} catch (Exception e) {
			LOGGER.error("Error occur while find document by id " + ExceptionUtils.getStackTrace(e));
		}
		return document;
	}

	/* List Of Active Document */
	@Override
	public List<UploadDocument> getRecordList(String isActive, String tenantId) throws Exception {
		List<UploadDocument> uploadDocList = uploadDocRepository.getRecordList(isActive, tenantId);
		try {
			if (Objects.nonNull(uploadDocList)) {
				return uploadDocList;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display upload document list " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	/* List Of Employee Department Document */
	@Override
	public List<UploadDocument> getEmpDepartment(String fileDepartment, String isActive, String tenantId) throws Exception {
		List<UploadDocument> uploadDocList = uploadDocRepository.getEmpDepartment(fileDepartment, isActive,tenantId);
		try {
			if (Objects.nonNull(uploadDocList)) {
				return uploadDocList;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display employee department upload document list "
					+ ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	/* List Of Organization Department Document */
	@Override
	public List<UploadDocument> getOrganizationDepart(String fileDepartment, String isActive, String tenantId) throws Exception {
		List<UploadDocument> uploadDocList = uploadDocRepository.getOrganizationDepart(fileDepartment, isActive, tenantId);
		try {
			if (Objects.nonNull(uploadDocList)) {
				return uploadDocList;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display organization department upload document list"
					+ ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}
}
