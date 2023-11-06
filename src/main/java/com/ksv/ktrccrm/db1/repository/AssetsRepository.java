package com.ksv.ktrccrm.db1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ksv.ktrccrm.db1.entities.Assets;

public interface AssetsRepository extends JpaRepository<Assets, Long> {

	@Query("SELECT e FROM Assets e WHERE e.isActive = :isActive AND e.tenantId=:tenantId")
	public List<Assets> getRecordList(String isActive, String tenantId);  

	@Query("SELECT e FROM Assets e WHERE e.empId =:empId AND e.isActive =:isActive AND e.confirmationStatus =:confirmationStatus")
	public List<Assets> getEmpAssetsList(String empId, String isActive, String confirmationStatus);

	@Query("SELECT u FROM Assets u WHERE u.empId =:userId AND u.isActive =:isActive")
	public List<Assets> assetsNotification(String userId, String isActive);

	@Query("SELECT u FROM Assets u WHERE u.empId =:userId AND u.isActive =:isActive AND u.notification =:notification")
	public List<Assets> unreadNotification(String userId, String isActive, String notification);

	@Query("SELECT u FROM Assets u WHERE u.assetsCode IN :assetsCode")
	public List<Assets> checkDuplicateAssetsCode(String assetsCode);

	@Query("SELECT u FROM Assets u WHERE u.id =:id")
	public Assets getDataById(Long id);        
}
