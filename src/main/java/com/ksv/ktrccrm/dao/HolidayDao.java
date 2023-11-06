package com.ksv.ktrccrm.dao;

import java.util.Date;
import java.util.List;

import com.ksv.ktrccrm.db1.entities.HolidayMaster;

public interface HolidayDao {
	
	public List<HolidayMaster> getHolidayUpcoming(String tenantId) throws Exception;
	
	public List<HolidayMaster> getHolidayHistory(Date holidayDate) throws Exception;
	
	public void saveAll(List<HolidayMaster> holidayMasters) throws Exception;
		
	public Long getHolidayDays() throws Exception;
	
	public List<HolidayMaster> findAll() throws Exception;
	
	public float getHolidayDaysForLeave(Date from, Date to) throws Exception;
}
