package com.ksv.ktrccrm.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.mail.internet.MimeMessage;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mobile.device.Device;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.CheckInCheckOut;
import com.ksv.ktrccrm.db1.entities.DepartmentMst;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.SequenceMst;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.CheckInCheckOutService;
import com.ksv.ktrccrm.service.DepartmentService;
import com.ksv.ktrccrm.service.EmpRegistartionService;
import com.ksv.ktrccrm.service.RestrictMailService;
import com.ksv.ktrccrm.service.SequenceMstService;
import com.ksv.ktrccrm.service.UserService;
import com.ksv.ktrccrm.validator.EmpBasicValidator;

@Controller
public class EmpRegistartionController {
	private static final Logger LOG = LogManager.getLogger(EmpRegistartionController.class);

	@Autowired
	private EmpBasicValidator empBasicValidator;

	@InitBinder
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(empBasicValidator);
	}

	@Value("${sender.email.address}")
	private String sendFrom;
	@Value("${receiver.email.address}")
	private String sendTo;
	@Value("${receiver.email.subject}")
	private String subject;

	@Value("${global.redirectmsg.save}")
	private String successSaveMsg;

	@Value("${global.redirect.update}")
	private String successUpdateMsg;

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private UserService userService;

	public EmpRegistartionController(TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private CheckInCheckOutService checkInCheckOutService;
	@Autowired
	private RestrictMailService restrictMailService;

	@Autowired
	private EmpRegistartionService empRegistartionService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private AuditRecordService auditRecordService;
	@Autowired
	private SequenceMstService sequenceMstService;

	@Scheduled(cron = "${scheduling.job.cron}")
	public void tiggerMail() throws Exception {
		List<String> userId = checkInCheckOutService.getNineHourNotComplete();
		List<String> restrictEmployees = restrictMailService.getRestrictEmployees(ProdConstant.TRUE);
		try {
			if (Objects.nonNull(userId) && Objects.nonNull(restrictEmployees)) {
				userId.removeAll(restrictEmployees);
				List<CheckInCheckOut> checkInCheckOutList = checkInCheckOutService.getByUserIdAndCurentDate(userId);
				if (Objects.nonNull(checkInCheckOutList)) {
					Context context = new Context();
					context.setVariable("userList", checkInCheckOutList);
					String body = templateEngine.process("employee/emailTrigger", context);

					MimeMessage message = javaMailSender.createMimeMessage();
					MimeMessageHelper helper = new MimeMessageHelper(message, true);
					helper.setSubject(subject);
					helper.setText(body, true);
					helper.setFrom(sendFrom, "Mail Contant");
					helper.setTo(sendTo);

					javaMailSender.send(message);

					System.out.println("Email send Successfully :::::::::::::::::::::: ");
				} else {
					System.out.println("Error Occur to get CurrentDate & List of empId Data :::::::::::: ");
				}
			} else {
				System.out.println("UserId & RestrictEmployees not found ::::::::::: ");
			}
		} catch (Exception e) {
			LOG.error("When sending mail, an error occurs..... " + ExceptionUtils.getStackTrace(e));
		}
	}

	/* Display All Employee in Organization */
	@GetMapping(value = "/allEmployee")
	public String allEmployee(Model model, Device device) throws Exception {
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
					List<EmpBasicDetails> allEmp = empRegistartionService.getRecordList(ProdConstant.TRUE, tenantId);
					if (Objects.nonNull(allEmp)) {
						model.addAttribute("allEmployee", allEmp);
					}
				}
			}
		} catch (Exception e) {
			LOG.error("Error occur while display all employee details  " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed all employee on dashboard"));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("All Service/Organization");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "employee/allEmployee";
	}

	/* Display Employee Profile in Organization */
	@GetMapping(value = "/profile/{empId}")
	public String getEmpProfileDashboard(@PathVariable(name = "empId") String empId, Model model, Device device)
			throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			EmpBasicDetails empProfile = empRegistartionService.getCurrentUser(empId);
			if (Objects.nonNull(empProfile)) {
				model.addAttribute("empProfile", empProfile);
			}
		} catch (Exception e) {
			LOG.error("Error occur while employee profile details  " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed all employee"));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("All Service/Organization");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "employee/empprofile2";
	}

	/* Display Profile Image */
	@GetMapping(value = "/image")
	public void showImage(@RequestParam("empId") String empId, HttpServletResponse response) throws Exception {
		try {
			EmpBasicDetails empBasicDetails = empRegistartionService.getDataByEmpId(empId);
			if (empBasicDetails != null) {
				response.setContentType(ProdConstant.IMAGEFORMATE);
				response.getOutputStream().write(empBasicDetails.getFile());
				response.getOutputStream().flush();
				response.getOutputStream().close();
			} else {
				System.out.println("Profile Image not get....");
			}
		} catch (Exception e) {
			LOG.error("Error occur while display profile image  " + ExceptionUtils.getStackTrace(e));
		}
	}

	/* Display QR Code Image */
	@GetMapping(value = "/qrCodeImage")
	public void qrCodeImage(@RequestParam("empId") String empId, HttpServletResponse response) throws Exception {
		try {
			EmpBasicDetails empBasicDetails = empRegistartionService.getDataByEmpId(empId);
			if (empBasicDetails != null) {
				response.setContentType(ProdConstant.IMAGEFORMATE);
				response.getOutputStream().write(empBasicDetails.getQrCode());
				response.getOutputStream().flush();
				response.getOutputStream().close();
			} else {
				System.out.println("QR Image not get....");
			}
		} catch (Exception e) {
			LOG.error("Error occur while display QR image  " + ExceptionUtils.getStackTrace(e));
		}
	}

	/* QR Code Download */
	@GetMapping("/downloadQRCode")
	public void downloadFile(@Param("empId") String empId, Model model, HttpServletResponse response, Device device)
			throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			Optional<EmpBasicDetails> optional = empRegistartionService.findById(empId);
			if (optional.isPresent()) {
				EmpBasicDetails basicDetails = optional.get();
				response.setContentType("application/octet-stream");
				String headerKey = "Content-Disposition";
				String headerValue = "attachment; filename = " + basicDetails.getEmpId() + ProdConstant.PNGEXTENSION;
				response.setHeader(headerKey, headerValue);
				ServletOutputStream outputStream = response.getOutputStream();
				outputStream.write(basicDetails.getQrCode());
				outputStream.close();
			}
		} catch (Exception e) {
			LOG.error("Error occur while download QR Code " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - download QR code by employee - " + empId));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Employee Management");
			auditRecord.setActivityCode("Download");
			auditRecordService.save(auditRecord, device);
		}
	}

	/* Display List of IsActive Employee Registration list */
	@GetMapping(value = "/activeEmpRegList")
	public String getActiveEmpRegList(Model model, Device device) throws Exception {
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
					List<EmpBasicDetails> empRegList = empRegistartionService.getRecordList(ProdConstant.TRUE,
							tenantId);
					if (Objects.nonNull(empRegList)) {
						model.addAttribute("empRegList", empRegList);
					}
				}
			}
		} catch (Exception e) {
			LOG.error("Error occur while display active employee registration list " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed active employee list"));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Employee Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "employee/activeEmpRegList";
	}

	/* Display List of InActive Employee Registration list */
	@GetMapping(value = "/inActiveEmpRegList")
	public String getInActiveEmpRegList(Model model, Device device) throws Exception {
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
					List<EmpBasicDetails> empRegList = empRegistartionService.getRecordList(ProdConstant.FALSE,
							tenantId);
					model.addAttribute("empRegList", empRegList);
				}
			}
		} catch (Exception e) {
			LOG.error(
					"Error occur while display inActive employee registration list " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed inActive employee list"));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Employee Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "employee/inActiveEmpRegList";
	}

	@GetMapping(value = "/empRegistration")
	public String empRegistration(@ModelAttribute EmpBasicDetails empBasicDetails, Model model, Device device)
			throws Exception {
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
					List<DepartmentMst> deptList = departmentService.getRecordList(ProdConstant.TRUE, tenantId);
					if (Objects.nonNull(deptList)) {
						model.addAttribute("departmentList", deptList);
					}
				}
			}
			List<SequenceMst> tenantId1 = sequenceMstService.getTenantId(ProdConstant.TRUE);
			if (Objects.nonNull(tenantId1)) {
				model.addAttribute("tenantId", tenantId1);
			}
		} catch (Exception e) {
			LOG.error("Error occur to open employee registration page... " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed employee management form"));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Employee Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "employee/empRegistration";
	}

	@PostMapping(value = "/saveEmployee")
	public String saveEmployee(@ModelAttribute("empBasicDetails") EmpBasicDetails empBasicDetails, BindingResult result,
			Model model, RedirectAttributes redirAttrs, Device device) throws Exception {
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
					List<DepartmentMst> deptList = departmentService.getRecordList(ProdConstant.TRUE, tenantId);
					if (Objects.nonNull(deptList)) {
						model.addAttribute("departmentList", deptList);
					}
				}
			}
			List<SequenceMst> tenantId1 = sequenceMstService.getTenantId(ProdConstant.TRUE);
			if (Objects.nonNull(tenantId1)) {
				model.addAttribute("tenantId", tenantId1);
			}

			if (Objects.nonNull(empBasicDetails)) {
				System.out.println("CONTROLLER IN EMPID :::: " + empBasicDetails.toString());
				empRegistartionService.saveBasicDetails(empBasicDetails);
			}

			redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
		} catch (Exception e) {
			LOG.error("Error occur while save employee records successfully " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - create employee successfully"));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Employee Management");
			auditRecord.setActivityCode("CREATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeEmpRegList";
	}

	/* Update of Registered Employee */
	@GetMapping(value = "/empUpdate/{empId}")
	public ModelAndView empUpdate(@PathVariable(name = "empId") String empId, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		ModelAndView mav = new ModelAndView("employee/editRegistration");
		String loginId = null;
		String tenantId = null;
		UserMst user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			loginId = authentication.getName();
			if (Objects.nonNull(loginId)) {
				user = userService.getUserById(loginId);
				tenantId = user.getTenantId();
				if (Objects.nonNull(tenantId)) {
					List<DepartmentMst> deptList = departmentService.getRecordList(ProdConstant.TRUE, tenantId);
					if (Objects.nonNull(deptList)) {
						mav.addObject("departmentList", deptList);
					}
				}
			}
			EmpBasicDetails empBasicDetails = empRegistartionService.getEmpById(empId);
			if (Objects.nonNull(empBasicDetails)) {
				mav.addObject("empBasicDetails", empBasicDetails);
			}
			List<SequenceMst> tenantId1 = sequenceMstService.getTenantId(ProdConstant.TRUE);
			if (Objects.nonNull(tenantId1)) {
				mav.addObject("tenantId", tenantId1);
			}
		} catch (Exception e) {
			LOG.error("Error occur while edit registered employee... " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed employee id - " + empId));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Employee Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return mav;
	}

	/* Deactivate of Registered Employee */
	@GetMapping(value = "/deactiveEmp/{empId}")
	public String deactiveEmp(EmpBasicDetails empBasicDetails, @PathVariable(name = "empId") String empId,
			Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			empRegistartionService.empEnableAndDisable(empId);
		} catch (Exception e) {
			LOG.error("Error occur while deactivate employee " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord
					.setRemarks(userDetails.getUsername().concat(" - deactivate employee by employee id - " + empId));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Employee Management");
			auditRecord.setActivityCode("DEACTIVATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeEmpRegList";
	}

	/* Activate of Registered Employee */
	@GetMapping(value = "/activeEmp/{empId}")
	public String activeEmp(EmpBasicDetails empBasicDetails, @PathVariable(name = "empId") String empId, Device device)
			throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			empRegistartionService.empEnableAndDisable(empId);
		} catch (Exception e) {
			LOG.error("Error occur while activate employee " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - activate employee by employee id - " + empId));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Employee Management");
			auditRecord.setActivityCode("ACTIVATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/inActiveEmpRegList";
	}

	/* Display Info into profile */
	@GetMapping(value = "/empProfile")
	public String getEmpProfile(@ModelAttribute EmpBasicDetails empBasicDetails, BindingResult result, Model model,
			Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId = null;
		try {
			/* Current user */
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();
			EmpBasicDetails userDetails = null;
			if (empId != null) {
				userDetails = empRegistartionService.getCurrentUser(empId);
				if (userDetails != null) {
					model.addAttribute("empProfile", userDetails);
				} else {
					// Do nothing
				}
			} else {
				// Do nothing
			}
		} catch (Exception e) {
			LOG.error("Error occur while display employee profile " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed employee profile"));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Employee Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "employee/empprofile";
	}

	@PostMapping(value = "/uploadImage")
	public @ResponseBody void uploadImage(@ModelAttribute EmpBasicDetails empBasicDetails, BindingResult result,
			Model model, @RequestParam("file") MultipartFile file, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId = null;
		try {
			/* Current user */
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();

			if (empBasicDetails != null) {
				empRegistartionService.uploadImage(file.getBytes(), empId);
			} else {
				System.out.println("UPLOAD IMAGE FAILED :::::::::::: ");
			}
		} catch (Exception e) {
			LOG.error("Error occur while upload employee image ::::: " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - upload employee image"));
			auditRecord.setMenuCode("Employee Management");
			auditRecord.setSubMenuCode("Profile");
			auditRecord.setActivityCode("UPLOAD");
			auditRecordService.save(auditRecord, device);
		}
	}

}
