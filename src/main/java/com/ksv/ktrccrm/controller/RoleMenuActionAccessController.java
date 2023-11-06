package com.ksv.ktrccrm.controller;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.MenuMst;
import com.ksv.ktrccrm.db1.entities.RoleMenuActionAccess;
import com.ksv.ktrccrm.db1.entities.RoleMst;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.MenuService;
import com.ksv.ktrccrm.service.RoleMenuActionAccessService;
import com.ksv.ktrccrm.service.RoleService;
import com.ksv.ktrccrm.service.UserService;

@Controller
public class RoleMenuActionAccessController {
	private static final Logger LOGGER = LogManager.getLogger(RoleMenuActionAccessController.class);

	@Autowired
	private RoleService roleService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private RoleMenuActionAccessService roleMenuActionAccessService;
	@Autowired
	private AuditRecordService auditRecordService;
	@Autowired
	private UserService userService;
	
	@Value("${global.redirectmsg.save}")
	private String successSaveMsg;

	/* For List Display */
	@GetMapping(value = "/roleMenuActionAccess")
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
			List<MenuMst> getMenuItem =  menuService.getAllIsActive();
			if(Objects.nonNull(getMenuItem)) {
				model.addAttribute("menuItem",getMenuItem);	
			}			
			List<RoleMst> role = roleService.getRecordList(ProdConstant.TRUE, tenantId);
			if (Objects.nonNull(role)) {
				model.addAttribute("listRole", role);
			}
		}
			}
		}catch (Exception e) {
			LOGGER.error("Error occur while display role menu action access list " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed role menu action access list"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Role Menu Action Access Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "rolemenuactionaccess/rolemenuactionaccess";
	}

	@PostMapping(value = "/menuDetails")
	public String selectRole(@ModelAttribute("roleMst") RoleMst roleMst, Model model, BindingResult result,
			String roleCode,RedirectAttributes redirects, Device device) throws Exception {
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
			if(Objects.nonNull(role)) {
				model.addAttribute("listRole", role);
			}
				}
			}
			System.out.println("Check :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::"
					+ roleMst.getRoleCode());
			
			List<MenuMst> getMenu = menuService.getMenu(roleCode);
			if(Objects.nonNull(getMenu)) {
				model.addAttribute("menuItem", getMenu);
			}
			/*
			 * System.out.println("menu list-----" + menuService.getAllIsActive());
			 * System.out.println("role menu action access list-----" +
			 * roleMenuActionAccessService.getRoleMenuActionAccessListByRoleCode(roleCode));
			 */

			List<MenuMst> menuList = menuService.getAllIsActive();
			System.out.println("Menu List ++++++" + menuList);

			List<RoleMenuActionAccess> roleMenuActionAccessList = roleMenuActionAccessService
					.getRoleMenuActionAccessListByRoleCode(roleCode);
			System.out.println("Role Menu Action Access List +++++" + roleMenuActionAccessList);
			System.out.println("------------------------------------------------------------------------------");
			boolean isEqual = menuList.equals(roleMenuActionAccessList);
			System.out.println("Compare Two Table Data ::::" + isEqual);

			/*
			 * List<Object> newList = new ArrayList<>(); newList.addAll(menuList);
			 * newList.addAll(roleMenuActionAccessList);
			 * System.out.println("Combine List:::::::::::::::::::::::::::" +
			 * newList.toString());
			 */

			System.out.println("------------------------------------------------------------------------------");
			// menuList.retainAll(roleMenuActionAccessList);
			System.out.println("Common Data from Two Different list..................." + menuList);
			System.out.println("------------------------------------------------------------------------------");
			/*
			 * List<Object> list = new ArrayList<>(menuList);
			 * list.retainAll(roleMenuActionAccessList);
			 * System.out.println("Common Data from Two list................." +
			 * list.toString());
			 */

//			List<Object> newList = Stream.concat(menuList.stream(), roleMenuActionAccessList.stream())
//                    .collect(Collectors.toList());
//			System.out.println("Menu List using Stream ::::" + menuList);
//			System.out.println("Role Menu Action Access List using Stream ::::" + roleMenuActionAccessList);
//			System.out.println("------------------------------------------------------------------------------");
//			System.out.println("Compare List using Stream ::::::::::::::::" + newList);

			Set<Object> set = new LinkedHashSet<>(menuList);
			set.addAll(roleMenuActionAccessList);
			List<Object> combinedlist = new ArrayList<>(set);
			System.out.println("List 1 - Menu List ::::" + menuList);
			System.out.println("List 2 - Role Menu Action Access List ::::" + roleMenuActionAccessList);
			System.out.println("------------------------------------------------------------------------------");
			System.out.println("Compare List ::::::" + combinedlist);

		} catch (Exception e) {
			LOGGER.error("Error occur while opening menu list " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord
					.setRemarks(userDetails.getUsername().concat(" - setRole in role menu action access management"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Role Menu Action Access Management");
			auditRecord.setActivityCode("CREATE");
			auditRecordService.save(auditRecord, device);
			redirects.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
		}
		return "rolemenuactionaccess/rolemenuactionaccess";
	}

}
