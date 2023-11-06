package com.ksv.ktrccrm.controller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ksv.ktrccrm.db1.entities.Assets;
import com.ksv.ktrccrm.db1.entities.CheckInCheckOut;
import com.ksv.ktrccrm.db1.entities.ExitActivity;
import com.ksv.ktrccrm.db1.entities.ExpenseReimb;
import com.ksv.ktrccrm.db1.entities.LeaveMst;
import com.ksv.ktrccrm.service.AssetsService;
import com.ksv.ktrccrm.service.CheckInCheckOutService;
import com.ksv.ktrccrm.service.ExitActivityService;
import com.ksv.ktrccrm.service.ExpenseReimbService;
import com.ksv.ktrccrm.service.LeaveService;

@Controller
public class NotificationAlertController {
	private static final Logger LOG = LogManager.getLogger(NotificationAlertController.class);

	@Autowired
	private CheckInCheckOutService checkInCheckOutService;
	@Autowired
	private ExitActivityService exitActivityService;
	@Autowired
	private ExpenseReimbService expenseReimbService;
	@Autowired
	private LeaveService leaveService;
	@Autowired
	private AssetsService assetsService;

	/* Add New Req Notification Alert */
	@GetMapping("/addNewReqNotificationAlert")
	public @ResponseBody String addNewReqNotificationAlert(@ModelAttribute CheckInCheckOut checkInCheckOut, Model model)
			throws Exception {
		String notification = "Read";
		String userId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			userId = authentication.getName();

			for (CheckInCheckOut checkInCheckOut2 : checkInCheckOutService.unreadNotification(userId)) {
				if ("Unread".equals(checkInCheckOut2.getNotification())) {
					notification = "Unread";
				} else {
					notification = "Read";
				}
			}
		} catch (Exception e) {
			LOG.error("Error occur while notification alert " + ExceptionUtils.getStackTrace(e));
		}
		return notification;
	}

	/* For Read Add New Req Notification Msg */
	@GetMapping(value = "/addNewReqNotification/{id}")
	public String getEmpIdAddNewReqNotification(@PathVariable(name = "id") Long id, Model model) throws Exception {
		try {
			checkInCheckOutService.notificationRead(id);
		} catch (Exception e) {
			LOG.error("Error occur while read notification ... " + ExceptionUtils.getStackTrace(e));
		}
		return "redirect:/approvalNewReqList";
	}

	/*-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

	/* Exit Activity Notification Alert */
	@GetMapping("/exitActivityNotificationAlert")
	public @ResponseBody String exitActivityNotificationAlert(@ModelAttribute ExitActivity exitActivity, Model model)
			throws Exception {
		String notification = "Read";
		String userId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			userId = authentication.getName();

			for (ExitActivity exitActivity2 : exitActivityService.unreadNotification(userId)) {
				if ("Unread".equals(exitActivity2.getNotification())) {
					notification = "Unread";
				} else {
					notification = "Read";
				}
			}
		} catch (Exception e) {
			LOG.error("Error occur while exitActivity notification alert " + ExceptionUtils.getStackTrace(e));
		}
		return notification;
	}

	/* For Read Exit Activity Notification Msg */
	@GetMapping(value = "/exitActivityNotification/{id}")
	public String getEmpIdexitActivityNotification(@PathVariable(name = "id") Long id, Model model) throws Exception {
		try {
			exitActivityService.notificationRead(id);
		} catch (Exception e) {
			LOG.error("Error occur while read exitActivity notification ... " + ExceptionUtils.getStackTrace(e));
		}
		return "redirect:/appliedExitActivityList";
	}

	/*-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

	/* Expense Notification Alert */
	@GetMapping("/expenseNotificationAlert")
	public @ResponseBody String expenseNotificationAlert(@ModelAttribute ExpenseReimb expenseReimb, Model model)
			throws Exception {
		String notification = "Read";
		String userId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			userId = authentication.getName();

			for (ExpenseReimb expenseReimb2 : expenseReimbService.unreadNotification()) {
				if ("Unread".equals(expenseReimb2.getNotification())) {
					notification = "Unread";
				} else {
					notification = "Read";
				}
			}
		} catch (Exception e) {
			LOG.error("Error occur while expense notification alert " + ExceptionUtils.getStackTrace(e));
		}
		return notification;
	}

	/* For Read Expense Notification Msg */
	@GetMapping(value = "/expenseNotification/{id}")
	public String getEmpIdexpenseNotification(@PathVariable(name = "id") Long id, Model model) throws Exception {
		try {
			expenseReimbService.notificationRead(id);
			ExpenseReimb expenseReimbs = expenseReimbService.getExpenseById(id);
			model.addAttribute("expenseReimbList", expenseReimbs);
		} catch (Exception e) {
			LOG.error("Error occur while read expense notification ... " + ExceptionUtils.getStackTrace(e));
		}
		return "expenseReimb/appliedExpenseList";
	}

	/*-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

	/* Leave Notification Alert */
	@GetMapping("/leaveNotificationAlert")
	public @ResponseBody String leaveNotificationAlert(@ModelAttribute LeaveMst leaveMst, Model model)
			throws Exception {
		String notification = "Read";
		String userId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			userId = authentication.getName();

			for (LeaveMst leaveMst2 : leaveService.unreadNotification(userId)) {
				if ("Unread".equals(leaveMst2.getNotification())) {
					notification = "Unread";
				} else {
					notification = "Read";
				}
			}
		} catch (Exception e) {
			LOG.error("Error occur while leave notification alert " + ExceptionUtils.getStackTrace(e));
		}
		return notification;
	}
	
	/* For Read Leave Notification Msg */
	@GetMapping(value = "/leaveNotification/{id}")
	public String getEmpIdleaveNotification(@PathVariable(name = "id") Long id, Model model) throws Exception {
		try {
			leaveService.notificationRead(id);
		} catch (Exception e) {
			LOG.error("Error occur while read leave notification ... " + ExceptionUtils.getStackTrace(e));
		}
		return "redirect:/appliedLeaveList";
	}
	
	/*-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
	
	// Assets Notification Alert
	@GetMapping("/assetsNotificationAlert")
	public @ResponseBody String assetsNotificationAlert(@ModelAttribute Assets assets, Model model) throws Exception {
		String notification = "Read";
		String userId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			userId = authentication.getName();

			for (Assets assets2 : assetsService.unreadNotification(userId)) {
				if ("Unread".equals(assets2.getNotification())) {
					notification = "Unread";
				} else {
					notification = "Read";
				}
			}
		} catch (Exception e) {
			LOG.error("Error occur while assets notification alert " + ExceptionUtils.getStackTrace(e));
		}
		return notification;

	}

	/* For Read Assets Notification Msg */
	@GetMapping(value = "/assetsNotification/{id}")
	public String assetsNotificationRead(@PathVariable(name = "id") Long id, Model model) throws Exception {
		try {
			assetsService.assetsNotificationRead(id);
		} catch (Exception e) {
			LOG.error("Error occur while assets read notification ... " + ExceptionUtils.getStackTrace(e));
		}
		return "redirect:/employeeAssetsList";
    
	}  
	/*-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

}
