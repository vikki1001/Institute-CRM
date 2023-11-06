package com.ksv.ktrccrm.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.dao.ProjectDetailsDao;
import com.ksv.ktrccrm.db1.entities.ProjectDetails;
import com.ksv.ktrccrm.db1.repository.ProjectDetailsRepository;
@Repository
public class ProjectDetailsDaoImpl implements ProjectDetailsDao {

	private static final Logger LOG = LogManager.getLogger(ProjectDetailsDaoImpl.class);
	
	@Autowired
	private ProjectDetailsRepository projectDetailsRepository;
	
	@Override
	public List<ProjectDetails> getRecordList(String isActive, String tenantId) throws Exception {
		List<ProjectDetails> isActiveProjectList = projectDetailsRepository.getRecordList(isActive, tenantId);
		try {
			if(Objects.nonNull(isActiveProjectList)) {
				return isActiveProjectList;
			}
		} catch (Exception e) {
			LOG.error("Error occur while get project name " + ExceptionUtils.getStackTrace(e));

		}
		return new ArrayList<>();
	}

}
