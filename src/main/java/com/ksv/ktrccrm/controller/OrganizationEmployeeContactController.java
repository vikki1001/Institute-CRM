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
import com.ksv.ktrccrm.db1.entities.OrganizationEmployeeContact;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.OrganizationContactService;
import com.ksv.ktrccrm.service.OrganizationEmployeeContactService;

@Controller
public class OrganizationEmployeeContactController {
	private static final Logger LOGGER = LogManager.getLogger(OrganizationEmployeeContactController.class);

	@Autowired
	private AuditRecordService auditRecordService;
	@Autowired
	private OrganizationEmployeeContactService oecService;
	@Autowired
	private OrganizationContactService organizationContactService;

	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;
	
	/* Active Organization Employee Contact List */
	@GetMapping("/activeOECList")
	public String activeOrganizationEmployeeContactList(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			List<OrganizationEmployeeContact> oec = oecService.activeListOfOEC(ProdConstant.TRUE);
			if (Objects.nonNull(oec)) {
				model.addAttribute("oec", oec);
			} else {
				System.out.println("NOT DISPLAY ACTIVE LIST OF ORGANIZATION EMPLOYEE CONTACT ::::::::::::");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display active organization employee contact list "
					+ ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(
					userDetails.getUsername().concat(" - viewed active organization employee contact list"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Organization Employee Contact");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "organizationEmployeeContact/activeOECList";
	}

	/* InActive Organization Employee Contact List */
	@GetMapping("/inActiveOECList")
	public String inActiveOrganizationEmployeeContactList(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			List<OrganizationEmployeeContact> oec = oecService.inActiveListOfOEC(ProdConstant.FALSE);
			if (Objects.nonNull(oec)) {
				model.addAttribute("oec", oec);
			} else {
				System.out.println("NOT DISPLAY INACTIVE LIST OF ORGANIZATION EMPLOYEE CONTACT ::::::::::::");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display inactive organization employee contact list "
					+ ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(
					userDetails.getUsername().concat(" - viewed inactive organization employee contact list"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Organization Employee Contact");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "organizationEmployeeContact/inactiveOECList";
	}

	/* SAVE */
	@GetMapping("/oecForm")
	public String organizationEmployeeContactForm(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			OrganizationEmployeeContact oec = new OrganizationEmployeeContact();
			model.addAttribute("oec", oec);
			
			List<OrganizationContact> organizationContact = organizationContactService.activeListOfOrganizationContact(ProdConstant.TRUE);
			if (Objects.nonNull(organizationContact)) {
				model.addAttribute("organizationContact", organizationContact);	
			}			
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while display organization employee contact page " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed organization employee contact form"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Organization Employee Contact");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "organizationEmployeeContact/newOEC";
	}

	@PostMapping("/saveOEC")
	public String saveOrganizationEmployeeContact(@ModelAttribute("oec") OrganizationEmployeeContact oec,
			BindingResult result, Model model, Device device, RedirectAttributes redirAttrs) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			List<OrganizationContact> organizationContact = organizationContactService.activeListOfOrganizationContact(ProdConstant.TRUE);
			if (Objects.nonNull(organizationContact)) {
				model.addAttribute("organizationContact", organizationContact);
			}			
			
			if (result.hasErrors()) {
				List<ObjectError> allErrors = result.getAllErrors();
				for (ObjectError temp : allErrors) {
					System.out.println(temp);
				}
				return "organizationEmployeeContact/newOEC";
			} else {
				if (Objects.nonNull(oec)) {
					oecService.saveOEC(oec);
					redirAttrs.addFlashAttribute("success", "Registration Complete successfully");
				}				
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occuring while create organization employee contact " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - create organization employee contact"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Organization Employee Contact");
			auditRecord.setActivityCode("CREATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeOECList";
	}

	/* UPDATE */
	@GetMapping("/oecForm/{orgEmpContactID}")
	public String organizationEmployeeContactForm(/*BindingResult bindingResult,*/ @PathVariable("orgEmpContactID") String orgEmpContactID, Model model,
			Device device) throws Exception {
		
	
		AuditRecord auditRecord = new AuditRecord();
		try {
	
			List<OrganizationContact> organizationContact = organizationContactService.activeListOfOrganizationContact(ProdConstant.TRUE);
			
			if (Objects.nonNull(organizationContact)) {
				model.addAttribute("organizationContact", organizationContact);	
			}
			
			OrganizationEmployeeContact oec = oecService.getOECByOrgEmpContactID(orgEmpContactID);
			
			if (Objects.nonNull(oec)) {
				model.addAttribute("oec", oec);
			}
			
		} catch (Exception e) {
			LOGGER.error("Error occur while display organization employee contact update page "
					+ ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(
					userDetails.getUsername().concat(" - viewed organization employee contact update form"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Organization Employee Contact");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "organizationEmployeeContact/editOEC";
	}

	@PostMapping("/updateOEC")
	public String updateOrganizationEmployeeContact(@ModelAttribute OrganizationEmployeeContact oec,
			BindingResult result, Model model, Device device, RedirectAttributes redirAttrs) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			List<OrganizationContact> organizationContact = organizationContactService.activeListOfOrganizationContact(ProdConstant.TRUE);
			if (Objects.nonNull(organizationContact)) {
				model.addAttribute("organizationContact", organizationContact);
			}				
			
			if (result.hasErrors()) {
				List<ObjectError> allErrors = result.getAllErrors();
				for (ObjectError temp : allErrors) {
					System.out.println(temp);
				}
				return "organizationEmployeeContact/editOEC";
			} else {
				if (Objects.nonNull(oec)) {
					oecService.saveOEC(oec);
					redirAttrs.addFlashAttribute("success", "Registration Complete successfully");
				}				
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occuring while update organization employee contact " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - update organization employee contact"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Organization Employee Contact");
			auditRecord.setActivityCode("UPDATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeOECList";
	}
	
	/* SAVE Bulk Data Using Excel File */
	@GetMapping("/OECExcel")
	public String OECExcel(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			OrganizationEmployeeContact oec = new OrganizationEmployeeContact();
			model.addAttribute("oec", oec);
		} catch (Exception e) {
			LOGGER.error("Error occur while display organization employee contact excel page " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed organization employee contact excel"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Organization employee Contact");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "organizationEmployeeContact/OECExcel";
	}
	
	@PostMapping(value = "/saveOECExcel")
	public String saveOECExcel(@ModelAttribute("oec") OrganizationEmployeeContact oec, @RequestParam("file") MultipartFile files,
			Model model, Device device, RedirectAttributes redirAttrs) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			if (Objects.nonNull(files)) {
				oecService.saveAll(files);
				redirAttrs.addFlashAttribute("success", "Registration Complete successfully");	
			}			
		} catch (Exception e) {
			LOGGER.error("Error occur while to upload organization employee contact excel record  " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - create organization employee contact successfully"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Organization Employee Contact");
			auditRecord.setActivityCode("CREATE");
			auditRecordService.save(auditRecord, device);
		}
		//return "redirect:/activeOrganizationContactList";
		return "redirect:/activeOECList";
	}
	
	/* Download Organization Contact Excel Format */
	@GetMapping("/downloadOECExcel")
	public void downloadOECExcel(HttpServletResponse response) throws Exception {
		List<OrganizationEmployeeContact> oec = oecService.findAll();
		if (Objects.nonNull(oec)) {
			ByteArrayInputStream byteArrayInputStream = oecService.exportOECExcel(oec);
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=".concat(ProdConstant.ORGEMPCONTACTEXCELNAME) + checkOutDaoImpl.getDateTime() + ProdConstant.EXCELEXTENSION);
			IOUtils.copy(byteArrayInputStream, response.getOutputStream());	
		}		
	}

	/* Activate Organization Employee Contact */
	@GetMapping(value = "/activeOEC/{orgEmpContactID}")
	public String activeOrganizationEmployeeContact(@PathVariable(name = "orgEmpContactID") String orgEmpContactID, Device device)
			throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			if (Objects.nonNull(orgEmpContactID)) {
				oecService.activateOEC(orgEmpContactID);	
			}			
		} catch (Exception e) {
			LOGGER.error("Error occur while activate organization employee contact " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername()
					.concat(" - activate organization employee contact id - " + orgEmpContactID));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Organization Employee Contact");
			auditRecord.setActivityCode("ACTIVATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/inActiveOECList";
	}

	/* Deactivate Organization Employee Contact */
	@GetMapping(value = "/deactiveOEC/{orgEmpContactID}")
	public String deactiveOrganizationEmployeeContact(@PathVariable(name = "orgEmpContactID") String orgEmpContactID, Device device)
			throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			if (Objects.nonNull(orgEmpContactID)) {
				oecService.deactivateOEC(orgEmpContactID);	
			}			
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while deactivate organization employee contact " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername()
					.concat(" - deactivate organization employee contact id - " + orgEmpContactID));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Organization Employee Contact");
			auditRecord.setActivityCode("DEACTIVATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeOECList";
	}
}
