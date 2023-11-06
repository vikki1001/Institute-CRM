package com.ksv.ktrccrm.controller;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.Slider;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.SliderService;
import com.ksv.ktrccrm.service.UserService;

@Controller
public class SliderController {
	private static final Logger LOGGER = LogManager.getLogger(SliderController.class);

	@Autowired
	private AuditRecordService auditRecordService;
	@Autowired
	private SliderService sliderService;
	@Autowired
	private UserService userService;

	@Value("${global.redirectmsg.save}")
	private String successSaveMsg;

	/* Display List of IsActive Slider list */
	@GetMapping(value = "/activeSliderList")
	public String activeSliderList(Model model, Device device) throws Exception {
		System.out.println("Inside method .........");
		AuditRecord auditRecord = new AuditRecord();
		String empId = null;
		String tenantId = null;
		UserMst user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			empId = authentication.getName();
			System.out.println("Inside method 2........." + empId);
			if (Objects.nonNull(empId)) {
				user = userService.getUserById(empId);
				tenantId = user.getTenantId();
				System.out.println("Inside method 3........." + tenantId);
				if (Objects.nonNull(tenantId)) {
					List<Slider> sliderList = sliderService.getRecordList(ProdConstant.TRUE, tenantId);
					for (Slider slider : sliderList) {
						System.out.println("sliderList.........." + slider.getId());
					}
					if (Objects.nonNull(sliderList)) {
						model.addAttribute("sliderList", sliderList);
					}
				} else {
					System.out.println("TenantId is null  ");
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display active slider list " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed active slider list"));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Slider");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "slider/activeSliderList";
	}

	/* Display List of IsActive Slider list */
	@GetMapping(value = "/inActiveSliderList")
	public String inActiveSliderList(Model model, Device device) throws Exception {
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
					List<Slider> sliderList = sliderService.getRecordList(ProdConstant.FALSE, tenantId);
					if (Objects.nonNull(sliderList)) {
						model.addAttribute("sliderList", sliderList);
					}
				} else {
					System.out.println("TenantId is null  ");
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display active slider list " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed active slider list"));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Slider");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "slider/inActiveSliderList";
	}

	// @GetMapping("/sliderHome")
	@RequestMapping(value = { "/sliderHome", "/sliderUpdate/{id}" }, method = RequestMethod.GET)
	public String sliderHome(@PathVariable(name = "id", required = false) Integer id, Model model, Device device)
			throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			Slider slider = new Slider();
			if (id != null) {
				slider = sliderService.getById(id);
				if (Objects.nonNull(slider)) {
					model.addAttribute("slider", slider);
				}
			} else {
				model.addAttribute("slider", slider);
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while display add slider " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed slider home page"));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Slider");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "slider/slider";
	}

	@PostMapping("/saveSlider")
	public String saveSlider(@ModelAttribute Slider slider, BindingResult result, Model model, Device device,
			@RequestParam("image") MultipartFile file, RedirectAttributes redirAttrs) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			if (Objects.nonNull(slider)) {
				slider.setImage(file.getBytes());
				slider.setImageName(file.getOriginalFilename());
				sliderService.saveSlider(slider);
				redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while create slider " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - create slider in hr management"));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Slider");
			auditRecord.setActivityCode("CREATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeSliderList";
	}

//	@GetMapping("/updateSlider/{id}")
//	public String updateSlider(@PathVariable Integer id, Model model, Device device) throws Exception {
//		AuditRecord auditRecord = new AuditRecord();
//		try {
//			Slider slider = sliderService.getById(id);
//			if (Objects.nonNull(slider)) {
//				model.addAttribute("slider", slider);
//			}
//		} catch (Exception e) {
//			LOGGER.error("Error occuring while display update slider ... " + ExceptionUtils.getStackTrace(e));
//		} finally {
//			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed slider update page"));
//			auditRecord.setMenuCode("HR Management");
//			auditRecord.setSubMenuCode("Slider");
//			auditRecord.setActivityCode("VIEW");
//			auditRecordService.save(auditRecord, device);
//		}
//		return "slider/slider";
//	}

	/* Display Image */
	@GetMapping(value = "/sliderImage")
	public void showImage(@RequestParam("id") Integer id, Model model, HttpServletResponse response) throws Exception {
		try {
			Slider slider = sliderService.getById(id);

			if (slider != null) {
				response.setContentType(ProdConstant.IMAGEFORMATE);
				response.getOutputStream().write(slider.getImage());
				response.getOutputStream().flush();
				response.getOutputStream().close();
			} else {
				System.out.println("Image not get....");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display image  " + ExceptionUtils.getStackTrace(e));
		}
	}

	/* Deactivate of Slider */
	@GetMapping(value = "/deactiveSlider/{id}")
	public String deactiveSlider(@PathVariable(name = "id") Integer id, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			sliderService.sliderEnableAndDisable(id);
		} catch (Exception e) {
			LOGGER.error("Error occur while deactivate slider " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - deactivate slider by id - " + id));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Slider");
			auditRecord.setActivityCode("DEACTIVATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeSliderList";
	}

	/* Activate of Slider */
	@GetMapping(value = "/activeSlider/{id}")
	public String activeSlider(@PathVariable(name = "id") Integer id, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			sliderService.sliderEnableAndDisable(id);
		} catch (Exception e) {
			LOGGER.error("Error occur while activate slider " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - activate slider by id - " + id));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Slider");
			auditRecord.setActivityCode("ACTIVATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/inActiveSliderList";
	}
}
