package com.ksv.ktrccrm.controller;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.SearchBoxService;

@Controller
public class SearchBoxController {
	private static final Logger LOGGER = LogManager.getLogger(SearchBoxController.class);

	@Autowired
	private SearchBoxService searchBoxService;
	@Autowired
	private AuditRecordService auditRecordService;

	@GetMapping("/detail/{value}")
	public String showIndex(@PathVariable("value") String value, Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			List<EmpBasicDetails> empBasicDetails = searchBoxService.getSearchEmployeeByFullName(value);
			if (Objects.nonNull(empBasicDetails)) {
				model.addAttribute("allEmployee", empBasicDetails);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display employee profile by employee id " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed employee profile"));
			auditRecord.setMenuCode("Global");
			auditRecord.setSubMenuCode("Search");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "employee/allEmployee";
	}

	@GetMapping("/search")
	public @ResponseBody ModelAndView search(@RequestParam("value") String value, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("fragments/searchFragment");
			if(value != null && !value.isEmpty()) {
				List<EmpBasicDetails> empBasicDetails = searchBoxService.getSearchEmployeeByFullName(value);
				if (Objects.nonNull(empBasicDetails)) {
					mv.addObject("empBasicDetails", empBasicDetails);
					mv.addObject("viewAll", value);
				}
			}
			
		} catch (Exception e) {
			LOGGER.error("Error occur while search employee details " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - search employee "));
			auditRecord.setMenuCode("Global");
			auditRecord.setSubMenuCode("Search");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return mv;
	}
}