package com.ksv.ktrccrm.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.dao.CountryStateCityDao;
import com.ksv.ktrccrm.db1.entities.CityModel;
import com.ksv.ktrccrm.db1.entities.CountryModel;
import com.ksv.ktrccrm.db1.entities.StateModel;
import com.ksv.ktrccrm.db1.repository.CityRepository;
import com.ksv.ktrccrm.db1.repository.CountryRepository;
import com.ksv.ktrccrm.db1.repository.StateRepository;

@Repository
public class CountryStateCityDaoImpl implements CountryStateCityDao {

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private StateRepository stateRepository;

	@Autowired
	private CityRepository cityRepository;

	@Override
	public Map<Integer, String> getCountriesMap() throws Exception {
		List<CountryModel> countries = null;
		Map<Integer, String> countryMap = new HashMap<>();
		try {
			countries = countryRepository.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (countries != null) {
			for (CountryModel model : countries) {
				countryMap.put(model.getCountryId(), model.getCountryName());
			}
		}
		return countryMap;
	}

	@Override
	public Map<Integer, String> getStateMap(Integer countryId) throws Exception {
		List<StateModel> states = null;
		Map<Integer, String> statesMap = new HashMap<>();
		try {
			statesMap.put(1, "Select State");
			states = stateRepository.getStateByCountry(countryId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (states != null) {
			for (StateModel model : states) {
				statesMap.put(model.getStateId(), model.getStateName());
			}
		}
		return statesMap;
	}

	@Override
	public Map<Integer, String> getCitiesMap(Integer stateId) throws Exception {
		List<CityModel> cities = null;
		Map<Integer, String> citiesMap = new HashMap<>();
		try {
			citiesMap.put(1, "Select City");
			cities = cityRepository.getCityByState(stateId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (cities != null) {
			for (CityModel model : cities) {
				citiesMap.put(model.getCityId(), model.getCityName());
			}
		}
		return citiesMap;
	}
}
