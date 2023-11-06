package com.ksv.ktrccrm.db1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ksv.ktrccrm.db1.entities.BranchMst;

public interface BranchRepository extends JpaRepository<BranchMst, Long> {

	@Query("SELECT u FROM BranchMst u WHERE u.isActive=:isActive AND u.tenantId=:tenantId")
	public List<BranchMst> getRecordList(String isActive, String tenantId);

	/* For Duplicate Branch Code Validation */
	@Query("SELECT e FROM BranchMst e WHERE e.branchCode = :branchCode")
	public Optional<BranchMst> findBranchByBranchCode(String branchCode);

	@Query("SELECT e FROM BranchMst e WHERE e.branchId =:branchId")
	public BranchMst getDataById(Long branchId);

}