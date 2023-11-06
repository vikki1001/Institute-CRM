package com.ksv.ktrccrm.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.dao.RestrictMailDao;
import com.ksv.ktrccrm.db1.repository.RestrictMailTriggerRepository;

@Repository
public class RestrictMailDaoImpl implements RestrictMailDao {
	private static final Logger LOGGER = LogManager.getLogger(RestrictMailDaoImpl.class);

	@Autowired
	private RestrictMailTriggerRepository restrictMailTriggerRepository;

	@Override
	public List<String> getRestrictEmployees(String isActive) throws Exception {
		List<String> restrictEmpList = restrictMailTriggerRepository.getRestrictEmployees(isActive);
		try {
			if (!restrictEmpList.isEmpty()) {
				return restrictEmpList;
			}
		} catch (Exception e) {
			LOGGER.error("------Error occur while get all restrict employee id ------" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}
}
