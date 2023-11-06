package com.ksv.ktrccrm.db1.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ksv.ktrccrm.db1.entities.TrainingForm;

@Transactional
public interface TrainingFormRepository extends JpaRepository<TrainingForm, Long> {

	@Query("SELECT e FROM TrainingForm e WHERE e.isActive = :isActive AND e.userId = :userId AND e.tenantId = :tenantId ORDER BY date DESC")
	public List<TrainingForm> getRecordList(String userId, String isActive, String tenantId);
	
	@Query("SELECT e FROM TrainingForm e WHERE e.createdBy = :userId AND e.date = CURDATE() AND e.isActive = :isActive AND e.subject = :subject")
	public TrainingForm getTrainingReq(String userId, String subject, String isActive);

	@Query("SELECT e FROM TrainingForm e WHERE e.isActive = :isActive ORDER BY date DESC")
	public List<TrainingForm> getListForHr(String isActive);

	@Query("SELECT e FROM TrainingForm e WHERE e.reportingManager = :userId AND e.isActive = :isActive ORDER BY date DESC")
	public List<TrainingForm> getListForManger(String userId, String isActive);

	/* DashBoard Count for HR */
	@Query("SELECT count(e) FROM TrainingForm e WHERE MONTH(e.date) = MONTH(CURRENT_DATE()) AND e.isActive = :isActive AND  e.tenantId=:tenantId")
	public Long getAllTraining(String isActive, String tenantId);
	
	/* Link */
	@Query("SELECT e FROM TrainingForm e WHERE MONTH(e.date) = MONTH(CURRENT_DATE()) AND e.isActive = :isActive ORDER BY date DESC")
	public List<TrainingForm> getTotalTraining(String isActive);

	@Query("SELECT u FROM TrainingForm u WHERE u.id=:id")
	public TrainingForm getDataById(Long id);

	
}
