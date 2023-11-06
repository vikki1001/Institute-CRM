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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.EmailTemplate;
import com.ksv.ktrccrm.db1.entities.TrainingForm;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.EmailAndOTPService;
import com.ksv.ktrccrm.service.EmailService;
import com.ksv.ktrccrm.service.TrainingFormService;
import com.ksv.ktrccrm.service.UserService;

@Controller
public class TrainingFormController {
	private static final Logger LOGGER = LogManager.getLogger(TrainingFormController.class);

	@Autowired
	private TemplateEngine templateEngine;

	public TrainingFormController(TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

	@Autowired
	private EmailAndOTPService emailAndOTPService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private TrainingFormService trainingFormService;
	@Autowired
	private AuditRecordService auditRecordService;
	@Autowired
	private UserService userService;

	@Value("${global.redirectmsg.save}")
	private String successSaveMsg;

	@Value("${global.redirect.update}")
	private String successUpdateMsg;

	@Value("${global.redirecrmsg.emailSend}")
	private String emailSuccess;

	@GetMapping("/activeTrainingList")
	private String activeList(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId = null;
		String tenantId = null;
		UserMst user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userId;
		try {
			empId = authentication.getName();
			if (Objects.nonNull(empId)) {
				user = userService.getUserById(empId);
				tenantId = user.getTenantId();
				if (Objects.nonNull(tenantId)) {
					userId = authentication.getName();
					List<TrainingForm> trainingList = null;
					if (Objects.nonNull(userId)) {
						trainingList = trainingFormService.getRecordList(userId, ProdConstant.TRUE, tenantId);
						if (Objects.nonNull(trainingList)) {
							model.addAttribute("trainingList", trainingList);
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error Occuring While Display Active Training List " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed active training list"));
			auditRecord.setMenuCode("Employee Management");
			auditRecord.setSubMenuCode("Training Form");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "training/activeTrainingList";
	}

	@GetMapping("/inActiveTrainingList")
	private String inActiveList(Model model, Device device) throws Exception {
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
			empId = authentication.getName();
			List<TrainingForm> trainingList = null;
			if (Objects.nonNull(empId)) {
				trainingList = trainingFormService.getRecordList(empId, ProdConstant.FALSE,tenantId);
				if (Objects.nonNull(trainingList)) {
					model.addAttribute("trainingList", trainingList);
				}
			}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error Occuring While Display Isactive Training List " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed inactive training list"));
			auditRecord.setMenuCode("Employee Management");
			auditRecord.setSubMenuCode("Training Form");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "training/inActiveTrainingList";
	}

	//@GetMapping("/trainingForm")
	@RequestMapping(value={"/trainingForm","/trainingForm/{id}"},method = RequestMethod.GET)
	public String trainingPage(@PathVariable(name = "id",required = false) Long id , Model model, Device device) throws Exception {
		
		AuditRecord auditRecord = new AuditRecord();
		try {
			System.out.println("Inside GET METHOD....");
			TrainingForm trainingForm = new TrainingForm();
			if(id != null) {
				System.out.println("Inside IF....");
				 trainingForm = trainingFormService.getById(id);
				if (Objects.nonNull(trainingForm)) {
					model.addAttribute("trainingForm", trainingForm);
				}
			}else {
				System.out.println("Inside ELSE....");
				model.addAttribute("trainingForm", trainingForm);
			}
		} catch (Exception e) {
			LOGGER.error("Error Occuring While Opening Training Home Page " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed training form"));
			auditRecord.setMenuCode("Employee Management");
			auditRecord.setSubMenuCode("Training Form");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "training/trainingForm";
	}

	@PostMapping("/saveTrainingForm")
	private String saveTrainingForm(@ModelAttribute TrainingForm trainingForm, RedirectAttributes redirects,
			Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String userId;
		EmailTemplate emailTemplate = emailService.getEmailTemplate(ProdConstant.TRUE, ProdConstant.TRAININGFORMREQ);
		System.out.println("EMAILTAMPLATE........... " + emailTemplate);
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			userId = authentication.getName();
			if (Objects.nonNull(trainingForm)) {
				trainingFormService.saveUpdate(trainingForm);
				redirects.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);

				/* Training Request Mail Send Project Manager & HR */
				if (emailTemplate != null) {
					Context context = new Context();
					context.setVariable("trainingFormList",
							trainingFormService.getTrainingReq(userId, trainingForm.getSubject(), ProdConstant.TRUE));
					String body = templateEngine.process("training/trainingFormMail", context);
					String from = emailTemplate.getEmailFrom();
					String to = emailTemplate.getEmailTo();
					String subject = emailTemplate.getEmailSub();
					String cc = emailTemplate.getEmailCc();
					emailAndOTPService.emailSend(from, to, subject, body, cc);
					redirects.addFlashAttribute(ProdConstant.SUCCESS, emailSuccess);
				} else {
					System.out.println("Error occur while sending mail:::::::::: ");
				}
			} else {
				System.out.println("Data not Save Successfully");
			}
		} catch (Exception e) {
			LOGGER.error("Error Occuring While save Training data " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - create training form successfully"));
			auditRecord.setMenuCode("Employee Management");
			auditRecord.setSubMenuCode("Training Form");
			auditRecord.setActivityCode("CREATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeTrainingList";
	}

//	@GetMapping("/trainingForm/{id}")
//	public ModelAndView roleUpdate(@PathVariable("id") Long id, Device device) throws Exception {
//		ModelAndView mav = new ModelAndView("training/trainingForm");
//		AuditRecord auditRecord = new AuditRecord();
//		try {
//			TrainingForm trainingForm = trainingFormService.getById(id);
//			if (Objects.nonNull(trainingForm)) {
//				mav.addObject("trainingForm", trainingForm);
//			}
//		} catch (Exception e) {
//			LOGGER.error("Error occured while Opening Edit Trainig Form Page " + ExceptionUtils.getStackTrace(e));
//		} finally {
//			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed by id - " + id));
//			auditRecord.setMenuCode("Organization");
//			auditRecord.setSubMenuCode("Training Request");
//			auditRecord.setActivityCode("VIEW");
//			auditRecordService.save(auditRecord, device);
//		}
//		return mav;
//	}

	@GetMapping("/trainingListForHR")
	private String trainingListForHR(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			List<TrainingForm> trainingList = trainingFormService.getListForHr(ProdConstant.TRUE);
			if (Objects.nonNull(trainingList)) {
				model.addAttribute("trainingList", trainingList);
			}
		} catch (Exception e) {
			LOGGER.error("Error Occuring While Display Training List for HR " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed training list for HR"));
			auditRecord.setMenuCode("Organization");
			auditRecord.setSubMenuCode("Training Request");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "training/trainingList";
	}

	@GetMapping("/trainingListForManager")
	private String trainingListForManager(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String userId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			userId = authentication.getName();
			if (Objects.nonNull(userId)) {
				List<TrainingForm> trainingList = trainingFormService.getListForManger(userId, ProdConstant.TRUE);
				if (Objects.nonNull(trainingList)) {
					model.addAttribute("trainingList", trainingList);
				}
			} else {
				System.out.println("Error occur while display training list for manager");
			}
		} catch (Exception e) {
			LOGGER.error("Error Occuring While Display Training List for Manager " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed training list for Manger"));
			auditRecord.setMenuCode("Organization");
			auditRecord.setSubMenuCode("Training Request");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "training/trainingList";
	}

	/* Deactivate of Training */
	@GetMapping(value = "/deactivetraining/{id}")
	public String deactiveTraining(@PathVariable(name = "id") Long id, Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			TrainingForm trainingForm = trainingFormService.trainingEnableAndDisable(id);
			if (Objects.nonNull(trainingForm)) {
				model.addAttribute("trainingList", trainingForm);
			}
			return "redirect:/activeTrainingList";
		} catch (Exception e) {
			LOGGER.error("Error occur while deactivate training" + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - deactivate training id - " + id));
			auditRecord.setMenuCode("Organization");
			auditRecord.setSubMenuCode("Training Request");
			auditRecord.setActivityCode("DEACTIVATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/inActiveTrainingList";

	}

	/* Activate of Training User */
	@GetMapping(value = "/activetraining/{id}")
	public String activeTraining(@PathVariable(name = "id") Long id, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			TrainingForm trainingForm = trainingFormService.trainingEnableAndDisable(id);
			if (Objects.nonNull(trainingForm)) {
				return "redirect:/inActiveTrainingList";
			}

		} catch (Exception e) {
			LOGGER.error("Error occur while activate training" + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - activate training id - " + id));
			auditRecord.setMenuCode("Organization");
			auditRecord.setSubMenuCode("Training Request");
			auditRecord.setActivityCode("ACTIVATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeTrainingList";
	}

	@GetMapping("/totalTraining")
	public String totalTraining(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		List<TrainingForm> trainingForms = trainingFormService.getTotalTraining(ProdConstant.TRUE);
		try {
			if (Objects.nonNull(trainingForms)) {
				model.addAttribute("totalTrainingForm", trainingForms);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get list of total training form" + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed training list for HR dashboard"));
			auditRecord.setMenuCode("Organization");
			auditRecord.setSubMenuCode("Training Request");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "training/totalTraining";
	}
}