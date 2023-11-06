package com.ksv.ktrccrm.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ksv.ktrccrm.dao.ProjectDetailsDao;
import com.ksv.ktrccrm.db1.entities.ProjectDetails;
import com.ksv.ktrccrm.service.ProjectDetailsService;

@Service
public class ProjectDetailsServiceImpl implements ProjectDetailsService{

	private static final Logger LOG = LogManager.getLogger(ProjectDetailsServiceImpl.class);
	
	@Autowired
	private ProjectDetailsDao projectDetailsDao;
	
	@Override
	public List<ProjectDetails> getRecordList(String isActive, String tenantId) throws Exception {
		List<ProjectDetails> projectName =new ArrayList<>();
		try {
			projectName = projectDetailsDao.getRecordList(isActive, tenantId);
			if(projectName!= null) {
				return projectName;
			}
		} catch (Exception e) {
			LOG.error("Error occur while get project name " + ExceptionUtils.getStackTrace(e));
		}
		return projectName;
	}

}
