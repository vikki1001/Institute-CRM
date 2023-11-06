package com.ksv.ktrccrm.db1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ksv.ktrccrm.db1.entities.RestrictMailTrigger;

public interface RestrictMailTriggerRepository extends JpaRepository<RestrictMailTrigger, Long> {

	@Query("SELECT userId FROM RestrictMailTrigger u WHERE u.isActive = :isActive")
	public List<String> getRestrictEmployees(String isActive);

}
