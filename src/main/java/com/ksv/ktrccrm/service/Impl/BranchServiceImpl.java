package com.ksv.ktrccrm.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.BranchDao;
import com.ksv.ktrccrm.db1.entities.BranchMst;
import com.ksv.ktrccrm.service.BranchService;

@Service
public class BranchServiceImpl implements BranchService {
	private static final Logger LOGGER = LogManager.getLogger(BranchServiceImpl.class);

	@Autowired
	private BranchDao branchDao;

	/* List Of IsActive Branch */
	public List<BranchMst> getRecordList(String isActive,  String tenantId) throws Exception {
		List<BranchMst> branchList = branchDao.getRecordList(isActive, tenantId);
		try {
			if (Objects.nonNull(branchList)) {
				return branchList;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display branch list " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	/* Save & Update Branch */
	public BranchMst saveBranch(BranchMst branch) throws Exception {
		try {
			if (Objects.nonNull(branch)) {
				this.branchDao.saveBranch(branch);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while save & update branch " + ExceptionUtils.getStackTrace(e));
		}
		return branch;
	}

	/* Find Branch By Id */
	public BranchMst getBranchById(Long branchId) throws Exception {
		BranchMst branchMst = branchDao.getBranchById(branchId);
		try {
			if (Objects.nonNull(branchMst)) {
				return branchMst;
			}
		} catch (Exception e) {
			LOGGER.error("Error while Branch not found for id " + ExceptionUtils.getStackTrace(e));
		}
		return new BranchMst();
	}

	/* For Duplicate Branch Code Validation */
	@Override
	public Optional<BranchMst> findBranchByBranchCode(String branchCode) throws Exception {
		Optional<BranchMst> optional = branchDao.findBranchByBranchCode(branchCode);
		try {
			if (optional.isPresent()) {
				return optional;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while find duplicate branch " + ExceptionUtils.getStackTrace(e));
		}
		return Optional.empty();
	}

	@Override
	public boolean branchExists(String branchCode) throws Exception {
		try {
			return findBranchByBranchCode(branchCode).isPresent();
		} catch (Exception e) {
			LOGGER.error("Error occur while find branch code " + ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	public BranchMst branchEnableAndDisable(Long branchId) {
		try {
			BranchMst branchDetails = branchDao.getBranchById(branchId);
			if (Objects.nonNull(branchDetails)) {
				String checkActiveOrInActiveData = branchDetails.getIsActive();

				if (checkActiveOrInActiveData.equals(ProdConstant.TRUE)) {
					branchDetails.setIsActive(ProdConstant.FALSE);
					branchDao.saveBranch(branchDetails);
				} else if (checkActiveOrInActiveData.equals(ProdConstant.FALSE)) {
					branchDetails.setIsActive(ProdConstant.TRUE);
					branchDao.saveBranch(branchDetails);
				}
			} else {
				System.out.println("docDetails object is null");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while activate and deactivate branch  " + ExceptionUtils.getStackTrace(e));
		}
		return new BranchMst();
	}
}
