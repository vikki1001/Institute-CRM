package com.ksv.ktrccrm.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.UploadDocument;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.UploadDocService;
import com.ksv.ktrccrm.service.UserService;

@Controller
public class UploadDocController {
	private static final Logger LOGGER = LogManager.getLogger(UploadDocController.class);

	@Value("${uploadDocDir}")
	private String uploadFolder;

	@Autowired
	private UploadDocService uploadDocService;

	@Autowired
	private AuditRecordService auditRecordService;

	@Autowired
	private UserService userService;

	@Value("${global.redirectmsg.save}")
	private String successSaveMsg;

	@Value("${global.redirect.update}")
	private String successUpdateMsg;

	/* Upload Document List in Service */
	@GetMapping("/documentList")
	public String documentList(Model model, Device device) throws Exception {
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
					List<UploadDocument> uploadDoc = uploadDocService.getRecordList(ProdConstant.TRUE, tenantId);

					if (Objects.nonNull(uploadDoc)) {
						model.addAttribute("documentList", uploadDoc);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur to display upload document list " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed upload document list"));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("All Service");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "document/empDocList";
	}

	/* Active Upload Document List */
	@GetMapping("/activeDocList")
	public String activeDocumentList(Model model, Device device) throws Exception {
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
					List<UploadDocument> uploadDoc = uploadDocService.getRecordList(ProdConstant.TRUE, tenantId);
					if (Objects.nonNull(uploadDoc)) {
						model.addAttribute("docList", uploadDoc);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur to display upload active document list " + ExceptionUtils.getStackTrace(e));
		} finally {

			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed active upload document list"));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Upload Document");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "document/activeDocList";
	}

	/* Deactive Upload Document List */
	@GetMapping("/inActiveDocList")
	public String inactiveDocumentList(Model model, Device device) throws Exception {
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
					List<UploadDocument> uploadDoc = uploadDocService.getRecordList(ProdConstant.FALSE, tenantId);
					if (Objects.nonNull(uploadDoc)) {
						model.addAttribute("docList", uploadDoc);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur to display upload deactive document list " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed deactive upload document list"));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Upload Document");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "document/inActiveDocList";
	}

	/* Upload Registration Page */
	@GetMapping("/getUploadDoc")
	public String uploadDocPage(UploadDocument uploadDoc, Model model) {
		try {
			if (Objects.nonNull(uploadDoc)) {
				model.addAttribute("uploadDoc", uploadDoc);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur to display upload document registration page " + ExceptionUtils.getStackTrace(e));
		}
		return "document/uploadDocument";
	}

	/* New Upload Doc Save */
	@PostMapping("/uploadDoc")
	public String uploadDocument(@ModelAttribute("uploadDoc") UploadDocument uploadDoc, BindingResult result,
			Device device, @RequestParam("file") MultipartFile file, RedirectAttributes redirAttrs) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			if (uploadDoc != null) {
				String fileName = StringUtils.cleanPath(uploadDoc.getTemplateName());
				uploadDoc.setTemplateName(fileName);
				uploadDoc.setOriginalName(file.getOriginalFilename());
				uploadDoc.setFile(file.getBytes());
				uploadDoc.setFileType(file.getContentType());
				uploadDoc.setSize(file.getSize());
				String originalFileName = uploadDoc.getOriginalName();
				uploadDoc.setExtension(originalFileName.substring(originalFileName.lastIndexOf(".") + 1));
				uploadDocService.saveUploadDoc(uploadDoc);
				redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);

			}
		} catch (Exception e) {
			LOGGER.error("Error occur while upload document successfully " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - upload document succesfully"));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Upload Document");
			auditRecord.setActivityCode("CREATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeDocList";
	}

	/* Edit Upload Documents(Registration) */
	@GetMapping(value = "/uploadDocUpdate/{id}")
	public ModelAndView uploadDocUpdate(@PathVariable(name = "id") Long id, Device device) throws Exception {
		ModelAndView mav = new ModelAndView("document/updateDocument");
		AuditRecord auditRecord = new AuditRecord();
		try {
			UploadDocument uploadDocument = uploadDocService.findDocById(id);
			if (Objects.nonNull(uploadDocument)) {
				mav.addObject("uploadDoc", uploadDocument);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display edit registration page " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed by id - " + id));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Upload Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return mav;
	}

	/* Edit Upload Doc Save */
	@PostMapping(value = "/updateDocument")
	public String updateUpdateDocument(@ModelAttribute("uploadDoc") UploadDocument uploadDocument, BindingResult result,
			Model model, Device device, RedirectAttributes redirAttrs, @RequestParam("file") MultipartFile file)
			throws Exception {
		AuditRecord auditRecord = new AuditRecord();

		try {
			System.out.println("Document update :::::" + uploadDocument.getTenantId());
			if (uploadDocument != null) {
				/* Save Upload Doc to Database */
				String fileName = StringUtils.cleanPath(uploadDocument.getTemplateName());
				uploadDocument.setTemplateName(fileName);
				uploadDocument.setOriginalName(file.getOriginalFilename());
				uploadDocument.setFile(file.getBytes());
				uploadDocument.setFileType(file.getContentType());
				uploadDocument.setSize(file.getSize());
				String originalFileName = uploadDocument.getOriginalName();
				uploadDocument.setExtension(originalFileName.substring(originalFileName.lastIndexOf(".") + 1));
				uploadDocService.saveUploadDoc(uploadDocument);
				redirAttrs.addFlashAttribute(ProdConstant.SUCCESS, successUpdateMsg);
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while update Upload registration successfully " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - update upload document successfully"));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Update Document");
			auditRecord.setActivityCode("UPDATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeDocList";
	}

	/* Documents Download */
	@GetMapping("/downloadfile")
	public void downloadFile(@Param("id") Long id, Model model, HttpServletResponse response) throws Exception {
		UploadDocument uploadDocument = uploadDocService.findDocById(id);
		if (uploadDocument != null) {
			String fileExtension = uploadDocument.getOriginalName();
			response.setContentType("application/octet-stream");
			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename = " + uploadDocument.getTemplateName() + "."
					+ fileExtension.substring(fileExtension.lastIndexOf(".") + 1);
			response.setHeader(headerKey, headerValue);
			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(uploadDocument.getFile());
			outputStream.close();
		}
	}

	/* Documents Display */
	@GetMapping("/document")
	public void showDocument(@RequestParam("id") Long id, HttpServletRequest request, HttpServletResponse response,
			Model model) throws Exception {
		try {

//			String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
//			LOGGER.info("UPLOADDIRECTORY ----- " + uploadDirectory);
//			
//			Path currentWorkingDir = Paths.get("").toAbsolutePath();
//			LOGGER.info("CURRENTWORKINGDIR  ------ " + currentWorkingDir.normalize().toString());

			UploadDocument uploadDocument = uploadDocService.findDocById(id);

			String fileType = uploadDocument.getFileType();

			BufferedImage img;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			if (Objects.equals(fileType, ProdConstant.EXCELFILEFORMAT)) {

//				String configFilePath = "";
//				System.out.println("configFilePath  === " + configFilePath);
//
//				Path parentDir = Paths.get("xls.png");
//				System.out.println("Path ............. "+ parentDir.toAbsolutePath());
//				System.out.println("parentDir :::::::::: " + parentDir);
//
//				File currentDirectory = new File(new File("xls.png").getAbsolutePath());
//				System.out.println("currentDirectory  ';;;;  " + currentDirectory);
//
//				FileSystem fileSystem = parentDir.getFileSystem();
//				System.out.println("fileSystem :::::::  " + fileSystem.getRootDirectories());
//
//				fileSystem.getRootDirectories();
//				System.out.println("fileSystem rootdirectory:::::::  " + fileSystem.getRootDirectories());
//
//				String strPath = parentDir.toAbsolutePath().toString();
//				System.out.println("STRPATH ------- " + strPath);
//				
//				 String userDirectory = System.getProperty("user.dir");
//			        System.out.println("userDirectory.." + userDirectory);
//			        
//				for (Path rootDirectories : fileSystem.getRootDirectories()) {
//
//					configFilePath = configFilePath.concat(rootDirectories.toAbsolutePath().toString()
//							.concat(File.separator).concat(strPath).concat(File.separator).concat("xls.png"));
//					File configFile = new File(configFilePath);
//
//					System.out.println("rootDirectories :::::: " + rootDirectories.toAbsolutePath().toString());
//
//					System.out.println("configFile :::::: " + configFile.getAbsolutePath());
//
//				}
//				img = ImageIO.read(currentDirectory);

				img = ImageIO.read(new File(uploadFolder + "/xls.png"));
				ImageIO.write(img, "png", baos);
				baos.flush();
				byte[] bytes = baos.toByteArray();
				baos.close();

				uploadDocument.setFileImage(bytes);
			}

			if (Objects.equals(fileType, ProdConstant.WORDFILEFORMAT)) {
				img = ImageIO.read(new File(uploadFolder + "/doc.png"));
				ImageIO.write(img, "png", baos);
				baos.flush();
				byte[] bytes = baos.toByteArray();
				baos.close();

				uploadDocument.setFileImage(bytes);
			}

			if (Objects.equals(fileType, ProdConstant.PDFFILEFORMAT)) {
				img = ImageIO.read(new File(uploadFolder + "/pdf.png"));
				ImageIO.write(img, "png", baos);
				baos.flush();
				byte[] bytes = baos.toByteArray();
				baos.close();

				uploadDocument.setFileImage(bytes);
			}

			if (Objects.equals(fileType, ProdConstant.TEXTFILEFORMAT)) {
				img = ImageIO.read(new File(uploadFolder + "/txt.png"));
				ImageIO.write(img, "png", baos);
				baos.flush();
				byte[] bytes = baos.toByteArray();
				baos.close();

				uploadDocument.setFileImage(bytes);
			}

			if (Objects.equals(fileType, ProdConstant.IMAGEFILEFORMAT)) {
				img = ImageIO.read(new File(uploadFolder + "/img.png"));
				ImageIO.write(img, "png", baos);
				baos.flush();
				byte[] bytes = baos.toByteArray();
				baos.close();

				uploadDocument.setFileImage(bytes);
			}

			if (Objects.equals(fileType, ProdConstant.PPTFILEFORMAT)) {
				img = ImageIO.read(new File(uploadFolder + "/ppt.png"));
				ImageIO.write(img, "png", baos);
				baos.flush();
				byte[] bytes = baos.toByteArray();
				baos.close();

				uploadDocument.setFileImage(bytes);
			}

			if (!Objects.equals(fileType, ProdConstant.PPTFILEFORMAT)
					&& !Objects.equals(fileType, ProdConstant.IMAGEFILEFORMAT)
					&& !Objects.equals(fileType, ProdConstant.TEXTFILEFORMAT)
					&& !Objects.equals(fileType, ProdConstant.PDFFILEFORMAT)
					&& !Objects.equals(fileType, ProdConstant.WORDFILEFORMAT)
					&& !Objects.equals(fileType, ProdConstant.EXCELFILEFORMAT)) {
				img = ImageIO.read(new File(uploadFolder + "/file.png"));
				ImageIO.write(img, "png", baos);
				baos.flush();
				byte[] bytes = baos.toByteArray();
				baos.close();

				uploadDocument.setFileImage(bytes);
			}

			if (Objects.nonNull(uploadDocument)) {
				response.setContentType(ProdConstant.IMAGEFORMATE);
				response.getOutputStream().write(uploadDocument.getFileImage());
				response.getOutputStream().flush();
				response.getOutputStream().close();
				// model.addAttribute("images", uploadDocument);
			} else {
				System.out.println("Document not get..");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display documents " + ExceptionUtils.getStackTrace(e));
		}
	}

	/* Activate upload document */
	@GetMapping(value = "/activeDoc/{id}")
	public String activeUploadDoc(@PathVariable(name = "id") Long id, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			if (Objects.nonNull(id)) {
				UploadDocument uploadDoc = uploadDocService.docEnableAndDisable(id);
				if (Objects.nonNull(uploadDoc)) {
					return "redirect:/activeDocList";
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while activate upload document " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - activate document by id - " + id));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Upload Document");
			auditRecord.setActivityCode("ACTIVATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/inActiveDocList";
	}

	/* Deactivate upload document */
	@GetMapping(value = "/deactiveDoc/{id}")
	public String deactiveUploadDoc(@PathVariable(name = "id") Long id, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			if (Objects.nonNull(id)) {
				UploadDocument uploadDoc = uploadDocService.docEnableAndDisable(id);
				if (Objects.nonNull(uploadDoc)) {
					return "redirect:/inActiveDocList";
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while inactivate upload document " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - deactivate document by id - " + id));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Upload Document");
			auditRecord.setActivityCode("DEACTIVATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeDocList";
	}
}