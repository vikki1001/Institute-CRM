package com.ksv.ktrccrm.dao;

import java.util.List;
import java.util.Optional;

import com.ksv.ktrccrm.db1.entities.BranchMst;

public interface BranchDao {

	public List<BranchMst> getRecordList(String isActive, String tenantId) throws Exception;

	public BranchMst saveBranch(BranchMst branch) throws Exception;

	public BranchMst getBranchById(Long branchId) throws Exception;
	
	public Optional<BranchMst> findBranchByBranchCode(String branchCode) throws Exception;

	/* For Duplicate Branch Code Validation */
	public boolean branchExists(String branchCode) throws Exception;
}
