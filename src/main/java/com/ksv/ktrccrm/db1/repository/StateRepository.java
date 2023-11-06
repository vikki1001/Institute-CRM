package com.ksv.ktrccrm.db1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ksv.ktrccrm.db1.entities.StateModel;

public interface StateRepository extends JpaRepository<StateModel, Integer> {

	@Query("SELECT u FROM StateModel u WHERE u.countryId = :stateId")
	public List<StateModel> getStateByCountry(@Param("stateId") Integer stateId);
}
