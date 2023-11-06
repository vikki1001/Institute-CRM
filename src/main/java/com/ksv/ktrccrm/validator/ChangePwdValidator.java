package com.ksv.ktrccrm.validator;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ksv.ktrccrm.controller.PasswordPolicyController;
import com.ksv.ktrccrm.db1.entities.UserMst;
@Component
public class ChangePwdValidator implements Validator {

	private static final Logger LOGGER = LogManager.getLogger(ChangePwdValidator.class);

	@Autowired
	private PasswordPolicyController passwordPolicyController;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return UserMst.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors err) {
		UserMst userMST = (UserMst) obj;
		try { /* Change password */
			if (userMST.getNewPassword() != null && !passwordPolicyController.isValid(userMST.getNewPassword())) {
				err.rejectValue("newPassword", "user.password.invalid");
			} else if (userMST.getNewPassword() != null	&& !passwordPolicyController.checkLastThreePassword(userMST.getNewPassword())) {
				err.rejectValue("newPassword", "user.newPassword.invalid");
			} 

		} catch (Exception e){
			LOGGER.error("Error occur while change password" + ExceptionUtils.getStackTrace(e));
			e.printStackTrace();
		}
	}

}
