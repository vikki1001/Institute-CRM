package com.ksv.ktrccrm.service.Impl;

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
import org.springframework.stereotype.Service;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.EmpRegistartionDao;
import com.ksv.ktrccrm.dao.PasswordPolicyDao;
import com.ksv.ktrccrm.dao.impl.CheckInCheckOutDaoImpl;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.PasswordPolicy;
import com.ksv.ktrccrm.service.PasswordPolicyService;

@Service
public class PasswordPolicyServiceImpl implements PasswordPolicyService {
	private static final Logger LOGGER = LogManager.getLogger(PasswordPolicyServiceImpl.class);

	@Autowired
	private PasswordPolicyDao passwordPolicyDao;
	@Autowired
	private EmpRegistartionDao empRegistartionDao;
	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;

	@Override
	public List<PasswordPolicy> findAll() throws Exception {
		List<PasswordPolicy> passwordPolicies = passwordPolicyDao.findAll();
		try {
			if (!passwordPolicies.isEmpty()) {
				return passwordPolicies;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display records " + ExceptionUtils.getStackTrace(e));
		}
		return passwordPolicies;
	}

	@Override
	public Optional<PasswordPolicy> findById(Long id) throws Exception {
		Optional<PasswordPolicy> passwordPolicies = passwordPolicyDao.findById(id);
		try {
			if (passwordPolicies.isPresent()) {
				return passwordPolicies;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while find record by id " + ExceptionUtils.getStackTrace(e));
		}
		return passwordPolicies;
	}

	@Override
	public PasswordPolicy savePasswordPolicy(PasswordPolicy passwordPolicy) throws Exception {
		String loginId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			loginId = authentication.getName();
			EmpBasicDetails empBasicDetails = empRegistartionDao.getEmpById(loginId);
			if (Objects.isNull(passwordPolicy.getId())) {
				passwordPolicy.setMinimumPasswordLength(passwordPolicy.getMinimumPasswordLength());
				passwordPolicy.setMaximumPasswordLength(passwordPolicy.getMaximumPasswordLength());
				passwordPolicy.setMinimumAlphabetsLength(passwordPolicy.getMinimumAlphabetsLength());
				passwordPolicy.setSameConsCharNotAllowedCount(passwordPolicy.getSameConsCharNotAllowedCount());
				passwordPolicy.setMinimumDigits(passwordPolicy.getMinimumDigits());
				passwordPolicy.setMinimumSpecialCharacter(passwordPolicy.getMinimumSpecialCharacter());
				passwordPolicy.setPasswordValidateDays(passwordPolicy.getPasswordValidateDays());
				passwordPolicy.setLastPasswordNotAllowedCount(passwordPolicy.getLastPasswordNotAllowedCount());
				passwordPolicy.setMaximumBadLoginCount(passwordPolicy.getMaximumBadLoginCount());
				passwordPolicy.setIsUserIdAllowed(passwordPolicy.getIsUserIdAllowed());
				passwordPolicy.setCreatedBy(loginId);
				passwordPolicy.setCreatedDate(checkOutDaoImpl.getDateTime());
				passwordPolicy.setIsActive(ProdConstant.TRUE);
				passwordPolicy.setTenantId(empBasicDetails.getTenantId());
				passwordPolicyDao.savePasswordPolicy(passwordPolicy);
			} else {
				Optional<PasswordPolicy> optional = passwordPolicyDao.findById(passwordPolicy.getId());
				if (optional.isPresent()) {
					PasswordPolicy newPasswordPolicy = optional.get();
					newPasswordPolicy.setMinimumPasswordLength(passwordPolicy.getMinimumPasswordLength());
					newPasswordPolicy.setMaximumPasswordLength(passwordPolicy.getMaximumPasswordLength());
					newPasswordPolicy.setMinimumAlphabetsLength(passwordPolicy.getMinimumAlphabetsLength());
					newPasswordPolicy.setSameConsCharNotAllowedCount(passwordPolicy.getSameConsCharNotAllowedCount());
					newPasswordPolicy.setMinimumDigits(passwordPolicy.getMinimumDigits());
					newPasswordPolicy.setMinimumSpecialCharacter(passwordPolicy.getMinimumSpecialCharacter());
					newPasswordPolicy.setPasswordValidateDays(passwordPolicy.getPasswordValidateDays());
					newPasswordPolicy.setLastPasswordNotAllowedCount(passwordPolicy.getLastPasswordNotAllowedCount());
					newPasswordPolicy.setMaximumBadLoginCount(passwordPolicy.getMaximumBadLoginCount());
					newPasswordPolicy.setIsUserIdAllowed(passwordPolicy.getIsUserIdAllowed());
					newPasswordPolicy.setLastModifiedBy(loginId);
					newPasswordPolicy.setLastModifiedDate(checkOutDaoImpl.getDateTime());
					newPasswordPolicy.setIsActive(ProdConstant.TRUE);
					passwordPolicyDao.savePasswordPolicy(newPasswordPolicy);
				} else {

				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while save & update password policy record " + ExceptionUtils.getStackTrace(e));
		}
		return passwordPolicy;
	}

@Override
	public PasswordPolicy getDataBytenantId(String tenantId) throws Exception {
	PasswordPolicy passwordPolicy = new PasswordPolicy();
		try {
			passwordPolicy = passwordPolicyDao.getDataBytenantId(tenantId);
			if(Objects.nonNull(passwordPolicy)) {
				return passwordPolicy;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get data by tenant id" + ExceptionUtils.getStackTrace(e));
		}
		return new PasswordPolicy();
	}

@Override
public PasswordPolicy findByTenantIdAndIsactive(String tenantId, String isActive) {
	PasswordPolicy passwordPolicy = new PasswordPolicy();
	try {
		passwordPolicy = passwordPolicyDao.findByTenantIdAndIsactive(tenantId, isActive);
		if(Objects.nonNull(passwordPolicy)) {
			return passwordPolicy;
		}
	} catch (Exception e) {
		LOGGER.error("Error occur while get tenantId and isactive" + ExceptionUtils.getStackTrace(e));
	}
	return new PasswordPolicy();
}

@Override
public List<PasswordPolicy> getRecordList(String isActive, String tenantId) {
	List<PasswordPolicy> passwordPolicy = new ArrayList<PasswordPolicy>();
	try {
		passwordPolicy = passwordPolicyDao.getRecordList(isActive, tenantId);
		if(Objects.nonNull(passwordPolicy)) {
			return passwordPolicy;
		}
	} catch (Exception e) {
		LOGGER.error("Error occur while get tenantId and isactive" + ExceptionUtils.getStackTrace(e));
	}
	return passwordPolicy;
}

}
