//package com.ksv.ktrccrm.validator;
//
//import java.util.regex.Pattern;
//
//import org.springframework.stereotype.Component;
//import org.springframework.validation.Errors;
//import org.springframework.validation.ValidationUtils;
//import org.springframework.validation.Validator;
//
//import com.ksv.ktrccrm.constant.ProdConstant;
//import com.ksv.ktrccrm.db1.entities.OrganizationEmployeeContact;
//
//@Component
//public class OrganizationEmployeeContactValidator implements Validator {
//
//	private static final Pattern EMAIL_REGEX = Pattern.compile(ProdConstant.EMAIL_REGEX);
//	private static final Pattern PHONE_REGEX = Pattern.compile(ProdConstant.PHONE_REGEX);
//
//	@Override
//	public boolean supports(Class<?> clazz) {
//		return OrganizationEmployeeContact.class.equals(clazz);
//	}
//
//	@Override
//	public void validate(Object target, Errors errors) {
//
//		OrganizationEmployeeContact oec = (OrganizationEmployeeContact) target;
//
////		if (oec.getFirstName().length() > 50 ) {
////			errors.rejectValue("firstName", "oec.firstName.length");
////		} else {
////			ValidationUtils.rejectIfEmpty(errors, "firstName", "oec.firstName.empty");
////		}
//		
//			ValidationUtils.rejectIfEmpty(errors, "firstName", "oec.firstName.empty");
//		
//		
////		if (oec.getLastName().isBlank() || oec.getLastName().isEmpty()) {
////			errors.rejectValue("lastName", "oec.lastName.empty");
////		} else if (oec.getLastName().length() > 50 ) {
////			errors.rejectValue("lastName", "oec.lastName.length");
////		}
////		
////		if (oec.getTitle().isBlank() || oec.getTitle().isEmpty()) {
////			errors.rejectValue("title", "oec.title.empty");
////		} else if (oec.getTitle().length() > 10 ) {
////			errors.rejectValue("title", "oec.title.length");
////		}
////		
////		if (oec.getOrganization().isBlank() || oec.getOrganization().isEmpty()) {
////			errors.rejectValue("organization", "oec.organization.empty");
////		} else if (oec.getOrganization().length() > 255 ) {
////			errors.rejectValue("organization", "oec.organization.length");
////		}
////				
////		if (oec.getEmailId().isBlank() || oec.getEmailId().isEmpty()) {
////			errors.rejectValue("emailId", "oec.email.empty");
////		} else if (oec.getEmailId() != null && !EMAIL_REGEX.matcher(oec.getEmailId()).matches()) {
////			errors.rejectValue("emailId", "oec.email.invalid");
////		} else if (oec.getEmailId().length() > 150 ) {
////			errors.rejectValue("emailId", "oec.email.length");
////		}
////
////		if (oec.getMobileNumber().isBlank() || oec.getMobileNumber().isEmpty()) {
////			errors.rejectValue("mobileNumber", "oec.mobile.empty");
////		} else if (oec.getMobileNumber() != null && !PHONE_REGEX.matcher(oec.getMobileNumber()).matches()) {
////			errors.rejectValue("mobileNumber", "oec.mobile.invalid");
////		}else if (oec.getMobileNumber().length() != 10 ) {
////			errors.rejectValue("mobileNumber", "oec.mobile.length");
////		}
////		
////		if (oec.getLinkedinURL().isBlank() || oec.getLinkedinURL().isEmpty()) {
////			errors.rejectValue("linkedinURL", "oec.linkedinURL.empty");
////		} else if (oec.getLinkedinURL().length() > 255 ) {
////			errors.rejectValue("linkedinURL", "oec.linkedinURL.length");
////		}
////
////		if (oec.getTwitterURL().isBlank() || oec.getTwitterURL().isEmpty()) {
////			errors.rejectValue("twitterURL", "oec.twitterURL.empty");
////		} else if (oec.getTwitterURL().length() > 255 ) {
////			errors.rejectValue("twitterURL", "oec.twitterURL.length");
////		}
////		
////		if (oec.getFacebookURL().isBlank() || oec.getFacebookURL().isEmpty()) {
////			errors.rejectValue("facebookURL", "oec.facebookURL.empty");
////		} else if (oec.getFacebookURL().length() > 255 ) {
////			errors.rejectValue("facebookURL", "oec.facebookURL.length");
////		}
////	
////		if (oec.getAddress().isBlank() || oec.getAddress().isEmpty()) {
////			errors.rejectValue("address", "oec.address.empty");
////		} else if (oec.getAddress().length() > 255 ) {
////			errors.rejectValue("address", "oec.address.length");
////		}
//	}
//}


