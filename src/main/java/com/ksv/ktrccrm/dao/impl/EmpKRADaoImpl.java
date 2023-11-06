package com.ksv.ktrccrm.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.EmpKRADao;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.EmpKRA;
import com.ksv.ktrccrm.db1.entities.EmpWorkDetails;
import com.ksv.ktrccrm.db1.entities.KRA;
import com.ksv.ktrccrm.db1.entities.KRADescription;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.db1.repository.EmpKRARepository;
import com.ksv.ktrccrm.db1.repository.EmpWorkRepository;
import com.ksv.ktrccrm.db1.repository.KRADescriptionRepository;
import com.ksv.ktrccrm.db1.repository.KRARepository;
import com.ksv.ktrccrm.service.EmpRegistartionService;
import com.ksv.ktrccrm.service.UserService;

@Repository
@Transactional
public class EmpKRADaoImpl implements EmpKRADao {
	private static final Logger LOGGER = LogManager.getLogger(EmpKRADaoImpl.class);

	@Autowired
	private EmpKRARepository empKRARepository;
	@Autowired
	private EmpRegistartionService empRegistartionService;
	@Autowired
	private EmpWorkRepository empWorkRepository;
	@Autowired
	private KRARepository kraRepository;
	@Autowired
	private KRADescriptionRepository kraDescRepository;
	@Autowired
	private UserService userService;
	
	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;

	@Override
	public List<EmpWorkDetails> getEmpWithManger(String empId) throws Exception {
		String loginId = null;
		String tenantId = null;
		UserMst user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			loginId = authentication.getName();
			if(Objects.nonNull(empId)) {
				user = userService.getUserById(loginId);
				tenantId = user.getTenantId();
				if(Objects.nonNull(tenantId)) {
			List<EmpWorkDetails> empKRAs = empWorkRepository.getEmpWithManger(empId,ProdConstant.TRUE,tenantId);
			if (Objects.nonNull(empKRAs)) {
				return empKRAs;
			}
				}
			}else {
				System.out.println("emp kra object is null ::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display list if emp with managerId " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<EmpKRA> getCurrentEmpAppraisal(String empId) throws Exception {
		List<EmpKRA> empKRAs = empKRARepository.getCurrentEmpAppraisal(empId,ProdConstant.TRUE);
		try {
			if (Objects.nonNull(empKRAs)) {
				return empKRAs;
			} else {
				System.out.println("emp kra object is null ::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display appraisal list of current emp " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public Optional<EmpKRA> findByempId(String empId) throws Exception {
		Optional<EmpKRA> optional = empKRARepository.findByempId(empId);
		try {
			if (optional.isPresent()) {
				return optional;
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while get emp id ... " + ExceptionUtils.getStackTrace(e));
		}
		return Optional.empty();
	}

	@Override
	public EmpKRA saveTagKra(EmpKRA empKRA) throws Exception {
		try {
			EmpBasicDetails basicDetails = empRegistartionService.getCurrentUser(empKRA.getEmpId());
			KRA kra = kraRepository.getKRA(empKRA.getKraI());

			Optional<EmpKRA> optional = empKRARepository.findByempId(empKRA.getEmpId());

			if (optional.isPresent()) {
				System.out.println("If Save2 Tag Kra :::::: ");
				EmpKRA newEmpKra = optional.get();
				if (newEmpKra.getKraI() == null) {
					newEmpKra.setKraI(empKRA.getKraI());
					newEmpKra.setWeightageI(empKRA.getWeightageI());
					newEmpKra.setDescriptionI(kra.getKraDescription().getDescription());
					empKRARepository.save(newEmpKra);
				} else if (newEmpKra.getKraII() == null) {
					newEmpKra.setKraII(empKRA.getKraI());
					newEmpKra.setWeightageII(empKRA.getWeightageI());
					newEmpKra.setDescriptionII(kra.getKraDescription().getDescription());
					empKRARepository.save(newEmpKra);
				} else if (newEmpKra.getKraIII() == null) {
					newEmpKra.setKraIII(empKRA.getKraI());
					newEmpKra.setWeightageIII(empKRA.getWeightageI());
					newEmpKra.setDescriptionIII(kra.getKraDescription().getDescription());
					empKRARepository.save(newEmpKra);
				} else if (newEmpKra.getKraIV() == null) {
					newEmpKra.setKraIV(empKRA.getKraI());
					newEmpKra.setWeightageIV(empKRA.getWeightageI());
					newEmpKra.setDescriptionIV(kra.getKraDescription().getDescription());
					empKRARepository.save(newEmpKra);
				} else if (newEmpKra.getKraV() == null) {
					newEmpKra.setKraV(empKRA.getKraI());
					newEmpKra.setWeightageV(empKRA.getWeightageI());
					newEmpKra.setDescriptionV(kra.getKraDescription().getDescription());
					empKRARepository.save(newEmpKra);
				} else if (newEmpKra.getKraVI() == null) {
					newEmpKra.setKraVI(empKRA.getKraI());
					newEmpKra.setWeightageVI(empKRA.getWeightageI());
					newEmpKra.setDescriptionVI(kra.getKraDescription().getDescription());
					empKRARepository.save(newEmpKra);
				} else if (newEmpKra.getKraVII() == null) {
					newEmpKra.setKraVII(empKRA.getKraI());
					newEmpKra.setWeightageVII(empKRA.getWeightageI());
					newEmpKra.setDescriptionVII(kra.getKraDescription().getDescription());
					empKRARepository.save(newEmpKra);
				} else if (newEmpKra.getKraVIII() == null) {
					newEmpKra.setKraVIII(empKRA.getKraI());
					newEmpKra.setWeightageVIII(empKRA.getWeightageI());
					newEmpKra.setDescriptionVIII(kra.getKraDescription().getDescription());
					empKRARepository.save(newEmpKra);
				} else if (newEmpKra.getKraIX() == null) {
					newEmpKra.setKraIX(empKRA.getKraI());
					newEmpKra.setWeightageIX(empKRA.getWeightageI());
					newEmpKra.setDescriptionIX(kra.getKraDescription().getDescription());
					empKRARepository.save(newEmpKra);
				} else if (newEmpKra.getKraX() == null) {
					newEmpKra.setKraX(empKRA.getKraI());
					newEmpKra.setWeightageX(empKRA.getWeightageI());
					newEmpKra.setDescriptionX(kra.getKraDescription().getDescription());
					empKRARepository.save(newEmpKra);
				}
			} else {
				System.out.println("If Save Tag Kra :::::: ");
				empKRA.setDescriptionI(kra.getKraDescription().getDescription());
				empKRA.setDepId(basicDetails.getDepartName());
				empKRA.setGrade(basicDetails.getGrade());
				empKRA.setManagerId(basicDetails.getEmpWorkDetails().getReportingManager());
				empKRA.setSelfAppStatus(ProdConstant.PENDING);
				empKRA.setIsActive(ProdConstant.TRUE);
				empKRA.setEmpBasicDetails(basicDetails);
				empKRA.setDate(checkOutDaoImpl.getDateTime());
				empKRARepository.save(empKRA);
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while save tag kra ... " + ExceptionUtils.getStackTrace(e));
		}
		return empKRA;
	}

	@Override
	public EmpKRA updateTagKra(EmpKRA empKRA) throws Exception {
		try {
			Optional<EmpKRA> optional = empKRARepository.findByempId(empKRA.getEmpId());

			if (optional.isPresent()) {
				EmpKRA newEmpKra = optional.get();
				if (newEmpKra.getKraI() != null) {
					KRA kra = kraRepository.getKRA(empKRA.getKraI());
					newEmpKra.setKraI(empKRA.getKraI());
					newEmpKra.setWeightageI(empKRA.getWeightageI());
					newEmpKra.setDescriptionI(kra.getKraDescription().getDescription());
				}if (newEmpKra.getKraII() != null) {
					KRA kra = kraRepository.getKRA(empKRA.getKraII());
					newEmpKra.setKraII(empKRA.getKraII());
					newEmpKra.setWeightageII(empKRA.getWeightageII());
					newEmpKra.setDescriptionII(kra.getKraDescription().getDescription());
				} if (newEmpKra.getKraIII() != null) {
					KRA kra = kraRepository.getKRA(empKRA.getKraIII());
					newEmpKra.setKraIII(empKRA.getKraIII());
					newEmpKra.setWeightageIII(empKRA.getWeightageIII());
					newEmpKra.setDescriptionIII(kra.getKraDescription().getDescription());
				}if (newEmpKra.getKraIV() != null) {
					KRA kra = kraRepository.getKRA(empKRA.getKraIV());
					newEmpKra.setKraIV(empKRA.getKraIV());
					newEmpKra.setWeightageIV(empKRA.getWeightageIV());
					newEmpKra.setDescriptionIV(kra.getKraDescription().getDescription());
				} if (newEmpKra.getKraV() != null) {
					KRA kra = kraRepository.getKRA(empKRA.getKraV());
					newEmpKra.setKraV(empKRA.getKraV());
					newEmpKra.setWeightageV(empKRA.getWeightageV());
					newEmpKra.setDescriptionV(kra.getKraDescription().getDescription());
				} if (newEmpKra.getKraVI() != null) {
					KRA kra = kraRepository.getKRA(empKRA.getKraVI());
					newEmpKra.setKraVI(empKRA.getKraVI());
					newEmpKra.setWeightageVI(empKRA.getWeightageVI());
					newEmpKra.setDescriptionVI(kra.getKraDescription().getDescription());
				} if (newEmpKra.getKraVII() != null) {
					KRA kra = kraRepository.getKRA(empKRA.getKraVII());
					newEmpKra.setKraVII(empKRA.getKraVII());
					newEmpKra.setWeightageVII(empKRA.getWeightageVII());
					newEmpKra.setDescriptionVII(kra.getKraDescription().getDescription());
				}if (newEmpKra.getKraVIII() != null) {
					KRA kra = kraRepository.getKRA(empKRA.getKraVIII());
					newEmpKra.setKraVIII(empKRA.getKraVIII());
					newEmpKra.setWeightageVIII(empKRA.getWeightageVIII());
					newEmpKra.setDescriptionVIII(kra.getKraDescription().getDescription());
				}if (newEmpKra.getKraIX() != null) {
					KRA kra = kraRepository.getKRA(empKRA.getKraIX());
					newEmpKra.setKraIX(empKRA.getKraIX());
					newEmpKra.setWeightageIX(empKRA.getWeightageIX());
					newEmpKra.setDescriptionIX(kra.getKraDescription().getDescription());
				}if (newEmpKra.getKraX() != null) {
					KRA kra = kraRepository.getKRA(empKRA.getKraX());
					newEmpKra.setKraX(empKRA.getKraX());
					newEmpKra.setWeightageX(empKRA.getWeightageX());
					newEmpKra.setDescriptionX(kra.getKraDescription().getDescription());
				}
				empKRARepository.save(newEmpKra);
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while update tag kra ... " + ExceptionUtils.getStackTrace(e));
		}
		return empKRA;
	}

	@Override
	public EmpKRA getById(Long id) throws Exception {
		EmpKRA empKRA = empKRARepository.getById(id);
		try {
			if (Objects.nonNull(empKRA)) {
				return empKRA;
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while get id ... " + ExceptionUtils.getStackTrace(e));
		}
		return new EmpKRA();
	}

	@Override
	public EmpKRA getKRAWithDepIdAndEmpId(String depId, String empId) throws Exception {
		EmpKRA empKRA = empKRARepository.getKRAWithDepIdAndEmpId(depId, empId, ProdConstant.TRUE);
		try {
			if (Objects.nonNull(empKRA)) {
				return empKRA;
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while get manager id & emp id ... " + ExceptionUtils.getStackTrace(e));
		}
		return new EmpKRA();
	}

	@Override
	public EmpKRA getManagerIdWithMangerIdWithDepId(String reportingManager, String depId, String empId)
			throws Exception {
		EmpKRA empKRA = empKRARepository.getManagerIdWithMangerIdWithDepId(reportingManager, depId, empId);
		try {
			if (Objects.nonNull(empKRA)) {
				return empKRA;
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while get reportingManager, dep id & emp id ... " + ExceptionUtils.getStackTrace(e));
		}
		return new EmpKRA();
	}

	@Override
	public EmpKRA saveDraftSelfAppraisal(EmpKRA empKRA) throws Exception {
		String empId = null;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();

			System.out.println("EMPLOYEE ID ----------- " + empKRA.getEmpId());

			Optional<EmpKRA> optional = empKRARepository.findByempId(empKRA.getEmpId());
			if (optional.isPresent()) {
				System.out.println("IF :::::::::::");
				EmpKRA newEmpKRA = optional.get();
				newEmpKRA.setKraId(empKRA.getKraId());
				newEmpKRA.setSelfRatingI(empKRA.getSelfRatingI());
				newEmpKRA.setSelfRatingII(empKRA.getSelfRatingII());
				newEmpKRA.setSelfRatingIII(empKRA.getSelfRatingIII());
				newEmpKRA.setSelfRatingIV(empKRA.getSelfRatingIV());
				newEmpKRA.setSelfRatingV(empKRA.getSelfRatingV());
				newEmpKRA.setSelfRatingVI(empKRA.getSelfRatingVI());
				newEmpKRA.setSelfRatingVII(empKRA.getSelfRatingVII());
				newEmpKRA.setSelfRatingVIII(empKRA.getSelfRatingVIII());
				newEmpKRA.setSelfRatingIX(empKRA.getSelfRatingIX());
				newEmpKRA.setSelfRatingX(empKRA.getSelfRatingX());
				newEmpKRA.setTotalSelf(empKRA.getTotalSelf());
				newEmpKRA.setSelfAppStatus(ProdConstant.DRAFT);
				newEmpKRA.setLevelIAppStatus(ProdConstant.TOBEPROVIDED);
				newEmpKRA.setDate(checkOutDaoImpl.getDateTime());
				newEmpKRA.setLastModifiedBy(empId);
				newEmpKRA.setLastModifiedDate(checkOutDaoImpl.getDateTime());
				newEmpKRA.setSelfRating(empKRA.getSelfRating());

				empKRARepository.save(newEmpKRA);
			} else {
				System.out.println("ELSE save draft :::::::::::");
				System.out.println("EmpKRA object is null save draft");
				EmpKRA newEmpKRA = new EmpKRA();
				newEmpKRA.setSelfRatingI(empKRA.getSelfRatingI());
				newEmpKRA.setSelfRatingII(empKRA.getSelfRatingII());
				newEmpKRA.setSelfRatingIII(empKRA.getSelfRatingIII());
				newEmpKRA.setSelfRatingIV(empKRA.getSelfRatingIV());
				newEmpKRA.setSelfRatingV(empKRA.getSelfRatingV());
				newEmpKRA.setSelfRatingVI(empKRA.getSelfRatingVI());
				newEmpKRA.setSelfRatingVII(empKRA.getSelfRatingVII());
				newEmpKRA.setSelfRatingVIII(empKRA.getSelfRatingVIII());
				newEmpKRA.setSelfRatingIX(empKRA.getSelfRatingIX());
				newEmpKRA.setSelfRatingX(empKRA.getSelfRatingX());
				newEmpKRA.setTotalSelf(empKRA.getTotalSelf());
				newEmpKRA.setSelfAppStatus(ProdConstant.DRAFT);
				newEmpKRA.setLevelIAppStatus(ProdConstant.TOBEPROVIDED);
				newEmpKRA.setDate(checkOutDaoImpl.getDateTime());
				newEmpKRA.setCreatedBy(empId);
				newEmpKRA.setCreatedDate(checkOutDaoImpl.getDateTime());
				newEmpKRA.setSelfRating(empKRA.getSelfRating());

				empKRARepository.save(newEmpKRA);
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while save draft self appraisal ... " + ExceptionUtils.getStackTrace(e));
		}
		return empKRA;
	}

	@Override
	public EmpKRA saveSelfAppraisal(EmpKRA empKRA) throws Exception {
		String empId = null;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();

			Optional<EmpKRA> optional = empKRARepository.findByempId(empKRA.getEmpId());
			if (optional.isPresent()) {
				System.out.println("IF :::::::::::");
				EmpKRA newEmpKRA = optional.get();
				newEmpKRA.setKraId(empKRA.getKraId());
				newEmpKRA.setSelfRatingI(empKRA.getSelfRatingI());
				newEmpKRA.setSelfRatingII(empKRA.getSelfRatingII());
				newEmpKRA.setSelfRatingIII(empKRA.getSelfRatingIII());
				newEmpKRA.setSelfRatingIV(empKRA.getSelfRatingIV());
				newEmpKRA.setSelfRatingV(empKRA.getSelfRatingV());
				newEmpKRA.setSelfRatingVI(empKRA.getSelfRatingVI());
				newEmpKRA.setSelfRatingVII(empKRA.getSelfRatingVII());
				newEmpKRA.setSelfRatingVIII(empKRA.getSelfRatingVIII());
				newEmpKRA.setSelfRatingIX(empKRA.getSelfRatingIX());
				newEmpKRA.setSelfRatingX(empKRA.getSelfRatingX());
				newEmpKRA.setTotalSelf(empKRA.getTotalSelf());
				newEmpKRA.setSelfAppStatus(ProdConstant.COMPLETED);
				newEmpKRA.setLevelIAppStatus(ProdConstant.TOBEPROVIDED);
				newEmpKRA.setLastModifiedDate(checkOutDaoImpl.getDateTime());
				newEmpKRA.setDate(checkOutDaoImpl.getDateTime());
				newEmpKRA.setLastModifiedBy(empId);
				newEmpKRA.setSelfRating(empKRA.getSelfRating());
				newEmpKRA.setSubmittedOn(checkOutDaoImpl.getDateTime());

				empKRARepository.save(newEmpKRA);
			} else {
				System.out.println("ELSE save :::::::::::");
				System.out.println("EmpKRA object is null save");
				
				EmpKRA newEmpKRA = new EmpKRA();
				newEmpKRA.setSelfRatingI(empKRA.getSelfRatingI());
				newEmpKRA.setSelfRatingII(empKRA.getSelfRatingII());
				newEmpKRA.setSelfRatingIII(empKRA.getSelfRatingIII());
				newEmpKRA.setSelfRatingIV(empKRA.getSelfRatingIV());
				newEmpKRA.setSelfRatingV(empKRA.getSelfRatingV());
				newEmpKRA.setSelfRatingVI(empKRA.getSelfRatingVI());
				newEmpKRA.setSelfRatingVII(empKRA.getSelfRatingVII());
				newEmpKRA.setSelfRatingVIII(empKRA.getSelfRatingVIII());
				newEmpKRA.setSelfRatingIX(empKRA.getSelfRatingIX());
				newEmpKRA.setSelfRatingX(empKRA.getSelfRatingX());
				newEmpKRA.setTotalSelf(empKRA.getTotalSelf());
				newEmpKRA.setSelfAppStatus(ProdConstant.COMPLETED);
				newEmpKRA.setLevelIAppStatus(ProdConstant.TOBEPROVIDED);
				newEmpKRA.setCreatedDate(checkOutDaoImpl.getDateTime());
				newEmpKRA.setDate(checkOutDaoImpl.getDateTime());
				newEmpKRA.setCreatedBy(empId);
				newEmpKRA.setSelfRating(empKRA.getSelfRating());
				newEmpKRA.setSubmittedOn(checkOutDaoImpl.getDateTime());

				empKRARepository.save(newEmpKRA);
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while save & update appraisal ... " + ExceptionUtils.getStackTrace(e));
		}
		return empKRA;
	}

	@Override
	public EmpKRA saveDraftRatingByReportingManager(EmpKRA empKRA) throws Exception {
		try {
			Optional<EmpKRA> optional = empKRARepository.findByempId(empKRA.getEmpId());
			if (optional.isPresent()) {
				System.out.println("IF ::::::::::: " + empKRA.getEmpId());
				EmpKRA newEmpKRA = optional.get();
				newEmpKRA.setLevel1ApproverRatingI(empKRA.getLevel1ApproverRatingI());
				newEmpKRA.setLevel1ApproverRatingII(empKRA.getLevel1ApproverRatingII());
				newEmpKRA.setLevel1ApproverRatingIII(empKRA.getLevel1ApproverRatingIII());
				newEmpKRA.setLevel1ApproverRatingIV(empKRA.getLevel1ApproverRatingIV());
				newEmpKRA.setLevel1ApproverRatingV(empKRA.getLevel1ApproverRatingV());
				newEmpKRA.setLevel1ApproverRatingVI(empKRA.getLevel1ApproverRatingVI());
				newEmpKRA.setLevel1ApproverRatingVII(empKRA.getLevel1ApproverRatingVII());
				newEmpKRA.setLevel1ApproverRatingVIII(empKRA.getLevel1ApproverRatingVIII());
				newEmpKRA.setLevel1ApproverRatingIX(empKRA.getLevel1ApproverRatingIX());
				newEmpKRA.setLevel1ApproverRatingX(empKRA.getLevel1ApproverRatingX());
				newEmpKRA.setTotalLevelI(empKRA.getTotalLevelI());
				newEmpKRA.setLevelIAppStatus(ProdConstant.DRAFT);
				newEmpKRA.setLevelIIAppStatus(ProdConstant.TOBEPROVIDED);
				newEmpKRA.setLevelIRating(empKRA.getLevelIRating());
				newEmpKRA.setTrainingReco(empKRA.getTrainingReco());
				newEmpKRA.setTrainingRecoComment(empKRA.getTrainingRecoComment());
				empKRARepository.save(newEmpKRA);
			} else {
				System.out.println("ELSE :::::::::::");
				System.out.println("EmpKRA object is null");
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while save draft rating by reporting manager ... " + ExceptionUtils.getStackTrace(e));
		}
		return empKRA;
	}

	@Override
	public EmpKRA saveRatingByReportingManager(EmpKRA empKRA) throws Exception {
		try {
			Optional<EmpKRA> optional = empKRARepository.findByempId(empKRA.getEmpId());
			if (optional.isPresent()) {
				System.out.println("IF ::::::::::: " + empKRA.getEmpId());
				EmpKRA newEmpKRA = optional.get();
				newEmpKRA.setLevel1ApproverRatingI(empKRA.getLevel1ApproverRatingI());
				newEmpKRA.setLevel1ApproverRatingII(empKRA.getLevel1ApproverRatingII());
				newEmpKRA.setLevel1ApproverRatingIII(empKRA.getLevel1ApproverRatingIII());
				newEmpKRA.setLevel1ApproverRatingIV(empKRA.getLevel1ApproverRatingIV());
				newEmpKRA.setLevel1ApproverRatingV(empKRA.getLevel1ApproverRatingV());
				newEmpKRA.setLevel1ApproverRatingVI(empKRA.getLevel1ApproverRatingVI());
				newEmpKRA.setLevel1ApproverRatingVII(empKRA.getLevel1ApproverRatingVII());
				newEmpKRA.setLevel1ApproverRatingVIII(empKRA.getLevel1ApproverRatingVIII());
				newEmpKRA.setLevel1ApproverRatingIX(empKRA.getLevel1ApproverRatingIX());
				newEmpKRA.setLevel1ApproverRatingX(empKRA.getLevel1ApproverRatingX());
				newEmpKRA.setTotalLevelI(empKRA.getTotalLevelI());
				newEmpKRA.setLevelIAppStatus(ProdConstant.COMPLETED);
				newEmpKRA.setLevelIIAppStatus(ProdConstant.TOBEPROVIDED);
				newEmpKRA.setLevelIRating(empKRA.getLevelIRating());
				newEmpKRA.setTrainingReco(empKRA.getTrainingReco());
				newEmpKRA.setTrainingRecoComment(empKRA.getTrainingRecoComment());
				empKRARepository.save(newEmpKRA);
			} else {
				System.out.println("ELSE :::::::::::");
				System.out.println("EmpKRA object is null");
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while save rating by reporting manager ... " + ExceptionUtils.getStackTrace(e));
		}
		return empKRA;
	}

	@Override
	public EmpKRA saveDraftRatingByReportingManager2(EmpKRA empKRA) throws Exception {
		try {
			Optional<EmpKRA> optional = empKRARepository.findByempId(empKRA.getEmpId());
			if (optional.isPresent()) {
				System.out.println("IF ::::::::::: " + empKRA.getEmpId());
				EmpKRA newEmpKRA = optional.get();
				newEmpKRA.setLevel2ApproverRatingI(empKRA.getLevel2ApproverRatingI());
				newEmpKRA.setLevel2ApproverRatingII(empKRA.getLevel2ApproverRatingII());
				newEmpKRA.setLevel2ApproverRatingIII(empKRA.getLevel2ApproverRatingIII());
				newEmpKRA.setLevel2ApproverRatingIV(empKRA.getLevel2ApproverRatingIV());
				newEmpKRA.setLevel2ApproverRatingV(empKRA.getLevel2ApproverRatingV());
				newEmpKRA.setLevel2ApproverRatingVI(empKRA.getLevel2ApproverRatingVI());
				newEmpKRA.setLevel2ApproverRatingVII(empKRA.getLevel2ApproverRatingVII());
				newEmpKRA.setLevel2ApproverRatingVIII(empKRA.getLevel2ApproverRatingVIII());
				newEmpKRA.setLevel2ApproverRatingIX(empKRA.getLevel2ApproverRatingIX());
				newEmpKRA.setLevel2ApproverRatingX(empKRA.getLevel2ApproverRatingX());
				newEmpKRA.setTotalLevelII(empKRA.getTotalLevelII());
				newEmpKRA.setLevelIIAppStatus(ProdConstant.DRAFT);
				newEmpKRA.setLevelIIRating(empKRA.getLevelIIRating());
				empKRARepository.save(newEmpKRA);
			} else {
				System.out.println("ELSE :::::::::::");
				System.out.println("EmpKRA object is null");
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while save draft rating by reporting super manager ... " + ExceptionUtils.getStackTrace(e));
		}
		return empKRA;
	}

	@Override
	public EmpKRA saveRatingByReportingManager2(EmpKRA empKRA) throws Exception {
		try {
			Optional<EmpKRA> optional = empKRARepository.findByempId(empKRA.getEmpId());
			if (optional.isPresent()) {
				System.out.println("IF ::::::::::: " + empKRA.getEmpId());
				EmpKRA newEmpKRA = optional.get();
				newEmpKRA.setLevel2ApproverRatingI(empKRA.getLevel2ApproverRatingI());
				newEmpKRA.setLevel2ApproverRatingII(empKRA.getLevel2ApproverRatingII());
				newEmpKRA.setLevel2ApproverRatingIII(empKRA.getLevel2ApproverRatingIII());
				newEmpKRA.setLevel2ApproverRatingIV(empKRA.getLevel2ApproverRatingIV());
				newEmpKRA.setLevel2ApproverRatingV(empKRA.getLevel2ApproverRatingV());
				newEmpKRA.setLevel2ApproverRatingVI(empKRA.getLevel2ApproverRatingVI());
				newEmpKRA.setLevel2ApproverRatingVII(empKRA.getLevel2ApproverRatingVII());
				newEmpKRA.setLevel2ApproverRatingVIII(empKRA.getLevel2ApproverRatingVIII());
				newEmpKRA.setLevel2ApproverRatingIX(empKRA.getLevel2ApproverRatingIX());
				newEmpKRA.setLevel2ApproverRatingX(empKRA.getLevel2ApproverRatingX());
				newEmpKRA.setTotalLevelII(empKRA.getTotalLevelII());
				newEmpKRA.setLevelIIAppStatus(ProdConstant.COMPLETED);
				newEmpKRA.setLevelIIRating(empKRA.getLevelIIRating());
				empKRARepository.save(newEmpKRA);
			} else {
				System.out.println("ELSE :::::::::::");
				System.out.println("EmpKRA object is null");
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occuring while save rating by reporting super manager ... " + ExceptionUtils.getStackTrace(e));
		}
		return empKRA;
	}

	@Override
	public List<EmpKRA> getTeammetsTeamMangerId(String empId, String depId) {
		try {
			return empKRARepository.getTeammetsTeamMangerId(empId, depId);
		} catch (Exception e) {
			LOGGER.error("Error occuring while get teammets team manager id  ... " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<EmpKRA> getEmpWithMangerIdOrReportingManager(String empId, String depId, List<String> empIds) {
		try {
			return empKRARepository.getEmpWithMangerIdOrReportingManager(empId, depId, empIds);
		} catch (Exception e) {
			LOGGER.error("Error occuring while get Emp With MangerId Or ReportingManager ... " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<String> getEmpWithManger3(String empId) {
		try {
			if (Objects.nonNull(empId)) {
				return empWorkRepository.getEmpWithManger3(empId);
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while to get Emp With Manger ... " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public EmpWorkDetails getEmpWithManger1(String empId, List<String> empId2) {
		try {
			return empWorkRepository.getEmpWithManger1(empId, empId2);
		} catch (Exception e) {
			LOGGER.error("Error occuring while get Emp With MangerId Or ReportingManager ... " + ExceptionUtils.getStackTrace(e));
		}
		return new EmpWorkDetails();
	}

	@Override
	public List<EmpKRA> findEmpByEmpId(String empId) throws Exception {
		try {
			return empKRARepository.findEmpByEmpId(empId,ProdConstant.TRUE);
		} catch (Exception e) {
			LOGGER.error("Error occuring while get Emp by empId " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<EmpKRA> findEmpByDepId(String depId) throws Exception {
		try {
			return empKRARepository.findEmpByDepId(depId,ProdConstant.TRUE);
		} catch (Exception e) {
			LOGGER.error("Error occuring while get Emp With depId" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}
	
	@Override
	public List<EmpKRA> findEmpByIsactive() throws Exception {
		try {
			return empKRARepository.findEmpByIsactive(ProdConstant.TRUE);
		} catch (Exception e) {
			LOGGER.error("Error occuring while get Emp With active " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<EmpKRA> findEmpByEmpIdAndDepId(String empId, String depId) throws Exception {
		try {
			return empKRARepository.findEmpByEmpIdAndDepId(empId, depId,ProdConstant.TRUE);
		} catch (Exception e) {
			LOGGER.error("Error occuring while get Emp With empId & depId" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<EmpKRA> appraisalCycleList(String empId) throws Exception {
		try {
			return empKRARepository.appraisalCycleList(empId,ProdConstant.COMPLETED);
		} catch (Exception e) {
			LOGGER.error("Error occuring while get appraisal Cycle List" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public EmpKRA getKRAWithDepIdAndEmpIdAndDate(String depId, String empId, Date date) throws Exception {
		try {
			return empKRARepository.getKRAWithDepIdAndEmpIdAndDate(depId, empId, ProdConstant.TRUE, date);
		} catch (Exception e) {
			LOGGER.error("Error occuring while get KRA With DepId And EmpId And Date" + ExceptionUtils.getStackTrace(e));
		}
		return new EmpKRA();

	}

	@Override
	public List<String> getTeammetsTeamMangerId2(String empId, String depId) throws Exception {
		try {
			return empWorkRepository.getTeammetsTeamMangerId2(empId, depId, ProdConstant.TRUE);
		} catch (Exception e) {
			LOGGER.error("Error occuring while get teammets by team manager" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

}