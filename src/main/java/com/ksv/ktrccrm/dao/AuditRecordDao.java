package com.ksv.ktrccrm.dao;

import java.util.Date;
import java.util.List;

import org.springframework.mobile.device.Device;

import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.UserMst;

public interface AuditRecordDao {

	public AuditRecord save(AuditRecord auditRecord, Device device) throws Exception;

	public List<UserMst> getAllUser(String tenantId) throws Exception;

	public List<AuditRecord> findByFromToDateBetweenOrLoginIdOrBranchName(Date from, Date to, String loginId, String branchName) throws Exception;

	public List<AuditRecord> findByFromToDateBetweenOrLoginId(Date from, Date to, String loginId) throws Exception;

	public List<AuditRecord> findByFromToDateBetweenOrRoleCode(Date from, Date to, String roleCode) throws Exception;
}
