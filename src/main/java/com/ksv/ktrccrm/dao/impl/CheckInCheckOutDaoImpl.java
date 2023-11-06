package com.ksv.ktrccrm.dao.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.CheckInCheckOutDao;
import com.ksv.ktrccrm.db1.entities.CheckInCheckOut;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.EmpPersonalDetails;
import com.ksv.ktrccrm.db1.entities.TenantMst;
import com.ksv.ktrccrm.db1.repository.CheckInCheckOutRepository;
import com.ksv.ktrccrm.db1.repository.EmpBasicRepository;
import com.ksv.ktrccrm.db1.repository.EmpPersonalRepository;
import com.ksv.ktrccrm.db1.repository.TenantMstRepository;

@Repository
public class CheckInCheckOutDaoImpl implements CheckInCheckOutDao {
	private final Logger LOGGER = LogManager.getLogger(CheckInCheckOutDaoImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private CheckInCheckOutRepository checkInOutRepository;
	@Autowired
	private EmpPersonalRepository empPersonalRepository;
	@Autowired
	private EmpBasicRepository empBasicRepository;
	@Autowired
	private TenantMstRepository tenantMstRepository;
	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;

	SimpleDateFormat sdfDateTime = new SimpleDateFormat(ProdConstant.DATETIMEFORMATE);
	SimpleDateFormat sdfDate = new SimpleDateFormat(ProdConstant.DATEFORMATE);
	
	public Date getDateTime() {
		String empId;
		Date dateTimeOnly = null;
		try {
			/* Current user */
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();
			
			EmpBasicDetails basicDetails = empBasicRepository.getCurrentUser(empId, ProdConstant.TRUE);
			if (Objects.nonNull(basicDetails)) {
				TenantMst tenantMst = tenantMstRepository.getTenantDetails(basicDetails.getTenantId(),
						ProdConstant.TRUE);
				if (Objects.nonNull(tenantMst)) {
					String timeZone = tenantMst.getTimeZone();					
					if (timeZone != null) {
						sdfDateTime.setTimeZone(TimeZone.getTimeZone(timeZone));
						dateTimeOnly = sdfDateTime.parse(sdfDateTime.format(new Date()));
					}
				} else {

				}
			} else {

			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get date & time " + ExceptionUtils.getStackTrace(e));
		}
		return dateTimeOnly;
	}
	public Date getDate() {
		String empId;
		Date dateOnly = null;
		try {
			/* Current user */
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();
			
			EmpBasicDetails basicDetails = empBasicRepository.getCurrentUser(empId, ProdConstant.TRUE);
			if (Objects.nonNull(basicDetails)) {
				TenantMst tenantMst = tenantMstRepository.getTenantDetails(basicDetails.getTenantId(),
						ProdConstant.TRUE);
				if (Objects.nonNull(tenantMst)) {
					String timeZone = tenantMst.getTimeZone();
					if (timeZone != null) {
						sdfDate.setTimeZone(TimeZone.getTimeZone(timeZone));
						dateOnly = sdfDate.parse(sdfDate.format(new Date()));
					}
				} else {

				}
			} else {

			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get date & time " + ExceptionUtils.getStackTrace(e));
		}
		return dateOnly;
	}

	/* Display All Emp in CheckInOut */
	@Override
	public List<CheckInCheckOut> getcheckInOutList() throws Exception {
		List<CheckInCheckOut> checkInOutList = checkInOutRepository.findAll();
		try {
			if (!checkInOutList.isEmpty()) {
				return checkInOutList;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display all checkInOut list" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	/* Display data using from date, to date, UserId & tenantId */
	@Override
	public List<CheckInCheckOut> findByDateorUserIdorTenantId(String from, String to, String userId, String tenantId)
			throws Exception {
		List<CheckInCheckOut> checkInOutList = checkInOutRepository.findByDateorUserIdorTenantId(from, to, userId,
				tenantId);
		try {
			if (!checkInOutList.isEmpty()) {
				return checkInOutList;
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while display findByDateorUserId checkInOut list" + ExceptionUtils.getStackTrace(e));
		}
		return checkInOutList;
	}

	@Override
	public List<CheckInCheckOut> findByDateorUserId(String from, String to, String userId) throws Exception {
		List<CheckInCheckOut> checkInOutList = checkInOutRepository.findByDateorUserId(from, to, userId);
		try {
			if (!checkInOutList.isEmpty()) {
				return checkInOutList;
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while display findByDateorUserId checkInOut list" + ExceptionUtils.getStackTrace(e));
		}
		return checkInOutList;
	}

	@Override
	public List<CheckInCheckOut> findByDateorTenantId(String from, String to, String tenantId) throws Exception {
		List<CheckInCheckOut> checkInOutList = checkInOutRepository.findByDateorTenantId(from, to, tenantId);
		try {
			if (!checkInOutList.isEmpty()) {
				return checkInOutList;
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while display findByDateorTenantId checkInOut list" + ExceptionUtils.getStackTrace(e));
		}
		return checkInOutList;
	}

	@Override
	public List<CheckInCheckOut> findAllEmp(String from, String to) throws Exception {
		List<CheckInCheckOut> checkInOutList = checkInOutRepository.findAllEmp(from, to);
		try {
			if (!checkInOutList.isEmpty()) {
				return checkInOutList;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display findByDate checkInOut list" + ExceptionUtils.getStackTrace(e));
		}
		return checkInOutList;
	}

	/* Check In */
	@Override
	public CheckInCheckOut saveCheckIn(CheckInCheckOut checkInCheckOut, Device device, HttpServletRequest request)
			throws Exception {
		String empId = null;
		try {
			/* Current user */
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			empId = authentication.getName();

			EmpBasicDetails basicDetails = empBasicRepository.getCurrentUser(empId, ProdConstant.TRUE);
			if (Objects.nonNull(basicDetails)) {
					/* Enable device detection */
					String deviceType = "";
					if (device.isMobile()) {
						deviceType = ProdConstant.MOBILE;
					} else if (device.isTablet()) {
						deviceType = ProdConstant.TABLET;
					} else if (device.isNormal()) {
						deviceType = ProdConstant.BROWSER;
					}

					/* Day of the Week */
					SimpleDateFormat simpleDateformat = new SimpleDateFormat(ProdConstant.DAY);

					Optional<CheckInCheckOut> checkInCheckOut2 = checkInOutRepository.findByDateAndCurrentUser(empId);
					if (checkInCheckOut2.isPresent()) {
						CheckInCheckOut newCheckInCheckOut = checkInCheckOut2.get();
						newCheckInCheckOut.setUserId(userDetails.getUsername());
						newCheckInCheckOut.setCheckinActionFrom(deviceType);
						newCheckInCheckOut.setCheckinIpAddress(ProdConstant.getClientIp(request));
						newCheckInCheckOut.setDate(sdfDate.format(getDateTime()));
						newCheckInCheckOut.setCheckinDateTime(sdfDateTime.format(getDateTime()));
						newCheckInCheckOut.setDayOfTheWeek(simpleDateformat.format(getDate()));
						newCheckInCheckOut.setTenantId(basicDetails.getTenantId());
						newCheckInCheckOut.setManagerId(basicDetails.getEmpWorkDetails().getReportingManager());
						newCheckInCheckOut.setFlag(ProdConstant.FLAG_Y);
						newCheckInCheckOut.setStatus(ProdConstant.PENDING);
						newCheckInCheckOut.setIsActive(ProdConstant.TRUE);
						newCheckInCheckOut.setCheckoutDateTime(null);
						newCheckInCheckOut.setTimeDuration(null);
						newCheckInCheckOut.setLastModifiedBy(empId);
						newCheckInCheckOut.setLastModifiedDate(checkOutDaoImpl.getDateTime());
						newCheckInCheckOut.setNotes(checkInCheckOut.getNotes());

//						System.out.println("CITY in Dao :::::: " + checkInCheckOut.getCity());
//						newCheckInCheckOut.setCity(checkInCheckOut.getCity());
//						newCheckInCheckOut.setState(checkInCheckOut.getState());
//						newCheckInCheckOut.setCountry(checkInCheckOut.getCountry());
						checkInOutRepository.save(newCheckInCheckOut);
					} else {
						checkInCheckOut.setUserId(userDetails.getUsername());
						checkInCheckOut.setCheckinActionFrom(deviceType);
						checkInCheckOut.setCheckinIpAddress(ProdConstant.getClientIp(request));
						checkInCheckOut.setDate(sdfDate.format(checkOutDaoImpl.getDate()));
						checkInCheckOut.setCheckinDateTime(sdfDateTime.format(getDateTime()));
						checkInCheckOut.setDayOfTheWeek(simpleDateformat.format(getDateTime()));
						checkInCheckOut.setTenantId(basicDetails.getTenantId());
						checkInCheckOut.setManagerId(basicDetails.getEmpWorkDetails().getReportingManager());
						checkInCheckOut.setFlag(ProdConstant.FLAG_Y);
						checkInCheckOut.setStatus(ProdConstant.PRESENT);
						checkInCheckOut.setIsActive(ProdConstant.TRUE);
						checkInCheckOut.setCreatedDate(checkOutDaoImpl.getDateTime());
						checkInCheckOut.setCreatedBy(empId);
						
						/* City,State & Country */
//						System.out.println("CITY in Dao :::::: " + checkInCheckOut.getCity());
//						checkInCheckOut.setCity(checkInCheckOut.getCity());
//						checkInCheckOut.setState(checkInCheckOut.getState());
//						checkInCheckOut.setCountry(checkInCheckOut.getCountry());
						
						checkInOutRepository.save(checkInCheckOut);
					}
			} else {

			}
		} catch (Exception e) {
			LOGGER.error("Error occur while set checkin data----- \n" + ExceptionUtils.getStackTrace(e));
		}
		return checkInCheckOut;
	}

	/* Check Out */
	@Override
	public CheckInCheckOut saveCheckOut(CheckInCheckOut checkInCheckOut, Device device, HttpServletRequest request)
			throws Exception {
		String userId = null;
		try {
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			userId = authentication.getName();
			
			String width = checkInCheckOut.getWidthForProgressBar();
			CheckInCheckOut userDetails = checkInOutRepository.findByDateAndCurrentUser2(userId);
			checkInCheckOut = userDetails;
			
			EmpBasicDetails basicDetails = empBasicRepository.getCurrentUser(userId, ProdConstant.TRUE);
			if (Objects.nonNull(basicDetails)) {
					/* Enable device detection */
					String deviceType = "";
					if (device.isMobile()) {
						deviceType = ProdConstant.MOBILE;
					} else if (device.isTablet()) {
						deviceType = ProdConstant.TABLET;
					} else if (device.isNormal()) {
						deviceType = ProdConstant.BROWSER;
					}

					final DecimalFormat df = new DecimalFormat("00");

					Optional<CheckInCheckOut> checkInCheckOut2 = checkInOutRepository.findByDateAndCurrentUser(userId);
					if (checkInCheckOut2.isPresent()) {
						CheckInCheckOut newCheckInCheckOut = checkInCheckOut2.get();
						newCheckInCheckOut.setCheckoutActionFrom(deviceType);
						newCheckInCheckOut.setCheckoutIpAddress(ProdConstant.getClientIp(request));
						newCheckInCheckOut.setCheckoutDateTime(sdfDateTime.format(getDateTime()));

						/* Time Duration */
						Date checkInDttm = sdfDateTime.parse(checkInCheckOut.getCheckinDateTime());
						Date checkOutDttm = sdfDateTime.parse(checkInCheckOut.getCheckoutDateTime());

						Long diff = checkOutDttm.getTime() - checkInDttm.getTime();
						Long hours = diff / (60 * 60 * 1000) % 24;
						Long minutes = diff / (60 * 1000) % 60;

						String s1 = df.format(hours);
						String s2 = df.format(minutes);

						String hourMinutes = s1 + ":" + s2 + " Hrs";
						newCheckInCheckOut.setTimeDuration(hourMinutes);

						newCheckInCheckOut.setFlag(ProdConstant.FLAG_N);
						newCheckInCheckOut.setWidthForProgressBar(width); 
						checkInOutRepository.save(newCheckInCheckOut);
					} else {
						System.out.println("checkOut else.......");
//						checkInCheckOut.setCheckoutActionFrom(deviceType);
//						checkInCheckOut.setCheckoutIpAddress(ProdConstant.getClientIp(request));
//						checkInCheckOut.setCheckoutDateTime(sdfDateTime.format(getDateTime()));
//
//						/* Time Duration */
//						Date checkInDttm = sdfDateTime.parse(checkInCheckOut.getCheckinDateTime());
//						Date checkOutDttm = sdfDateTime.parse(checkInCheckOut.getCheckoutDateTime());
//
//						Long diff = checkOutDttm.getTime() - checkInDttm.getTime();
//						Long hours = diff / (60 * 60 * 1000) % 24;
//						Long minutes = diff / (60 * 1000) % 60;
//
//						String s1 = df.format(hours);
//						String s2 = df.format(minutes);
//
//						String hourMinutes = s1 + ":" + s2 + " Hrs";
//
//						checkInCheckOut.setTimeDuration(hourMinutes);
//						checkInCheckOut.setFlag(ProdConstant.FLAG_N);
//						
//						System.out.println("IN dao Impl before save    ...."+ checkInCheckOut.getWidthForProgressBar());
//						checkInOutRepository.save(checkInCheckOut);
					}
			} else {

			}
		} catch (Exception e) {
			LOGGER.error("Error occur while set checkout data----- \n" + ExceptionUtils.getStackTrace(e));
		}
		return checkInCheckOut;
	}

	/* Display Total Login Time of Employee by userId */
	@Override
	public List<CheckInCheckOut> getTotalTime(String userId) throws Exception {
		List<CheckInCheckOut> checkInOutList = checkInOutRepository.getCurrentUser(userId, ProdConstant.TRUE);
		try {
			if (checkInOutList != null) {
				return checkInOutList;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display current user " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	/* Display Data for Employee Weekly */
	@Override
	public List<CheckInCheckOut> findByDayOfTheWeek(String from, String to, String userId) throws Exception {
		List<CheckInCheckOut> checkInOutList = checkInOutRepository.findByDayOfTheWeek(from, to, userId);
		try {
			if (!checkInOutList.isEmpty()) {
				return checkInOutList;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display current user weekly data " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	/* Mail Trigger (If Employee work Hour <= 8 ) */
	@Override
	public List<String> getNineHourNotComplete() throws Exception {
		try {
			List<String> checkInOutList = checkInOutRepository.getNineHourNotComplete(ProdConstant.HALFDAY);
			if (Objects.nonNull(checkInOutList)) {
				return checkInOutList;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display today date data" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	/* Employee Upcoming Birthday */
	@Override
	public List<EmpPersonalDetails> getUpcomingEmpBirthday(String tenantId) throws Exception {
		List<EmpPersonalDetails> personalDetailsList = empPersonalRepository.getUpcomingEmpBirthday(tenantId);
		try {
			if (!personalDetailsList.isEmpty()) {
				return personalDetailsList;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display employee birthdays " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	/* Update New Request */
	@Override
	public CheckInCheckOut updateNewRequest(CheckInCheckOut checkInCheckOut, Device device, HttpServletRequest request)
			throws Exception {
		String userId;
		try {
			SimpleDateFormat fromUser = new SimpleDateFormat(ProdConstant.DATETIMEFORMATE2);
			SimpleDateFormat myFormat2 = new SimpleDateFormat(ProdConstant.DATETIMEFORMATE);
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			userId = authentication.getName();

			EmpBasicDetails basicDetails = empBasicRepository.getCurrentUser(userId, ProdConstant.TRUE);
			final DecimalFormat df = new DecimalFormat("00");
			if (Objects.nonNull(checkInCheckOut)) {
				if (Objects.nonNull(basicDetails)) {
						/* Time Duration */
						SimpleDateFormat myFormat = new SimpleDateFormat(ProdConstant.DATETIMEFORMATE2, Locale.ENGLISH);

						Date from = myFormat.parse(checkInCheckOut.getCheckinDateTime());
						Date to = myFormat.parse(checkInCheckOut.getCheckoutDateTime());
						Long diff = to.getTime() - from.getTime();
						/* Day Calculate */
						// float daysBetween = TimeUnit.MILLISECONDS.toDays(diff) % 365;

						/* Hour & Minute Calculate */
						Long hourBetween = TimeUnit.MILLISECONDS.toHours(diff) % 24;
						Long minitBetween = TimeUnit.MILLISECONDS.toMinutes(diff) % 60;

						String hours = df.format(hourBetween);
						String minit = df.format(minitBetween);

						String hourMinutes = hours + ":" + minit + " Hrs";

						/* Day of the Week */
						Date now = myFormat.parse(checkInCheckOut.getCheckinDateTime());
						SimpleDateFormat simpleDateformat = new SimpleDateFormat(ProdConstant.DAY);

						/* Enable device detection */
						String deviceType = "";
						if (device.isMobile()) {
							deviceType = ProdConstant.MOBILE;
						} else if (device.isTablet()) {
							deviceType = ProdConstant.TABLET;
						} else if (device.isNormal()) {
							deviceType = ProdConstant.BROWSER;
						}

						Optional<CheckInCheckOut> checkInCheckOut2 = checkInOutRepository
								.getByUserIdAndDate(checkInCheckOut.getDate(), userId);
						if (checkInCheckOut2.isPresent()) {
							CheckInCheckOut newCheckInCheckOut = checkInCheckOut2.get();
							newCheckInCheckOut.setUserId(userId);
							newCheckInCheckOut.setManagerId(basicDetails.getEmpWorkDetails().getReportingManager());
							newCheckInCheckOut.setApprovalReq(ProdConstant.PENDING);
							newCheckInCheckOut.setDayOfTheWeek(simpleDateformat.format(now));
							newCheckInCheckOut.setTimeDuration(hourMinutes);
							newCheckInCheckOut.setTenantId(basicDetails.getTenantId());
							newCheckInCheckOut.setLastModifiedBy(userId);
							newCheckInCheckOut.setLastModifiedDate(getDateTime());
							newCheckInCheckOut.setCheckinActionFrom(deviceType);
							newCheckInCheckOut.setCheckoutActionFrom(deviceType);
							newCheckInCheckOut.setCheckinIpAddress(ProdConstant.getClientIp(request));
							newCheckInCheckOut.setCheckoutIpAddress(ProdConstant.getClientIp(request));
							newCheckInCheckOut.setIsActive(ProdConstant.TRUE);
							newCheckInCheckOut.setFlag(ProdConstant.FLAG_Y);
							newCheckInCheckOut.setStatus(ProdConstant.PENDING);
							newCheckInCheckOut.setAddNewReq(ProdConstant.TRUE);
							newCheckInCheckOut.setDate(checkInCheckOut.getDate());
							newCheckInCheckOut.setCheckinDateTime(myFormat2.format(fromUser.parse(checkInCheckOut.getCheckinDateTime())));
							newCheckInCheckOut.setCheckoutDateTime(myFormat2.format(fromUser.parse(checkInCheckOut.getCheckoutDateTime())));
							newCheckInCheckOut.setDescription(checkInCheckOut.getDescription());
							newCheckInCheckOut.setReason(checkInCheckOut.getReason());
							newCheckInCheckOut.setNotification(ProdConstant.UNREAD);
							checkInOutRepository.save(newCheckInCheckOut);
							return newCheckInCheckOut;
						} else {
							checkInCheckOut.setUserId(userId);
							checkInCheckOut.setManagerId(basicDetails.getEmpWorkDetails().getReportingManager());
							checkInCheckOut.setApprovalReq(ProdConstant.PENDING);
							checkInCheckOut.setDayOfTheWeek(simpleDateformat.format(now));
							checkInCheckOut.setTimeDuration(hourMinutes);
							checkInCheckOut.setTenantId(basicDetails.getTenantId());
							checkInCheckOut.setCheckinActionFrom(deviceType);
							checkInCheckOut.setCheckoutActionFrom(deviceType);
							checkInCheckOut.setCheckinIpAddress(ProdConstant.getClientIp(request));
							checkInCheckOut.setCheckoutIpAddress(ProdConstant.getClientIp(request));
							checkInCheckOut.setIsActive(ProdConstant.TRUE);
							checkInCheckOut.setFlag(ProdConstant.FLAG_Y);
							checkInCheckOut.setStatus(ProdConstant.PENDING);
							checkInCheckOut.setAddNewReq(ProdConstant.TRUE);
							checkInCheckOut.setNotification(ProdConstant.UNREAD);
							checkInCheckOut.setCreatedDate(getDateTime());
							checkInCheckOut.setCreatedBy(userId);
							checkInCheckOut.setCheckinDateTime(myFormat2.format(fromUser.parse(checkInCheckOut.getCheckinDateTime())));
							checkInCheckOut.setCheckoutDateTime(myFormat2.format(fromUser.parse(checkInCheckOut.getCheckoutDateTime())));

							checkInOutRepository.save(checkInCheckOut);
						}
				} else {

				}

			}
		} catch (Exception e) {
			LOGGER.error("Error occur while update new request" + ExceptionUtils.getStackTrace(e));
		}
		return checkInCheckOut;
	}

	/* Cancel New add req by employee */
	@Override
	public CheckInCheckOut cancelAddReq(CheckInCheckOut checkInCheckOut) throws Exception {
		try {
			Optional<CheckInCheckOut> checkInCheckOut2 = checkInOutRepository.findById(checkInCheckOut.getId());
			if (checkInCheckOut2.isPresent()) {
				CheckInCheckOut newcheckInCheckOut2 = checkInCheckOut2.get();
				newcheckInCheckOut2.setIsActive(ProdConstant.FALSE);
				checkInOutRepository.save(newcheckInCheckOut2);
			} else {
				checkInOutRepository.save(checkInCheckOut);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while cancel request-----" + ExceptionUtils.getStackTrace(e));
		}
		return checkInCheckOut;
	}

	/* Display Employee Attendance request in Manager Dashboard */
	@Override
	public List<CheckInCheckOut> getAttendancePending(String userId, String tenantId) throws Exception {
		List<CheckInCheckOut> checkInCheckOutList = new ArrayList<>();
		try {
			checkInCheckOutList = checkInOutRepository.getAttendancePending(userId, ProdConstant.TRUE,
					ProdConstant.TRUE, ProdConstant.PENDING,tenantId);
		} catch (Exception e) {
			LOGGER.error("Error occur while display attendance pending request in manager dashboard"
					+ ExceptionUtils.getStackTrace(e));
		}
		return checkInCheckOutList;
	}

	@Override
	public List<String> getDateAndUserId() throws Exception {
		List<String> listOfUserWithDate = checkInOutRepository.getDateAndUserId(ProdConstant.TRUE);
		try {
			if (Objects.nonNull(listOfUserWithDate)) {
				return listOfUserWithDate;
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while get date and userId " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public void insertEmployee(String userId, Date createdDate, String date, String dayOfTheWeek, String tenantId, String isActive,
			String checkinDateTime, String checkoutDateTime, String timeDuration, String status) throws Exception {
		try {
			checkInOutRepository.insertEmployee(userId, createdDate, date, dayOfTheWeek, tenantId, isActive, checkinDateTime,
					checkoutDateTime, timeDuration, status);
		} catch (Exception e) {
			LOGGER.error("Error occur while insert absent employee data " + ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public CheckInCheckOut findByEmpId(String userId) throws Exception {
		CheckInCheckOut checkInCheckOut = checkInOutRepository.findByEmpId(userId, ProdConstant.TRUE);
		try {
			if (Objects.nonNull(checkInCheckOut)) {
				return checkInCheckOut;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while find employee by employee ID " + ExceptionUtils.getStackTrace(e));
		}
		return checkInCheckOut;
	}

	@Override
	public Long getPresentDays(String userId) throws Exception {
		try {
			return checkInOutRepository.getPresentDays(userId, ProdConstant.TRUE);
		} catch (Exception e) {
			LOGGER.error(
					"Error while get present days of employee in current month " + ExceptionUtils.getStackTrace(e));
		}
		return (long) 0;
	}

	@Override
	public Long getLateDays(String userId) throws Exception {
		try {
			return checkInOutRepository.getLateDays(userId, ProdConstant.LATEDAY);
		} catch (Exception e) {
			LOGGER.error("Error while get late days of employee in current month " + ExceptionUtils.getStackTrace(e));
		}
		return (long) 0;
	}

	@Override
	public Long getHalfDays(String userId) throws Exception {
		try {
			return checkInOutRepository.getHalfDays(userId, ProdConstant.HALFDAY);
		} catch (Exception e) {
			LOGGER.error("Error while get half days of employee in current month " + ExceptionUtils.getStackTrace(e));
		}
		return (long) 0;
	}

	@Override
	public Long getAbsentDays(String userId) throws Exception {
		try {
			return checkInOutRepository.getAbsentDays(userId, ProdConstant.FALSE);
		} catch (Exception e) {
			LOGGER.error("Error while get absent days of employee in current month " + ExceptionUtils.getStackTrace(e));
		}
		return (long) 0;
	}

	@Override
	public List<CheckInCheckOut> getNewReqList(String empId) throws Exception {
		List<CheckInCheckOut> checkInCheckOut = checkInOutRepository.getNewReqList(empId, ProdConstant.TRUE,
				ProdConstant.TRUE);
		try {
			if (Objects.nonNull(checkInCheckOut)) {
				return checkInCheckOut;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display new request list " + ExceptionUtils.getStackTrace(e));
		}
		return checkInCheckOut;
	}

	@Override
	public List<CheckInCheckOut> cancelReqList(String empId) throws Exception {
		List<CheckInCheckOut> checkInCheckOut = checkInOutRepository.cancelReqList(empId, ProdConstant.TRUE,
				ProdConstant.FALSE);
		try {
			if (Objects.nonNull(checkInCheckOut)) {
				return checkInCheckOut;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display cancel request list " + ExceptionUtils.getStackTrace(e));
		}
		return checkInCheckOut;
	}

	@Override
	public CheckInCheckOut sendMail(String userId) throws Exception {
		CheckInCheckOut checkInCheckOut = checkInOutRepository.sendMail(userId);
		try {
			if (Objects.nonNull(checkInCheckOut)) {
				return checkInCheckOut;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while send mail to hr & manager " + ExceptionUtils.getStackTrace(e));
		}
		return checkInCheckOut;
	}

	@Override
	public List<CheckInCheckOut> getEmpWithManger(String empId,  String tenantId) throws Exception {
		List<CheckInCheckOut> checkInCheckOut = checkInOutRepository.getEmpWithManger(empId, ProdConstant.TRUE, tenantId);
		try {
			if (Objects.nonNull(checkInCheckOut)) {
				return checkInCheckOut;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display employee record to manager " + ExceptionUtils.getStackTrace(e));
		}
		return checkInCheckOut;
	}

	@Override
	public List<CheckInCheckOut> getAcceptLeaveById(Long id) throws Exception {
		List<CheckInCheckOut> checkInCheckOut = checkInOutRepository.getAcceptLeaveById(id);
		try {
			if (Objects.nonNull(checkInCheckOut)) {
				return checkInCheckOut;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while accept/reject request by id " + ExceptionUtils.getStackTrace(e));
		}
		return checkInCheckOut;
	}

	@Override
	public void acceptStatus(String approvalReq, String status, String isActive, String flag, Long id)
			throws Exception {
		try {
			checkInOutRepository.acceptStatus(approvalReq, status, isActive, flag, id);
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while save data if manager accept/reject request " + ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public List<CheckInCheckOut> getByUserIdAndCurentDate(List<String> userId) throws Exception {
		List<CheckInCheckOut> checkInCheckOuts = checkInOutRepository.getByUserIdAndCurentDate(userId);
		try {
			if (Objects.nonNull(checkInCheckOuts)) {
				return checkInCheckOuts;
			} else {
				System.out.println("Null Data Found :::::");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get list of userId with currentDate " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<CheckInCheckOut> getTotalAbsentEmp(String tenantId) throws Exception {
		List<CheckInCheckOut> checkInCheckOuts = checkInOutRepository.getTotalAbsentEmp(ProdConstant.ABSENT,
				ProdConstant.FALSE, tenantId);
		try {
			if (Objects.nonNull(checkInCheckOuts)) {
				return checkInCheckOuts;
			} else {
				System.out.println("Null Data Get :::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get list of total absent emp " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<CheckInCheckOut> getTotalAttendance(String tenantId) throws Exception {
		List<CheckInCheckOut> checkInCheckOuts = checkInOutRepository.getTotalAttendance(ProdConstant.PRESENT,
				ProdConstant.TRUE, tenantId);
		try {
			if (Objects.nonNull(checkInCheckOuts)) {
				return checkInCheckOuts;
			} else {
				System.out.println("Null Data Get :::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get list of total attendance " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<CheckInCheckOut> getTotalNewReq() throws Exception {
		List<CheckInCheckOut> checkInCheckOuts = checkInOutRepository.getTotalNewReq(ProdConstant.APPROVED,
				ProdConstant.TRUE, ProdConstant.TRUE);
		try {
			if (Objects.nonNull(checkInCheckOuts)) {
				return checkInCheckOuts;
			} else {
				System.out.println("Null Data Get :::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get list of total new req. " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public Long getAllAttendance(String tenantId) throws Exception {
		try {
			return checkInOutRepository.getAllAttendance(ProdConstant.PRESENT, ProdConstant.TRUE, tenantId);
		} catch (Exception e) {
			LOGGER.error("Error occuring while get all attendance of employees in current month "
					+ ExceptionUtils.getStackTrace(e));
		}
		return (long) 0;
	}

	@Override
	public Long getAllEmpAbsent(String tenantId) throws Exception {
		try {
			return checkInOutRepository.getAllEmpAbsent(ProdConstant.FALSE, tenantId);
		} catch (Exception e) {
			LOGGER.error("Error occuring while get all absent of employees in current month "
					+ ExceptionUtils.getStackTrace(e));
		}
		return (long) 0;
	}

	@Override
	public Long getAllNewReq(String tenantId) throws Exception {
		try {
			return checkInOutRepository.getAllNewReq(ProdConstant.APPROVED, ProdConstant.TRUE, ProdConstant.TRUE, tenantId);
		} catch (Exception e) {
			LOGGER.error("Error occuring while get all new req of employees in current month "
					+ ExceptionUtils.getStackTrace(e));
		}
		return (long) 0;
	}

	@Override
	public CheckInCheckOut findByDateAndCurrentUser2(String userId) throws Exception {
		CheckInCheckOut checkInOutList = checkInOutRepository.findByDateAndCurrentUser2(userId);
		try {
			if (checkInOutList != null) {
				return checkInOutList;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display current user with today date " + ExceptionUtils.getStackTrace(e));
		}
		return new CheckInCheckOut();
	}

	@Override
	public CheckInCheckOut notificationRead(Long id) throws Exception {
		try {
			Optional<CheckInCheckOut> optional = checkInOutRepository.findById(id);
			if (optional.isPresent()) {
				CheckInCheckOut checkInOut = optional.get();
				checkInOut.setNotification(ProdConstant.READ);

				checkInOutRepository.save(checkInOut);
				return checkInOut;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while read notification /n" + ExceptionUtils.getStackTrace(e));
		}
		return new CheckInCheckOut();
	}

	@Override
	public List<CheckInCheckOut> addNewReqNotificationBell(String userId) throws Exception {
		List<CheckInCheckOut> checkInCheckOuts = checkInOutRepository.addNewReqNotificationBell(userId,
				ProdConstant.TRUE, ProdConstant.TRUE);
		try {
			if (checkInCheckOuts != null) {
				return checkInCheckOuts;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display list of notification " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<CheckInCheckOut> unreadNotification(String userId) throws Exception {
		List<CheckInCheckOut> checkInCheckOuts = checkInOutRepository.unreadNotification(userId, ProdConstant.UNREAD,
				ProdConstant.TRUE);
		try {
			if (checkInCheckOuts != null) {
				return checkInCheckOuts;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display list of unread notification " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public CheckInCheckOut getCurrentUserWithId(String userId, Long id) {
		CheckInCheckOut checkInCheckOut = new CheckInCheckOut();
		try {
			checkInCheckOut = checkInOutRepository.getCurrentUserWithId(userId, id, ProdConstant.TRUE);
		} catch (Exception e) {
			LOGGER.error("Error occur while get details of user " + ExceptionUtils.getStackTrace(e));
		}
		return checkInCheckOut;
	}
	@Override
	public List<CheckInCheckOut> currentWeekData(String from, String to, String userId, String isActive)
			throws Exception {
		List<CheckInCheckOut> currentWeekData = checkInOutRepository.currentWeekData(from,to,userId,isActive);
		try {
			if(Objects.nonNull(currentWeekData)) {
				return currentWeekData;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while get current week data  " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}
}