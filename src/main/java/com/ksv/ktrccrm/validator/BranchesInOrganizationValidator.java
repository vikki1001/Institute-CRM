package com.ksv.ktrccrm.validator;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.ksv.ktrccrm.db1.entities.BranchesInOrganization;
import com.ksv.ktrccrm.db1.repository.BranchesInOrganizationRepository;

@Component
public class BranchesInOrganizationValidator implements Validator {

	@Autowired
	private BranchesInOrganizationRepository bioRepository;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return BranchesInOrganization.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors err) {
		BranchesInOrganization boi = (BranchesInOrganization) obj;

		if (boi.getId() != null) {
			Optional<BranchesInOrganization> optional = bioRepository.findById(boi.getId());
			if (optional.isPresent()) {
				BranchesInOrganization newBOI = optional.get();
				
				System.out.println("ID NOT NULL ::::::::: " + newBOI.getBranchId());

				if (newBOI.getBranchId().length() > 50) {
					System.out.println("IF ::::::::: ");
					err.rejectValue("branchId", "branch.branchId.length");
				} else if(newBOI.getBranchId().isEmpty()) {
					System.out.println("ELSE ::::::::: ");
					err.rejectValue("branchId", "branch.branchId.empty");
					//ValidationUtils.rejectIfEmpty(err, "branchId", "branch.branchId.empty");
				}

				if (newBOI.getBranchName().length() > 255) {
					System.out.println("IF ::::::::: ");
					err.rejectValue("branchName", "branch.branchName.length");
				} else if(newBOI.getBranchName().isEmpty()) {
					System.out.println("ELSE ::::::::: ");
					err.rejectValue("branchName", "branch.branchName.empty");
					//ValidationUtils.rejectIfEmpty(err, "branchName", "branch.branchName.empty");
				}

				/*
				 * if (newBOI.getBranchType().length() > 255) {
				 * System.out.println("IF ::::::::: "); err.rejectValue("branchType",
				 * "branch.branchType.length"); } else
				 */ if(newBOI.getBranchType().isEmpty()) {
					System.out.println("ELSE ::::::::: ");
					err.rejectValue("branchType", "branch.branchType.empty");
					//ValidationUtils.rejectIfEmpty(err, "branchType", "branch.branchType.empty");
				}
			} else {
				System.out.println("ELSE ID IS NULL ::::::::: " + boi.getId());
			}			
		} else {
			System.out.println("ID NULL ::::::::: " + boi.getBranchId());
			if (boi.getBranchId().length() > 50) {
				System.out.println("IF ::::::::: ");
				err.rejectValue("branchId", "branch.branchId.length");
				// ValidationUtils.rejectIfEmpty(err, "branchId", "branch.branchId.length");
			} else {
				System.out.println("ELSE ::::::::: ");
				ValidationUtils.rejectIfEmpty(err, "branchId", "branch.branchId.empty");
			}

			if (boi.getBranchName().length() > 255) {
				System.out.println("IF ::::::::: ");
				err.rejectValue("branchName", "branch.branchName.length");
			} else {
				System.out.println("ELSE ::::::::: ");
				ValidationUtils.rejectIfEmpty(err, "branchName", "branch.branchName.empty");
			}

			if (boi.getBranchType().length() > 255) {
				System.out.println("IF ::::::::: ");
				err.rejectValue("branchType", "branch.branchType.length");
			} else {
				System.out.println("ELSE ::::::::: ");
				ValidationUtils.rejectIfEmpty(err, "branchType", "branch.branchType.empty");
			}
		}
	}
}