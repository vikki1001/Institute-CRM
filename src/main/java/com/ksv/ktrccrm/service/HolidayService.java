package com.ksv.ktrccrm.service;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ksv.ktrccrm.db1.entities.HolidayMaster;

public interface HolidayService {

	public List<HolidayMaster> getHolidayUpcoming(String tenantId) throws Exception;

	public List<HolidayMaster> getHolidayHistory(Date holidayDate) throws Exception;

	public void saveAll(MultipartFile files) throws Exception;
	
	public ByteArrayInputStream exportHoliday(List<HolidayMaster> holidayMasters);

	public Long getHolidayDays() throws Exception;

	public List<HolidayMaster> findAll() throws Exception;

	public float getHolidayDaysForLeave(Date from, Date to) throws Exception;
}
