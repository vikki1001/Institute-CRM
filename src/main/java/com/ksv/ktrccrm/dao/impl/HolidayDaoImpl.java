package com.ksv.ktrccrm.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.dao.HolidayDao;
import com.ksv.ktrccrm.db1.entities.HolidayMaster;
import com.ksv.ktrccrm.db1.repository.HolidayRepository;

@Repository
public class HolidayDaoImpl implements HolidayDao {
	private static final Logger LOGGER = LogManager.getLogger(HolidayDaoImpl.class);
	
	@Autowired
	private HolidayRepository holidayRepository;

	/* List of Upcoming Holidays */
	@Override
	public List<HolidayMaster> getHolidayUpcoming(String tenantId) throws Exception {
		List<HolidayMaster> holidayList = holidayRepository.getHolidayUpcoming(tenantId);
		try {
			if(!holidayList.isEmpty()) {
				return holidayList;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display upcoming holiday list" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}
	
	/* List of History Holidays */
	@Override
	public List<HolidayMaster> getHolidayHistory(Date createdDate) throws Exception {
		List<HolidayMaster> holidayList = holidayRepository.getHolidayHistory(createdDate);
		try {
			if(!holidayList.isEmpty()) {
				return holidayList;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display history holiday list" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	/* For Save/Upload Holiday Excel File */	
	public void saveAll(List<HolidayMaster> holidayMasters) throws Exception {
		try {
			this.holidayRepository.saveAll(holidayMasters);
		} catch (Exception e) {
			LOGGER.error("Error while save holiday master" + ExceptionUtils.getStackTrace(e));
		}
	
	}

	/* Count Total Holiday for Current Month */
	@Override
	public Long getHolidayDays() throws Exception {
		try {
			return holidayRepository.getHolidayDays();
		} catch (Exception e) {
			LOGGER.error(" Error while get total holiday in current month " + ExceptionUtils.getStackTrace(e));
		}
		return (long) 0;
	}
	
	/* Get all Records into DB */
	@Override
	public List<HolidayMaster> findAll() throws Exception {
		List<HolidayMaster> holidayMaster = new ArrayList<>();
		try {
			holidayMaster = holidayRepository.findAll();
		} catch (Exception e) {
			LOGGER.error("Error occur while display all holiday " + ExceptionUtils.getStackTrace(e));
		}
		return holidayMaster;
	}

	@Override
	public float getHolidayDaysForLeave(Date from, Date to) throws Exception {
		try {
			return holidayRepository.getHolidayDaysForLeave(from, to);
		} catch (Exception e) {
			LOGGER.error("Error occur while get no. of holidays " + ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}
}
