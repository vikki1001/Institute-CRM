package com.ksv.ktrccrm.controller;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

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
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.BranchMst;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.BranchService;
import com.ksv.ktrccrm.service.UserService;
import com.ksv.ktrccrm.validator.BranchValidator;

@Controller
public class BranchController {
	private static final Logger LOGGER = LogManager.getLogger(BranchController.class);

	@Autowired
	private BranchValidator branchMstValidator;

	@InitBinder
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(branchMstValidator);
	}

	@Autowired
	private AuditRecordService auditRecordService;
	@Autowired
	private BranchService branchService;
	@Autowired
	private UserService userService;

	@Value("${global.redirectmsg.save}")
	private String successSaveMsg;

	@Value("${global.redirect.update}")
	private String successUpdateMsg;

	/* Display List of IsActive Branch */
	@GetMapping(value = "/activeBranchList")
	public String getIsActiveBranchs(Model model, Device device) throws Exception {
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
					List<BranchMst> branchMstList = branchService.getRecordList(ProdConstant.TRUE, tenantId);
					if (Objects.nonNull(branchMstList)) {
						model.addAttribute("listBranch", branchMstList);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display active branch list " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed active branch list"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Branch Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "branch/activebranchlist";
	}

	/* Display List of InActive Branch */
	@GetMapping(value = "/inActiveBranchList")
	public String getInActiveBranchs(Model model, Device device) throws Exception {
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
					List<BranchMst> branchMstList = branchService.getRecordList(ProdConstant.FALSE, tenantId);
					if (Objects.nonNull(branchMstList)) {
						model.addAttribute("listBranch", branchMstList);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display inactive branch list " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed inActive branch list"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Branch Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "branch/inactivebranchlist";
	}

	@GetMapping(value = "/branchForm")
	public String saveBranch(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			BranchMst branchMst = new BranchMst();
			model.addAttribute("branchAttribute", branchMst);
		} catch (Exception e) {
			LOGGER.error("Error occur to open branch registration page... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed branch management form"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Branch Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "branch/newbranch";
	}

	@PostMapping(value = "/saveBranch")
	public String successBranch(@Valid @ModelAttribute("branchAttribute") BranchMst mst, BindingResult result,
			Model model, Device device, RedirectAttributes redirAttrs) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			Long id = mst.getBranchId();
			if (id == null) {
				if (branchService.branchExists(mst.getBranchCode())) {
					result.addError(new FieldError("mst", "branchCode", "Branch Code already in use"));
				}
			}
			if (result.hasErrors()) {
				List<ObjectError> allErrors = result.getAllErrors();
				for (ObjectError temp : allErrors) {
					System.out.println(temp);
				}
				return "branch/newbranch";
			} else {
				/* Save Branch to Database */
				if (Objects.nonNull(mst)) {
					branchService.saveBranch(mst);
					redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while branch registration successfully " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - create branch "));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Branch Management");
			auditRecord.setActivityCode("CREATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeBranchList";
	}

	/* Update of Registered Branch */
	@GetMapping(value = "/branchUpdate/{branchId}")
	public ModelAndView branchUpdate(@PathVariable(name = "branchId") Long branchId, Device device) throws Exception {
		ModelAndView mav = new ModelAndView("branch/updatebranch");
		AuditRecord auditRecord = new AuditRecord();
		try {
			BranchMst branchMST = branchService.getBranchById(branchId);
			if (Objects.nonNull(branchMST)) {
				mav.addObject("branchAttribute", branchMST);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while edit branch registartion page " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed branch by id " + branchId));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Branch Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return mav;
	}

	@PostMapping(value = "/updateBranch")
	public String updateBranch(@Valid @ModelAttribute("branchAttribute") BranchMst mst, BindingResult result,
			Model model, Device device, RedirectAttributes redirAttrs) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			if (result.hasErrors()) {
				List<ObjectError> allErrors = result.getAllErrors();
				for (ObjectError temp : allErrors) {
					System.out.println(temp);
				}
				return "branch/updatebranch";
			} else {
				/* Save Branch to Database */
				if (Objects.nonNull(mst)) {
					branchService.saveBranch(mst);
					redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successUpdateMsg);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while update branch successfully " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - update branch "));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Branch Management");
			auditRecord.setActivityCode("UPDATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeBranchList";
	}

	/* Deactivate of Registered Branch */
	@GetMapping(value = "/deactiveBranch/{branchId}")
	public String deactiveBranch(@PathVariable(name = "branchId") Long branchId, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			BranchMst branchMst = branchService.branchEnableAndDisable(branchId);
			if (Objects.nonNull(branchMst)) {
				return "redirect:/activeBranchList";
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while deactivate branch " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - deactivate branch by branch id - " + branchId));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Branch Management");
			auditRecord.setActivityCode("DEACTIVATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/inActiveBranchList";
	}

	/* Activate of Registered Branch */
	@GetMapping(value = "/activeBranch/{branchId}")
	public String activeBranch(@PathVariable(name = "branchId") Long branchId, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			BranchMst branchMst = branchService.branchEnableAndDisable(branchId);
			if (Objects.nonNull(branchMst)) {
				return "redirect:/inActiveBranchList";
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while activate branch " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - activate branch by branch id - " + branchId));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Branch Management");
			auditRecord.setActivityCode("ACTIVATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeBranchList";
	}
}