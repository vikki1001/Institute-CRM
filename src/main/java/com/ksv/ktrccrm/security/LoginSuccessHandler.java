//package com.ksv.ktrccrm.security;
//
//import java.io.IOException;
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.time.temporal.ChronoUnit;
//import java.util.Date;
//import java.util.Objects;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.lang3.exception.ExceptionUtils;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import com.ksv.ktrccrm.constant.ProdConstant;
//import com.ksv.ktrccrm.db1.entities.PasswordPolicy;
//import com.ksv.ktrccrm.db1.entities.UserMst;
//import com.ksv.ktrccrm.service.PasswordPolicyService;
//import com.ksv.ktrccrm.service.UserService;
//
//@Component
//public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//	private static final Logger LOGGER = LogManager.getLogger(LoginSuccessHandler.class);
//
//	@Autowired
//	private UserService userService;
//	@Autowired
//	private BCryptPasswordEncoder bCryptPasswordEncoder;
//	@Autowired
//	private PasswordPolicyService passwordPolicyService;
//
//	@Override
//	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//			Authentication authentication) throws IOException, ServletException {
//		try {
//			String loginId = request.getParameter("loginId").toUpperCase();
//			String password = request.getParameter("password");
//			String captcha = request.getParameter("captcha");
//			String hiddenCaptcha = request.getParameter("hiddenCaptcha");
//			
//			System.out.println("onAuthenticationSuccess --------------- ");
//
//			UserMst userMst = userService.findByLoginId1(loginId);
//			
//			if (Objects.nonNull(userMst)) {
//					if (Objects.equals(loginId, userMst.getLoginId()) && bCryptPasswordEncoder.matches(password, userMst.getPassword())
//							&& Objects.deepEquals(captcha, hiddenCaptcha)) {
//					System.out.println("onAuthenticationSuccess login success handle -=@@@@@@@@@@@@@@@@@");
//					
//					if (Objects.equals(userMst.getIsActive(), ProdConstant.ISACTIVE)) {
//						if (Objects.equals(userMst.getIsUserLocked(), ProdConstant.ISACTIVE) && userMst.getIsUserLoggedIn() != 2) {
//							
//							PasswordPolicy passwordPolicies = passwordPolicyService.findByTenantIdAndIsactive(userMst.getTenantId(), ProdConstant.TRUE);
//							
//							if (userMst.getOtpRequestedTime() != null) {
//								/* Check Password Expire date if not expire then direct login  Otherwise change Password ... */
//																
//								System.out.println("onAuthenticationSuccess User is already authentication (Two Factor Email Auth):::::::::::---------- ");
//								if (passwordPolicies.getPasswordValidateDays() != null) {
//
//									Integer pwd = passwordPolicies.getPasswordValidateDays();
//									System.out.println("onAuthenticationSuccess Password Validate Days ::::::: " + pwd);
//									
//									Date ort = userMst.getOtpRequestedTime();
//									LocalDate dateBefore = ort.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//									LocalDate dateAfter = LocalDate.now();
//									long daysDiff = ChronoUnit.DAYS.between(dateBefore, dateAfter);
//									System.out.println("onAuthenticationSuccess The number of days between dates login success handler ::::::: " + daysDiff);
//									
//									if (daysDiff > pwd) {
//										System.out.println("onAuthenticationSuccess Password OTP Expire , Change Password :::::::::: ");
//										response.sendRedirect(request.getContextPath() + "/changePassword");										
//									} else {
//										System.out.println("onAuthenticationSuccess Valid Password, Login directly :::::::::: ");
//										
//										userService.resetFailedAttempts(userMst.getLoginId());
//										userService.updateEnableStatus(userMst.getLoginId());
//										
//										response.sendRedirect(request.getContextPath() + "/loginSuccess");
//									}
//								} else {
//									System.out.println("else onAuthenticationSuccess - PWD Validate Day field null/empty ------------ ");
//								}
//								
//							} else {								
//								/* Two Factor Email Verification Area */
//								System.out.println("onAuthenticationSuccess OTP Send On Email Id ::::::::::::::: ");
//								
//								if (userMst.isOTPRequired()) {
//									System.out.println("CLEAR OTP METHOD CALLING :::::::::::");
//									//userService.clearOTP(userMst);
//									response.sendRedirect(request.getContextPath() + "/loginSuccess");	
//								}
//
//								if (userMst.getOneTimePassword() != null) {
//									System.out.println("One Time Password is not null --------- ");
//									response.sendRedirect(request.getContextPath() + "/loginSuccess");
//								} else {
//									System.out.println("One Time Password is null --------- ");
//								}
//								
//								
//	
//
//							}
//						} else {
//							System.out.println("onAuthenticationSuccess User is locked for 3 time failed attempted :::::::::::::::: ");
//							response.sendRedirect(request.getContextPath() + "/login?failedAttempt");
//						}
//					} else {
//						System.out.println("onAuthenticationSuccess User is inActive :::::::::::::::: ");
//					}					
//				} else if (Objects.equals(loginId, userMst.getLoginId()) && bCryptPasswordEncoder.matches(password, userMst.getPassword()) && Objects.equals(captcha, hiddenCaptcha) && userMst.getIsUserLoggedIn() == 2) {
//					System.out.println("onAuthenticationSuccess User Locked for 3 time failed attempted ::::::::::::::-----------");
//					response.sendRedirect(request.getContextPath() + "/login?failedAttempt");
//				} else if (!Objects.equals(loginId, userMst.getLoginId()) && !Objects.equals(captcha, hiddenCaptcha)) {
//					System.out.println("onAuthenticationSuccess Wrong id & password & Captcha Insert ::::::::::::---------");
//					response.sendRedirect(request.getContextPath() + "/login?error3");
//				} else if (Objects.equals(loginId, userMst.getLoginId()) && !Objects.equals(captcha, hiddenCaptcha)) {
//					System.out.println("onAuthenticationSuccess Wrong password & Captcha Insert ::::::::::::---------");
//					response.sendRedirect(request.getContextPath() + "/login?error7");
//				} else if (Objects.equals(loginId, userMst.getLoginId()) && bCryptPasswordEncoder.matches(password, userMst.getPassword()) && !Objects.equals(captcha, hiddenCaptcha)) {
//					System.out.println("onAuthenticationSuccess Wrong Captcha Insert ::::::::::::---------");
//					response.sendRedirect(request.getContextPath() + "/login?error4");
//				} else if (!Objects.equals(loginId, userMst.getLoginId()) && bCryptPasswordEncoder.matches(password, userMst.getPassword()) && Objects.equals(captcha, hiddenCaptcha)) {
//					System.out.println("onAuthenticationSuccess Wrong userMst Insert ::::::::::::---------");
//					response.sendRedirect(request.getContextPath() + "/login?error6");
//				} else if (Objects.equals(loginId, userMst.getLoginId()) && Objects.equals(captcha, hiddenCaptcha)) {
//					System.out.println("onAuthenticationSuccess Wrong Password Insert ::::::::::::---------");
//					response.sendRedirect(request.getContextPath() + "/login?error5");
//				} else if (!Objects.equals(loginId, userMst.getLoginId()) && Objects.equals(captcha, hiddenCaptcha)) {
//					System.out.println("onAuthenticationSuccess Wrong id and Password Insert ::::::::::::---------");
//					response.sendRedirect(request.getContextPath() + "/login?error3");
//				}  else {
//					System.out.println("onAuthenticationSuccess SOMETHING WRONG  ::::::::::::---------");
//					response.sendRedirect(request.getContextPath() + "/login?error2");
//				}
//					//return super.attemptAuthentication(request, response);				
//			} else {
//				System.out.println("onAuthenticationSuccess User is null/empty :::::::::::::::: ");
//			}
//						
//			super.onAuthenticationSuccess(request, response, authentication);
//		} catch (Exception e) {
//			LOGGER.error("Error occur while onAuthenticationSuccess " + ExceptionUtils.getStackTrace(e));
//		}
//
//	}
//
////	@Override
////	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
////			Authentication authentication) throws IOException, ServletException {
////		try {
////			/* Get Form Login Form */
////			String loginId = request.getParameter("loginId").toUpperCase();
////			String password = request.getParameter("password");
////			String captcha = request.getParameter("captcha");
////			String hiddenCaptcha = request.getParameter("hiddenCaptcha");
////
////			System.out.println("Inside onAuthenticationSuccess Method --------------- ");
////			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
////			UserMst userMst = userDetails.getUser();
////			/*--------------------------------------------------------------------------------*/
////			
////			if (Objects.nonNull(userMst)) {
////				if (Objects.equals(loginId, userMst.getLoginId()) && bCryptPasswordEncoder.matches(password, userMst.getPassword()) 
////							&& Objects.deepEquals(captcha, hiddenCaptcha)) {
////					System.out.println("onAuthenticationSuccess login success handle -=@@@@@@@@@@@@@@@@@");
////					
////					if (Objects.equals(userMst.getIsActive(), ProdConstant.ISACTIVE)) {
////						if (Objects.equals(userMst.getIsUserLocked(), ProdConstant.ISACTIVE) && userMst.getIsUserLoggedIn() != 2) {
////							
////							PasswordPolicy passwordPolicies = passwordPolicyService.findByTenantIdAndIsactive(userMst.getTenantId(), ProdConstant.TRUE);
////							
////							if (userMst.getOtpRequestedTime() == null) {
////								/* Check Password Expire date if not expire then direct login ... */
////								System.out.println("onAuthenticationSuccess 2 Attempt to relogin ------------ ");
////								userService.resetFailedAttempts(userMst.getLoginId());
////								userService.updateEnableStatus(userMst.getLoginId());
////								
//////								userService.generateOneTimePassword(userMst);
//////								response.sendRedirect(request.getContextPath() + "/login?otp=true&loginId1=" + loginId);
////								super.onAuthenticationSuccess(request, response, authentication);
////							} else {
////								/* Check Password Expire Date if expire then change it and then login */
////								System.out.println("onAuthenticationSuccess User is already authentication (Two Factor Email Auth):::::::::::---------- ");
////								if (passwordPolicies.getPasswordValidateDays() != null) {
////
////									Integer pwd = passwordPolicies.getPasswordValidateDays();
////									System.out.println("onAuthenticationSuccess Password Validate Days ::::::: " + pwd);
////									
////									Date ort = userMst.getOtpRequestedTime();
////									LocalDate dateBefore = ort.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
////									LocalDate dateAfter = LocalDate.now();
////									long daysDiff = ChronoUnit.DAYS.between(dateBefore, dateAfter);
////									System.out.println("onAuthenticationSuccess The number of days between dates login success handler ::::::: " + daysDiff);
////									
////									if (daysDiff > pwd) {
////										System.out.println("onAuthenticationSuccess Password Expire , Change Password :::::::::: ");
////										response.sendRedirect(request.getContextPath() + "/changePassword");
////										
////									} else {
////										/* Two Factor Email Verification Area */
////										System.out.println("onAuthenticationSuccess OTP Send On Email Id ::::::::::::::: ");
////										
////										super.onAuthenticationSuccess(request, response, authentication);
////										//response.sendRedirect(request.getContextPath() + "/loginSuccess");
////									}
////								
////									
////									
////								} else {
////									System.out.println("else onAuthenticationSuccess - PWD Validate Day field null/empty ------------ ");
////								}
////							}
////						} else {
////							System.out.println(
////									"onAuthenticationSuccess User is locked for 3 time failed attempted :::::::::::::::: ");
////							response.sendRedirect(request.getContextPath() + "/login?failedAttempt");
////						}
////					} else {
////						System.out.println("onAuthenticationSuccess User is inActive :::::::::::::::: ");
////					}					
////				} else if (Objects.equals(loginId, userMst.getLoginId()) && bCryptPasswordEncoder.matches(password, userMst.getPassword()) && Objects.equals(captcha, hiddenCaptcha) && userMst.getIsUserLoggedIn() == 2) {
////					System.out.println("onAuthenticationSuccess User Locked for 3 time failed attempted ::::::::::::::-----------");
////					response.sendRedirect(request.getContextPath() + "/login?failedAttempt");
////				} else if (!Objects.equals(loginId, userMst.getLoginId()) && !Objects.equals(captcha, hiddenCaptcha)) {
////					System.out.println("onAuthenticationSuccess Wrong id & password & Captcha Insert ::::::::::::---------");
////					response.sendRedirect(request.getContextPath() + "/login?error3");
////				} else if (Objects.equals(loginId, userMst.getLoginId()) && !Objects.equals(captcha, hiddenCaptcha)) {
////					System.out.println("onAuthenticationSuccess Wrong password & Captcha Insert ::::::::::::---------");
////					response.sendRedirect(request.getContextPath() + "/login?error7");
////				} else if (Objects.equals(loginId, userMst.getLoginId()) && bCryptPasswordEncoder.matches(password, userMst.getPassword()) && !Objects.equals(captcha, hiddenCaptcha)) {
////					System.out.println("onAuthenticationSuccess Wrong Captcha Insert ::::::::::::---------");
////					response.sendRedirect(request.getContextPath() + "/login?error4");
////				} else if (!Objects.equals(loginId, userMst.getLoginId()) && bCryptPasswordEncoder.matches(password, userMst.getPassword()) && Objects.equals(captcha, hiddenCaptcha)) {
////					System.out.println("onAuthenticationSuccess Wrong userMst Insert ::::::::::::---------");
////					response.sendRedirect(request.getContextPath() + "/login?error6");
////				} else if (Objects.equals(loginId, userMst.getLoginId()) && Objects.equals(captcha, hiddenCaptcha)) {
////					System.out.println("onAuthenticationSuccess Wrong Password Insert ::::::::::::---------");
////					response.sendRedirect(request.getContextPath() + "/login?error5");
////				} else if (!Objects.equals(loginId, userMst.getLoginId()) && Objects.equals(captcha, hiddenCaptcha)) {
////					System.out.println("onAuthenticationSuccess Wrong id and Password Insert ::::::::::::---------");
////					response.sendRedirect(request.getContextPath() + "/login?error3");
////				}  else {
////					System.out.println("onAuthenticationSuccess SOMETHING WRONG  ::::::::::::---------");
////					response.sendRedirect(request.getContextPath() + "/login?error2");
////				}
////						
////			} else {
////				System.out.println("onAuthenticationSuccess User is null/empty :::::::::::::::: ");
////			}
////			super.onAuthenticationSuccess(request, response, authentication);
////		} catch (Exception e) {
////			LOGGER.error("Error occur while onAuthenticationSuccess " + ExceptionUtils.getStackTrace(e));
////		}
////
////	}
//}

package com.ksv.ktrccrm.security;

import java.io.IOException;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.impl.CheckInCheckOutDaoImpl;
import com.ksv.ktrccrm.db1.entities.PasswordPolicy;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.PasswordPolicyService;
import com.ksv.ktrccrm.service.UserService;



@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private static final Logger LOGGER = LogManager.getLogger(LoginSuccessHandler.class);

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UserService userService;
	@Autowired
	private PasswordPolicyService passwordPolicyService;
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		try {
			System.out.println("Inside onAuthenticationSuccess method........");

			String loginId = request.getParameter("loginId").toUpperCase();
			String password = request.getParameter("password");
			String captcha = request.getParameter("captcha");
			String hiddenCaptcha = request.getParameter("hiddenCaptcha");

			System.out.println("Before attempt Authentication Captcha ::::::: " + captcha);
			System.out.println("Before attempt Authentication Hidden Captcha ::::::: " + hiddenCaptcha);
			System.out.println("Before attempt Authentication LoginID ::::::: " + loginId);
			System.out.println("Before attempt Authentication Password ::::::: " + password);

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			UserMst userMst = userDetails.getUser();

			System.out.println(".................... onAuthenticationSuccess " + userDetails.getUser());
			if (userMst != null) {
				System.out.println("first if............. onAuthenticationSuccess ");
				if (Objects.equals(userMst.getIsActive(), ProdConstant.ISACTIVE)) {
					System.out.println("Second if.............. onAuthenticationSuccess ");
					if (Objects.equals(userMst.getIsUserLocked(), ProdConstant.ISACTIVE) && userMst.getIsUserLoggedIn() != 2) {
						if (userMst.getIsUserLocked() > 0) {
							System.out.println("Attempt to relogin  ::::::::::::---------  onAuthenticationSuccess ");
							userService.resetFailedAttempts(userMst.getLoginId());
							userService.updateEnableStatus(userMst.getLoginId());
						System.out.println("Third if.............. onAuthenticationSuccess ");
						System.out.println("***************** onAuthenticationSuccess " + userMst.getIsUserLoggedIn());
		  					Date pwdExpireDate = userMst.getPasswordExpireDate();
							if (pwdExpireDate != null) {
								System.out.println("Expire Date ,,,,,,,,,,,,,,, onAuthenticationSuccess " + pwdExpireDate);
								PasswordPolicy passwordPolicies = passwordPolicyService
										.findByTenantIdAndIsactive(userMst.getTenantId(), ProdConstant.TRUE);

								LocalDate dateBefore = pwdExpireDate.toInstant().atZone(ZoneId.systemDefault())
										.toLocalDate();
								LocalDate dateAfter = LocalDate.now();
								Integer daysDiff = (int) ChronoUnit.DAYS.between(dateAfter, dateBefore);
								System.out.println("onAuthenticationSuccess The number of days between dates login success handler ::::::: " + daysDiff);

								Integer getValidateDays = passwordPolicies.getPasswordValidateDays();
								System.out.println("getValidateDays......... onAuthenticationSuccess " + getValidateDays);
								if (daysDiff <= getValidateDays && daysDiff >=0) {
									
											// super.onAuthenticationSuccess(request, response, authentication);
										 
											if (Objects.equals(loginId, userMst.getLoginId()) && bCryptPasswordEncoder.matches(password, userMst.getPassword()) && Objects.equals(captcha, hiddenCaptcha)) {
												System.out.println("onAuthenticationSuccess login success handle -=@@@@@@@@@@@@@@@@@");
													response.sendRedirect(request.getContextPath() + "/loginSuccess");
//												Date otpGenerateDate = userMst.getOtpRequestedTime();
//												Integer otpValidateDays = passwordPolicies.getOtpValidateDays();
//												
//												LocalDate dateBefore_1 = otpGenerateDate.toInstant().atZone(ZoneId.systemDefault())
//														.toLocalDate();
//												LocalDate dateAfter_2 = LocalDate.now();
//												Integer daysDiff_1 = (int) ChronoUnit.DAYS.between(dateBefore_1, dateAfter_2);
//												if(daysDiff_1 <= otpValidateDays && daysDiff_1 >=0 && userMst.getOtpRequestedTime() != null) {
//													response.sendRedirect(request.getContextPath() + "/loginSuccess");
//												}else
//												{												
//													try {
//														userService.generateOneTimePassword(userMst);     
//														if (userMst.isOTPRequired()) {
//														String checkOtp = request.getParameter("oneTimePassword");
//														String encodeCheckOtp = passwordEncoder.encode(checkOtp);
//														if(bCryptPasswordEncoder.matches(encodeCheckOtp, userMst.getOneTimePassword())) {
//															response.sendRedirect(request.getContextPath() + "/loginSuccess");
//														}else {
//															response.sendRedirect(request.getContextPath() + "/login?otp");
//														}
//														}else {
//															userService.generateOneTimePassword(userMst);
//														}
//													}  catch (Exception e) {
//														throw new AuthenticationServiceException("Error while sending OTP email.");
//													}
//												}
											} else if (Objects.equals(loginId, userMst.getLoginId()) && bCryptPasswordEncoder.matches(password, userMst.getPassword()) && Objects.equals(captcha, hiddenCaptcha) && userMst.getIsUserLoggedIn() == 2) {
												System.out.println("onAuthenticationSuccess User Locked for 3 time failed attempted ::::::::::::::-----------");
												response.sendRedirect(request.getContextPath() + "/login?failedAttempt");
											} else if (!Objects.equals(loginId, userMst.getLoginId()) && !bCryptPasswordEncoder.matches(password, userMst.getPassword()) && !Objects.equals(captcha, hiddenCaptcha)) {
												System.out.println("onAuthenticationSuccess Wrong id & password & Captcha Insert ::::::::::::---------");
												response.sendRedirect(request.getContextPath() + "/login?error3");
											} else if (Objects.equals(loginId, userMst.getLoginId()) && !bCryptPasswordEncoder.matches(password, userMst.getPassword()) && !Objects.equals(captcha, hiddenCaptcha)) {
												System.out.println("onAuthenticationSuccess Wrong password & Captcha Insert ::::::::::::---------");
												response.sendRedirect(request.getContextPath() + "/login?error7");
											} else if (Objects.equals(loginId, userMst.getLoginId()) && bCryptPasswordEncoder.matches(password, userMst.getPassword()) && !Objects.equals(captcha, hiddenCaptcha)) {
												System.out.println("onAuthenticationSuccess Wrong Captcha Insert ::::::::::::---------");
												response.sendRedirect(request.getContextPath() + "/login?error4");
											} else if (!Objects.equals(loginId, userMst.getLoginId()) && bCryptPasswordEncoder.matches(password, userMst.getPassword()) && Objects.equals(captcha, hiddenCaptcha)) {
												System.out.println("onAuthenticationSuccess Wrong userMst Insert ::::::::::::---------");
												response.sendRedirect(request.getContextPath() + "/login?error6");
											} else if (Objects.equals(loginId, userMst.getLoginId()) && !bCryptPasswordEncoder.matches(password, userMst.getPassword()) && Objects.equals(captcha, hiddenCaptcha)) {
												System.out.println("onAuthenticationSuccess Wrong Password Insert ::::::::::::---------");
												response.sendRedirect(request.getContextPath() + "/login?error5");
											} else if (!Objects.equals(loginId, userMst.getLoginId()) && !bCryptPasswordEncoder.matches(password, userMst.getPassword()) && Objects.equals(captcha, hiddenCaptcha)) {
												System.out.println("onAuthenticationSuccess Wrong id and Password Insert ::::::::::::---------");
												response.sendRedirect(request.getContextPath() + "/login?error3");
											}else {
												System.out.println("onAuthenticationSuccess SOMETHING WRONG  ::::::::::::---------");
												response.sendRedirect(request.getContextPath() + "/login?error2");
											}
								
									} else {
										System.out.println("Your password is expire so change your password ..");
										//response.flushBuffer();
										response.sendRedirect(request.getContextPath() + "/changePwd");
									}
								
								} else {
									response.sendRedirect(request.getContextPath() + "/changePwd");
									System.out.println("Expire date is null please change pwd");
								}
							} else {
								System.out.println("onAuthenticationSuccess User is locked for 3 time failed attempted :::::::::::::::: ");
								response.sendRedirect(request.getContextPath() + "/login?failedAttempt");
							}
					}else {
						System.out.println("onAuthenticationSuccess User is locked for 3 time failed attempted :::::::::::::::: ");
						response.sendRedirect(request.getContextPath() + "/login?failedAttempt");
					}	
				
				} else {
					System.out.println("InActive User..........");
					response.sendRedirect(request.getContextPath() + "/login?inActive");
				}
			} else {
				System.out.println("onAuthenticationSuccess User is null/empty :::::::::::::::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur in attemptAuthentication (BeforeAuthenticationFilter) "
					+ ExceptionUtils.getStackTrace(e));
		}
		super.onAuthenticationSuccess(request, response, authentication);
	}

}