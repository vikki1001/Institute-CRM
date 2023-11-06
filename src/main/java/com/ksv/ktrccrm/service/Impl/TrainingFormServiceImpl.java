package com.ksv.ktrccrm.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.TrainingFormDao;
import com.ksv.ktrccrm.db1.entities.TrainingForm;
import com.ksv.ktrccrm.service.TrainingFormService;

@Service
public class TrainingFormServiceImpl implements TrainingFormService {
	private static final Logger LOGGER = LogManager.getLogger(TrainingFormServiceImpl.class);

	@Autowired
	private TrainingFormDao trainingFormDao;

	@Override
	public List<TrainingForm> getRecordList(String userId, String isActive, String tenantId) throws Exception {
		List<TrainingForm> trainingForm = trainingFormDao.getRecordList(userId, isActive,tenantId);
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
			if (Objects.nonNull(trainingForm)) {
				trainingFormDao.saveUpdate(trainingForm);
			}
		} catch (Exception e) {
			LOGGER.error("Error Occuring While Saving Data " + ExceptionUtils.getStackTrace(e));
		}
		return trainingForm;
	}

	@Override
	public List<TrainingForm> getListForHr(String isActive) throws Exception {
		List<TrainingForm> trainingForm = trainingFormDao.getListForHr(isActive);
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
		List<TrainingForm> trainingForm = trainingFormDao.getListForManger(userId, isActive);
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
		TrainingForm trainingForm = trainingFormDao.getTrainingReq(userId, subject, isActive);
		try {
			if (Objects.nonNull(trainingForm)) {
				return trainingForm;
			}
		} catch (Exception e) {
			LOGGER.error("Error Occuring While Display Training Data into Mail " + ExceptionUtils.getStackTrace(e));
		}
		return new TrainingForm();
	}

	@Override
	public TrainingForm getById(Long id) throws Exception {
		TrainingForm trainingForm = trainingFormDao.getById(id);
		try {
			if (Objects.nonNull(trainingForm)) {
				return trainingForm;
			}
		} catch (Exception e) {
			LOGGER.error("Error Occuring While Get Training by ID " + ExceptionUtils.getStackTrace(e));
		}
		return new TrainingForm();
	}

	@Override
	public List<TrainingForm> getTotalTraining(String isActive) throws Exception {
		List<TrainingForm> trainingForms = trainingFormDao.getTotalTraining(isActive);
		try {
			if (Objects.nonNull(trainingForms)) {
				return trainingForms;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get list of total training form " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public Long getAllTraining(String isActive,String tenantId) throws Exception {
		try {
			return trainingFormDao.getAllTraining(isActive, tenantId);
		} catch (Exception e) {
			LOGGER.error("Error occuring while get all training of employee in current month "
					+ ExceptionUtils.getStackTrace(e));
		}
		return (long) 0;
	}

	public TrainingForm trainingEnableAndDisable(Long id) {
		try {
			TrainingForm trainingDetails = trainingFormDao.getById(id);
			if (Objects.nonNull(trainingDetails)) {
				String checkActiveOrInActiveData = trainingDetails.getIsActive();

				if (checkActiveOrInActiveData.equals(ProdConstant.TRUE)) {
					trainingDetails.setIsActive(ProdConstant.FALSE);
					trainingFormDao.saveUpdate(trainingDetails);
				} else if (checkActiveOrInActiveData.equals(ProdConstant.FALSE)) {
					trainingDetails.setIsActive(ProdConstant.TRUE);
					trainingFormDao.saveUpdate(trainingDetails);
				}
			} else {
				System.out.println("trainingDetails object is null");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while activate and deactivate training  " + ExceptionUtils.getStackTrace(e));
		}

		return new TrainingForm();

	}
}