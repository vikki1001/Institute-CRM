package com.ksv.ktrccrm.service;

import java.util.List;

import com.ksv.ktrccrm.db1.entities.ProjectDetails;

public interface ProjectDetailsService {

	public List<ProjectDetails> getRecordList(String isActive, String tenantId) throws Exception;

}
