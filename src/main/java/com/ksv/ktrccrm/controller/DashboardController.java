package com.ksv.ktrccrm.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.query.Param;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.impl.CheckInCheckOutDaoImpl;
import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.CheckInCheckOut;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.EmpPersonalDetails;
import com.ksv.ktrccrm.db1.entities.EmpWorkDetails;
import com.ksv.ktrccrm.db1.entities.ExitActivity;
import com.ksv.ktrccrm.db1.entities.ExpenseReimb;
import com.ksv.ktrccrm.db1.entities.HolidayMaster;
import com.ksv.ktrccrm.db1.entities.LeaveMst;
import com.ksv.ktrccrm.db1.entities.Slider;
import com.ksv.ktrccrm.db1.entities.TenantMst;
import com.ksv.ktrccrm.db1.entities.UploadDocument;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.CheckInCheckOutService;
import com.ksv.ktrccrm.service.CustomerContactService;
import com.ksv.ktrccrm.service.DailyWorkReportService;
import com.ksv.ktrccrm.service.EmpRegistartionService;
import com.ksv.ktrccrm.service.ExitActivityService;
import com.ksv.ktrccrm.service.ExpenseReimbService;
import com.ksv.ktrccrm.service.HolidayService;
import com.ksv.ktrccrm.service.LeaveService;
import com.ksv.ktrccrm.service.OrganizationContactService;
import com.ksv.ktrccrm.service.OrganizationEmployeeContactService;
import com.ksv.ktrccrm.service.SliderService;
import com.ksv.ktrccrm.service.TenantMstService;
import com.ksv.ktrccrm.service.TrainingFormService;
import com.ksv.ktrccrm.service.UploadDocService;
import com.ksv.ktrccrm.service.UserService;

@Controller
public class DashboardController {
	private static final Logger LOG = LogManager.getLogger(DashboardController.class);

	@Autowired
	private CheckInCheckOutService chkInChkOutService;
	@Autowired
	private AuditRecordService auditRecordService;
	@Autowired
	private HolidayService holidayService;
	@Autowired
	private EmpRegistartionService empRegistartionService;
	@Autowired
	private LeaveService leaveService;
	@Autowired
	private UploadDocService uploadDocService;
	@Autowired
	private ExitActivityService exitActivityService;
	@Autowired
	private ExpenseReimbService expenseReimbService;
	@Autowired
	private UserService userService;
	@Autowired
	private TrainingFormService trainingFormService;
	@Autowired
	private DailyWorkReportService dailyWorkReportService;
	@Autowired
	private SliderService sliderService;  
	@Autowired
	private TenantMstService tenantMstService;
	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;
	
	
	@Autowired
	private OrganizationContactService organizationContactService;
	@Autowired
	private OrganizationEmployeeContactService organizationEmployeeContactService;
	@Autowired
	private CustomerContactService customerContactService;

	/* Display attendance(check in & out) page */
	@GetMapping(value = "/empDashboard")
	public String empDashboard(@ModelAttribute CheckInCheckOut checkInCheckOut, BindingResult result,
			@Param("id") Long id, Model model, Device device, HttpServletResponse response) throws Exception {
		System.out.println("Inside deshboard controller..............");
		AuditRecord auditRecord = new AuditRecord();
		String userId = null;
		try {
			/* Current user */
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			userId = authentication.getName();
			UserMst userMst = userService.getUserDetails(userId, ProdConstant.ISACTIVE);
			String tenantId = userMst.getTenantId();
			TenantMst tenantMst = tenantMstService.getTenantDetails(userMst.getTenantId(), ProdConstant.TRUE);

			String role = userMst.getMainRole();
			if (Objects.nonNull(role)) {
				model.addAttribute("mainRole", role);
			}

			Long allLeave = leaveService.getAllLeaves(tenantId);
			if (Objects.nonNull(allLeave)) {
				model.addAttribute("allLeave", allLeave);
			}
			Long allAttendance = chkInChkOutService.getAllAttendance(tenantId);
			if (Objects.nonNull(allAttendance)) {
				model.addAttribute("allAttendance", allAttendance);
			}
			Long allAbsent = chkInChkOutService.getAllEmpAbsent(tenantId);
			if (Objects.nonNull(allAbsent)) {
				model.addAttribute("allAbsent", allAbsent);
			}
			Long allPendingLeaves = leaveService.getAllPendingLeaves(tenantId);
			if (Objects.nonNull(allPendingLeaves)) {
				model.addAttribute("allPendingLeaves", allPendingLeaves);
			}
			Long allExitActivity = exitActivityService.getAllExitActivity(tenantId);
			if (Objects.nonNull(allExitActivity)) {
				model.addAttribute("allExitActivity", allExitActivity);
			}
			Long allNewReq = chkInChkOutService.getAllNewReq(tenantId);
			if (Objects.nonNull(allNewReq)) {
				model.addAttribute("allNewReq", allNewReq);
			}
			Long allTraining = trainingFormService.getAllTraining(ProdConstant.TRUE, tenantId);
			if (Objects.nonNull(allTraining)) {
				model.addAttribute("allTraining", allTraining);
			}
			Long allWorkReport = dailyWorkReportService.getAllWorkReport(tenantId);
			if (Objects.nonNull(allWorkReport)) {
				model.addAttribute("allWorkReport", allWorkReport);
			}

			CheckInCheckOut checkInCheckOut1 = chkInChkOutService.findByEmpId(userId);
			/* Check-In & Check-out Button in Attendance */
			if (checkInCheckOut1 != null) {
				model.addAttribute("flag", checkInCheckOut1);
			} else {
				model.addAttribute("flag", new CheckInCheckOut());
			}

			/* Weekly Data Display in Attendance */
			LocalDateTime today = LocalDateTime.now();
			ZonedDateTime timeZoneSet = ZonedDateTime.of(today, ZoneId.of(tenantMst.getTimeZone()));
			// Go backward to get Monday
			LocalDate monday = timeZoneSet.toLocalDate();
			while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
				monday = monday.minusDays(1);
			}

			// Go forward to get Sunday
			LocalDate sunday = timeZoneSet.toLocalDate();
			while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
				sunday = sunday.plusDays(1);
			}

			String from = monday.format(DateTimeFormatter.ofPattern(ProdConstant.DATEFORMATE));
			String to = sunday.format(DateTimeFormatter.ofPattern(ProdConstant.DATEFORMATE));

			/* List of Weekly Data, Upcoming BirthDay, Holiday List */
			List<CheckInCheckOut> userDetails = null;
			if (userId != null) {
				userDetails = chkInChkOutService.findByDayOfTheWeek(from, to, userId);

				/* Request for Approval */
				List<LeaveMst> leaveDashboard = leaveService.getLeavePending(userId, tenantId);
				if (Objects.nonNull(leaveDashboard)) {
					model.addAttribute("leaveDashboard", leaveDashboard);
				}
				List<CheckInCheckOut> attendanceDashboard = chkInChkOutService.getAttendancePending(userId, tenantId);
				if (Objects.nonNull(attendanceDashboard)) {
					model.addAttribute("attendanceDashboard", attendanceDashboard);
				}
				EmpBasicDetails image = empRegistartionService.getCurrentUser(userId);
				if (Objects.nonNull(image)) {
					model.addAttribute("image", image);
				}
				List<ExitActivity> exitActivityDashboard = exitActivityService.getEmpWithMangerWithPending(userId,
						tenantId);
				if (Objects.nonNull(exitActivityDashboard)) {
					model.addAttribute("exitActivityDashboard", exitActivityDashboard);
				}
				List<ExpenseReimb> expenseReimbDashboard = expenseReimbService.getEmpWithMangerWithPending(tenantId);
				if (Objects.nonNull(expenseReimbDashboard)) {
					model.addAttribute("expenseReimbDashboard", expenseReimbDashboard);
				}

				if (leaveDashboard.isEmpty() && attendanceDashboard.isEmpty() && exitActivityDashboard.isEmpty()
						&& expenseReimbDashboard.isEmpty()) {
					model.addAttribute("NoDataForReq", "NoData");
				}

				/* On Leave Today */
				List<LeaveMst> onLeaveToday = leaveService.onLeaveToday(tenantId);
				if (Objects.nonNull(onLeaveToday)) {
					model.addAttribute("onLeaveToday", onLeaveToday);
				}

				if (onLeaveToday.isEmpty()) {
					model.addAttribute("NoDataForLeave", "NoData");
				}

				/* For CheckIn CheckOut */
				CheckInCheckOut checkin = chkInChkOutService.findByDateAndCurrentUser2(userId);
				if (Objects.nonNull(checkin)) {
					model.addAttribute("checkin", checkin);
				}
				
				/*For Count......*/
//				Long allEmp = empRegistartionService.getAllEmployees();
//				
//				if (Objects.nonNull(allEmp)) {
//					System.out.println("Toatl Employee :::::::: "+allEmp);
//					model.addAttribute("allEmp", allEmp);
//				}
				
				/*For Count......*/	
				long allOrg = organizationContactService.getOrganizationCount(ProdConstant.TRUE);
				
				if (Objects.nonNull(allOrg)) {
					System.out.println("Total Active Organisation :::::::: "+allOrg);
					model.addAttribute("allOrg", allOrg);
				}
				
				/*For Count......*/	
				long allOrgEmp = organizationEmployeeContactService.getOrganizationEmployeeCount(ProdConstant.TRUE);   
				
				if (Objects.nonNull(allOrgEmp)) {
					System.out.println("Total Active Organisation Employee :::::::: "+allOrgEmp);
					model.addAttribute("allOrgEmp", allOrgEmp);
				}
				
				/*For Count......*/	
				long allCustomer = customerContactService.getStudentsCount(ProdConstant.TRUE);   
				
				if (Objects.nonNull(allCustomer)) {
					System.out.println("Total Active Student :::::::: "+allCustomer);
					model.addAttribute("allCustomer", allCustomer);
				}
				
				

				/* For Teammates */
				List<EmpWorkDetails> empWorkDetailsList = empRegistartionService.getEmpWithManger(userId, tenantId);
				if (empWorkDetailsList != null && !empWorkDetailsList.isEmpty()) {
					model.addAttribute("teammatesList", empWorkDetailsList);
				}

				if (empWorkDetailsList.isEmpty()) {
					model.addAttribute("NoDataForTeammates", "NoData");
				}

				/* Upcoming Birthday */
				List<EmpPersonalDetails> upComingBirthday = chkInChkOutService.getUpcomingEmpBirthday(tenantId);
				List<EmpPersonalDetails> birthDayToday = empRegistartionService.getEmpBirthdayToday(tenantId);

				if (Objects.nonNull(upComingBirthday)) {
					model.addAttribute("upcomingBirthDay", upComingBirthday);
				}
				if (Objects.nonNull(birthDayToday)) {
					model.addAttribute("birthDayToday", birthDayToday);
				}

				if (upComingBirthday.isEmpty() && birthDayToday.isEmpty()) {
					model.addAttribute("NoData", "NoData");
				}

				/* For Upcoming Holiday */
				List<HolidayMaster> holidayList = holidayService.getHolidayUpcoming(tenantId);
				if (Objects.nonNull(holidayList)) {
					model.addAttribute("holidayList", holidayList);
				}
				if (holidayList.isEmpty()) {
					model.addAttribute("NoDataForHoliday", "NoData");
				}

				/* for Upload Document */
				List<UploadDocument> organizationList = uploadDocService
						.getOrganizationDepart(ProdConstant.ORGDEPARTMENT, ProdConstant.TRUE, tenantId);
				if (Objects.nonNull(organizationList)) {
					model.addAttribute("organizationList", organizationList);
				}
				if (organizationList.isEmpty()) {
					model.addAttribute("NoDataForOrgDoc", "NoData");
				}

				List<UploadDocument> employeeList = uploadDocService.getEmpDepartment(ProdConstant.EMPDEPARTMENT,
						ProdConstant.TRUE, tenantId);
				if (Objects.nonNull(employeeList)) {
					model.addAttribute("employeeList", employeeList);
				}
				if (employeeList.isEmpty()) {
					model.addAttribute("NoDataForEmpDoc", "NoData");
				}

				/* For Slider */
				List<Slider> sliderList = sliderService.getRecordList(ProdConstant.TRUE, tenantId);
				if (Objects.nonNull(sliderList)) {
					model.addAttribute("sliderList", sliderList);
				}

				if (sliderList.isEmpty()) {
					model.addAttribute("NoDataForSlider", "NoData");
				}

				if (userDetails != null) {
					model.addAttribute("weeklyList", userDetails);
					Date date = checkOutDaoImpl.getDateTime();
					if (Objects.nonNull(date)) {
						model.addAttribute("date", date);
					}

				}
			}

			/* Present Days in Current Month (Employee Check-In) */
			Long presentDays = chkInChkOutService.getPresentDays(userId);
			if (Objects.nonNull(presentDays)) {
				model.addAttribute("presentDays", presentDays);
			}

			/* Leave Count in Current Month */
			Long totallLeave = leaveService.getLeaveDays(userId);
			if (Objects.nonNull(totallLeave)) {
				model.addAttribute("totallLeave", totallLeave);
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
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed HR/employee dashboard "));
			auditRecord.setMenuCode("Employee Administrator");
			auditRecord.setSubMenuCode("Dashboard ");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "dashboard/dashboard";
	}

}
