package com.ksv.ktrccrm.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.mobile.device.Device;
import org.springframework.scheduling.annotation.Scheduled;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.impl.CheckInCheckOutDaoImpl;
import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.CheckInCheckOut;
import com.ksv.ktrccrm.db1.entities.EmailTemplate;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.TenantMst;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.CheckInCheckOutService;
import com.ksv.ktrccrm.service.EmailAndOTPService;
import com.ksv.ktrccrm.service.EmailService;
import com.ksv.ktrccrm.service.EmpRegistartionService;
import com.ksv.ktrccrm.service.HolidayService;
import com.ksv.ktrccrm.service.TenantMstService;
import com.ksv.ktrccrm.service.UserService;

@Controller
public class CheckInCheckOutController {
	private static final Logger LOG = LogManager.getLogger(CheckInCheckOutController.class);

	@Autowired
	private EmailService emailService;
	@Autowired
	private EmailAndOTPService emailAndOTPService;
	@Autowired
	private TemplateEngine templateEngine;
	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;
	@Autowired
	private UserService userService;

	public CheckInCheckOutController(TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

	@Value("${global.redirectmsg.save}")
	private String successSaveMsg;

	@Value("${global.redirecrmsg.emailSend}")
	private String emailSuccess;

	@Autowired
	private CheckInCheckOutService chkInChkOutService;
	@Autowired
	private AuditRecordService auditRecordService;
	@Autowired
	private HolidayService holidayService;
	@Autowired
	private EmpRegistartionService empRegistartionService;
	@Autowired
	private TenantMstService tenantMstService;

	SimpleDateFormat sdfDateTime = new SimpleDateFormat(ProdConstant.DATETIMEFORMATE);
	SimpleDateFormat sdfDate = new SimpleDateFormat(ProdConstant.DATEFORMATE);

	/* Total Working Days Calculate */
	public int getBusinessDays(LocalDate startInclusive, LocalDate endExclusive) {
		if (startInclusive.isAfter(endExclusive)) {
			String msg = "Start date " + startInclusive + " must be earlier than end date " + endExclusive;
			throw new IllegalArgumentException(msg);
		}
		int businessDays = 0;
		LocalDate d = startInclusive;
		while (d.isBefore(endExclusive)) {
			DayOfWeek dw = d.getDayOfWeek();
			if (dw != DayOfWeek.SUNDAY) {
				businessDays++;
			}
			d = d.plusDays(1);
		}
		return businessDays;
	}

	/* If employee not Check-In today then automatic save data */
	@Scheduled(cron = "${scheduling.job.cron3}")
	public void absentEmp() throws ParseException {

		String checkinDateTime = "-";
		String checkoutDateTime = "-";
		String timeDuration = "-";
		String isActive = ProdConstant.FALSE;
		String status = ProdConstant.ABSENT;
		String dayOfTheWeek = new SimpleDateFormat(ProdConstant.DAY).format(new Date());
		Date dateOnly = null;
		String date;
		Date createdDate = null;
		TenantMst tenantMst;
		try {
			List<String> userId = empRegistartionService.getEmpIdWithIsActive();
			List<String> checkInCheckOut = chkInChkOutService.getDateAndUserId();
			if (Objects.nonNull(userId) && Objects.nonNull(checkInCheckOut)) {
				userId.removeAll(checkInCheckOut);
				for (String id : userId) {
					String tenantId = empRegistartionService.getTenantIdWithEmpId(id);
					if (Objects.nonNull(tenantId)) {
						tenantMst = tenantMstService.getTenantDetails(tenantId, ProdConstant.TRUE);
						if (Objects.nonNull(tenantMst)) {
							String timeZone = tenantMst.getTimeZone();
							if (timeZone != null) {
								sdfDate.setTimeZone(TimeZone.getTimeZone(timeZone));
								dateOnly = sdfDate.parse(sdfDate.format(new Date()));
								createdDate = sdfDateTime.parse(sdfDateTime.format(new Date()));

								date = sdfDate.format(dateOnly);
								if (!Objects.deepEquals(checkInCheckOut, userId)) {
									chkInChkOutService.insertEmployee(id, createdDate, date, dayOfTheWeek, tenantId,
											isActive, checkinDateTime, checkoutDateTime, timeDuration, status);
								} else {
									System.out.println("Not Match ::: ");
								}
							}
						}
					} else {
						System.out.println("tenantId id is null");
					}
				}
			} else {
				System.out.println("userId & checkInCheckOut is Null ::::::::::::::::::");
			}
		} catch (Exception e) {
			LOG.error("Error occur while save absent employee data... " + ExceptionUtils.getStackTrace(e));
		}
	}

	/* Display attendance(check in & out) page */
	@GetMapping(value = "/attendance")
	public String getcheckInOut(@ModelAttribute CheckInCheckOut checkInCheckOut, Model model, Device device,
			HttpServletRequest request) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String userId = null;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			userId = authentication.getName();
			CheckInCheckOut checkInCheckOut1 = chkInChkOutService.findByEmpId(userId);
			/* Check-In & Check-out Button in Attendance */
			if (checkInCheckOut1 != null) {
				model.addAttribute("flag", checkInCheckOut1);
			} else {
				model.addAttribute("flag", new CheckInCheckOut());
			}

			CheckInCheckOut checkIn = chkInChkOutService.findByDateAndCurrentUser2(userId);
			if (Objects.nonNull(checkIn)) {
				model.addAttribute("checkin", checkIn);
			}
			/* Weekly Data Display in Attendance */
			LocalDate today = LocalDate.now();
			// Go backward to get Monday
			LocalDate monday = today;
			while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
				monday = monday.minusDays(1);
			}
			// Go forward to get Sunday
			LocalDate sunday = today;
			while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
				sunday = sunday.plusDays(1);
			}

			String from = monday.format(DateTimeFormatter.ofPattern(ProdConstant.DATEFORMATE));
			String to = sunday.format(DateTimeFormatter.ofPattern(ProdConstant.DATEFORMATE));

			/* Current user */
			List<CheckInCheckOut> userDetails = null;
			if (Objects.nonNull(userId)) {
				userDetails = chkInChkOutService.findByDayOfTheWeek(from, to, userId);
				
				for(CheckInCheckOut getWidth: userDetails) {
					System.out.println("Get Width ..........." + getWidth.getWidthForProgressBar());
				}
				if (Objects.nonNull(userDetails)) {
					model.addAttribute("attendance", userDetails);
				}

			}

			/* DAYS OVERVIEW THIS MONTH */
			LocalDate firstDay = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
			LocalDate lastDay = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());

			/* Working Days */
			int days = getBusinessDays(firstDay, lastDay);
			if (Objects.nonNull(days)) {
				model.addAttribute("workingDays", days);
			}

			/* Present Days (Employee Check-In) */
			Long presentDays = chkInChkOutService.getPresentDays(userId);
			if (Objects.nonNull(presentDays)) {
				model.addAttribute("presentDays", presentDays);
			}
			/* Holidays */
			Long holidayDays = holidayService.getHolidayDays();
			if (Objects.nonNull(holidayDays)) {
				model.addAttribute("holidayDays", holidayDays);
			}
			/* Late Days (Late Login) */
			Long lateDays = chkInChkOutService.getLateDays(userId);
			if (Objects.nonNull(lateDays)) {
				model.addAttribute("lateDays", lateDays);
			}

			/* Half Days (Half Day Leave) */
			Long halfDays = chkInChkOutService.getHalfDays(userId);
			if (Objects.nonNull(halfDays)) {
				model.addAttribute("halfDays", halfDays);
			}
			/* Previous Day for Absent Day */
			Long absentDays = chkInChkOutService.getAbsentDays(userId);
			if (Objects.nonNull(absentDays)) {
				model.addAttribute("absentDays", absentDays);
			}
		} catch (Exception e) {
			LOG.error("Error occur to open checkInOut page... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed attendance"));
			auditRecord.setMenuCode("Employee Administrator");
			auditRecord.setSubMenuCode("Attendance");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "checkincheckout/attendance";
	}

	/* Click to check in for Attendance */
	@GetMapping(value = "/saveCheckInAttendance")
	public @ResponseBody String saveCheckInAttendance(@ModelAttribute CheckInCheckOut checkInCheckOut,
			@RequestParam(value="city", required = false) String city,
			@RequestParam(value="state",required = false)String state,@RequestParam(value="country", required = false)String country,
			Device device, Model model, HttpServletRequest request)
			throws Exception {
		System.out.println("Inside save checkIn and view data..."+ checkInCheckOut.toString());
		AuditRecord auditRecord = new AuditRecord();
		String flag = ProdConstant.FLAG_N;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			checkInCheckOut.setUserId(userDetails.getUsername());
			checkInCheckOut.setCity(city);
			CheckInCheckOut checkInCheckOut1 = chkInChkOutService.saveCheckIn(checkInCheckOut, device, request);
			if (checkInCheckOut1 != null) {
				flag = ProdConstant.FLAG_Y;
			}
		} catch (Exception e) {
			LOG.error("Error occur while save checkIn data in Attendance /n" + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - checkin attendance"));
			auditRecord.setMenuCode("Employee Administrator");
			auditRecord.setSubMenuCode("Attendance");
			auditRecord.setActivityCode("CREATE");
			auditRecordService.save(auditRecord, device);
		}
		return flag;
	}

	/* Click to click out for Attendance */
	@GetMapping(value = "/saveCheckOutAttendance")
	public @ResponseBody String saveCheckOutAttendance(@ModelAttribute CheckInCheckOut checkInCheckOut,
			@RequestParam(value ="width",required = false) String width, Model model, Device device, HttpServletRequest request) throws Exception {
		System.out.println("Inside saveCheckOutAttendance method");
		AuditRecord auditRecord = new AuditRecord();
		String flag = ProdConstant.FLAG_N;
		try {
			System.out.println("Other data of saveCheckOutAttendance "+ checkInCheckOut.toString());
			System.out.println("Attandance ProgressBar width........." + width);
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			checkInCheckOut.setUserId(userDetails.getUsername());
			checkInCheckOut.setRemark(checkInCheckOut.getRemark());
			checkInCheckOut.setWidthForProgressBar(width);
			CheckInCheckOut checkInCheckOut2 = chkInChkOutService.saveCheckOut(checkInCheckOut, device, request);
			if (checkInCheckOut2 != null) {
				flag = ProdConstant.FLAG_Y;
			}
		} catch (Exception e) {
			LOG.error("Error occur while save checkOut data /n" + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - checkout Attendance"));
			auditRecord.setMenuCode("Employee Administrator");
			auditRecord.setSubMenuCode("Attendance");
			auditRecord.setActivityCode("CREATE");
			auditRecordService.save(auditRecord, device);
		}
		return flag;
	}

	/* Click to check in for DashBoard */
	@GetMapping(value = "/saveCheckIn")
	public @ResponseBody String saveCheckIn(@ModelAttribute CheckInCheckOut checkInCheckOut, BindingResult result,
			@RequestParam(value="addNotes",required = false) String addNotes,@RequestParam(value="city", required = false) String city,
			@RequestParam(value="state",required = false)String state,@RequestParam(value="country", required = false)String country,
			Device device, Model model, HttpServletRequest request) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String flag = ProdConstant.FLAG_N;
		try {

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			checkInCheckOut.setUserId(userDetails.getUsername());
			checkInCheckOut.setNotes(addNotes);

			CheckInCheckOut checkInCheckOut1 = chkInChkOutService.saveCheckIn(checkInCheckOut, device, request);
			if (checkInCheckOut1 != null) {
				flag = ProdConstant.FLAG_Y;
			}
		} catch (Exception e) {
			LOG.error("Error occur while save checkIn data /n" + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - checkin attendance in dashboard"));
			auditRecord.setMenuCode("Employee Administrator");
			auditRecord.setSubMenuCode("Employee Dashboard");
			auditRecord.setActivityCode("CREATE");
			auditRecordService.save(auditRecord, device);
		}
		return flag;
	}

	/* Click to click out for DashBoard */
	@GetMapping(value = "/saveCheckOut")
	public @ResponseBody String saveCheckOut(@ModelAttribute CheckInCheckOut checkInCheckOut,
			@RequestParam(value ="width",required = false) String width, Model model, Device device, HttpServletRequest request)
			throws Exception {
		System.out.println("Inside saveCheckOut method");
		AuditRecord auditRecord = new AuditRecord();
		String flag = ProdConstant.FLAG_N;
		try {
			System.out.println("width >>>>>>>" + width);
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			checkInCheckOut.setUserId(userDetails.getUsername());
			checkInCheckOut.setRemark(checkInCheckOut.getRemark());
			checkInCheckOut.setWidthForProgressBar(width);
			CheckInCheckOut checkInCheckOut2 = chkInChkOutService.saveCheckOut(checkInCheckOut, device, request);
			if (checkInCheckOut2 != null) {
				flag = ProdConstant.FLAG_Y;
			}
		} catch (Exception e) {
			LOG.error("Error occur while save checkOut data /n" + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - checkout attendance in dashboard"));
			auditRecord.setMenuCode("Employee Administrator");
			auditRecord.setSubMenuCode("Employee Dashboard");
			auditRecord.setActivityCode("CREATE");
			auditRecordService.save(auditRecord, device);
		}
		return flag;
	}

	/* Display Info for Employee about timeSummary */
	@GetMapping(value = "/totalTime")
	public String getEmpProfile(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String userId = null;
		try {
			/* Current user */
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			userId = authentication.getName();
			List<CheckInCheckOut> userDetails = null;
			if (userId != null) {
				userDetails = chkInChkOutService.getTotalTime(userId);
				if (userDetails != null) {
					model.addAttribute("totalTime", userDetails);
				}
			}
		} catch (Exception e) {
			LOG.error("Error occur while display time summary to employee " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed attendance in employee total time"));
			auditRecord.setMenuCode("Employee Administrator");
			auditRecord.setSubMenuCode("Employee Total Time");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "checkincheckout/emptotaltimelist";
	}

	/* Display Data for Employee Weekly */
	@GetMapping(value = "/dayOfWeek")
	public String getDataWeekly(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String userId = null;
		try {
			LocalDate today = LocalDate.now();
			// Go backward to get Monday
			LocalDate monday = today;
			while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
				monday = monday.minusDays(1);
			}
			// Go forward to get Sunday
			LocalDate sunday = today;
			while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
				sunday = sunday.plusDays(1);
			}

			String from = monday.format(DateTimeFormatter.ofPattern(ProdConstant.DATEFORMATE));
			String to = sunday.format(DateTimeFormatter.ofPattern(ProdConstant.DATEFORMATE));

			/* Current user */
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			userId = authentication.getName();
			List<CheckInCheckOut> userDetails = null;
			if (userId != null) {
				userDetails = chkInChkOutService.findByDayOfTheWeek(from, to, userId);
				if (userDetails != null) {
					model.addAttribute("weeklyList", userDetails);
				} else {
					return "checkincheckout/weeklyEmpData";
				}
			} else {
				return "checkincheckout/weeklyEmpData";
			}
		} catch (Exception e) {
			LOG.error("Error occur while display weekly list " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed attendance in employee weekly data"));
			auditRecord.setMenuCode("Employee Administrator");
			auditRecord.setSubMenuCode("Employee Weekly Data");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "checkincheckout/weeklyEmpData";
	}

	/* For New Request List */
	@GetMapping(value = "/newReqList")
	public String getNewReqList(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();
			if (empId != null) {
				List<CheckInCheckOut> getNewReqList = chkInChkOutService.getNewReqList(empId);
				if (Objects.nonNull(getNewReqList)) {
					model.addAttribute("addReq", getNewReqList);
				}
			} else {
				System.out.println("No Employee Found ::::::::::");
			}
		} catch (Exception e) {
			LOG.error("Error occur while approval new request list... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed new request list"));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("New Request List");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "checkincheckout/newRequestList";
	}

	/* For Cancel Request List */
	@GetMapping(value = "/cancelReqList")
	public String cancelReqList(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String empId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();
			if (empId != null) {
				List<CheckInCheckOut> getCancelReqList = chkInChkOutService.cancelReqList(empId);
				if (Objects.nonNull(getCancelReqList)) {
					model.addAttribute("addReq", getCancelReqList);
				}
			} else {
				System.out.println("No Employee Found ::::::::::");
			}
		} catch (Exception e) {
			LOG.error("Error occur while cacnel new request list... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed cancel request list"));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Cancel Request List");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "checkincheckout/cancelRequestList";
	}

	/* Add Request Home Page */
	@GetMapping("/getAddRequest")
	public String getAddRequest(@ModelAttribute CheckInCheckOut checkInCheckOut, Model model, Device device)
			throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String userId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			userId = authentication.getName();
			EmpBasicDetails getCurrentUser = empRegistartionService.getCurrentUser(userId);
			if (Objects.nonNull(getCurrentUser)) {
				model.addAttribute("user", getCurrentUser);
			}
		} catch (Exception e) {
			LOG.error("Error occur while display add request form " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed add request in check-in check-out"));
			auditRecord.setMenuCode("Employee Administrator");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "checkincheckout/addRequest-PopUp";
	}
	
	private int convertTimeToSeconds(String totalTime1){
        String[] parts = totalTime1.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1].split(" ")[0]);

        int totalSeconds = hours * 3600 + minutes * 60;
        return totalSeconds;
    }
	
	/* Save Add Request Data */
	@PostMapping("/addRequest")
	public String addRequest(@ModelAttribute CheckInCheckOut checkInCheckOut, BindingResult result, Model model,
			Device device, HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String userId;
		EmailTemplate emailTemplate = emailService.getEmailTemplate(ProdConstant.TRUE, ProdConstant.ADDNEWREQ_REQUEST);
		double totalWorkingTime = ProdConstant.totalWorkingTimeConvertInSec;
		double progressWidth;
		try {
			System.out.println("Time duration......"+ checkInCheckOut.getTimeDuration());
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			userId = authentication.getName();
			model.addAttribute("user", empRegistartionService.getCurrentUser(userId));
			if (Objects.nonNull(checkInCheckOut)) {
				String totalTime = checkInCheckOut.getTimeDuration();
				if(Objects.nonNull(totalTime)) {
				int seconds = convertTimeToSeconds(totalTime);
			
			        progressWidth = (seconds/totalWorkingTime)*100;
			        System.out.println("progressWidth..11.........." + progressWidth);

			        String progressBarWidth = Double.toString(progressWidth);
			        checkInCheckOut.setWidthForProgressBar(progressBarWidth);
				model.addAttribute("addReq", checkInCheckOut);
				chkInChkOutService.updateNewRequest(checkInCheckOut, device, request);
				redirectAttributes.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
				if (emailTemplate != null) {
					/* Mail Send */
					CheckInCheckOut checkInCheckOuts = chkInChkOutService.sendMail(userId);
					if (Objects.nonNull(checkInCheckOuts)) {
						Context context = new Context();
						context.setVariable("getEmp", checkInCheckOuts);
						String body = templateEngine.process("checkincheckout/chkInOutRequest", context);
						String from = emailTemplate.getEmailFrom();
						String to = emailTemplate.getEmailTo();
						String subject = emailTemplate.getEmailSub();
						String cc = emailTemplate.getEmailCc();
						emailAndOTPService.emailSend(from, to, subject, body, cc);
						redirectAttributes.addFlashAttribute(ProdConstant.SUCCESS, emailSuccess);
					}
				}
				}else {
					System.out.println("totalTime is null.....");
				}
			}
		} catch (Exception e) {
			LOG.error("Error occur while send request" + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - create add request in check-in check-out"));
			auditRecord.setMenuCode("Employee Administrator");
			auditRecord.setSubMenuCode("Add Request");
			auditRecord.setActivityCode("UPDATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/newReqList";
	}
	
//	/* Save Add Request Data */
//	@PostMapping("/addRequest")
//	public String addRequest(@ModelAttribute CheckInCheckOut checkInCheckOut, BindingResult result, Model model,
//			Device device, HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
//		AuditRecord auditRecord = new AuditRecord();
//		String userId;
//		EmailTemplate emailTemplate = emailService.getEmailTemplate(ProdConstant.TRUE, ProdConstant.ADDNEWREQ_REQUEST);
//		try {
//			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//			userId = authentication.getName();
//			model.addAttribute("user", empRegistartionService.getCurrentUser(userId));
//			if (Objects.nonNull(checkInCheckOut)) {
//				model.addAttribute("addReq", checkInCheckOut);
//				chkInChkOutService.updateNewRequest(checkInCheckOut, device, request);
//				redirectAttributes.addFlashAttribute(ProdConstant.SUCCESS, successSaveMsg);
//				if (emailTemplate != null) {
//					/* Mail Send */
//					CheckInCheckOut checkInCheckOuts = chkInChkOutService.sendMail(userId);
//					if (Objects.nonNull(checkInCheckOuts)) {
//						Context context = new Context();
//						context.setVariable("getEmp", checkInCheckOuts);
//						String body = templateEngine.process("checkincheckout/chkInOutRequest", context);
//						String from = emailTemplate.getEmailFrom();
//						String to = emailTemplate.getEmailTo();
//						String subject = emailTemplate.getEmailSub();
//						String cc = emailTemplate.getEmailCc();
//						emailAndOTPService.emailSend(from, to, subject, body, cc);
//						redirectAttributes.addFlashAttribute(ProdConstant.SUCCESS, emailSuccess);
//					}
//				}
//			}
//		} catch (Exception e) {
//			LOG.error("Error occur while send request" + ExceptionUtils.getStackTrace(e));
//		} finally {
//			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//			auditRecord.setRemarks(userDetails.getUsername().concat(" - create add request in check-in check-out"));
//			auditRecord.setMenuCode("Employee Administrator");
//			auditRecord.setSubMenuCode("Add Request");
//			auditRecord.setActivityCode("UPDATE");
//			auditRecordService.save(auditRecord, device);
//		}
//		return "redirect:/newReqList";
//	}

	/* For Approved/Reject List */
	@GetMapping(value = "/approvalNewReqList")
	public String getEmpLeave(Model model, Device device) throws Exception {
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
					if (empId != null) {
						List<CheckInCheckOut> checkInCheckOuts = chkInChkOutService.getEmpWithManger(empId, tenantId);
						if (Objects.nonNull(checkInCheckOuts)) {
							model.addAttribute("addReq", checkInCheckOuts);
						}
					}
				} else {
					System.out.println("No Employee Found ::::::::::");
				}
			}
		} catch (Exception e) {
			LOG.error("Error occur while approval new request by manager... " + ExceptionUtils.getStackTrace(e));
		} finally {

			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed approved/reject list"));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("New Request");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "checkincheckout/approvalRequest";
	}

	/* Reject New Request By Manager */
	@GetMapping(value = "/cancelAddReq/{id}")
	public String cancelAddReq(CheckInCheckOut checkInCheckOut, @PathVariable(name = "id") Long id, Model model,
			Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			chkInChkOutService.cancelAddReq(checkInCheckOut);
		} catch (Exception e) {
			LOG.error("Error occur while cancel new request by employee ... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - cancel viewed id - " + id));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Add New Request");
			auditRecord.setActivityCode("Cancel Request");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/newReqList";
	}

	/* Leave Request Approved by Manager */
	public void approvedNewReqMail(Long id, RedirectAttributes redirect) throws Exception {
		String userId;
		EmailTemplate emailTemplate = emailService.getEmailTemplate(ProdConstant.TRUE, ProdConstant.ADDNEWREQ_APPROVED);
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			userId = authentication.getName();
			CheckInCheckOut checkInCheckOut = null;
			if (Objects.nonNull(emailTemplate)) {
				if (userId != null) {
					checkInCheckOut = chkInChkOutService.getCurrentUserWithId(userId, id);
					if (checkInCheckOut != null) {
						Context context = new Context();
						context.setVariable("approvedNewReqList", checkInCheckOut);
						String body = templateEngine.process("checkincheckout/approvedNewReqMail", context);
						String from = emailTemplate.getEmailFrom();
						String to = emailTemplate.getEmailTo();
						String subject = emailTemplate.getEmailSub();
						String cc = emailTemplate.getEmailCc();

						emailAndOTPService.emailSend(from, to, subject, body, cc);
						redirect.addFlashAttribute(ProdConstant.SUCCESS, emailSuccess);

					} else {
						System.out.println("Nothing Happen ::::::");
					}
				}
			} else {
				System.out.println("Nothing Happen ::::::");
			}
		} catch (Exception e) {
			LOG.error("Error occurring while sending emails to employee who add new request rejected by manager..... "
					+ ExceptionUtils.getStackTrace(e));
		}
	}

	/* Approval New Request By Manager */
	@GetMapping(value = "/acceptNew/{id}")
	public String acceptNewRequest(@ModelAttribute CheckInCheckOut checkInCheckOut, @PathVariable(name = "id") Long id,
			Model model, RedirectAttributes redirect, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			System.out.println("checkInCheckOut in progressWidth..........." + checkInCheckOut.toString());
			List<CheckInCheckOut> getAcceptById = chkInChkOutService.getAcceptLeaveById(id);
			if (Objects.nonNull(getAcceptById)) {
				model.addAttribute("approvedNewReqList", getAcceptById);
			}
			if (checkInCheckOut != null) {
				String approvalReq = ProdConstant.APPROVED;
				String status = ProdConstant.PRESENT;
				String isActive = ProdConstant.TRUE;
				String flag = ProdConstant.FLAG_N;
				checkInCheckOut.setApprovalReq(approvalReq);
				checkInCheckOut.setStatus(status);
				checkInCheckOut.setIsActive(isActive);
				checkInCheckOut.setFlag(flag);
				chkInChkOutService.acceptStatus(approvalReq, status, isActive, flag, id);

				approvedNewReqMail(id, redirect);
			} 
		} catch (Exception e) {
			LOG.error("Error occur while accept new request by manager ... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - accept new request viewed id - " + id));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("New Request");
			auditRecord.setActivityCode("Accept Request");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/approvalNewReqList";
	}

	/* Leave Request Rejected by Manager */
	public void rejectNewReqMail(Long id, RedirectAttributes redirect) throws Exception {
		String userId;
		EmailTemplate emailTemplate = emailService.getEmailTemplate(ProdConstant.TRUE, ProdConstant.ADDNEWREQ_REJECT);
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			userId = authentication.getName();
			CheckInCheckOut checkInCheckOut = null;
			if (userId != null) {
				checkInCheckOut = chkInChkOutService.getCurrentUserWithId(userId, id);
				if (checkInCheckOut != null) {
					Context context = new Context();
					context.setVariable("rejectNewReqList", checkInCheckOut);
					String body = templateEngine.process("checkincheckout/rejectNewReqMail", context);
					String from = emailTemplate.getEmailFrom();
					String to = emailTemplate.getEmailTo();
					String subject = emailTemplate.getEmailSub();
					String cc = emailTemplate.getEmailCc();

					emailAndOTPService.emailSend(from, to, subject, body, cc);
					redirect.addFlashAttribute(ProdConstant.SUCCESS, emailSuccess);
				} else {
					System.out.println("Nothing Happen ::::::");
				}
			} else {
				System.out.println("Nothing Happen ::::::");
			}
		} catch (Exception e) {
			LOG.error("Error occurring while sending emails to employee who add new request rejected by manager..... "
					+ ExceptionUtils.getStackTrace(e));
		}
	}

	/* Reject New Request By Manager */
	@GetMapping(value = "/rejectNew/{id}")
	public String rejectNewRequest(@ModelAttribute CheckInCheckOut checkInCheckOut, @PathVariable(name = "id") Long id,
			Model model, RedirectAttributes redirect, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			List<CheckInCheckOut> getAcceptById = chkInChkOutService.getAcceptLeaveById(id);
			if (Objects.nonNull(getAcceptById)) {
				model.addAttribute("addReq", getAcceptById);
			}
			if (checkInCheckOut != null) {
				String approvalReq = ProdConstant.REJECTED;
				String status = ProdConstant.ABSENT;
				String isActive = ProdConstant.TRUE;
				String flag = ProdConstant.FLAG_N;
				checkInCheckOut.setApprovalReq(approvalReq);
				checkInCheckOut.setStatus(status);
				chkInChkOutService.acceptStatus(approvalReq, status, isActive, flag, id);

				rejectNewReqMail(id, redirect);
			}
		} catch (Exception e) {
			LOG.error("Error occur while reject new request by manager ... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - rejet request viewed id - " + id));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("New Request");
			auditRecord.setActivityCode("Reject Request");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/approvalNewReqList";
	}

	@GetMapping("/totalAbsentEmp")
	public String totalAbsentEmp(Model model, Device device) throws Exception {
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
					List<CheckInCheckOut> checkInCheckOuts = chkInChkOutService.getTotalAbsentEmp(tenantId);
					if (Objects.nonNull(checkInCheckOuts)) {
						model.addAttribute("totalAbsentEmp", checkInCheckOuts);
					}
				}
			} else {
				System.out.println("Error occuring while get total absent employee");
			}
		} catch (Exception e) {
			LOG.error("Error occur while get list of total absent emp " + ExceptionUtils.getStackTrace(e));
		} finally {

			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - view total absent employee"));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Dashboard");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "checkincheckout/totalAbsentEmp";
	}

	@GetMapping("/totalAttendance")
	public String totalAttendance(Model model, Device device) throws Exception {
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
					List<CheckInCheckOut> checkInCheckOuts = chkInChkOutService.getTotalAttendance(tenantId);
					if (Objects.nonNull(checkInCheckOuts)) {
						model.addAttribute("totalAttendance", checkInCheckOuts);
					}
				}
			} else {
				System.out.println("Error occuring while get total attendance employee");
			}
		} catch (Exception e) {
			LOG.error("Error occur while get list of total attendance emp " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - view total attendance of employee"));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Dashboard");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "checkincheckout/totalAttendance";
	}

	@GetMapping("/totalNewReq")
	public String totalNewReq(Model model, Device device) throws Exception {
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
					List<CheckInCheckOut> checkInCheckOuts = chkInChkOutService.getTotalNewReq();
					if (Objects.nonNull(checkInCheckOuts)) {
						model.addAttribute("totalNewReq", checkInCheckOuts);
					}
				}
			} else {
				System.out.println("Error occuring while get total new req");
			}
		} catch (Exception e) {
			LOG.error("Error occur while get list of total new req " + ExceptionUtils.getStackTrace(e));
		} finally {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - view new request of employee"));
			auditRecord.setMenuCode("HR Management");
			auditRecord.setSubMenuCode("Dashboard");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "checkincheckout/totalNewReq";
	}

	/* Display Data for Employee Weekly */
	@GetMapping(value = "/currentWeek")
	public String getWeekButton(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		String userId = null;
		try {
			/* Weekly Data Display in Attendance */
			LocalDate today = LocalDate.now();
			// Go backward to get Monday
			LocalDate monday = today;
			while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
				monday = monday.minusDays(1);
			}
			// Go forward to get Sunday
			LocalDate sunday = today;
			while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
				sunday = sunday.plusDays(1);
			}

			String from = monday.format(DateTimeFormatter.ofPattern(ProdConstant.DATEFORMATE));
			String to = sunday.format(DateTimeFormatter.ofPattern(ProdConstant.DATEFORMATE));

			/* Current user */
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			userId = authentication.getName();
			List<CheckInCheckOut> userDetails = null;
			if (userId != null) {
				userDetails = chkInChkOutService.currentWeekData(from, to, userId, ProdConstant.TRUE);
				if (userDetails != null) {
					model.addAttribute("currentWeek", userDetails);
				} else {
					System.out.println("Else 1 ::::::::::::::::::");
					return "checkincheckout/weeklyEmpData2";
				}
			} else {
				System.out.println("Else 2 ::::::::::::::::::");
				return "checkincheckout/weeklyEmpData2";
			}
		} catch (Exception e) {
			LOG.error("Error occur while display weekly list " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed attendance in employee weekly data"));
			auditRecord.setMenuCode("Employee Administrator");
			auditRecord.setSubMenuCode("Employee Weekly Data");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "checkincheckout/weeklyEmpData2";
	}
}