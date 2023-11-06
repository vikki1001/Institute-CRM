package com.ksv.ktrccrm.validator;

import java.util.regex.Pattern;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.ksv.ktrccrm.controller.PasswordPolicyController;
import com.ksv.ktrccrm.db1.entities.UserMst;

@Component
public class UserValidator implements Validator {
	private static final Logger LOGGER = LogManager.getLogger(UserValidator.class);

	@Autowired
	private PasswordPolicyController passwordPolicyController;

    private static final Pattern STRING_PATTERN = Pattern.compile("^[a-zA-Z]([a-zA-Z]){3,30}[a-zA-Z]$");
	private static final Pattern EMAIL_REGEX = Pattern
			.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
//	private static final Pattern PWD_REGEX = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{6,16}$");	 
	private static final Pattern PHONE_REGEX = Pattern.compile("^[0-9]{10}$");

	@Override
	public boolean supports(Class<?> clazz) {
		return UserMst.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors err) {

		UserMst userMST = (UserMst) obj;

		try {
			if (userMST.getPassword() != null && !passwordPolicyController.isValid(userMST.getPassword())) {
				err.rejectValue("password", "user.password.invalid");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while password validation" + ExceptionUtils.getStackTrace(e));
			e.printStackTrace();
		}
		
		if (userMST.getEmailId() != null && !EMAIL_REGEX.matcher(userMST.getEmailId()).matches()) {
			err.rejectValue("emailId", "user.email.invalid");
		}

		if (userMST.getFirstName() != null && !STRING_PATTERN.matcher(userMST.getFirstName()).matches()) {
			err.rejectValue("firstName", "user.firstname.invalid");
		}

		if (userMST.getUserDisplayName() != null && !STRING_PATTERN.matcher(userMST.getUserDisplayName()).matches()) {
			err.rejectValue("userDisplayName", "user.userdisplayname.invalid");
		}

		if (userMST.getLastName() != null && !STRING_PATTERN.matcher(userMST.getLastName()).matches()) {
			err.rejectValue("lastName", "user.lastname.invalid");
		}

		if (userMST.getMobileNo1() != null && !PHONE_REGEX.matcher(userMST.getMobileNo1()).matches()) {
			err.rejectValue("mobileNo1", "user.mobile.invalid");
		}

		/* Change Password */
		
		
		ValidationUtils.rejectIfEmpty(err, "loginId", "user.loginid.invalid");
		ValidationUtils.rejectIfEmpty(err, "title", "user.title.empty");
		ValidationUtils.rejectIfEmpty(err, "gender", "user.gender.empty");
		ValidationUtils.rejectIfEmpty(err, "dob", "user.dob.empty");
		ValidationUtils.rejectIfEmpty(err, "baseBranch", "user.basebranch.empty");
		ValidationUtils.rejectIfEmpty(err, "mainRole", "user.mainrole.empty");
		ValidationUtils.rejectIfEmpty(err, "secondaryUser", "user.secondaryuser.empty");
		ValidationUtils.rejectIfEmpty(err, "escalationManager", "user.escalationmanager.empty");
		ValidationUtils.rejectIfEmpty(err, "isUserLocked", "user.isuserlocked.empty");
		ValidationUtils.rejectIfEmpty(err, "loginType", "user.logintype.empty");
		ValidationUtils.rejectIfEmpty(err, "userGroup", "user.usergroup.empty");
		ValidationUtils.rejectIfEmpty(err, "tenantId", "user.tenantId.empty");
		ValidationUtils.rejectIfEmpty(err, "branch", "user.branch.empty");
		ValidationUtils.rejectIfEmpty(err, "department", "user.department.empty");

	}
}