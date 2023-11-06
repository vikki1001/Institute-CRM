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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.BranchMst;
import com.ksv.ktrccrm.db1.entities.DepartmentMst;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.RoleMst;
import com.ksv.ktrccrm.db1.entities.SequenceMst;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.BranchService;
import com.ksv.ktrccrm.service.DepartmentService;
import com.ksv.ktrccrm.service.RoleService;
import com.ksv.ktrccrm.service.SequenceMstService;
import com.ksv.ktrccrm.service.UserService;
import com.ksv.ktrccrm.validator.UserValidator;

@Controller
public class UserController {
	private static final Logger LOGGER = LogManager.getLogger(UserController.class);

	@Autowired
	private UserValidator userMstValidator;

	@InitBinder
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(userMstValidator);
	}

	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private BranchService branchService;
	@Autowired
	private AuditRecordService auditRecordService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private SequenceMstService sequenceMstService;
	
	@Value("${global.redirectmsg.save}")
	private String successSaveMsg;
	
	@Value("${global.redirect.update}")
	private String successUpdateMsg;
	
	/* Display List of IsActive Users */
	@GetMapping(value = "/activeUserList")
	public String getAllUsers(Model model, Device device) throws Exception {
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
					List<UserMst> userMstList = userService.getRecordList(ProdConstant.ISACTIVE,tenantId);
					for(UserMst user2: userMstList) {
						System.out.println("USER LIST _______ " +  user2.getFirstName());
					}
					if (Objects.nonNull(userMstList)) {
						model.addAttribute("listUser", userMstList);
					}				
				}			     
			}				
		} catch (Exception e) {
			LOGGER.error("Error occur while display actice user list " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed active user list"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("User Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "user/activeuserlist";
	}

	/* Display List of InActive Users */
	@GetMapping(value = "/inActiveUserList")
	public String inActiveUser(Model model, Device device) throws Exception {
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
					List<UserMst> userMstList = userService.getRecordList(ProdConstant.INACTIVE,tenantId);
					if (Objects.nonNull(userMstList)) {
						model.addAttribute("listUser", userMstList);
					}				
				}			     
			}	
		} catch (Exception e) {
			LOGGER.error("Error occur to display inactive user list " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed inactive user list"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("User Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "user/inactiveuserlist";
	}

	@GetMapping(value = "/userForm")
	public String saveUser(Model model, Device device) throws Exception {
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
			UserMst userMst = new UserMst(); 
			model.addAttribute("userMst", userMst);
			List<RoleMst> listRole = roleService.getRecordList(ProdConstant.FALSE, tenantId);
			if(Objects.nonNull(listRole)){
				model.addAttribute("listRole", listRole);
			}
				}
			}
			List<BranchMst> listBranch = branchService.getRecordList(ProdConstant.TRUE, tenantId);
			if(Objects.nonNull(listBranch)){
				model.addAttribute("listBranch", listBranch);
			}
			List<DepartmentMst> departmentList = departmentService.getRecordList(ProdConstant.TRUE,tenantId);
			if(Objects.nonNull(departmentList)){
				model.addAttribute("departmentList", departmentList);
			}
			List<SequenceMst> getTenantId = sequenceMstService.getTenantId(ProdConstant.TRUE);
			if(Objects.nonNull(getTenantId)){
				model.addAttribute("tenantId", getTenantId);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur to display user registration page " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed user management"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("User Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "user/newuser";
	}

	@PostMapping(value = "/saveUser")
	public String successUser(@Valid @ModelAttribute UserMst userMst, BindingResult result, Model model, Device device,
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
				if(Objects.nonNull(tenantId)) {
			List<RoleMst> getlistRole = roleService.getRecordList(ProdConstant.TRUE, tenantId); 
			if(Objects.nonNull(getlistRole)){
				model.addAttribute("listRole", getlistRole);
			}
				}
			}
			List<BranchMst> listBranch = branchService.getRecordList(ProdConstant.TRUE, tenantId); 
			if(Objects.nonNull(listBranch)){
				model.addAttribute("listBranch", listBranch);
			}
			List<DepartmentMst> departmentList =departmentService.getRecordList(ProdConstant.TRUE,tenantId); 
			if(Objects.nonNull(departmentList)){
				model.addAttribute("departmentList", departmentList);
			}
			List<SequenceMst> getTenantId =sequenceMstService.getTenantId(ProdConstant.TRUE);
			if(Objects.nonNull(getTenantId)){
				model.addAttribute("tenantId", getTenantId);
			}
			if (userService.loginIdExists(userMst.getLoginId())) {
				System.out.println("Inside check exists loginId");
				result.addError(new FieldError("userMst", "loginId", "LoginId already in use"));
				return "user/newuser";
			} 
//			else if (userService.userExists(userMst.getPassword())) {
//				result.addError(new FieldError("userMst", "password", "Password must be required"));
//			}
		else if (result.hasErrors()) {
				List<ObjectError> allErrors = result.getAllErrors();
				for (ObjectError temp : allErrors) {
					System.out.println(temp);
				}
				return "user/newuser";
			} else {
				/* Save Users to Database */
				userService.saveOrUpdateUser(userMst);
				redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while save user" + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - create user in user management"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("User Management");
			auditRecord.setActivityCode("CREATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeUserList";
	}

	/* Update of Registered Users */
	@GetMapping(value = "/showFormForUpdate/{loginId}")
	public String showEditProductPage(@ModelAttribute UserMst userMst, @PathVariable(name = "loginId") String loginId,
			Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId = null;
		String tenantId1 = null;
		UserMst user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			empId = authentication.getName();
			if(Objects.nonNull(empId)) {
				user = userService.getUserById(empId);
				tenantId1 = user.getTenantId();
				if(Objects.nonNull(tenantId1)) {
			List<RoleMst> listRole = roleService.getRecordList(ProdConstant.TRUE, tenantId1);
			if(Objects.nonNull(listRole)){
				model.addAttribute("listRole", listRole);
			}
				}
			}
			List<BranchMst> listBranch = branchService.getRecordList(ProdConstant.TRUE, tenantId1);
			if(Objects.nonNull(listBranch)){
				model.addAttribute("listBranch", listBranch);
			}
			List<DepartmentMst> departmentList = departmentService.getRecordList(ProdConstant.TRUE, tenantId1);
			if(Objects.nonNull(departmentList)){
				model.addAttribute("departmentList", departmentList);
			}
			UserMst userMst1 = userService.getUserById(loginId);
			if(Objects.nonNull(userMst1)){
				model.addAttribute("userMst", userMst1);
			}
			List<SequenceMst> tenantId = sequenceMstService.getTenantId(ProdConstant.TRUE);
			if(Objects.nonNull(tenantId)){
				model.addAttribute("tenantId", tenantId);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while show form for update user" + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed user id - " + loginId));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("User Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "user/updateuser";
	}

	@PostMapping(value = "/saveUpdate")
	public String updateUser(@Valid @ModelAttribute UserMst userMst, BindingResult result, Model model, Device device,
			RedirectAttributes redirAttrs) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId = null;
		String tenantId1 = null;
		UserMst user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			empId = authentication.getName();
			if(Objects.nonNull(empId)) {
				user = userService.getUserById(empId);
				tenantId1 = user.getTenantId();
				if(Objects.nonNull(tenantId1)) {
			List<RoleMst> listRole = roleService.getRecordList(ProdConstant.TRUE, tenantId1);
			if(Objects.nonNull(listRole)){
				model.addAttribute("listRole", listRole);
			}
				}
			}
			List<BranchMst> listBranch = branchService.getRecordList(ProdConstant.TRUE, tenantId1);
			if(Objects.nonNull(listBranch)){
				model.addAttribute("listBranch", listBranch);
			}
			List<DepartmentMst> departmentList = departmentService.getRecordList(ProdConstant.TRUE, tenantId1);
			if(Objects.nonNull(departmentList)){
				model.addAttribute("departmentList", departmentList);
			}
			List<SequenceMst> tenantId = sequenceMstService.getTenantId(ProdConstant.TRUE);
			if(Objects.nonNull(tenantId)){
				model.addAttribute("tenantId", tenantId);
			}

			if (result.hasErrors()) {
				List<ObjectError> allErrors = result.getAllErrors();
				for (ObjectError temp : allErrors) {
					System.out.println(temp);
				}
				return "user/updateuser";
			} else {
				/* Save Users to Database */
				userService.saveOrUpdateUser(userMst);
				redirAttrs.addFlashAttribute(ProdConstant.SUCCESS,successUpdateMsg);
				return "redirect:/activeUserList";
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while save & update user" + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - update user in user management"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("User Management");
			auditRecord.setActivityCode("UPDATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeUserList";
	}

	/* Deactivate of Registered User */
	@GetMapping(value = "/deactiveUser/{loginId}")
	public String deactiveUser(@PathVariable(name = "loginId") String loginId, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			UserMst userMst = userService.userEnableAndDisable(loginId);
			if (Objects.nonNull(userMst)) {
				return "redirect:/activeUserList";
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while deactivate user " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - deactivate user login id - " + loginId));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("User Management");
			auditRecord.setActivityCode("DEACTIVATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/InActiveUserList";
	}

	/* Activate of Registered User */
	@GetMapping(value = "/activeUser/{loginId}")
	public String activeUser(@PathVariable(name = "loginId") String loginId, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			UserMst userMst = userService.userEnableAndDisable(loginId);
			if (Objects.nonNull(userMst)) {
				return "redirect:/inActiveUserList";
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while activate user " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - activate user login id - " + loginId));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("User Management");
			auditRecord.setActivityCode("ACTIVATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeUserList";
	}

	@GetMapping(value = "/escalationSearch")
	public @ResponseBody ModelAndView search(@RequestParam("value") String value, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		ModelAndView mv = new ModelAndView();
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
					mv.setViewName("fragments/searchEscalation");
					List<EmpBasicDetails> empBasicDetails = userService.getSearchEmpByIdAndFullName(value, tenantId);
					if (Objects.nonNull(empBasicDetails)) {
						mv.addObject("empBasicDetails", empBasicDetails);
						return mv;
					}
				}
			}

		} catch (Exception e) {
			LOGGER.error("Error occur while search employee details " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - escalation search"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("User Management");
			auditRecord.setActivityCode("SEARCH");
			auditRecordService.save(auditRecord, device);
		}
		return mv;
	}
}
