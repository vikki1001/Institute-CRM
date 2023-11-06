package com.ksv.ktrccrm.controller;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.PayrollMst;
import com.ksv.ktrccrm.db1.entities.TenantMst;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.methodUtils.PayrollPDFGenerator;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.EmpRegistartionService;
import com.ksv.ktrccrm.service.PayrollService;
import com.ksv.ktrccrm.service.TenantMstService;
import com.ksv.ktrccrm.service.UserService;

@Controller
public class PayrollController {
	private static final Logger LOGGER = LogManager.getLogger(PayrollController.class);

	@Autowired
	private AuditRecordService auditRecordService;
	@Autowired
	private PayrollService payrollService;
	@Autowired
	private EmpRegistartionService empRegistartionService;
	@Autowired
	private TenantMstService tenantMstService;
	@Autowired
	private UserService userService;

	@Value("${global.redirectmsg.save}")
	private String successSaveMsg;

	/* List of uploaded records */
	@GetMapping("/payrollSlipList")
	public String payrollSlipList(Model model, Device device) throws Exception {
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
					List<PayrollMst> payrollMstList = payrollService.getRecordList(ProdConstant.TRUE, tenantId);
					if (Objects.nonNull(payrollMstList)) {
						model.addAttribute("payrollSlipList", payrollMstList);
					} else {
						System.out.println("List is null ::::::: ");
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring to display salary slip page " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - payroll list"));
			auditRecord.setMenuCode("USER MANAGEMENT");
			auditRecord.setSubMenuCode("Salary Slip");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "payroll/payrollSlipList";
	}

	/* Payroll Home Page under in HR */
	@GetMapping("/payroll")
	public String payroll(PayrollMst payrollMst, Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			model.addAttribute("payrollMst", payrollMst);
		} catch (Exception e) {
			LOGGER.error("Error occuring to open payroll page... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed payroll home page"));
			auditRecord.setMenuCode("HR MANAGEMENT");
			auditRecord.setSubMenuCode("Payroll");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "payroll/payroll";
	}

	/* Save/Upload record into DB */
	@PostMapping("/payroll")
	public String importExcelFile(@RequestParam("file") MultipartFile files, Device device,
			RedirectAttributes redirects) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			if (Objects.nonNull(files)) {
				payrollService.saveAll(files);
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring to upload excel file " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - create payroll successfully"));
			auditRecord.setMenuCode("HR MANAGEMENT");
			auditRecord.setSubMenuCode("Payroll");
			auditRecord.setActivityCode("CREATE");
			auditRecordService.save(auditRecord, device);
			redirects.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
		}

		return "redirect:/payrollSlipList";
	}

	/* Download Payroll Excel Format */
	@GetMapping("/downloadPayroll")
	public void downloadPayrollFile(HttpServletResponse response, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			List<PayrollMst> payrollMst = payrollService.findAll();
			if (Objects.nonNull(payrollMst) && !payrollMst.isEmpty()) {

				ByteArrayInputStream byteArrayInputStream = payrollService.exportPayroll(payrollMst);

				if (Objects.nonNull(byteArrayInputStream)) {
					response.setContentType("application/octet-stream");
					response.setHeader("Content-Disposition", "attachment; filename=" + ProdConstant.PAYROLLFILE
							+ new Date() + ProdConstant.EXCELEXTENSION);
					IOUtils.copy(byteArrayInputStream, response.getOutputStream());
				} else {
					System.out.println("byteArrayInputStream object is null");
				}
			} else {
				System.out.println("PayrollMst object is null");
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while download payroll excel file " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - download payroll format"));
			auditRecord.setMenuCode("HR MANAGEMENT");
			auditRecord.setSubMenuCode("Payroll");
			auditRecord.setActivityCode("PAYROLL FORMAT");
			auditRecordService.save(auditRecord, device);
		}
	}

	/* View Payroll Slip Home Page */
	@GetMapping("/payrollSlipHome")
	public String homePayrollSlip(PayrollMst payrollMst, Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			model.addAttribute("payrollMst", payrollMst);
		} catch (Exception e) {
			LOGGER.error("Error occuring while displaying payroll Slip Home Page " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed payroll slip home page"));
			auditRecord.setMenuCode("EMPLOYEE MANAGEMENT");
			auditRecord.setSubMenuCode("Payroll");
			auditRecord.setActivityCode("PAYROLLSlip");
			auditRecordService.save(auditRecord, device);
		}
		 return "payroll/payrollSlip"; 
			/* return "payroll/popup"; */ 
	}

	/* View Payroll */
	@GetMapping(value = "/payrollSlip", params = "view")
	public String viewPayrollSlip(@RequestParam Integer month, @RequestParam Integer year, Model model,
			Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId = null;
		List<PayrollMst> payrolls = null;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();
			if (Objects.nonNull(empId)) {
			UserMst userMst = userService.getUserById(empId);
			String tenantId = userMst.getTenantId();
			if (Objects.nonNull(tenantId)) {
				payrolls = payrollService.getPayrollSlipByMonthAndYear(month, year, empId,tenantId,ProdConstant.TRUE);
				EmpBasicDetails basicDetails = empRegistartionService.getCurrentUser(empId);
				if (Objects.isNull(payrolls) || payrolls == null) {
					model.addAttribute("empDetails", basicDetails);
					String notPresent = " - ";
					model.addAttribute("notPresent", notPresent);
				} else {
					model.addAttribute("empPayroll", payrolls);
				}
			}else {
				System.out.println("Error occur to get tenantId ::::::::::");
			}
			}else {
				System.out.println("Error occur to get current user ::::::::::");
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while view payroll Slip  " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername()
					.concat(" - viewed payroll slip by employee - " + userDetails.getUsername()));
			auditRecord.setMenuCode("EMPLOYEE MANAGEMENT");
			auditRecord.setSubMenuCode("Payroll");
			auditRecord.setActivityCode("PAYROLLSlip");
			auditRecordService.save(auditRecord, device);
		}
		 return "payroll/payrollSlipView";
	}

	/* Payroll PDF Download */
	@GetMapping(value = "/payrollSlip", params = "download")
	public void exportToPDF(@RequestParam Integer month, @RequestParam Integer year, HttpServletResponse response,
			Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId = null;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();
			if (Objects.nonNull(empId)) {
					UserMst userMst = userService.getUserById(empId);
					String tenantId = userMst.getTenantId();
					if (Objects.nonNull(tenantId)) {
				response.setContentType("application/pdf");

				String headerKey = "Content-Disposition";
				String headerValue = "attachment; filename=salarySlip " + new Date() + ProdConstant.PDFEXTENSION;
				response.setHeader(headerKey, headerValue);

				List<PayrollMst> payrollList = payrollService.getPayrollSlipByMonthAndYear(month, year, empId,tenantId
						,ProdConstant.TRUE);
				EmpBasicDetails basicDetailsList = empRegistartionService.getCurrentUser(empId);
				TenantMst tenantMstList = tenantMstService.getTenantDetails(basicDetailsList.getTenantId(),
						ProdConstant.TRUE);
				PayrollPDFGenerator exporter = new PayrollPDFGenerator(payrollList, basicDetailsList, tenantMstList);
				exporter.exportPDF(response);
		
					}else {
						System.out.println("Error occur while get tenantId.....");
					}
					} else {
				System.out.println("Error occur to get current user ::::::::::");
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while download payroll Slip " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername()
					.concat(" - downlaod payroll slip by employee - " + userDetails.getUsername()));
			auditRecord.setMenuCode("EMPLOYEE MANAGEMENT");
			auditRecord.setSubMenuCode("Payroll");
			auditRecord.setActivityCode("PAYROLLSlip");
			auditRecordService.save(auditRecord, device);
		}
	}
}