package com.ksv.ktrccrm.dao.impl;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.AuditRecordDao;
import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.db1.repository.AuditRecordRepository;
import com.ksv.ktrccrm.db1.repository.UserRepository;

@Repository
public class AuditRecordDaoImpl implements AuditRecordDao {
	private static final Logger LOGGER = LogManager.getLogger(AuditRecordDaoImpl.class);

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuditRecordRepository auditRecordRepository;
	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;

	
	@Override
	public AuditRecord save(AuditRecord auditRecord, Device device) throws Exception {
		String userId = null;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			userId = authentication.getName();
			UserMst userDetail = null;
				userDetail = userRepository.getUserDetails(userId,ProdConstant.ISACTIVE);
				if (userDetail != null) {
					/* Save current user details to AuditRecord */
					auditRecord.setLoginId(userDetail.getLoginId());
					auditRecord.setTenantId(userDetail.getTenantId());
					auditRecord.setBranchCode(userDetail.getBaseBranch());
					auditRecord.setRoleCode(userDetail.getMainRole());
					
					/* Save device ActionFrom to AuditRecord */
					String Devicetype = "";
					if (device.isNormal()) {
						Devicetype = ProdConstant.BROWSER;
					} else if (device.isMobile()) {
						Devicetype = ProdConstant.MOBILE;
					} else if (device.isTablet()) {
						Devicetype = ProdConstant.TABLET;
					}
					auditRecord.setActionFrom(auditRecord.getActionFrom());
					auditRecord.setActionFrom(Devicetype);
					
					/* Save IPaddress and HostName to Database */
					InetAddress inetAddress = InetAddress.getLocalHost();
					auditRecord.setIpAddr(inetAddress.getHostAddress());
					auditRecord.setHostName(inetAddress.getHostName());
					/* Save AuditRecord to Database */
					
					auditRecord.setCreatedDate(checkOutDaoImpl.getDateTime());
					auditRecord.setCreatedBy(userId);
					auditRecord.setAuditDateTime(checkOutDaoImpl.getDateTime());
				}		
				else {
					auditRecord.setLastModifiedBy(userId);
					auditRecord.setModifiedDateTime(checkOutDaoImpl.getDateTime());
				}
				auditRecordRepository.save(auditRecord);
		} catch (Exception e) {
			LOGGER.error("Error occur while save & update AuditRecord " + ExceptionUtils.getStackTrace(e));
		}
		return auditRecord;
	}

	/* Dropdown userlist in Audit */
	@Override
	public List<UserMst> getAllUser(String tenantId) throws Exception {
		List<UserMst> user = userRepository.getRecordList(ProdConstant.ISACTIVE,tenantId);
		try {
			if (Objects.nonNull(user)) {
				return user;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display dropdown userlist in Audit  " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	/* Search data using FromToDate, toDate, loginId & branchName */
	@Override
	public List<AuditRecord> findByFromToDateBetweenOrLoginIdOrBranchName(Date from, Date to, String loginId,
			String branchName) throws Exception {
		List<AuditRecord> auditRecord = new ArrayList<>();
		try {
			if (from != null && to != null && ProdConstant.ALL.equalsIgnoreCase(loginId) && ProdConstant.ALL.equalsIgnoreCase(branchName)) {
				auditRecord = auditRecordRepository.findAll(from, to);
			} else if (from != null && to != null && !loginId.isEmpty() && ProdConstant.ALL.equalsIgnoreCase(branchName)) {
				auditRecord = auditRecordRepository.findByFromToDateBetweenOrLoginId(from, to, loginId);
			} else if (from != null && to != null && ProdConstant.ALL.equalsIgnoreCase(loginId) && !branchName.isEmpty()) {
				auditRecord = auditRecordRepository.findByFromToDateBetweenOrBranchCode(from, to, branchName);
			} else if (from != null && to != null && !loginId.isEmpty() && !branchName.isEmpty()) {
				auditRecord = auditRecordRepository.findByFromToDateBetweenOrLoginIdOrBranchName(from, to, loginId, branchName);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while search data using FromToDate, toDate, loginId & branchName " + ExceptionUtils.getStackTrace(e));
		}
		return auditRecord;
	}

	/* Search data using FromToDate, toDate & loginId */
	@Override
	public List<AuditRecord> findByFromToDateBetweenOrLoginId(Date from, Date to, String loginId) throws Exception {
		List<AuditRecord> auditRecord = new ArrayList<>();
		try {
			if (ProdConstant.ALL.equalsIgnoreCase(loginId)) {
				auditRecord = auditRecordRepository.findAll(from, to);
			} else if (from != null && to != null && !loginId.isEmpty()) {
				auditRecord = auditRecordRepository.findByFromToDateBetweenOrLoginId(from, to, loginId);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while search data using FromToDate, toDate & loginId  " + ExceptionUtils.getStackTrace(e));
		}
		return auditRecord;
	}

	/* Search data using FromToDate, toDate & roleCode */
	@Override
	public List<AuditRecord> findByFromToDateBetweenOrRoleCode(Date from, Date to, String roleCode) throws Exception {
		List<AuditRecord> auditRecord = new ArrayList<>();
		try {
			if (ProdConstant.ALL.equalsIgnoreCase(roleCode)) {
				auditRecord = auditRecordRepository.findAll(from, to);
			} else if (from != null && to != null && !roleCode.isEmpty()) {
				auditRecord = auditRecordRepository.findByFromToDateBetweenOrRoleCode(from, to, roleCode);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while search data using FromToDate, toDate & roleCode " + ExceptionUtils.getStackTrace(e));
		}
		return auditRecord;
	}
}
