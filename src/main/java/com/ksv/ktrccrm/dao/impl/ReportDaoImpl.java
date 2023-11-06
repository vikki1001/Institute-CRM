package com.ksv.ktrccrm.dao.impl;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.ReportDao;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.LeaveMst;
import com.ksv.ktrccrm.db1.repository.EmpBasicRepository;
import com.ksv.ktrccrm.db1.repository.LeaveRepository;

@Controller
public class ReportDaoImpl implements ReportDao {
	private static final Logger LOG = LogManager.getLogger(ReportDaoImpl.class);

	@Autowired
	private EmpBasicRepository basicRepository;
	
	@Autowired
	private LeaveRepository leaveRepository;
		
	@Override
	public List<EmpBasicDetails> getBySearch(String empId, String fullName, String departName,
			String grade) throws Exception {
		List<EmpBasicDetails> empBasicDetailsList =  basicRepository.getBySearch(empId, fullName, departName, grade, ProdConstant.TRUE);
		try {
			if (!empBasicDetailsList.isEmpty()) {
				return empBasicDetailsList;
			}
		} catch (Exception e) {
			LOG.error("------Error occur while display getDepartIdorNameorempIdorGrade EmpBasicDetails list------" + ExceptionUtils.getStackTrace(e));
		}
		return empBasicDetailsList;
	}
		
	@Override
	public List<LeaveMst> activeLeave() throws Exception {
		List<LeaveMst> leaveMsts = leaveRepository.activeLeave(ProdConstant.TRUE);
		try {
			if (!leaveMsts.isEmpty()) {
				return leaveMsts;
			}
		} catch (Exception e) {
			LOG.error("------Error occur while display all active leaves ------" + ExceptionUtils.getStackTrace(e));
		}
		return leaveMsts;
	}

	@Override
	public List<LeaveMst> getApprovedLeave() throws Exception {
		List<LeaveMst> leaveMsts = leaveRepository.getApprovedLeave(ProdConstant.APPROVED,ProdConstant.TRUE);
		try {
			if (!leaveMsts.isEmpty()) {
				return leaveMsts;
			}
		} catch (Exception e) {
			LOG.error("------Error occur while display approved leaves ------" + ExceptionUtils.getStackTrace(e));
		}
		return leaveMsts;
	}

	@Override
	public List<LeaveMst> getApprovedAndLeaveType(String leaveType) throws Exception {
		List<LeaveMst> leaveMsts = leaveRepository.getApprovedAndLeaveType(ProdConstant.APPROVED,leaveType,ProdConstant.TRUE);
		try {
			if (!leaveMsts.isEmpty()) {
				return leaveMsts;
			}
		} catch (Exception e) {
			LOG.error("------Error occur while get leave by type of leave ------" + ExceptionUtils.getStackTrace(e));
		}
		return leaveMsts;
	}

}