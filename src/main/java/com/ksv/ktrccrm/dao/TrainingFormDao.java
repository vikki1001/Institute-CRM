package com.ksv.ktrccrm.dao;

import java.util.List;

import com.ksv.ktrccrm.db1.entities.TrainingForm;

public interface TrainingFormDao {

	public List<TrainingForm> getRecordList(String userId,String isActive, String tenantId) throws Exception;

	public TrainingForm saveUpdate(TrainingForm trainingForm) throws Exception;

	public List<TrainingForm> getListForHr(String isActive) throws Exception;

	public List<TrainingForm> getListForManger(String userId,String isActive) throws Exception;

	public TrainingForm getTrainingReq(String userId, String subject, String isActive) throws Exception;

	public TrainingForm getById(Long id) throws Exception;
	
	public List<TrainingForm> getTotalTraining(String isActive) throws Exception;
	
	public Long getAllTraining(String isActive, String tenantId) throws Exception;
}
