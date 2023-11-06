package com.ksv.ktrccrm.dao.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.UserDao;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.PasswordPolicy;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.db1.repository.EmpBasicRepository;
import com.ksv.ktrccrm.db1.repository.UserRepository;
import com.ksv.ktrccrm.service.PasswordPolicyService;
import com.ksv.ktrccrm.service.UserService;

@Repository
public class UserDaoImpl implements UserDao {
	private static final Logger LOGGER = LogManager.getLogger(UserDaoImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private EmpBasicRepository empBasicRepository;
	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;
	@Autowired
	private PasswordPolicyService passwordPolicyService;
	
	/* List Of IsActive Users */
	@Override
	public List<UserMst> getRecordList(Integer isActive,String tenantId) throws Exception {
		List<UserMst> userList = userRepository.getRecordList(isActive, tenantId);
		try {
			if (Objects.nonNull(userList)) {
				return userList;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display users list " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	public String getPwdExpireDate() throws Exception {
		String formattedDate = null;
		try {
			String loginId;
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			loginId = authentication.getName();
			UserMst userMst = userRepository.findByLoginId(loginId);
			PasswordPolicy passwordPolicies = passwordPolicyService.findByTenantIdAndIsactive(userMst.getTenantId(), ProdConstant.TRUE);
			Integer getPwdValidateDays = passwordPolicies.getPasswordValidateDays();
		       Calendar calendar = Calendar.getInstance();

		        Date currentDate = calendar.getTime();

		        calendar.add(Calendar.DAY_OF_MONTH, getPwdValidateDays);

		        Date dateAfter30Days = calendar.getTime();

		        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		         formattedDate = sdf.format(dateAfter30Days);

		        System.out.println("Date after 30 days: " + formattedDate);
		} catch (Exception e) {
			LOGGER.error("Error occur white get password expire date " + ExceptionUtils.getStackTrace(e));
		}
			    return formattedDate;
	}
	
	/* Save & Update User */
	@Override
	public UserMst saveOrUpdateUser(UserMst userMst) throws Exception {
		String loginId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			loginId = authentication.getName();
			if(Objects.nonNull(loginId)) {
			 String dateFormat = "yyyy-MM-dd";
			  DateFormat formatter = new SimpleDateFormat(dateFormat);
			 Date getPwdExpireDate = formatter.parse(getPwdExpireDate());
	
			Optional<UserMst> user2 = userRepository.findById(userMst.getLoginId());
			if (user2.isPresent()) {
				UserMst newUserMst = user2.get();
				newUserMst.setLoginType(userMst.getLoginType());
				newUserMst.setTitle(userMst.getTitle());
				newUserMst.setFirstName(userMst.getFirstName());
				newUserMst.setUserMiddleName(userMst.getUserMiddleName());
				newUserMst.setLastName(userMst.getLastName());
				newUserMst.setUserDisplayName(userMst.getUserDisplayName());
				newUserMst.setGender(userMst.getGender());
				newUserMst.setDob(userMst.getDob());
				newUserMst.setEmailId(userMst.getEmailId().toLowerCase());
				newUserMst.setMobileNo1(userMst.getMobileNo1());
				newUserMst.setBaseBranch(userMst.getBaseBranch());
				newUserMst.setMainRole(userMst.getMainRole());
				newUserMst.setEscalationManager(userMst.getEscalationManager());
				newUserMst.setSecondaryUser(userMst.getSecondaryUser());
				newUserMst.setUserGroup(userMst.getUserGroup());
				newUserMst.setTenantId(userMst.getTenantId());
				newUserMst.setBranch(userMst.getBranch());
				newUserMst.setDepartment(userMst.getDepartment());
				newUserMst.setLastModifieDate(checkOutDaoImpl.getDateTime());
				newUserMst.setLastModifiedBy(loginId);
				newUserMst.setIsUserLocked(userMst.getIsUserLocked());
				newUserMst.setIsUserLoggedIn(0);
				newUserMst.setChangePwdDate(checkOutDaoImpl.getDateTime());
				newUserMst.setPasswordExpireDate(getPwdExpireDate);
				//newUserMst.setTwoFactor(ProdConstant.TRUE);
				//newUserMst.setIsActive(ProdConstant.ISACTIVE);

				userRepository.save(newUserMst);
				return newUserMst;
			} else {
				userMst.setAllowSwitchingUserYn(0);
				userMst.setAdsAutoLoginYn(0);
				userMst.setAllowConCurrentLogin(0);
				userMst.setAppLoginYn(0);
				userMst.setConsequetiveBadLoginCount(0);
				userMst.setForceMinLoginFreqDays(0);
				userMst.setForceMinLoginReqYn(0);
				userMst.setForcePwdChangeYn(0);
				userMst.setForcePwdChgDays(0);
				userMst.setIdAmlUser(0);
				userMst.setIsAdminFlag(0);
				userMst.setIsChecker(0);
				userMst.setIsLoginSuspended(0);
				userMst.setIsMaker(0);
				userMst.setLoginSessionActive(0);
				userMst.setUserFirstNameOnPhotoId(0);
				userMst.setIsActive(ProdConstant.ISACTIVE);
				userMst.setCreatedDate(checkOutDaoImpl.getDateTime());
				userMst.setCreatedBy(loginId);
				userMst.setIsUserLoggedIn(0);
				userMst.setEmailId(userMst.getEmailId().toLowerCase());
				//userMst.setTwoFactor(ProdConstant.TRUE);

				/* Encrypt the Password */
				String encodedPassword = bCryptPasswordEncoder.encode(userMst.getPassword());
				userMst.setPassword(encodedPassword);
				userMst.setLastPassword(encodedPassword);
				/* Save token to database */
				userMst.getPasswordToken();
				userMst.setPasswordToken(UUID.randomUUID().toString());

				this.userRepository.save(userMst);
				return userMst;
			}
			}else {
				System.out.println("loginId is null......");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while save & update user " + ExceptionUtils.getStackTrace(e));
		}
		return userMst;
	}

	/* get User by Id */
	@Override
	public UserMst getUserById(String loginId) throws Exception {
		Optional<UserMst> optional = userRepository.findById(loginId);
		UserMst userMst = null;
		try {
			if (optional.isPresent()) {
				userMst = optional.get();
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while find user record for given id " + ExceptionUtils.getStackTrace(e));
		}
		return userMst;
	}

	/* For Login Page */
	@Override
	public UserMst getUserDetails(String loginId, Integer isActive) throws Exception {
		UserMst userMst = userRepository.getUserDetails(loginId, isActive);
		try {
			if (Objects.nonNull(userMst)) {
				return userMst;
			}
		} catch (Exception e) {
			LOGGER.error(" Error occur while get user details " + ExceptionUtils.getStackTrace(e));
		}
		return new UserMst();
	}
	/* Password Check */
	@Override
	public Optional<UserMst> getPasswordCheck(String password) throws Exception {
		Optional<UserMst> optional = userRepository.getPasswordCheck(password);
		try {
			if (optional.isPresent()) {
				return optional;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while password check " + ExceptionUtils.getStackTrace(e));
		}
		return Optional.empty();

	}

	@Override
	public boolean userExists(String password) throws Exception {
		try {
			return getPasswordCheck(password).isPresent();
		} catch (Exception e) {
			LOGGER.error("Error occur while check user exist or not " + ExceptionUtils.getStackTrace(e));
		}
		return false;

	}

	/* Login ID Verify */
	@Override
	public Optional<UserMst> findLoginByLoginId(String loginId) throws Exception {
		Optional<UserMst> optional = userRepository.findLoginByLoginId(loginId);
		try {
			if (optional.isPresent()) {
				return optional;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while find login id " + ExceptionUtils.getStackTrace(e));
		}
		return Optional.empty();

	}

	@Override
	public boolean loginIdExists(String loginId) throws Exception {
		try {
			return findLoginByLoginId(loginId).isPresent();
		} catch (Exception e) {
			LOGGER.error("Error occur while loginId exist or not " + ExceptionUtils.getStackTrace(e));
		}
		return false;

	}

	@Override
	public void resetPassword(String password, String passwordToken, String emailId) throws Exception {
		try {
			userRepository.resetPassword(password, passwordToken, emailId);
		} catch (Exception e) {
			LOGGER.error("Error occur while reset password " + ExceptionUtils.getStackTrace(e));
		}

	}

	@Override
	public void setRandomOTP(int randomOTP, String emailId) throws Exception {
		try {
			userRepository.setRandomOTP(randomOTP, emailId);
		} catch (Exception e) {
			LOGGER.error("Error occur while save rendom number " + ExceptionUtils.getStackTrace(e));
		}

	}

	@Override
	public List<EmpBasicDetails> getSearchEmpByIdAndFullName(String value, String tenantId) throws Exception {
		try {
			List<EmpBasicDetails> basicDetailsList = empBasicRepository.getSearchEmpByIdAndFullName(value, tenantId, ProdConstant.TRUE);
			if (!basicDetailsList.isEmpty()) {
				return basicDetailsList;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while search employee by id and name " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	/* For Two Factor Authentication By Email ID */

//	@Override
//	public UserMst findByLoginId1(String loginId) throws Exception {
//		UserMst userMst = entityManager.find(UserMst.class, loginId);
//		try {
//			if (userMst != null) {
//				return userMst;
//			}
//		} catch (Exception e) {
//			LOGGER.error("Error occur while get loginId " + ExceptionUtils.getStackTrace(e));
//		}
//		return new UserMst();
//	}

	@Override
	public void saveOneTimePassword(UserMst newUserMst) throws Exception {
		try {
			this.entityManager.persist(newUserMst);
		} catch (Exception e) {
			LOGGER.error("Error occur while save one time password (OTP) " + ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public Optional<UserMst> findLoginByLoginId2(String loginId) throws Exception {
		Optional<UserMst> optional = Optional.of(entityManager.find(UserMst.class, loginId));
		try {
			System.out.println("USER DATA ++++ " + optional.get().getLoginId());
			if (optional.isPresent()) {
				return optional;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while find login id " + ExceptionUtils.getStackTrace(e));
		}
		return Optional.empty();

	}
	
	@Override
	public UserMst getUserByToken(String passwordToken) throws Exception {
		UserMst userMst = new UserMst();
		try {
			userMst = userRepository.getPasswordPolicy(passwordToken);
		} catch (Exception e) {
			LOGGER.error("Error occur while open reset password page " + ExceptionUtils.getStackTrace(e));
		}
		return userMst;
	}
	
	@Override
	public UserMst saveResetPassword(UserMst user) throws Exception {
		String loginId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			loginId = authentication.getName();
			Optional<UserMst> user2 = userRepository.findById(user.getLoginId());
			if (user2.isPresent()) {
				System.out.println("Inside Dao...");
				UserMst newUser = user2.get();
				String secLastPwd = newUser.getSecondLastPassword();
				String lastPwd = newUser.getLastPassword();
				if(secLastPwd != null) {
				newUser.setThirdLastpassword(secLastPwd);
				}
				if(lastPwd != null) {
				newUser.setSecondLastPassword(lastPwd);
				}
				if(user != null) {
					System.out.println("USER NEW PASSWORD........."+ user.getPassword());
				newUser.setLastPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
				newUser.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
				newUser.setPasswordToken(UUID.randomUUID().toString());
				}
				newUser.setLastModifieDate(checkOutDaoImpl.getDateTime());
				newUser.setLastModifiedBy(loginId);
				
				userRepository.save(newUser);
				System.out.println("Reset password save........");
			} else {
				return user;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while reset password " + ExceptionUtils.getStackTrace(e));
		}
		return user;
	}
}
