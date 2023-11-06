package com.ksv.ktrccrm.db1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ksv.ktrccrm.db1.entities.EmpPersonalDetails;

public interface EmpPersonalRepository extends JpaRepository<EmpPersonalDetails, Long> {

	@Query("SELECT u FROM EmpPersonalDetails u WHERE  day(u.birthDate) > day(CURRENT_DATE) AND month(u.birthDate) = month(CURRENT_DATE) AND u.empBasicDetails.tenantId=:tenantId ORDER BY birthDate ASC")
	public List<EmpPersonalDetails> getUpcomingEmpBirthday(String tenantId);
	
	@Query("SELECT u FROM EmpPersonalDetails u WHERE  day(u.birthDate) = day(CURRENT_DATE) AND month(u.birthDate) = month(CURRENT_DATE) AND u.empBasicDetails.tenantId=:tenantId ORDER BY birthDate ASC")
	public List<EmpPersonalDetails> getEmpBirthdayToday(String tenantId);
	
	@Query("SELECT u FROM EmpPersonalDetails u WHERE u.maritalStatus = :maritalStatus AND u.empBasicDetails = :empId")
	public EmpPersonalDetails getMarriedOnly(@Param("maritalStatus") String maritalStatus, @Param("empId") String empId);
	
	@Query("SELECT u.empBasicDetails FROM EmpPersonalDetails u WHERE u.maritalStatus = :maritalStatus")
	public List<String> getMarriedEmployee(@Param("maritalStatus") String maritalStatus);
	
	@Query("SELECT u.empBasicDetails.empId FROM EmpPersonalDetails u WHERE  day(u.birthDate) = day(CURRENT_DATE) AND month(u.birthDate) = month(CURRENT_DATE) ORDER BY birthDate ASC")
	public List<String> getUserIdWhoseBirthdayToday();
	
	@Query("SELECT u FROM EmpPersonalDetails u WHERE  day(u.birthDate) = day(CURRENT_DATE) AND month(u.birthDate) = month(CURRENT_DATE) ORDER BY birthDate ASC")
	public List<EmpPersonalDetails> getEmpBirthdayTodayForMail();

}
