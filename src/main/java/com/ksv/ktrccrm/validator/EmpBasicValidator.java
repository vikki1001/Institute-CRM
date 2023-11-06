package com.ksv.ktrccrm.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;

@Component
public class EmpBasicValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return EmpBasicDetails.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors err) {
		
		ValidationUtils.rejectIfEmpty(err, "coEmailId", "role.rolecode.empty");
		//ValidationUtils.rejectIfEmpty(err, "empBasicDetails.empPersonalDetails.birthDate", "role.rolename.empty");
	}
	
}
