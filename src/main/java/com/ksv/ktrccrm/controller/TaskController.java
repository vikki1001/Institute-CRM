package com.ksv.ktrccrm.controller;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.impl.CheckInCheckOutDaoImpl;
import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.Task;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.EmpRegistartionService;
import com.ksv.ktrccrm.service.TaskService;
import com.ksv.ktrccrm.service.UserService;

@Controller
public class TaskController {
	private static final Logger LOGGER = LogManager.getLogger(TaskController.class);

	@Autowired
	private TaskService taskService;
	@Autowired
	private EmpRegistartionService empRegistartionService;
	@Autowired
	private AuditRecordService auditRecordService;
	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;
	@Autowired
	private UserService userService;

	@Value("${global.redirectmsg.save}")
	private String successSaveMsg;

	@Value("${global.redirect.update}")
	private String successUpdateMsg;

	/* All Emp Weekly Task Display to Task Assigner */
	@GetMapping(value = "/weeklyDataList")
	public String weeklyDataList(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId = null;
		String tenantId = null;
		UserMst user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			empId = authentication.getName();
			if (Objects.nonNull(empId)) {
				user = userService.getUserById(empId);
				tenantId = user.getTenantId();
				if (Objects.nonNull(tenantId)) {
					if (Objects.nonNull(empId)) {
						List<Task> weeklyTask = taskService.weeklyTask(empId, ProdConstant.TRUE, tenantId);
						if (Objects.nonNull(weeklyTask)) {
							model.addAttribute("weeklyTaskList", weeklyTask);
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display weekly data list " + ExceptionUtils.getStackTrace(e));
		} finally {

			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed weekly data list"));
			auditRecord.setMenuCode("Employee Management");
			auditRecord.setSubMenuCode("Task");
			auditRecord.setActivityCode("VIEW");
		}
		return "task/weeklyAssinList";
	}

	@GetMapping(value = "/completeTask/{id}")
	public String completeTask(@PathVariable(name = "id") Long id, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			taskService.completeTask(id);
		} catch (Exception e) {
			LOGGER.error("Error occur while complete task " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - complete task login id - " + id));
			auditRecord.setMenuCode("Employee Management");
			auditRecord.setSubMenuCode("Task");
			auditRecord.setActivityCode("COMPLETE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/taskAssignList";
	}

	/* For Complete Task List */
	@GetMapping(value = "/completedTaskList")
	public String completeTaskList(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();
			if (Objects.nonNull(empId)) {
				List<Task> completeTask = taskService.completedTask(empId, ProdConstant.TRUE, ProdConstant.COMPLETED);
				if (Objects.nonNull(completeTask)) {
					model.addAttribute("completedTask", completeTask);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur to display task list " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed complete task list"));
			auditRecord.setMenuCode("Employe Management");
			auditRecord.setSubMenuCode("Task");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "task/completedTaskList";
	}

	/* Cancel Registered Task */
	@GetMapping(value = "/cancelTask/{id}")
	public String cancelTask(@PathVariable(name = "id") Long id, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			taskService.cancelTask(id);
		} catch (Exception e) {
			LOGGER.error("Error occur while cancel registered task " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - cancel task login id - " + id));
			auditRecord.setMenuCode("Employee Management");
			auditRecord.setSubMenuCode("Task");
			auditRecord.setActivityCode("CANCEL");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/cancelTaskList";
	}

	/* List of Cancel Task */
	@GetMapping(value = "/cancelTaskList")
	public String cancelTaskList(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId = null;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();
			List<Task> userDetails = null;
			if (empId != null) {
				userDetails = taskService.cancelTaskList(empId, ProdConstant.FALSE);
				if (userDetails != null) {
					model.addAttribute("cancelTask", userDetails);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur to display cancel task list " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed cancel task list"));
			auditRecord.setMenuCode("Employee Management");
			auditRecord.setSubMenuCode("Task");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "task/cancelTaskList";

	}

	/* Task Home Page */
	@GetMapping(value = "/task")
	public String task(@ModelAttribute Task task, Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId = null;
		String tenantId = null;
		UserMst user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			empId = authentication.getName();
			if (Objects.nonNull(empId)) {
				user = userService.getUserById(empId);
				tenantId = user.getTenantId();
				if (Objects.nonNull(tenantId)) {
					List<EmpBasicDetails> empIdList = empRegistartionService.getRecordList(ProdConstant.TRUE, tenantId);
					if (Objects.nonNull(empIdList)) {
						model.addAttribute("empIdList", empIdList);
						EmpBasicDetails userDetails = null;
						if (empId != null) {
							userDetails = empRegistartionService.getCurrentUser(empId);
							if (userDetails != null) {
								model.addAttribute("taskList", userDetails);
							}
						}
					}
				} else {
					System.out.println("empIdList object is null ");
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur to get task page " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed task form"));
			auditRecord.setMenuCode("Employee Management");
			auditRecord.setSubMenuCode("Task");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "task/task";
	}

	/* save task data */
	@PostMapping(value = "/saveTask")
	public String successTask(@ModelAttribute Task task, BindingResult result, Model model, Device device,
			RedirectAttributes redirAttrs) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId = null;
		String tenantId = null;
		UserMst user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			empId = authentication.getName();
			if (Objects.nonNull(empId)) {
				user = userService.getUserById(empId);
				tenantId = user.getTenantId();
				if (Objects.nonNull(tenantId)) {
					List<EmpBasicDetails> empIdList = empRegistartionService.getRecordList(ProdConstant.TRUE, tenantId);
					if (Objects.nonNull(empIdList)) {

						model.addAttribute("empIdList", empIdList);

						empId = authentication.getName();
						EmpBasicDetails userDetails = null;
						if (empId != null) {
							userDetails = empRegistartionService.getCurrentUser(empId);
							if (userDetails != null) {
								model.addAttribute("taskList", userDetails);
							}
						}
						/* Save tasks to Database */
						if (Objects.nonNull(task)) {
							taskService.saveTask(task);
							redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
						}
					} else {
						System.out.println("empIdList object is null ");
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while save task " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - create task in employee management"));
			auditRecord.setMenuCode("Employee Management");
			auditRecord.setSubMenuCode("Task");
			auditRecord.setActivityCode("CREATE");
		}
		return "redirect:/taskAssignList";

	}

	/* Update Task Data */
	@GetMapping(value = "/taskUpdate/{id}")
	public ModelAndView taskUpdate(@PathVariable(name = "id") Long id, Device device) throws Exception {
		ModelAndView mav = new ModelAndView("task/task");
		AuditRecord auditRecord = new AuditRecord();
		String empId = null;
		String tenantId = null;
		UserMst user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			empId = authentication.getName();
			if (Objects.nonNull(empId)) {
				user = userService.getUserById(empId);
				tenantId = user.getTenantId();
				if (Objects.nonNull(tenantId)) {
					List<EmpBasicDetails> empIdList = empRegistartionService.getRecordList(ProdConstant.TRUE, tenantId);
					if (Objects.nonNull(empIdList)) {
						mav.addObject("empIdList", empIdList);

						Task task = taskService.getTaskById(id);
						if (Objects.nonNull(task)) {
							mav.addObject("task", task);
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display edit task " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed update task - " + id));
			auditRecord.setMenuCode("Employee management");
			auditRecord.setSubMenuCode("Task");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return mav;
	}

	/* Update Task */
	@PostMapping(value = "/updateTask")
	public String updateTask(@ModelAttribute Task task, BindingResult result, Model model, Device device,
			RedirectAttributes redirAttrs) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId = null;
		String tenantId = null;
		UserMst user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			empId = authentication.getName();
			if (Objects.nonNull(empId)) {
				user = userService.getUserById(empId);
				tenantId = user.getTenantId();
				if (Objects.nonNull(tenantId)) {
					List<EmpBasicDetails> empIdList = empRegistartionService.getRecordList(ProdConstant.TRUE, tenantId);
					if (Objects.nonNull(empIdList)) {
						model.addAttribute("empIdList", empIdList);
						empId = authentication.getName();
						EmpBasicDetails userDetails = null;
						if (empId != null) {
							userDetails = empRegistartionService.getCurrentUser(empId);
							if (userDetails != null) {
								model.addAttribute("taskList", userDetails);
								model.addAttribute("currentDate", checkOutDaoImpl.getDateTime());
							}
						}
						/* Save tasks to Database */
						taskService.saveTask(task);
						redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successUpdateMsg);
					} else {
						System.out.println("empIdList object is null ");
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while update task " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - update task in employee management"));
			auditRecord.setMenuCode("Employee Management");
			auditRecord.setSubMenuCode("Task");
			auditRecord.setActivityCode("UPDATE");
		}
		return "redirect:/taskAssignList";

	}

	/* Employee Task List Assign by manager */
	@GetMapping(value = "/taskAssignList")
	public String activeTaskList(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId = null;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();
			List<Task> userDetails = null;
			if (empId != null) {
				userDetails = taskService.taskAssign(empId, ProdConstant.TRUE);
				if (userDetails != null) {
					model.addAttribute("taskList", userDetails);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur to display assign task list " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed active task list"));
			auditRecord.setMenuCode("Employee Management");
			auditRecord.setSubMenuCode("Task");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "task/taskAssignList";
	}

	/* for dashBoard task */
	@GetMapping(value = "/dashboardTask")
	public String dashboardTask(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();
			List<Task> userDetails = null;
			if (empId != null) {
				userDetails = taskService.dashboardTask(empId, ProdConstant.TRUE);
				if (userDetails != null) {
					model.addAttribute("dashboardTaskList", userDetails);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur to display dashboard task list " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed dashboard task"));
			auditRecord.setMenuCode("Employe Management");
			auditRecord.setSubMenuCode("Task");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "task/dashboardTask";
	}
}