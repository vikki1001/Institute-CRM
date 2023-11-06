package com.ksv.ktrccrm.controller;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.db1.entities.DailyWorkReport;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.ProjectDetails;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.DailyWorkReportService;
import com.ksv.ktrccrm.service.EmpRegistartionService;
import com.ksv.ktrccrm.service.ProjectDetailsService;
import com.ksv.ktrccrm.service.UserService;

@Controller
public class DailyWorkReportController {
	private static final Logger LOGGER = LogManager.getLogger(DailyWorkReportController.class);

	@Autowired
	private DailyWorkReportService dailyWorkReportService;
	@Autowired
	private EmpRegistartionService empRegistartionService; 
	@Autowired
	private ProjectDetailsService projectDetailsService;
	@Autowired
	private UserService userService;
	
	@Value("${global.redirectmsg.save}")
	private String successSaveMsg;
	
	@Value("${global.redirect.update}")
	private String successUpdateMsg;
	
	
	@GetMapping(value = "/activeWorkReport")
	public String activeWorkReport(Model model, Device device) throws Exception {
		String empId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();
			List<DailyWorkReport> userDetails = null;
			if (empId != null) {
				userDetails = dailyWorkReportService.getRecordList(empId,ProdConstant.TRUE);
				if (userDetails != null) {
					model.addAttribute("workReportList", userDetails);

					return "dailyworkreport/activeDailyWorkReportList";
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur to get active work report... " + ExceptionUtils.getStackTrace(e));
		}
		return "dailyworkreport/activeDailyWorkReportList";
	}

	@GetMapping(value = "/inActiveWorkReport")
	public String inActiveWorkReport(Model model, Device device) throws Exception {
		String empId;
		
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();
			List<DailyWorkReport> userDetails = null;
			if (empId != null) {
				userDetails = dailyWorkReportService.getRecordList(empId,ProdConstant.FALSE);
				if (userDetails != null) {
					model.addAttribute("workReportList", userDetails);

					return "dailyworkreport/inActiveDailyWorkReportList";
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur to get inactive work report... " + ExceptionUtils.getStackTrace(e));
		} 
		return "dailyworkreport/inActiveDailyWorkReportList";
	}

	@GetMapping(value = "/cancelWorkReport/{id}")
	public String cancelWorkReport(DailyWorkReport dailyWorkReport, @PathVariable(name = "id") Long id, Model model, Device device) throws Exception {
		String empId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();
			DailyWorkReport userDetails = null;
			if (empId != null) {
				userDetails = dailyWorkReportService.cancelWorkReport(dailyWorkReport);
				if (userDetails != null) {
					model.addAttribute("workReportList", userDetails);

					return "redirect:/activeWorkReport";
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur to get cancel work report... " + ExceptionUtils.getStackTrace(e));
		}
		return "dailyworkreport/activeWorkReport";
	}

	@GetMapping(value = "/dailyWorkReport")
	public String dailyWorkReport(@ModelAttribute("dailyWorkReport") DailyWorkReport dailyWorkReport, Model model,
			Device device) throws Exception {
		String empId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();
			String tenantId = null;
			UserMst user = null;
			if(Objects.nonNull(empId)) {
				user = userService.getUserById(empId);
				tenantId = user.getTenantId();
				if(Objects.nonNull(tenantId)) {
			List<EmpBasicDetails> userDetails = null;
			if (empId != null) {
				userDetails = empRegistartionService.listOfCurrentUser(empId);
				if (userDetails != null) {
					model.addAttribute("empDetailsList", userDetails);				
				} else {
					return "dailyworkreport/dailyWorkReport";
				}
				List<ProjectDetails> projetDetails = projectDetailsService.getRecordList(ProdConstant.TRUE , tenantId);
				if(Objects.nonNull(projetDetails)) {
					model.addAttribute("projectDetails", projetDetails);
				}
			}
				}
			} else {
				return "dailyworkreport/dailyWorkReport";
			}
			
		} catch (Exception e) {
			LOGGER.error("Error occur to get expense page... " + ExceptionUtils.getStackTrace(e));
		}
		return "dailyworkreport/dailyWorkReport";
	}

	@PostMapping(value = "/saveDailyWorkReport")
	public String saveDailyWorkReport(@ModelAttribute("dailyWorkReport") DailyWorkReport dailyWorkReport,
			BindingResult bindingResult, Model model, Device device, RedirectAttributes redirAttrs) throws Exception {
		String empId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();
			List<EmpBasicDetails> userDetails = null;
			if (empId != null) {
				userDetails = empRegistartionService.listOfCurrentUser(empId);
				if (userDetails != null) {
					model.addAttribute("dailyWorkReport", userDetails);
				} else {
					return "dailyworkreport/dailyWorkReport";
				}
			} else {
				return "dailyworkreport/dailyWorkReport";
			}
			if(Objects.nonNull(dailyWorkReport)) {
				dailyWorkReportService.saveWorkReport(dailyWorkReport);
				redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
			}

		} catch (Exception e) {
			LOGGER.error(
					"Error occur while save successfully data of work report ... " + ExceptionUtils.getStackTrace(e));
		}
		return "redirect:/activeWorkReport";
	}

	@GetMapping(value = "/workReportUpdate/{id}")
	public ModelAndView updateWorkReport(@PathVariable(name = "id") Long id, Model model, Device device)
			throws Exception {
		ModelAndView mav = new ModelAndView("dailyworkreport/updateDailyWorkReport");
		String empId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();
			String tenantId = null;
			UserMst user = null;
			if(Objects.nonNull(empId)) {
				user = userService.getUserById(empId);
				tenantId = user.getTenantId();
				if(Objects.nonNull(tenantId)) {
			List<EmpBasicDetails> userDetails = null;
			if (empId != null) {
				userDetails = empRegistartionService.listOfCurrentUser(empId);
				if (userDetails != null) {
					model.addAttribute("empDetailsList", userDetails);
					List<ProjectDetails> projetDetails = projectDetailsService.getRecordList(ProdConstant.TRUE, tenantId);
					if(Objects.nonNull(projetDetails)) {
						model.addAttribute("projectDetails", projetDetails);
					}
				}
			}
				}
			}
			DailyWorkReport dailyWorkReport = dailyWorkReportService.getWorkReportById(id);
			if(Objects.nonNull(dailyWorkReport)) {
				mav.addObject("dailyWorkReport", dailyWorkReport);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while update work report... " + ExceptionUtils.getStackTrace(e));
		}
		return mav;
	}

	@PostMapping(value = "/updateWorkReport")
	public String updateWorkReport(@ModelAttribute("dailyWorkReport") DailyWorkReport dailyWorkReport,
			BindingResult result, Model model, Device device, RedirectAttributes redirAttrs) throws Exception {
		try {
			if (result.hasErrors()) {
				List<ObjectError> allErrors = result.getAllErrors();
				for (ObjectError temp : allErrors) {
					System.out.println(temp);
				}
				return "dailyworkreport/updateDailyWorkReport";
			} else {
				if(Objects.nonNull(dailyWorkReport)) {
					dailyWorkReportService.saveWorkReport(dailyWorkReport);
					redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successUpdateMsg);
					return "redirect:/activeWorkReport";
				}		
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while update daily work report successfully... " + ExceptionUtils.getStackTrace(e));
		}
		return "redirect:/activeWorkReport";
	}

	@GetMapping("/totalWorkReport")
	public String totalWorkReport(Model model) throws Exception {
		List<DailyWorkReport> dailyWorkReports = dailyWorkReportService.getTotalWorkReport();
		try {
			if (Objects.nonNull(dailyWorkReports)) {
				model.addAttribute("totalWorkReport", dailyWorkReports);				
			} else {
				System.out.println("Error occuring while get total work report");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get list of total work report" + ExceptionUtils.getStackTrace(e));
		}
		return "dailyworkreport/totalWorkReport";
	}
}
