package com.ksv.ktrccrm.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.UploadDocDao;
import com.ksv.ktrccrm.db1.entities.UploadDocument;
import com.ksv.ktrccrm.service.UploadDocService;

@Service
public class UploadDocServiceImpl implements UploadDocService {
	private static final Logger LOGGER = LogManager.getLogger(UploadDocServiceImpl.class);

	@Autowired
	private UploadDocDao uploadDocDao;

	/* Save & Update Upload Document */
	@Override
	public UploadDocument saveUploadDoc(UploadDocument uploadDoc) throws Exception {
		try {
			if (Objects.nonNull(uploadDoc)) {
				uploadDocDao.saveUploadDoc(uploadDoc);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while save & update upload doc " + ExceptionUtils.getStackTrace(e));
		}
		return uploadDoc;
	}

	/* Find Doc By Id */
	@Override
	public UploadDocument findDocById(Long id) throws Exception {
		UploadDocument document = uploadDocDao.findDocById(id);
		try {
			if (Objects.nonNull(document)) {
				return document;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while find document by id " + ExceptionUtils.getStackTrace(e));
		}
		return new UploadDocument();
	}

	/* List Of IsActive Document */
	@Override
	public List<UploadDocument> getRecordList(String isActive, String tenantId) throws Exception {
		List<UploadDocument> uploadDocument = uploadDocDao.getRecordList(isActive, tenantId);
		try {
			if (Objects.nonNull(uploadDocument)) {
				return uploadDocument;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display upload documnet list " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	/* List Of Employee Department Document */
	@Override
	public List<UploadDocument> getEmpDepartment(String fileDepartment, String isActive, String tenantId) throws Exception {
		List<UploadDocument> uploadDocList = uploadDocDao.getEmpDepartment(fileDepartment, isActive, tenantId);
		try {
			if (Objects.nonNull(uploadDocList)) {
				return uploadDocList;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display upload employee department documnet list"
					+ ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	/* List Of Organization Department Document */
	@Override
	public List<UploadDocument> getOrganizationDepart(String fileDepartment, String isActive, String tenantId) throws Exception {
		List<UploadDocument> uploadDocList = uploadDocDao.getOrganizationDepart(fileDepartment, isActive, tenantId);
		try {
			if (Objects.nonNull(uploadDocList)) {
				return uploadDocList;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display upload organization department documnet list "
					+ ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	public UploadDocument docEnableAndDisable(Long id) {
		try {
			UploadDocument docDetails = uploadDocDao.findDocById(id);
			if (Objects.nonNull(docDetails)) {
				String checkActiveOrInActiveData = docDetails.getIsActive();

				if (checkActiveOrInActiveData.equals(ProdConstant.TRUE)) {
					docDetails.setIsActive(ProdConstant.FALSE);
					uploadDocDao.saveUploadDoc(docDetails);
				} else if (checkActiveOrInActiveData.equals(ProdConstant.FALSE)) {
					docDetails.setIsActive(ProdConstant.TRUE);
					uploadDocDao.saveUploadDoc(docDetails);
				}
			} else {
				System.out.println("docDetails object is null");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while activate and deactivate doc  " + ExceptionUtils.getStackTrace(e));
		}

		return new UploadDocument();

	}
}
