package com.ksv.ktrccrm.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.ksv.ktrccrm.db1.entities.DepartmentMst;

@Component
public class DepartmentValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return DepartmentMst.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors err) {

		ValidationUtils.rejectIfEmpty(err, "departmentName", "department.departmentName.empty");
		ValidationUtils.rejectIfEmpty(err, "roleId", "department.roleId.empty");
		ValidationUtils.rejectIfEmpty(err, "departmentDesc", "department.departmentDesc.empty");
	}
}
