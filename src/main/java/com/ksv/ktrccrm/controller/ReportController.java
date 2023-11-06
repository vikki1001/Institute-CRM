package com.ksv.ktrccrm.controller;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.CheckInCheckOut;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.LeaveMst;
import com.ksv.ktrccrm.db1.entities.PayrollMst;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.CheckInCheckOutService;
import com.ksv.ktrccrm.service.EmpRegistartionService;
import com.ksv.ktrccrm.service.LeaveService;
import com.ksv.ktrccrm.service.PayrollService;
import com.ksv.ktrccrm.service.ReportService;
import com.ksv.ktrccrm.service.UserService;

@Controller
public class ReportController {
	private static final Logger LOG = LogManager.getLogger(ReportController.class);

	@Autowired
	private ReportService reportService;
	@Autowired
	private EmpRegistartionService empRegistartionService;
	@Autowired
	private CheckInCheckOutService chkInChkOutService;
	@Autowired
	private AuditRecordService auditRecordService;
	@Autowired
	private LeaveService leaveService;
	@Autowired
	PayrollService payrollService;
	@Autowired
	private UserService userService;

	/* Display List of CheckInOut */
	@GetMapping(value = "/empTotalSummaryReport")
	public String checkInCheckOutList(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			EmpBasicDetails basicDetails = new EmpBasicDetails();
			basicDetails.setEmpId(ProdConstant.ALL);
			List<EmpBasicDetails> empBasicDetails = empRegistartionService.getFindAllEmpRegList();
			empBasicDetails.add(basicDetails);
			Collections.sort(empBasicDetails, EmpBasicDetails.Comparators.EMPLOYEEID);
			model.addAttribute("empIdList", empBasicDetails);

			List<String> empBasicDetails2 = empRegistartionService.getUniqueTenantId();
			// empBasicDetails2.add(ProdConstant.ALL);
			// Collections.sort(empBasicDetails2, Collections.reverseOrder());
			if (Objects.nonNull(empBasicDetails2)) {
				model.addAttribute("tenantIdList", empBasicDetails2);
			}

		} catch (Exception e) {
			LOG.error("Error occur while display checkInOut list " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed emp time summary report"));
			auditRecord.setMenuCode("Report");
			auditRecord.setSubMenuCode("Employee FTime Summary Report");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "report/empsummarylist";
	}

	/* Search data using from date, to date, userId & tenantId */
	@GetMapping(value = "/between/fromortooruserIdortenantId")
	public String getfromortooruserId(Model model, @RequestParam("from") String from, @RequestParam("to") String to,
			@RequestParam String userId, @RequestParam String tenantId, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			SimpleDateFormat fromUser = new SimpleDateFormat(ProdConstant.DATETIMEFORMATE2);
			SimpleDateFormat myFormat = new SimpleDateFormat(ProdConstant.DATETIMEFORMATE);

			List<CheckInCheckOut> inCheckOuts = chkInChkOutService.findByDateorUserIdorTenantId(
					myFormat.format(fromUser.parse(from)), myFormat.format(fromUser.parse(to)), userId, tenantId);

			EmpBasicDetails basicDetails = new EmpBasicDetails();
			basicDetails.setEmpId(ProdConstant.ALL);
			List<EmpBasicDetails> empBasicDetails = empRegistartionService.getFindAllEmpRegList();
			empBasicDetails.add(basicDetails);
			Collections.sort(empBasicDetails, EmpBasicDetails.Comparators.EMPLOYEEID);
			model.addAttribute("empIdList", empBasicDetails);

			List<String> empBasicDetails2 = empRegistartionService.getUniqueTenantId();
			// empBasicDetails2.add(ProdConstant.ALL);
			// Collections.sort(empBasicDetails2, Collections.reverseOrder());
			if (Objects.nonNull(empBasicDetails2)) {
				model.addAttribute("tenantIdList", empBasicDetails2);
			}
			if (Objects.nonNull(inCheckOuts)) {
				model.addAttribute("checkInCheckOutList", inCheckOuts);
			}
		} catch (Exception e) {
			LOG.error("Error occur while Search data using from date , userId & tenantId"
					+ ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - search-view emp time summary report"));
			auditRecord.setMenuCode("Report");
			auditRecord.setSubMenuCode("Employee Time Summary Report");
			auditRecord.setActivityCode("SEARCH-VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "report/empsummarylist";
	}

	/* Display List of CheckInOut */
	@GetMapping(value = "/empSummaryReport")
	public String empSummaryReport(String empId, String fullName, String departName, String grade, Model model,
			Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			if (!empId.isEmpty() || !fullName.isEmpty() || !departName.isEmpty() || !grade.isEmpty()) {
				System.out.println("If :::::::::::::");
				model.addAttribute("empBasicList", reportService.getBySearch(empId, fullName, departName, grade));
			} else {
				System.out.println("else :::::::::::::");
			}
		} catch (Exception e) {
			LOG.error("Error occur while display employee summary report list " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - search-view emp summary report"));
			auditRecord.setMenuCode("Report");
			auditRecord.setSubMenuCode("Employee Summary Report");
			auditRecord.setActivityCode("SEARCH-VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "report/empsummaryreport";
	}

	/* Display List of Leave Summary Report */
	@GetMapping(value = "/leaveSummaryReport")
	public String leaveSummaryReport(Model model, @ModelAttribute LeaveMst leaveMst, BindingResult result,
			Device device, @Param("leaveType") String leaveType) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			if (Objects.nonNull(leaveMst)) {
				model.addAttribute("leaveMst", leaveMst);
			}
		} catch (Exception e) {
			LOG.error("Error occur while display leave summary page " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - view emp leave summary home page"));
			auditRecord.setMenuCode("Report");
			auditRecord.setSubMenuCode("Leave Summary Report");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "report/leaveSummary";
	}

	/* Display List of Leave Summary Report */
	@GetMapping(value = "/leaveSummary")
	public String getLeaveSummaryReport(Model model, @ModelAttribute LeaveMst leaveMst, BindingResult result,
			Device device, @Param("leaveType") String leaveType) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			if (Objects.nonNull(leaveMst)) {
				model.addAttribute("leaveMst", leaveMst);
			}
			// search then display all active & type of leaves
			if (Objects.nonNull(leaveType) && !ObjectUtils.isEmpty(leaveType)) {
				model.addAttribute("leaveSummaryList", leaveService.getLeaveByLeaveType(leaveType));
			}
		} catch (Exception e) {
			LOG.error("Error occur while display leave summary list " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - search-view emp leave summary report"));
			auditRecord.setMenuCode("Report");
			auditRecord.setSubMenuCode("Leave Summary Report");
			auditRecord.setActivityCode("SEARCH-VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "report/leaveSummary";
	}

	/* Display Leave Approval Report Page */
	@GetMapping(value = "/leaveApprovalReport")
	public String leaveApprovalReport(@ModelAttribute LeaveMst leaveMst, BindingResult result, Model model,
			Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			if (Objects.nonNull(leaveMst)) {
				model.addAttribute("leaveMst", leaveMst);
			}
		} catch (Exception e) {
			LOG.error("Error occur while display leave  approval report Page  " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - view emp leave approval home page"));
			auditRecord.setMenuCode("Report");
			auditRecord.setSubMenuCode("Leave Approval Report");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "report/leaveApproval";
	}

	/* Display List of Leave Approval Report */
	@GetMapping(value = "/leaveApproval")
	public String getLeaveApprovalReport(@ModelAttribute LeaveMst leaveMst, BindingResult result, Model model,
			Device device, @Param("leaveType") String leaveType) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			// search then display all approved, active & type of leave
			if (Objects.nonNull(leaveType)) {
				List<LeaveMst> approvealList = reportService.getApprovedAndLeaveType(leaveType);
				if (Objects.nonNull(approvealList)) {
					model.addAttribute("leaveApprovalList", approvealList);
				}
			}
		} catch (Exception e) {
			LOG.error("Error occur while display leave approval report" + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - search-view emp leave approval report"));
			auditRecord.setMenuCode("Report");
			auditRecord.setSubMenuCode("Leave Approval Report");
			auditRecord.setActivityCode("SEARCH-VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "report/leaveApproval";
	}

	@GetMapping("/payrollDetails")
	public String getPayrollDetails(@ModelAttribute PayrollMst payrollMst, BindingResult result, Model model,
			Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId = null;
		String tenantId = null;
		UserMst user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			empId = authentication.getName();
			if (Objects.nonNull(empId)) {
				user = userService.getUserById(empId);
				tenantId = user.getTenantId();
				if (Objects.nonNull(tenantId)) {
					if (Objects.nonNull(payrollMst)) {
						List<PayrollMst> payRollList = payrollService.getRecordList(ProdConstant.TRUE, tenantId);
						if (Objects.nonNull(payRollList)) {
							model.addAttribute("payrollSlipList", payRollList);
						}
					}
				}
			}
		} catch (Exception e) {
			LOG.error("Error occur while display payroll summary list " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - search-view emp payroll report"));
			auditRecord.setMenuCode("Report");
			auditRecord.setSubMenuCode("Payroll Report");
			auditRecord.setActivityCode("SEARCH-VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "report/payrollDetails";
	}
}
