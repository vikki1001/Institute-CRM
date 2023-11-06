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
import com.ksv.ktrccrm.db1.entities.OrganizationContact;
import com.ksv.ktrccrm.exception.DuplicateDataException;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.OrganizationContactService;

@Controller
public class OrganizationContactController {
	private static final Logger LOGGER = LogManager.getLogger(OrganizationContactController.class);

	@Autowired
	private AuditRecordService auditRecordService;
	@Autowired
	private OrganizationContactService organizationContactService;

	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;
	
	/* Active Organization Contact List */
	@GetMapping("/activeOrganizationContactList")
	public String activeOrganizationContactList(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			List<OrganizationContact> organizationContact = organizationContactService
					.activeListOfOrganizationContact(ProdConstant.TRUE);
			if (Objects.nonNull(organizationContact)) {
				model.addAttribute("organizationContact", organizationContact);
			} else {
				System.out.println("NOT DISPLAY ACTIVE LIST OF ORGANIZATION CONTACT ::::::::::::");
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while display active organization contact list " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed active organization contact list"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Organization Contact");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "organizationContact/activeOrganizationContactList";
	}

	/* InActive Organization Contact List */
	@GetMapping("/inActiveOrganizationContactList")
	public String inActiveOrganizationContactList(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			List<OrganizationContact> organizationContact = organizationContactService
					.inActiveListOfOrganizationContact(ProdConstant.FALSE);
			if (Objects.nonNull(organizationContact)) {
				model.addAttribute("organizationContact", organizationContact);
			} else {
				System.out.println("NOT DISPLAY INACTIVE LIST OF ORGANIZATION CONTACT ::::::::::::");
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while display inactive organization contact list " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed inactive organization contact list"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Organization Contact");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "organizationContact/inactiveOrganizationContactList";
	}

	/* SAVE */
	@GetMapping("/organizationContactForm")
	public String organizationContactForm(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			OrganizationContact organizationContact = new OrganizationContact();
			model.addAttribute("organizationContact", organizationContact);
		} catch (Exception e) {
			LOGGER.error("Error occur while display organization contact page " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed organization contact form"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Organization Contact");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "organizationContact/newOrganizationContact";
	}

	@PostMapping("/saveorganizationContact")
	public String saveOrganizationContact(
			@ModelAttribute("organizationContact") OrganizationContact organizationContact, BindingResult result,
			Model model, Device device, RedirectAttributes redirAttrs) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			if (result.hasErrors()) {
				List<ObjectError> allErrors = result.getAllErrors();
				for (ObjectError temp : allErrors) {
					System.out.println(temp);
				}
				return "organizationContact/newOrganizationContact";
			} else {
				organizationContactService.saveOrganizationContact(organizationContact);
				redirAttrs.addFlashAttribute("success", "Registration Completed Successfully");
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while create organization contact " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - create organization contact"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Organization Contact");
			auditRecord.setActivityCode("CREATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeOrganizationContactList";
	}

	/* UPDATE */
	@GetMapping("/organizationContactForm/{organizationId}")
	public String organizationContactForm(@PathVariable(name = "organizationId") String organizationId, Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			OrganizationContact organizationContact = organizationContactService.getOrgContactById(organizationId);
			if (Objects.nonNull(organizationContact)) {
				model.addAttribute("organizationContact", organizationContact);	
			}			
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while display organization contact update page " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed organization contact update form"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Organization Contact");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "organizationContact/editOrganizationContact";
	}

	@PostMapping("/updateOrganizationContact")
	public String updateOrganizationContact(
			@ModelAttribute("organizationContact") OrganizationContact organizationContact, BindingResult result,
			Model model, Device device, RedirectAttributes redirAttrs) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			if (result.hasErrors()) {
				List<ObjectError> allErrors = result.getAllErrors();
				for (ObjectError temp : allErrors) {
					System.out.println(temp);
				}
				return "organizationContact/editOrganizationContact";
			} else {
				if (Objects.nonNull(organizationContact)) {
					organizationContactService.saveOrganizationContact(organizationContact);
					redirAttrs.addFlashAttribute("success", "Registration Completed Successfully");	
				}				
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while update organization contact " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - update organization contact"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Organization Contact");
			auditRecord.setActivityCode("EDIT");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeOrganizationContactList";
	}

	/* SAVE Bulk Data Using Excel File */
	@GetMapping("/organizationContactExcel")
	public String organizationContactExcel(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			OrganizationContact organizationContact = new OrganizationContact();
			model.addAttribute("organizationContact", organizationContact);
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while display organization contact excel page " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed organization contact excel form"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Organization Contact");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "organizationContact/excelOrganizationContact";
	}

	@PostMapping(value = "/saveOrgContactExcel")
	public String saveOrgContactExcel(@ModelAttribute("organizationContact") OrganizationContact organizationContact,
			@RequestParam("file") MultipartFile files, BindingResult result, Model model, Device device,
			RedirectAttributes redirAttrs) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
//			try (XSSFWorkbook workbook = new XSSFWorkbook(files.getInputStream())) {
//				// Read data form excel file sheet
//				XSSFSheet worksheet = workbook.getSheetAt(0);
//				for (int index = 0; index < worksheet.getPhysicalNumberOfRows(); index++) {
//					if (index > 0) {
//						XSSFRow row = worksheet.getRow(index);
//						Integer mobileNo = (int) row.getCell(3).getNumericCellValue();
//						System.out.println("MOBILE NUMBER in controller :::::: " + mobileNo);
//
//						boolean b = organizationContactService.checkDuplicateException(String.valueOf(mobileNo));
//						if (b) {
//							result.addError(new FieldError("organizationContact", "mobileNumber",
//									"Mobile number already in use"));
//							System.out.println("ALREADY :::: ");
//							redirAttrs.addFlashAttribute("success", "Mobile number already in use - " + mobileNo);
//							return "redirect:/activeOrganizationContactList";
//						} else {
			if (Objects.nonNull(files)) {
				organizationContactService.saveAll(files);
				redirAttrs.addFlashAttribute("success", "Record Insert successfully");
			}							
//						}
//					}
//				}
//			}
		} catch (DuplicateDataException e) {
			//redirAttrs.addFlashAttribute("error", e.getMessage());
			model.addAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
			LOGGER.error("Error occur while to upload organization contact excel record  "
					+ ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - create organization contact successfully"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Organization Contact");
			auditRecord.setActivityCode("CREATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeOrganizationContactList";
		//return "organizationContact/excelOrganizationContact";
	}

	/* Download Organization Contact Excel Format */
	@GetMapping("/downloadOrgContact")
	public void downloadOrgContact(HttpServletResponse response) throws Exception {
		List<OrganizationContact> organizationContact = organizationContactService
				.activeListOfOrganizationContact(ProdConstant.TRUE);
		if (Objects.nonNull(organizationContact)) {
			ByteArrayInputStream byteArrayInputStream = organizationContactService.exportOrgContact(organizationContact);
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition",
					"attachment; filename=".concat(ProdConstant.ORGCONTACTEXCELNAME) + checkOutDaoImpl.getDateTime() + ProdConstant.EXCELEXTENSION);
			IOUtils.copy(byteArrayInputStream, response.getOutputStream());
		}		
	}

	/* Activate Organization Contact */
	@GetMapping(value = "/activeOrganizationContact/{id}")
	public String activeOrganizationContact(@PathVariable(name = "id") String id, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			if (Objects.nonNull(id)) {
				organizationContactService.activateOrganizationContact(id);
			}			
		} catch (Exception e) {
			LOGGER.error("Error occur while activate organization contact " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - activate organization contact id - " + id));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Organization Contact");
			auditRecord.setActivityCode("ACTIVATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/inActiveOrganizationContactList";
	}

	/* Deactivate Organization Contact */
	@GetMapping(value = "/deactiveOrganizationContact/{id}")
	public String deactiveOrganizationContact(@PathVariable(name = "id") String id, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			if (Objects.nonNull(id)) {
				organizationContactService.deactivateOrganizationContact(id);	
			}			
		} catch (Exception e) {
			LOGGER.error("Error occur while deactivate organization contact " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - deactivate organization contact id - " + id));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Organization Contact");
			auditRecord.setActivityCode("DEACTIVATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeOrganizationContactList";
	}
}
