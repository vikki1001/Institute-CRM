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
import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.CustomerContactDao;
import com.ksv.ktrccrm.dao.EmpRegistartionDao;
import com.ksv.ktrccrm.dao.OrganizationContactDao;
import com.ksv.ktrccrm.dao.impl.CheckInCheckOutDaoImpl;
import com.ksv.ktrccrm.db1.entities.CustomerContact;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.OrganizationContact;
import com.ksv.ktrccrm.db1.entities.SequenceMstCRM;
import com.ksv.ktrccrm.db1.repository.CustomerContactRepository;
import com.ksv.ktrccrm.db1.repository.SequenceMstCRMRepository;
import com.ksv.ktrccrm.service.CustomerContactService;
import com.ksv.ktrccrm.service.OrganizationContactService;

@Service
public class CustomerContactServiceImpl implements CustomerContactService {
	private static final Logger LOGGER = LogManager.getLogger(CustomerContactServiceImpl.class);

	@Autowired
	private CustomerContactDao customerContactDao;
	@Autowired
	private EmpRegistartionDao empRegistartionDao;
	@Autowired
	private OrganizationContactDao organizationContactDao;
	@Autowired
	private SequenceMstCRMRepository sequenceMstCRMRepository;
	@Autowired
	private CustomerContactRepository ccr;

	@Autowired
	private OrganizationContactDao OrganizationContactDao;

	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;

	@Override
	public List<CustomerContact> activeListOfCustomerContact(String isActive) throws Exception {
		List<CustomerContact> customerContactsList = new ArrayList<>();
		try {
			customerContactsList = customerContactDao.activeListOfCustomerContact(isActive);
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while display active list of customer contact " + ExceptionUtils.getStackTrace(e));
		}
		return customerContactsList;
	}

	@Override
	public List<CustomerContact> inActiveListOfCustomerContact(String inActive) throws Exception {
		List<CustomerContact> customerContactsList = new ArrayList<>();
		try {
			customerContactsList = customerContactDao.inActiveListOfCustomerContact(inActive);
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while display inactive list of customer contact " + ExceptionUtils.getStackTrace(e));
		}
		return customerContactsList;
	}

	@Override
	public CustomerContact saveCustomerContact(CustomerContact customerContact) throws Exception {
		String empId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();

			OrganizationContact organizationContact = organizationContactDao
					.getOrgContactById(customerContact.getOrganization());

			EmpBasicDetails basicDetails = empRegistartionDao.getEmpById(empId);

			if (Objects.isNull(customerContact.getCustomerId())) {
				Optional<SequenceMstCRM> sequenceMstCRMOptional = sequenceMstCRMRepository
						.getSequenceKey(ProdConstant.CUSTOMERCONTACT);
				if (sequenceMstCRMOptional.isPresent()) {
					SequenceMstCRM sequenceMstCRM = sequenceMstCRMOptional.get();

					String customerId = sequenceMstCRM.getSequenceKey() + sequenceMstCRM.getSequenceNo();

					/* Duplicate Check */
					List<String> idList = customerContactDao.getListOfCustomerId();
					System.out.println("List of Id's ::::::: " + idList);

					int customerIdNo;
					while (idList.contains(customerId)) {
						System.out.println("DuplicatId..." + customerId);

						String separator = "ST";
						int sepPos = customerId.indexOf(separator);
						if (sepPos == -1) {
						}
						String stringCustomerIdNo = customerId.substring(sepPos + separator.length());
						customerIdNo = Integer.parseInt(stringCustomerIdNo);
						customerIdNo = customerIdNo + 1;
						customerId = sequenceMstCRM.getSequenceKey() + customerIdNo;

						int newSeqNo = customerIdNo + 1;
						String newSequenceNo = String.valueOf(newSeqNo);
						sequenceMstCRM.setSequenceNo(newSequenceNo);

						String newLastSequenceNo = String.valueOf(customerIdNo);
						sequenceMstCRM.setLastSequenceNo(newLastSequenceNo);

						sequenceMstCRMRepository.save(sequenceMstCRM);
					}
					/* Unique ID */
					System.out.println("unique..." + customerId);

					customerContact.setCustomerId(customerId);
					customerContact.setAddedDate(checkOutDaoImpl.getDateTime());
					customerContact.setAddedBy(empId);
					customerContact.setIsActive(ProdConstant.TRUE);
					customerContact.setTenantId(basicDetails.getTenantId());
		//			customerContact.setOrganizationContact(organizationContact);
					customerContactDao.saveCustomerContact(customerContact);
				} else {
					System.out.println("Not avaliable the customer contact :::::: ");
				}
			} else {
				System.out.println("Edit Customer Contact AREA :::::::::::: ");

				Optional<CustomerContact> optional = customerContactDao
						.getCustomerContactById(customerContact.getCustomerId());
				if (optional.isPresent()) {
					CustomerContact newCustomerContact = optional.get();
					newCustomerContact.setFirstName(customerContact.getFirstName());
					newCustomerContact.setLastName(customerContact.getLastName());
					newCustomerContact.setOrganization(customerContact.getOrganization());
					newCustomerContact.setBranch(customerContact.getBranch());
					newCustomerContact.setYear(customerContact.getYear());
					newCustomerContact.setEmailId(customerContact.getEmailId());
					newCustomerContact.setMobileNumber(customerContact.getMobileNumber());
					newCustomerContact.setLinkedinURL(customerContact.getLinkedinURL());
					newCustomerContact.setTwitterURL(customerContact.getTwitterURL());
					newCustomerContact.setFacebookURL(customerContact.getFacebookURL());
					newCustomerContact.setAddress(customerContact.getAddress());
					newCustomerContact.setModifiedDate(checkOutDaoImpl.getDateTime());
					newCustomerContact.setModifiedBy(empId);
					newCustomerContact.setTenantId(basicDetails.getTenantId());
					customerContactDao.saveCustomerContact(newCustomerContact);
				} else {
					System.out.println("ERROR IN EDIT OF CUSTOMER CONTACT :::::::::::: ");
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while save customer contact record " + ExceptionUtils.getStackTrace(e));
		}
		return customerContact;
	}

	@Override
	public CustomerContact activateCustomerContact(String id) throws Exception {
		try {
			Optional<CustomerContact> optional = customerContactDao.getCustomerContactById(id);
			if (optional.isPresent()) {
				CustomerContact customerContact = optional.get();
				customerContact.setIsActive("1");
				customerContactDao.saveCustomerContact(customerContact);
			} else {
				System.out.println("CUSTOMER CONTACT NOT ACTIVATE SERVICE::::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while active customer contact " + ExceptionUtils.getStackTrace(e));
		}
		return new CustomerContact();
	}

	@Override
	public CustomerContact deactivateCustomerContact(String id) throws Exception {
		try {
			Optional<CustomerContact> optional = customerContactDao.getCustomerContactById(id);
			if (optional.isPresent()) {
				CustomerContact customerContact = optional.get();
				customerContact.setIsActive("0");
				customerContactDao.saveCustomerContact(customerContact);
			} else {
				System.out.println("CUSTOMER CONTACT NOT DEACTIVATE SERVICE::::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while deactive customer contact " + ExceptionUtils.getStackTrace(e));
		}
		return new CustomerContact();
	}

	@Override
	public CustomerContact getDataByCustomerContact(String id) throws Exception {
		CustomerContact customerContact = customerContactDao.getDataByCustomerContact(id);
		try {
			if (Objects.nonNull(customerContact)) {
				return customerContact;
			} else {
				System.out.println("CUSTOMER CONTACT DATA GET BY ID SERVICE::::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get data by id customer contact " + ExceptionUtils.getStackTrace(e));
		}
		return new CustomerContact();
	}

/* Read data form excel and update to database */	
	@Override
	public void saveAll(MultipartFile files) throws Exception {
		String empId;
		
		List<CustomerContact> customerContactList = new ArrayList<>();
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();

			EmpBasicDetails basicDetails = empRegistartionDao.getEmpById(empId);

			List<CustomerContact> customerContactsList = customerContactDao
					.activeListOfCustomerContact(ProdConstant.TRUE);

			try (XSSFWorkbook workbook = new XSSFWorkbook(files.getInputStream())) {
				// Read data form excel file sheet
				XSSFSheet worksheet = workbook.getSheetAt(0);
				System.out.println("number of rows :::::::"+worksheet.getPhysicalNumberOfRows());
				for (int index = 0; index < worksheet.getPhysicalNumberOfRows(); index++) {
					
					if (index > 0) {
						XSSFRow row = worksheet.getRow(index);
	
		if (row.getCell(index) == null) {
			 System.out.println("BLANK RECORD IN EXCEL :::::::::::");
		} else {
		}
						
						
						
						/* Custom Id Generate */
						Optional<SequenceMstCRM> sequenceMstCRMOptional = sequenceMstCRMRepository
								.getSequenceKey(ProdConstant.CUSTOMERCONTACT);

						if (sequenceMstCRMOptional.isPresent()) {
							SequenceMstCRM sequenceMstCRM = sequenceMstCRMOptional.get();

							String customerId = sequenceMstCRM.getSequenceKey() + sequenceMstCRM.getSequenceNo();

							/* Duplicate Check */
		System.out.println("Customer Duplicate Check start............");					
							List<String> idList = customerContactDao.getListOfCustomerId();
							System.out.println("List of Id's ::::::: " + idList);

							int customerIdNo;
							while (idList.contains(customerId)) {
								System.out.println("DuplicatId..." + customerId);

								String separator = "ST";
								int sepPos = customerId.indexOf(separator);
								if (sepPos == -1) {
								}
								String stringCustomerIdNo = customerId.substring(sepPos + separator.length());
								customerIdNo = Integer.parseInt(stringCustomerIdNo);
								customerIdNo = customerIdNo + 1;
								customerId = sequenceMstCRM.getSequenceKey() + customerIdNo;

								int newSeqNo = customerIdNo + 1;
								String newSequenceNo = String.valueOf(newSeqNo);
								sequenceMstCRM.setSequenceNo(newSequenceNo);

								String newLastSequenceNo = String.valueOf(customerIdNo);
								sequenceMstCRM.setLastSequenceNo(newLastSequenceNo);

								sequenceMstCRMRepository.save(sequenceMstCRM);
							}
							
				/* Unique ID */
							System.out.println("unique..." + customerId);
							

				//			System.out.println("MOBILE NO ::::: " + row.getCell(7).getStringCellValue());
                //		Integer mobileNo = (int) row.getCell(7).getNumericCellValue();
				//			String mobileNo = row.getCell(7).getStringCellValue();
							
							String mobileNo = null;
							String year = null;
							
							if (row.getCell(7).getCellType() == CellType.NUMERIC) {
							    // Convert the numeric value to a string
								System.out.println("mobile number = "+row.getCell(7).getNumericCellValue());
								mobileNo = 	String.valueOf((long)row.getCell(7).getNumericCellValue());
								System.out.println("MOBILE NUMBER in Service (if):::::: " + mobileNo);
							} else if (row.getCell(7).getCellType() == CellType.STRING) {
							    // Handle string values
									
								 mobileNo = row.getCell(7).getStringCellValue();
									System.out.println("MOBILE NUMBER in Service (else) :::::: " + mobileNo);
							}
							else {
								System.out.println("else block to ckeck mobile number ");
							}
							
							
							if (row.getCell(5).getCellType() == CellType.NUMERIC) {
							    // Convert the numeric value to a string
									year = String.valueOf((long)row.getCell(5).getNumericCellValue());
								} else if (row.getCell(5).getCellType() == CellType.STRING) {
							    // Handle string values
									year = row.getCell(5).getStringCellValue();
								} else {
									System.out.println("else block to ckeck year number ");
								}
							
							/* Insert Record */				
/*duplicate check*/
							if (customerContactsList != null && !(customerContactsList.isEmpty())) {
						System.out.println("inside duplicate check start...........");
						
								if (!checkDuplicateException(mobileNo)) {
						System.out.println("if........duplicate not found");			
//									if (!checkDuplicateException(row.getCell(6).getStringCellValue())) {
								//	if (!checkDuplicateException(String.valueOf((int) row.getCell(7).getNumericCellValue()))) {
						  
										System.out.println("Duplicate NOT find::::::::");
										
										CustomerContact customerContact = new CustomerContact();
										
//			if (row.getCell(index) == null) {
//				System.out.println("BLANK RECORD IN EXCEL :::::::::::");
//				customerContact.setLinkedinURL("");
//				customerContact.setTwitterURL("");
//				customerContact.setFacebookURL("");
//				customerContact.setAddress("");
//			} else {
//				customerContact.setLinkedinURL(row.getCell(index).getStringCellValue());
//				customerContact.setTwitterURL(row.getCell(index).getStringCellValue());
//				customerContact.setFacebookURL(row.getCell(index).getStringCellValue());
//				customerContact.setAddress(row.getCell(index).getStringCellValue());
//				
//			}						
										
										customerContact.setCustomerId(customerId);
										customerContact.setFirstName(row.getCell(1).getStringCellValue());
										customerContact.setLastName(row.getCell(2).getStringCellValue());
										customerContact.setOrganization(row.getCell(3).getStringCellValue());
										customerContact.setBranch(row.getCell(4).getStringCellValue());
									
							   customerContact.setYear(year);
										customerContact.setEmailId(row.getCell(6).getStringCellValue());
						//				customerContact.setMobileNumber(mobileNo);
						//			customerContact.setMobileNumber(row.getCell(6).getStringCellValue());
										
								customerContact.setMobileNumber(mobileNo);
//										customerContact.setLinkedinURL(row.getCell(8).getStringCellValue());
//										customerContact.setTwitterURL(row.getCell(9).getStringCellValue());
//										customerContact.setFacebookURL(row.getCell(10).getStringCellValue());
//										customerContact.setAddress(row.getCell(11).getStringCellValue());
//										customerContact.setAddedBy(empId);
										customerContact.setAddedDate(checkOutDaoImpl.getDateTime());
										customerContact.setIsActive("1");
										customerContact.setTenantId(basicDetails.getTenantId());

										customerContactList.add(customerContact);
//									} else {
//										System.out.println("Duplicate find ::::::::");
//									}
									
								} else {
									System.out.println("fresh records");
									CustomerContact customerContact = new CustomerContact();
//			OrganizationContact oc = organizationContactDao.getOrgContactById(row.getCell(3).getStringCellValue());									
									
//			if (row.getCell(index) == null) {
//				System.out.println("BLANK RECORD IN EXCEL :::::::::::");
//				customerContact.setLinkedinURL("");
//				customerContact.setTwitterURL("");
//				customerContact.setFacebookURL("");
//				customerContact.setAddress("");
//			} else {
//				customerContact.setLinkedinURL(row.getCell(index).getStringCellValue());
//				customerContact.setTwitterURL(row.getCell(index).getStringCellValue());
//				customerContact.setFacebookURL(row.getCell(index).getStringCellValue());
//				customerContact.setAddress(row.getCell(index).getStringCellValue());					
//			}
									
									customerContact.setCustomerId(customerId);
									customerContact.setFirstName(row.getCell(1).getStringCellValue());
									customerContact.setLastName(row.getCell(2).getStringCellValue());
									customerContact.setOrganization(row.getCell(3).getStringCellValue());
							        customerContact.setBranch(row.getCell(4).getStringCellValue());
				//	customerContact.setYear(row.getCell(5).getStringCellValue());
					
	
					    customerContact.setYear(year);
					            customerContact.setEmailId(row.getCell(6).getStringCellValue());
//								customerContact.setMobileNumber(row.getCell(6).getStringCellValue());
					   //    	customerContact.setMobileNumber(mobileNo);
					
					    customerContact.setMobileNumber(mobileNo);				
//									customerContact.setLinkedinURL(row.getCell(8).getStringCellValue());
//									customerContact.setTwitterURL(row.getCell(9).getStringCellValue());
//									customerContact.setFacebookURL(row.getCell(10).getStringCellValue());
//									customerContact.setAddress(row.getCell(11).getStringCellValue());
									customerContact.setAddedBy(empId);
									customerContact.setAddedDate(checkOutDaoImpl.getDateTime());
									customerContact.setIsActive("1");
									customerContact.setTenantId(basicDetails.getTenantId());									
									
							//		OrganizationContact oc = new OrganizationContact();
							//		oc.setCustomerContact(customerContactsList);
							//		customerContact.setOrganizationContact(oc);
									
//	OrganizationContact oc = organizationContactDao.getOrgContactById(row.getCell(3).getStringCellValue());
//	    					            
//	System.out.println("organisation id--- :::::::::::::: "+oc.getOrganizationId());
//	
//	customerContact.setOrganizationContact(oc);
	
	                                /*
									 * if(oc.getOrganizationId().equals(row.getCell(3).getStringCellValue())) {
									 * System.out.println("organisation id ::::::::::"+oc.getOrganizationId());
									 * customerContact.setOrganizationContact(oc); }
									 */
									
									// customerContact.setOrganizationContact();
									
									customerContactList.add(customerContact);
								}
								customerContactDao.saveAll(customerContactList);
							} else {
								System.out.println("Not avaliable the customer contact :::::: ");
							}
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while upload customer contact excel " + ExceptionUtils.getStackTrace(e));
		}
	}

	/*check Duplicate Customer Contact*/
	@Override
	public List<CustomerContact> checkDuplicateCustomerContact(String customerId) throws Exception {
		System.out.println("mob................(checkDuplicateCustomerContact) = "+customerId);
		List<CustomerContact> customerContacts = customerContactDao.checkDuplicateCustomerContact(customerId);
		try {
			if (Objects.nonNull(customerContacts)) {
				return customerContacts;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while check duplicate customer contact code" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public boolean checkDuplicateException(String customerId) throws Exception {
		try {
			List<CustomerContact> customerContacts = customerContactDao.checkDuplicateCustomerContact(customerId);
			if (Objects.nonNull(customerContacts)) {
				return true;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while find duplicate customer contact excel" + ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public List<CustomerContact> findAll() throws Exception {
		try {
			List<CustomerContact> customerContact = customerContactDao.findAll();
			if (customerContact != null) {
				return customerContact;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display all customer contact" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public ByteArrayInputStream exportCustomerContactExcel(List<CustomerContact> customerContacts) throws Exception {
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Customer_Contact");

			// Header Bold
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);

			Row row = sheet.createRow(0);

			// Define header cell style
			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);

			// Creating header cells
			Cell cell = row.createCell(0);
			cell.setCellValue("Id");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(1);
			cell.setCellValue("First Name");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(2);
			cell.setCellValue("Last Name");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(3);
			cell.setCellValue("Organization");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(4);
			cell.setCellValue("Branch");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(5);
			cell.setCellValue("Year");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(6);
			cell.setCellValue("Email Id");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(7);
			cell.setCellValue("Mobile Number");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(8);
			cell.setCellValue("Linkedin URL");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(9);
			cell.setCellValue("Twitter URL");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(10);
			cell.setCellValue("Facebook URL");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(11);
			cell.setCellValue("Address");
			cell.setCellStyle(headerCellStyle);

			// Creating data rows for each contact
			for (int i = 0; i < customerContacts.size(); i++) {
				CustomerContact customerContact = customerContacts.get(i);
				if (Objects.nonNull(customerContact)) {
					Row dataRow = sheet.createRow(i + 1);
					dataRow.createCell(0).setCellValue(customerContact.getCustomerId());

					dataRow.createCell(1).setCellValue(customerContact.getFirstName());
					dataRow.createCell(2).setCellValue(customerContact.getLastName());
					dataRow.createCell(3).setCellValue(customerContact.getOrganization());
					dataRow.createCell(4).setCellValue(customerContact.getBranch());
					dataRow.createCell(5).setCellValue(customerContact.getYear());
					dataRow.createCell(6).setCellValue(customerContact.getEmailId());
					dataRow.createCell(7).setCellValue(customerContact.getMobileNumber());
					dataRow.createCell(8).setCellValue(customerContact.getLinkedinURL());
					dataRow.createCell(9).setCellValue(customerContact.getTwitterURL());
					dataRow.createCell(10).setCellValue(customerContact.getFacebookURL());
					dataRow.createCell(11).setCellValue(customerContact.getAddress());
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
			sheet.autoSizeColumn(10);
			sheet.autoSizeColumn(11);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			return new ByteArrayInputStream(outputStream.toByteArray());
		} catch (Exception e) {
			LOGGER.error("Error while download customer contact excel format" + ExceptionUtils.getStackTrace(e));
			throw new RuntimeException("Error Occur");
		}
	}

	@Override
	public long getStudentsCount(String isActive) throws Exception {
		try {
			long count = customerContactDao.getStudentsCount(isActive);
			if (Objects.nonNull(count)) {
				return count;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while count record " + ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}

}
