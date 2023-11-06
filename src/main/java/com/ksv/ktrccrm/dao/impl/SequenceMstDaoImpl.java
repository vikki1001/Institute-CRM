package com.ksv.ktrccrm.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.dao.SequenceMstDao;
import com.ksv.ktrccrm.db1.entities.SequenceMst;
import com.ksv.ktrccrm.db1.repository.SequenceMstRepository;
import com.ksv.ktrccrm.service.Impl.SequenceMstServiceImpl;

@Repository
public class SequenceMstDaoImpl implements SequenceMstDao {
	private static final Logger LOGGER = LogManager.getLogger(SequenceMstServiceImpl.class);
	
	@Autowired
	private SequenceMstRepository sequenceMstRepository;
	
	@Override
	public List<SequenceMst> getTenantId(String isActive) throws Exception {
		List<SequenceMst> sequenceMst = sequenceMstRepository.getTenantId(isActive);
		try {
			if(Objects.nonNull(sequenceMst)) {
				return sequenceMst;
			} else {
				System.out.println("sequenceMst is null");
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while get tenant id " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}
}
