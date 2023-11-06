package com.ksv.ktrccrm.controller;

import java.util.List;
import java.util.Objects;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.RoleMst;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.RoleService;
import com.ksv.ktrccrm.service.UserService;

@Controller
public class RoleHierarchyController {
	private static final Logger LOGGER = LogManager.getLogger(RoleHierarchyController.class);

	@Autowired
	private RoleService roleService;
	@Autowired
	private AuditRecordService auditRecordService;
	@Autowired UserService userService;
	

	/* For List Display */
	@GetMapping("/roleHierarchyList")
	public String getAllMenus(@ModelAttribute("roleMst") RoleMst roleMst, Model model, BindingResult result,
			Device device) throws Exception {
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
			List<RoleMst> role = roleService.getRecordList(ProdConstant.TRUE, tenantId);
			if (Objects.nonNull(role)) {
				model.addAttribute("listRole", role);
			}
		} 
			}
		}catch (Exception e) {
			LOGGER.error("Error occur while display role hierarcy list " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed role hierarchy list"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Role Hierarcy Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "rolehierarchy/rolehierarchy";
	}
}
