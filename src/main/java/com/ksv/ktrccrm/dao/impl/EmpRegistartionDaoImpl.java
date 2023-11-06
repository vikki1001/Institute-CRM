package com.ksv.ktrccrm.dao.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.EmpRegistartionDao;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.EmpPersonalDetails;
import com.ksv.ktrccrm.db1.entities.EmpWorkDetails;
import com.ksv.ktrccrm.db1.entities.LeaveTracker;
import com.ksv.ktrccrm.db1.entities.SequenceMst;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.methodUtils.MethodUtils;
import com.ksv.ktrccrm.db1.repository.EmpBasicRepository;
import com.ksv.ktrccrm.db1.repository.EmpPersonalRepository;
import com.ksv.ktrccrm.db1.repository.EmpWorkRepository;
import com.ksv.ktrccrm.db1.repository.LeaveTrackerRepository;
import com.ksv.ktrccrm.db1.repository.SequenceMstRepository;
import com.ksv.ktrccrm.service.UserService;

@Repository
public class EmpRegistartionDaoImpl implements EmpRegistartionDao {
	private static final Logger LOG = LogManager.getLogger(EmpRegistartionDaoImpl.class);
	
	@Autowired
	private EmpBasicRepository empBasicRepository;	
	@Autowired
	private EmpWorkRepository empWorkRepository;
	@Autowired
	private SequenceMstRepository sequenceMstRepository;	
	@Autowired
	private EmpPersonalRepository empPersonalRepository;
	@Autowired
	private LeaveTrackerRepository leaveTrackerRepository;
	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;
	@Autowired
	private UserService userService;
	
	@Override
	public List<EmpBasicDetails> getRecordList(String isActive, String tenantId) throws Exception {
		List<EmpBasicDetails> empBasicDetails = empBasicRepository.getRecordList(isActive, tenantId);
		try {
			if (!empBasicDetails.isEmpty()) {
				return empBasicDetails;
			}
		} catch (Exception e) {
			LOG.error("Error occur while display findAll employee registartion list" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}
	/* Find Employee By Id */
	@Override
	public EmpBasicDetails getEmpById(String empId) throws Exception {
		Optional<EmpBasicDetails> optional = empBasicRepository.findById(empId);
		EmpBasicDetails empBasicDetails = null;
		try {
			if (optional.isPresent()) {
				empBasicDetails = optional.get();
			}
		} catch (Exception e) {
			LOG.error("Error while Emp not found for id " + ExceptionUtils.getStackTrace(e));
		}
		return empBasicDetails;
	}

	/* Save & Update Employee */
	@Override
	public EmpBasicDetails saveBasicDetails(EmpBasicDetails empBasicDetails) throws Exception {
		String loginId;
		try {
			final String QR_CODE_IMAGE_PATH = empBasicDetails.getEmpId() + ProdConstant.PNGEXTENSION;
			System.out.println("saveBasicDetails in daoImpl :::: ");
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			loginId = authentication.getName();
			if (empBasicDetails.getEmpId() == null || empBasicDetails.getEmpId().isEmpty()) {
				System.out.println("INSIDE IF CONDITION OF DAO IMPL :::: ");
				
				Optional<SequenceMst> sequenceMstOptional = sequenceMstRepository.getSequenceKey(empBasicDetails.getTenantId());
				if (sequenceMstOptional.isPresent()) {
					SequenceMst sequenceMst = sequenceMstOptional.get();
					if (Objects.equals(sequenceMst.getTenantId(), empBasicDetails.getTenantId())) {
						Optional<SequenceMst> optional = sequenceMstRepository.findById(sequenceMst.getId());
						if (optional.isPresent()) {
							SequenceMst sequenceMst2 = optional.get();

							String newEmpId = sequenceMst.getSequenceKey() + sequenceMst.getSequenceNo();
							
							/* Duplicate Check */
							List<String> idList = empBasicRepository.getEmpIdWithIsActive(ProdConstant.TRUE);
							System.out.println("List of Emp Id's ::::::: " + idList);
							
							int empIdNo;
							while (idList.contains(newEmpId)) {
								System.out.println("DuplicatId..." + newEmpId);

								String separator = "EP";
								int sepPos = newEmpId.indexOf(separator);
								if (sepPos == -1) {
								}
								String stringempIdNo = newEmpId.substring(sepPos + separator.length());
								empIdNo = Integer.parseInt(stringempIdNo);
								empIdNo = empIdNo + 1;
								newEmpId = sequenceMst2.getSequenceKey() + empIdNo;

								int newSeqNo = empIdNo + 1;
								String newSequenceNo = String.valueOf(newSeqNo);
								sequenceMst2.setSequenceNo(newSequenceNo);

								String newLastSequenceNo = String.valueOf(empIdNo);
								sequenceMst2.setLastSequenceNo(newLastSequenceNo);

								sequenceMstRepository.save(sequenceMst2);
							}
							
								LOG.info("Unique Employee Id " + newEmpId);
								
								empBasicDetails.setEmpId(newEmpId);
								empBasicDetails.setIsActive(ProdConstant.TRUE);
								empBasicDetails.setCreatedDate(checkOutDaoImpl.getDateTime());
								empBasicDetails.setCreatedBy(loginId);
								empBasicDetails.setCoEmailId(empBasicDetails.getCoEmailId().toLowerCase());

								empBasicDetails.getEmpAddressDetails().setEmpBasicDetails(empBasicDetails);
								empBasicDetails.getEmpAddressDetails().setCreatedDate(checkOutDaoImpl.getDateTime());
								empBasicDetails.getEmpAddressDetails().setCreatedBy(loginId);

								empBasicDetails.getEmpWorkDetails().setEmpBasicDetails(empBasicDetails);
								empBasicDetails.getEmpWorkDetails().setCreatedDate(checkOutDaoImpl.getDateTime());
								empBasicDetails.getEmpWorkDetails().setCreatedBy(loginId);

								empBasicDetails.getEmpSkillDetails().setEmpBasicDetails(empBasicDetails);
								empBasicDetails.getEmpSkillDetails().setCreatedDate(checkOutDaoImpl.getDateTime());
								empBasicDetails.getEmpSkillDetails().setCreatedBy(loginId);

								empBasicDetails.getEmpCertificationDetails().setEmpBasicDetails(empBasicDetails);
								empBasicDetails.getEmpCertificationDetails().setCreatedDate(checkOutDaoImpl.getDateTime());
								empBasicDetails.getEmpCertificationDetails().setCreatedBy(loginId);

								empBasicDetails.getEmpSalaryDetails().setEmpBasicDetails(empBasicDetails);
								empBasicDetails.getEmpSalaryDetails().setCreatedDate(checkOutDaoImpl.getDateTime());
								empBasicDetails.getEmpSalaryDetails().setCreatedBy(loginId);

								empBasicDetails.getEmpPassportDetails().setEmpBasicDetails(empBasicDetails);
								empBasicDetails.getEmpPassportDetails().setCreatedDate(checkOutDaoImpl.getDateTime());
								empBasicDetails.getEmpPassportDetails().setCreatedBy(loginId);

								empBasicDetails.getEmpPersonalDetails().setEmpBasicDetails(empBasicDetails);
								empBasicDetails.getEmpPersonalDetails().setCreatedDate(checkOutDaoImpl.getDateTime());
								empBasicDetails.getEmpPersonalDetails().setCreatedBy(loginId);
								
								empBasicDetails.getEmpEmergContactDetails().setEmpBasicDetails(empBasicDetails);
								empBasicDetails.getEmpPersonalDetails().setCreatedDate(checkOutDaoImpl.getDateTime());
								empBasicDetails.getEmpPersonalDetails().setCreatedBy(loginId);
								
								LeaveTracker leaveTracker = new LeaveTracker();
								leaveTracker.setEmpId(newEmpId);
								leaveTracker.setCreatedDate(checkOutDaoImpl.getDateTime());
								leaveTracker.setCreatedBy(loginId);
								leaveTracker.setIsActive(ProdConstant.TRUE);
								leaveTracker.setTenantId(empBasicDetails.getTenantId());
								leaveTracker.setTotalLeave((float) 0);
								leaveTracker.setPaidLeave((float) 0);
								leaveTracker.setMaternityLeave((float) 0);
								leaveTracker.setBookedTotalLeave((float) 0);
								leaveTracker.setBookedPaidLeave((float) 0);
								leaveTracker.setBookedMaternityLeave((float) 0);
								
								final String QR_CODE_IMAGE_PATH2 = newEmpId + ProdConstant.PNGEXTENSION;
								System.out.println("Unique IMAGE ::::::::::: " + QR_CODE_IMAGE_PATH2);
								/* QR Code Generate */
								createQRCode(empBasicDetails);
								
								/* QR Save in DB from local */

								File fnew = new File(QR_CODE_IMAGE_PATH2);
								BufferedImage originalImage = ImageIO.read(fnew.getAbsoluteFile());
								ByteArrayOutputStream baos = new ByteArrayOutputStream();
								ImageIO.write(originalImage, ProdConstant.PNGSMALL, baos);
								byte[] imageInByte = baos.toByteArray();

								empBasicDetails.setQrCode(imageInByte);
								this.empBasicRepository.save(empBasicDetails);
								leaveTrackerRepository.save(leaveTracker);
															
								if (fnew.delete()) {
									System.out.println("File deleted successfully");
								} else {
									System.out.println("Failed to delete the file");
								}
								 
								 return empBasicDetails;							
						}
					}
				}
			} else {
				System.out.println("EDIT EMPLOYEE RECORD AREA :::: ");
				
				Optional<EmpBasicDetails> empBasicDetails2 = empBasicRepository.findById(empBasicDetails.getEmpId());
				if (empBasicDetails2.isPresent()) {
					EmpBasicDetails newEmpBasicDetails = empBasicDetails2.get();
					newEmpBasicDetails.setCoEmailId(empBasicDetails.getCoEmailId().toLowerCase());
					newEmpBasicDetails.setFirstName(empBasicDetails.getFirstName());
					newEmpBasicDetails.setMiddleName(empBasicDetails.getMiddleName());
					newEmpBasicDetails.setLastName(empBasicDetails.getLastName());
					newEmpBasicDetails.setFullName(empBasicDetails.getFullName());
					newEmpBasicDetails.setGrade(empBasicDetails.getGrade());
					newEmpBasicDetails.setGender(empBasicDetails.getGender());
					newEmpBasicDetails.setDepartName(empBasicDetails.getDepartName());
				//	newEmpBasicDetails.setIsActive(ProdConstant.TRUE);
					newEmpBasicDetails.setTenantId(empBasicDetails.getTenantId());
					newEmpBasicDetails.setLastModifiedDate(checkOutDaoImpl.getDateTime());
					newEmpBasicDetails.setLastModifiedBy(loginId);

					newEmpBasicDetails.getEmpPersonalDetails()
							.setBirthDate(empBasicDetails.getEmpPersonalDetails().getBirthDate());
					newEmpBasicDetails.getEmpPersonalDetails().setAge(empBasicDetails.getEmpPersonalDetails().getAge());
					newEmpBasicDetails.getEmpPersonalDetails()
							.setPlaceOfBirth(empBasicDetails.getEmpPersonalDetails().getPlaceOfBirth());
					newEmpBasicDetails.getEmpPersonalDetails()
							.setBloodGroup(empBasicDetails.getEmpPersonalDetails().getBloodGroup());
					newEmpBasicDetails.getEmpPersonalDetails()
							.setContactNo(empBasicDetails.getEmpPersonalDetails().getContactNo());
					newEmpBasicDetails.getEmpPersonalDetails()
							.setAlternateContactNo(empBasicDetails.getEmpPersonalDetails().getAlternateContactNo());
					newEmpBasicDetails.getEmpPersonalDetails()
							.setMaritalStatus(empBasicDetails.getEmpPersonalDetails().getMaritalStatus());
					newEmpBasicDetails.getEmpPersonalDetails()
							.setReligion(empBasicDetails.getEmpPersonalDetails().getReligion());
					newEmpBasicDetails.getEmpPersonalDetails()
							.setAddharNumber(empBasicDetails.getEmpPersonalDetails().getAddharNumber());
					newEmpBasicDetails.getEmpPersonalDetails()
							.setPanNumber(empBasicDetails.getEmpPersonalDetails().getPanNumber());
					newEmpBasicDetails.getEmpPersonalDetails()
							.setPersonalEmailId1(empBasicDetails.getEmpPersonalDetails().getPersonalEmailId1().toLowerCase());
					newEmpBasicDetails.getEmpPersonalDetails()
							.setPersonalEmailId2(empBasicDetails.getEmpPersonalDetails().getPersonalEmailId2().toLowerCase());
					newEmpBasicDetails.getEmpPersonalDetails().setLastModifiedDate(checkOutDaoImpl.getDateTime());
					newEmpBasicDetails.getEmpPersonalDetails().setLastModifiedBy(loginId);

					newEmpBasicDetails.getEmpWorkDetails()
							.setDateOfJoining(empBasicDetails.getEmpWorkDetails().getDateOfJoining());
					newEmpBasicDetails.getEmpWorkDetails()
							.setEmployeeType(empBasicDetails.getEmpWorkDetails().getEmployeeType());
					newEmpBasicDetails.getEmpWorkDetails()
							.setBusinessUnit(empBasicDetails.getEmpWorkDetails().getBusinessUnit());
					newEmpBasicDetails.getEmpWorkDetails()
							.setProjectId(empBasicDetails.getEmpWorkDetails().getProjectId());
					newEmpBasicDetails.getEmpWorkDetails()
							.setKraRole(empBasicDetails.getEmpWorkDetails().getProjectName());
					newEmpBasicDetails.getEmpWorkDetails()
							.setProjectName(empBasicDetails.getEmpWorkDetails().getProjectName());
					newEmpBasicDetails.getEmpWorkDetails()
							.setDesignation(empBasicDetails.getEmpWorkDetails().getDesignation());
					newEmpBasicDetails.getEmpWorkDetails()
							.setReportingManager(empBasicDetails.getEmpWorkDetails().getReportingManager());
					newEmpBasicDetails.getEmpWorkDetails()
							.setBaseLocation(empBasicDetails.getEmpWorkDetails().getBaseLocation());
					newEmpBasicDetails.getEmpWorkDetails()
							.setCurrentLocation(empBasicDetails.getEmpWorkDetails().getCurrentLocation());
					newEmpBasicDetails.getEmpWorkDetails().setLastModifiedDate(checkOutDaoImpl.getDateTime());
					newEmpBasicDetails.getEmpWorkDetails().setLastModifiedBy(loginId);

					newEmpBasicDetails.getEmpAddressDetails()
							.setPresentAddress(empBasicDetails.getEmpAddressDetails().getPresentAddress());
					newEmpBasicDetails.getEmpAddressDetails()
							.setPermanentAddress(empBasicDetails.getEmpAddressDetails().getPermanentAddress());
					newEmpBasicDetails.getEmpAddressDetails()
							.setPresentCity(empBasicDetails.getEmpAddressDetails().getPresentCity());
					newEmpBasicDetails.getEmpAddressDetails()
							.setPermanentCity(empBasicDetails.getEmpAddressDetails().getPermanentCity());
					newEmpBasicDetails.getEmpAddressDetails()
							.setPresentState(empBasicDetails.getEmpAddressDetails().getPresentState());
					newEmpBasicDetails.getEmpAddressDetails()
							.setPermanentState(empBasicDetails.getEmpAddressDetails().getPermanentState());
					newEmpBasicDetails.getEmpAddressDetails()
							.setPresentPinCode(empBasicDetails.getEmpAddressDetails().getPresentPinCode());
					newEmpBasicDetails.getEmpAddressDetails()
							.setPermanentPinCode(empBasicDetails.getEmpAddressDetails().getPermanentPinCode());
					newEmpBasicDetails.getEmpAddressDetails()
							.setPresentCountry(empBasicDetails.getEmpAddressDetails().getPresentCountry());
					newEmpBasicDetails.getEmpAddressDetails()
							.setPermanentCountry(empBasicDetails.getEmpAddressDetails().getPermanentCountry());
					newEmpBasicDetails.getEmpAddressDetails().setLastModifiedDate(checkOutDaoImpl.getDateTime());
					newEmpBasicDetails.getEmpAddressDetails().setLastModifiedBy(loginId);

					newEmpBasicDetails.getEmpSalaryDetails()
							.setSalaryAccBank(empBasicDetails.getEmpSalaryDetails().getSalaryAccBank());
					newEmpBasicDetails.getEmpSalaryDetails()
							.setSalaryAccNo(empBasicDetails.getEmpSalaryDetails().getSalaryAccNo());
					newEmpBasicDetails.getEmpSalaryDetails()
							.setAccIFSCode(empBasicDetails.getEmpSalaryDetails().getAccIFSCode());
					newEmpBasicDetails.getEmpSalaryDetails().setLastModifiedDate(checkOutDaoImpl.getDateTime());
					newEmpBasicDetails.getEmpSalaryDetails().setLastModifiedBy(loginId);

					newEmpBasicDetails.getEmpCertificationDetails()
							.setCertificationName(empBasicDetails.getEmpCertificationDetails().getCertificationName());
					newEmpBasicDetails.getEmpCertificationDetails()
							.setCertVersion(empBasicDetails.getEmpCertificationDetails().getCertVersion());
					newEmpBasicDetails.getEmpCertificationDetails()
							.setCertCompleteDate(empBasicDetails.getEmpCertificationDetails().getCertCompleteDate());
					newEmpBasicDetails.getEmpCertificationDetails()
							.setCertValidTill(empBasicDetails.getEmpCertificationDetails().getCertValidTill());
					newEmpBasicDetails.getEmpCertificationDetails()
							.setCertCode(empBasicDetails.getEmpCertificationDetails().getCertScore());
					newEmpBasicDetails.getEmpCertificationDetails()
							.setCertCode(empBasicDetails.getEmpCertificationDetails().getCertCode());
					newEmpBasicDetails.getEmpCertificationDetails().setLastModifiedDate(checkOutDaoImpl.getDateTime());
					newEmpBasicDetails.getEmpCertificationDetails().setLastModifiedBy(loginId);

					newEmpBasicDetails.getEmpSkillDetails().setSkill(empBasicDetails.getEmpSkillDetails().getSkill());
					newEmpBasicDetails.getEmpSkillDetails()
							.setSkillVersion(empBasicDetails.getEmpSkillDetails().getSkillVersion());
					newEmpBasicDetails.getEmpSkillDetails()
							.setProficiencyLevel(empBasicDetails.getEmpSkillDetails().getProficiencyLevel());
					newEmpBasicDetails.getEmpSkillDetails()
							.setExperienceSkill(empBasicDetails.getEmpSkillDetails().getExperienceSkill());
					newEmpBasicDetails.getEmpSkillDetails()
							.setSelfRating(empBasicDetails.getEmpSkillDetails().getSelfRating());
					newEmpBasicDetails.getEmpSkillDetails().setLastModifiedDate(checkOutDaoImpl.getDateTime());
					newEmpBasicDetails.getEmpSkillDetails().setLastModifiedBy(loginId);

					newEmpBasicDetails.getEmpPassportDetails()
							.setPassportName(empBasicDetails.getEmpPassportDetails().getPassportName());
					newEmpBasicDetails.getEmpPassportDetails()
							.setPassportNo(empBasicDetails.getEmpPassportDetails().getPassportNo());
					newEmpBasicDetails.getEmpPassportDetails()
							.setPassportAddress(empBasicDetails.getEmpPassportDetails().getPassportAddress());
					newEmpBasicDetails.getEmpPassportDetails()
							.setPassportCountry(empBasicDetails.getEmpPassportDetails().getPassportCountry());
					newEmpBasicDetails.getEmpPassportDetails()
							.setPassportDateOfIssue(empBasicDetails.getEmpPassportDetails().getPassportDateOfIssue());
					newEmpBasicDetails.getEmpPassportDetails()
							.setPassportDateOfExpire(empBasicDetails.getEmpPassportDetails().getPassportDateOfExpire());
					newEmpBasicDetails.getEmpPassportDetails().setLastModifiedDate(checkOutDaoImpl.getDateTime());
					newEmpBasicDetails.getEmpPassportDetails().setLastModifiedBy(loginId);

					newEmpBasicDetails.getEmpEmergContactDetails()
							.setEmergContactName1(empBasicDetails.getEmpEmergContactDetails().getEmergContactName1());
					newEmpBasicDetails.getEmpEmergContactDetails()
							.setEmergContactNo1(empBasicDetails.getEmpEmergContactDetails().getEmergContactNo1());
					newEmpBasicDetails.getEmpEmergContactDetails()
							.setEmergContactName2(empBasicDetails.getEmpEmergContactDetails().getEmergContactName2());
					newEmpBasicDetails.getEmpEmergContactDetails()
							.setEmergContactNo2(empBasicDetails.getEmpEmergContactDetails().getEmergContactNo2());
					newEmpBasicDetails.getEmpEmergContactDetails().setLastModifiedDate(checkOutDaoImpl.getDateTime());
					newEmpBasicDetails.getEmpEmergContactDetails().setLastModifiedBy(loginId);
										
					/* QR Code Generate */
					createQRCode(empBasicDetails);
					System.out.println("QRCode Path ::: " + QR_CODE_IMAGE_PATH);
					// QR Save in DB from local
					File fnew = new File(QR_CODE_IMAGE_PATH);
					
					BufferedImage originalImage = ImageIO.read(fnew.getAbsoluteFile());
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(originalImage, ProdConstant.PNGSMALL, baos);
					byte[] imageInByte = baos.toByteArray();

					newEmpBasicDetails.setQrCode(imageInByte);					
					empBasicRepository.save(newEmpBasicDetails);
					
					if (fnew.delete()) {
			            System.out.println("File deleted successfully");
			        }
			        else {
			            System.out.println("Failed to delete the file");
			        }
				} else {
					return empBasicDetails;
				}
			}
		} catch (Exception e) {
			LOG.error("Error occur while save record" + ExceptionUtils.getStackTrace(e));
		}
		return empBasicDetails;
	}
	
	/* Create/Update QR Code */
	private String createQRCode(EmpBasicDetails empBasicDetails) {
		String empDetails = null;
		try {
			empDetails = ProdConstant.NAME + empBasicDetails.getFullName() + '\n' + ProdConstant.BLOODGROUP
					+ empBasicDetails.getEmpPersonalDetails().getBloodGroup() + '\n' + ProdConstant.PRESENTADDRESS
					+ empBasicDetails.getEmpAddressDetails().getPresentAddress() + ","
					+ empBasicDetails.getEmpAddressDetails().getPresentCity() + ","
					+ empBasicDetails.getEmpAddressDetails().getPresentState() + ","
					+ empBasicDetails.getEmpAddressDetails().getPresentPinCode() + '\n' + ProdConstant.PERMANENTADDRESS
					+ empBasicDetails.getEmpAddressDetails().getPermanentAddress() + ","
					+ empBasicDetails.getEmpAddressDetails().getPermanentCity() + ","
					+ empBasicDetails.getEmpAddressDetails().getPermanentState() + ","
					+ empBasicDetails.getEmpAddressDetails().getPermanentPinCode() + '\n' + ProdConstant.EMERGENCYCONTACTNO
					+ empBasicDetails.getEmpEmergContactDetails().getEmergContactNo1();
			
			final String QR_CODE_IMAGE_PATH = empBasicDetails.getEmpId() + ProdConstant.PNGEXTENSION;
			//	Generate and Save QR Code Image in static/image folder
			MethodUtils.generateQRCodeImage(empDetails, 250, 250, QR_CODE_IMAGE_PATH);
		} catch (Exception e) {
			LOG.error("Error occur while create/update QR code" + ExceptionUtils.getStackTrace(e));
		}
		return empDetails;
	}

	@Override
	public Optional<EmpBasicDetails> findEmpByEmail(String coEmailId) throws Exception {
		return empBasicRepository.findEmpByEmail(coEmailId);
	}

	@Override
	public boolean userExists(String coEmailId) throws Exception {
		return findEmpByEmail(coEmailId).isPresent();
	}

	public Optional<EmpBasicDetails> getImageById(String empId) {
		return empBasicRepository.findById(empId);
	}	
	/* Get Current Active Employee Data by empId */
	@Override
	public EmpBasicDetails getCurrentUser(String empId) throws Exception {
		EmpBasicDetails basicDetails = null;
		try {
			return this.empBasicRepository.getCurrentUser(empId,ProdConstant.TRUE);
		} catch (Exception e) {
			LOG.error("Error occur while display employee data" + ExceptionUtils.getStackTrace(e));
		}
		return basicDetails;
	}

	/* Get Employee Image by empId */
	@Override
	public EmpBasicDetails getDataByEmpId(String empId) throws Exception {
		EmpBasicDetails basicDetails = null;
		try {
			basicDetails =  this.empBasicRepository.getDataByEmpId(empId);
		} catch (Exception e) {
			LOG.error("Error occur while display employee image" + ExceptionUtils.getStackTrace(e));
		}
		return basicDetails;
	}

	@Override
	public List<EmpWorkDetails> getEmpWithManger(String userId, String tenantId) throws Exception {
		List<EmpWorkDetails> empWorkDetails = empWorkRepository.getEmpWithManger(userId,ProdConstant.TRUE, tenantId);
		try {
			if (Objects.nonNull(empWorkDetails)) {
				return empWorkDetails;
			}
		} catch (Exception e) {
			LOG.error("Error occur while display records to manager" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<String> getEmpIdWithIsActive() throws Exception {
		List<String> listOfEmp = empBasicRepository.getEmpIdWithIsActive(ProdConstant.TRUE);
		try {
			if (Objects.nonNull(listOfEmp)) {
				return listOfEmp;
			}
		} catch (Exception e) {
			LOG.error("Error occur while get list of active employees " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public Long getAllEmployees() throws Exception {
		try {
			return empBasicRepository.getAllEmployees(ProdConstant.TRUE);
		} catch (Exception e) {
			LOG.error("Error occuring while get all employee in current month " + ExceptionUtils.getStackTrace(e));
		}
		return (long) 0;
	}
	
	@Override
	public List<String> getUniqueTenantId() throws Exception {
		List<String> listOfUniqueTenantId = empBasicRepository.getUniqueTenantId(ProdConstant.TRUE);
		try {
			if (Objects.nonNull(listOfUniqueTenantId)) {
				return listOfUniqueTenantId;
			} else {
				System.out.println("list Of Unique Tenant Id is null");
			} 
		} catch (Exception e) {
			LOG.error("Error occuring while get Unique Tenant Id  " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<EmpBasicDetails> listOfCurrentUser(String empId) throws Exception {
		List<EmpBasicDetails> empBasicDetails = empBasicRepository.listOfCurrentUser(empId,ProdConstant.TRUE);
		try {
			if (!empBasicDetails.isEmpty()) {
				return empBasicDetails;
			}
		} catch (Exception e) {
			LOG.error("Error occur while display list of current user" + ExceptionUtils.getStackTrace(e));
		}
		return empBasicDetails;
	}

	@Override
	public void uploadImage(byte[] imageData, String empId) throws Exception {
		try {
			empBasicRepository.uploadImage(imageData, empId);
		} catch (Exception e) {
			LOG.error("Error occur while upload image " + ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public List<String> getUniqueDepId() throws Exception {
			List<String> empsList = empBasicRepository.getUniqueDepId(ProdConstant.TRUE);
			try {
				if (Objects.nonNull(empsList)) {
					return empsList;
				}
		} catch (Exception e) {
			LOG.error("Error occur while get unique depId " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<String> getAllEmpId(String empId) throws Exception {
		List<String> empsList = empWorkRepository.getAllEmpId(empId);
		try {
			if (Objects.nonNull(empsList)) {
				return empsList;
			}
		} catch (Exception e) {
			LOG.error("Error occur while get All EmpId " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<EmpWorkDetails> getAllEmpJoiningDate(List<String> empIds) throws Exception {
		List<EmpWorkDetails> empsList = empWorkRepository.getAllEmpJoiningDate(empIds);
		try {
			if (Objects.nonNull(empsList)) {
				return empsList;
			}
		} catch (Exception e) {
			LOG.error("Error occur while get All Emp JoiningDate " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public EmpBasicDetails getKRAWithDepIdAndEmpId(String depId, String reportingManager) throws Exception {
		EmpBasicDetails empBasicDetails = empBasicRepository.getKRAWithDepIdAndEmpId(depId, reportingManager,ProdConstant.TRUE);
		try {
			if (Objects.nonNull(empBasicDetails)) {
				return empBasicDetails;
			}
		} catch (Exception e) {
			LOG.error("Error occur while get KRA With DepId And EmpId " + ExceptionUtils.getStackTrace(e));
		}
		return new EmpBasicDetails();
	}

	@Override
	public Optional<EmpBasicDetails> findById(String empId) throws Exception {
		Optional<EmpBasicDetails> optional = empBasicRepository.findById(empId);
		try {
			if (optional.isPresent()) {
				return optional;
			}
		} catch (Exception e) {
			LOG.error("Error occur while get find by id " + ExceptionUtils.getStackTrace(e));
		}
		return Optional.empty();
	}
	
	@Override
	public List<EmpPersonalDetails> getEmpBirthdayToday(String tenantId) throws Exception {
		List<EmpPersonalDetails> empPersonalDetails = empPersonalRepository.getEmpBirthdayToday(tenantId);
		try {
			if (Objects.nonNull(empPersonalDetails)) {
				return empPersonalDetails;
			}
		} catch (Exception e) {
			LOG.error("Error occur while get All Emp birthday's" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}
	
	@Override
	public List<EmpBasicDetails> getEmpWithMangerIdOrReportingManager(String empId, String depId, List<String> empIds) {
		try {
			return empBasicRepository.getEmpWithMangerIdOrReportingManager(empId, depId, empIds);
		} catch (Exception e) {
			LOG.error("Error occuring while get Emp With MangerId Or ReportingManager ... " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public String[] getAllEmpMailIdsForBirthday() throws Exception {
		String[] empBasicDetails = null;
		try {
			empBasicDetails = empBasicRepository.getAllEmpMailIdsForBirthDay(ProdConstant.TRUE);
		} catch (Exception e) {
			LOG.error("Error occuring while get all employees email ids for birthday ... "
					+ ExceptionUtils.getStackTrace(e));
		}
		return empBasicDetails;
	}

	@Override
	public String[] getBirthdayMailId(List<String> empId) throws Exception {
		String[] empBasicDetails = null;
		try {
			empBasicDetails = empBasicRepository.getBirthdayMailId(empId);
		} catch (Exception e) {
			LOG.error(
					"Error occuring while get employee email ids for birthday ... " + ExceptionUtils.getStackTrace(e));
		}
		return empBasicDetails;
	}

	@Override
	public String[] getMailIdForSendMailByAdmin(String departmentName, String grade) throws Exception {
		String[] empBasicDetails = null;
		try {
			if(ProdConstant.ALL.equalsIgnoreCase(departmentName) && ProdConstant.ALL.equalsIgnoreCase(grade)){
				System.out.println("First condition Both condition true");
				empBasicDetails = empBasicRepository.findAll(ProdConstant.TRUE); 
			}else if((ProdConstant.ALL.equalsIgnoreCase(grade)) && !(ProdConstant.ALL.equalsIgnoreCase(departmentName))){
				System.out.println("Second condition GRADE ALL");
				empBasicDetails = empBasicRepository.findAll1(departmentName);
			}else if((ProdConstant.ALL.equalsIgnoreCase(departmentName)) && !(ProdConstant.ALL.equalsIgnoreCase(grade))){
				System.out.println("Third condition DEPARTMENT ALL");
				empBasicDetails = empBasicRepository.findAll2(grade);
			}else {
				System.out.println("ELSE..........");
			empBasicDetails = empBasicRepository.getMailIdForSendMailByAdmin(departmentName, grade);
			}
		}catch (Exception e){
			LOG.error("Error occuring while get employee email ids for send mail by admin ... "
					+ ExceptionUtils.getStackTrace(e));
		}
		return empBasicDetails;
	}

	@Override
	public String getTenantIdWithEmpId(String id) throws Exception {
		String empBasicDetails = empBasicRepository.getTenantIdWithEmpId(id);
		try {
			if(Objects.nonNull(empBasicDetails)) {
				return empBasicDetails;
			}
		} catch (Exception e) {
			LOG.error("Error occuring while get tenantId according to employee id " + ExceptionUtils.getStackTrace(e));
		}
		return empBasicDetails;
	}

	@Override
	public List<EmpBasicDetails> getAllEmpDataAccordingToMailIds(String[] getMailIds) throws Exception {
		List<EmpBasicDetails> empBasicDetails = empBasicRepository.getAllEmpDataAccordingToMailIds(getMailIds);
		try {
			if(Objects.nonNull(empBasicDetails)) {
				return empBasicDetails;
			}
		} catch (Exception e) {
			LOG.error("Error occuring while get employee data according to mail ids  " + ExceptionUtils.getStackTrace(e));
		}
		return empBasicDetails;	}

	@Override
	public EmpBasicDetails getEmpData(String emailId) throws Exception {
		EmpBasicDetails empBasicDetails = empBasicRepository.getEmpData(emailId);
		try {
			if(Objects.nonNull(empBasicDetails)) {
				return empBasicDetails;
			}
		} catch (Exception e) {
			LOG.error("Error occuring while get employee data according to mail id  " + ExceptionUtils.getStackTrace(e));
		}
		return empBasicDetails;
	}

	@Override
	public List<EmpPersonalDetails> getEmpBirthdayTodayForMail() throws Exception {
		List<EmpPersonalDetails> empBasicDetails = empPersonalRepository.getEmpBirthdayTodayForMail();
		try {
			if(Objects.nonNull(empBasicDetails)) {
				return empBasicDetails;
			}
		} catch (Exception e) {
			LOG.error("Error occuring while get employee birthday today for mail " + ExceptionUtils.getStackTrace(e));
		}
		return empBasicDetails;
	}
	@Override
	public List<EmpBasicDetails> getFindAllEmpRegList() throws Exception {
		String empId = null;
		String tenantId = null;
		UserMst user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			empId = authentication.getName();
			if(Objects.nonNull(empId)) {
				user = userService.getUserById(empId);
				tenantId = user.getTenantId();
				if(Objects.nonNull(tenantId)) {
		List<EmpBasicDetails> empBasicDetails = empBasicRepository.getRecordList(ProdConstant.TRUE, tenantId);
			if (!empBasicDetails.isEmpty()) {
				return empBasicDetails;
			}
		}
			}
		}catch (Exception e) {
			LOG.error("Error occur while display findAll employee registartion list" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}
	@Override
	public List<String> getUserIdWhoseBirthdayToday() throws Exception {
		List<String> empBasicDetails = empPersonalRepository.getUserIdWhoseBirthdayToday();
		try {
			if(Objects.nonNull(empBasicDetails)) {
				return empBasicDetails;
			}
		} catch (Exception e) {
			LOG.error("Error occuring while get employee birthday today for mail   " + ExceptionUtils.getStackTrace(e));
		}
		return empBasicDetails;
	}
	
}