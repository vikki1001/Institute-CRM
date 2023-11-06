package com.ksv.ktrccrm.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.TaskDao;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.Task;
import com.ksv.ktrccrm.db1.repository.EmpBasicRepository;
import com.ksv.ktrccrm.db1.repository.TaskRepository;

@Repository
public class TaskDaoImpl implements TaskDao {
	private static final Logger LOGGER = LogManager.getLogger(TaskDaoImpl.class);

	@Autowired
	private TaskRepository taskRepository;
	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;
	@Autowired EmpBasicRepository empBasicRepository;
	
	/* All Emp Weekly Task Display to Task Assigner */
	@Override
	public List<Task> weeklyTask(String empId,String isActive, String tenantId) throws Exception {
		List<Task> listTask = new ArrayList<>();
		try {
			listTask = taskRepository.weeklyTask(empId, ProdConstant.PENDING, isActive, tenantId);
		} catch (Exception e) {
			LOGGER.error("Error occur while display Weekly Data " + ExceptionUtils.getStackTrace(e));
		}
		return listTask;
	}

	/* List Of IsActive Task */
	@Override
	public List<Task> taskAssign(String empId,String isActive) throws Exception {
		List<Task> listTask = taskRepository.taskAssign(empId, isActive);
		try {
			if (Objects.nonNull(listTask)) {
				return listTask;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display assign task list " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}


	/*Complete Task*/
	@Override
	public Task completeTask(Long id) throws Exception {
		try {
			Optional<Task> task1 = taskRepository.findById(id);
			if (task1.isPresent()) {
				Task newTask = task1.get();
				newTask.setStatus(ProdConstant.COMPLETED);
				taskRepository.save(newTask);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while Complete task " + ExceptionUtils.getStackTrace(e));
		}
		return new Task();
	}
	
	/* Cancel Registered Task */
	@Override
	public Task cancelTask(Long id) throws Exception {
		try {
			Optional<Task> task1 = taskRepository.findById(id);
			if (task1.isPresent()) {
				Task newTask = task1.get();
				newTask.setIsActive(ProdConstant.FALSE);
				taskRepository.save(newTask);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while cancel task " + ExceptionUtils.getStackTrace(e));
		}
		return new Task();
	}

	/* List of cancel Task */
	@Override
	public List<Task> cancelTaskList(String empId,String isActive) throws Exception {
		List<Task> listTask = taskRepository.cancelTaskList(empId,isActive);
		try {
			if (Objects.nonNull(listTask)) {
				return listTask;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display cancel task list " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	/* List of Save and Update Task */
	@Override
	public Task saveTask(Task task) throws Exception {
		String loginId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			 loginId = authentication.getName();
			if (Objects.nonNull(loginId)) {
				EmpBasicDetails userDetails = empBasicRepository.getCurrentUser(loginId,ProdConstant.TRUE);
				System.out.println("TenantId:::::::::"+ userDetails.getTenantId());
			if (Objects.isNull(task.getId())) {
				task.setIsActive(ProdConstant.TRUE);
				task.setCreatedDate(checkOutDaoImpl.getDateTime());
				task.setStatus(ProdConstant.PENDING);
				task.setManagerId(loginId);
				task.setCreatedBy(loginId);	
				task.setTenantId(userDetails.getTenantId());
				taskRepository.save(task);
			} else {
				Optional<Task> task1 = taskRepository.findById(task.getId());
				if (task1.isPresent()) {
					Task newTask = task1.get();
					newTask.setEmpId(task.getEmpId());
					newTask.setProjects(task.getProjects());
					newTask.setHours(task.getHours());
					newTask.setDescription(task.getDescription());
					newTask.setDate(new SimpleDateFormat().format(checkOutDaoImpl.getDate()));
					newTask.setLastModifiedDate(checkOutDaoImpl.getDateTime());
					newTask.setLastModifiedBy(loginId);
					taskRepository.save(newTask);
				} else {
					return task;
				}
			}
			}else {
				
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get save and update list " + ExceptionUtils.getStackTrace(e));
		}
		return task;
	}

	/* Get Task By Manager */
	@Override
	public Task getTaskById(Long id) throws Exception {
		Optional<Task> optional = taskRepository.findById(id);
		Task task = new Task();
		try {
			if (optional.isPresent()) {
				task = optional.get();
			} else {
				return task;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get Task by empId " + ExceptionUtils.getStackTrace(e));
		}
		return task;
	}

	/* List for Complete Task... */
	@Override
	public List<Task> completedTask(String empId, String isActive,String status) throws Exception {
		List<Task> listTask = new ArrayList<>();
		try {
			listTask = taskRepository.completedTask(empId,isActive,status);
		} catch (Exception e) {
			LOGGER.error("Error occur while complete Task activity by Id " + ExceptionUtils.getStackTrace(e));
		}
		return listTask;
	}

	/* List for DashBoard Task.... */
	@Override
	public List<Task> dashboardTask(String empId,String isActive) throws Exception {
		List<Task> listTask = new ArrayList<>();
		try {
			listTask = taskRepository.dashboardTask(empId,isActive);
		} catch (Exception e) {
			LOGGER.error("Error occur while display dashboardTask by empId " + ExceptionUtils.getStackTrace(e));
		}
		return listTask;
	}
}