package com.ksv.ktrccrm.db1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ksv.ktrccrm.db1.entities.CityModel;


public interface CityRepository extends JpaRepository<CityModel, Integer> {

	@Query("SELECT u FROM CityModel u WHERE u.stateId = :cityId")
	public List<CityModel> getCityByState(@Param("cityId") Integer cityId);
}
