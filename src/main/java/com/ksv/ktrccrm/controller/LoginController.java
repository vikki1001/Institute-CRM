package com.ksv.ktrccrm.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.ksv.ktrccrm.captcha.CaptchaUtil;
import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.impl.CheckInCheckOutDaoImpl;
import com.ksv.ktrccrm.dto.WebSessionObject;
import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.CheckInCheckOut;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.ExitActivity;
import com.ksv.ktrccrm.db1.entities.ExpenseReimb;
import com.ksv.ktrccrm.db1.entities.LeaveMst;
import com.ksv.ktrccrm.db1.entities.MenuMst;
import com.ksv.ktrccrm.db1.entities.RoleMenuActionAccess;
import com.ksv.ktrccrm.db1.entities.TenantMst;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.CheckInCheckOutService;
import com.ksv.ktrccrm.service.EmpRegistartionService;
import com.ksv.ktrccrm.service.ExitActivityService;
import com.ksv.ktrccrm.service.ExpenseReimbService;
import com.ksv.ktrccrm.service.LeaveService;
import com.ksv.ktrccrm.service.MenuService;
import com.ksv.ktrccrm.service.RoleMenuActionAccessService;
import com.ksv.ktrccrm.service.TenantMstService;
import com.ksv.ktrccrm.service.UserService;

import cn.apiclub.captcha.Captcha;

@Controller
public class LoginController {
	private static final Logger LOGGER = LogManager.getLogger(LoginController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private RoleMenuActionAccessService roleMenuActionAccessService;
	@Autowired
	private MenuService menuMstService;
	@Autowired
	private AuditRecordService auditRecordService;
	@Autowired
	private EmpRegistartionService empRegistartionService;
	@Autowired
	private TenantMstService tenantMstService;
	@Autowired
	private CheckInCheckOutService checkInCheckOutService;
	@Autowired
	private ExitActivityService exitActivityService;
	@Autowired
	private ExpenseReimbService expenseReimbService;
	@Autowired
	private LeaveService leaveService;
	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;

	@GetMapping(value = "/success")
	public String success() {
		return "success";
	}
	
	@GetMapping(value = "/error")
	public String errorPage() throws Exception {
		try {
		} catch (Exception e) {
			LOGGER.error("Error while open error page... " + ExceptionUtils.getStackTrace(e));
		}
		return "error";
	}

	/* For Captcha */
	private void getCaptcha(UserMst userMst) {
		Captcha captcha = CaptchaUtil.createCaptcha(240, 70);
		userMst.setHiddenCaptcha(captcha.getAnswer());
		userMst.setCaptcha(""); // value entered by the User
		userMst.setRealCaptcha(CaptchaUtil.encodeCaptcha(captcha));
	}
	
	@GetMapping("/login")
	public String viewLogin(Model model, Device device) throws Exception {
		System.out.println("Login method call..........");
		try {
			UserMst userMst = new UserMst();
			getCaptcha(userMst);
			model.addAttribute("userMst", userMst);
		} catch (Exception e) {
			LOGGER.error("Error while open login page " + ExceptionUtils.getStackTrace(e));
		}
		return "login/login";
	}

	@GetMapping("/loginSuccess")
	public String loginSuccess(@ModelAttribute UserMst userMst, Authentication auth, HttpServletRequest request,
			HttpSession session, BindingResult result, Model model, Device device) throws Exception {
		System.out.println("Inside login success......");
		AuditRecord auditRecord = new AuditRecord();
		WebSessionObject wso = new WebSessionObject();
		String userId = null;
		try {
			// For Authentication of the user details created by Owner Date 01-11-2021:23:52
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			userId = authentication.getName();
			UserMst userDetail = null;
			EmpBasicDetails empBasicDetails = null;
			TenantMst tenantMst = null;
			if (userId != null) {
				// Get Active user details created by Owner Date 01-11-2021:23:52
				userDetail = userService.getUserDetails(userId, ProdConstant.ISACTIVE);
				empBasicDetails = empRegistartionService.getCurrentUser(userId);
				tenantMst = tenantMstService.getTenantDetails(userDetail.getTenantId(),ProdConstant.TRUE);

				List<CheckInCheckOut> addNewReqNotificationBell = checkInCheckOutService.addNewReqNotificationBell(userId);
				List<ExitActivity> exitActivityNotificationBell = exitActivityService.exitActivityNotificationBell(userId);
				List<ExpenseReimb> expenseReimbNotificationBell = expenseReimbService.expenseReimbNotificationBell();
				List<LeaveMst> leaveMstsNotificationBell = leaveService.leaveMstsNotificationBell(userId);
			
				if (userDetail != null) {
					wso.setLoginId(userDetail.getLoginId());
					wso.setFirstName(userDetail.getFirstName());
					wso.setTenantId(userDetail.getTenantId());
					wso.setBranch(userDetail.getBranch());
					wso.setMainRole(userDetail.getMainRole());
					wso.setLastSuccessfullLoginDateTime(userDetail.getLastSuccessfullLoginDateTime());
					wso.setLastName(userDetail.getLastName());

					/* Display Current Employee Image in Header */
					wso.setEmpId(empBasicDetails.getEmpId());
					wso.setFile(empBasicDetails.getFile());

					/* Get Logo From DataBase */
					wso.setTenantLogoPath(tenantMst.getTenantLogoPath());
					wso.setTenantFaviconPath(tenantMst.getTenantFaviconPath());

					/* Notification Alert */
					wso.setAddNewReqNotification(addNewReqNotificationBell);
					wso.setExitActivityNotification(exitActivityNotificationBell);
					wso.setExpenseReimbNotification(expenseReimbNotificationBell);
					wso.setLeaveMstNotification(leaveMstsNotificationBell);
					

					// Check user Bad Login Count created By Owner Date 02-11-2021:00:14
					if (userDetail.getConsequetiveBadLoginCount() > 0) {
						
					} else if (userDetail.getMainRole() != null) {
						Set<String> userMenuCodeSet = new HashSet<>();
						List<RoleMenuActionAccess> rmaaList = roleMenuActionAccessService.getMenuListByModuleRoleMap(
								userDetail.getTenantId().trim(), userDetail.getMainRole().trim(), ProdConstant.TRUE);
						for (RoleMenuActionAccess rm : rmaaList) {
							LOGGER.info("Value is empty from rm:::::" + rm.getMenuCode());
						}

						if (null != rmaaList && !rmaaList.isEmpty()) {
							Set<String> menuCodeSet = rmaaList.stream().map(x -> {
								return x.getMenuCode().trim();
							}).collect(Collectors.toSet());

							for (String str : menuCodeSet) {
								LOGGER.info("STRING::::" + str);  
							}

							List<MenuMst> menuCodeList = menuMstService.getMenuMstIn(userDetail.getTenantId().trim(),
									menuCodeSet);
							for (MenuMst menu : menuCodeList) {
								LOGGER.info("STRING::::" + menu.getMenuName());
							}
							if (menuCodeList != null && !menuCodeList.isEmpty()) {
								// this is where user accessible menus are decided
								for (RoleMenuActionAccess rmaa : rmaaList) {
									userMenuCodeSet.addAll(menuCodeList.stream().filter(
											x -> x.getMenuCode().trim().equalsIgnoreCase(rmaa.getMenuCode().trim()))
											.map(x -> {
												return x.getMenuCode().trim();
											}).collect(Collectors.toSet()));
									// wso.setMenuMst(menuCodeList.get(menuCodeList.size() - 1));
								}
							}
						} else {
							System.out.println(" Nothing Happened..... ");
						}

						Map<String, List<MenuMst>> accessibleMenuMap1 = new HashMap<String, List<MenuMst>>();
						accessibleMenuMap1 = menuMstService.getAccessibleMenu(userDetail.getTenantId().trim(),
								wso.getLanguage(), userMenuCodeSet);
						Map<String, List<MenuMst>> menuLHMap1 = new LinkedHashMap<String, List<MenuMst>>();
						// Sorting the accessibleMenuMap by key
						/************************************************************************************************************/
						Set<Map.Entry<String, List<MenuMst>>> menuSet1 = accessibleMenuMap1.entrySet();
						List<Map.Entry<String, List<MenuMst>>> menuList1 = new ArrayList<Map.Entry<String, List<MenuMst>>>(
								menuSet1);
						Collections.sort(menuList1, new Comparator<Map.Entry<String, List<MenuMst>>>() {
							public int compare(Entry<String, List<MenuMst>> arg0, Entry<String, List<MenuMst>> arg1) {
								return arg0.getKey().compareTo(arg1.getKey());
							}
						});

						for (Map.Entry<String, List<MenuMst>> localMap : menuList1) {
							menuLHMap1.put(localMap.getKey(), localMap.getValue());
						}
						wso.setMenuLHMap(menuLHMap1);
					}
				} else {
					System.out.println("Else 1 ::::::::::::::::::::::::::::::::");
					return "login/login";
				}

			} else {
				System.out.println("Else 2 ::::::::::::::::::::::::::::::::");
				return "login/login";
			}
			session.setAttribute("webSession", wso);

			/* To set login date time */
			Optional<UserMst> optional = userService.findLoginByLoginId(userId);
			if (optional.isPresent()) {
				UserMst newUserMst = optional.get();
				newUserMst.setLastSuccessfullLoginDateTime(checkOutDaoImpl.getDateTime());
				userService.saveOrUpdateUser(newUserMst);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur in login success " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - login "));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Login");
			auditRecord.setActivityCode("LOGIN");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/empDashboard";
	}

	
	@GetMapping("/check-session")
	public ResponseEntity<String> checkSession() {
	    return new ResponseEntity<>(HttpStatus.OK);
	}
	
}