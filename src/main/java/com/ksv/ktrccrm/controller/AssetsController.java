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
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.db1.entities.Assets;
import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.AssetsService;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.UserService;
import com.ksv.ktrccrm.validator.AssetsValidator;

@Controller
public class AssetsController {
	private static final Logger LOGGER = LogManager.getLogger(AssetsController.class);

	@Autowired
	private AssetsValidator assetsValidator;

	@InitBinder
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(assetsValidator);
	}

	@Autowired
	private AssetsService assetsService;
	@Autowired
	private AuditRecordService auditRecordService;

	@Value("${global.redirectmsg.save}")
	private String successSaveMsg;

	@Value("${global.redirectmsg.saveExcel}")
	private String successSaveExcel;
	@Autowired
	private UserService userService;
	
	/* Active Assets List */
	@GetMapping("/activeAssetsList")
	public String getIsActiveAssetsList(Model model, Device device) throws Exception {
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
				
				if(Objects.nonNull(tenantId)) {
			List<Assets> assets = assetsService.getRecordList(ProdConstant.TRUE,tenantId);
			
			for(Assets a : assets) {
				System.out.println(a);
			}
			
			if (Objects.nonNull(assets)) {
				model.addAttribute("assetsList", assets);
			}
	
				}
			}
		
		} catch (Exception e) {
			LOGGER.error("Error occur to display active Assets list " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed active assets list"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Assets Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "assets/activeAssetsList";
	}

	/* InActive Assets List */
	@GetMapping("/inActiveAssetsList")
	public String getInActiveAssetsList(Model model, Device device) throws Exception {
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
				if(Objects.nonNull(tenantId)) {
			List<Assets> assets = assetsService.getRecordList(ProdConstant.TRUE,tenantId);
			if (Objects.nonNull(assets)) {
				model.addAttribute("assetsList", assets);
			}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur to display Inactive Assets list " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed inactive assets list"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Assets Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "assets/inActiveAssetsList";
	}

	/* Add New Assets */
	@GetMapping("/addNewAssets")
	public String addNewAssets(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			Assets assets = new Assets();
			model.addAttribute("assets", assets);
		} catch (Exception e) {
			LOGGER.error("Error occur to new assets page " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed assets form"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Assets Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "assets/newAssets";
	}

	/* Save Assets */
	@PostMapping("/saveAssets")
	public String saveAssets(@Validated @ModelAttribute("assets") Assets assets, BindingResult result, Model model,
			RedirectAttributes redirect, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();

		try {
			
			if (result.hasErrors()) {
				List<ObjectError> allErrors = result.getAllErrors();
				for (ObjectError temp : allErrors) {
					System.out.println(temp);
				}
				return "assets/newAssets";
			} else {
				if (Objects.nonNull(assets)) {
					
					assetsService.saveAssets(assets);
					redirect.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occuring while save & update Assets " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - create assets"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Assets Management");
			auditRecord.setActivityCode("CREATE");
			auditRecordService.save(auditRecord, device);
		}

		return "redirect:/activeAssetsList";

	}

	/* Update Assets Home */
	@GetMapping("/assetsUpdate/{id}")
	public ModelAndView assetsUpdate(@PathVariable(name = "id") Long id, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		ModelAndView mav = new ModelAndView("assets/updateAssets");
		try {
			Assets assets = assetsService.getAssetsById(id);
			if (Objects.nonNull(assets)) {
				mav.addObject("assets", assets);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display assets edit page " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed assets update page"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Assets Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return mav;
	}

	/* Deactivate Assets By Id */
	@GetMapping("deactivateAssets/{id}")
	public String deactivateAssets(@PathVariable(name = "id") Long id, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			assetsService.assetsEnableAndDisable(id);
		} catch (Exception e) {
			LOGGER.error("Error occur while deactivate assets " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - deactivate assets by id" + id));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Assets Management");
			auditRecord.setActivityCode("DEACTIVATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/inActiveAssetsList";
	}

	/* Activate Assets By Id */
	@GetMapping("activateAssets/{id}")
	public String activateAssets(@PathVariable(name = "id") Long id, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			assetsService.assetsEnableAndDisable(id);
		} catch (Exception e) {
			LOGGER.error("Error occur while activate assets " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - activate assets by id" + id));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Assets Management");
			auditRecord.setActivityCode("ACTIVATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeAssetsList";
	}

	/* Assets Display to Pending Employee List */
	@GetMapping("/employeeAssetsList")
	public String employeeAssetsList(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();
			if (empId != null) {
				List<Assets> assetsList = assetsService.getEmpAssetsList(empId);
				if (Objects.nonNull(assetsList)) {
					model.addAttribute("assetsList", assetsList);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display applied assets list ... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - applied assets for employee"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Assets Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "assets/appliedAssetsList";
	}

	/* Assets Display to Approved Employee List */
	@GetMapping("/empApprovedAssetsList")
	public String getEmpApprovedAssetsList(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();
			if (empId != null) {
				List<Assets> assetsList = assetsService.getEmpApprovedAssetsList(empId);
				if (Objects.nonNull(assetsList)) {
					model.addAttribute("assetsList", assetsList);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display approved assets list ... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - employee approved assets list "));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Assets Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "assets/empApprovedAssetsList";
	}

	/* Assets Display to Rejected Employee List */
	@GetMapping("/empRejectAssetsList")
	public String getEmpRejectedAssetsList(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();
			if (empId != null) {
				List<Assets> assetsList = assetsService.getEmpRejectedAssetsList(empId);
				if (Objects.nonNull(assetsList)) {
					model.addAttribute("assetsList", assetsList);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display rejected assets list ... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - employee rejected assets list "));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Assets Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "assets/empRejectedAssetsList";
	}

	/* Assets Request Accept By Employee */
	@GetMapping("/acceptAssetsRequest/{id}")
	public String acceptAssets(@ModelAttribute Assets assetsList, @PathVariable(name = "id") Long id, Model model,
			Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			Assets getAcceptAssets = assetsService.acceptAssets(id);
			if (Objects.nonNull(getAcceptAssets)) {
				model.addAttribute("assetsList", getAcceptAssets);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while accept assets request " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - employee accept assets list by id" + id));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Assets Management");
			auditRecord.setActivityCode("ACCEPTED");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/employeeAssetsList";
	}

	/* Assets Request Reject By Employee */
	@GetMapping("/rejectAssetsRequest/{id}")
	public String rejectAssets(@ModelAttribute Assets assetsList, @PathVariable(name = "id") Long id, Model model,
			Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			Assets getAssets = assetsService.rejectAssets(id);
			if (Objects.nonNull(getAssets)) {
				model.addAttribute("assetsList", getAssets);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while reject assets request " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - employee reject assets list by id" + id));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Assets Management");
			auditRecord.setActivityCode("REJECTED");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/employeeAssetsList";
	}

	/* Add Bulk of Assets By Excel File */
	@GetMapping("/addNewAssetsByExcel")
	public String addNewAssetsByExcel(Model model, Assets assetsExcel, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			model.addAttribute("assetsExcel", assetsExcel);
		} catch (Exception e) {
			LOGGER.error("Error occur to open assets excel form  " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed assets excel form"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Assets Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "assets/assetsAddByExcel";
	}

	/* Save/Upload Assets Excel Record */
	@PostMapping(value = "/saveAssetsExcel")
	public String save(@ModelAttribute("assetsExcel") Assets assetsExcel, @RequestParam("file") MultipartFile files,
			Model model, Device device, RedirectAttributes redirAttrs) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			if (Objects.nonNull(files)) {
				assetsService.saveAll(files);
				redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveExcel);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while to upload assets excel record  " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - upload assets successfully"));
			auditRecord.setMenuCode("User Administrator");
			auditRecord.setSubMenuCode("Assets Management");
			auditRecord.setActivityCode("CREATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeAssetsList";
	}

	/* Download Assets Excel Format */
	@GetMapping("/downloadAssets")
	public void downloadAssetsFile(HttpServletResponse response) throws Exception {
		List<Assets> assets = assetsService.findAll();
		if (Objects.nonNull(assets)) {
			ByteArrayInputStream byteArrayInputStream = assetsService.exportAssets(assets);
			if (Objects.nonNull(byteArrayInputStream)) {
				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition",
						"attachment; filename= " + ProdConstant.ASSESTSFILE + new Date() + ProdConstant.EXCELEXTENSION);
				IOUtils.copy(byteArrayInputStream, response.getOutputStream());
			}
		} else {
			System.out.println("assets objects is null ");
		}
	}

}
