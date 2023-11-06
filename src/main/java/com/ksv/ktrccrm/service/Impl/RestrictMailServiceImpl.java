package com.ksv.ktrccrm.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ksv.ktrccrm.dao.RestrictMailDao;
import com.ksv.ktrccrm.service.RestrictMailService;

@Service
public class RestrictMailServiceImpl implements RestrictMailService {
	private static final Logger LOGGER = LogManager.getLogger(RestrictMailServiceImpl.class);

	@Autowired
	private RestrictMailDao restrictMailDao;

	@Override
	public List<String> getRestrictEmployees(String isActive) throws Exception {
		List<String> restrictEmpList = restrictMailDao.getRestrictEmployees(isActive);
		try {
			if (!restrictEmpList.isEmpty()) {
				return restrictEmpList;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get all restrict employee id " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}
}