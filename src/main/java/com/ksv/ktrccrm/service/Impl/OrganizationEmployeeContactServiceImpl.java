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
import com.ksv.ktrccrm.dao.OrganizationEmployeeContactDao;
import com.ksv.ktrccrm.dao.impl.CheckInCheckOutDaoImpl;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.OrganizationContact;
import com.ksv.ktrccrm.db1.entities.OrganizationEmployeeContact;
import com.ksv.ktrccrm.db1.entities.SequenceMstCRM;
import com.ksv.ktrccrm.db1.repository.SequenceMstCRMRepository;
import com.ksv.ktrccrm.service.OrganizationEmployeeContactService;

@Service
public class OrganizationEmployeeContactServiceImpl implements OrganizationEmployeeContactService {
	private static final Logger LOGGER = LogManager.getLogger(OrganizationEmployeeContactServiceImpl.class);

	@Autowired
	private OrganizationEmployeeContactDao oecDao;
	@Autowired
	private EmpRegistartionDao empRegistartionDao;
	@Autowired
	private SequenceMstCRMRepository sequenceMstCRMRepository;
	@Autowired
	private OrganizationContactDao organizationContactDao;

	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;
	
	@Override
	public List<OrganizationEmployeeContact> activeListOfOEC(String isActive) throws Exception {
		List<OrganizationEmployeeContact> oecList = new ArrayList<>();
		try {
			oecList = oecDao.activeListOfOEC(isActive);
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
			oecList = oecDao.inActiveListOfOEC(inActive);
		} catch (Exception e) {
			LOGGER.error("Error occur while display inactive list of organization employee contact "
					+ ExceptionUtils.getStackTrace(e));
		}
		return oecList;
	}

/* Read data form excel and update to database */	
	@Override
	public OrganizationEmployeeContact saveOEC(OrganizationEmployeeContact oec) throws Exception {
		String empId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();

			EmpBasicDetails basicDetails = empRegistartionDao.getEmpById(empId);

			OrganizationContact organizationContact = organizationContactDao.getOrgContactById(oec.getOrganization());

			System.out.println("TENANT ID " + basicDetails.getTenantId());

			if (Objects.isNull(oec.getOrgEmpContactID())) {
				Optional<SequenceMstCRM> sequenceMstCRMOptional = sequenceMstCRMRepository
						.getSequenceKey(ProdConstant.ORGANIZATIONCONTACTEMPLOYEE);
				if (sequenceMstCRMOptional.isPresent()) {
					SequenceMstCRM sequenceMstCRM = sequenceMstCRMOptional.get();

					String orgOECId = sequenceMstCRM.getSequenceKey() + sequenceMstCRM.getSequenceNo();

					/* Duplicate Check */
					List<String> idList = oecDao.getListOfOECId();
					System.out.println("List of Id's ::::::: " + idList);

					int orgOECIdNo;
					while (idList.contains(orgOECId)) {
						System.out.println("DuplicatId..." + orgOECId);

						String separator = "ORE";
						int sepPos = orgOECId.indexOf(separator);
						if (sepPos == -1) {
						}
						String stringorgOECIdNo = orgOECId.substring(sepPos + separator.length());
						orgOECIdNo = Integer.parseInt(stringorgOECIdNo);
						orgOECIdNo = orgOECIdNo + 1;
						orgOECId = sequenceMstCRM.getSequenceKey() + orgOECIdNo;

						int newSeqNo = orgOECIdNo + 1;
						String newSequenceNo = String.valueOf(newSeqNo);
						sequenceMstCRM.setSequenceNo(newSequenceNo);

						String newLastSequenceNo = String.valueOf(orgOECIdNo);
						sequenceMstCRM.setLastSequenceNo(newLastSequenceNo);

						sequenceMstCRMRepository.save(sequenceMstCRM);
					}
					/* Unique ID */
					System.out.println("unique..." + orgOECId);

					
					oec.setOrgEmpContactID(orgOECId);
					oec.setAddedDate(checkOutDaoImpl.getDateTime());
					oec.setAddedBy(empId);
					oec.setIsActive(ProdConstant.TRUE);
					oec.setTenantId(basicDetails.getTenantId());
	//				oec.setOrganizationContact(organizationContact);
					oecDao.saveOEC(oec);
				} else {
					System.out.println("Not avaliable the organization employee contact :::::: ");
				}
			} else {
				System.out.println("Edit Organization Employee Contact AREA :::::::::::: ");

				Optional<OrganizationEmployeeContact> optional = oecDao.getOECById(oec.getOrgEmpContactID());
				if (optional.isPresent()) {
					OrganizationEmployeeContact newOEC = optional.get();
					newOEC.setFirstName(oec.getFirstName());
					newOEC.setLastName(oec.getLastName());
					newOEC.setTitle(oec.getTitle());
					newOEC.setOrganization(oec.getOrganization());
					newOEC.setEmailId(oec.getEmailId());
					newOEC.setMobileNumber(oec.getMobileNumber());
					newOEC.setLinkedinURL(oec.getLinkedinURL());
					newOEC.setTwitterURL(oec.getTwitterURL());
					newOEC.setFacebookURL(oec.getFacebookURL());
					newOEC.setAddress(oec.getAddress());
					newOEC.setModifiedDate(checkOutDaoImpl.getDateTime());
					newOEC.setModifiedBy(empId);
					newOEC.setTenantId(basicDetails.getTenantId());
					oecDao.saveOEC(newOEC);
				} else {
					System.out.println("ERROR IN EDIT OF ORGANIZATION EMPLOYEE CONTACT :::::::::::: ");
				}
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while save organization employee contact record " + ExceptionUtils.getStackTrace(e));
		}
		return oec;
	}

	@Override
	public OrganizationEmployeeContact activateOEC(String id) throws Exception {
		try {
			Optional<OrganizationEmployeeContact> optional = oecDao.getOECById(id);
			if (optional.isPresent()) {
				OrganizationEmployeeContact oec = optional.get();
				oec.setIsActive("1");
				oecDao.saveOEC(oec);
			} else {
				System.out.println("ORGANIZATION EMPLOYEE CONTACT ACTIVATE SERVICE::::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while active organization employee contact " + ExceptionUtils.getStackTrace(e));
		}
		return new OrganizationEmployeeContact();
	}

	@Override
	public OrganizationEmployeeContact deactivateOEC(String id) throws Exception {
		try {
			Optional<OrganizationEmployeeContact> optional = oecDao.getOECById(id);
			if (optional.isPresent()) {
				OrganizationEmployeeContact oec = optional.get();
				oec.setIsActive("0");
				oecDao.saveOEC(oec);
			} else {
				System.out.println("ORGANIZATION EMPLOYEE CONTACT NOT DEACTIVATE SERVICE::::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while deactive organization employee contact " + ExceptionUtils.getStackTrace(e));
		}
		return new OrganizationEmployeeContact();
	}

	@Override
	public OrganizationEmployeeContact getOECByOrgEmpContactID(String orgEmpContactID) throws Exception {
		OrganizationEmployeeContact organizationEmployeeContact = oecDao.getOECByOrgEmpContactID(orgEmpContactID);
		try {
			if (Objects.nonNull(organizationEmployeeContact)) {
				return organizationEmployeeContact;
			} else {
				System.out.println("ORGANIZATION EMPLOYEE CONTACT NOT GET BY ID SERVICE::::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while get organization employee contact by id " + ExceptionUtils.getStackTrace(e));
		}
		return new OrganizationEmployeeContact();
	}

/* Read data form excel and update to database */
	@Override
	public void saveAll(MultipartFile files) throws Exception {
		String empId;
		List<OrganizationEmployeeContact> oecList = new ArrayList<>();
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();

			EmpBasicDetails basicDetails = empRegistartionDao.getEmpById(empId);

			List<OrganizationEmployeeContact> orgEmpContactID = oecDao.activeListOfOEC(ProdConstant.TRUE);

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
								.getSequenceKey(ProdConstant.ORGANIZATIONCONTACTEMPLOYEE);
						if (sequenceMstCRMOptional.isPresent()) {
							SequenceMstCRM sequenceMstCRM = sequenceMstCRMOptional.get();

							String orgOECId = sequenceMstCRM.getSequenceKey() + sequenceMstCRM.getSequenceNo();

							/* Duplicate Check */
							System.out.println("Organisation Customer Duplicate Check start............");
							List<String> idList = oecDao.getListOfOECId();
							System.out.println("List of Id's ::::::: " + idList);

							int orgOECIdNo;
							while (idList.contains(orgOECId)) {
								System.out.println("DuplicatId..." + orgOECId);

								String separator = "ORE";
								int sepPos = orgOECId.indexOf(separator);
								if (sepPos == -1) {
								}
								String stringorgOECIdNo = orgOECId.substring(sepPos + separator.length());
								orgOECIdNo = Integer.parseInt(stringorgOECIdNo);
								orgOECIdNo = orgOECIdNo + 1;
								orgOECId = sequenceMstCRM.getSequenceKey() + orgOECIdNo;

								int newSeqNo = orgOECIdNo + 1;
								String newSequenceNo = String.valueOf(newSeqNo);
								sequenceMstCRM.setSequenceNo(newSequenceNo);

								String newLastSequenceNo = String.valueOf(orgOECIdNo);
								sequenceMstCRM.setLastSequenceNo(newLastSequenceNo);

								sequenceMstCRMRepository.save(sequenceMstCRM);
							}
							/* Unique ID */
							System.out.println("unique..." + orgOECId);

//							Integer mobileNo =  (int) row.getCell(6).getNumericCellValue();
//							System.out.println("MOBILE NUMBER in Service :::::: " + mobileNo);
							
							String mobileNo = null;
							String year = null;
							
							if (row.getCell(5).getCellType() == CellType.NUMERIC) {
							    // Convert the numeric value to a string
								System.out.println("mobile number = "+row.getCell(5).getNumericCellValue());
								mobileNo = 	String.valueOf((long)row.getCell(5).getNumericCellValue());
								System.out.println("MOBILE NUMBER in Service (if):::::: " + mobileNo);
							} else if (row.getCell(5).getCellType() == CellType.STRING) {
							    // Handle string values
									
								 mobileNo = row.getCell(5).getStringCellValue();
									System.out.println("MOBILE NUMBER in Service (else) :::::: " + mobileNo);
							}
							else {
								System.out.println("else block to ckeck mobile number ");
							}
							
							
							if (orgEmpContactID != null && !(orgEmpContactID.isEmpty())) {
								if (!checkDuplicateException(String.valueOf(mobileNo))) {
									System.out.println("Duplicate NOT find::::::::");
									OrganizationEmployeeContact organizationEmployeeContact = new OrganizationEmployeeContact();
									organizationEmployeeContact.setOrgEmpContactID(orgOECId);
									organizationEmployeeContact.setFirstName(row.getCell(0).getStringCellValue());
									organizationEmployeeContact.setLastName(row.getCell(1).getStringCellValue());
									organizationEmployeeContact.setTitle(row.getCell(2).getStringCellValue());
									organizationEmployeeContact.setOrganization(row.getCell(3).getStringCellValue());
									organizationEmployeeContact.setEmailId(row.getCell(4).getStringCellValue());
					//				organizationEmployeeContact.setMobileNumber(String.valueOf((int) row.getCell(6).getNumericCellValue()));
									organizationEmployeeContact.setMobileNumber(mobileNo);
						
									organizationEmployeeContact.setLinkedinURL(row.getCell(6).getStringCellValue());
									organizationEmployeeContact.setTwitterURL(row.getCell(7).getStringCellValue());
									organizationEmployeeContact.setFacebookURL(row.getCell(8).getStringCellValue());
									organizationEmployeeContact.setAddress(row.getCell(9).getStringCellValue());
									organizationEmployeeContact.setAddedBy(empId);
									organizationEmployeeContact.setAddedDate(checkOutDaoImpl.getDateTime());
									organizationEmployeeContact.setIsActive("1");
									organizationEmployeeContact.setTenantId(basicDetails.getTenantId());

									oecList.add(organizationEmployeeContact);
//								} else {
//									System.out.println("Duplicate find ::::::::");
//								}
							} else {
								System.out.println("fresh records");
								OrganizationEmployeeContact organizationEmployeeContact = new OrganizationEmployeeContact();
								organizationEmployeeContact.setOrgEmpContactID(orgOECId);
								organizationEmployeeContact.setFirstName(row.getCell(0).getStringCellValue());
								organizationEmployeeContact.setLastName(row.getCell(1).getStringCellValue());
								organizationEmployeeContact.setTitle(row.getCell(2).getStringCellValue());
								organizationEmployeeContact.setOrganization(row.getCell(3).getStringCellValue());
								organizationEmployeeContact.setEmailId(row.getCell(4).getStringCellValue());
						//		organizationEmployeeContact.setMobileNumber(String.valueOf((int) row.getCell(6).getNumericCellValue()));
								organizationEmployeeContact.setMobileNumber(mobileNo);
								
								organizationEmployeeContact.setLinkedinURL(row.getCell(6).getStringCellValue());
								organizationEmployeeContact.setTwitterURL(row.getCell(7).getStringCellValue());
								organizationEmployeeContact.setFacebookURL(row.getCell(8).getStringCellValue());
								organizationEmployeeContact.setAddress(row.getCell(9).getStringCellValue());
								organizationEmployeeContact.setAddedBy(empId);
								organizationEmployeeContact.setAddedDate(checkOutDaoImpl.getDateTime());
								organizationEmployeeContact.setIsActive("1");
								organizationEmployeeContact.setTenantId(basicDetails.getTenantId());

								oecList.add(organizationEmployeeContact);
							}
							oecDao.saveAll(oecList);							
						} else {
							System.out.println("Not avaliable the organization employee contact :::::: ");
						}						
					}
				}
			}
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while upload organization empoyee contact excel " + ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public List<OrganizationEmployeeContact> checkDuplicateOEC(String mobileNo) throws Exception {
		List<OrganizationEmployeeContact> oec = oecDao.checkDuplicateOEC(mobileNo);
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
			List<OrganizationEmployeeContact> oec = oecDao.findAll();
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
	public ByteArrayInputStream exportOECExcel(List<OrganizationEmployeeContact> oec) throws Exception {
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Organization_Employee_Contact");

			// Header Bold
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);

			Row row = sheet.createRow(0);

			// Define header cell style
			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);

			// Creating header cells
			Cell cell = row.createCell(0);
			cell.setCellValue("First Name");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(1);
			cell.setCellValue("Last Name");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(2);
			cell.setCellValue("Title");
			cell.setCellStyle(headerCellStyle);
			
			cell = row.createCell(3);
			cell.setCellValue("Organization");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(4);
			cell.setCellValue("Email Id");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(5);
			cell.setCellValue("Mobile Number");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(6);
			cell.setCellValue("Linkedin URL");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(7);
			cell.setCellValue("Twitter URL");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(8);
			cell.setCellValue("Facebook URL");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(9);
			cell.setCellValue("Address");
			cell.setCellStyle(headerCellStyle);

			// Creating data rows for each contact
			for (int i = 0; i < oec.size(); i++) {
				OrganizationEmployeeContact organizationEmployeeContact = oec.get(i);
				if (Objects.nonNull(organizationEmployeeContact)) {
					Row dataRow = sheet.createRow(i + 1);
					dataRow.createCell(0).setCellValue(organizationEmployeeContact.getFirstName());
					dataRow.createCell(1).setCellValue(organizationEmployeeContact.getLastName());
					dataRow.createCell(2).setCellValue(organizationEmployeeContact.getTitle());
					dataRow.createCell(3).setCellValue(organizationEmployeeContact.getOrganization());
					dataRow.createCell(4).setCellValue(organizationEmployeeContact.getEmailId());
					dataRow.createCell(5).setCellValue(organizationEmployeeContact.getMobileNumber());
					dataRow.createCell(6).setCellValue(organizationEmployeeContact.getLinkedinURL());
					dataRow.createCell(7).setCellValue(organizationEmployeeContact.getTwitterURL());
					dataRow.createCell(8).setCellValue(organizationEmployeeContact.getFacebookURL());
					dataRow.createCell(9).setCellValue(organizationEmployeeContact.getAddress());
				}
			}

			// Making size of column auto resize to fit with data
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(6);
			sheet.autoSizeColumn(7);
			sheet.autoSizeColumn(8);
			sheet.autoSizeColumn(9);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			return new ByteArrayInputStream(outputStream.toByteArray());
		} catch (Exception e) {
			LOGGER.error("Error while download organization employee contact excel format"
					+ ExceptionUtils.getStackTrace(e));
			throw new RuntimeException("Error Occur");
		}
	}

	@Override
	public long getOrganizationEmployeeCount(String isActive) throws Exception {
		try {
			long count = oecDao.getOrganizationEmployeeCount(isActive);
			if (Objects.nonNull(count)) {
				return count;	
			}			
		} catch (Exception e) {
			LOGGER.error("Error occur while count record " + ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}

}
