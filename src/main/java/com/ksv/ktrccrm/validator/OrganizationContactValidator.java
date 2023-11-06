//package com.ksv.ktrccrm.validator;
//
//import java.util.regex.Pattern;
//
//import org.springframework.stereotype.Component;
//import org.springframework.validation.Errors;
//import org.springframework.validation.Validator;
//
//import com.ksv.ktrccrm.constant.ProdConstant;
//import com.ksv.ktrccrm.db1.entities.OrganizationContact;
//
//@Component
//public class OrganizationContactValidator implements Validator {
//
//	private static final Pattern EMAIL_REGEX = Pattern.compile(ProdConstant.EMAIL_REGEX);
//	private static final Pattern PHONE_REGEX = Pattern.compile(ProdConstant.PHONE_REGEX);
//
//	@Override
//	public boolean supports(Class<?> clazz) {
//		return OrganizationContact.class.equals(clazz);
//	}
//
//	@Override
//	public void validate(Object target, Errors errors) {
//
//		OrganizationContact organizationContact = (OrganizationContact) target;
//
//		if (organizationContact.getOrganizationName().trim().isBlank()
//				&& organizationContact.getOrganizationName().trim().isEmpty()) {
//			errors.rejectValue("organizationName", "organizationContact.organizationName.empty");
//		} else if (organizationContact.getOrganizationName().length() > 255) {
//			errors.rejectValue("organizationName", "organizationContact.organizationName.length");
//		}
//
//		if (organizationContact.getAffiliatedBy().trim().isBlank() && organizationContact.getAffiliatedBy().trim().isEmpty()) {
//			errors.rejectValue("affiliatedBy", "organizationContact.affiliatedBy.empty");
//		} else if (organizationContact.getAffiliatedBy().length() > 255) {
//			errors.rejectValue("affiliatedBy", "organizationContact.affiliatedBy.length");
//		}
//
//		if (organizationContact.getEmailId().trim().isBlank() && organizationContact.getEmailId().trim().isEmpty()) {
//			errors.rejectValue("emailId", "organizationContact.email.empty");
//		} else if (organizationContact.getEmailId() != null
//				&& !EMAIL_REGEX.matcher(organizationContact.getEmailId()).matches()) {
//			errors.rejectValue("emailId", "organizationContact.email.invalid");
//		} else if (organizationContact.getEmailId().length() > 150) {
//			errors.rejectValue("emailId", "organizationContact.email.length");
//		}
//
//		if (organizationContact.getMobileNumber().trim().isBlank() && organizationContact.getMobileNumber().trim().isEmpty()) {
//			errors.rejectValue("mobileNumber", "organizationContact.mobile.empty");
//		} else if (organizationContact.getMobileNumber() != null
//				&& !PHONE_REGEX.matcher(organizationContact.getMobileNumber()).matches()) {
//			errors.rejectValue("mobileNumber", "organizationContact.mobile.invalid");
//		} else if (organizationContact.getMobileNumber().length() != 10) {
//			errors.rejectValue("mobileNumber", "organizationContact.mobile.length");
//		}
//
//		if (organizationContact.getWebsiteLink().trim().isBlank() && organizationContact.getWebsiteLink().trim().isEmpty()) {
//			errors.rejectValue("websiteLink", "organizationContact.websiteLink.empty");
//		} else if (organizationContact.getWebsiteLink().length() > 255) {
//			errors.rejectValue("websiteLink", "organizationContact.websiteLink.length");
//		}
//
//		if (organizationContact.getAddress().trim().isBlank() && organizationContact.getAddress().trim().isEmpty()) {
//			errors.rejectValue("address", "organizationContact.address.empty");
//		} else if (organizationContact.getAddress().length() > 255) {
//			errors.rejectValue("address", "organizationContact.address.length");
//		}
//	}
//}

