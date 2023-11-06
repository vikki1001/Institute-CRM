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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.PasswordPolicy;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.PasswordPolicyService;
import com.ksv.ktrccrm.service.UserService;

@Controller
public class PasswordPolicyController {
	private static final Logger LOGGER = LogManager.getLogger(PasswordPolicyController.class);

	@Autowired
	private AuditRecordService auditRecordService;
	@Autowired
	private PasswordPolicyService passwordPolicyService;
	@Autowired
	private UserService userService;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Value("${global.redirectmsg.save}")
	private String successSaveMsg;
	
	@Value("${global.redirect.update}")
	private String successUpdateMsg;

	@GetMapping(value = "/passwordPolicy")
	public String form(@ModelAttribute("pwd") PasswordPolicy passwordPolicy, Model model, Device device)
			throws Exception {
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
			List<PasswordPolicy> passwordPolicyList = passwordPolicyService.getRecordList(ProdConstant.TRUE,tenantId);
			if (Objects.nonNull(passwordPolicyList)) {
				model.addAttribute("passwordPolicy", passwordPolicyList);
			}  
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display password policy page " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed password policy management form"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Password Policy Form");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "passwordpolicy/passwordpolicy";
	}

	@PostMapping(value = "/savePasswordPolicy")
	public String saveForm(@Valid @ModelAttribute("pwd") PasswordPolicy passwordPolicy,BindingResult result, Model model, Device device,
			RedirectAttributes redirAttrs) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		List<PasswordPolicy> passwordPolicyList = passwordPolicyService.findAll();
		try {
			if(result.hasErrors()) {
				List<ObjectError> allErrors = result.getAllErrors();
				for (ObjectError temp : allErrors) {
					System.out.println(temp);
				}
				return "passwordpolicy/passwordpolicy";
			}
			if(Objects.nonNull(passwordPolicyList)) {
				model.addAttribute("passwordPolicy", passwordPolicyList);
				passwordPolicyService.savePasswordPolicy(passwordPolicy);
				redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successUpdateMsg);
			}		
			
		} catch (Exception e) {
			LOGGER.error("Error occur while display save/update password policy " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - save/update password policy management"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Password Policy Form");
			auditRecord.setActivityCode("SAVE/UPDATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/passwordPolicy";
	}

	public boolean isValid(String password) throws Exception {

		System.out.println("Inside Isvalid Method :");
		String userId = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		userId = authentication.getName();
		if (userId != null) {

			UserMst userDetail = null;
			userDetail = userService.getUserDetails(userId, ProdConstant.ISACTIVE);
			if (userDetail != null) {
				String tenantId = userDetail.getTenantId();

				if (tenantId != null) {
					PasswordPolicy passwordPolicy = passwordPolicyService.getDataBytenantId(tenantId);

					if (passwordPolicy != null) {

						Integer minimumPasswordLength = passwordPolicy.getMinimumPasswordLength();
						Integer maximumBadLoginCount = passwordPolicy.getMaximumBadLoginCount();
						Integer isMixCase = passwordPolicy.getIsMixCase();
						Integer minimumSpecialCharacterInteger = passwordPolicy.getMinimumSpecialCharacter();
						Integer minimumAlphabetsLength = passwordPolicy.getMinimumAlphabetsLength();
						Integer lastPasswordNotAllowedCount = passwordPolicy.getLastPasswordNotAllowedCount();
						Integer maximumPasswordLength = passwordPolicy.getMaximumPasswordLength();
						String isUserIdAllowed = passwordPolicy.getIsUserIdAllowed();
						Integer sameConsCharNotAllowedCount = passwordPolicy.getSameConsCharNotAllowedCount();
						String description;
						Long version;
						Integer minimumDigit = passwordPolicy.getMinimumDigits();
						Integer passwordValidateDays;
						Integer passwordNotificationDays;

						if (!((password.length() >= minimumPasswordLength)
								&& (password.length() <= maximumPasswordLength))) {
							System.out.println("password length Invalid ::::::::::::");
							return false;
						}
      
						// to check space
						if (password.contains(" ")) {
							System.out.println("password space not allowed ::::::::::::");
							return false;
						}
						if (true) {
							int count = 0;

							// check digits from 0 to 9
							for (int i = 0; i <= 9; i++) {

								// to convert int to string
								String str1 = Integer.toString(i);

								if (password.contains(str1)) {
									count++;
								}
							}
							if (!(count >= minimumDigit)) {
								System.out.println("In password Digit are required ::::::::::::");
								return false;
							}
						}

						// for special characters
						int temp = 0;
						for (int i = 0; i < password.length(); i++) {
							if ((password.contains("@") || password.contains("#") || password.contains("!")
									|| password.contains("~") || password.contains("$") || password.contains("%")
									|| password.contains("^") || password.contains("&") || password.contains("*")
									|| password.contains("(") || password.contains(")") || password.contains("-")
									|| password.contains("+") || password.contains("/") || password.contains(":")
									|| password.contains(".") || password.contains(", ") || password.contains("<")
									|| password.contains(">") || password.contains("?") || password.contains("|"))) {
								temp++;
							}
						}
						if (!(temp >= minimumSpecialCharacterInteger)) {
							System.out.println("More Special characters are not allowed :::::::");
							return false;
						}

						int countCapitalLetter = 0;
						int countSmallLetter = 0;
						int totalAlphabetsCount = 0;
						if (true) {
							int count = 0;

							// checking capital letters
							for (int i = 65; i <= 90; i++) {

								// type casting
								char c = (char) i;

								String str1 = Character.toString(c);
								if (password.contains(str1)) {
									countCapitalLetter++;
									count = 1;
								}
							}
							if (count == 0) {
								System.out.println("In password capital letter are required ::::::::::::");
								return false;
							}
						}

						if (true) {
							int count = 0;

							// checking small letters
							for (int j = 97; j <= 122; j++) {
								// type casting
								char c = (char) j;
								String str1 = Character.toString(c);
								if (password.contains(str1)) {
									countSmallLetter++;
									count = 1;
								}
							}
							if (count == 0) {
								System.out.println("In password small letter are required :::::::::::");
								return false;
							}
							if (!(countCapitalLetter == 0 && countSmallLetter == 0)) {
								totalAlphabetsCount = countCapitalLetter + countSmallLetter;
							} else if (!(totalAlphabetsCount >= minimumAlphabetsLength)) {
								System.out.println("In password alphabets are required ::::::::::::");
								return false;
							}
						}
					} else {
						System.out.println("Error occur while get data by tenantId from password policy ");
					}
				} else {
					System.out.println("Error occur while get current user tenantId");
				}
			} else {
				System.out.println("Error occur while get current user detail");
			}
		} else {
			System.out.println("Error occur while get userId ");
		}
		System.out.println("RETURN POLICY TRUE");
		return true;
	}

	public boolean checkLastThreePassword(String newPassword) throws Exception{
		UserMst userDetails = null;
		String loginId = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		loginId = authentication.getName();
		if(loginId != null){
			System.out.println("Inside check three password..");
			System.out.println("New Password ::"+ newPassword);
			userDetails = userService.getUserDetails(loginId, ProdConstant.ISACTIVE);
			String lastPwd = userDetails.getLastPassword();
			System.out.println("Last Password :::"+ lastPwd);
			String secLastPwd = userDetails.getSecondLastPassword();
			System.out.println("Second last Password :::"+ secLastPwd);
			String thirdLastPwd = userDetails.getThirdLastpassword();
			System.out.println("Third last Password :::"+ thirdLastPwd);

			System.out.println("INSIDE :::"+ this.bCryptPasswordEncoder.encode(newPassword));	
			
			if(lastPwd != null || secLastPwd != null || thirdLastPwd != null) {
				System.out.println("third last Password :::"+ thirdLastPwd);
				if(this.bCryptPasswordEncoder.matches(newPassword, lastPwd) || this.bCryptPasswordEncoder.matches(newPassword, secLastPwd) || this.bCryptPasswordEncoder.matches(newPassword, thirdLastPwd)){
				System.out.println("RETURN FALSE");
				return false;
				}
			}
		}
		System.out.println("RETURN TRUE");
		return true;
	}

}
