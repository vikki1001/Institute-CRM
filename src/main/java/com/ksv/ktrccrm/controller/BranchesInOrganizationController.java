package com.ksv.ktrccrm.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.BranchesInOrganization;
import com.ksv.ktrccrm.db1.repository.BranchesInOrganizationRepository;
import com.ksv.ktrccrm.service.AuditRecordService;

@Controller
public class BranchesInOrganizationController {
	private static final Logger LOGGER = LogManager.getLogger(BranchesInOrganizationController.class);
	
	@Autowired
	private AuditRecordService auditRecordService;
	@Autowired
	private BranchesInOrganizationRepository bioRepo;

	/* Display List of IsActive BranchesInOrganizatio */
	@GetMapping(value = "/activeOrgBranchList")
	public String activeOrgBranchList(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			List<BranchesInOrganization> list = bioRepo.getBranchList(ProdConstant.TRUE);
			if (Objects.nonNull(list)) {
				model.addAttribute("list", list);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display active branch list " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed active branch list"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Branch");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "organizationContact/activeOrgBranchList";
	}

	/* Display List of InActive BranchesInOrganizatio */
	@GetMapping(value = "/inActiveOrgBranchList")
	public String inActiveOrgBranchList(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			List<BranchesInOrganization> list = bioRepo.getBranchList(ProdConstant.FALSE);
			if (Objects.nonNull(list)) {
				model.addAttribute("list", list);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display inactive branch list " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed inactive user list"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Branch");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "organizationContact/inActiveOrgBranchList";
	}

	@GetMapping("/addBranchesInOrg")
	public String addBranchesInOrg(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			BranchesInOrganization bio = new BranchesInOrganization();
			model.addAttribute("bio", bio);
		} catch (Exception e) {
			LOGGER.error("Error occur to display add branch organization form " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed add branch form"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Branch");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "organizationContact/addBranchesInOrg";
	}

	@PostMapping("/saveBranchesInOrg")
	public String saveBranchesInOrg(@Valid @ModelAttribute("bio") BranchesInOrganization bio, BindingResult result,
			Model model, Device device, RedirectAttributes redirAttrs) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			if (result.hasErrors()) {
				List<ObjectError> allErrors = result.getAllErrors();
				for (ObjectError temp : allErrors) {
					System.out.println(temp);
				}
				return "organizationContact/addBranchesInOrg";
			} else {
				if (bio.getId() == null) {
					System.out.println("SAVE RECORD ::::::::: ");
					bio.setIsActive(ProdConstant.TRUE);
					bioRepo.save(bio);
					redirAttrs.addFlashAttribute("success", "Registration Completed Successfully");
				} else {
					Optional<BranchesInOrganization> optional = bioRepo.findById(bio.getId());
					if (optional.isPresent()) {
						System.out.println("UPDATE RECORD ::::::::: ");
						BranchesInOrganization branchesInOrganization = optional.get();

						branchesInOrganization.setBranchId(bio.getBranchId());
						branchesInOrganization.setBranchName(bio.getBranchName());
						branchesInOrganization.setBranchType(bio.getBranchType());

						bioRepo.save(branchesInOrganization);
						redirAttrs.addFlashAttribute("success", "Update Completed Successfully");
					} else {

					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur to save branch in organization " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - create branch in branch organization "));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Branch");
			auditRecord.setActivityCode("CREATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeOrgBranchList";
	}

	@GetMapping("/editBranchesInOrg/{id}")
	public String editBranchesInOrg(@Valid @ModelAttribute("bio") BranchesInOrganization bio, @PathVariable("id") Long id,
			BindingResult result, Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			if (result.hasErrors()) {
				List<ObjectError> allErrors = result.getAllErrors();
				for (ObjectError temp : allErrors) {
					System.out.println(temp);
				}
				return "organizationContact/addBranchesInOrg";
			} else {
				if (Objects.nonNull(bio)) {
					bio = bioRepo.getById(id);
					model.addAttribute("bio", bio);
				} else {

				}
			}			
		} catch (Exception e) {
			LOGGER.error("Error occur to display add branch organization form " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed edit branch form"));
			auditRecord.setMenuCode("Contact Management");
			auditRecord.setSubMenuCode("Branch");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "organizationContact/addBranchesInOrg";
	}

}
