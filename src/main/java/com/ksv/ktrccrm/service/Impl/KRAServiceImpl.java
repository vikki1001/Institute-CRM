package com.ksv.ktrccrm.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ksv.ktrccrm.dao.KRADao;
import com.ksv.ktrccrm.db1.entities.KRA;
import com.ksv.ktrccrm.service.KRAService;

@Service
@Transactional
public class KRAServiceImpl implements KRAService {
	private static final Logger LOGGER = LogManager.getLogger(KRAServiceImpl.class);

	@Autowired
	private KRADao kraDao;

	@Override
	public List<KRA> getRecordList(String isActive, String tenantId) throws Exception {
		List<KRA> kras = kraDao.getRecordList(isActive , tenantId);
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
		try {
			kraDao.saveOrUpdate(kra);
		} catch (Exception e) {
			LOGGER.error("------Error occur while save & update kra ------" + ExceptionUtils.getStackTrace(e));
		}
		return kra;
	}

	@Override
	public KRA getById(Long id) throws Exception {
		KRA kra = kraDao.getById(id);
		try {
			if (Objects.nonNull(kra)) {
				return kra;
			}
		} catch (Exception e) {
			LOGGER.error("------Error occur while get id ------" + ExceptionUtils.getStackTrace(e));
		}
		return new KRA();
	}

	@Override
	public List<KRA> getKRAWithDepIdAndEmpId(String depId) throws Exception {
		List<KRA> kralist = kraDao.getKRAWithDepIdAndEmpId(depId);
		try {
			if (!kralist.isEmpty()) {
				return kralist;
			}
		} catch (Exception e) {
			LOGGER.error("------Error occur while get depid ------" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public void deleteById(Long id) throws Exception {
		try {
			kraDao.deleteById(id);
		} catch (Exception e) {
			LOGGER.error("------Error occur while delete kra by id ------" + ExceptionUtils.getStackTrace(e));
		}
	}
}
