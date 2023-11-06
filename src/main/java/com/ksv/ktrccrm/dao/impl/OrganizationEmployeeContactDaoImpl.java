package com.ksv.ktrccrm.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.dao.OrganizationEmployeeContactDao;
import com.ksv.ktrccrm.db1.entities.OrganizationEmployeeContact;
import com.ksv.ktrccrm.db1.repository.OrganizationEmployeeContactRepository;

@Repository
public class OrganizationEmployeeContactDaoImpl implements OrganizationEmployeeContactDao {
	private static final Logger LOGGER = LogManager.getLogger(OrganizationEmployeeContactDaoImpl.class);

	@Autowired
	private OrganizationEmployeeContactRepository oecRepository;

	@Override
	public List<OrganizationEmployeeContact> activeListOfOEC(String isActive) throws Exception {
		List<OrganizationEmployeeContact> oecList = new ArrayList<>();
		try {
			oecList = oecRepository.activeListOfOEC(isActive);
		} catch (Exception e) {
			LOGGER.error("Error occur while display active list of organization employee contact "
					+ ExceptionUtils.getStackTrace(e));
		}
		return oecList;
	}

	@Override
	public List<OrganizationEmployeeContact> inActiveListOfOEC(String inActive) throws Exception {
		List<OrganizationEmployeeContact> oecList = new ArrayList<>();
		try {
			oecList = oecRepository.inActiveListOfOEC(inActive);
		} catch (Exception e) {
			LOGGER.error("Error occur while display inactive list of organization employee contact "
					+ ExceptionUtils.getStackTrace(e));
		}
		return oecList;
	}

	@Override
	public OrganizationEmployeeContact saveOEC(OrganizationEmployeeContact oec) throws Exception {
		try {
			if (Objects.nonNull(oec)) {
				oecRepository.save(oec);
			} else {
				System.out.println("ORGANIZATION EMPLOYEE CONTACT RECORD NOT SAVE DAO ::::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while save organization employee contact record " + ExceptionUtils.getStackTrace(e));
		}
		return oec;
	}

	@Override
	public OrganizationEmployeeContact activateOEC(String orgEmpContactID) throws Exception {
		OrganizationEmployeeContact oec = oecRepository.getById(orgEmpContactID);
		try {
			if (Objects.nonNull(oec)) {
				return oec;
			} else {
				System.out.println("ORGANIZATION EMPLOYEE CONTACT NOT ACTIVATE DAO ::::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while active organization employee contact " + ExceptionUtils.getStackTrace(e));
		}
		return new OrganizationEmployeeContact();
	}

	@Override
	public OrganizationEmployeeContact deactivateOEC(String orgEmpContactID) throws Exception {
		OrganizationEmployeeContact oec = oecRepository.getById(orgEmpContactID);
		try {
			if (Objects.nonNull(oec)) {
				return oec;
			} else {
				System.out.println("ORGANIZATION EMPLOYEE CONTACT NOT DEACTIVATE DAO ::::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while deactive organization employee contact " + ExceptionUtils.getStackTrace(e));
		}
		return new OrganizationEmployeeContact();
	}

	@Override
	public Optional<OrganizationEmployeeContact> getOECById(String id) throws Exception {
		Optional<OrganizationEmployeeContact> optional = oecRepository.findByOrgEmpContactID(id);
		try {
			if (optional.isPresent()) {
				return optional;
			} else {
				System.out.println("NOT GET ORGANIZATION EMPLOYEE CONTACT BY ID DAO ::::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while get organization employee contact by id " + ExceptionUtils.getStackTrace(e));
		}
		return Optional.empty();
	}

	@Override
	public List<String> getListOfOECId() throws Exception {
		List<String> list = oecRepository.getListOfOECId();
		try {
			if (Objects.nonNull(list)) {
				return list;
			} else {
				System.out.println("LIST OF ID NOT GET DAO ::::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get list of empid's of organization employee contact "
					+ ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public OrganizationEmployeeContact getOECByOrgEmpContactID(String orgEmpContactID) throws Exception {
		OrganizationEmployeeContact organizationEmployeeContact = oecRepository.getByOrgEmpContactID(orgEmpContactID);
		try {
			if (Objects.nonNull(organizationEmployeeContact)) {
				return organizationEmployeeContact;
			} else {
				System.out.println("ORGANIZATION EMPLOYEE CONTACT NOT GET BY ID DAO ::::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while get organization employee contact by id " + ExceptionUtils.getStackTrace(e));
		}
		return new OrganizationEmployeeContact();
	}

	@Override
	public void saveAll(List<OrganizationEmployeeContact> organizationContact) throws Exception {
		try {
			if (Objects.nonNull(organizationContact)) {
				this.oecRepository.saveAll(organizationContact);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while save organization employee contact excel file data "
					+ ExceptionUtils.getStackTrace(e));
		}

	}

	@Override
	public List<OrganizationEmployeeContact> checkDuplicateOEC(String mobileNo) throws Exception {
		List<OrganizationEmployeeContact> oec = oecRepository.checkDuplicateOrgEmpContactOrgEmpId(mobileNo);
		try {
			if (Objects.nonNull(oec)) {
				return oec;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while check duplicate organization employee contact code"
					+ ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public boolean checkDuplicateException(String mobileNo) throws Exception {
		try {
			List<OrganizationEmployeeContact> oec = checkDuplicateOEC(mobileNo);
			if (Objects.nonNull(oec)) {
				return true;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while find duplicate organization employee contact excel"
					+ ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public List<OrganizationEmployeeContact> findAll() throws Exception {
		try {
			List<OrganizationEmployeeContact> oec = oecRepository.findAll();
			if (Objects.nonNull(oec)) {
				return oec;
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while display all organization employee contact" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public long getOrganizationEmployeeCount(String isActive) throws Exception {
		try {
			long count = oecRepository.getOrganizationEmployeeCount(isActive);
			if (Objects.nonNull(count)) {
				return count;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while count record " + ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}
}