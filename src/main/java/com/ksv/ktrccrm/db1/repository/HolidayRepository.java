package com.ksv.ktrccrm.db1.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ksv.ktrccrm.db1.entities.HolidayMaster;

public interface HolidayRepository extends JpaRepository<HolidayMaster, Long>{

	@Query("SELECT u FROM HolidayMaster u WHERE u.tenantId=:tenantId AND u.holidayDate > CURDATE() ORDER BY holidayDate ASC")
	public List<HolidayMaster> getHolidayUpcoming(String tenantId);
	
	@Query("SELECT u FROM HolidayMaster u WHERE u.holidayDate < :holidayDate")
	public List<HolidayMaster> getHolidayHistory(@Param("holidayDate") Date holidayDate);

	@Query("SELECT count(u) FROM HolidayMaster u WHERE MONTH(u.holidayDate) = MONTH(CURRENT_DATE())")
	public Long getHolidayDays();
	
	@Query("SELECT COUNT(u) FROM HolidayMaster u WHERE u.holidayDate between DATE_FORMAT(:from, '%Y-%m-%d') AND DATE_FORMAT(:to, '%Y-%m-%d')")
	public float getHolidayDaysForLeave(Date from, Date to);
}