package com.ksv.ktrccrm.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.SearchBoxDao;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.repository.EmpBasicRepository;

@Repository
public class SearchBoxDaoImpl implements SearchBoxDao {
	private static final Logger LOGGER = LogManager.getLogger(SearchBoxDaoImpl.class);

	@Autowired
	private EmpBasicRepository empBasicRepository;

	@Override
	public List<EmpBasicDetails> getSearchEmployeeByFullName(String value) throws Exception {
		try {
			List<EmpBasicDetails> basicDetailsList = empBasicRepository.getSearchEmployeeByFullName(value, ProdConstant.TRUE);
			if (!basicDetailsList.isEmpty()) {
				return basicDetailsList;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while search employee by name " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}
}
