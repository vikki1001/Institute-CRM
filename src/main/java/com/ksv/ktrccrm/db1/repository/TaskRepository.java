package com.ksv.ktrccrm.db1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ksv.ktrccrm.db1.entities.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

	@Query("SELECT u FROM Task u WHERE WEEKOFYEAR(u.date) = WEEKOFYEAR(CURRENT_DATE()) AND u.empId = :empId  AND u.status = :status AND u.isActive = :isActive  AND u.tenantId = :tenantId ORDER BY date ASC")
	public List<Task> weeklyTask(String empId, String status, String isActive, String tenantId);

	@Query("SELECT u FROM Task u WHERE WEEKOFYEAR(u.date) = WEEKOFYEAR(CURRENT_DATE()) AND u.managerId = :empId AND u.isActive = :isActive ORDER BY date ASC")
	public List<Task> taskAssign(String empId, String isActive);

	@Query("SELECT u FROM Task u WHERE WEEKOFYEAR(u.date) = WEEKOFYEAR(CURRENT_DATE()) AND u.managerId = :empId AND u.isActive = :isActive")
	public List<Task> cancelTaskList(String empId, String isActive);

	@Query("SELECT u FROM Task u WHERE WEEKOFYEAR(u.date) = WEEKOFYEAR(CURRENT_DATE()) AND u.empId = :empId AND u.isActive = :isActive AND u.status = :status ")
	public List<Task> completedTask(String empId, String isActive, String status);

	@Query("SELECT u FROM Task u WHERE u.isActive = :isActive AND WEEKOFYEAR(u.date) = WEEKOFYEAR(CURRENT_DATE()) AND u.empId = :empId ORDER BY date ASC")
	public List<Task> dashboardTask(String empId, String isActive);

}
