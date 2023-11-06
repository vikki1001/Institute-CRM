package com.ksv.ktrccrm.controller;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.HolidayMaster;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.HolidayService;
import com.ksv.ktrccrm.service.UserService;

@Controller
public class HolidayController {
	private static final Logger LOGGER = LogManager.getLogger(HolidayController.class);

	@Autowired
	private HolidayService holidayService;
	@Autowired
	private AuditRecordService auditRecordService;
	@Autowired
	private UserService userService;
	
	@Value("${global.redirectmsg.save}")
	private String successSaveMsg;
	
	/* List of uploaded records */
	@GetMapping(value = "/holidayList")
	public String saveHoliday(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId = null;
		String tenantId = null;
		UserMst user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			empId = authentication.getName();
			if(Objects.nonNull(empId)) {
				user = userService.getUserById(empId);
				tenantId = user.getTenantId();
				System.out.println("getHolidayUpcoming .." + tenantId);
				if(Objects.nonNull(tenantId)) {
			List<HolidayMaster> holidayMasters = holidayService.getHolidayUpcoming(tenantId);
			if(Objects.nonNull(holidayMasters)) {
				model.addAttribute("holidayList", holidayMasters);
			}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur to open holiday list page... " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed holiday list"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Holiday Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "holidaymaster/holidayList";
	}

	/* Holiday Home Page */
	@GetMapping(value = "/holidayForm")
	public String saveHoliday(Model model, HolidayMaster holidayMaste, BindingResult bindingResult, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			if(Objects.nonNull(holidayMaste)) {
				model.addAttribute("holidayMaste", holidayMaste);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur to open holiday form registration page... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed holiday management form"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Holiday Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "holidaymaster/holidaymaster";
	}

	/* Save/Upload record into DB*/
	@PostMapping(value = "/saveHolidayMaster")
	public String save(@RequestParam("file") MultipartFile files, Model model, Device device, RedirectAttributes redirAttrs) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			holidayService.saveAll(files);
			redirAttrs.addFlashAttribute(ProdConstant.SUCCESS,successSaveMsg);
			return "redirect:/holidayList";
		} catch (Exception e) {
			LOGGER.error("Error occur while holiday master registration successfully... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - create holiday successfully"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Holiday Management");
			auditRecord.setActivityCode("CREATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/holidayForm";
	}
	
	/* Download Holiday Excel Format */
	@GetMapping("/downloadHoliday")
	public void downloadHolidayFile(HttpServletResponse response) throws Exception {
		List<HolidayMaster> holidayMaster = holidayService.findAll();
		if(Objects.nonNull(holidayMaster)) {
	        ByteArrayInputStream byteArrayInputStream = holidayService.exportHoliday(holidayMaster);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + ProdConstant.HOLIDAYFILE + new Date() + ProdConstant.EXCELEXTENSION);
        IOUtils.copy(byteArrayInputStream, response.getOutputStream());
		}
		else {
			System.out.println("holidayMaster object is null");
		}
	}
}
