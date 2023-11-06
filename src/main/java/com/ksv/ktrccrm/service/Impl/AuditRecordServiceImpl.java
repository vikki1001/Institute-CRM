package com.ksv.ktrccrm.service.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Service;

import com.ksv.ktrccrm.dao.AuditRecordDao;
import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.AuditRecordService;

@Service
public class AuditRecordServiceImpl implements AuditRecordService {
	private static final Logger LOGGER = LogManager.getLogger(AuditRecordServiceImpl.class);

	@Autowired
	private AuditRecordDao auditRecordDao;

	@Override
	public AuditRecord save(AuditRecord auditRecord, Device device) throws Exception {
		try {
			if (Objects.nonNull(auditRecord)) {
				this.auditRecordDao.save(auditRecord, device);				
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while save & update audit record " + ExceptionUtils.getStackTrace(e));
		}
		return auditRecord;
	}

	@Override
	public List<UserMst> getAllUser(String tenantId) throws Exception {
		List<UserMst> user = auditRecordDao.getAllUser(tenantId);
		try {
			if (Objects.nonNull(user)) {
				return user;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display Audit Record list " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<AuditRecord> findByFromToDateBetweenOrLoginIdOrBranchName(Date from, Date to, String loginId,
			String branchName) throws Exception {
		List<AuditRecord> auditRecord = auditRecordDao.findByFromToDateBetweenOrLoginIdOrBranchName(from, to, loginId, branchName);
		try {
			if (Objects.nonNull(auditRecord)) {
				return auditRecord;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while find audit record for given id " + ExceptionUtils.getStackTrace(e));
		}
		return auditRecord;
	}

	@Override
	public List<AuditRecord> findByFromToDateBetweenOrLoginId(Date from, Date to, String loginId) throws Exception {
		List<AuditRecord> auditRecord = auditRecordDao.findByFromToDateBetweenOrLoginId(from, to, loginId);
		try {
			if (Objects.nonNull(auditRecord)) {
				return auditRecord;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while find audit record for given id " + ExceptionUtils.getStackTrace(e));
		}
		return auditRecord;
	}

	@Override
	public List<AuditRecord> findByFromToDateBetweenOrRoleCode(Date from, Date to, String roleCode) throws Exception {
		List<AuditRecord> auditRecord = auditRecordDao.findByFromToDateBetweenOrRoleCode(from, to, roleCode);
		try {
			if (Objects.nonNull(auditRecord)) {
				return auditRecord;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while find audit record for given roleCode " + ExceptionUtils.getStackTrace(e));
		}
		return auditRecord;
	}

}
