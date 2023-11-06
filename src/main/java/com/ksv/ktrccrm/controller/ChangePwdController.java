package com.ksv.ktrccrm.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.ChangePwdService;
import com.ksv.ktrccrm.service.UserService;
import com.ksv.ktrccrm.validator.ChangePwdValidator;


@Controller
public class ChangePwdController {
	private static final Logger LOGGER = LogManager.getLogger(ChangePwdController.class);

	@Autowired
	private ChangePwdValidator userValidator;

	@InitBinder
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(userValidator);
	}

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UserService userService;
	@Autowired
	private ChangePwdService changePwdService;
	@Autowired
	private AuditRecordService auditRecordService;
	
	@Value("${global.redirectmsg.save}")
	private String successSaveMsg;
	
	
	@GetMapping(value = "/changePassword")
	public String showEditProductPage(Model model, @ModelAttribute UserMst userMst, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String loginId = null;
		try {
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			loginId = authentication.getName();
			System.out.println("Call changePwd mehtod...... " + loginId);
			UserMst userDetails = null;
			if (Objects.nonNull(loginId)) {
				userDetails = userService.getUserDetails(loginId, ProdConstant.ISACTIVE);
				if (Objects.nonNull(userDetails)) {
					model.addAttribute("userMst", userDetails);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display change password page " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed change password form"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Change Password");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "passwordpolicy/changepassword";
	}

//	@PostMapping(value = "/changePassword")
//	public String changePassword(@Valid @ModelAttribute UserMst userMst, BindingResult result,
//			@RequestParam("password") String password, @RequestParam("newPassword") String newPassword,
//			@RequestParam("confirmPassword") String confirmPassword, Principal principal, Device device,
//			RedirectAttributes redirAttrs) throws Exception {
//		AuditRecord auditRecord = new AuditRecord();
//		String loginId = principal.getName();
//		UserMst currentUser = changePwdService.getUserByLoginId(loginId);
//		try {
//			if (result.hasErrors()) {
//				System.out.println("change password validation");
//				List<ObjectError> allErrors = result.getAllErrors();
//				for (ObjectError temp : allErrors) {
//					System.out.println(temp);
//				}
//				return "passwordpolicy/changepassword";
//			} else if (this.bCryptPasswordEncoder.matches(password, currentUser.getPassword())) {
//				currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
//				currentUser.setPasswordToken(UUID.randomUUID().toString());
//				String secLastPwd = currentUser.getSecondLastPassword();
//				currentUser.setThirdLastpassword(secLastPwd);
//				String lastPwd = currentUser.getLastPassword();
//				currentUser.setSecondLastPassword(lastPwd);
//				String currentPwd = currentUser.getPassword();
//				currentUser.setLastPassword(this.bCryptPasswordEncoder.encode(currentPwd));
//				
//				
//				this.changePwdService.changePwd(currentUser);
//				   
//				redirAttrs.addFlashAttribute("notMatch", "Old Password not Match..");
//				System.out.println("Old Password not match form db ::::::::::::");
//			} else if (!Objects.equals(newPassword, confirmPassword)) {
//				redirAttrs.addFlashAttribute("pwdNotMatch", "new Password & Confirm New Password not match");
//				System.out.println("new Password & Confirm New Password not match:::::::::: ");
//			} else {
//				Optional<UserMst> optional = userService.findLoginByLoginId(loginId);
//				if (optional.isPresent()) {
//					UserMst newUserMst = optional.get();
//					newUserMst.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
//					newUserMst.setPasswordToken(UUID.randomUUID().toString());
//					userService.saveOrUpdateUser(newUserMst);
//					redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
//					System.out.println("Change Password Successfully");
//
//				}else {
//					System.err.println("Password not Change :::::::::::::::::");
//				}
//			}
//		} catch (Exception e) {
//			LOGGER.error("Error occur while change password    " + ExceptionUtils.getStackTrace(e));
//		} finally {
//			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//			auditRecord.setRemarks(userDetails.getUsername().concat(" - change password successfully "));
//			auditRecord.setMenuCode("User Administrator");
//			auditRecord.setSubMenuCode("Change Password");
//			auditRecord.setActivityCode("UPDATE");
//			auditRecordService.save(auditRecord, device);
//		}
//		return "redirect:/changePassword";
//	}
//}
	@PostMapping(value = "/changePassword")
	public String changePassword(@Valid @ModelAttribute UserMst userMst, BindingResult result,
			@RequestParam("password") String password, @RequestParam("newPassword") String newPassword,
			@RequestParam("confirmPassword") String confirmPassword, Principal principal, Device device,
			RedirectAttributes redirAttrs) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String loginId = principal.getName();
		UserMst currentUser = changePwdService.getUserByLoginId(loginId);

		try {
			userMst.setLoginId(loginId);
			 if (this.bCryptPasswordEncoder.matches(password, currentUser.getPassword())) {
				 System.out.println("NewPassword.........."+ newPassword);
				 System.out.println("confirmPassword.........."+ confirmPassword);
				 if (!Objects.equals(newPassword, confirmPassword)) {
					redirAttrs.addFlashAttribute("pwdNotMatch", "new Password & Confirm New Password not match");
					System.out.println("new Password & Confirm New Password not match:::::::::: ");
				}
				 else  if (result.hasErrors()) {
						System.out.println("change password validation");
						List<ObjectError> allErrors = result.getAllErrors();
						for (ObjectError temp : allErrors) {
							System.out.println(temp);
						}
						return "passwordpolicy/changepassword";
					}
				  else {
				  System.out.println("After check the validation..**********************************...");
				currentUser.setThirdLastpassword(currentUser.getSecondLastPassword());
				currentUser.setSecondLastPassword(currentUser.getLastPassword());
				currentUser.setLastPassword(this.bCryptPasswordEncoder.encode(newPassword));
				currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
				currentUser.setPasswordToken(UUID.randomUUID().toString());
				
//				currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
//				currentUser.setPasswordToken(UUID.randomUUID().toString());
//				String secLastPwd = currentUser.getSecondLastPassword();
//				currentUser.setThirdLastpassword(secLastPwd);
//				String lastPwd = currentUser.getLastPassword();
//				currentUser.setSecondLastPassword(lastPwd);
//				String currentPwd = currentUser.getPassword();
//				currentUser.setLastPassword(this.bCryptPasswordEncoder.encode(currentPwd));
				
				userService.saveOrUpdateUser(currentUser);
				//this.changePwdService.changePwd(currentUser);
				redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
				System.out.println("Change Password Successfully");
				
//				Optional<UserMst> optional = userService.findLoginByLoginId(loginId);
//				if (optional.isPresent()) {
//					UserMst newUserMst = optional.get();
//					newUserMst.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
//					newUserMst.setPasswordToken(UUID.randomUUID().toString());
//					userService.saveOrUpdateUser(newUserMst);
//					redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
//					System.out.println("Change Password Successfully");
//				  }
			}
			 }	else {   
				redirAttrs.addFlashAttribute("notMatch", "Old Password not Match..");
				System.out.println("Old Password not match form db ::::::::::::");
		
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while change password    " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - change password successfully "));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Change Password");
			auditRecord.setActivityCode("UPDATE");
			auditRecordService.save(auditRecord, device);
		}
		
			return "redirect:/changePassword";
		
	
	}
	@GetMapping("/changePwd")
	public String changePwdAtTheTimeOgLogin(Model model,@ModelAttribute UserMst userMst,Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String loginId = null;
		try {		
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			loginId = authentication.getName();
			System.out.println("Call changePwd mehtod...... " + loginId);
			UserMst userDetails = null;
			if (Objects.nonNull(loginId)) {
				userDetails = userService.getUserDetails(loginId, ProdConstant.ISACTIVE);
				if (Objects.nonNull(userDetails)) {
					model.addAttribute("userMst", userDetails);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display change password page " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed change password form"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Change Password");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "passwordpolicy/changePwdLoginTime";
	}
	@PostMapping("/changePwdLoginTime")
	public String changePwdLoginTime(@Valid @ModelAttribute UserMst userMst, BindingResult result, 	@RequestParam("password") String password, @RequestParam("newPassword") String newPassword,
			@RequestParam("confirmPassword") String confirmPassword, Principal principal, Device device,
			RedirectAttributes redirAttrs) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String loginId = principal.getName();
		UserMst currentUser = changePwdService.getUserByLoginId(loginId);
		System.out.println("Inside changePwd method.................." + password);
		try {
			userMst.setLoginId(loginId);
			 if (this.bCryptPasswordEncoder.matches(password, currentUser.getPassword())) {
				 System.out.println("NewPassword.........."+ newPassword);
				 System.out.println("confirmPassword.........."+ confirmPassword);
				 if (!Objects.equals(newPassword, confirmPassword)) {
					redirAttrs.addFlashAttribute("pwdNotMatch", "new Password & Confirm New Password not match");
					System.out.println("new Password & Confirm New Password not match:::::::::: ");
				}
				 else  if (result.hasErrors()) {
						System.out.println("change password validation");
						List<ObjectError> allErrors = result.getAllErrors();
						for (ObjectError temp : allErrors) {
							System.out.println(temp);
						}
						return "passwordpolicy/changepassword";
					}
				  else {
				  System.out.println("After check the validation..**********************************...");
				currentUser.setThirdLastpassword(currentUser.getSecondLastPassword());
				currentUser.setSecondLastPassword(currentUser.getLastPassword());
				currentUser.setLastPassword(this.bCryptPasswordEncoder.encode(newPassword));
				currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
				currentUser.setPasswordToken(UUID.randomUUID().toString());
				
				userService.saveOrUpdateUser(currentUser);
				//this.changePwdService.changePwd(currentUser);
				redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
				System.out.println("Change Password Successfully");
			}
			 }	else {   
				redirAttrs.addFlashAttribute("notMatch", "Old Password not Match..");
				System.out.println("Old Password not match form db ::::::::::::");
				return "redirect:/login";
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while change password    " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - change password successfully "));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Change Password");
			auditRecord.setActivityCode("UPDATE");
			auditRecordService.save(auditRecord, device);
		}
			return "redirect:/changePwd";
	}
	
}