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

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.OrganizationContactDao;
import com.ksv.ktrccrm.db1.entities.OrganizationContact;
import com.ksv.ktrccrm.db1.repository.OrganizationContactRepository;
import com.ksv.ktrccrm.db1.repository.OrganizationEmployeeContactRepository;

@Repository
public class OrganizationContactDaoImpl implements OrganizationContactDao {
	private static final Logger LOGGER = LogManager.getLogger(OrganizationContactDaoImpl.class);

	@Autowired
	private OrganizationContactRepository organizationContactRepository;
	
	@Autowired
	private OrganizationEmployeeContactRepository organizationEmployeeContactRepository;

	@Override
	public List<OrganizationContact> activeListOfOrganizationContact(String isActive) throws Exception {
		List<OrganizationContact> organizationContactList = new ArrayList<>();
		try {
			organizationContactList = organizationContactRepository.activeListOfOrganizationContact(isActive);
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while display active list of organization contact " + ExceptionUtils.getStackTrace(e));
		}
		return organizationContactList;
	}

	@Override
	public List<OrganizationContact> inActiveListOfOrganizationContact(String inActive) throws Exception {
		List<OrganizationContact> organizationContactList = new ArrayList<>();
		try {
			organizationContactList = organizationContactRepository.inActiveListOfOrganizationContact(inActive);
		} catch (Exception e) {
			LOGGER.error("Error occur while display inactive list of organization contact "
					+ ExceptionUtils.getStackTrace(e));
		}
		return organizationContactList;
	}

	@Override
	public OrganizationContact saveOrganizationContact(OrganizationContact organizationContact) throws Exception {
		try {
			if (Objects.nonNull(organizationContact)) {
				organizationContactRepository.save(organizationContact);
			} else {
				System.out.println("ORGANIZATION CONTACT RECORD NOT SAVE DAO ::::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while save organization contact record " + ExceptionUtils.getStackTrace(e));
		}
		return organizationContact;
	}

	@Override
	public OrganizationContact activateOrganizationContact(String id) throws Exception {
		OrganizationContact organizationContact = organizationContactRepository.getById(id);
		try {
			if (Objects.nonNull(organizationContact)) {
				return organizationContact;
			} else {
				System.out.println("ORGANIZATION CONTACT NOT ACTIVATE DAO ::::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while active organization contact " + ExceptionUtils.getStackTrace(e));
		}
		return new OrganizationContact();
	}

	@Override
	public OrganizationContact deactivateOrganizationContact(String id) throws Exception {
		OrganizationContact organizationContact = organizationContactRepository.getById(id);
		try {
			if (Objects.nonNull(organizationContact)) {
				return organizationContact;
			} else {
				System.out.println("ORGANIZATION CONTACT NOT DEACTIVATE DAO ::::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while deactive organization contact " + ExceptionUtils.getStackTrace(e));
		}
		return new OrganizationContact();
	}

	@Override
	public Optional<OrganizationContact> getOrganizationContactById(String id) throws Exception {
		Optional<OrganizationContact> optional = organizationContactRepository.findByOrganizationId(id);
		try {
			if (optional.isPresent()) {
				return optional;
			} else {
				System.out.println("NOT GET ORGANIZATION CONTACT BY ID DAO ::::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get organization contact by id " + ExceptionUtils.getStackTrace(e));
		}
		return Optional.empty();
	}

	@Override
	public List<String> getListOfOrgId() throws Exception {
		List<String> list = organizationContactRepository.getListOfOrgId();
		try {
			if (Objects.nonNull(list)) {
				return list;
			} else {
				System.out.println("LIST OF ID NOT GET DAO ::::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while get list of empid's of organization contact " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public OrganizationContact getOrgContactById(String organizationId) throws Exception {
		OrganizationContact organizationContact = organizationContactRepository.getByOrganizationId(organizationId);
		try {
			if (Objects.nonNull(organizationContact)) {
				return organizationContact;
			} else {
				System.out.println("ORGANIZATION CONTACT NOT GET BY ID DAO::::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get organization contact by id " + ExceptionUtils.getStackTrace(e));
		}
		return new OrganizationContact();
	}

	@Override
	public void saveAll(List<OrganizationContact> organizationContact) throws Exception {
		try {
			if (Objects.nonNull(organizationContact)) {
				this.organizationContactRepository.saveAll(organizationContact);				
			}			
		} catch (Exception e) {
			LOGGER.error("Error while save all excel data of  organization contact" + ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public List<OrganizationContact> checkDuplicateOrgContact(String mobileNo) throws Exception {
		List<OrganizationContact> organizationContacts = organizationContactRepository.checkDuplicateOrgContactMobileNo(mobileNo);
		try {
			if (Objects.nonNull(organizationContacts)) {
				return organizationContacts;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while check duplicate organization contact code" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public boolean checkDuplicateException(String mobileNo) throws Exception {
		try {
			List<OrganizationContact> organizationContacts = checkDuplicateOrgContact(mobileNo);
			 if (Objects.nonNull(organizationContacts)) {
				return true;
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while find duplicate organization contact excel" + ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public List<OrganizationContact> sendMailToCustomer(String category, String subCategory) throws Exception {
		try {
			if (Objects.equals(category, ProdConstant.ORGANIZATION) && Objects.nonNull(subCategory)) {
				if (ProdConstant.ALL.trim().equalsIgnoreCase(subCategory)) {
					return organizationContactRepository.activeListOfOrganizationContact(ProdConstant.TRUE);
				} else {
					return organizationContactRepository.getDataBySubCategory(subCategory);
				}
			} else if (Objects.equals(category, ProdConstant.STUDENTS) && Objects.nonNull(subCategory)) {
				if (ProdConstant.ALL.trim().equalsIgnoreCase(subCategory)) {
					return organizationContactRepository.activeListOfOrganizationContact(ProdConstant.TRUE);
				} else {
					return organizationEmployeeContactRepository.getDataBySubCategory(subCategory);
				}
			} else if (Objects.equals(category, ProdConstant.STUDENTSBRANCH) && Objects.nonNull(subCategory)) {
				if (ProdConstant.ALL.trim().equalsIgnoreCase(subCategory)) {
					return organizationContactRepository.activeListOfOrganizationContact(ProdConstant.TRUE);
				} else {
					return organizationContactRepository.getDataBySubCategory(subCategory);
				}
			} else if (Objects.equals(category, ProdConstant.ORGANIZATIONEMPLOYEE) && Objects.nonNull(subCategory)) {
				if (ProdConstant.ALL.trim().equalsIgnoreCase(subCategory)) {
					return organizationContactRepository.activeListOfOrganizationContact(ProdConstant.TRUE);
				} else {
					return organizationContactRepository.getDataBySubCategory(subCategory);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while send mail " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public long getOrganizationCount(String isActive) throws Exception {
		try {
			long count = organizationContactRepository.getOrganizationCount(isActive);
			if (Objects.nonNull(count)) {
				return count;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while count record " + ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}
}