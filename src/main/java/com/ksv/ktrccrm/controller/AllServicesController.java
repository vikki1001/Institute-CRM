package com.ksv.ktrccrm.controller;

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

import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.service.AuditRecordService;

@Controller
public class AllServicesController {
	private static final Logger LOGGER = LogManager.getLogger(AllServicesController.class);

	@Autowired
	private AuditRecordService auditRecordService;

	@GetMapping(value = "/allServices")
	public String allServices(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {

		} catch (Exception e) {
			LOGGER.error("Error occur while display all service " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed audit record list"));
			auditRecord.setMenuCode("Audit Record");
			auditRecord.setSubMenuCode("Audit Listing Report");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "allServices";
	}

}
