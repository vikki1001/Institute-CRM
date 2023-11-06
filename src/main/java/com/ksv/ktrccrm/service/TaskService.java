package com.ksv.ktrccrm.service;

import java.util.List;

import com.ksv.ktrccrm.db1.entities.Task;

public interface TaskService {
	
	public List<Task> weeklyTask(String empId,String isActive, String tenantId) throws Exception;

	public List<Task> taskAssign(String empId,String isActive) throws Exception;

	public Task cancelTask(Long id) throws Exception;

	public List<Task> cancelTaskList(String empId,String isActive) throws Exception;

	public Task saveTask(Task task) throws Exception;

	public Task getTaskById(Long id) throws Exception;

	public List<Task> completedTask(String empId, String isActive,String status) throws Exception;

	public List<Task> dashboardTask(String empId,String isActive) throws Exception;

	public Task completeTask(Long id) throws Exception;

}
