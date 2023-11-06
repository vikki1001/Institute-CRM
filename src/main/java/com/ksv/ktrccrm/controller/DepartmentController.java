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
import com.ksv.ktrccrm.db1.entities.DepartmentMst;
import com.ksv.ktrccrm.db1.entities.RoleMst;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.DepartmentService;
import com.ksv.ktrccrm.service.RoleService;
import com.ksv.ktrccrm.service.UserService;
import com.ksv.ktrccrm.validator.DepartmentValidator;

@Controller
public class DepartmentController {
	private static final Logger LOGGER = LogManager.getLogger(DepartmentController.class);

	@Autowired
	private DepartmentValidator departmentValidator;

	@InitBinder
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(departmentValidator);
	}

	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private AuditRecordService auditRecordService;
	@Autowired
	private UserService userService;

	@Value("${global.redirectmsg.save}")
	private String successSaveMsg;

	@Value("${global.redirect.update}")
	private String successUpdateMsg;

	@GetMapping(value = "/activeDepartment")
	public String getIsActiveDepartment(Model model, Device device) throws Exception {
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
					List<DepartmentMst> departmentMstList = departmentService.getRecordList(ProdConstant.TRUE,
							tenantId);
					
					if (Objects.nonNull(departmentMstList)) {
						model.addAttribute("listdepartment", departmentMstList);
					}
					
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur to display active department list " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed active department list"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Department Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "department/activedepartment";
	}

	@GetMapping(value = "/inActiveDepartment")
	public String getInActiveDepartment(Model model, Device device) throws Exception {
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
					List<DepartmentMst> departmentMstList = departmentService.getRecordList(ProdConstant.FALSE,
							tenantId);
					if (Objects.nonNull(departmentMstList)) {
						model.addAttribute("listdepartment", departmentMstList);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur to display inactive department list " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed inActive department list"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Department Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "department/inactivedepartment";
	}

	// @GetMapping(value = "/department")
	@RequestMapping(value = { "/department", "/departmentUpdate/{departmentId}" }, method = RequestMethod.GET)
	public String department(@PathVariable(name = "departmentId", required = false) Long departmentId, Model model,
			Device device) throws Exception {
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
					DepartmentMst departmentMst = new DepartmentMst();
					List<RoleMst> getIsActiveRole = roleService.getRecordList(ProdConstant.TRUE, tenantId);
					
					if (Objects.nonNull(getIsActiveRole)) {
						model.addAttribute("listRole", getIsActiveRole);
					}
		
					if (departmentId != null) {
						departmentMst = departmentService.getByDepartmentId(departmentId);
						
						if (Objects.nonNull(departmentMst)) {
							model.addAttribute("departmentMst", departmentMst);
						}
					} else {
						model.addAttribute("departmentMst", departmentMst);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur to open department registration page " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed department management form"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Department Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "department/newdepartment";
	}

	@PostMapping(value = "/saveDepartment")
	public String successDepartment(@Valid @ModelAttribute DepartmentMst departmentMst, BindingResult result,
			Model model, Device device, RedirectAttributes redirAttrs) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		Long deptId = departmentMst.getDepartmentId();
		String empId = null;
		String tenantId = null;
		UserMst user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			empId = authentication.getName();
	   System.out.println("empid = "+empId);
			if (Objects.nonNull(empId)) {
				user = userService.getUserById(empId);
				tenantId = user.getTenantId();
		System.out.println("tanent id = "+tenantId);		
				if (Objects.nonNull(tenantId)) {
					List<RoleMst> getIsActiveRole = roleService.getRecordList(ProdConstant.TRUE, tenantId);
				
					if (Objects.nonNull(getIsActiveRole)) {
						model.addAttribute("listRole", getIsActiveRole);
					}
			
					if (result.hasErrors()) {
						List<ObjectError> allErrors = result.getAllErrors();
						for (ObjectError temp : allErrors) {
							System.out.println(temp);
						}
	    				
						return "department/newdepartment";
					} else if (deptId == null) {
						if (departmentService.departmentExists(departmentMst.getDepartmentName().toUpperCase())) {
							result.addError(new FieldError("departmentMst", "departmentName",
									"Department Name already in use"));
							return "department/newdepartment";
						}
					}
		/* Save Department to Database */
					departmentService.saveDepartment(departmentMst);
	    System.out.println("department Save");		
					redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while department registration successfully " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - create new department"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Department Management");
			auditRecord.setActivityCode("CREATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeDepartment";
	}

//	@GetMapping(value = "/departmentUpdate/{departmentId}")
//	public ModelAndView DepartmentUpdate(@PathVariable(name = "departmentId") Long departmentId, Model model,
//			Device device) throws Exception {
//		ModelAndView mav = new ModelAndView("department/newdepartment");
//		AuditRecord auditRecord = new AuditRecord();
//		try {
//			 List<RoleMst> getIsActiveRole =roleService.getRecordList(ProdConstant.TRUE);
//				if(Objects.nonNull(getIsActiveRole)) {
//					mav.addObject("listRole", getIsActiveRole);
//				}
//			DepartmentMst departmentMst = departmentService.getByDepartmentId(departmentId);
//			if (Objects.nonNull(departmentMst)) {
//				mav.addObject("departmentMst", departmentMst);
//			}
//		} catch (Exception e) {
//			LOGGER.error("Error occur while display edit registered department page " + ExceptionUtils.getStackTrace(e));
//		} finally {
//			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed department id - " + departmentId));
//			auditRecord.setMenuCode("User Administrator");
//			auditRecord.setSubMenuCode("Department Management");
//			auditRecordService.save(auditRecord, device);
//		}
//		return mav;
//	}

	@PostMapping(value = "/updateDepartment")
	public String updateDepartment(@Valid @ModelAttribute DepartmentMst departmentMst, BindingResult result,
			Model model, Device device, RedirectAttributes redirAttrs) throws Exception {
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
					List<RoleMst> getIsActiveRole = roleService.getRecordList(ProdConstant.TRUE, tenantId);
					if (Objects.nonNull(getIsActiveRole)) {
						model.addAttribute("listRole", getIsActiveRole);
					}
					if (result.hasErrors()) {
						List<ObjectError> allErrors = result.getAllErrors();
						for (ObjectError temp : allErrors) {
							System.out.println(temp);
						}
						return "department/updatedepartment";
					} else {
						/* Save Department to Database */
						departmentService.saveDepartment(departmentMst);
						redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successUpdateMsg);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while update registration successfully " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - update registered department"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Department Management");
			auditRecord.setActivityCode("UPDATE");
			auditRecordService.save(auditRecord, device);
		}

		return "redirect:/activeDepartment";
	}

	/* Deactivate of Registered Department */
	@GetMapping(value = "/deactivedepartment/{departmentId}")
	public String deactiveDepartment(@PathVariable(name = "departmentId") Long departmentId, Device device)
			throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			DepartmentMst departmentMst = departmentService.deptEnableAndDisable(departmentId);
			if (Objects.nonNull(departmentMst)) {
				return "redirect:/activeDepartment";
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while activate department" + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - deactivate department id - " + departmentId));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Department Management");
			auditRecord.setActivityCode("DEACTIVATE");
			auditRecordService.save(auditRecord, device);
		}

		return "redirect:/activeDepartment";
	}

	/* Activate of Registered Department */
	@GetMapping(value = "/activedepartment/{departmentId}")
	public String activeDepartment(@PathVariable(name = "departmentId") Long departmentId, Device device)
			throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			DepartmentMst departmentMst = departmentService.deptEnableAndDisable(departmentId);
			if (Objects.nonNull(departmentMst)) {
				return "redirect:/inActiveDepartment";
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while inactivate department " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - activate department id - " + departmentId));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Department Management");
			auditRecord.setActivityCode("ACTIVATE");
			auditRecordService.save(auditRecord, device);
		}

		return "redirect:/inActiveDepartment";
	}
}
