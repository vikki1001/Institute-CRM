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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.db1.entities.ProjectDetails;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.ProjectDetailsService;
import com.ksv.ktrccrm.service.UserService;

@Controller
public class ProjectDetailsController {
	private static final Logger LOGGER = LogManager.getLogger(ProjectDetailsController.class);

	@Autowired
	private ProjectDetailsService projectDetailsService;
	@Autowired
	private UserService userService;

	@GetMapping(value = "/isActiveProjectList")
	public @ResponseBody String isActiveProjectList(Model model, Device device) throws Exception {
		String empId = null;
		String tenantId1 = null;
		UserMst user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			empId = authentication.getName();
			if(Objects.nonNull(empId)) {
				user = userService.getUserById(empId);
				tenantId1 = user.getTenantId();
				if(Objects.nonNull(tenantId1)) {
			List<ProjectDetails> projectName = projectDetailsService.getRecordList(ProdConstant.TRUE, tenantId1);
			for (ProjectDetails proName : projectName) {
				System.out.println("PROJECT NAME PROJECT CONTROLLER:" + proName.toString());
			}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while isActive project list. " + ExceptionUtils.getStackTrace(e));
		}
		return "success";
	}

}
