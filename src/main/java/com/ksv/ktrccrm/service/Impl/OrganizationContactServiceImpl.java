package com.ksv.ktrccrm.service.Impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.EmpRegistartionDao;
import com.ksv.ktrccrm.dao.OrganizationContactDao;
import com.ksv.ktrccrm.dao.impl.CheckInCheckOutDaoImpl;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.OrganizationContact;
import com.ksv.ktrccrm.db1.entities.SequenceMstCRM;
import com.ksv.ktrccrm.db1.repository.SequenceMstCRMRepository;
import com.ksv.ktrccrm.exception.DuplicateDataException;
import com.ksv.ktrccrm.service.OrganizationContactService;

@Service
public class OrganizationContactServiceImpl implements OrganizationContactService {
	private static final Logger LOGGER = LogManager.getLogger(OrganizationContactServiceImpl.class);

	@Autowired
	private OrganizationContactDao organizationContactDao;
	@Autowired
	private EmpRegistartionDao empRegistartionDao;
	@Autowired
	private SequenceMstCRMRepository sequenceMstCRMRepository;

	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;

	@Override
	public List<OrganizationContact> activeListOfOrganizationContact(String isActive) throws Exception {
		List<OrganizationContact> organizationContactList = new ArrayList<>();
		try {
			organizationContactList = organizationContactDao.activeListOfOrganizationContact(isActive);
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
			organizationContactList = organizationContactDao.inActiveListOfOrganizationContact(inActive);
		} catch (Exception e) {
			LOGGER.error("Error occur while display inactive list of organization contact "
					+ ExceptionUtils.getStackTrace(e));
		}
		return organizationContactList;
	}

	@Override
	public OrganizationContact saveOrganizationContact(OrganizationContact organizationContact) throws Exception {
		String empId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();

			EmpBasicDetails basicDetails = empRegistartionDao.getEmpById(empId);

			if (Objects.isNull(organizationContact.getOrganizationId())) {
				Optional<SequenceMstCRM> sequenceMstCRMOptional = sequenceMstCRMRepository
						.getSequenceKey(ProdConstant.ORGANIZATIONCONTACT);
				if (sequenceMstCRMOptional.isPresent()) {
					SequenceMstCRM sequenceMstCRM = sequenceMstCRMOptional.get();

					String orgId = sequenceMstCRM.getSequenceKey() + sequenceMstCRM.getSequenceNo();

					/* Duplicate Check */
					List<String> idList = organizationContactDao.getListOfOrgId();
					System.out.println("List of Id's ::::::: " + idList);

					int orgIdNo;
					while (idList.contains(orgId)) {
						System.out.println("DuplicatId..." + orgId);

						String separator = "OR";
						int sepPos = orgId.indexOf(separator);
						if (sepPos == -1) {
						}
						String stringOrgIdNo = orgId.substring(sepPos + separator.length());
						orgIdNo = Integer.parseInt(stringOrgIdNo);
						orgIdNo = orgIdNo + 1;
						orgId = sequenceMstCRM.getSequenceKey() + orgIdNo;

						int newSeqNo = orgIdNo + 1;
						String newSequenceNo = String.valueOf(newSeqNo);
						sequenceMstCRM.setSequenceNo(newSequenceNo);

						String newLastSequenceNo = String.valueOf(orgIdNo);
						sequenceMstCRM.setLastSequenceNo(newLastSequenceNo);

						sequenceMstCRMRepository.save(sequenceMstCRM);
					}
					/* Unique ID */
					System.out.println("unique..." + orgId);

					organizationContact.setOrganizationId(orgId);
					organizationContact.setAddedDate(checkOutDaoImpl.getDateTime());
					organizationContact.setAddedBy(empId);
					organizationContact.setIsActive(ProdConstant.TRUE);
					organizationContact.setTenantId(basicDetails.getTenantId());
					organizationContactDao.saveOrganizationContact(organizationContact);
				} else {
					System.out.println("Not avaliable the organization contact :::::: ");
				}
			} else {
				System.out.println("Edit Organization Contact AREA :::::::::::: ");

				Optional<OrganizationContact> optional = organizationContactDao
						.getOrganizationContactById(organizationContact.getOrganizationId());
				if (optional.isPresent()) {
					OrganizationContact newOrganizationContact = optional.get();
					newOrganizationContact.setOrganizationName(organizationContact.getOrganizationName());
					newOrganizationContact.setAffiliatedBy(organizationContact.getAffiliatedBy());
					newOrganizationContact.setEmailId(organizationContact.getEmailId());
					newOrganizationContact.setMobileNumber(organizationContact.getMobileNumber());
					newOrganizationContact.setAddress(organizationContact.getAddress());
					newOrganizationContact.setWebsiteLink(organizationContact.getWebsiteLink());
					newOrganizationContact.setModifiedDate(checkOutDaoImpl.getDateTime());
					newOrganizationContact.setModifiedBy(empId);
					newOrganizationContact.setTenantId(basicDetails.getTenantId());
					organizationContactDao.saveOrganizationContact(newOrganizationContact);
				} else {
					System.out.println("ERROR IN EDIT OF ORGANIZATION CONTACT :::::::::::: ");
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while save organization contact record " + ExceptionUtils.getStackTrace(e));
		}
		return organizationContact;
	}

	@Override
	public OrganizationContact activateOrganizationContact(String id) throws Exception {
		try {
			Optional<OrganizationContact> optional = organizationContactDao.getOrganizationContactById(id);
			if (optional.isPresent()) {
				OrganizationContact organizationContact = optional.get();
				organizationContact.setIsActive("1");
				organizationContactDao.saveOrganizationContact(organizationContact);
			} else {
				System.out.println("ORGANIZATION CONTACT ACTIVATE SERVICE::::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while active organization contact " + ExceptionUtils.getStackTrace(e));
		}
		return new OrganizationContact();
	}

	@Override
	public OrganizationContact deactivateOrganizationContact(String id) throws Exception {
		try {
			Optional<OrganizationContact> optional = organizationContactDao.getOrganizationContactById(id);
			if (optional.isPresent()) {
				OrganizationContact organizationContact = optional.get();
				organizationContact.setIsActive("0");
				organizationContactDao.saveOrganizationContact(organizationContact);
			} else {
				System.out.println("ORGANIZATION CONTACT NOT DEACTIVATE SERVICE::::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while deactive organization contact " + ExceptionUtils.getStackTrace(e));
		}
		return new OrganizationContact();
	}

	@Override
	public OrganizationContact getOrgContactById(String organizationId) throws Exception {
		OrganizationContact organizationContact = organizationContactDao.getOrgContactById(organizationId);
		try {
			if (Objects.nonNull(organizationContact)) {
				return organizationContact;
			} else {
				System.out.println("ORGANIZATION CONTACT NOT GET BY ID SERVICE::::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get organization contact by id " + ExceptionUtils.getStackTrace(e));
		}
		return new OrganizationContact();
	}

	/* Read data form excel and update to database */
	@Override
	public void saveAll(MultipartFile files) throws Exception {
		String empId;
		List<OrganizationContact> orgContactList = new ArrayList<>();
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();
			
			EmpBasicDetails basicDetails = empRegistartionDao.getEmpById(empId);

			List<OrganizationContact> organizationContactsList = organizationContactDao.activeListOfOrganizationContact(ProdConstant.TRUE);

			try (XSSFWorkbook workbook = new XSSFWorkbook(files.getInputStream())) {
				// Read data form excel file sheet
				XSSFSheet worksheet = workbook.getSheetAt(0);
				for (int index = 0; index < worksheet.getPhysicalNumberOfRows(); index++) {
					if (index > 0) {
						XSSFRow row = worksheet.getRow(index);
						
	if (row.getCell(index) == null) {
	   System.out.println("BLANK RECORD IN EXCEL :::::::::::");
	} else {
	}
																
						
							/* Custom Id Generate */
							Optional<SequenceMstCRM> sequenceMstCRMOptional = sequenceMstCRMRepository
									.getSequenceKey(ProdConstant.ORGANIZATIONCONTACT);
							if (sequenceMstCRMOptional.isPresent()) {
								SequenceMstCRM sequenceMstCRM = sequenceMstCRMOptional.get();

								String orgId = sequenceMstCRM.getSequenceKey() + sequenceMstCRM.getSequenceNo();

								/* Duplicate Check */
						System.out.println("Organistion Duplicate Check start............");
						
						        List<String> idList = organizationContactDao.getListOfOrgId();
								System.out.println("List of Id's ::::::: " + idList);

								int orgIdNo;
								while (idList.contains(orgId)) {
									System.out.println("DuplicatId..." + orgId);

									String separator = "OR";
									int sepPos = orgId.indexOf(separator);
									if (sepPos == -1) {
									}
									String stringOrgIdNo = orgId.substring(sepPos + separator.length());
									orgIdNo = Integer.parseInt(stringOrgIdNo);
									orgIdNo = orgIdNo + 1;
									orgId = sequenceMstCRM.getSequenceKey() + orgIdNo;

									int newSeqNo = orgIdNo + 1;
									String newSequenceNo = String.valueOf(newSeqNo);
									sequenceMstCRM.setSequenceNo(newSequenceNo);

									String newLastSequenceNo = String.valueOf(orgIdNo);
									sequenceMstCRM.setLastSequenceNo(newLastSequenceNo);

									sequenceMstCRMRepository.save(sequenceMstCRM);
								}
								
								
								/* Unique ID */
								System.out.println("unique..." + orgId);
								
//								Integer mobileNo =  (int) row.getCell(3).getNumericCellValue();
//								System.out.println("MOBILE NUMBER in Service :::::: " + mobileNo);
//								
								String mobileNo = null;
								String year = null;
								
								if (row.getCell(3).getCellType() == CellType.NUMERIC) {
								    // Convert the numeric value to a string
									System.out.println("mobile number = "+row.getCell(3).getNumericCellValue());
									mobileNo = 	String.valueOf((long)row.getCell(3).getNumericCellValue());
									System.out.println("MOBILE NUMBER in Service (if):::::: " + mobileNo);
								} else if (row.getCell(3).getCellType() == CellType.STRING) {
								    // Handle string values
										
									 mobileNo = row.getCell(3).getStringCellValue();
										System.out.println("MOBILE NUMBER in Service (else) :::::: " + mobileNo);
								}
								else {
									System.out.println("else block to ckeck mobile number ");
								}
								
								
								/* Insert Record */		
								if (organizationContactsList != null && !(organizationContactsList.isEmpty())) {
						System.out.println("inside duplicate check start...........");			
									
									if (!checkDuplicateException(mobileNo)) {
						System.out.println("Duplicate NOT find then create unique id::::::::");
									
										OrganizationContact organizationContact = new OrganizationContact();
										organizationContact.setOrganizationId(orgId);
										organizationContact.setOrganizationName(row.getCell(0).getStringCellValue());
										organizationContact.setAffiliatedBy(row.getCell(1).getStringCellValue());
										organizationContact.setEmailId(row.getCell(2).getStringCellValue());
								//		organizationContact.setMobileNumber(String.valueOf((int) row.getCell(3).getNumericCellValue()));
								organizationContact.setMobileNumber(mobileNo);
										organizationContact.setAddress(row.getCell(4).getStringCellValue());
										organizationContact.setWebsiteLink(row.getCell(5).getStringCellValue());
										organizationContact.setAddedBy(empId);
										organizationContact.setAddedDate(checkOutDaoImpl.getDateTime());
										organizationContact.setIsActive("1");
										organizationContact.setTenantId(basicDetails.getTenantId());

										orgContactList.add(organizationContact);
//									} 
//									else {
//										System.out.println("Duplicate find ::::::::");
//										throw new DuplicateDataException("Duplicate data found for name: " + mobileNo);
//									}
								} else {
									System.out.println("first time records");
									OrganizationContact organizationContact = new OrganizationContact();
									organizationContact.setOrganizationId(orgId);
									organizationContact.setOrganizationName(row.getCell(0).getStringCellValue());
									organizationContact.setAffiliatedBy(row.getCell(1).getStringCellValue());
									organizationContact.setEmailId(row.getCell(2).getStringCellValue());
							//		organizationContact.setMobileNumber(String.valueOf((int) row.getCell(3).getNumericCellValue()));
							organizationContact.setMobileNumber(mobileNo);
									organizationContact.setAddress(row.getCell(4).getStringCellValue());
				//					organizationContact.setWebsiteLink(row.getCell(5).getStringCellValue());
									organizationContact.setAddedBy(empId);
									organizationContact.setAddedDate(checkOutDaoImpl.getDateTime());
									organizationContact.setIsActive("1");
									organizationContact.setTenantId(basicDetails.getTenantId());

									orgContactList.add(organizationContact);
								}
								organizationContactDao.saveAll(orgContactList);								
							} else {
								System.out.println("Not avaliable the organization contact :::::: ");
							}					
					}
				}
			}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while upload organization contact excel " + ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public List<OrganizationContact> checkDuplicateOrgContact(String mobileNo) throws Exception {
		List<OrganizationContact> organizationContacts = organizationContactDao.checkDuplicateOrgContact(mobileNo);
		try {
			if (Objects.nonNull(organizationContacts)) {
				return organizationContacts;
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while check duplicate organization contact code" + ExceptionUtils.getStackTrace(e));
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
	public ByteArrayInputStream exportOrgContact(List<OrganizationContact> organizationContacts) throws Exception {
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Organization_Contact");

			// Header Bold
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);

			Row row = sheet.createRow(0);

			// Define header cell style
			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);

			// Creating header cells
			Cell cell = row.createCell(0);
			cell.setCellValue("Organization Name");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(1);
			cell.setCellValue("Affiliated By");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(2);
			cell.setCellValue("Email Id");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(3);
			cell.setCellValue("Mobile Number");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(4);
			cell.setCellValue("Address");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(5);
			cell.setCellValue("Website Link");
			cell.setCellStyle(headerCellStyle);

			// Creating data rows for each contact
			for (int i = 0; i < organizationContacts.size(); i++) {
				OrganizationContact organizationContact = organizationContacts.get(i);
				if (Objects.nonNull(organizationContact)) {
					Row dataRow = sheet.createRow(i + 1);
					dataRow.createCell(0).setCellValue(organizationContact.getOrganizationName());
					dataRow.createCell(1).setCellValue(organizationContact.getAffiliatedBy());
					dataRow.createCell(2).setCellValue(organizationContact.getEmailId());
					dataRow.createCell(3).setCellValue(organizationContact.getMobileNumber());
					dataRow.createCell(4).setCellValue(organizationContact.getAddress());
					dataRow.createCell(5).setCellValue(organizationContact.getWebsiteLink());
				}
			}

			// Making size of column auto resize to fit with data
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			return new ByteArrayInputStream(outputStream.toByteArray());
		} catch (Exception e) {
			LOGGER.error("Error while download organization contact excel format" + ExceptionUtils.getStackTrace(e));
			throw new RuntimeException("Error Occur");
		}
	}

	@Override
	public List<OrganizationContact> sendMailToCustomer(String category, String subCategory) throws Exception {
		try {
			List<OrganizationContact> organizationContactList = organizationContactDao.sendMailToCustomer(category,
					subCategory);
			if (Objects.nonNull(organizationContactList)) {
				return organizationContactList;
			} else {
				System.out.println("Organization Contact List is Null :::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while send mail " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public long getOrganizationCount(String isActive) throws Exception {
		try {
			long count = organizationContactDao.getOrganizationCount(isActive);
			if (Objects.nonNull(count)) {
				return count;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while count record " + ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}

}
