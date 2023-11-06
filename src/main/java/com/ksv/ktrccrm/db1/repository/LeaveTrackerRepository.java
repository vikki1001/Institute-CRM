package com.ksv.ktrccrm.db1.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ksv.ktrccrm.db1.entities.LeaveTracker;

@Transactional
public interface LeaveTrackerRepository extends JpaRepository<LeaveTracker, Long> {

	@Query("SELECT u FROM LeaveTracker u WHERE u.empId = :empId AND u.isActive = :isActive")
	public List<LeaveTracker> getEmpDetails(String empId, String isActive);

	@Query("SELECT u FROM LeaveTracker u WHERE u.empId = :empId AND u.isActive = :isActive")
	public Optional<LeaveTracker> findByEmpId(String empId,String isActive);
	
	@Query("SELECT u FROM LeaveTracker u WHERE u.empId = :empId AND u.isActive = :isActive")
	public LeaveTracker getCurrentUser(String empId,String isActive);

	/* BY ID */
	@Query("SELECT u FROM LeaveTracker u WHERE u.id = :id AND u.isActive = :isActive")
	public Optional<LeaveTracker> getEmpDetailsById(Long id,String isActive);

	@Modifying
	@Query("update LeaveTracker set totalLeave = :totalLeave where id = :id and isActive = :isActive")
	void updateTotalLeaveById(Float totalLeave, Long id,String isActive);

	@Modifying
	@Query("update LeaveTracker set paidLeave = :paidLeave where id = :id and isActive = :isActive")
	void updatePaidLeaveById(Float paidLeave, Long id, String isActive);

	@Modifying
	@Query("update LeaveTracker set maternityLeave = :maternityLeave where id = :id and isActive = :isActive")
	void updateMaternityLeaveById(Float maternityLeave, Long id, String isActive);

	@Query("SELECT e FROM LeaveTracker e WHERE e.isActive=:isActive AND e.tenantId = :tenantId")
	public List<LeaveTracker> getRecordList(String isActive , String tenantId);

	@Modifying
	@Query("UPDATE LeaveTracker SET totalLeave = :totalLeave WHERE empId = :empId AND isActive = :isActive")
	public void updateTotalLeave(float totalLeave, String empId,String isActive);

	@Query("SELECT totalLeave FROM LeaveTracker e WHERE e.isActive=:isActive")
	public List<Float> getAllEmpTotalLeave(String isActive);

	@Modifying
	@Query("UPDATE LeaveTracker SET paidLeave = :paidLeave WHERE empId = :empId AND isActive = :isActive")
	public void updatePaidLeave(float paidLeave, String empId,String isActive);

	@Query("SELECT paidLeave FROM LeaveTracker e WHERE e.isActive=:isActive")
	public List<Float> getAllEmpPaidLeave(String isActive);

	@Modifying
	@Query("UPDATE LeaveTracker SET maternityLeave = :maternityLeave WHERE empId = :empId AND isActive = :isActive")
	public void updateMaternityLeave(float maternityLeave, String empId,String isActive);

	@Query("SELECT maternityLeave FROM LeaveTracker e WHERE e.isActive=:isActive")
	public List<Float> getAllEmpMaternityLeave(String isActive);

	@Query("SELECT empId FROM LeaveTracker e WHERE e.isActive=:isActive")
	public List<String> getAllEmp(String isActive);

}
