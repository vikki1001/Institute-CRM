package com.ksv.ktrccrm.db1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ksv.ktrccrm.db1.entities.BranchesInOrganization;

public interface BranchesInOrganizationRepository extends JpaRepository<BranchesInOrganization, Long> {

	@Query("SELECT u FROM BranchesInOrganization u WHERE u.isActive = :isActive")
	List<BranchesInOrganization> getBranchList(String isActive);

}
