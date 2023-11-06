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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.RoleMst;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.RoleService;
import com.ksv.ktrccrm.service.UserService;
import com.ksv.ktrccrm.validator.RoleValidator;

@Controller
public class RoleController {
	private static final Logger LOGGER = LogManager.getLogger(RoleController.class);

	@Autowired
	private RoleValidator roleMstValidator;

	@InitBinder
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(roleMstValidator);
	}

	@Autowired
	private RoleService roleService;
	@Autowired
	private AuditRecordService auditRecordService;
	@Autowired UserService userService;
	@Value("${global.redirectmsg.save}")
	private String successSaveMsg;
	
	@Value("${global.redirect.update}")
	private String successUpdateMsg;

	/* Display List of Registered Role */
	@GetMapping(value = "/activeRoleList")
	public String getIsActiveRoles(Model model, Device device) throws Exception {
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
			List<RoleMst> roleMstList = roleService.getRecordList(ProdConstant.TRUE, tenantId);
			if (Objects.nonNull(roleMstList)) {
				model.addAttribute("listRole", roleMstList);
			}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur to display active role list " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed active role list"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Role Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "role/activerolelist";
	}

	/* For Inactive List */
	@GetMapping(value = "/inActiveRoleList")
	public String inactiveRole(Model model, Device device) throws Exception {
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
			List<RoleMst> roleMstList = roleService.getRecordList(ProdConstant.FALSE, tenantId);
			if (Objects.nonNull(roleMstList)) {
				model.addAttribute("listRole", roleMstList);
			}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur to display inactive role list " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed inactive role list"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Role Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "role/inactiverolelist";
	}

//	@GetMapping(value = "/roleForm")
//	public String saveRole(Model model, Device device) throws Exception {
//		AuditRecord auditRecord = new AuditRecord();
//		try {
//			/* Create Model attribute to bind form data */
//			RoleMst mst = new RoleMst();
//			model.addAttribute("roleAttribute", mst);
//		} catch (Exception e) {
//			LOGGER.error("Error occur to open role registration page " + ExceptionUtils.getStackTrace(e));
//		} finally {
//			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed role management"));
//			auditRecord.setMenuCode("User Administrator");
//			auditRecord.setSubMenuCode("Role Management");
//			auditRecord.setActivityCode("VIEW");
//			auditRecordService.save(auditRecord, device);
//		}
//		return "role/newrole";
//	}

	//@GetMapping(value = "/roleForm")
	
	@RequestMapping(value={"/roleForm","/roleUpdate/{roleId}"},method = RequestMethod.GET)
	public String saveRole(@PathVariable(name = "roleId",required = false) Long roleId , Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			RoleMst mst = new RoleMst();
			/* Create Model attribute to bind form data */
			if (roleId != null){
				mst = roleService.getRoleById(roleId);
				if (Objects.nonNull(mst)) {
					model.addAttribute("roleAttribute", mst);
				}				
			} else {				
				model.addAttribute("roleAttribute", mst);
			}
			
		} catch (Exception e) {
			LOGGER.error("Error occur to open role registration page " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed role management"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Role Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "role/new";
	}
	
	@PostMapping(value = "/saveRole")
	public String successRole(@Valid @ModelAttribute("roleAttribute") RoleMst roleMst, BindingResult result,
			Model model, Device device, RedirectAttributes redirAttrs) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			if(roleMst.getRoleId() == null) {
			if (roleService.roleExists(roleMst.getRoleCode())) {
				result.addError(new FieldError("roleMst", "roleCode", "Role Code already in use"));
				return "role/new";
			} 
			}
			 if (result.hasErrors()) {
				List<ObjectError> allErrors = result.getAllErrors();
				for (ObjectError temp : allErrors) {					
					System.out.println(temp);
				}    
				return "role/new";
			} else {
				/* Save Roles to Database */
				roleService.saveRole(roleMst);
				redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while save role" + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - create role successfully"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Role Management");
			auditRecord.setActivityCode("CREATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeRoleList";
	}

//	/* Update of Registered Role */
//	@GetMapping(value = "/roleUpdate/{roleId}")
//	public ModelAndView roleUpdate(@PathVariable(name = "roleId") Long roleId, Device device) throws Exception {
//		ModelAndView mav = new ModelAndView("role/updaterole");
//		AuditRecord auditRecord = new AuditRecord();
//		try {
//			RoleMst roleMST = roleService.getRoleById(roleId);
//			if (Objects.nonNull(roleMST)) {
//				mav.addObject("updateRoleAttribute", roleMST);
//			}
//		} catch (Exception e) {
//			LOGGER.error("Error occur while display edit role page " + ExceptionUtils.getStackTrace(e));
//		} finally {
//			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed role id - " + roleId));
//			auditRecord.setMenuCode("User Administrator");
//			auditRecord.setSubMenuCode("Role Management");
//			auditRecord.setActivityCode("VIEW");
//			auditRecordService.save(auditRecord, device);
//		}
//		return mav;
//	}

//	/* Update of Registered Role */
//	@GetMapping(value = "/roleUpdate/{roleId}")
//	public ModelAndView roleUpdate(@PathVariable(name = "roleId") Long roleId, Device device) throws Exception {
//		ModelAndView mav = new ModelAndView("role/new");
//		AuditRecord auditRecord = new AuditRecord();
//		try {
//			RoleMst roleMST = roleService.getRoleById(roleId);
//			if (Objects.nonNull(roleMST)) {
//				mav.addObject("roleAttribute", roleMST);
//			}
//		} catch (Exception e) {
//			LOGGER.error("Error occur while display edit role page " + ExceptionUtils.getStackTrace(e));
//		} finally {
//			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed role id - " + roleId));
//			auditRecord.setMenuCode("User Administrator");
//			auditRecord.setSubMenuCode("Role Management");
//			auditRecord.setActivityCode("VIEW");
//			auditRecordService.save(auditRecord, device);
//		}
//		return mav;
//	}
//	@PostMapping(value = "/updateRole")
//	public String updateRole(@Valid @ModelAttribute("updateRoleAttribute") RoleMst roleMst, BindingResult result,
//			Model model, Device device, RedirectAttributes redirAttrs) throws Exception {
//		AuditRecord auditRecord = new AuditRecord();
//		try {
//			if (result.hasErrors()) {
//				List<ObjectError> allErrors = result.getAllErrors();
//				for (ObjectError temp : allErrors) {
//					System.out.println(temp);
//				}
//				return "role/updaterole";
//			} else {
//				/* Save Roles to Database */
//				if(Objects.nonNull(roleMst)) {
//					System.out.println("ROLE CODE..."+ roleMst.getRoleCode());
//					System.out.println("ROLE NAME..."+ roleMst.getRoleName());
//					roleService.saveRole(roleMst);
//					redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successUpdateMsg);
//				}
//			}
//		} catch (Exception e) {
//			LOGGER.error("Error occur while update role registration successfully " + ExceptionUtils.getStackTrace(e));
//		} finally {
//			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//			auditRecord.setRemarks(userDetails.getUsername().concat(" - update registered role "));
//			auditRecord.setMenuCode("User Administrator");
//			auditRecord.setSubMenuCode("Role Management");
//			auditRecord.setActivityCode("UPDATE");
//			auditRecordService.save(auditRecord, device);
//		}
//		return "redirect:/activeRoleList";
//	}

	/* Deactivate of Registered Role */
	@GetMapping(value = "/deactiveRole/{roleId}")
	public String deactiveRole(@PathVariable(name = "roleId") Long roleId, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			//RoleMst roleMst = roleService.deactiveRole(roleId);
			RoleMst roleMst = roleService.roleEnableAndDisable(roleId);
			if (Objects.nonNull(roleMst)) {
				return "redirect:/activeRoleList";
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while inactivate role " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - deactivate role role id - " + roleId));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Role Management");
			auditRecord.setActivityCode("DEACTIVATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/inActiveRoleList";
	}

	/* Activate of Registered Role */
	@GetMapping(value = "/activeRole/{roleId}")
	public String activeRole(@PathVariable(name = "roleId") Long roleId, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			//RoleMst roleMst = roleService.activateRole(roleId);
			RoleMst roleMst = roleService.roleEnableAndDisable(roleId);
			if (Objects.nonNull(roleMst)) {
				return "redirect:/inActiveRoleList";
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while activate role " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - activate role by role id - " + roleId));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Role Management");
			auditRecord.setActivityCode("ACTIVATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeRoleList";
	}
}
