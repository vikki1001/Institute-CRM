package com.ksv.ktrccrm.controller;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.EmailTemplate;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.ExpenseReimb;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.EmailAndOTPService;
import com.ksv.ktrccrm.service.EmailService;
import com.ksv.ktrccrm.service.EmpRegistartionService;
import com.ksv.ktrccrm.service.ExpenseReimbService;
import com.ksv.ktrccrm.service.UserService;

@Controller
public class ExpenseReimbController {
	private static final Logger LOGGER = LogManager.getLogger(ExpenseReimbController.class);

	@Autowired
	private TemplateEngine templateEngine;

	public ExpenseReimbController(TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

	@Autowired
	private EmailService emailService;

	@Autowired
	private EmailAndOTPService emailAndOTPService;

	@Autowired
	private ExpenseReimbService expenseReimbService;

	@Autowired
	private AuditRecordService auditRecordService;

	@Autowired
	private EmpRegistartionService empRegistartionService;

	@Autowired
	private UserService userService;

	@Value("${global.redirectmsg.save}")
	private String successSaveMsg;

	@Value("${global.redirect.update}")
	private String successUpdateMsg;

	@Value("${global.redirect.saveDraft}")
	private String successSaveDraft;

	@Value("${global.redirecrmsg.emailSend}")
	private String emailSuccess;

	/* List of Active Expense Reimbursement */
	@GetMapping(value = "/activeExpenseReimbList")
	public String activeExpenseReimbList(Model model, Device device) throws Exception {
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
					empId = authentication.getName();
					List<ExpenseReimb> userDetails = null;
					if (empId != null) {
						userDetails = expenseReimbService.getRecordList(empId, ProdConstant.TRUE, tenantId);
						if (userDetails != null) {
							model.addAttribute("expenseList", userDetails);

							return "expenseReimb/activeExpenseList";
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur to display active expense list... " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed active expense list"));
			auditRecord.setMenuCode("Employee Management");
			auditRecord.setSubMenuCode("Expense Reimb");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "expenseReimb/activeExpenseList";
	}

	/* List of Cancel Expense Reimbursement */
	@GetMapping(value = "/cancelExpenseReimbList")
	public String cancelExpenseReimbList(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId = null;
		String tenantId = null;
		UserMst user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			empId = authentication.getName();
			List<ExpenseReimb> userDetails = null;
			if (empId != null) {
				user = userService.getUserById(empId);
				tenantId = user.getTenantId();
				if (Objects.nonNull(tenantId)) {
					userDetails = expenseReimbService.getRecordList(empId, ProdConstant.FALSE, tenantId);
					if (userDetails != null) {
						model.addAttribute("expenseList", userDetails);
						return "expenseReimb/cancelExpenseList";
					}
				}
			}

		} catch (Exception e) {
			LOGGER.error("Error occur to display cancel expense list... " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed active expense list"));
			auditRecord.setMenuCode("Employee Management");
			auditRecord.setSubMenuCode("Expense Reimb");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "expenseReimb/cancelExpenseList";
	}

	/* Expense Reimb Apply Mail Send Admin */
	public void sendMailExpenseReimb(RedirectAttributes redirAttrs) throws Exception {
		String empId;
		EmailTemplate emailTemplate = emailService.getEmailTemplate(ProdConstant.TRUE,
				ProdConstant.EXPENSEREIMB_REQUEST);
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();
			List<ExpenseReimb> expenseReimb = null;
			if (empId != null) {
				expenseReimb = expenseReimbService.getExpenseReimbByEmpId(empId);
				if (expenseReimb != null) {
					if (emailTemplate != null) {
						Context context = new Context();
						context.setVariable("expenseReimbList", expenseReimb);
						String body = templateEngine.process("expenseReimb/expenseReimbMail", context);

						String from = emailTemplate.getEmailFrom();
						String to = emailTemplate.getEmailTo();
						String subject = emailTemplate.getEmailSub();
						String cc = emailTemplate.getEmailCc();

						System.out.println(" Your Expense reimb Mail Sent ");
						emailAndOTPService.emailSend(from, to, subject, body, cc);
						redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, emailSuccess);
					}
				} else {
					System.out.println("Nothing Happen ::::::");
				}
			} else {
				System.out.println("Nothing Happen ::::::");
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occurring while sending emails to expense reimb ..... " + ExceptionUtils.getStackTrace(e));
		}
	}

	/* Expense Reimbursement Registration Page */
	@GetMapping(value = "/expenseReimbHome")
	public String expenseReimbHome(@ModelAttribute ExpenseReimb expenseReimb, BindingResult result, Model model,
			Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();
			EmpBasicDetails userDetails = null;
			if (empId != null) {
				userDetails = empRegistartionService.getCurrentUser(empId);
				if (userDetails != null) {
					model.addAttribute("expenseList", userDetails);
				} else {
					return "expenseReimb/activeExpenseList";
				}
			} else {
				return "expenseReimb/activeExpenseList";
			}
		} catch (Exception e) {
			LOGGER.error("Error occur to get expense page... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed active expense reimb list"));
			auditRecord.setMenuCode("Employee Management");
			auditRecord.setSubMenuCode("Expense Reimb");
			auditRecord.setActivityCode("VIEW");

			auditRecordService.save(auditRecord, device);
		}
		return "expenseReimb/expenseReimb";
	}

	/* Save and save draft Expense Reimbursement Data */
	@PostMapping(value = "/saveExpenseReimb")
	public String saveExpenseReimb(@ModelAttribute ExpenseReimb expenseReimb, BindingResult result, Model model,
			@RequestParam("attachment") MultipartFile file, RedirectAttributes redirAttrs,
			@RequestParam(value = "action", required = true) String action, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();
			EmpBasicDetails userDetails = null;
			if (empId != null) {
				userDetails = empRegistartionService.getCurrentUser(empId);
				if (userDetails != null) {
					model.addAttribute("expenseList", userDetails);
				}
			}

			if (action.equals("saveDraft")) {
				System.out.println("SAVE DRAFT ::::::::");
				expenseReimb.setAttachment(file.getBytes());
				expenseReimbService.saveDraftExpenseReimb(expenseReimb);
				redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveDraft);
				return "redirect:/activeExpenseReimbList";
			} else {
				System.out.println("expenseReimb save draft error :::::::::::::::::: " + expenseReimb);
			}

			if (action.equals("save")) {
				System.out.println("SAVE ::::::::");
				expenseReimb.setAttachment(file.getBytes());
				expenseReimbService.saveExpenseReimb(expenseReimb);
				sendMailExpenseReimb(redirAttrs);
				redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
				return "redirect:/activeExpenseReimbList";
			} else {
				System.out.println("expenseReimb save error :::::::::::::::::: " + expenseReimb);
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while save expense Reimbursement successfully... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - create expense successfully"));
			auditRecord.setMenuCode("Employee Management");
			auditRecord.setSubMenuCode("Expense Reimb");
			auditRecord.setActivityCode("CREATE");
			auditRecordService.save(auditRecord, device);
		}
		return "expenseReimb/expenseReimb";

	}

	/* Update Registered Expense Reimbursement by Employee */
	@GetMapping(value = "/updateExpenseReimb/{id}")
	public String updateExpenseReimb(@PathVariable(name = "id") Long id, Device device, Model model) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String userId = null;
		String tenantId = null;
		UserMst user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			userId = authentication.getName();
			if (Objects.nonNull(userId)) {
				user = userService.getUserById(userId);
				tenantId = user.getTenantId();
				if (Objects.nonNull(tenantId)) {
					ExpenseReimb expenseReimbById = expenseReimbService.getExpenseById(id);
					if (Objects.nonNull(expenseReimbById)) {
						model.addAttribute("expenseReimb", expenseReimbById);
					}
				} else {
					System.out.println("tenantId is null  ");
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while update expense.. " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - update expense by id " + id));
			auditRecord.setMenuCode("Employee Management");
			auditRecord.setSubMenuCode("Expense Reimb");
			auditRecord.setActivityCode("UPDATE");
			auditRecordService.save(auditRecord, device);
		}
		return "expenseReimb/expenseReimb";
	}

	/* Cancel Registered Expense Reimbursement by Employee */
	@GetMapping(value = "/cancelExpenseReimb/{id}")
	public String deactiveExpense(ExpenseReimb expenseReimb, @PathVariable(name = "id") String empId, Device device)
			throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			expenseReimbService.getCancelById(expenseReimb);
		} catch (Exception e) {
			LOGGER.error("Error occur while cancel expense.. " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - cancel expense by id " + empId));
			auditRecord.setMenuCode("Employee Management");
			auditRecord.setSubMenuCode("Expense Reimb");
			auditRecord.setActivityCode("CANCEL");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeExpenseReimbList";
	}

	/* Display List of Expense Reimbursement to Manager */
	@GetMapping(value = "/appliedExpenseReimbList")
	public String appliedExpenseReimbList(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String userId = null;
		String tenantId = null;
		UserMst user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			userId = authentication.getName();
			if (Objects.nonNull(userId)) {
				user = userService.getUserById(userId);
				tenantId = user.getTenantId();
				if (Objects.nonNull(tenantId)) {
					List<EmpBasicDetails> empDetails = empRegistartionService.getRecordList(ProdConstant.TRUE,
							tenantId);
					if (Objects.nonNull(empDetails)) {
						model.addAttribute("expenseReimbList", empDetails);
					}
				} else {
					System.out.println("tenantId is null__ ");
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display applied expense reimb list ... " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed apply emp list"));
			auditRecord.setMenuCode("Employee Management");
			auditRecord.setSubMenuCode("Expense Reimb");
			auditRecord.setActivityCode("Applied Expense Reimb List");
			auditRecordService.save(auditRecord, device);
		}
		return "expenseReimb/empExpenseList";
	}

	@GetMapping("/getEmpId/{empId}")
	public String getEmpByEmpId(@PathVariable(name = "empId") String empId, Model model, Device device)
			throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			List<ExpenseReimb> userDetails = null;
			if (empId != null) {
				userDetails = expenseReimbService.getEmpDetails(empId);
				if (userDetails != null) {
					model.addAttribute("expenseReimbList", userDetails);
				} else {
					System.out.println(" UserDetails Is Null :::::::::::: ");
				}
			} else {
				System.out.println(" Employee Id is Null ::::::::::::::::");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get emp by empId " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed emp by empId"));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Leave Bucket");
			auditRecord.setActivityCode("Employees Leave");
			auditRecordService.save(auditRecord, device);
		}
		return "expenseReimb/appliedExpenseList";
	}

	/* Expense Reimbursement Request Approved by Manager, Mail Send */
	public void approvedExpenseReimb(RedirectAttributes redirAttrs) throws Exception {
		String empId;
		EmailTemplate emailTemplate = emailService.getEmailTemplate(ProdConstant.TRUE,
				ProdConstant.EXPENSEREIMB_APPROVED);
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();
			List<ExpenseReimb> expenseReimb = null;
			if (empId != null) {
				expenseReimb = expenseReimbService.getExpenseReimbByEmpId(empId);
				if (emailTemplate != null) {
					Context context = new Context();
					context.setVariable("approvedExpenseReimbList", expenseReimb);
					String body = templateEngine.process("expenseReimb/approvedExpenseReimbMail", context);

					String from = emailTemplate.getEmailFrom();
					String to = emailTemplate.getEmailTo();
					String subject = emailTemplate.getEmailSub();
					String cc = emailTemplate.getEmailCc();

					System.out.println(" Expense Reimb Request Approved ");
					emailAndOTPService.emailSend(from, to, subject, body, cc);
					redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, emailSuccess);
				} else {
					System.out.println("Nothing Happen ::::::");
				}
			} else {
				System.out.println("Nothing Happen ::::::");
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occurring while sending mail to employee who ExpenseReimb request approved by manager..... "
							+ ExceptionUtils.getStackTrace(e));
		}
	}

	/* Accept Expense Reimbursement by admin */
	@GetMapping(value = "/acceptExpenseReimb/{id}")
	public String acceptExpenseReimb(@ModelAttribute ExpenseReimb expenseReimb, @PathVariable(name = "id") Long id,
			RedirectAttributes redirAttrs, Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			if (Objects.nonNull(expenseReimb)) {
				String status = "Approved";
				String flag = "Y";
				expenseReimb.setStatus(status);
				expenseReimb.setFlag(flag);
				expenseReimbService.acceptStatus(status, flag, id);

				approvedExpenseReimb(redirAttrs);
			} else {
				System.out.println("Nothing Happen ::::::");
				return "redirect:/appliedExitActivityList";
			}
			List<ExpenseReimb> expenseList = expenseReimbService.acceptExpenseReimbById(id);
			if (Objects.nonNull(expenseList)) {
				model.addAttribute("expenseReimbList", expenseList);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while accept Expense Reimb by admin ... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - approved expense of id - " + id));
			auditRecord.setMenuCode("Employee Management");
			auditRecord.setSubMenuCode("Expense Reimb");
			auditRecord.setActivityCode("Approved Expense Reimb");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/appliedExpenseReimbList";
	}

	/* Expense Reimbursement Request Approved by Admin, Mail Send */
	public void rejectExpenseReimb(RedirectAttributes redirAttrs) throws Exception {
		String empId;
		EmailTemplate emailTemplate = emailService.getEmailTemplate(ProdConstant.TRUE,
				ProdConstant.EXPENSEREIMB_REJECT);
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();
			List<ExpenseReimb> expenseReimb = null;
			if (empId != null) {
				expenseReimb = expenseReimbService.getExpenseReimbByEmpId(empId);
				if (expenseReimb != null) {
					if (emailTemplate != null) {
						Context context = new Context();
						context.setVariable("rejectExpenseReimbList", expenseReimb);
						String body = templateEngine.process("expenseReimb/rejectExpenseReimbMail", context);

						String from = emailTemplate.getEmailFrom();
						String to = emailTemplate.getEmailTo();
						String subject = emailTemplate.getEmailSub();
						String cc = emailTemplate.getEmailCc();

						System.out.println(" Expense Reimb Reject ");
						emailAndOTPService.emailSend(from, to, subject, body, cc);
						redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, emailSuccess);
					}
				} else {
					System.out.println("Nothing Happen ::::::");
				}
			} else {
				System.out.println("Nothing Happen ::::::");
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occurring while sending mail to employee who ExpenseReimb request reject by manager..... "
							+ ExceptionUtils.getStackTrace(e));
		}
	}

	/* Reject Expense Reimbursement by admin */
	@GetMapping(value = "/rejectExpenseReimb/{id}")
	public String rejectExpenseReimb(@ModelAttribute ExpenseReimb expenseReimb, @PathVariable(name = "id") Long id,
			RedirectAttributes redirAttrs, Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			if (Objects.nonNull(expenseReimb)) {
				String status = "Reject";
				String flag = "Y";
				expenseReimb.setStatus(status);
				expenseReimb.setFlag(flag);
				expenseReimbService.acceptStatus(status, flag, id);

				rejectExpenseReimb(redirAttrs);
			} else {
				System.out.println("Nothing Happen ::::::");
				return "redirect:/appliedExitActivityList";
			}

			List<ExpenseReimb> expenseList = expenseReimbService.acceptExpenseReimbById(id);
			if (Objects.nonNull(expenseList)) {
				model.addAttribute("expenseReimbList", expenseList);
			}

		} catch (Exception e) {
			LOGGER.error("Error occur while reject Expense Reimb by admin ... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - reject expense of id - " + id));
			auditRecord.setMenuCode("Employee Management");
			auditRecord.setSubMenuCode("Expense Reimb");
			auditRecord.setActivityCode("Reject Expense Reimb");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/appliedExpenseReimbList";
	}

	/* Display Attachment(Image) */
	@GetMapping(value = "/attachment")
	public void showattachment(@RequestParam("id") Long id, HttpServletResponse response) throws Exception {
		try {
			ExpenseReimb expenseReimb = expenseReimbService.getAttachment(id);
			if (expenseReimb != null) {
				response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
				response.getOutputStream().write(expenseReimb.getAttachment());
				response.getOutputStream().flush();
				response.getOutputStream().close();
			} else {
				System.out.println("attachment not get....");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display attachment  " + ExceptionUtils.getStackTrace(e));
		}
	}
}