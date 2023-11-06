package com.ksv.ktrccrm.controller;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.impl.CheckInCheckOutDaoImpl;
import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.BranchesInOrganization;
import com.ksv.ktrccrm.db1.entities.CustomerContact;
import com.ksv.ktrccrm.db1.entities.OrganizationContact;
import com.ksv.ktrccrm.db1.repository.BranchesInOrganizationRepository;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.CustomerContactService;
import com.ksv.ktrccrm.service.OrganizationContactService;

@Controller
public class CustomerContactController {
	private static final Logger LOGGER = LogManager.getLogger(CustomerContactController.class);

	@Autowired
	private AuditRecordService auditRecordService;
	@Autowired
	private CustomerContactService customerContactService;
	@Autowired
	private OrganizationContactService organizationContactService;
	@Autowired
	private BranchesInOrganizationRepository biorepository;
	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;

	/* Active Customer Contact List */
	@GetMapping("/activeCustomerContactList")
	public String activeCustomerContactList(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			List<CustomerContact> customerContacts = customerContactService
					.activeListOfCustomerContact(ProdConstant.TRUE);
			if (Objects.nonNull(customerContacts)) {
				model.addAttribute("customerContacts", customerContacts);
			} else {
				System.out.println("NOT DISPLAY ACTIVE LIST OF CUSTOMER CONTACT ::::::::::::");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display active customer contact list " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed active customer contact list"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Customer Contact");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "customerContact/activeCustomerContactList";
	}

	/* InActive Customer Contact List */
	@GetMapping("/inActiveCustomerContactList")
	public String inActiveCustomerContactList(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			List<CustomerContact> customerContacts = customerContactService
					.inActiveListOfCustomerContact(ProdConstant.FALSE);
			if (Objects.nonNull(customerContacts)) {
				model.addAttribute("customerContacts", customerContacts);
			} else {
				System.out.println("NOT DISPLAY INACTIVE LIST OF CUSTOMER CONTACT ::::::::::::");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display inactive customer contact list " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed inactive customer contact list"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Customer Contact");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "customerContact/inactiveCustomerContactList";
	}

	/* SAVE */
	@GetMapping("/customerContactForm")
	public String customerContactForm(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			CustomerContact customerContact = new CustomerContact();
			model.addAttribute("customerContact", customerContact);

			List<OrganizationContact> organizationContact = organizationContactService
					.activeListOfOrganizationContact(ProdConstant.TRUE);
			if (Objects.nonNull(organizationContact)) {
				model.addAttribute("organizationContact", organizationContact);
			}

			List<BranchesInOrganization> branchesInOrganizations = biorepository.findAll();
			if (Objects.nonNull(branchesInOrganizations)) {
				model.addAttribute("bio", branchesInOrganizations);
			}

		} catch (Exception e) {
			LOGGER.error("Error occur while display customer contact page " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed customer contact form"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Customer Contact");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "customerContact/newCustomerContact";
	}

	@PostMapping("/saveCustomerContact")
	public String saveCustomerContact(@ModelAttribute("customerContact") CustomerContact customerContact,
			BindingResult result, Model model, Device device, RedirectAttributes redirAttrs) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			List<OrganizationContact> organizationContact = organizationContactService
					.activeListOfOrganizationContact(ProdConstant.TRUE);
			if (Objects.nonNull(organizationContact)) {
				model.addAttribute("organizationContact", organizationContact);
			}

			List<BranchesInOrganization> branchesInOrganizations = biorepository.findAll();
			if (Objects.nonNull(branchesInOrganizations)) {
				model.addAttribute("bio", branchesInOrganizations);
			}

			if (result.hasErrors()) {
				List<ObjectError> allErrors = result.getAllErrors();
				for (ObjectError temp : allErrors) {
					System.out.println(temp);
				}
				return "customerContact/newCustomerContact";
			} else {
				if (Objects.nonNull(customerContact)) {
					customerContactService.saveCustomerContact(customerContact);
					redirAttrs.addFlashAttribute("success", "Registration Completed Successfully");
				}				
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while create customer contact " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - create customer contact"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Customer Contact");
			auditRecord.setActivityCode("CREATE");
			auditRecordService.save(auditRecord, device);
		}

		return "redirect:/activeCustomerContactList";

	}

	/* UPDATE */
	@GetMapping("/customerContactForm/{customerId}")
	public String customerContactForm(@PathVariable("customerId") String customerId, Model model, Device device)
			throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			CustomerContact customerContact = customerContactService.getDataByCustomerContact(customerId);
			if (Objects.nonNull(customerContact)) {
				model.addAttribute("customerContact", customerContact);
			}

			List<OrganizationContact> organizationContact = organizationContactService
					.activeListOfOrganizationContact(ProdConstant.TRUE);
			if (Objects.nonNull(organizationContact)) {
				model.addAttribute("organizationContact", organizationContact);
			}

			List<BranchesInOrganization> branchesInOrganizations = biorepository.findAll();
			if (Objects.nonNull(branchesInOrganizations)) {
				model.addAttribute("bio", branchesInOrganizations);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display customer contact update page " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed update customer contact form"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Customer Contact");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "customerContact/editCustomerContact";
	}

	@PostMapping("/editCustomerContact")
	public String editCustomerContact(@ModelAttribute("customerContact") CustomerContact customerContact,
			BindingResult result, Model model, Device device, RedirectAttributes redirAttrs) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			List<OrganizationContact> organizationContact = organizationContactService
					.activeListOfOrganizationContact(ProdConstant.TRUE);
			if (Objects.nonNull(organizationContact)) {
				model.addAttribute("organizationContact", organizationContact);
			}

			List<BranchesInOrganization> branchesInOrganizations = biorepository.findAll();
			if (Objects.nonNull(branchesInOrganizations)) {
				model.addAttribute("bio", branchesInOrganizations);
			}

			if (result.hasErrors()) {
				List<ObjectError> allErrors = result.getAllErrors();
				for (ObjectError temp : allErrors) {
					System.out.println(temp);
				}
				return "customerContact/editCustomerContact";
			} else {
				if (Objects.nonNull(customerContact)) {
					customerContactService.saveCustomerContact(customerContact);
					redirAttrs.addFlashAttribute("success", "Registration Completed Successfully");
				}				
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while update customer contact " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - update customer contact"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Customer Contact");
			auditRecord.setActivityCode(" UPDATE");
			auditRecordService.save(auditRecord, device);
		}

		return "redirect:/activeCustomerContactList";

	}

	/* SAVE Bulk Data Using Excel File */
	@GetMapping("/customerContactExcel")
	public String customerContactExcel(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			CustomerContact customerContact = new CustomerContact();
			model.addAttribute("customerContact", customerContact);
		} catch (Exception e) {
			LOGGER.error("Error occur while display customer contact excel page " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed customer contact excel"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Customer Contact");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "customerContact/excelCustomerContact";
	}

	@PostMapping(value = "/saveCustomerContactExcel")
	public String saveCustomerContactExcel(@ModelAttribute("customerContact") CustomerContact customerContact,
			@RequestParam("file") MultipartFile files, Model model, Device device, RedirectAttributes redirAttrs)
			throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			if (Objects.nonNull(files)) {
				customerContactService.saveAll(files);
				redirAttrs.addFlashAttribute("success", "Registration Complete successfully");
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while to upload customer contact excel record " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - create customer contact successfully"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Customer Contact");
			auditRecord.setActivityCode("CREATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeCustomerContactList";
	}

	/* Download Organization Contact Excel Format */
	@GetMapping("/downloadCustomerContactExcel")
	public void downloadCustomerContactExcel(HttpServletResponse response) throws Exception {
		List<CustomerContact> customerContact = customerContactService.findAll();
		if (Objects.nonNull(customerContact)) {
			ByteArrayInputStream byteArrayInputStream = customerContactService
					.exportCustomerContactExcel(customerContact);
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=".concat(ProdConstant.CUSTOMEREXCELNAME)
					+ checkOutDaoImpl.getDateTime() + ProdConstant.EXCELEXTENSION);
			IOUtils.copy(byteArrayInputStream, response.getOutputStream());
		}
	}

	/* Activate Customer Contact */
	@GetMapping(value = "/activeCustomerContact/{customerId}")
	public String activeCustomerContact(@PathVariable(name = "customerId") String customerId, Device device)
			throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			if (Objects.nonNull(customerId)) {
				customerContactService.activateCustomerContact(customerId);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while activate customer contact " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - activate customer contact id - " + customerId));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Customer Contact");
			auditRecord.setActivityCode("ACTIVATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeCustomerContactList";
	}

	/* Deactivate Customer Contact */
	@GetMapping(value = "/deactiveCustomerContact/{customerId}")
	public String deactiveCustomerContact(@PathVariable(name = "customerId") String customerId, Device device)
			throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			if (Objects.nonNull(customerId)) {
				customerContactService.deactivateCustomerContact(customerId);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while deactivate customer contact " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord
					.setRemarks(userDetails.getUsername().concat(" - deactivate customer contact id - " + customerId));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Customer Contact");
			auditRecord.setActivityCode("DEACTIVATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeCustomerContactList";
	}
}
