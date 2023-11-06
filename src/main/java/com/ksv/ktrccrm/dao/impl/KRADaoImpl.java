package com.ksv.ktrccrm.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.KRADao;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.KRA;
import com.ksv.ktrccrm.db1.repository.KRARepository;
import com.ksv.ktrccrm.service.EmpRegistartionService;

@Repository
@Transactional
public class KRADaoImpl implements KRADao {
	private static final Logger LOGGER = LogManager.getLogger(KRADaoImpl.class);

	@Autowired
	private KRARepository kraRepository;
	@Autowired
	private EmpRegistartionService empRegistartionService;
	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;

	@Override
	public List<KRA> getRecordList(String isActive, String tenantId) throws Exception {
		List<KRA> kras = kraRepository.getRecordList(isActive, tenantId);
		try {
			if (Objects.nonNull(kras)) {
				return kras;
			} else {
				System.out.println("kra object is null ::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while get kra list" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public KRA saveOrUpdate(KRA kra) throws Exception {
		String loginId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			loginId = authentication.getName();
			if (Objects.nonNull(loginId)) {
				EmpBasicDetails basicDetails = empRegistartionService.getCurrentUser(loginId);
				if (Objects.isNull(kra.getId())) {
					kra.setCreatedBy(loginId);
					kra.setCreatedDate(checkOutDaoImpl.getDateTime());
					kra.setIsActive(ProdConstant.TRUE);
					kra.setTenantId(basicDetails.getTenantId());
					kra.getKraDescription().setKra(kra);
					kraRepository.save(kra);
				} else {
					Optional<KRA> optional = kraRepository.findById(kra.getId());
					if (optional.isPresent()) {
						KRA newKra = optional.get();
						newKra.setKraName(kra.getKraName());
						newKra.setWeightage(kra.getWeightage());
						newKra.setDepartment(kra.getDepartment());
						newKra.setGrade(kra.getGrade());
						newKra.getKraDescription().setDescription(kra.getKraDescription().getDescription());
						newKra.setLastModifiedBy(loginId);
						newKra.setLastModifiedDate(checkOutDaoImpl.getDateTime());
						kraRepository.save(newKra);
					} else {
						return kra;
					}
				}
			} else {
				System.out.println("Error occur to get basic details");
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while save or update kra" + ExceptionUtils.getStackTrace(e));
		}
		return kra;
	}
	
	@Override
	public KRA getById(Long id) throws Exception {
		KRA kra = kraRepository.getById(id);
		try {
			if (Objects.nonNull(kra)) {
				return kra;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get id " + ExceptionUtils.getStackTrace(e));
		}
		return new KRA();
	}

	@Override
	public List<KRA> getKRAWithDepIdAndEmpId(String depId) throws Exception {
		List<KRA> kralist = kraRepository.getKRAWithDepIdAndEmpId(depId,ProdConstant.TRUE);
		try {
			if (!kralist.isEmpty()) {
				return kralist;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get depid " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public void deleteById(Long id) throws Exception {
		try {
			kraRepository.deleteById(id);
		} catch (Exception e) {
			LOGGER.error("Error occur while delete kra by id " + ExceptionUtils.getStackTrace(e));
		}
	}
}
