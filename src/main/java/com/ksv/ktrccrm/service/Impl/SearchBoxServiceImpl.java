package com.ksv.ktrccrm.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ksv.ktrccrm.dao.SearchBoxDao;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.service.SearchBoxService;

@Service
public class SearchBoxServiceImpl implements SearchBoxService {
	private static final Logger LOGGER = LogManager.getLogger(SearchBoxServiceImpl.class);

	@Autowired
	private SearchBoxDao searchBoxDao;

	@Override
	public List<EmpBasicDetails> getSearchEmployeeByFullName(String value) throws Exception {
		try {
			List<EmpBasicDetails> basicDetailsList = searchBoxDao.getSearchEmployeeByFullName(value);
			if (!basicDetailsList.isEmpty()) {
				return basicDetailsList;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while search employee by name " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}
}