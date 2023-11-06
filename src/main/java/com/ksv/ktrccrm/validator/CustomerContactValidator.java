//package com.ksv.ktrccrm.validator;
//
//import java.util.regex.Pattern;
//
//import org.springframework.stereotype.Component;
//import org.springframework.validation.Errors;
//import org.springframework.validation.Validator;
//
//import com.ksv.ktrccrm.constant.ProdConstant;
//import com.ksv.ktrccrm.db1.entities.CustomerContact;
//
//@Component
//public class CustomerContactValidator implements Validator {
//
//	private static final Pattern EMAIL_REGEX = Pattern.compile(ProdConstant.EMAIL_REGEX);
//	private static final Pattern PHONE_REGEX = Pattern.compile(ProdConstant.PHONE_REGEX);
//
//	@Override
//	public boolean supports(Class<?> clazz) {
//		return CustomerContact.class.equals(clazz);
//	}
//
//	@Override
//	public void validate(Object target, Errors errors) {
//
//		CustomerContact customerContact = (CustomerContact) target;
//
//		if (customerContact.getFirstName().isBlank() && customerContact.getFirstName().isEmpty()) {
//			errors.rejectValue("firstName", "customerContact.firstName.empty");
//		} else if (customerContact.getFirstName().length() > 50 ) {
//			errors.rejectValue("firstName", "customerContact.firstName.length");
//		}
//		
//		if (customerContact.getLastName().isBlank() && customerContact.getLastName().isEmpty()) {
//			errors.rejectValue("lastName", "customerContact.lastName.empty");
//		} else if (customerContact.getLastName().length() > 50 ) {
//			errors.rejectValue("lastName", "customerContact.lastName.length");
//		}
//
//		
//		if (customerContact.getOrganization().isBlank() && customerContact.getOrganization().isEmpty()) {
//			errors.rejectValue("college", "customerContact.college.empty");
//		} else if (customerContact.getOrganization().length() > 255 ) {
//			errors.rejectValue("college", "customerContact.college.length");
//		}
//		
//		if (customerContact.getBranch().isBlank() && customerContact.getBranch().isEmpty()) {
//			errors.rejectValue("branch", "customerContact.branch.empty");
//		} else if (customerContact.getBranch().length() > 150 ) {
//			errors.rejectValue("branch", "customerContact.branch.length");
//		}
//		
//		if (customerContact.getYear().isBlank() && customerContact.getYear().isEmpty()) {
//			errors.rejectValue("year", "customerContact.year.empty");
//		} else if (customerContact.getYear().length() > 10 ) {
//			errors.rejectValue("year", "customerContact.year.length");
//		}
//		
//		if (customerContact.getEmailId().isBlank() && customerContact.getEmailId().isEmpty()) {
//			errors.rejectValue("emailId", "customerContact.email.empty");
//		} else if (customerContact.getEmailId() != null && !EMAIL_REGEX.matcher(customerContact.getEmailId()).matches()) {
//			errors.rejectValue("emailId", "customerContact.email.invalid");
//		} else if (customerContact.getEmailId().length() > 150 ) {
//			errors.rejectValue("emailId", "customerContact.email.length");
//		}
//
//		if (customerContact.getMobileNumber().isBlank() && customerContact.getMobileNumber().isEmpty()) {
//			errors.rejectValue("mobileNumber", "customerContact.mobile.empty");
//		} else if (customerContact.getMobileNumber() != null && !PHONE_REGEX.matcher(customerContact.getMobileNumber()).matches()) {
//			errors.rejectValue("mobileNumber", "customerContact.mobile.invalid");
//		}else if (customerContact.getMobileNumber().length() != 10 ) {
//			errors.rejectValue("mobileNumber", "customerContact.mobile.length");
//		}
//		
//		if (customerContact.getLinkedinURL().isBlank() && customerContact.getLinkedinURL().isEmpty()) {
//			errors.rejectValue("linkedinURL", "customerContact.linkedinURL.empty");
//		} else if (customerContact.getLinkedinURL().length() > 255 ) {
//			errors.rejectValue("linkedinURL", "customerContact.linkedinURL.length");
//		}
//
//		if (customerContact.getTwitterURL().isBlank() && customerContact.getTwitterURL().isEmpty()) {
//			errors.rejectValue("twitterURL", "customerContact.twitterURL.empty");
//		} else if (customerContact.getTwitterURL().length() > 255 ) {
//			errors.rejectValue("twitterURL", "customerContact.twitterURL.length");
//		}
//		
//		if (customerContact.getFacebookURL().isBlank() && customerContact.getFacebookURL().isEmpty()) {
//			errors.rejectValue("facebookURL", "customerContact.facebookURL.empty");
//		} else if (customerContact.getFacebookURL().length() > 255 ) {
//			errors.rejectValue("facebookURL", "customerContact.facebookURL.length");
//		}
//	
//		if (customerContact.getAddress().isBlank() && customerContact.getAddress().isEmpty()) {
//			errors.rejectValue("address", "customerContact.address.empty");
//		} else if (customerContact.getAddress().length() > 255 ) {
//			errors.rejectValue("address", "customerContact.address.length");
//		}
//	}
//}

