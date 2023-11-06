package com.ksv.ktrccrm.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.TrainingFormDao;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.TrainingForm;
import com.ksv.ktrccrm.db1.repository.EmpBasicRepository;
import com.ksv.ktrccrm.db1.repository.TrainingFormRepository;

@Repository
public class TrainingFormDaoImpl implements TrainingFormDao {
	private static final Logger LOGGER = LogManager.getLogger(TrainingFormDaoImpl.class);

	@Autowired
	private TrainingFormRepository trainingFormRepository;
	@Autowired
	private EmpBasicRepository empBasicRepository;
	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;

	@Override
	public List<TrainingForm> getRecordList(String userId, String isActive, String tenantId) throws Exception {
		List<TrainingForm> trainingForm = trainingFormRepository.getRecordList(userId,isActive,tenantId);
		try {
			if (Objects.nonNull(trainingForm)) {
				return trainingForm;
			}
		} catch (Exception e) {
			LOGGER.error("Error Occuring While Display List " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public TrainingForm saveUpdate(TrainingForm trainingForm) throws Exception {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String loginId = authentication.getName();
			if (Objects.nonNull(loginId)) {
				EmpBasicDetails userDetails = empBasicRepository.getCurrentUser(loginId,ProdConstant.TRUE);
				System.out.println("TenantId:::::::::"+ userDetails.getTenantId());
				if (Objects.isNull(trainingForm.getId())) {
					trainingForm.setIsActive(ProdConstant.TRUE);
					trainingForm.setCreatedDate(checkOutDaoImpl.getDateTime());
					trainingForm.setCreatedBy(loginId);
					trainingForm.setDate(checkOutDaoImpl.getDate());
					trainingForm.setUserId(loginId);
					trainingForm.setReportingManager(userDetails.getEmpWorkDetails().getReportingManager());
					trainingForm.setTenantId(userDetails.getTenantId());

					trainingFormRepository.save(trainingForm);
				} else {
					Optional<TrainingForm> trainingFormOptional = trainingFormRepository.findById(trainingForm.getId());
					if (trainingFormOptional.isPresent()) {
						TrainingForm newTrainingForm = trainingFormOptional.get();
						newTrainingForm.setSubject(trainingForm.getSubject());
						newTrainingForm.setTrainingPurpose(trainingForm.getTrainingPurpose());
						newTrainingForm.setLastModifiedBy(loginId);
						newTrainingForm.setLastModifiedDate(checkOutDaoImpl.getDateTime());

						trainingFormRepository.save(newTrainingForm);
					} else {
						return trainingForm;
					}
				}
			} else {
				LOGGER.error("Error occur to get login id");
			}

		} catch (Exception e) {
			LOGGER.error("Error Occuring While Save & update Data " + ExceptionUtils.getStackTrace(e));
		}
		return trainingForm;
	}

	@Override
	public List<TrainingForm> getListForHr(String isActive) throws Exception {
		List<TrainingForm> trainingForm = trainingFormRepository.getListForHr(isActive);
		try {
			if (Objects.nonNull(trainingForm)) {
				return trainingForm;
			}
		} catch (Exception e) {
			LOGGER.error("Error Occuring While Display Training List to HR " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<TrainingForm> getListForManger(String userId, String isActive) throws Exception {
		List<TrainingForm> trainingForm = trainingFormRepository.getListForManger(userId, isActive);
		try {
			if (Objects.nonNull(trainingForm)) {
				return trainingForm;
			}
		} catch (Exception e) {
			LOGGER.error("Error Occuring While Display Training List to Manager " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public TrainingForm getTrainingReq(String userId, String subject, String isActive) throws Exception {
		TrainingForm trainingForm = trainingFormRepository.getTrainingReq(userId, subject, isActive);
		try {
			if (Objects.nonNull(trainingForm)) {
				return trainingForm;
			}
		} catch (Exception e) {
			LOGGER.error("Error Occuring While Display Training Training Data into Mail " + ExceptionUtils.getStackTrace(e));
		}
		return new TrainingForm();
	}

	@Override
	public TrainingForm getById(Long id) throws Exception {
		TrainingForm trainingForm = null;
		Optional<TrainingForm> optional = trainingFormRepository.findById(id);
		try {
			if (optional.isPresent()) {
				trainingForm = optional.get();
			} else {
				System.out.println("Error occur while get training by ID");
			}
		} catch (Exception e) {
			LOGGER.error("Error Occuring While Get Training by ID " + ExceptionUtils.getStackTrace(e));
		}
		return trainingForm;
	}

	@Override
	public List<TrainingForm> getTotalTraining(String isActive) throws Exception {
		List<TrainingForm> trainingForms = trainingFormRepository.getTotalTraining(isActive);
		try {
			if (Objects.nonNull(trainingForms)) {
				return trainingForms;
			} else {
				System.out.println("Null Data Get:: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get list of total training form " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	
	}

	@Override
	public Long getAllTraining(String isActive, String tenantId) throws Exception {
		try {
			return trainingFormRepository.getAllTraining(isActive,tenantId);
		} catch (Exception e) {
			LOGGER.error("Error occuring while get all training of employee in current month " + ExceptionUtils.getStackTrace(e));
		}
		return (long) 0;
	}
}