package com.ksv.ktrccrm.db1.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ksv.ktrccrm.db1.entities.ExpenseReimb;

public interface ExpenseReimbRepository extends JpaRepository<ExpenseReimb, Long> {

	@Query("SELECT u FROM ExpenseReimb u WHERE u.empId =:empId AND u.isActive=:isActive AND u.tenantId=:tenantId ORDER BY u.date DESC")
	public List<ExpenseReimb> getRecordList(String empId,String isActive, String tenantId);

	@Query("SELECT u FROM ExpenseReimb u WHERE u.id = :id AND u.isActive=:isActive")
	public Optional<ExpenseReimb> getCancelById(Long id,String isActive);

	@Query("SELECT u FROM ExpenseReimb u WHERE u.empId = :empId AND u.isActive = :isActive  ORDER BY u.date DESC")
	public List<ExpenseReimb> getExpenseReimbByEmpId(String empId,String isActive);

	@Query("SELECT u FROM ExpenseReimb u WHERE u.isActive = :isActive ORDER BY u.date DESC")
	public List<ExpenseReimb> getEmpWithManger(String isActive);

	@Transactional
	@Modifying
	@Query("update ExpenseReimb set status = :status, flag = :flag  where id = :id")
	public void acceptStatus(String status, String flag, Long id);

	@Query("SELECT u FROM ExpenseReimb u WHERE u.id = :id AND u.isActive = :isActive")
	public List<ExpenseReimb> acceptExpenseReimbById(Long id,String isActive);

	@Query("SELECT u FROM ExpenseReimb u WHERE u.id = :id")
	public ExpenseReimb getAttachment(Long id);

	@Query("SELECT u FROM ExpenseReimb u WHERE u.empId = :empId")
	public ExpenseReimb getCurrentUser(String empId);
	
	@Query("SELECT u FROM ExpenseReimb u WHERE u.isActive =:isActive AND u.notification =:notification")
	public List<ExpenseReimb> unreadNotification(String isActive, String notification);
	
	@Query("SELECT u FROM ExpenseReimb u WHERE u.empId =:empId AND u.isActive =:isActive AND u.saveDraft =:saveDraft")
	public List<ExpenseReimb> getEmpDetails(String empId, String isActive, String saveDraft);
	
	@Query("SELECT u FROM ExpenseReimb u WHERE u.empBasicDetails.empId = :empId AND u.isActive = :isActive AND u.status = :status AND u.tenantId=:tenantId ORDER BY u.date DESC")
	public List<ExpenseReimb> getEmpWithMangerWithPending(String empId, String isActive, String status, String tenantId);	
	
}