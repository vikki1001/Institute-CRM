//package com.ksv.ktrccrm.security;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.lang3.exception.ExceptionUtils;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
//import org.springframework.stereotype.Component;
//
//import com.itextpdf.io.IOException;
//import com.ksv.ktrccrm.db1.entities.UserMst;
//import com.ksv.ktrccrm.service.UserService;
//
//@Component
//public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
//	private static final Logger LOGGER = LogManager.getLogger(LoginFailureHandler.class);
//
//	@Autowired
//	private UserService userService;
//
//	@Override
//	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
//			AuthenticationException exception) throws IOException, ServletException, java.io.IOException {
//		try {
//			String loginId = request.getParameter("loginId").toUpperCase();
//			String password = request.getParameter("password");
//			String captcha = request.getParameter("captcha");
//			String hiddenCaptcha = request.getParameter("hiddenCaptcha");
//
//			System.out.println("onAuthenticationFailure --------------- ");
//			
//			System.out.println("onAuthenticationFailure loginId: " + loginId);
//			request.setAttribute("loginId", loginId);
//
//			String redirectURL = "/login?error&loginId=" + loginId;
//
//			if (exception.getMessage().contains("OTP")) {
//				System.out.println("IF in onAuthenticationFailure ::::::::: ");
//				redirectURL = "/login?otp=true&loginId=" + loginId;
//			} else {
//				System.out.println("ELSE in onAuthenticationFailure ::::::::: ");
//				UserMst userMst = userService.findByLoginId1(loginId);
//				if (userMst.isOTPRequired()) {
//					System.out.println("ELSE IF in onAuthenticationFailure ::::::::: ");
//					redirectURL = "/login?otp=true&loginId=" + loginId;
//				}
//			}
//
//			super.setDefaultFailureUrl(redirectURL);
//			super.onAuthenticationFailure(request, response, exception);
//		} catch (Exception e) {
//			LOGGER.error("Error occur while onAuthenticationFailure " + ExceptionUtils.getStackTrace(e));
//		}
//
//	}
//
////	@Override
////	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
////			AuthenticationException exception) throws IOException, ServletException, java.io.IOException {
////		try {
////			String username = request.getParameter("loginId").toUpperCase();
////			String password = request.getParameter("password");
////			String captcha = request.getParameter("captcha");
////			String hiddenCaptcha = request.getParameter("hiddenCaptcha");
////
////			System.out.println("onAuthenticationFailure --------------- ");
////			UserMst user = userService.findByLoginId1(username);
////			if (user != null) {
////				if (user.getIsUserLocked() != null) {
////					if (user.getIsUserLoggedIn() < ProdConstant.MAX_FAILED_ATTEMPTS - 1) {
////						userService.increaseFailedAttempts(user);
////						System.out.println("1111111111111111111111111111111111111111");
//////						request.setAttribute("email", user.getEmailId());
//////						redirectURL = "/login?error";
////
////						if (exception.getMessage().contains("OTP")) {
////							System.out.println("22222222222222222222222222222222222");
////							redirectURL = "/login?otp=true&loginId1=" + username;
////						} else {
////							UserMst userMst = userService.findByLoginId1(username);
////							System.out.println("333333333333333333333333333333333333333");
////							// if(userMst.getOtpRequestedTime() == null) {
////							System.out.println("444444444444444444444444444444444444444444");
////
////							/*--------------------------------------------------------------------------------*/
////							PasswordPolicy passwordPolicies = passwordPolicyService
////									.findByTenantIdAndIsactive(user.getTenantId(), String.valueOf(user.getIsActive()));
////
////							Integer pwd = passwordPolicies.getPasswordValidateDays();
////							System.out.println("Password Validate Days ::::::: " + pwd);
////							Date ort = user.getOtpRequestedTime();
////
////							LocalDate dateBefore = ort.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
////							LocalDate dateAfter = LocalDate.now();
////
////							long daysDiff = ChronoUnit.DAYS.between(dateBefore, dateAfter);
////							System.out.println(
////									"The number of days between dates onAuthenticationFailure ::::::: " + daysDiff);
////							/*--------------------------------------------------------------------------------*/
////
////							if (userMst.isOTPRequired()) {
////								System.out.println("55555555555555555555555555555555");
////								request.setAttribute("email", user.getEmailId());
////								redirectURL = "/login?otp=true&loginId2=" + username;
////							} else if (userMst.getOtpRequestedTime() != null && daysDiff != pwd || daysDiff != ProdConstant.CHECKDAY) {
////								System.out.println("login failure handle -=@@@@@@@@@@@@@@@@@");
////								redirectURL = "/login?otp=true&loginId2=" + username;
////							} else {
////								System.out.println("F0");
////
////								if (Objects.equals(username, user.getLoginId()) && bCryptPasswordEncoder.matches(password, user.getPassword()) && Objects.equals(captcha, hiddenCaptcha)) {
////									System.out.println("login success handle -=@@@@@@@@@@@@@@@@@");
////									response.sendRedirect(request.getContextPath() + "/loginSuccess");
////								} else if (!Objects.equals(username, userMst.getLoginId()) && !Objects.equals(password, userMst.getPassword()) && !Objects.equals(captcha, hiddenCaptcha)) {
////									System.out.println("onAuthenticationFailure Wrong id & password & Captcha Insert ::::::::::::---------");
////									redirectURL = "/login?error3";
////								} else if (Objects.equals(username, user.getLoginId())
////										&& !bCryptPasswordEncoder.matches(password, user.getPassword())
////										&& !Objects.equals(captcha, hiddenCaptcha)) {
////									System.out.println(
////											"onAuthenticationFailure Wrong password & Captcha Insert ::::::::::::---------");
////									redirectURL = "/login?error7";
////								} else if (Objects.equals(username, userMst.getLoginId())
////										&& bCryptPasswordEncoder.matches(password, user.getPassword())
////										&& !Objects.equals(captcha, hiddenCaptcha)) {
////									System.out.println(
////											"onAuthenticationFailure Wrong Captcha Insert ::::::::::::---------");
////									redirectURL = "/login?error4";
////								} else if (!Objects.equals(username, userMst.getLoginId())
////										&& bCryptPasswordEncoder.matches(password, user.getPassword())
////										&& Objects.equals(captcha, hiddenCaptcha)) {
////									System.out
////											.println("onAuthenticationFailure Wrong User Insert ::::::::::::---------");
////									redirectURL = "/login?error6";
////								} else if (Objects.equals(username, userMst.getLoginId())
////										&& !bCryptPasswordEncoder.matches(password, user.getPassword())
////										&& Objects.equals(captcha, hiddenCaptcha)) {
////									System.out.println(
////											"onAuthenticationFailure Wrong Password Insert ::::::::::::---------");
////									redirectURL = "/login?error5";
////								} else if (!Objects.equals(username, userMst.getLoginId())
////										&& !bCryptPasswordEncoder.matches(password, user.getPassword())
////										&& Objects.equals(captcha, hiddenCaptcha)) {
////									System.out.println(
////											"onAuthenticationFailure Wrong id and Password Insert ::::::::::::---------");
////									redirectURL = "/login?error3";
////								} else if (Objects.equals(username, user.getLoginId())
////										&& bCryptPasswordEncoder.matches(password, user.getPassword())
////										&& Objects.equals(captcha, hiddenCaptcha) && user.getIsUserLoggedIn() == 2) {
////									System.out.println("2222222222222");
////									redirectURL = "/login?error2";
////								} else {
////									System.out
////											.println("onAuthenticationFailure SOMETHING WRONG  ::::::::::::---------");
////									redirectURL = "/login?error2";
////								}
////							}
////
////							// }
////						}
////					} else {
////						userService.lock(user);
////						exception = new LockedException("Your account has been locked due to 3 failed attempts."
////								+ "Please connect with the admin team to unlock..");
////					}
////				} else {
////					System.out.println("F1");
////					redirectURL = "/login?error3";
////				}
////			}
////			super.setDefaultFailureUrl(redirectURL);
////			super.onAuthenticationFailure(request, response, exception);
////		} catch (Exception e) {
////			LOGGER.error("Error occur while onAuthenticationFailure " + ExceptionUtils.getStackTrace(e));
////		}
////
////	}}
//}

package com.ksv.ktrccrm.security;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.itextpdf.io.IOException;
import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.db1.entities.PasswordPolicy;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.PasswordPolicyService;
import com.ksv.ktrccrm.service.UserService;

@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	private static final Logger LOGGER = LogManager.getLogger(LoginFailureHandler.class);

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UserService userService;

	private String redirectURL;

	@Autowired
	private PasswordPolicyService passwordPolicyService;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException, java.io.IOException {
		try {
			System.out.println("Inside onAuthenticationFailure method........");

			String loginId = request.getParameter("loginId").toUpperCase();
			String password = request.getParameter("password");
			String captcha = request.getParameter("captcha");
			String hiddenCaptcha = request.getParameter("hiddenCaptcha");

			System.out.println("Before attempt Authentication Captcha ::::::: " + captcha);
			System.out.println("Before attempt Authentication Hidden Captcha ::::::: " + hiddenCaptcha);
			System.out.println("Before attempt Authentication LoginID ::::::: " + loginId);
			System.out.println("Before attempt Authentication Password ::::::: " + password);

			System.out.println("onAuthenticationFailure --------------- ");
			UserMst userMst = userService.getUserById(loginId);

			System.out.println(".................... onAuthenticationFailure " + userMst);
			if (userMst != null) {
				System.out.println("first if............. onAuthenticationFailure ");
				if (Objects.equals(userMst.getIsActive(), ProdConstant.ISACTIVE)) {
					System.out.println("Second if.............. onAuthenticationFailure ");
					if (userMst.getIsUserLoggedIn() < ProdConstant.MAX_FAILED_ATTEMPTS - 1) {
						userService.increaseFailedAttempts(userMst);
						System.out.println("Third if.............. onAuthenticationFailure ");
						System.out.println("***************** onAuthenticationFailure " + userMst.getIsUserLoggedIn());
						if (Objects.equals(loginId, userMst.getLoginId()) && bCryptPasswordEncoder.matches(password, userMst.getPassword()) && Objects.equals(captcha, hiddenCaptcha)) {
							System.out.println("onAuthenticationFailure login success handle -=@@@@@@@@@@@@@@@@@");
						
							Date pwdExpireDate = userMst.getPasswordExpireDate();
							if (pwdExpireDate != null) {
								System.out.println("Expire Date ,,,,,,,,,,,,,,, onAuthenticationFailure " + pwdExpireDate);
								PasswordPolicy passwordPolicies = passwordPolicyService
										.findByTenantIdAndIsactive(userMst.getTenantId(), ProdConstant.TRUE);

								LocalDate dateBefore = pwdExpireDate.toInstant().atZone(ZoneId.systemDefault())
										.toLocalDate();
								LocalDate dateAfter = LocalDate.now();
								Integer daysDiff = (int) ChronoUnit.DAYS.between(dateAfter, dateBefore);
								System.out.println("onAuthenticationSuccess The number of days between dates login success handler ::::::: " + daysDiff);

								Integer getValidateDays = passwordPolicies.getPasswordValidateDays();
								System.out.println("getValidateDays......... onAuthenticationFailure " + getValidateDays);
								if (daysDiff <= getValidateDays && daysDiff >=0) {
									System.out.println("OTP Code........... onAuthenticationFailure ");
								} else {
									System.out.println("Your password is expire so change your password .. onAuthenticationFailure ");
									redirectURL = "/changePassword";
							}
							} else {
								redirectURL = "/changePassword";
								System.out.println("Expire date is null please change pwd");
							}	
					
											} else if (Objects.equals(loginId, userMst.getLoginId()) && bCryptPasswordEncoder.matches(password, userMst.getPassword()) && Objects.equals(captcha, hiddenCaptcha) && userMst.getIsUserLoggedIn() == 2) {
												System.out.println("onAuthenticationFailure User Locked for 3 time failed attempted ::::::::::::::-----------");
												redirectURL = "/login?failedAttempt";
											} else if (!Objects.equals(loginId, userMst.getLoginId()) && !bCryptPasswordEncoder.matches(password, userMst.getPassword()) && !Objects.equals(captcha, hiddenCaptcha)) {
												System.out.println("onAuthenticationFailure Wrong id & password & Captcha Insert ::::::::::::---------");
												redirectURL = "/login?error3";
											} else if (Objects.equals(loginId, userMst.getLoginId()) && !bCryptPasswordEncoder.matches(password, userMst.getPassword()) && !Objects.equals(captcha, hiddenCaptcha)) {
												System.out.println("onAuthenticationFailure Wrong password & Captcha Insert ::::::::::::---------");
												redirectURL = "/login?error7";
											} else if (Objects.equals(loginId, userMst.getLoginId()) && bCryptPasswordEncoder.matches(password, userMst.getPassword()) && !Objects.equals(captcha, hiddenCaptcha)) {
												System.out.println("onAuthenticationFailure Wrong Captcha Insert ::::::::::::---------");
												redirectURL = "/login?error4";
											} else if (!Objects.equals(loginId, userMst.getLoginId()) && bCryptPasswordEncoder.matches(password, userMst.getPassword()) && Objects.equals(captcha, hiddenCaptcha)) {
												System.out.println("onAuthenticationFailure Wrong userMst Insert ::::::::::::---------");
												redirectURL = "/login?error6";
											} else if (Objects.equals(loginId, userMst.getLoginId()) && !bCryptPasswordEncoder.matches(password, userMst.getPassword()) && Objects.equals(captcha, hiddenCaptcha)) {
												System.out.println("onAuthenticationFailure Wrong Password Insert ::::::::::::---------");
											   redirectURL = "/login?error5";
												System.out.println("onAuthenticationFailure Wrong Password Insert ::::::::::::---------");												
											} else if (!Objects.equals(loginId, userMst.getLoginId()) && !bCryptPasswordEncoder.matches(password, userMst.getPassword()) && Objects.equals(captcha, hiddenCaptcha)) {
												System.out.println("onAuthenticationFailure Wrong id and Password Insert ::::::::::::---------");
												redirectURL = "/login?error3";
											}  else {
												System.out.println("onAuthenticationFailure SOMETHING WRONG  ::::::::::::---------");
												redirectURL = "/login?error3";
											}
												
								
								
					} else {
						System.out.println("onAuthenticationFailure User is locked for 3 time failed attempted :::::::::::::::: ");
						redirectURL = "/login?failedAttempt";						
					}
//							} else {
//								System.out.println("onAuthenticationSuccess User is locked for 3 time failed attempted :::::::::::::::: ");
//								response.sendRedirect(request.getContextPath() + "/login?failedAttempt");
//							}
//					}else {
//						System.out.println("onAuthenticationSuccess User is locked for 3 time failed attempted :::::::::::::::: ");
//						response.sendRedirect(request.getContextPath() + "/login?failedAttempt");
//					}	
				
				} else {
					System.out.println("InActive User..........");
					redirectURL =  "/login?inActive";
				}
			} else {
				System.out.println("onAuthenticationFailure User is null/empty :::::::::::::::: ");
			}
		}catch (Exception e) {
			LOGGER.error("Error occur in onAuthenticationFailure (BeforeAuthenticationFilter) " + ExceptionUtils.getStackTrace(e));		
			}
		 super.setDefaultFailureUrl(redirectURL);
		super.onAuthenticationFailure(request, response, exception);
	}
}