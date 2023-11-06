package com.ksv.ktrccrm.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.BranchDao;
import com.ksv.ktrccrm.db1.entities.BranchMst;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.repository.BranchRepository;
import com.ksv.ktrccrm.db1.repository.EmpBasicRepository;

@Repository
public class BranchDaoImpl implements BranchDao {
	private static final Logger LOGGER = LogManager.getLogger(BranchDaoImpl.class);

	@Autowired
	private BranchRepository branchRepository;
	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;
	@Autowired
	private EmpBasicRepository empBasicRepository;

	/* List Of IsActive Branch */
	public List<BranchMst> getRecordList(String isActive,  String tenantId) throws Exception {
		List<BranchMst> branchList = branchRepository.getRecordList(isActive,   tenantId);
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
	public BranchMst saveBranch(BranchMst branchMst) throws Exception {
		String loginId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			loginId = authentication.getName();
			EmpBasicDetails userDetails = empBasicRepository.getCurrentUser(loginId,ProdConstant.TRUE);
			System.out.println("TenantId:::::::::"+ userDetails.getTenantId());
			String tenantId = userDetails.getTenantId();
			  System.out.println("BRANCH ID INSIDE SAVE METHOD......"+ branchMst.getBranchId());
			  if(Objects.nonNull(tenantId)) {
			if (branchMst.getBranchId() == null) {
				branchMst.setBranchCode(branchMst.getBranchCode().toUpperCase());
				branchMst.setAuthStatus("0");
				branchMst.setBranchTypeCode("0");
				branchMst.setIsactive(ProdConstant.TRUE);
				branchMst.setTenantId(userDetails.getTenantId());
				branchMst.setVersion(1);
				branchMst.setBranchCodeName(branchMst.getBranchCode().concat(" - ").concat(branchMst.getBranchName()));
				branchMst.setCreatedDate(checkOutDaoImpl.getDateTime());
				branchMst.setCreatedBy(loginId);
				this.branchRepository.save(branchMst);
			} else {
				Optional<BranchMst> branchMst2 = branchRepository.findById(branchMst.getBranchId());
				if (branchMst2.isPresent()) {
					BranchMst newBranchMst = branchMst2.get();
					newBranchMst.setBranchRefCode(branchMst.getBranchRefCode());
					newBranchMst.setBranchName(branchMst.getBranchName());
					newBranchMst.setBranchType(branchMst.getBranchType());
					newBranchMst.setPostalCode(branchMst.getPostalCode());
					newBranchMst.setAddress1(branchMst.getAddress1());
					newBranchMst.setAddress2(branchMst.getAddress2());
					newBranchMst.setAddress3(branchMst.getAddress3());
					newBranchMst.setCountry(branchMst.getCountry());
					newBranchMst.setState(branchMst.getState());
					newBranchMst.setCity(branchMst.getCity());
					newBranchMst.setContactPerson(branchMst.getContactPerson());
					newBranchMst.setEmail(branchMst.getEmail());
					newBranchMst.setTelephoneno(branchMst.getTelephoneno());
					newBranchMst.setFax(branchMst.getFax());
					newBranchMst.setVersion(1);
					newBranchMst.setBranchCodeName(branchMst.getBranchCode().concat(" - ").concat(branchMst.getBranchName()));
					newBranchMst.setLastModifiedDate(checkOutDaoImpl.getDateTime());
					newBranchMst.setLastModifiedBy(loginId);
					branchRepository.save(newBranchMst);
				} else {
					return branchMst;
				}
			}
			}else{
				System.out.println("tenantId is null");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while save & update branch " + ExceptionUtils.getStackTrace(e));
		}
		return branchMst;
	}

	/* Find Branch By Id */
	public BranchMst getBranchById(Long branchId) throws Exception {
		Optional<BranchMst> optional = branchRepository.findById(branchId);
		BranchMst branchMst = null;
		try {
			if (optional.isPresent()) {
				branchMst = optional.get();
			}
		} catch (Exception e) {
			LOGGER.error("Error while Branch not found for id " + ExceptionUtils.getStackTrace(e));
		}
		return branchMst;
	}

	/* For Duplicate Branch Code Validation */
	@Override
	public Optional<BranchMst> findBranchByBranchCode(String branchCode) throws Exception {
		Optional<BranchMst> optional = branchRepository.findBranchByBranchCode(branchCode);
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
}
