package com.ksv.ktrccrm.controller;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.BranchMst;
import com.ksv.ktrccrm.db1.entities.RoleMst;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.BranchService;
import com.ksv.ktrccrm.service.RoleService;
import com.ksv.ktrccrm.service.UserService;

@Controller
public class AuditRecordController {
	private static final Logger LOGGER = LogManager.getLogger(AuditRecordController.class);

	@Autowired
	private BranchService branchService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private AuditRecordService auditRecordService;
	@Autowired
	private UserService userService;
	
	/* Audit Record List */
	@GetMapping(value = "/auditRecordList")
	public String homeAllRecord(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String empId = null;
		String tenantId = null;
		UserMst user = null;
		try {
			empId = authentication.getName();
			if (Objects.nonNull(empId)) {
				user = userService.getUserById(empId);
				tenantId = user.getTenantId();
				if (Objects.nonNull(tenantId)) {
					UserMst userMst = new UserMst();
					userMst.setLoginId(ProdConstant.ALL);
					List<UserMst> userMst2 = auditRecordService.getAllUser(tenantId);
					userMst2.add(userMst);
					Collections.sort(userMst2, UserMst.Comparators.LOGINID);
					if (Objects.nonNull(userMst2)) {
						model.addAttribute("allUsers", userMst2);
					}
				}
				BranchMst branchMst = new BranchMst();
				branchMst.setBranchName(ProdConstant.ALL);
				List<BranchMst> branchMst2 = branchService.getRecordList(ProdConstant.TRUE, tenantId);
				branchMst2.add(branchMst);
				Collections.sort(branchMst2, BranchMst.Comparators.BRANCH);
				if (Objects.nonNull(branchMst2)) {
					model.addAttribute("listBranch", branchMst2);
				}

			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display audit record list " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed audit record list"));
			auditRecord.setMenuCode("Audit Record");
			auditRecord.setSubMenuCode("Audit Listing Report");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "auditrecord/auditrecordlist";
	}

	/* Search Data using fromDate, toDate, loginId & branchName */
	@GetMapping(value = "/between/fromortoorloginIdorbranchName")
	public String getAllRecord(@RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
			@RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd") Date to, @RequestParam String loginId,
			@RequestParam String branchName, Model model, Device device, RedirectAttributes redirAttrs)
			throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String empId = null;
		String tenantId = null;
		UserMst user = null;
		try {
			List<AuditRecord> auditRecords = auditRecordService.findByFromToDateBetweenOrLoginIdOrBranchName(from, to,
					loginId, branchName);
			empId = authentication.getName();
			if (Objects.nonNull(empId)) {
				user = userService.getUserById(empId);
				tenantId = user.getTenantId();
				if (Objects.nonNull(tenantId)) {
					UserMst userMst = new UserMst();
					userMst.setLoginId(ProdConstant.ALL);
					List<UserMst> userMst2 = auditRecordService.getAllUser(tenantId);
					userMst2.add(userMst);
					Collections.sort(userMst2, UserMst.Comparators.LOGINID);
					if (Objects.nonNull(userMst2)) {
						model.addAttribute("allUsers", userMst2);
					}
				}
			}
			BranchMst branchMst = new BranchMst();
			branchMst.setBranchName(ProdConstant.ALL);
			List<BranchMst> branchMst2 = branchService.getRecordList(ProdConstant.TRUE, tenantId);
			branchMst2.add(branchMst);
			Collections.sort(branchMst2, BranchMst.Comparators.BRANCH);
			if (Objects.nonNull(branchMst2)) {
				model.addAttribute("listBranch", branchMst2);
			}
			if (Objects.nonNull(auditRecords)) {
				model.addAttribute("listAuditRecord", auditRecords);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while search audit record " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - search viewed audit record list"));
			auditRecord.setMenuCode("Audit Record");
			auditRecord.setActivityCode("SEARCH-VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "auditrecord/auditrecordlist";
	}

	/* User Audit Record List */
	@GetMapping(value = "/userAudit")
	public String homeAllUserRecord(AuditRecord auditRecord, Model model, Device device) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String empId = null;
		String tenantId = null;
		UserMst user = null;
		try {
			empId = authentication.getName();
			if (Objects.nonNull(empId)) {
				user = userService.getUserById(empId);
				tenantId = user.getTenantId();
				if (Objects.nonNull(tenantId)) {
					UserMst userMst = new UserMst();
					userMst.setLoginId(ProdConstant.ALL);
					List<UserMst> userMst2 = auditRecordService.getAllUser(tenantId);
					userMst2.add(userMst);
					Collections.sort(userMst2, UserMst.Comparators.LOGINID);
					if (Objects.nonNull(userMst2)) {
						model.addAttribute("allUsers", userMst2);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display user audit list " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed user audit record list"));
			auditRecord.setMenuCode("Audit Record");
			auditRecord.setSubMenuCode("User Audit");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "auditrecord/userauditrecordlist";
	}

	/* Search Data using fromDate, toDate & loginId */
	@GetMapping(value = "/between/fromortoorloginId")
	public String getAllUserRecord(@RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
			@RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd") Date to, @RequestParam String loginId,
			Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String empId = null;
		String tenantId = null;
		UserMst user = null;
		try {
			List<AuditRecord> auditRecordList = auditRecordService.findByFromToDateBetweenOrLoginId(from, to, loginId);
			empId = authentication.getName();
			if (Objects.nonNull(empId)) {
				user = userService.getUserById(empId);
				tenantId = user.getTenantId();
				if (Objects.nonNull(tenantId)) {
			UserMst userMst = new UserMst();
			userMst.setLoginId(ProdConstant.ALL);
			List<UserMst> userMst2 = auditRecordService.getAllUser(tenantId);
			userMst2.add(userMst);
			Collections.sort(userMst2, UserMst.Comparators.LOGINID);
			if (Objects.nonNull(userMst2)) {
				model.addAttribute("allUsers", userMst2);
			}
				}
			}
			
			if (Objects.nonNull(auditRecordList)) {
				model.addAttribute("listAuditRecord", auditRecordList);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get all user record" + ExceptionUtils.getStackTrace(e));
		} finally {
		
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - search viewed user audit record list"));
			auditRecord.setMenuCode("Audit Record");
			auditRecord.setSubMenuCode("User Audit");
			auditRecord.setActivityCode("SEARCH-VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "auditrecord/userauditrecordlist";
	}

	/* Role Audit Record List */
	@GetMapping(value = "/roleAudit")
	public String homeAllRoleRecord(AuditRecord auditRecord, Model model, Device device) throws Exception {
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
			RoleMst roleMst = new RoleMst();
			roleMst.setRoleCode(ProdConstant.ALL);
			List<RoleMst> listRoleMst = roleService.getRecordList(ProdConstant.TRUE, tenantId);
			listRoleMst.add(roleMst);
			Collections.sort(listRoleMst, RoleMst.Comparators.ROLECODE);
			if (Objects.nonNull(listRoleMst)) {
				model.addAttribute("listRole", listRoleMst);
			}
		}
			}
		}catch (Exception e) {
			LOGGER.error("Error occur while display role audit list " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed role audit record list"));
			auditRecord.setMenuCode("Audit Record");
			auditRecord.setSubMenuCode("Role Audit");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "auditrecord/rolebasedauditrecord";
	}

	/* Search Data using fromDate, toDate & roleCode */
	@GetMapping(value = "/between/fromortoorroleCode")
	public String getAllRoleRecord(@RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
			@RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd") Date to, @RequestParam String roleCode,
			Model model, Device device) throws Exception {
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
			List<AuditRecord> auditRecordList = auditRecordService.findByFromToDateBetweenOrRoleCode(from, to,
					roleCode);

			RoleMst roleMst = new RoleMst();
			roleMst.setRoleCode(ProdConstant.ALL);
			List<RoleMst> listRoleMst = roleService.getRecordList(ProdConstant.TRUE, tenantId);
			listRoleMst.add(roleMst);
			Collections.sort(listRoleMst, RoleMst.Comparators.ROLECODE);
			if (Objects.nonNull(listRoleMst)) {
				model.addAttribute("listRole", listRoleMst);
			}
			if (Objects.nonNull(auditRecordList)) {
				model.addAttribute("listAuditRecord", auditRecordList);
			}
		}
			}
		}catch (Exception e) {
			LOGGER.error("Error occur while get all role record " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - search viewed role audit record list"));
			auditRecord.setMenuCode("Audit Record");
			auditRecord.setSubMenuCode("Role Audit");
			auditRecord.setActivityCode("SEARCH-VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "auditrecord/rolebasedauditrecord";
	}
}
