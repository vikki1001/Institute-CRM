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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.SMSTemplate;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.SMSService;
import com.ksv.ktrccrm.service.UserService;

@Controller
public class SmsTemplateController {
	private static final Logger LOGGER = LogManager.getLogger(SmsTemplateController.class);

	@Autowired
	private SMSService smsService;
	@Autowired
	private AuditRecordService auditRecordService;
	@Autowired
	private UserService userService;

	@Value("${global.redirectmsg.save}")
	private String successSaveMsg;

	@Value("${global.redirect.update}")
	private String successUpdateMsg;

	@GetMapping(value = "/activeSMSList")
	public String getIsActiveSMS(Model model, Device device) throws Exception {
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
					List<SMSTemplate> smsTemplates = smsService.getRecordList(ProdConstant.TRUE, tenantId);
					if (Objects.nonNull(smsTemplates)) {
						model.addAttribute("listSMS", smsTemplates);
					}
				} else {
					System.out.println("TenantId is null  ");
				}
			}

		} catch (Exception e) {
			LOGGER.error("Error occur to display active sms list " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed active SMS list"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Sms Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "sms/activesmslist";
	}

	@GetMapping(value = "/inActiveSMSList")
	public String getInActiveSMS(Model model, Device device) throws Exception {
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
					List<SMSTemplate> smsTemplates = smsService.getRecordList(ProdConstant.FALSE, tenantId);
					if (Objects.nonNull(smsTemplates)) {
						model.addAttribute("listSMS", smsTemplates);
					}
				} else {
					System.out.println("TenantId is null  ");
				}
			}

		} catch (Exception e) {
			LOGGER.error("Error occur to display inActive sms list " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed inActive SMS list"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Sms Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "sms/inactivesmslist";
	}

	// @GetMapping(value = "/SMSTemplate")
	@RequestMapping(value = { "/SMSTemplate", "/SMSUpdate/{id}" }, method = RequestMethod.GET)
	public String smsTemplate(@PathVariable(name = "id", required = false) Long id, Model model, Device device)
			throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			SMSTemplate smsTemplate = new SMSTemplate();
			if (id != null) {
				smsTemplate = smsService.getById(id);
				if (Objects.nonNull(smsTemplate)) {
					model.addAttribute("smsAttribute", smsTemplate);
				}
			} else {
				model.addAttribute("smsAttribute", smsTemplate);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur to open sms registration page " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed sms management form"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Sms Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "sms/newsmstemplate";
	}

	@PostMapping(value = "/saveSMS")
	public String successSMS(@ModelAttribute("smsAttribute") SMSTemplate smsTemplate, BindingResult result, Model model,
			Device device, RedirectAttributes redirAttrs) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			if (result.hasErrors()) {
				List<ObjectError> allErrors = result.getAllErrors();
				for (ObjectError temp : allErrors) {
					System.out.println(temp);
				}
				return "email/newsmstemplate";
			} else {
				/* Save Sms to Database */
				if (Objects.nonNull(smsTemplate)) {
					smsService.save(smsTemplate);
					redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while sms registration successfully " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - create sms successfully"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Sms Management");
			auditRecord.setActivityCode("CREATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeSMSList";
	}

//	@GetMapping(value = "/SMSUpdate/{id}")
//	public ModelAndView emailUpdate(@PathVariable(name = "id") long id, Device device) throws Exception {
//		AuditRecord auditRecord = new AuditRecord();
//		ModelAndView mav = new ModelAndView("sms/newsmstemplate");
//		try {
//			SMSTemplate smsTemplate = smsService.getById(id);
//			if (Objects.nonNull(smsTemplate)) {
//				mav.addObject("smsAttribute", smsTemplate);
//			}
//		} catch (Exception e) {
//			LOGGER.error("Error occur while display edit registered page " + ExceptionUtils.getStackTrace(e));
//		} finally {
//			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed sms id " + id));
//			auditRecord.setMenuCode("User Administrator");
//			auditRecord.setSubMenuCode("Sms Management");
//			auditRecord.setActivityCode("VIEW");
//			auditRecordService.save(auditRecord, device);
//		}
//		return mav;
//	}

//	@GetMapping(value = "/SMSUpdate/{id}")
//	public ModelAndView emailUpdate(@PathVariable(name = "id") Long id, Device device) throws Exception {
//		AuditRecord auditRecord = new AuditRecord();
//		ModelAndView mav = new ModelAndView("sms/updatesmstemplate");
//		try {
//			SMSTemplate smsTemplate = smsService.getById(id);
//			if (Objects.nonNull(smsTemplate)) {
//				mav.addObject("updateSMSAttribute", smsTemplate);
//			}
//		} catch (Exception e) {
//			LOGGER.error("Error occur while display edit registered page " + ExceptionUtils.getStackTrace(e));
//		} finally {
//			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed sms id " + id));
//			auditRecord.setMenuCode("User Administrator");
//			auditRecord.setSubMenuCode("Sms Management");
//			auditRecord.setActivityCode("VIEW");
//			auditRecordService.save(auditRecord, device);
//		}
//		return mav;
//	}

//	@PostMapping(value = "/updateSMS")
//	public String updateEmail(@ModelAttribute("updateSMSAttribute") SMSTemplate smsTemplate, BindingResult result,
//			Model model, Device device, RedirectAttributes redirAttrs) throws Exception {
//		AuditRecord auditRecord = new AuditRecord();
//		try {
//			if (result.hasErrors()) {
//				List<ObjectError> allErrors = result.getAllErrors();
//				for (ObjectError temp : allErrors) {
//					System.out.println(temp);
//				}
//				return "sms/updatesmstemplate";
//			} else {
//				/* Save Sms to Database */
//				smsService.save(smsTemplate);
//				redirAttrs.addFlashAttribute(ProdConstant.SUCCESS,successUpdateMsg);
//			}
//		} catch (Exception e) {
//			LOGGER.error("Error occur while update sms registration successfully " + ExceptionUtils.getStackTrace(e));
//		} finally {
//			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//			auditRecord.setRemarks(userDetails.getUsername().concat(" - update registered sms successfully"));
//			auditRecord.setMenuCode("User Administrator");
//			auditRecord.setSubMenuCode("Sms Management");
//			auditRecord.setActivityCode("UPDATE");
//			auditRecordService.save(auditRecord, device);
//		}
//		return "redirect:/activeSMSList";
//	}

	@GetMapping(value = "/deactiveSMS/{id}")
	public String deactiveSMS(@PathVariable(name = "id") Long id, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			SMSTemplate smsTemplate = smsService.SMSEnableAndDisable(id);
			if (Objects.nonNull(smsTemplate)) {
				return "redirect:/activeSMSList";
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while inactivate sms " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - deactivate sms by sms id - " + id));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Sms Management");
			auditRecord.setActivityCode("DEACTIVATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/inActiveSMSList";
	}

	@GetMapping(value = "/activeSMS/{id}")
	public String activeSMS(@PathVariable(name = "id") Long id, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			SMSTemplate smsTemplate = smsService.SMSEnableAndDisable(id);
			if (Objects.nonNull(smsTemplate)) {
				return "redirect:/inActiveSMSList";
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while activate sms " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - activate sms by sms id - " + id));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Sms Management");
			auditRecord.setActivityCode("ACTIVATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeSMSList";
	}

}