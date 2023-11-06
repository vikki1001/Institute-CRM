package com.ksv.ktrccrm.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.DepartmentDao;
import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.DepartmentMst;
import com.ksv.ktrccrm.db1.entities.EmailTemplate;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.EmpPersonalDetails;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.EmailAndOTPService;
import com.ksv.ktrccrm.service.EmailService;
import com.ksv.ktrccrm.service.EmpRegistartionService;
import com.ksv.ktrccrm.service.UserService;

@Controller
public class EmailTemplateController {
	private static final Logger LOGGER = LogManager.getLogger(EmailTemplateController.class);

	@Autowired
	private EmailService emailService;
	@Autowired
	private AuditRecordService auditRecordService;
	@Autowired
	private EmpRegistartionService empRegistartionService;
	@Autowired
	private TemplateEngine templateEngine;
	@Autowired
	private EmailAndOTPService emailAndOTPService;
	@Autowired
	private DepartmentDao departmentDao;
	@Autowired 
	private UserService userService;

	@Value("${global.redirectmsg.save}")
	private String successSaveMsg;

	@Value("${global.redirect.update}")
	private String successUpdateMsg;

	@Value("${global.redirecrmsg.emailSend}")
	private String emailSuccess;

	/* Display List of IsActive Email */
	@GetMapping(value = "/activeEmailList")
	public String getIsActiveEmail(Model model, Device device) throws Exception {
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
					List<EmailTemplate> emailList = emailService.getRecordList(ProdConstant.TRUE, tenantId);
					if (Objects.nonNull(emailList)) {
						model.addAttribute("listEmail", emailList);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur to display active email list... " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" viewed active email list"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Email Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "email/activeemaillist";
	}

	/* Display List of Active */
	@GetMapping(value = "/inActiveEmailList")
	public String getInActiveEmail(Model model, Device device) throws Exception {
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
					if (Objects.nonNull(empId)) {
						user = userService.getUserById(empId);
						tenantId = user.getTenantId();
						if (Objects.nonNull(tenantId)) {
							List<EmailTemplate> emailList = emailService.getRecordList(ProdConstant.FALSE, tenantId);
							if (Objects.nonNull(emailList)) {
								model.addAttribute("listEmail", emailList);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur to display inActive email list... " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails_1 = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails_1.getUsername().concat(" - viewed inActive email list"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Email Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "email/inactiveemaillist";
	}

	// @GetMapping(value = "/emailTemplate")
	@RequestMapping(value = { "/emailTemplate", "/emailUpdate/{id}" }, method = RequestMethod.GET)
	public String emailTemplate(@PathVariable(name = "id", required = false) Long id, Model model, Device device)
			throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		EmailTemplate emailTemplate = new EmailTemplate();
		String empId = null;
		String tenantId = null;
		UserMst user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			if (id != null) {
				emailTemplate = emailService.getById(id);
				if (Objects.nonNull(emailTemplate)) {
					model.addAttribute("emailAttribute", emailTemplate);
				}
			} else {
				model.addAttribute("emailAttribute", emailTemplate);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur to display email registration page... " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed email management form"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Email Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "email/newemailtemplate";
	}

	@PostMapping(value = "/saveEmail")
	public String successEmail(@ModelAttribute("emailAttribute") EmailTemplate emailTemplate, BindingResult result,
			Model model, Device device, RedirectAttributes redirAttrs) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			if (result.hasErrors()) {
				List<ObjectError> allErrors = result.getAllErrors();
				for (ObjectError temp : allErrors) {
					System.out.println(temp);
				}
				return "email/newemailtemplate";
			} else {
				/* Save Email to Database */
				if (Objects.nonNull(emailTemplate)) {
					emailService.save(emailTemplate);
					redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
				}
				return "redirect:/activeEmailList";
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while email registration successfully... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - create new email"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Email Management");
			auditRecord.setActivityCode("CREATE");
			auditRecordService.save(auditRecord, device);
		}

		return "email/newemailtemplate";
	}

	/* Deactivate of Registered Email */
	@GetMapping(value = "/deactiveEmail/{id}")
	public String deactiveEmail(EmailTemplate emailTemplate, @PathVariable(name = "id") Long id, Device device)
			throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			emailService.emailEnableAndDisable(id);

		} catch (Exception e) {
			LOGGER.error("Error occur while deactivate email" + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - deactivate email id - " + id));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Email Management");
			auditRecord.setActivityCode("DEACTIVATE");
			auditRecordService.save(auditRecord, device);
		}

		return "redirect:/activeEmailList";
	}

	/* Activate of Registered Email */
	@GetMapping(value = "/activeEmail/{id}")
	public String activeEmail(EmailTemplate emailTemplate, @PathVariable(name = "id") Long id, Device device)
			throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			emailService.emailEnableAndDisable(id);

		} catch (Exception e) {
			LOGGER.error("Error occur while activate email" + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - activate email id - " + id));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Email Management");
			auditRecord.setActivityCode("ACTIVATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/inactiveemaillist";
	}

	@GetMapping(value = "/sendMailByAdminAndHr")
	public String homeAllRecord(@ModelAttribute EmpBasicDetails empBasicDetails, Model model, Device device)
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
					DepartmentMst department = new DepartmentMst();
					department.setDepartmentName(ProdConstant.ALL);
					List<DepartmentMst> deptName = departmentDao.getRecordList(ProdConstant.TRUE, tenantId);
					deptName.add(department);
					Collections.sort(deptName, DepartmentMst.Comparators.DEPT);
					model.addAttribute("allUsers", deptName);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while mail send by admin " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed audit record list"));
			auditRecord.setMenuCode("Audit Record");
			auditRecord.setSubMenuCode("Audit Listing Report");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "email/emailSendByAdminAndHr";
	}
 
	/* Send mail by Admin and HR */
	@GetMapping(value = "/sendMail")
	public String sendMailByAdmin(@ModelAttribute("empBasicDetails") EmpBasicDetails empBasicDetails,
			BindingResult result, RedirectAttributes redirAttrs, Device device) throws Exception {
		String userId = null;
		try {
			EmailTemplate emailTemplate = null;
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			userId = authentication.getName();

			EmpBasicDetails empBasicDetails2 = empRegistartionService.getCurrentUser(userId);
			String getCurrentUserName = empBasicDetails2.getFullName();

			if (ProdConstant.ADMIN.equalsIgnoreCase(getCurrentUserName)) {
				emailTemplate = emailService.getEmailTemplate(ProdConstant.TRUE, ProdConstant.ADMIN);
			} else if (ProdConstant.HR.equalsIgnoreCase(getCurrentUserName)) {
				emailTemplate = emailService.getEmailTemplate(ProdConstant.TRUE, ProdConstant.HR);
			}

			String departmentName;
			String grade;
			String description;
			String[] getMailIds;
			if (Objects.nonNull(empBasicDetails) && !ObjectUtils.isEmpty(empBasicDetails)) {

				departmentName = empBasicDetails.getDepartName();
				grade = empBasicDetails.getGrade();
				description = empBasicDetails.getDescription();

				if (Objects.nonNull(emailTemplate) && !ObjectUtils.isEmpty(emailTemplate)) {
					getMailIds = empRegistartionService.getMailIdForSendMailByAdmin(departmentName, grade);
					for (int i = 0; i < getMailIds.length; i++) {
						System.out.println("Get Mail IDs ------------ " + getMailIds[i]);
					}
					List<EmpBasicDetails> employeesDetails = empRegistartionService
							.getAllEmpDataAccordingToMailIds(getMailIds);

					if (Objects.nonNull(employeesDetails)) {
						// Context context = new Context();
						for (int i = 0; i < employeesDetails.size(); i++) {
							String from = null;
							String subject = null;
							String to = null;
							String body = null;

							String emailId = employeesDetails.get(i).getCoEmailId();
							EmpBasicDetails userData = empRegistartionService.getEmpData(emailId);
							userData.setDescription(description);
							System.out.println("BODY::" + userData.getDescription());
							if (Objects.nonNull(userData)) {
								Context context = new Context();
								context.setVariable("Details", userData);
								body = templateEngine.process("email/emailSendByAdmin", context);
								from = emailTemplate.getEmailFrom();
								subject = emailTemplate.getEmailSub();
								to = emailId;
								emailAndOTPService.emailsend(from, to, subject, body);
								System.out.println("send mail successfully :::::::::::::");
								redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, emailSuccess);
							}
						}
					} else {
						System.out.println("employee detail is null");
					}
				} else {
					System.out.println("emailTemplate is null");
				}
			} else {
				System.out.println("Null Data Found ::::");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get department " + ExceptionUtils.getStackTrace(e));
		}
		return "redirect:/sendMailByAdminAndHr";
	}

	/* @Scheduled(cron = "${scheduling.job.cron.birthday}") */
	public void sendBirthdayMailToOtherEmp() throws Exception {
		EmailTemplate emailTemplate = emailService.getEmailTemplate(ProdConstant.TRUE, ProdConstant.BIRTHDAY);
		try {
			String userId = null;
			String tenantId = null;
			if (emailTemplate != null) {
				List<EmpPersonalDetails> empDetails = empRegistartionService.getEmpBirthdayTodayForMail();
				if (Objects.nonNull(empDetails)) {
					for (int i = 0; i < empDetails.size(); i++) {
						userId = empDetails.get(i).getEmpBasicDetails().getEmpId();
						System.out.println("userId......." + userId);
						tenantId = empDetails.get(i).getEmpBasicDetails().getTenantId();
						System.out.println("tenantId........" + tenantId);
					}
					List<EmpBasicDetails> getActiveData = empRegistartionService.getRecordList(ProdConstant.TRUE,
							tenantId);
					List<String> allUserId = new ArrayList<>();
					if (Objects.nonNull(getActiveData)) {
						for (int i = 0; i < getActiveData.size(); i++) {
							allUserId.add(getActiveData.get(i).getEmpId());
							System.out.println("ALL IDS" + getActiveData.get(i).getEmpId());
						}
						if (Objects.nonNull(getActiveData) && Objects.nonNull(allUserId)) {
							allUserId.remove(userId);

							String[] getBirthdayMailId = empRegistartionService.getBirthdayMailId(allUserId);

							if (Objects.nonNull(getBirthdayMailId)) {
								List<EmpBasicDetails> employeesDetails = empRegistartionService
										.getAllEmpDataAccordingToMailIds(getBirthdayMailId);

								if (Objects.nonNull(employeesDetails)) {

									for (int i = 0; i < employeesDetails.size(); i++) {

										String from = null;
										String subject = null;
										String to = null;
										String body = null;

										String emailId = employeesDetails.get(i).getCoEmailId();

										System.out.println("Mail share these ids..." + emailId);

										EmpBasicDetails userData = empRegistartionService.getEmpData(emailId);
										if (Objects.nonNull(userData)) {

											Context context = new Context();
											context.setVariable("userList", userData);
											body = templateEngine.process("email/birthdayMailTemplate", context);
											from = emailTemplate.getEmailFrom();
											subject = emailTemplate.getEmailSub();
											to = emailId;
											emailAndOTPService.emailsend(from, to, subject, body);
										}
									}

								}
							}
						}
					}
				}
			} else {
				System.out.println("mailTamplate not find..");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while birthday mail trigger... " + ExceptionUtils.getStackTrace(e));
		}
	}

	/* @Scheduled(cron = "${scheduling.job.cron.birthday}") */
	public void  sendMailToBirthdayEmp() {
		try {
			EmailTemplate emailTemplate = emailService.getEmailTemplate(ProdConstant.TRUE, ProdConstant.BIRTHDAY);
			if (emailTemplate != null) {
				System.out.println("EmailTemplate......." + emailTemplate.getTemplateName());
				List<String> userId = empRegistartionService.getUserIdWhoseBirthdayToday();

				if (Objects.nonNull(userId)) {
					String[] getBirthdayMailId = empRegistartionService.getBirthdayMailId(userId);

					if (Objects.nonNull(getBirthdayMailId)) {

						List<EmpBasicDetails> employeesDetails = empRegistartionService
								.getAllEmpDataAccordingToMailIds(getBirthdayMailId);

						for (int i = 0; i < employeesDetails.size(); i++) {
							String from = null;
							String to = null;
							String subject = null;
							String body = null;

							String coEmailId = employeesDetails.get(i).getCoEmailId();
							EmpBasicDetails userData = empRegistartionService.getEmpData(coEmailId);
							Context context = new Context();
							context.setVariable("userList", userData);
							body = templateEngine.process("email/sendMailToBirthdayEmp", context);
							from = emailTemplate.getEmailFrom();
							subject = emailTemplate.getEmailSub();
							to = coEmailId;
							emailAndOTPService.emailsend(from, to, subject, body);
							System.out.println("Send mail successfully.......");
						}
					}

				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while birthday mail trigger... " + ExceptionUtils.getStackTrace(e));
		}
		

	}
}
