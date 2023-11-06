package com.ksv.ktrccrm.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;

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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dto.EmpWorkDetailsDto;
import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.DepartmentMst;
import com.ksv.ktrccrm.db1.entities.EmailTemplate;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.EmpKRA;
import com.ksv.ktrccrm.db1.entities.EmpWorkDetails;
import com.ksv.ktrccrm.db1.entities.KRA;
import com.ksv.ktrccrm.db1.entities.TenantMst;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.DepartmentService;
import com.ksv.ktrccrm.service.EmailAndOTPService;
import com.ksv.ktrccrm.service.EmailService;
import com.ksv.ktrccrm.service.EmpKRAService;
import com.ksv.ktrccrm.service.EmpRegistartionService;
import com.ksv.ktrccrm.service.KRAService;
import com.ksv.ktrccrm.service.TenantMstService;
import com.ksv.ktrccrm.service.UserService;

@Controller
public class KRAController {
	private static final Logger LOGGER = LogManager.getLogger(KRAController.class);

	@Autowired
	private AuditRecordService auditRecordService;
	@Autowired
	private KRAService kraService;
	@Autowired
	private EmpKRAService empKRAService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private EmpRegistartionService empRegistartionService;
	@Autowired
	private TemplateEngine templateEngine;
	@Autowired
	private EmailService emailService;
	@Autowired
	private TenantMstService tenantMstService;
	@Autowired
	private UserService userService;
	
	@Value("${global.redirectmsg.save}")
	private String successSaveMsg;

	@Value("${global.redirect.update}")
	private String successUpdateMsg;

	@Value("${global.redirecrmsg.emailSend}")
	private String emailSuccess;

	@Value("${global.redirect.saveDraft}")
	private String saveDraft;

	@Value("${global.redirectmsg.maxKra}")
	private String maxKra;

	public KRAController(TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

	@Autowired
	private EmailAndOTPService emailAndOTPService;

	/* Active KRA List in HR */
	@GetMapping("/kraList")
	public String kraList(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId = null;
		String tenantId = null;
		UserMst user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			empId = authentication.getName();
			if(Objects.nonNull(empId)) {
				user = userService.getUserById(empId);
				tenantId = user.getTenantId();
				if(Objects.nonNull(tenantId)) {
			List<KRA> kraList = kraService.getRecordList(ProdConstant.TRUE, tenantId);
			if (Objects.nonNull(kraList)) {
				model.addAttribute("kraList", kraList);
			}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while display add kra list... " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed add kra list"));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Appraisal Process");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "appraisal/kraList";
	}

	/* Home Page of Add New KRA in HR */
	@GetMapping("/addKRA")
	public String addKRA(@ModelAttribute("kra") KRA kra, Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId = null;
		String tenantId = null;
		UserMst user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			// model.addAttribute("kra", kra);
			empId = authentication.getName();
			if(Objects.nonNull(empId)) {
				user = userService.getUserById(empId);
				tenantId = user.getTenantId();
				if(Objects.nonNull(tenantId)) {
			List<DepartmentMst> deptList = departmentService.getRecordList(ProdConstant.TRUE,tenantId);
			if (Objects.nonNull(deptList)) {
				model.addAttribute("departmentList", deptList);
			}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while display KRA add page... " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed KRA home page"));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Appraisal Process");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "appraisal/addKRA";
	}

	/* Add New KRA Method in HR */
	@PostMapping("/saveKRA")
	public String saveKRA(@ModelAttribute("kra") KRA kra, BindingResult bindingResult, Model model, Device device,
			RedirectAttributes redirAttrs) throws Exception { 
		AuditRecord auditRecord = new AuditRecord();
		String empId = null;
		String tenantId = null;
		UserMst user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			empId = authentication.getName();
			if(Objects.nonNull(empId)) {
				user = userService.getUserById(empId);
				tenantId = user.getTenantId();
				if(Objects.nonNull(tenantId)); 
			List<DepartmentMst> deptList = departmentService.getRecordList(ProdConstant.TRUE, tenantId);
			if (Objects.nonNull(deptList)) {
				model.addAttribute("departmentList", deptList);
			}
			}
		
			kraService.saveOrUpdate(kra);
			redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
		} catch (Exception e) {
			LOGGER.error("Error occuring while save/update kra ... " + ExceptionUtils.getStackTrace(e));
		} finally {
	
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - create new KRA"));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Appraisal Process");
			auditRecord.setActivityCode("CREATE/UPDATE");
			auditRecordService.save(auditRecord, device);

		}
		return "redirect:/kraList";
	}

	/* update Page of Edit Registered KRA in HR */
	@GetMapping("/updateKRA/{id}")
	public String updateKRA(@ModelAttribute("kra") KRA kra, @PathVariable("id") Long id, Model model, Device device)
			throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId = null;
		String tenantId = null;
		UserMst user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			empId = authentication.getName();
			if(Objects.nonNull(empId)) {
				user = userService.getUserById(empId);
				tenantId = user.getTenantId();
				if(Objects.nonNull(tenantId)) {
			if (Objects.nonNull(kra)) {
				model.addAttribute("kra", kra);
			}
			List<DepartmentMst> deptList = departmentService.getRecordList(ProdConstant.TRUE, tenantId);
			if (Objects.nonNull(deptList)) {
				model.addAttribute("departmentList", deptList);
			}
			KRA kra1 = kraService.getById(id);
			if (Objects.nonNull(kra1)) {
				model.addAttribute("kra", kra1);
			}
		} 
			}
		}catch (Exception e) {
			LOGGER.error("Error occuring while display KRA update page... " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed KRA update page"));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Appraisal Process");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "appraisal/updateKRA";
	}

	/* Delete of Registered KRA */
	@GetMapping(value = "/deleteKRA/{id}")
	public String deleteKRA(KRA kra, @PathVariable(name = "id") Long id, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			kraService.deleteById(id);
		} catch (Exception e) {
			LOGGER.error("Error occur while delete KRA " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - delete KRA Id - " + id));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Appraisal Process");
			auditRecord.setActivityCode("DELETE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/kraList";
	}

	/* All Employee Appraisal Rating Report in HR */
	@GetMapping("/appraisalList")
	public String appraisalList(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId = null;
		String tenantId = null;
		UserMst user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try{
			empId = authentication.getName();
			if(Objects.nonNull(empId)) {
				user = userService.getUserById(empId);
				tenantId = user.getTenantId();
				if(Objects.nonNull(tenantId)) {
			EmpBasicDetails allEmp = new EmpBasicDetails();
			allEmp.setEmpId(ProdConstant.ALL);
			List<EmpBasicDetails> empList = empRegistartionService.getRecordList(ProdConstant.TRUE, tenantId);
			empList.add(allEmp);
			Collections.sort(empList, EmpBasicDetails.Comparators.EMPLOYEEID);
			model.addAttribute("empList", empList);

			List<String> empDepId = empRegistartionService.getUniqueDepId();
			empDepId.add(ProdConstant.ALL);
			// Collections.sort(empDepId);
			if (Objects.nonNull(empDepId)) {
				model.addAttribute("empDepId", empDepId);
			} 
		} 
			}
			}catch (Exception e) {
			LOGGER.error("Error occuring while display kra list... " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed appraisal list"));
			auditRecord.setMenuCode("Report");
			auditRecord.setSubMenuCode("Appraisal Report");
			auditRecord.setActivityCode("VIEW REPORT");
			auditRecordService.save(auditRecord, device);
		}
		return "appraisal/hrAppraisalList";
	}

	@GetMapping("/empIdAndDepId")
	public String empIdAndDepId(@ModelAttribute EmpKRA empKRA, @RequestParam String empId, @RequestParam String depId,
			Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String userId = null;
		String tenantId = null;
		UserMst user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			userId = authentication.getName();
			if(Objects.nonNull(userId)) {
				user = userService.getUserById(userId);
				tenantId = user.getTenantId();
				if(Objects.nonNull(tenantId)) {
			List<EmpKRA> empAppraisalList = empKRAService.findByEmpIdAndDepId(empId, depId);
			EmpBasicDetails allEmp = new EmpBasicDetails();
			allEmp.setEmpId(ProdConstant.ALL);
			List<EmpBasicDetails> empList = empRegistartionService.getRecordList(ProdConstant.TRUE, tenantId);
			empList.add(allEmp);
			Collections.sort(empList, EmpBasicDetails.Comparators.EMPLOYEEID);
			model.addAttribute("empList", empList);

			List<String> empDepId = empRegistartionService.getUniqueDepId();
			empDepId.add(ProdConstant.ALL);
			// Collections.sort(empDepId);
			if (Objects.nonNull(empDepId)) {
				model.addAttribute("empDepId", empDepId);
			}
			if (Objects.nonNull(empAppraisalList)) {
				model.addAttribute("appraisalList", empAppraisalList);
			}
		}else {
			System.out.println("tenantId is null___ ");
		}
			}
		}catch (Exception e) {
			LOGGER.error("Error occuring while display empId and deptId.. " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - dispaly search appraisal data"));
			auditRecord.setMenuCode("Report");
			auditRecord.setSubMenuCode("Appraisal Report");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "appraisal/hrAppraisalList";
	}

	/*---*/

	public List<EmpWorkDetailsDto> getListOfEmpDetails() throws Exception {
		List<EmpWorkDetailsDto> dojEmp = new ArrayList<>();
		String empId;
		String allEmpIds;
		String allEmpFullName;
		String allEmpDoj;
		byte[] image;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();
			EmpBasicDetails basicDetails = empRegistartionService.getCurrentUser(empId);
			TenantMst tenantMst = tenantMstService.getTenantDetails(basicDetails.getTenantId(), ProdConstant.TRUE);
			if (empId != null) {
				List<String> empIds = empRegistartionService.getAllEmpId(empId);
				if (empIds != null) {
					List<EmpWorkDetails> allEmpjoiningDate = empRegistartionService.getAllEmpJoiningDate(empIds);
					if (allEmpjoiningDate != null) {

						for (int i = 0; i < allEmpjoiningDate.size(); i++) {
							String allEmpDOJ = allEmpjoiningDate.get(i).getDateOfJoining();

							Date startDate = Calendar.getInstance().getTime();
							DateFormat dateFormat = new SimpleDateFormat(ProdConstant.DATEFORMATE);
							dateFormat.setTimeZone(TimeZone.getTimeZone(tenantMst.getTimeZone()));
							String strDate = dateFormat.format(startDate);

							LocalDateTime localDateTime = LocalDateTime.parse(allEmpDOJ);
							ZonedDateTime timeZoneSet = ZonedDateTime.of(localDateTime,
									ZoneId.of(tenantMst.getTimeZone()));
							LocalDate localDate = timeZoneSet.toLocalDate();
							Date endDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
							String strDate2 = dateFormat.format(endDate);

							DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ProdConstant.DATEFORMATE);
							LocalDate date1 = LocalDate.parse(strDate2, formatter);
							LocalDate date2 = LocalDate.parse(strDate, formatter);

							Period p = Period.between(date1, date2);

							allEmpIds = allEmpjoiningDate.get(i).getEmpBasicDetails().getEmpId();
							allEmpFullName = allEmpjoiningDate.get(i).getEmpBasicDetails().getFullName();
							image = allEmpjoiningDate.get(i).getEmpBasicDetails().getFile();
							allEmpDoj = p.getYears() + " Year(s) " + p.getMonths() + " Month(s)";

							// dojEmp.add(allEmpIds + " - " + allEmpFullName + " - " + p.getYears() + "
							// Year(s) " + p.getMonths() + " Month(s)");
							EmpWorkDetailsDto dto = new EmpWorkDetailsDto();
							dto.setAllEmpIds(allEmpIds);
							dto.setAllEmpFullName(allEmpFullName);
							dto.setAllEmpDoj(allEmpDoj);
							dto.setAllEmpImage(image);

							dojEmp.add(dto);
						}
					} else {
						System.out.println("List of EmpWorkDetails is null :::::: " + allEmpjoiningDate);
					}
				} else {
					System.out.println("List of Employee Id's is null :::::::: " + empIds);
				}
			} else {
				System.out.println("EmpId is null :::::::: " + empId);
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while get list of employee details ... " + ExceptionUtils.getStackTrace(e));
		}
		return dojEmp;
	}

	/* Teammates List */
	@GetMapping("/teammatesList")
	public String teammatesList(@ModelAttribute EmpKRA empKRA, Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			List<EmpWorkDetailsDto> listOfEmpDetails = getListOfEmpDetails();
			if (Objects.nonNull(listOfEmpDetails)) {
				model.addAttribute("listOfEmpDetails", listOfEmpDetails);
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while display teammates list ... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - dispaly teammates list"));
			auditRecord.setMenuCode("Organization");
			auditRecord.setSubMenuCode("Teammates");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "appraisal/teammatesList";
	}

	/* Tag New KRA List by HR to Manager */
	@GetMapping("/tagKRAReqList/{empId}")
	public String tagKRAReqList(@ModelAttribute EmpKRA empKRA, @PathVariable(name = "empId") String empId, Model model,
			Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			EmpBasicDetails basicDetails = empRegistartionService.getCurrentUser(empId);
			if (Objects.nonNull(basicDetails)) {
				model.addAttribute("currentUser", basicDetails);
			}
			Optional<EmpBasicDetails> empBasicDetails = empRegistartionService.findById(empId);
			if (empBasicDetails.isPresent()) {
				EmpBasicDetails empBasicDetails2 = empBasicDetails.get();
				if (Objects.nonNull(empBasicDetails2)) {
					model.addAttribute("empBasicDetailsList", empBasicDetails2);
				}
			}

			Optional<EmpKRA> optional = empKRAService.findByempId(empId);
			if (optional.isPresent()) {
				EmpKRA empKRA2 = optional.get();
				if (Objects.nonNull(empKRA2)) {
					model.addAttribute("empList", empKRA2);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while display tag KRA Req List ... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - teammate tag KRA list "));
			auditRecord.setMenuCode("Organization");
			auditRecord.setSubMenuCode("Teammates");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "appraisal/tagKRAReqList";
	}

	/* Tag KRA Home Page */
	@GetMapping("/tagKraHome/{empId}")
	public String tagKraHome(@ModelAttribute EmpKRA empKRA, @PathVariable("empId") String empId, Model model,
			Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			Optional<EmpBasicDetails> optional = empRegistartionService.findById(empId);
			if (optional.isPresent()) {
				EmpBasicDetails empBasicDetail = optional.get();
				if (Objects.nonNull(empBasicDetail)) {
					model.addAttribute("empBasicDetail", empBasicDetail);
				}
			}
			EmpBasicDetails userDetails = null;
			if (empId != null) {
				userDetails = empRegistartionService.getCurrentUser(empId);
				if (userDetails != null) {
					String depId = userDetails.getDepartName();
					if (Objects.nonNull(depId)) {
						List<KRA> kraList = kraService.getKRAWithDepIdAndEmpId(depId);
						if (Objects.nonNull(kraList)) {
							model.addAttribute("kraList", kraList);
						}
					}
				} else {
					System.out.println("Employee data not get ::::::");
				}
			} else {
				System.out.println("Current employee is not present ::::::");
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while display tag KRA home page ... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - teammate tag KRA home page "));
			auditRecord.setMenuCode("Organization");
			auditRecord.setSubMenuCode("Teammates");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "appraisal/tagKRA";
	}

	/* Save Tag KRA */
	@PostMapping("/tagKRA")
	public String tagKra(@ModelAttribute EmpKRA empKRA, Model model, RedirectAttributes redirAttrs, Device device)
			throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			EmpKRA getEmKra = empKRAService.getKRAWithDepIdAndEmpId(empKRA.getDepId(), empKRA.getEmpId());

//			if (empKRAService.empKRAExists(empKRA.getEmpId(), empKRA.getKraI())) {
//				redirAttrs.addFlashAttribute("duplicate", "This kra is already exist....");
//			} else {
			if (getEmKra.getWeightageI() != null && getEmKra.getWeightageII() != null
					&& getEmKra.getWeightageIII() != null && getEmKra.getWeightageIV() != null
					&& getEmKra.getWeightageV() != null && getEmKra.getWeightageVI() != null
					&& getEmKra.getWeightageVII() != null && getEmKra.getWeightageVIII() != null
					&& getEmKra.getWeightageIX() != null && getEmKra.getWeightageX() != null
					&& empKRA.getWeightageI() != null) {

				BigDecimal totalWeightage = getEmKra.getWeightageI()
						.add(getEmKra.getWeightageII().add(getEmKra.getWeightageIII().add(getEmKra.getWeightageIV()
								.add(getEmKra.getWeightageV().add(getEmKra.getWeightageVI().add(getEmKra
										.getWeightageVII().add(getEmKra.getWeightageVIII().add(getEmKra.getWeightageIX()
												.add(getEmKra.getWeightageX().add(empKRA.getWeightageI()))))))))));
				double d = totalWeightage.doubleValue();
				if (empKRA.getKraX() != null || d > 100.00) {
					System.out.println("kra I redirect....");
					redirAttrs.addFlashAttribute(ProdConstant.FULL, maxKra);
				} else {
					System.out.println("kra I");
					empKRAService.saveTagKra(empKRA);
					redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
				}
			} else if (getEmKra.getWeightageI() != null && getEmKra.getWeightageII() != null
					&& getEmKra.getWeightageIII() != null && getEmKra.getWeightageIV() != null
					&& getEmKra.getWeightageV() != null && getEmKra.getWeightageVI() != null
					&& getEmKra.getWeightageVII() != null && getEmKra.getWeightageVIII() != null
					&& getEmKra.getWeightageIX() != null && empKRA.getWeightageI() != null) {

				BigDecimal totalWeightage = getEmKra.getWeightageI()
						.add(getEmKra.getWeightageII().add(getEmKra.getWeightageIII()
								.add(getEmKra.getWeightageIV().add(getEmKra.getWeightageV().add(getEmKra
										.getWeightageVI().add(getEmKra.getWeightageVII().add(getEmKra.getWeightageVIII()
												.add(getEmKra.getWeightageIX().add(empKRA.getWeightageI())))))))));
				double d = totalWeightage.doubleValue();
				if (empKRA.getKraX() != null || d < 100.00 || d > 100.00) {
					System.out.println("kra II redirect....");
					redirAttrs.addFlashAttribute(ProdConstant.FULL, maxKra);
				} else {
					System.out.println("kra II");
					empKRAService.saveTagKra(empKRA);
					redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
				}
			} else if (getEmKra.getWeightageI() != null && getEmKra.getWeightageII() != null
					&& getEmKra.getWeightageIII() != null && getEmKra.getWeightageIV() != null
					&& getEmKra.getWeightageV() != null && getEmKra.getWeightageVI() != null
					&& getEmKra.getWeightageVII() != null && getEmKra.getWeightageVIII() != null
					&& empKRA.getWeightageI() != null) {

				BigDecimal totalWeightage = getEmKra.getWeightageI()
						.add(getEmKra.getWeightageII()
								.add(getEmKra.getWeightageIII().add(getEmKra.getWeightageIV().add(getEmKra
										.getWeightageV().add(getEmKra.getWeightageVI().add(getEmKra.getWeightageVII()
												.add(getEmKra.getWeightageVIII().add(empKRA.getWeightageI()))))))));
				double d = totalWeightage.doubleValue();
				if (empKRA.getKraX() != null || d > 100.00) {
					System.out.println("kra III redirect....");
					redirAttrs.addFlashAttribute(ProdConstant.FULL, maxKra);
				} else {
					System.out.println("kra III");
					empKRAService.saveTagKra(empKRA);
					redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
				}
			} else if (getEmKra.getWeightageI() != null && getEmKra.getWeightageII() != null
					&& getEmKra.getWeightageIII() != null && getEmKra.getWeightageIV() != null
					&& getEmKra.getWeightageV() != null && getEmKra.getWeightageVI() != null
					&& getEmKra.getWeightageVII() != null && empKRA.getWeightageI() != null) {

				BigDecimal totalWeightage = getEmKra.getWeightageI().add(getEmKra.getWeightageII()
						.add(getEmKra.getWeightageIII().add(
								getEmKra.getWeightageIV().add(getEmKra.getWeightageV().add(getEmKra.getWeightageVI()
										.add(getEmKra.getWeightageVII().add(empKRA.getWeightageI())))))));
				double d = totalWeightage.doubleValue();
				System.out.println("weightage." + totalWeightage);
				if (empKRA.getKraX() != null || d > 100.00) {
					System.out.println("kra IV redirect....");
					redirAttrs.addFlashAttribute(ProdConstant.FULL, maxKra);
				} else {
					System.out.println("kra IV ....");
					empKRAService.saveTagKra(empKRA);
					redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
				}
			} else if (getEmKra.getWeightageI() != null && getEmKra.getWeightageII() != null
					&& getEmKra.getWeightageIII() != null && getEmKra.getWeightageIV() != null
					&& getEmKra.getWeightageV() != null && getEmKra.getWeightageVI() != null
					&& empKRA.getWeightageI() != null) {

				BigDecimal totalWeightage = getEmKra.getWeightageI()
						.add(getEmKra.getWeightageII().add(getEmKra.getWeightageIII().add(getEmKra.getWeightageIV().add(
								getEmKra.getWeightageV().add(getEmKra.getWeightageVI().add(empKRA.getWeightageI()))))));
				double d = totalWeightage.doubleValue();
				if (empKRA.getKraX() != null || d > 100.00) {
					System.out.println("kra V redirect....");
					redirAttrs.addFlashAttribute(ProdConstant.FULL, maxKra);
				} else {
					System.out.println("kra V ....");
					empKRAService.saveTagKra(empKRA);
					redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
				}
			} else if (getEmKra.getWeightageI() != null && getEmKra.getWeightageII() != null
					&& getEmKra.getWeightageIII() != null && getEmKra.getWeightageIV() != null
					&& getEmKra.getWeightageV() != null && empKRA.getWeightageI() != null) {

				BigDecimal totalWeightage = getEmKra.getWeightageI()
						.add(getEmKra.getWeightageII().add(getEmKra.getWeightageIII().add(
								getEmKra.getWeightageIV().add(getEmKra.getWeightageV().add(empKRA.getWeightageI())))));
				double d = totalWeightage.doubleValue();
				if (empKRA.getKraX() != null || d > 100.00) {
					System.out.println("kra VI redirect....");
					redirAttrs.addFlashAttribute(ProdConstant.FULL, maxKra);
				} else {
					System.out.println("kra VI ....");
					redirAttrs.addFlashAttribute(ProdConstant.FULL, maxKra);
				}
			} else if (getEmKra.getWeightageI() != null && getEmKra.getWeightageII() != null
					&& getEmKra.getWeightageIII() != null && getEmKra.getWeightageIV() != null
					&& empKRA.getWeightageI() != null) {

				BigDecimal totalWeightage = getEmKra.getWeightageI().add(getEmKra.getWeightageII()
						.add(getEmKra.getWeightageIII().add(getEmKra.getWeightageIV().add(empKRA.getWeightageI()))));
				double d = totalWeightage.doubleValue();
				if (empKRA.getKraX() != null || d > 100.00) {
					redirAttrs.addFlashAttribute(ProdConstant.FULL, maxKra);
				} else {
					empKRAService.saveTagKra(empKRA);
					redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
				}
			} else if (getEmKra.getWeightageI() != null && getEmKra.getWeightageII() != null
					&& getEmKra.getWeightageIII() != null && empKRA.getWeightageI() != null) {

				BigDecimal totalWeightage = getEmKra.getWeightageI()
						.add(getEmKra.getWeightageII().add(getEmKra.getWeightageIII().add(empKRA.getWeightageI())));
				double d = totalWeightage.doubleValue();
				if (empKRA.getKraX() != null || d > 100.00) {
					redirAttrs.addFlashAttribute(ProdConstant.FULL, maxKra);
				} else {
					empKRAService.saveTagKra(empKRA);
					redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
				}
			} else if (getEmKra.getWeightageI() != null && getEmKra.getWeightageII() != null
					&& empKRA.getWeightageI() != null) {
				BigDecimal totalWeightage = getEmKra.getWeightageI()
						.add(getEmKra.getWeightageII().add(empKRA.getWeightageI()));
				double d = totalWeightage.doubleValue();
				if (empKRA.getKraX() != null || d > 100.00) {
					redirAttrs.addFlashAttribute(ProdConstant.FULL, maxKra);
				} else {
					empKRAService.saveTagKra(empKRA);
					redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
				}
			} else if (getEmKra.getWeightageI() != null && empKRA.getWeightageI() != null) {
				BigDecimal totalWeightage = getEmKra.getWeightageI().add(empKRA.getWeightageI());
				double d = totalWeightage.doubleValue();
				if (empKRA.getKraX() != null || d > 100.00) {
					redirAttrs.addFlashAttribute(ProdConstant.FULL, maxKra);
				} else {
					empKRAService.saveTagKra(empKRA);
					redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
				}
			} else {
				empKRAService.saveTagKra(empKRA);
				redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
			}
			// }
		} catch (Exception e) {
			LOGGER.error("Error occuring while save tag kra  ... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - teammate create tag KRA"));
			auditRecord.setMenuCode("Organization");
			auditRecord.setSubMenuCode("Teammates");
			auditRecord.setActivityCode("CREATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/teammatesList";
	}

	/* Update Tag KRA Home Page */
	@GetMapping("/updateTagKra/{empId}")
	public String updateTagKra(@ModelAttribute EmpKRA empKRA, @PathVariable("empId") String empId, Model model,
			Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			EmpBasicDetails userDetails = null;
			if (empId != null) {
				userDetails = empRegistartionService.getCurrentUser(empId);
				if (userDetails != null) {
					String depId = userDetails.getDepartName();
					List<KRA> kraList = kraService.getKRAWithDepIdAndEmpId(depId);
					if (Objects.nonNull(kraList)) {
						model.addAttribute("kraList", kraList);
					}
					EmpKRA kraList2 = empKRAService.getKRAWithDepIdAndEmpId(depId, empId);
					if (Objects.nonNull(kraList2)) {
						model.addAttribute("kraList2", kraList2);
					}
				} else {
					System.out.println("Employee data not get ::::::");
				}
			} else {
				System.out.println("Current employee is not present ::::::");
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occuring while display update tag KRA home page ... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - teammate update tag KRA home page "));
			auditRecord.setMenuCode("Organization");
			auditRecord.setSubMenuCode("Teammates");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "appraisal/updateTagKra";
	}

	/* Update Tag KRA */
	@PostMapping("/updateTagKRA")
	public String updateTagKRA(@ModelAttribute EmpKRA empKRA, Model model, RedirectAttributes redirAttrs, Device device)
			throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
//			if (empKRAService.empKRAExists(empKRA.getKraI(), empKRA.getKraII(), empKRA.getKraIII(), empKRA.getKraIV(),
//					empKRA.getKraV(), empKRA.getKraVI(), empKRA.getKraVII(), empKRA.getKraVIII(), empKRA.getKraIX(),
//					empKRA.getKraX())) {
//				redirAttrs.addFlashAttribute("duplicate", "This kra is already exist....");
//			} else {
			if (empKRA.getWeightageI() != null && empKRA.getWeightageII() != null && empKRA.getWeightageIII() != null
					&& empKRA.getWeightageIV() != null && empKRA.getWeightageV() != null
					&& empKRA.getWeightageVI() != null && empKRA.getWeightageVII() != null
					&& empKRA.getWeightageVIII() != null && empKRA.getWeightageIX() != null
					&& empKRA.getWeightageX() != null) {
				BigDecimal totalWeightage = empKRA.getWeightageI()
						.add(empKRA.getWeightageII().add(empKRA.getWeightageIII()
								.add(empKRA.getWeightageIV().add(empKRA.getWeightageV().add(empKRA.getWeightageVI()
										.add(empKRA.getWeightageVII().add(empKRA.getWeightageVIII()
												.add(empKRA.getWeightageIX().add(empKRA.getWeightageX())))))))));
				double d = totalWeightage.doubleValue();
				if (empKRA.getKraX() != null || d > 100.00) {
					redirAttrs.addFlashAttribute(ProdConstant.FULL, maxKra);
				} else {
					empKRAService.updateTagKra(empKRA);
					redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successUpdateMsg);
				}
			} else if (empKRA.getWeightageI() != null && empKRA.getWeightageII() != null
					&& empKRA.getWeightageIII() != null && empKRA.getWeightageIV() != null
					&& empKRA.getWeightageV() != null && empKRA.getWeightageVI() != null
					&& empKRA.getWeightageVII() != null && empKRA.getWeightageVIII() != null
					&& empKRA.getWeightageIX() != null) {
				BigDecimal totalWeightage = empKRA.getWeightageI()
						.add(empKRA.getWeightageII()
								.add(empKRA.getWeightageIII().add(empKRA.getWeightageIV().add(
										empKRA.getWeightageV().add(empKRA.getWeightageVI().add(empKRA.getWeightageVII()
												.add(empKRA.getWeightageVIII().add(empKRA.getWeightageIX()))))))));
				double d = totalWeightage.doubleValue();
				if (empKRA.getKraX() != null || d > 100.00) {
					redirAttrs.addFlashAttribute(ProdConstant.FULL, maxKra);
				} else {
					empKRAService.updateTagKra(empKRA);
					redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successUpdateMsg);
				}
			} else if (empKRA.getWeightageI() != null && empKRA.getWeightageII() != null
					&& empKRA.getWeightageIII() != null && empKRA.getWeightageIV() != null
					&& empKRA.getWeightageV() != null && empKRA.getWeightageVI() != null
					&& empKRA.getWeightageVII() != null && empKRA.getWeightageVIII() != null) {
				BigDecimal totalWeightage = empKRA.getWeightageI()
						.add(empKRA.getWeightageII()
								.add(empKRA.getWeightageIII().add(
										empKRA.getWeightageIV().add(empKRA.getWeightageV().add(empKRA.getWeightageVI()
												.add(empKRA.getWeightageVII().add(empKRA.getWeightageVIII())))))));
				double d = totalWeightage.doubleValue();
				if (empKRA.getKraX() != null || d > 100.00) {
					redirAttrs.addFlashAttribute(ProdConstant.FULL, maxKra);
				} else {
					empKRAService.updateTagKra(empKRA);
					redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successUpdateMsg);
				}
			} else if (empKRA.getWeightageI() != null && empKRA.getWeightageII() != null
					&& empKRA.getWeightageIII() != null && empKRA.getWeightageIV() != null
					&& empKRA.getWeightageV() != null && empKRA.getWeightageVI() != null
					&& empKRA.getWeightageVII() != null) {
				BigDecimal totalWeightage = empKRA.getWeightageI()
						.add(empKRA.getWeightageII().add(empKRA.getWeightageIII().add(empKRA.getWeightageIV().add(
								empKRA.getWeightageV().add(empKRA.getWeightageVI().add(empKRA.getWeightageVII()))))));
				double d = totalWeightage.doubleValue();
				if (empKRA.getKraX() != null || d > 100.00) {
					redirAttrs.addFlashAttribute(ProdConstant.FULL, maxKra);
				} else {
					empKRAService.updateTagKra(empKRA);
					redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successUpdateMsg);
				}
			} else if (empKRA.getWeightageI() != null && empKRA.getWeightageII() != null
					&& empKRA.getWeightageIII() != null && empKRA.getWeightageIV() != null
					&& empKRA.getWeightageV() != null && empKRA.getWeightageVI() != null) {
				BigDecimal totalWeightage = empKRA.getWeightageI()
						.add(empKRA.getWeightageII().add(empKRA.getWeightageIII().add(
								empKRA.getWeightageIV().add(empKRA.getWeightageV().add(empKRA.getWeightageVI())))));
				double d = totalWeightage.doubleValue();
				if (empKRA.getKraX() != null || d > 100.00) {
					redirAttrs.addFlashAttribute(ProdConstant.FULL, maxKra);
				} else {
					empKRAService.updateTagKra(empKRA);
					redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successUpdateMsg);
				}
			} else if (empKRA.getWeightageI() != null && empKRA.getWeightageII() != null
					&& empKRA.getWeightageIII() != null && empKRA.getWeightageIV() != null
					&& empKRA.getWeightageV() != null) {
				BigDecimal totalWeightage = empKRA.getWeightageI().add(empKRA.getWeightageII()
						.add(empKRA.getWeightageIII().add(empKRA.getWeightageIV().add(empKRA.getWeightageV()))));
				double d = totalWeightage.doubleValue();
				if (empKRA.getKraX() != null || d > 100.00) {
					redirAttrs.addFlashAttribute(ProdConstant.FULL, maxKra);
				} else {
					empKRAService.updateTagKra(empKRA);
					redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successUpdateMsg);
				}
			} else if (empKRA.getWeightageI() != null && empKRA.getWeightageII() != null
					&& empKRA.getWeightageIII() != null && empKRA.getWeightageIV() != null) {
				BigDecimal totalWeightage = empKRA.getWeightageI()
						.add(empKRA.getWeightageII().add(empKRA.getWeightageIII().add(empKRA.getWeightageIV())));
				double d = totalWeightage.doubleValue();
				if (empKRA.getKraX() != null || d > 100.00) {
					redirAttrs.addFlashAttribute(ProdConstant.FULL, maxKra);
				} else {
					empKRAService.updateTagKra(empKRA);
					redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successUpdateMsg);
				}
			} else if (empKRA.getWeightageI() != null && empKRA.getWeightageII() != null
					&& empKRA.getWeightageIII() != null) {
				BigDecimal totalWeightage = empKRA.getWeightageI()
						.add(empKRA.getWeightageII().add(empKRA.getWeightageIII()));
				double d = totalWeightage.doubleValue();
				if (empKRA.getKraX() != null || d > 100.00) {
					redirAttrs.addFlashAttribute(ProdConstant.FULL, maxKra);
				} else {
					empKRAService.updateTagKra(empKRA);
					redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successUpdateMsg);
				}
			} else if (empKRA.getWeightageI() != null && empKRA.getWeightageII() != null) {
				BigDecimal totalWeightage = empKRA.getWeightageI().add(empKRA.getWeightageII());
				double d = totalWeightage.doubleValue();
				if (empKRA.getKraX() != null || d > 100.00) {
					redirAttrs.addFlashAttribute(ProdConstant.FULL, maxKra);
				} else {
					empKRAService.updateTagKra(empKRA);
					redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successUpdateMsg);
				}
			} else if (empKRA.getWeightageI() != null) {
				BigDecimal totalWeightage = empKRA.getWeightageI();
				double d = totalWeightage.doubleValue();
				if (empKRA.getKraX() != null || d > 100.00) {
					redirAttrs.addFlashAttribute(ProdConstant.FULL, maxKra);
				} else {
					empKRAService.updateTagKra(empKRA);
					redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successUpdateMsg);
				}
			}
			// }
		} catch (Exception e) {
			LOGGER.error("Error occuring while update tag kra  ... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - teammate update tag KRA"));
			auditRecord.setMenuCode("Organization");
			auditRecord.setSubMenuCode("Teammates");
			auditRecord.setActivityCode("UPDATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/teammatesList";
	}

	/*---*/

	/* Employee Self Appraisal List */
	@GetMapping("/selfAppraisalList")
	public String selfAppraisalList(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId = null;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();

			if (Objects.nonNull(empId)) {
				EmpBasicDetails basicDetails = empRegistartionService.getCurrentUser(empId);
				model.addAttribute("currentUser", basicDetails);

				List<EmpBasicDetails> empBasicDetails = empRegistartionService.listOfCurrentUser(empId);
				model.addAttribute("getEmpId", empBasicDetails);

				List<EmpKRA> empKRA = empKRAService.getCurrentEmpAppraisal(empId);
				model.addAttribute("appraisalList", empKRA);

				List<EmpKRA> empKRA2 = empKRAService.appraisalCycleList(empId);
				for (EmpKRA emp : empKRA2) {
					System.out.println("Get daya..." + emp.getDate());
				}
				model.addAttribute("appraisalCycleList", empKRA2);

				LocalDate currentdate = LocalDate.now();
				String month = currentdate.format(DateTimeFormatter.ofPattern(ProdConstant.MONTHFORMAT));
				String year = currentdate.format(DateTimeFormatter.ofPattern(ProdConstant.YEARFORMAT));
				int nextYear = currentdate.getYear() + 1;

				if ("10".equals(month) || "11".equals(month) || "12".equals(month) || "01".equals(month)
						|| "02".equals(month) || "03".equals(month)) {
					model.addAttribute("month", "Appraisal Cycle October " + year + " to March " + nextYear);
				} else {
					System.out.println("Else Month ::::: " + month);
					model.addAttribute("month", "Appraisal Cycle April " + year + " to September " + year);
				}

			} else {
				System.out.println("Current empId is not present");
			}
		} catch (Exception e) {
			LOGGER.error("Error Ocuring while display current employee appraisal " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed appraisal list"));
			auditRecord.setMenuCode("Employee Management");
			auditRecord.setSubMenuCode("Performance");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "appraisal/myreview";
	}

	/* Employee Self Appraisal Home Page */
	@GetMapping("/selfAppraisal/{empId}")
	public String selfAppraisal(@ModelAttribute EmpKRA empKRA, @PathVariable String empId, Model model, Device device)
			throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			Optional<EmpKRA> optional = empKRAService.findByempId(empId);
			if (optional.isPresent()) {
				EmpKRA empKRA2 = optional.get();
				if (Objects.nonNull(empKRA2)) {
					model.addAttribute("empKRA", empKRA2);
				}
			}

			EmpBasicDetails userDetails = null;
			if (empId != null) {
				userDetails = empRegistartionService.getCurrentUser(empId);
				if (userDetails != null) {
					model.addAttribute("userDetail", userDetails);
					String depId = userDetails.getDepartName();

					EmpKRA empKRAList = empKRAService.getKRAWithDepIdAndEmpId(depId, empId);
					if (Objects.nonNull(empKRAList)) {
						model.addAttribute("kraList", empKRAList);
					}
				} else {
					System.out.println("Employee data not get ::::::");
				}
			} else {
				System.out.println("Current employee is not present ::::::");
			}

			LocalDate currentdate = LocalDate.now();
			String month = currentdate.format(DateTimeFormatter.ofPattern(ProdConstant.MONTHFORMAT));
			String year = currentdate.format(DateTimeFormatter.ofPattern(ProdConstant.YEARFORMAT));
			int nextYear = currentdate.getYear() + 1;

			if ("10".equals(month) || "11".equals(month) || "12".equals(month) || "01".equals(month)
					|| "02".equals(month) || "03".equals(month)) {
				model.addAttribute("month", "Appraisal Cycle October " + year + " to March " + nextYear);
			} else {
				System.out.println("Else Month ::::: " + month);
				model.addAttribute("month", "Appraisal Cycle April " + year + " to September " + year);
			}
		} catch (Exception e) {
			LOGGER.error("Error Ocuring while display add self appraisal home page " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - view self appraisal home page"));
			auditRecord.setMenuCode("Employee Management");
			auditRecord.setSubMenuCode("Performance");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "appraisal/selfAppraisal";
	}

	/* Employee Self Appraisal Save & Save Draft */
	@PostMapping(value = "/saveAppraisal")
	public String saveAppraisal(@ModelAttribute EmpKRA empKRA, Model model, Device device,
			RedirectAttributes redirAttrs, @RequestParam(value = "action", required = true) String action)
			throws Exception {
		System.out.println("GET EMP KRA......." + empKRA.toString());
		System.out.println("GET EMP KRA......." + empKRA.getEmpId());
		System.out.println("ACTION......." + action);
		AuditRecord auditRecord = new AuditRecord();
		EmailTemplate emailTemplate = emailService.getEmailTemplate(ProdConstant.TRUE, ProdConstant.SELFAPPRAISAL);
		try {
			if (action.equals("saveDraft")) {
				empKRAService.saveDraftSelfAppraisal(empKRA);
				redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, saveDraft);
			}

			if (action.equals("save")) {
				empKRAService.saveSelfAppraisal(empKRA);
				redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);

				/* Self Appraisal Complete Mail send to Reporting Manager */
				List<EmpKRA> empKRAList = null;
				if (Objects.nonNull(empKRA)) {
					empKRAList = empKRAService.getCurrentEmpAppraisal(empKRA.getEmpId());
					if (Objects.nonNull(empKRAList)) {
						Context context = new Context();
						context.setVariable("empKRAList", empKRAList);
						String body = templateEngine.process("appraisal/selfAppraisalMail", context);
						String from = emailTemplate.getEmailFrom();
						String to = emailTemplate.getEmailTo();
						String subject = emailTemplate.getEmailSub();
						String cc = emailTemplate.getEmailCc();

						emailAndOTPService.emailSend(from, to, subject, body, cc);
						redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, emailSuccess);

					} else {
						System.out.println("Nothing Happen ::::::");
					}
				} else {
					System.out.println("Nothing Happen ::::::");
				}
			}

			redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
			// return "redirect:/selfAppraisalList";
		} catch (Exception e) {
			LOGGER.error("Error occuring while save self rating ... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - create self appraisal "));
			auditRecord.setMenuCode("Employee Management");
			auditRecord.setSubMenuCode("Performance");
			auditRecord.setActivityCode("CREATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/selfAppraisalList";
	}

	/* Employee Self Appraisal Home Page */
	@GetMapping("/previousAppraisal/{empId}/{date}")
	public String previousAppraisal(@ModelAttribute EmpKRA empKRA, @PathVariable String empId, @PathVariable Date date,
			Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			Optional<EmpKRA> optional = empKRAService.findByempId(empId);
			if (optional.isPresent()) {
				EmpKRA empKRA2 = optional.get();
				model.addAttribute("empKRA", empKRA2);
			}

			EmpBasicDetails userDetails = null;
			if (empId != null) {
				userDetails = empRegistartionService.getCurrentUser(empId);
				if (userDetails != null) {
					model.addAttribute("userDetail", userDetails);
					String depId = userDetails.getDepartName();
					System.out.println("DEPT.." + depId);

					EmpKRA empKRAList2 = empKRAService.getKRAWithDepIdAndEmpIdAndDate(depId, empId, date);
					if (Objects.nonNull(empKRAList2)) {
						model.addAttribute("kraList", empKRAList2);
					}
				} else {
					System.out.println("Employee data not get ::::::");
				}
			} else {
				System.out.println("Current employee is not present ::::::");
			}
		} catch (Exception e) {
			LOGGER.error("Error Ocuring while display previous appraisal page " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - view previous appraisal"));
			auditRecord.setMenuCode("Employee Management");
			auditRecord.setSubMenuCode("Performance");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "appraisal/previousAppraisal";
	}

	/*---*/

	/* Display List of Employee Appraisal By Manager */
	@GetMapping(value = "/empAppraisalByManager")
	public String empAppraisalByManager(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();

			EmpBasicDetails userDetails = null;
			if (empId != null) {
				userDetails = empRegistartionService.getCurrentUser(empId);
				if (userDetails != null) {
					String depId = userDetails.getDepartName();

					List<String> empIds = empKRAService.getTeammetsTeamMangerId2(empId, depId);
					if (Objects.nonNull(empIds)) {
						List<EmpKRA> empKRA = empKRAService.getEmpWithMangerIdOrReportingManager(empId, depId, empIds);
						if (Objects.nonNull(empKRA)) {
							model.addAttribute("appraisal", empKRA);
						}
					}
					List<EmpWorkDetailsDto> listOfEmpDetails = getListOfEmpDetails();
					if (Objects.nonNull(listOfEmpDetails)) {
						model.addAttribute("listOfEmpDetails", listOfEmpDetails);
					}
				} else {
					System.out.println("No Employee record Found ::::::::::");
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while display rating list to manager " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - appraisal rating list "));
			auditRecord.setMenuCode("Organization");
			auditRecord.setSubMenuCode("Appraisee View");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "appraisal/appraisalList";
	}

	/* Teammet Review Pages */
	@GetMapping(value = "/teammetReview/{empId}")
	public String teammetReview(@ModelAttribute EmpKRA empKRA, Model model, @PathVariable(name = "empId") String empId,
			Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		System.out.println("EMPID....." + empId);

		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();
			Optional<EmpKRA> optional = empKRAService.findByempId(empId);
			System.out.println("Get Current emp data....." + optional.get().getEmpId());

			if (optional.isPresent()) {
				EmpKRA empKRA2 = optional.get();
				if (Objects.nonNull(empKRA2)) {
					model.addAttribute("empKRA", empKRA2);
				}
				// if (Objects.nonNull(empId)) {

				EmpBasicDetails basicDetailsOfEmp = empRegistartionService.getCurrentUser(empId);
				if (Objects.nonNull(basicDetailsOfEmp)) {
					model.addAttribute("currentUser", basicDetailsOfEmp);
				}
				List<EmpKRA> appraisalList = empKRAService.appraisalCycleList(empId);
				if (Objects.nonNull(appraisalList)) {
					model.addAttribute("appraisalCycleList", appraisalList);
				}

				List<EmpKRA> empKRAList = empKRAService.getCurrentEmpAppraisal(empId);

				if (Objects.nonNull(empKRAList)) {
					model.addAttribute("appraisalList", empKRAList);
				}

				EmpBasicDetails empBasicDetails = empRegistartionService.getKRAWithDepIdAndEmpId(empKRA2.getDepId(),
						empId);
				String managerId = empBasicDetails.getEmpWorkDetails().getReportingManager();
				if (Objects.nonNull(managerId)) {
					EmpBasicDetails basicDetails = empRegistartionService.getCurrentUser(managerId);
					if (Objects.nonNull(basicDetails)) {
						model.addAttribute("managerDetails", basicDetails);
					}
				}
				EmpBasicDetails empBasicDetails2 = empRegistartionService.getKRAWithDepIdAndEmpId(empKRA2.getDepId(),
						managerId);
				String managerId2 = empBasicDetails2.getEmpWorkDetails().getReportingManager();
				if (Objects.nonNull(managerId2)) {
					EmpBasicDetails basicDetails2 = empRegistartionService.getCurrentUser(managerId2);
					if (Objects.nonNull(basicDetails2)) {
						model.addAttribute("managerDetails2", basicDetails2);
					}
				}
				if (Objects.nonNull(empId)) {
					model.addAttribute("empId", empId);
				}

				LocalDate currentdate = LocalDate.now();
				String month = currentdate.format(DateTimeFormatter.ofPattern(ProdConstant.MONTHFORMAT));
				String year = currentdate.format(DateTimeFormatter.ofPattern(ProdConstant.YEARFORMAT));
				int nextYear = currentdate.getYear() + 1;

				if ("10".equals(month) || "11".equals(month) || "12".equals(month) || "01".equals(month)
						|| "02".equals(month) || "03".equals(month)) {
					model.addAttribute("month", "Appraisal Cycle October " + year + " to March " + nextYear);
				} else {
					System.out.println("Else Month ::::: " + month);
					model.addAttribute("month", "Appraisal Cycle April " + year + " to September " + year);
				}
			} else {
				System.out.println("Current empId is not present");
			}
			// }
		} catch (Exception e) {
			LOGGER.error("Error occuring while give rating by manager to employee " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - teammates review"));
			auditRecord.setMenuCode("Organization");
			auditRecord.setSubMenuCode("Appraisee View");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "appraisal/teammetReview";
	}

	/* Leave 1 Rating Home Page */
	@GetMapping(value = "/ratingByReportingManager/{empId}")
	public String ratingByReportingManager(@ModelAttribute EmpKRA empKRA, Model model,
			@PathVariable(name = "empId") String empId, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			Optional<EmpKRA> optional = empKRAService.findByempId(empId);
			if (optional.isPresent()) {
				EmpKRA empKRA2 = optional.get();
				if (Objects.nonNull(empKRA2)) {
					model.addAttribute("empKRA", empKRA2);
				}
			}

			EmpBasicDetails userDetails = null;
			if (empId != null) {
				userDetails = empRegistartionService.getCurrentUser(empId);
				if (userDetails != null) {
					model.addAttribute("userDetail", userDetails);
					String depId = userDetails.getDepartName();
					if (Objects.nonNull(depId)) {
						EmpKRA empKRAList = empKRAService.getKRAWithDepIdAndEmpId(depId, empId);
						if (Objects.nonNull(empKRAList)) {
							model.addAttribute("kraList", empKRAList);
						}
					}
					EmpBasicDetails managerList = empRegistartionService.getKRAWithDepIdAndEmpId(depId,
							userDetails.getEmpWorkDetails().getReportingManager());
					if (Objects.nonNull(managerList)) {
						model.addAttribute("managerList", managerList);
					}
				}
			}

			LocalDate currentdate = LocalDate.now();
			String month = currentdate.format(DateTimeFormatter.ofPattern(ProdConstant.MONTHFORMAT));
			String year = currentdate.format(DateTimeFormatter.ofPattern(ProdConstant.YEARFORMAT));
			int nextYear = currentdate.getYear() + 1;

			if ("10".equals(month) || "11".equals(month) || "12".equals(month) || "01".equals(month)
					|| "02".equals(month) || "03".equals(month)) {
				model.addAttribute("month", "Appraisal Cycle October " + year + " to March " + nextYear);
			} else {
				System.out.println("Else Month ::::: " + month);
				model.addAttribute("month", "Appraisal Cycle April " + year + " to September " + year);
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while give rating by manager to employee " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - level 1 appraisal rating home page"));
			auditRecord.setMenuCode("Organization");
			auditRecord.setSubMenuCode("Appraisee View");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "appraisal/ratingByReportingManager";
	}

	/* Leave 1 Rating by Manager Save & Save Draft */
	@PostMapping(value = "/saveRatingByReportingManager")
	public String saveRatingByReportingManager(@ModelAttribute EmpKRA empKRA, Model model, Device device,
			RedirectAttributes redirAttrs, @RequestParam(value = "action", required = true) String action)
			throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		EmailTemplate emailTemplate = emailService.getEmailTemplate(ProdConstant.TRUE,
				ProdConstant.RATINGBYREPORTINGMANAGER);
		try {
			if (action.equals("saveDraft")) {
				empKRAService.saveDraftRatingByReportingManager(empKRA);
			}
			if (action.equals("save")) {
				empKRAService.saveRatingByReportingManager(empKRA);

				/* Rating by Reporting Manager to employee */
				List<EmpKRA> empKRAList = null;
				if (Objects.nonNull(empKRA)) {
					empKRAList = empKRAService.getCurrentEmpAppraisal(empKRA.getEmpId());
					if (Objects.nonNull(empKRAList)) {
						Context context = new Context();
						context.setVariable("empKRAList", empKRAList);
						String body = templateEngine.process("appraisal/ratingByRMMail", context);
						String from = emailTemplate.getEmailFrom();
						String to = emailTemplate.getEmailTo();
						String subject = emailTemplate.getEmailSub();
						String cc = emailTemplate.getEmailCc();

						emailAndOTPService.emailSend(from, to, subject, body, cc);
						redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, emailSuccess);
					} else {
						System.out.println("Nothing Happen ::::::");
					}
				} else {
					System.out.println("Nothing Happen ::::::");
				}
			}
			redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
		} catch (Exception e) {
			LOGGER.error(
					"Error occuring while save rating by manager to employee ... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - create level 1 appraisal rating"));
			auditRecord.setMenuCode("Organization");
			auditRecord.setSubMenuCode("Appraisee View");
			auditRecord.setActivityCode("CREATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/empAppraisalByManager";
	}

	/* Leave 2 Rating Home Page */
	@GetMapping(value = "/ratingByReportingManager2/{empId}")
	public String ratingByReportingManager2(@ModelAttribute EmpKRA empKRA, @PathVariable(name = "empId") String empId,
			Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			Optional<EmpKRA> optional = empKRAService.findByempId(empId);
			if (optional.isPresent()) {
				EmpKRA empKRA2 = optional.get();
				if (Objects.nonNull(empKRA2)) {
					model.addAttribute("empKRA", empKRA2);
				}
			}
			EmpBasicDetails userDetails = null;
			if (empId != null) {
				userDetails = empRegistartionService.getCurrentUser(empId);
				if (userDetails != null) {
					model.addAttribute("userDetail", userDetails);
					String depId = userDetails.getDepartName();
					String managerId = userDetails.getEmpWorkDetails().getReportingManager();

					EmpKRA empKRAList = empKRAService.getManagerIdWithMangerIdWithDepId(managerId, depId, empId);
					model.addAttribute("kraList", empKRAList);

					EmpBasicDetails managerList = empRegistartionService.getKRAWithDepIdAndEmpId(depId,
							userDetails.getEmpWorkDetails().getReportingManager());
					model.addAttribute("managerList", managerList);

					String seniorManger = managerList.getEmpWorkDetails().getReportingManager();
					EmpBasicDetails managerList2 = empRegistartionService.getKRAWithDepIdAndEmpId(depId, seniorManger);
					model.addAttribute("managerList2", managerList2);
				}
			}

			LocalDate currentdate = LocalDate.now();
			String month = currentdate.format(DateTimeFormatter.ofPattern(ProdConstant.MONTHFORMAT));
			String year = currentdate.format(DateTimeFormatter.ofPattern(ProdConstant.YEARFORMAT));
			int nextYear = currentdate.getYear() + 1;

			if ("10".equals(month) || "11".equals(month) || "12".equals(month) || "01".equals(month)
					|| "02".equals(month) || "03".equals(month)) {
				model.addAttribute("month", "Appraisal Cycle October " + year + " to March " + nextYear);
			} else {
				System.out.println("Else Month ::::: " + month);
				model.addAttribute("month", "Appraisal Cycle April " + year + " to September " + year);
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occuring while give rating by super manager to employee " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - level 2 appraisal rating home page"));
			auditRecord.setMenuCode("Organization");
			auditRecord.setSubMenuCode("Appraisee View");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "appraisal/ratingByReportingManager2";
	}

	/* Leave 2 Rating by Senior Manager Save & Save Draft */
	@PostMapping(value = "/saveRatingByReportingManager2")
	public String saveRatingByReportingManager2(@ModelAttribute EmpKRA empKRA, Model model, Device device,
			RedirectAttributes redirAttrs, @RequestParam(value = "action", required = true) String action)
			throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		EmailTemplate emailTemplate = emailService.getEmailTemplate(ProdConstant.TRUE, ProdConstant.RATINGBYMANAGER);
		try {

			if (action.equals("saveDraft")) {
				empKRAService.saveDraftRatingByReportingManager2(empKRA);
			}
			if (action.equals("save")) {
				empKRAService.saveRatingByReportingManager2(empKRA);

				/* Rating by Manager to employee */
				List<EmpKRA> empKRAList = null;
				if (Objects.nonNull(empKRA)) {
					empKRAList = empKRAService.getCurrentEmpAppraisal(empKRA.getEmpId());
					if (Objects.nonNull(empKRAList)) {
						Context context = new Context();
						context.setVariable("empKRAList", empKRAList);
						String body = templateEngine.process("appraisal/ratingByManagerMail", context);
						String from = emailTemplate.getEmailFrom();
						String to = emailTemplate.getEmailTo();
						String subject = emailTemplate.getEmailSub();
						String cc = emailTemplate.getEmailCc();

						emailAndOTPService.emailSend(from, to, subject, body, cc);
						redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, emailSuccess);
					} else {
						System.out.println("Nothing Happen ::::::");
					}
				} else {
					System.out.println("Nothing Happen ::::::");
				}
			}
			redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
		} catch (Exception e) {
			LOGGER.error(
					"Error occuring while save rating by manager to employee ... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - create level 2 appraisal rating"));
			auditRecord.setMenuCode("Organization");
			auditRecord.setSubMenuCode("Appraisee View");
			auditRecord.setActivityCode("CREATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/empAppraisalByManager";
	}
}