package com.ksv.ktrccrm.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.PasswordPolicyDao;
import com.ksv.ktrccrm.db1.entities.PasswordPolicy;
import com.ksv.ktrccrm.db1.repository.PasswordPolicyRepository;

@Repository
public class PasswordPolicyDaoImpl implements PasswordPolicyDao {
	private static final Logger LOGGER = LogManager.getLogger(PasswordPolicyDaoImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private PasswordPolicyRepository passwordPolicyRepository;

	@Override
	public List<PasswordPolicy> findAll() throws Exception {
		List<PasswordPolicy> passwordPolicies = passwordPolicyRepository.findAll();
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
		Optional<PasswordPolicy> passwordPolicies = passwordPolicyRepository.findById(id);
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
	public PasswordPolicy savePasswordPolicy(PasswordPolicy newPasswordPolicy) throws Exception {
		PasswordPolicy passwordPolicy = passwordPolicyRepository.save(newPasswordPolicy);
		try {
			if (Objects.nonNull(passwordPolicy)) {
				return passwordPolicy;
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
			passwordPolicy = passwordPolicyRepository.getDataBytenantId(ProdConstant.TRUE,tenantId);
			if(Objects.nonNull(passwordPolicy)) {
				System.out.println("Password policy daoImpl :"+ passwordPolicy.toString());
				return passwordPolicy;
			}
		} catch (Exception e) {
			LOGGER.error("------ Error occur while get data by tenant id ------" + ExceptionUtils.getStackTrace(e));
		}
		return new PasswordPolicy();
	}

	@Override
	public PasswordPolicy findByTenantIdAndIsactive(String tenantId, String isActive) {
	//	PasswordPolicy passwordPolicy = new PasswordPolicy();
		try {
			System.out.println("TENANT ID & ISACTIVE :::: " + tenantId + " , " + isActive);
			PasswordPolicy passwordPolicy = entityManager
					.createQuery("SELECT e FROM PasswordPolicy e WHERE e.tenantId = ?1 AND e.isActive = ?2", PasswordPolicy.class)
					.setParameter(1, tenantId)
					.setParameter(2, isActive).getSingleResult();
			
			//passwordPolicy = passwordPolicyRepository.findByTenantIdAndIsactive(tenantId, isActive);
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
			passwordPolicy = passwordPolicyRepository.getRecordList(isActive, tenantId);
			if(Objects.nonNull(passwordPolicy)) {
				return passwordPolicy;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get tenantId and isactive" + ExceptionUtils.getStackTrace(e));
		}
		return passwordPolicy;
	} 
}
