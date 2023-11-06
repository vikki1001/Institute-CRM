package com.ksv.ktrccrm.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.mobile.device.Device;
import org.springframework.scheduling.annotation.Scheduled;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ksv.ktrccrm.config.SmsServiceConfiguration;
import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.impl.CheckInCheckOutDaoImpl;
import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.CustomerContact;
import com.ksv.ktrccrm.db1.entities.Email;
import com.ksv.ktrccrm.db1.entities.EmailTemplate;
import com.ksv.ktrccrm.db1.entities.MailRecordStore;
import com.ksv.ktrccrm.db1.entities.OrganizationContact;
import com.ksv.ktrccrm.db1.entities.OrganizationEmployeeContact;
import com.ksv.ktrccrm.db1.entities.SMSRecordStore;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.CustomerContactService;
import com.ksv.ktrccrm.service.EmailAndOTPService;
import com.ksv.ktrccrm.service.EmailService;
import com.ksv.ktrccrm.service.MailRecordStoreService;
import com.ksv.ktrccrm.service.OrganizationContactService;
import com.ksv.ktrccrm.service.OrganizationEmployeeContactService;
import com.ksv.ktrccrm.service.SMSRecordStoreService;

@Controller
public class EmailAndSmsController {
	private static final Logger LOGGER = LogManager.getLogger(EmailAndSmsController.class);

	@Autowired
	private AuditRecordService auditRecordService;
	@Autowired
	private OrganizationContactService organizationContactService;
	@Autowired
	private CustomerContactService customerContactService;
	@Autowired
	private OrganizationEmployeeContactService oecService;
	@Autowired
	private EmailAndOTPService emailAndOTPService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private MailRecordStoreService mailRecordStoreService;
	@Autowired
	private SMSRecordStoreService smsRecordStoreService;

	@Autowired
	private SmsServiceConfiguration smsServiceConfiguration;
	@Autowired
	private SimpMessagingTemplate webSocket;

	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;

	/* FOR EMAIL */
	@GetMapping("/emailSendOrganization")
	public String emailSendOrganizationHome(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			OrganizationContact organizationContact = new OrganizationContact();
			model.addAttribute("organizationContact", organizationContact);
		} catch (Exception e) {
			LOGGER.error("Error occur to display email send organization page ... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed email send organization page"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Organization Email Send");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "emailAndSms/emailSendOrganization";
	}

	@ResponseBody
	@GetMapping("/subcategories/{categoryId}")
	public String getSubCategories(@PathVariable String categoryId, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			/* Organization Contact */
			OrganizationContact orgContact = new OrganizationContact();
			if (Objects.nonNull(orgContact)) {
				orgContact.setOrganizationName(ProdConstant.ALL.trim());
			}

			List<OrganizationContact> organizationContactList = organizationContactService
					.activeListOfOrganizationContact(ProdConstant.TRUE);
			if (Objects.nonNull(organizationContactList)) {
				organizationContactList.add(orgContact);
				Collections.sort(organizationContactList, OrganizationContact.Comparators.ORGANIZATIONNAME);
			}

			/* Customer Contact */
			CustomerContact customerContact = new CustomerContact();
			customerContact.setFirstName(ProdConstant.ALL.trim());
			customerContact.setLastName(ProdConstant.EMPTY.trim());
			customerContact.setBranch(ProdConstant.ALL.trim());

			List<CustomerContact> customerContactList = customerContactService
					.activeListOfCustomerContact(ProdConstant.TRUE);
			if (Objects.nonNull(customerContactList)) {
				customerContactList.add(customerContact);
				Collections.sort(customerContactList, CustomerContact.Comparators.CUSTOMERCONTACTNAME);
			}

			/* Organization Employee Contact */
			OrganizationEmployeeContact oec = new OrganizationEmployeeContact();
			oec.setFirstName(ProdConstant.ALL.trim());
			oec.setLastName(ProdConstant.EMPTY.trim());

			List<OrganizationEmployeeContact> oecList = oecService.activeListOfOEC(ProdConstant.TRUE);
			if (Objects.nonNull(oecList)) {
				oecList.add(oec);
				Collections.sort(oecList, OrganizationEmployeeContact.Comparators.ORGEMPCONTACTNAME);
			}

			if (Objects.equals(categoryId, ProdConstant.ORGANIZATION) && Objects.nonNull(categoryId)) {
				// Generate HTML options for sub-categories
				StringBuilder sb = new StringBuilder();
				for (OrganizationContact subCategory : organizationContactList) {
					sb.append("<option value=\"").append(subCategory.getOrganizationId()).append("\">")
							.append(subCategory.getOrganizationName()).append("</option>");
				}
				return sb.toString();

			} else if (Objects.equals(categoryId, ProdConstant.STUDENTS) && Objects.nonNull(categoryId)) {
				// Generate HTML options for sub-categories
				StringBuilder sb = new StringBuilder();
				for (CustomerContact subCategory : customerContactList) {
					sb.append("<option value=\"").append(subCategory.getCustomerId()).append("\">")
							.append(subCategory.getFirstLastName()).append("</option>");
				}
				return sb.toString();

			} else if (Objects.equals(categoryId, ProdConstant.STUDENTSBRANCH) && Objects.nonNull(categoryId)) {
				// Generate HTML options for sub-categories
				StringBuilder sb = new StringBuilder();
				for (CustomerContact subCategory : customerContactList) {
					sb.append("<option value=\"").append(subCategory.getBranch()).append("\">")
							.append(subCategory.getBranch()).append("</option>");
				}
				return sb.toString();

			} else if (Objects.equals(categoryId, ProdConstant.ORGANIZATIONEMPLOYEE) && Objects.nonNull(categoryId)) {
				// Generate HTML options for sub-categories
				StringBuilder sb = new StringBuilder();
				for (OrganizationEmployeeContact subCategory : oecList) {
					sb.append("<option value=\"").append(subCategory.getOrgEmpContactID()).append("\">")
							.append(subCategory.getFirstLastName()).append("</option>");
				}
				return sb.toString();
			}
		} catch (Exception e) {
			LOGGER.error("Error occur to display sub category list " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed sub category list"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Organization Email Send");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "";
	}

	@PostMapping("/emailSendOrganization")
	public String emailSendOrganization(@ModelAttribute("organizationContact") OrganizationContact organizationContact,
			@RequestParam(value = "category") String category, @RequestParam(value = "subCategory") String subCategory,
			@RequestParam(value = "description") String body, @RequestParam(value = "subject") String subject,
			BindingResult result, Model model, Device device, RedirectAttributes redirAttrs) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId = null;
		try {
			System.out.println("category -- " + category);
			System.out.println("subCategory -- " + subCategory);
			System.out.println("subject -- " + subject);
			System.out.println("description -- " + body);
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();
			
			OrganizationContact oc = new OrganizationContact();
			
			String cat1 = "College";
			String cat2 = "Students";
			String cat3 = "Students Branch";
			String cat4 = "College Employee";
			
			if(category.equals(cat1)) {
				System.out.println("go to organisation contact");
			}
			else if(category.equals(cat2)) {
				System.out.println("go to Student contact");
			}
			else if(category.equals(cat3)) {
				System.out.println("go to Student Branch contact");
			}
			else if(category.equals(cat4)) {
				System.out.println("go to Organisation Employee contact");
			}
			else {
				System.out.println("Error while check category");
			}
			
			List<OrganizationContact> orgContactList = organizationContactService.sendMailToCustomer(category,
					subCategory);

			System.out.println("ORGANIZATIONCONTACT :::::: " + orgContactList.toString());
			
			if (Objects.nonNull(orgContactList)) {
				for (int i = 0; i < orgContactList.size(); i++) {
	//				String toEmailIds = orgContactList.get(i).getCustomerContact().get(i).getEmailId();
	//				String customerId = orgContactList.get(i).getCustomerContact().get(i).getCustomerId();
					String organizationId = orgContactList.get(i).getOrganizationId();

					MailRecordStore mailRecordStore = new MailRecordStore();
	//				mailRecordStore.setCustomerId(customerId);
					mailRecordStore.setOrganizationId(organizationId);
	//				mailRecordStore.setToId(toEmailIds);
					mailRecordStore.setSubject(subject);
					mailRecordStore.setDescription(body);
					mailRecordStore.setCategory(category);
					mailRecordStore.setSubCategory(subCategory);
					mailRecordStore.setStatus(ProdConstant.PENDING);
					mailRecordStore.setCreatedDate(checkOutDaoImpl.getDateTime());
					mailRecordStore.setCreatedBy(empId);
					System.out.println("MAILRECORDSTORE :::::: " + mailRecordStore.toString());
					mailRecordStoreService.saveRecords(mailRecordStore);
				}
				redirAttrs.addFlashAttribute("success", "Email Send Successfully");
			} else {
				System.out.println("Organization Contact List is null :::::");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while save send email records ... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - send new email"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Organization Email Send");
			auditRecord.setActivityCode("SEND MAIL");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/emailSendOrganization";
	}

	// @Scheduled(cron = "${scheduling.job.cronEveryTenMin}")
	@GetMapping("/emailSendToCustomer")
	public @ResponseBody void emailSendToCustomer() {
		try {
			EmailTemplate emailTemplate = emailService.getEmailTemplate(ProdConstant.TRUE,
					ProdConstant.CUSTOMERCONTACTMAIL);

			if (Objects.nonNull(emailTemplate)) {
				String from = null;
				String to = null;
				String subject = null;
				String body = null;

				List<MailRecordStore> mailRecordStore = mailRecordStoreService.getPendingRecords(ProdConstant.PENDING);

				// creating some emails
				List<Email> emailList = new ArrayList<>();

				if (Objects.nonNull(mailRecordStore)) {
					for (int i = 0; i < mailRecordStore.size(); i++) {
						to = mailRecordStore.get(i).getToId();
						subject = mailRecordStore.get(i).getSubject();
						body = mailRecordStore.get(i).getDescription();
						from = emailTemplate.getEmailFrom();

						Email email1 = new Email(from, to, subject, body);
						emailList.add(email1);
					}

					// Emails are sending simultaneously
					ExecutorService executor = Executors.newFixedThreadPool(3);

					// executor.submit(new Email(from, to, subject, body));
					List<Email> myCallables = new ArrayList<>();

					boolean b = false;
					for (Email email : emailList) {
						b = myCallables
								.add(new Email(email.getFrom(), email.getTo(), email.getSubject(), email.getBody()));
						System.out.println("myCallables retun +++++ " + b);
					}
					executor.invokeAll(myCallables);
					executor.shutdown();

					for (int i = 0; i < mailRecordStore.size(); i++) {
						/* Store success/failed mail record */
						MailRecordStore mailRecord = mailRecordStoreService.getById(mailRecordStore.get(i).getId());

						if (b) {
							System.out.println("MAIL SEND SUCCESSFULLY :::: ");
							mailRecord.setFromId(from);
							mailRecord.setStatus(ProdConstant.SUCCESS);
						} else {
							System.out.println("MAIL FAILED SUCCESSFULLY :::: ");
							mailRecord.setFromId(from);
							mailRecord.setStatus(ProdConstant.FAILED);
						}

						List<MailRecordStore> smsRecordStoreList = new ArrayList<>();
						smsRecordStoreList.add(mailRecord);

						// mailRecordStoreService.saveAllRecords(smsRecordStoreList);
					}
					System.out.println("Completed email sending!");
				} else {
					System.out.println("Organization Contact List is null :::::");
				}
			} else {
				System.out.println("Email Template is null :::::");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while send email  ... " + ExceptionUtils.getStackTrace(e));
		}
	}

	/*
	 * public void emailSendToCustomer() { try { EmailTemplate emailTemplate =
	 * emailService.getEmailTemplate(ProdConstant.TRUE,
	 * ProdConstant.CUSTOMERCONTACTMAIL);
	 * 
	 * if (Objects.nonNull(emailTemplate)) { String from =
	 * emailTemplate.getEmailFrom(); String to; String subject; String body;
	 * 
	 * List<MailRecordStore> mailRecordStore =
	 * mailRecordStoreService.getPendingRecords(ProdConstant.PENDING);
	 * System.out.println("MAILRECORDSTORELIST :::::::: " +
	 * mailRecordStore.toString());
	 * 
	 * if (Objects.nonNull(mailRecordStore)) { for (int i = 0; i <
	 * mailRecordStore.size(); i++) {
	 * 
	 * to = mailRecordStore.get(i).getToId(); subject =
	 * mailRecordStore.get(i).getSubject(); body =
	 * mailRecordStore.get(i).getDescription();
	 * 
	 * System.out.println("Customers Email Id is :::::::: " + to);
	 * 
	 * boolean emailSend = emailAndOTPService.emailsend(from, to, subject, body);
	 * 
	 * Store success/failed mail record MailRecordStore mailRecord =
	 * mailRecordStoreService.getById(mailRecordStore.get(i).getId());
	 * 
	 * if (emailSend) { mailRecord.setFromId(from);
	 * mailRecord.setStatus(ProdConstant.SUCCESS);
	 * 
	 * } else { mailRecord.setFromId(from);
	 * mailRecord.setStatus(ProdConstant.FAILED); }
	 * 
	 * List<MailRecordStore> smsRecordStoreList = new ArrayList<>();
	 * smsRecordStoreList.add(mailRecord);
	 * 
	 * mailRecordStoreService.saveAllRecords(smsRecordStoreList); } } else {
	 * System.out.println("Organization Contact List is null :::::"); } } else {
	 * System.out.println("Email Template is null :::::"); } } catch (Exception e) {
	 * LOGGER.error("Error occur while send email  ... " +
	 * ExceptionUtils.getStackTrace(e)); }
	 * 
	 * }
	 */

	/* FOR SMS */

	@GetMapping("/smsSendOrganization")
	public String smsSendOrganizationHome(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			OrganizationContact organizationContact = new OrganizationContact();
			model.addAttribute("organizationContact", organizationContact);
		} catch (Exception e) {
			LOGGER.error("Error occur to display sms send organization page ... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed sms send organization page"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Organization SMS Send");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "emailAndSms/smsSendOrganization";
	}

	@PostMapping("/smsSendOrganization")
	public String smsSendOrganization(@ModelAttribute("organizationContact") OrganizationContact organizationContact,
			@RequestParam(value = "category") String category, @RequestParam(value = "subCategory") String subCategory,
			@RequestParam(value = "message") String message, BindingResult result, Model model, Device device,
			RedirectAttributes redirAttrs) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId = null;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();

			String toNo = null;

			List<OrganizationContact> orgContactList = organizationContactService.sendMailToCustomer(category,
					subCategory);

			if (Objects.nonNull(orgContactList)) {
				for (int i = 0; i < orgContactList.size(); i++) {

	//				String toMobileNo = orgContactList.get(i).getCustomerContact().get(i).getMobileNumber();

	//				toNo = "+91" + toMobileNo;
	//				String customerId = orgContactList.get(i).getCustomerContact().get(i).getCustomerId();
					String organizationId = orgContactList.get(i).getOrganizationId();

					System.out.println("Customers SMS Number is :::::::: " + toNo);

					/* Store success/failed mail record */
					SMSRecordStore smsRecordStore = new SMSRecordStore();
	//				smsRecordStore.setCustomerId(customerId);
					smsRecordStore.setOrganizationId(organizationId);
					smsRecordStore.setToNumber(toNo);
					smsRecordStore.setMessage(message);
					smsRecordStore.setCategory(category);
					smsRecordStore.setSubCategory(subCategory);
					smsRecordStore.setStatus(ProdConstant.PENDING);
					smsRecordStore.setCreatedDate(checkOutDaoImpl.getDateTime());
					smsRecordStore.setCreatedBy(empId);

					List<SMSRecordStore> smsRecordStoresList = new ArrayList<>();
					smsRecordStoresList.add(smsRecordStore);

					smsRecordStoreService.saveAllSMSRecords(smsRecordStoresList);

					redirAttrs.addFlashAttribute("success", "SMS Send Successfully");
				}
			} else {
				System.out.println("Organization Contact List is null :::::");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while send SMS ... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - send new sms"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Organization SMS Send");
			auditRecord.setActivityCode("SEND SMS");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/smsSendOrganization";
	}

	private final String TOPIC_DESTINATION = "/lesson/smsSendToCustomer";

	@Scheduled(cron = "${scheduling.job.cronEveryTenMin}")
	// @GetMapping("/smsSendToCustomer")
	public void smsSendToCustomer() {
		try {
			List<SMSRecordStore> smsRecordStoreList = smsRecordStoreService.getPendingRecords(ProdConstant.PENDING);
			System.out.println("SMSRECORDSTORELIST :::::::: " + smsRecordStoreList.toString());

			if (Objects.nonNull(smsRecordStoreList)) {
				for (int i = 0; i < smsRecordStoreList.size(); i++) {

					String toNo = smsRecordStoreList.get(i).getToNumber();
					String message = smsRecordStoreList.get(i).getMessage();

					System.out.println("Customers SMS Number & Message is :::::::: " + toNo + " & " + message);

					boolean smsSend = smsServiceConfiguration.sendSMS(toNo, message);

					/* Store success/failed mail record */
					SMSRecordStore smsRecordStore = smsRecordStoreService.getById(smsRecordStoreList.get(i).getId());
					if (smsSend) {
						smsRecordStore.setStatus(ProdConstant.SUCCESS);
					} else {
						smsRecordStore.setStatus(ProdConstant.FAILED);
					}

					List<SMSRecordStore> smsRecordStoresList = new ArrayList<>();
					smsRecordStoresList.add(smsRecordStore);

					smsRecordStoreService.saveAllSMSRecords(smsRecordStoresList);
				}
			} else {
				System.out.println("Organization Contact List is null :::::");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while send SMS ... " + ExceptionUtils.getStackTrace(e));
			webSocket.convertAndSend(TOPIC_DESTINATION, getTimeStamp() + ": Error sending the SMS: " + e.getMessage());
			new Throwable(e);
		}
		webSocket.convertAndSend(TOPIC_DESTINATION, getTimeStamp() + ": SMS has been sent!: ");
	}

	private String getTimeStamp() {
		return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
	}
}