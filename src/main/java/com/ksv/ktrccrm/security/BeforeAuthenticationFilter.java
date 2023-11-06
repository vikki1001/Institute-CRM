//package com.ksv.ktrccrm.security;
//
//import java.io.UnsupportedEncodingException;
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.time.temporal.ChronoUnit;
//import java.util.Date;
//import java.util.Objects;
//
//import javax.mail.MessagingException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.lang3.exception.ExceptionUtils;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationServiceException;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import org.springframework.stereotype.Component;
//
//import com.ksv.ktrccrm.constant.ProdConstant;
//import com.ksv.ktrccrm.db1.entities.PasswordPolicy;
//import com.ksv.ktrccrm.db1.entities.UserMst;
//import com.ksv.ktrccrm.service.PasswordPolicyService;
//import com.ksv.ktrccrm.service.UserService;
//
//@Component
//public class BeforeAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//	private static final Logger LOGGER = LogManager.getLogger(BeforeAuthenticationFilter.class);
//
//	@Autowired
//	private UserService userService;
//	@Autowired
//	private BCryptPasswordEncoder bCryptPasswordEncoder;
//	@Autowired
//	private PasswordPolicyService passwordPolicyService;
//
//	@Autowired
//	@Override
//	public void setAuthenticationManager(AuthenticationManager authManager) {
//		super.setAuthenticationManager(authManager);
//	}
//
//	@Autowired
//	@Override
//	public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
//		super.setAuthenticationFailureHandler(failureHandler);
//	}
//
//	@Autowired
//	@Override
//	public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
//		super.setAuthenticationSuccessHandler(successHandler);
//	}
//
//	public BeforeAuthenticationFilter() {
//		setUsernameParameter("loginId");
//		super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/loginSuccess", "POST"));
//	}
//
//	@Override
//	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
//			throws AuthenticationException {
//		try {
//
//			System.out.println("BeforeAuthenticationFilter ----------------- ");
//
//			String loginId = request.getParameter("loginId").toUpperCase();
//			String password = request.getParameter("password");
//			String captcha = request.getParameter("captcha");
//			String hiddenCaptcha = request.getParameter("hiddenCaptcha");
////			String oneTimePassword = request.getParameter("oneTimePassword");
//			
//
////			System.out.println("Before attempt Authentication Captcha ::::::: " + captcha);
////			System.out.println("Before attempt Authentication Hidden Captcha ::::::: " + hiddenCaptcha);			
////			System.out.println("Before attempt Authentication LoginID ::::::: " + loginId);
////			System.out.println("Before attempt Authentication Password ::::::: " + password);
////			System.out.println("Before attempt Authentication oneTimePassword ::::::: " + oneTimePassword);
//			
//
//			UserMst userMst = userService.findByLoginId(loginId);
//			if (Objects.nonNull(userMst)) {
//					if (Objects.equals(loginId, userMst.getLoginId()) 
//							&& Objects.deepEquals(captcha, hiddenCaptcha)) {
//					System.out.println("BeforeAuthenticationFilter login success handle -=@@@@@@@@@@@@@@@@@");
//					
//					if (Objects.equals(userMst.getIsActive(), ProdConstant.ISACTIVE)) {
//						if (Objects.equals(userMst.getIsUserLocked(), ProdConstant.ISACTIVE) && userMst.getIsUserLoggedIn() != 2) {
//							
//							PasswordPolicy passwordPolicies = passwordPolicyService.findByTenantIdAndIsactive(userMst.getTenantId(), ProdConstant.TRUE);
//							
//							if (userMst.getOtpRequestedTime() != null) {
//								/* Check Password Expire date if not expire then direct login  Otherwise change Password ... */
//																
//								System.out.println("BeforeAuthenticationFilter User is already authentication (Two Factor Email Auth):::::::::::---------- ");
//								if (passwordPolicies.getPasswordValidateDays() != null) {
//
//									Integer pwd = passwordPolicies.getPasswordValidateDays();
//									System.out.println("BeforeAuthenticationFilter Password Validate Days ::::::: " + pwd);
//									
//									Date ort = userMst.getOtpRequestedTime();
//									LocalDate dateBefore = ort.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//									LocalDate dateAfter = LocalDate.now();
//									long daysDiff = ChronoUnit.DAYS.between(dateBefore, dateAfter);
//									System.out.println("BeforeAuthenticationFilter The number of days between dates login success handler ::::::: " + daysDiff);
//									
//									if (daysDiff > pwd) {
//										System.out.println("BeforeAuthenticationFilter Password OTP Expire , Change Password :::::::::: ");
//										response.sendRedirect(request.getContextPath() + "/changePassword");										
//									} else {
//										System.out.println("BeforeAuthenticationFilter Valid Password, Login directly :::::::::: ");
//										
//										userService.resetFailedAttempts(userMst.getLoginId());
//										userService.updateEnableStatus(userMst.getLoginId());
//										
//										//response.sendRedirect(request.getContextPath() + "/loginSuccess");
//										return super.attemptAuthentication(request, response);
//									}
//								} else {
//									System.out.println("else BeforeAuthenticationFilter - PWD Validate Day field null/empty ------------ ");
//								}
//								
//							} else {								
//								/* Two Factor Email Verification Area */
//								System.out.println("BeforeAuthenticationFilter OTP Send On Email Id ::::::::::::::: ");
//								
//								if (userMst.isOTPRequired()) {
//									return super.attemptAuthentication(request, response);
//								}
//								
//								if (userMst.getIsUserLocked() == 1) {
//									System.out.println("ATTEMPTAUTHENTICATION - LOGIN: " + loginId);
//									float spamScore = getGoogleRecaptchaScore();
//									
//									if (spamScore < 0.5) {
//										try {
//											userService.generateOneTimePassword(userMst);
//											// throw new InsufficientAuthenticationException("OTP.... ");
//											//response.sendRedirect(request.getContextPath() + "/loginSuccess");													
//										} catch (MessagingException | UnsupportedEncodingException ex) {
//											throw new AuthenticationServiceException("Error while sending OTP email.");
//										}
//									}
//								}	
//
//							}
//						} else {
//							System.out.println("BeforeAuthenticationFilter User is locked for 3 time failed attempted :::::::::::::::: ");
//							response.sendRedirect(request.getContextPath() + "/login?failedAttempt");
//						}
//					} else {
//						System.out.println("BeforeAuthenticationFilter User is inActive :::::::::::::::: ");
//					}					
//				} else if (Objects.equals(loginId, userMst.getLoginId()) && bCryptPasswordEncoder.matches(password, userMst.getPassword()) && Objects.equals(captcha, hiddenCaptcha) && userMst.getIsUserLoggedIn() == 2) {
//					System.out.println("BeforeAuthenticationFilter User Locked for 3 time failed attempted ::::::::::::::-----------");
//					response.sendRedirect(request.getContextPath() + "/login?failedAttempt");
//				} else if (!Objects.equals(loginId, userMst.getLoginId()) && !bCryptPasswordEncoder.matches(password, userMst.getPassword()) && !Objects.equals(captcha, hiddenCaptcha)) {
//					System.out.println("BeforeAuthenticationFilter Wrong id & password & Captcha Insert ::::::::::::---------");
//					response.sendRedirect(request.getContextPath() + "/login?error3");
//				} else if (Objects.equals(loginId, userMst.getLoginId()) && !bCryptPasswordEncoder.matches(password, userMst.getPassword()) && !Objects.equals(captcha, hiddenCaptcha)) {
//					System.out.println("BeforeAuthenticationFilter Wrong password & Captcha Insert ::::::::::::---------");
//					response.sendRedirect(request.getContextPath() + "/login?error7");
//				} else if (Objects.equals(loginId, userMst.getLoginId()) && bCryptPasswordEncoder.matches(password, userMst.getPassword()) && !Objects.equals(captcha, hiddenCaptcha)) {
//					System.out.println("BeforeAuthenticationFilter Wrong Captcha Insert ::::::::::::---------");
//					response.sendRedirect(request.getContextPath() + "/login?error4");
//				} else if (!Objects.equals(loginId, userMst.getLoginId()) && bCryptPasswordEncoder.matches(password, userMst.getPassword()) && Objects.equals(captcha, hiddenCaptcha)) {
//					System.out.println("BeforeAuthenticationFilter Wrong userMst Insert ::::::::::::---------");
//					response.sendRedirect(request.getContextPath() + "/login?error6");
//				} else if (Objects.equals(loginId, userMst.getLoginId()) && !bCryptPasswordEncoder.matches(password, userMst.getPassword()) && Objects.equals(captcha, hiddenCaptcha)) {
//					System.out.println("BeforeAuthenticationFilter Wrong Password Insert ::::::::::::---------");
//					response.sendRedirect(request.getContextPath() + "/login?error5");
//				} else if (!Objects.equals(loginId, userMst.getLoginId()) && !bCryptPasswordEncoder.matches(password, userMst.getPassword()) && Objects.equals(captcha, hiddenCaptcha)) {
//					System.out.println("BeforeAuthenticationFilter Wrong id and Password Insert ::::::::::::---------");
//					response.sendRedirect(request.getContextPath() + "/login?error3");
//				}  else {
//					System.out.println("BeforeAuthenticationFilter SOMETHING WRONG  ::::::::::::---------");
//					response.sendRedirect(request.getContextPath() + "/login?error2");
//				}
//					//return super.attemptAuthentication(request, response);				
//			} else {
//				System.out.println("BeforeAuthenticationFilter User is null/empty :::::::::::::::: ");
//			}
//		} catch (Exception e) {
//			LOGGER.error("Error occur in attemptAuthentication (BeforeAuthenticationFilter) " + ExceptionUtils.getStackTrace(e));
//		}
//		return super.attemptAuthentication(request, response);
//	}
//
//	private float getGoogleRecaptchaScore() {
//		return 0.43f;
//	}
//}

package com.ksv.ktrccrm.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

@Component
public class BeforeAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private static final Logger LOGGER = LogManager.getLogger(BeforeAuthenticationFilter.class);

	@Autowired
	@Override
	public void setAuthenticationManager(AuthenticationManager authManager) {
		//System.out.println("Inside first method........3");
		super.setAuthenticationManager(authManager);
	}

	@Autowired
	@Override
	public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
		//System.out.println("Inside Second method........4");
		super.setAuthenticationFailureHandler(failureHandler);
	}

	@Autowired
	@Override
	public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
		//System.out.println("Inside third method........5");
		super.setAuthenticationSuccessHandler(successHandler);
	}

	public BeforeAuthenticationFilter() {
		//System.out.println("Inside forth method........1");
		setUsernameParameter("loginId");
		super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/loginSuccess", "POST"));
	}

}

