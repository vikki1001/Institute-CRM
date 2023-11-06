package com.ksv.ktrccrm.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ksv.ktrccrm.dao.SequenceMstDao;
import com.ksv.ktrccrm.db1.entities.SequenceMst;
import com.ksv.ktrccrm.service.SequenceMstService;

@Service
public class SequenceMstServiceImpl implements SequenceMstService {
	private static final Logger LOGGER = LogManager.getLogger(SequenceMstServiceImpl.class);
	
	@Autowired
	private SequenceMstDao sequenceMstDao;
	
	@Override
	public List<SequenceMst> getTenantId(String isActive) throws Exception {
		List<SequenceMst> sequenceMst = sequenceMstDao.getTenantId(isActive);
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
