package com.ksv.ktrccrm.controller;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.EmailAndOTPService;
import com.ksv.ktrccrm.service.UserService;

@Controller
public class ForgotPasswordController {
	private static final Logger LOGGER = LogManager.getLogger(ForgotPasswordController.class);

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private EmailAndOTPService emailAndOTPService;
	@Autowired
	private UserService userService;

	@Value("${global.redirectmsg.otpSend}")
	private String successOtpSend;
	@Value("${global.redirectmsg.successVerifyOtp}")
	private String successVerifyOtp;
	@Value("${global.redirectmsg.wrongVerifyOtp}")
	private String wrongVerifyOtp;
	@Value("${global.redirectmsg.emailIdfailed}")
	private String emailIdfailed;
	@Value("${global.redirectmsg.pwdNotMatch}")
	private String pwdNotMatch;
	@Value("${global.redirectmsg.resetPwdSuccess}")
	private String resetPwdSuccess;
	
	@GetMapping("/forgot")
	public String forgotPwdHomePage(Model model) throws Exception {
		try {
			System.out.println("Forgot Password Home Page :::::::::::::::::");
		} catch (Exception e) {
			LOGGER.error("Error occur while display forgot password page" + ExceptionUtils.getStackTrace(e));
		}
		return "forgotPassword/forgot";
	}

	@PostMapping("/sendOtp")
	public String sendOtp(@RequestParam("emailId") String emailId, Model model, RedirectAttributes redirAttrs,
			HttpSession session) throws Exception {
		try {
			/* Generating OTP of 6 Digit */
			Random random = new Random();
			int randomOTP = random.nextInt(999999);

			List<UserMst> getEmailId = entityManager
					.createQuery("SELECT e FROM UserMst e WHERE e.emailId = ?1 AND e.isActive = ?2", UserMst.class)
					.setParameter(1, emailId)
					.setParameter(2, ProdConstant.ISACTIVE).getResultList();

			userService.setRandomOTP(randomOTP, emailId);
			session.setAttribute("email", emailId);
			for (UserMst user : getEmailId) {
				if (user.getEmailId().equals(emailId)) {
					/* Write Code for Send OTP to Email */
					String body = "<h1> OTP = " + randomOTP + " </h1>";
					String subject = "OTP From KSVSoft-Tech";
					String to = emailId;
					String from = "springbootmail.2022@gmail.com";
					emailAndOTPService.emailsend(from, to, subject, body);
					redirAttrs.addFlashAttribute(ProdConstant.OTPSEND, successOtpSend);
					return "forgotPassword/otp";
				} else {
					return "redirect:/forgot";
				}
			}
			redirAttrs.addFlashAttribute(ProdConstant.FAILED, emailIdfailed);
		} catch (Exception e) {
			LOGGER.error("Error occur while send otp" + ExceptionUtils.getStackTrace(e));
		}
		return "redirect:/forgot";
	}

	@GetMapping("/otp")
	public String otpPage(Model model) throws Exception {
		try {
		} catch (Exception e) {
			LOGGER.error("Error occur while otp page" + ExceptionUtils.getStackTrace(e));
		}
		return "forgotPassword/otp";
	}

	@PostMapping("/verifyOtp")
	public String otpPage(@RequestParam("otp") Integer otp, Model model, RedirectAttributes redirAttrs,
			HttpSession session) throws Exception {
		try {
			String emailId = (String) session.getAttribute("email");
			List<UserMst> getRandomOTP = entityManager
					.createQuery("SELECT e FROM UserMst e WHERE e.emailId = ?1 AND e.isActive = ?2", UserMst.class)
					.setParameter(1, emailId).setParameter(2, ProdConstant.ISACTIVE).getResultList();
			for (UserMst userMst : getRandomOTP) {
				if (userMst.getRandomOTP().equals(otp)) {
					redirAttrs.addFlashAttribute(ProdConstant.OTPSUCCESS, successVerifyOtp);
					return "forgotPassword/reset";
				} else {
					redirAttrs.addFlashAttribute(ProdConstant.WRONGOTP, wrongVerifyOtp);
					return "redirect:/otp";
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display otp verify page" + ExceptionUtils.getStackTrace(e));
		}
		return "redirect:/otp";
	}

	@GetMapping("/reset")
	public String resetPage(Model model) throws Exception {
		try {
		} catch (Exception e) {
			LOGGER.error("Error occur while display reset password page" + ExceptionUtils.getStackTrace(e));
		}
		return "forgotPassword/reset";
	}

	@PostMapping("/resetPassword")
	public String resetPassword(@ModelAttribute UserMst userMst, @RequestParam("password") String password,
			@RequestParam("confirmPassword") String confirmPassword, HttpSession session, Model model,
			BindingResult result, RedirectAttributes redirAttrs) throws Exception {
		String emailId = (String) session.getAttribute("email");
		try {
			if (result.hasErrors()) {
				List<ObjectError> allErrors = result.getAllErrors();
				for (ObjectError temp : allErrors) {
					System.out.println(temp);
				}
				return "forgotPassword/reset";
			} else if (!Objects.equals(password, confirmPassword)) {
				redirAttrs.addFlashAttribute(ProdConstant.PWDNOTMATCH, pwdNotMatch);
				return "redirect:/reset";
			} else {
				password = bCryptPasswordEncoder.encode(password);
				String passwordToken = UUID.randomUUID().toString();
				userService.resetPassword(password, passwordToken, emailId);
				redirAttrs.addFlashAttribute(ProdConstant.PASSWORD,	resetPwdSuccess);
				return "redirect:/login";
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display reset password" + ExceptionUtils.getStackTrace(e));
		}
		return "forgotPassword/reset";
	}
}