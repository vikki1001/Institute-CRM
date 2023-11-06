package com.ksv.ktrccrm.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.db1.entities.AuditRecord;
import com.ksv.ktrccrm.db1.entities.CustomerDetails;
import com.ksv.ktrccrm.service.AuditRecordService;
import com.ksv.ktrccrm.service.CustomerDetailsService;

@Controller
public class CustomerDetailsController {
	private static final Logger LOGGER = LogManager.getLogger(CustomerDetailsController.class);

	@Autowired
	private AuditRecordService auditRecordService;
	@Autowired
	private CustomerDetailsService customerDetailsService;

	/* Display List of Active Customer */
	@GetMapping("/activeCustomerList")
	public String activeCustomerList(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			List<CustomerDetails> customerDetailsList = customerDetailsService.activeCustomerList(ProdConstant.ISACTIVE);
			if (customerDetailsList != null && !customerDetailsList.isEmpty()) {
				model.addAttribute("activeCustomerList", customerDetailsList);
			} else {
				System.out.println("No Data in Active Customer List");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display active customer list... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed active customer list"));
			auditRecord.setMenuCode("CUSTOMER MANAGEMENT");
			auditRecord.setSubMenuCode("Customer Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "customer/activeListCustomer";
	}

	/* Display List of InActive Customer */
	@GetMapping("/inActiveCustomerList")
	public String inActiveCustomerList(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			List<CustomerDetails> customerDetailsList = customerDetailsService.inActiveCustomerList(ProdConstant.INACTIVE);
			if (customerDetailsList != null && !customerDetailsList.isEmpty()) {
				model.addAttribute("inActiveCustomerList", customerDetailsList);
			} else {
				System.out.println("No Data in inActive Customer List");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display inactive customer list... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed inactive customer list"));
			auditRecord.setMenuCode("CUSTOMER MANAGEMENT");
			auditRecord.setSubMenuCode("Customer Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "customer/inActiveListCustomer";
	}

	@GetMapping("/customerHome")
	public String customerHome(Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			CustomerDetails customerDetails = new CustomerDetails();
			model.addAttribute("customerDetails", customerDetails);
		} catch (Exception e) {
			LOGGER.error("Error occur while display customer home page... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed customer home page"));
			auditRecord.setMenuCode("CUSTOMER MANAGEMENT");
			auditRecord.setSubMenuCode("Customer Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "customer/newCustomer";
	}

	@PostMapping("/saveCustomer")
	public String saveCustomer(@ModelAttribute CustomerDetails customerDetails, BindingResult result, Model model,
			RedirectAttributes redirAttrs, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			if (result.hasErrors()) {
				List<ObjectError> allErrors = result.getAllErrors();
				for (ObjectError temp : allErrors) {
					System.out.println(temp);
				}
				return "customer/newCustomer";
			} else {
				customerDetailsService.saveCustomer(customerDetails);
				redirAttrs.addFlashAttribute("success", "Record Insert Successfully");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while insert customer record ... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();	
			auditRecord.setRemarks(userDetails.getUsername().concat(" - create customer successfully "));
			auditRecord.setMenuCode("CUSTOMER MANAGEMENT");
			auditRecord.setSubMenuCode("Customer Management");
			auditRecord.setActivityCode("CREATE/UPDATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeCustomerList";
	}
	
	/* Update of Registered Customer */
	@GetMapping("/updateCustomer/{id}")
	public String updateCustomer(@ModelAttribute CustomerDetails customerDetails, @PathVariable(name = "id") Long id,
			Model model, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			model.addAttribute("customerDetails", customerDetailsService.findCustomerById(id));
		} catch (Exception e) {
			LOGGER.error("Error occur while display customer update page... " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - viewed customer update page"));
			auditRecord.setMenuCode("CUSTOMER MANAGEMENT");
			auditRecord.setSubMenuCode("Customer Management");
			auditRecord.setActivityCode("VIEW");
			auditRecordService.save(auditRecord, device);
		}
		return "customer/newCustomer";
	}
	
	/* Activate of Registered Customer */
	@GetMapping(value = "/activeCustomer/{id}")
	public String activeCustomer(@PathVariable(name = "id") Long id, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			 customerDetailsService.activeCustomer(id);
		} catch (Exception e) {
			LOGGER.error("Error occur while activate customer " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - activate customer id - " + id));
			auditRecord.setMenuCode("CUSTOMER MANAGEMENT");
			auditRecord.setSubMenuCode("Customer Management");
			auditRecord.setActivityCode("ACTIVATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/activeCustomerList";
	}
	
	/* Deactivate of Registered Customer */
	@GetMapping(value = "/deactiveCustomer/{id}")
	public String deactiveCustomer(@PathVariable(name = "id") Long id, Device device) throws Exception {
		AuditRecord auditRecord = new AuditRecord();
		try {
			 customerDetailsService.deactiveCustomer(id);
		} catch (Exception e) {
			LOGGER.error("Error occur while deactivate customer " + ExceptionUtils.getStackTrace(e));
		} finally {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			auditRecord.setRemarks(userDetails.getUsername().concat(" - deactivate customer id - " + id));
			auditRecord.setMenuCode("CUSTOMER MANAGEMENT");
			auditRecord.setSubMenuCode("Customer Management");
			auditRecord.setActivityCode("DEACTIVATE");
			auditRecordService.save(auditRecord, device);
		}
		return "redirect:/inActiveCustomerList";
	}
}
