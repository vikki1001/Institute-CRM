package com.ksv.ktrccrm.dao;

import java.util.List;

import com.ksv.ktrccrm.db1.entities.ProjectDetails;

public interface ProjectDetailsDao {

	public List<ProjectDetails> getRecordList(String isActive, String tenantId) throws Exception;

}
