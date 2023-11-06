package com.ksv.ktrccrm.service;

import java.util.List;

import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.LeaveMst;

public interface ReportService {
	
	public List<EmpBasicDetails> getBySearch(String empId, String fullName, String departName,
			String grade) throws Exception;
	
	public List<LeaveMst> activeLeave() throws Exception;

	public List<LeaveMst> getApprovedLeave() throws Exception;

	public List<LeaveMst> getApprovedAndLeaveType(String leaveType) throws Exception;
}
