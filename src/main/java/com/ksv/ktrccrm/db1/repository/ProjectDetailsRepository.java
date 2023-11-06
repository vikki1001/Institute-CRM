package com.ksv.ktrccrm.db1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.db1.entities.ProjectDetails;

@Repository
public interface ProjectDetailsRepository extends JpaRepository<ProjectDetails, Long> {

	@Query("SELECT u FROM ProjectDetails u WHERE u.isActive =:isActive AND u.tenantId =:tenantId")
	public List<ProjectDetails> getRecordList(String isActive, String tenantId);

}
