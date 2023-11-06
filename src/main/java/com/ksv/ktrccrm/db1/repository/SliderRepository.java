package com.ksv.ktrccrm.db1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ksv.ktrccrm.db1.entities.Slider;

public interface SliderRepository extends JpaRepository<Slider, Integer> {

	@Query("SELECT u FROM Slider u WHERE u.isActive = :isActive AND u.tenantId=:tenantId")
	List<Slider> getRecordList(String isActive, String tenantId);

	@Query("SELECT u FROM Slider u WHERE u.id=:id")
	public Slider getDataById(Integer id);
}
