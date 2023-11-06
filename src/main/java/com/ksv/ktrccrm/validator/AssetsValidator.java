package com.ksv.ktrccrm.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.ksv.ktrccrm.db1.entities.Assets;

@Component
public class AssetsValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return Assets.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
	
		ValidationUtils.rejectIfEmpty(errors, "empId", "assets.empId.empty");
		ValidationUtils.rejectIfEmpty(errors, "empName", "assets.empName.empty");
		ValidationUtils.rejectIfEmpty(errors, "assetsCode", "assets.assetsCode.empty");
		ValidationUtils.rejectIfEmpty(errors, "assetsModel", "assets.assetsModel.empty");
		ValidationUtils.rejectIfEmpty(errors, "assetsMake", "assets.assetsMake.empty");
		ValidationUtils.rejectIfEmpty(errors, "assetsServiceTag", "assets.assetsServiceTag.empty");
		ValidationUtils.rejectIfEmpty(errors, "assetsDescription", "assets.assetsDescription.empty");
		ValidationUtils.rejectIfEmpty(errors, "addedTime", "assets.addedTime.empty");
	}

}
