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
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.UserService;
import com.ksv.ktrccrm.validator.ResetPasswordValidator;

@Controller
public class ResetPasswordController {
	private static final Logger LOGGER = LogManager.getLogger(ResetPasswordController.class);

	@Autowired
	private ResetPasswordValidator userValidator;

	@InitBinder
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(userValidator);
	}

	@Autowired
	private UserService userService;
	@Autowired
	private AuditRecordService auditRecordService;

	@Value("${global.redirectmsg.save}")
	private String successSaveMsg;

	@Value("${global.redirect.update}")
	private String successUpdateMsg;

	@GetMapping(value = "/resetPasswordList")
	public String pwdList(Model model, Device device) throws Exception {
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
					List<UserMst> userMstList = userService.getRecordList(ProdConstant.ISACTIVE, tenantId);
					if (Objects.nonNull(userMstList)) {
						model.addAttribute("listUser", userMstList);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display reset password list " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed reset password list"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Reset Password Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "passwordpolicy/resetpasswordlist";
	}

	@GetMapping(value = "/resetPassword/{passwordToken}")
	public String showEditProductPage(@PathVariable(name = "passwordToken") String passwordToken, Model model,
			Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			UserMst userMst = userService.getUserByToken(passwordToken);
			if (Objects.nonNull(userMst)) {
				model.addAttribute("userAttribute", userMst);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display reset password page " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed password token - " + passwordToken));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Reset Password Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "passwordpolicy/resetpassword";
	}

	@PostMapping(value = "/resetSuccess")
	public String resetPwdSave(@Valid @ModelAttribute("userAttribute") UserMst user, BindingResult result, Model model,
			Device device, RedirectAttributes redirAttrs) throws Exception {
		AuditRecord auditRecord = new AuditRecord();

		try {
			if (result.hasErrors()) {
				List<ObjectError> allErrors = result.getAllErrors();
				for (ObjectError temp : allErrors) {
					System.out.println(temp);
				}
				return "passwordpolicy/resetpassword";
			} else {
				/* Save Users to Database */
				System.out.println("Inside reset success");
				userService.saveResetPassword(user);
				redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while reset password " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - password reset successfully"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Reset Password Management");
			auditRecord.setActivityCode("RESET PASSWORD");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/resetPasswordList";
	}
}
