package com.ksv.ktrccrm.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ksv.ktrccrm.dao.TaskDao;
import com.ksv.ktrccrm.db1.entities.Task;
import com.ksv.ktrccrm.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService {
	private static final Logger LOGGER = LogManager.getLogger(TaskServiceImpl.class);

	@Autowired
	private TaskDao taskDao;

	/* All Employee Weekly Task Display to Task Assigner */
	@Override
	public List<Task> weeklyTask(String empId,String isActive, String tenantId) throws Exception {
		List<Task> listTask = new ArrayList<>();
		try {
			listTask = taskDao.weeklyTask(empId,isActive,tenantId);
		} catch (Exception e) {
			LOGGER.error("Error occur while display Weekly Data " + ExceptionUtils.getStackTrace(e));
		}
		return listTask;
	}

	/* List Of Active Task */
	public List<Task> taskAssign(String empId,String isActive) throws Exception {
		List<Task> listTask = new ArrayList<>();
		try {
			listTask = taskDao.taskAssign(empId,isActive);
		} catch (Exception e) {
			LOGGER.error("Error occur while display assign task list " + ExceptionUtils.getStackTrace(e));
		}
		return listTask;
	}

	/* Cancel Registered Task */
	@Override
	public Task cancelTask(Long id) throws Exception {
		Task task = new Task();
		try {
			task = taskDao.cancelTask(id);
		} catch (Exception e) {
			LOGGER.error("Error occur while cancel registered task " + ExceptionUtils.getStackTrace(e));
		}
		return task;
	}

	/* List of Cancel Task .... */
	@Override
	public List<Task> cancelTaskList(String empId,String isActive) throws Exception {
		List<Task> listTask = new ArrayList<>();
		try {
			listTask = taskDao.cancelTaskList(empId,isActive);
		} catch (Exception e) {
			LOGGER.error("Error occur while display cancel task list " + ExceptionUtils.getStackTrace(e));
		}
		return listTask;
	}

	/* Save Task.... */
	@Override
	public Task saveTask(Task task) throws Exception {
		try {
			if (Objects.nonNull(task)) {
				this.taskDao.saveTask(task);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while save Task list " + ExceptionUtils.getStackTrace(e));
		}
		return task;
	}

	/* Get Task By Manager */
	@Override
	public Task getTaskById(Long id) throws Exception {
		Task task = new Task();
		try {
			task = taskDao.getTaskById(id);
		} catch (Exception e) {
			LOGGER.error("Error occur while get Task activity by Id " + ExceptionUtils.getStackTrace(e));
		}
		return task;
	}

	/* Complete Task */
	@Override
	public Task completeTask(Long id) throws Exception {
		Task task = new Task();
		try {
			task = taskDao.completeTask(id);
		} catch (Exception e) {
			LOGGER.error("Error occur while complete task " + ExceptionUtils.getStackTrace(e));
		}
		return task;
	}

	/* List for Complete Task... */
	@Override
	public List<Task> completedTask(String empId, String isActive,String status) throws Exception {
		List<Task> listTask = new ArrayList<>();
		try {
			listTask = taskDao.completedTask(empId,isActive, status);
		} catch (Exception e) {
			LOGGER.error("Error occur while get complete Task list " + ExceptionUtils.getStackTrace(e));
		}
		return listTask;
	}

	/* List for DashBoard Task.... */
	@Override
	public List<Task> dashboardTask(String empId,String isActive) throws Exception {
		List<Task> listTask = new ArrayList<>();
		try {
			listTask = taskDao.dashboardTask(empId,isActive);
		} catch (Exception e) {
			LOGGER.error("Error occur while display dashboardTask by Id " + ExceptionUtils.getStackTrace(e));
		}
		return listTask;
	}
}