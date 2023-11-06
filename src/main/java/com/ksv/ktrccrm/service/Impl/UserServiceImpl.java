package com.ksv.ktrccrm.service.Impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.UserDao;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.db1.repository.UserRepository;
import com.ksv.ktrccrm.service.EmailAndOTPService;
import com.ksv.ktrccrm.service.UserService;

import net.bytebuddy.utility.RandomString;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDao userDao;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private EmailAndOTPService emailAndOTPService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	/* List Of IsActive Users */
	@Override
	public List<UserMst> getRecordList(Integer isActive, String tenantId) throws Exception {
		List<UserMst> userMst = userDao.getRecordList(isActive, tenantId);
		try {
			if (Objects.nonNull(userMst)) {
				return userMst;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display user list " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	/* Save & Update User */
	@Override
	public UserMst saveOrUpdateUser(UserMst userMst) throws Exception {
		try {
			if (Objects.nonNull(userMst)) {
				userDao.saveOrUpdateUser(userMst);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while save & update user " + ExceptionUtils.getStackTrace(e));
		}
		return userMst;
	}

	/* get User by Id */
	@Override
	public UserMst getUserById(String loginId) throws Exception {
		UserMst userMst = userDao.getUserById(loginId);
		try {
			if (Objects.nonNull(userMst)) {
				return userMst;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while find user record for given id " + ExceptionUtils.getStackTrace(e));
		}
		return new UserMst();
	}

	/* For Login Page */
	@Override
	public UserMst getUserDetails(String loginId, Integer isActive) throws Exception {
		UserMst userMst = userDao.getUserDetails(loginId, isActive);
		try {
			if (Objects.nonNull(userMst)) {
				return userMst;
			}
		} catch (Exception e) {
			LOGGER.error(" Error occur while find user details " + ExceptionUtils.getStackTrace(e));
		}
		return new UserMst();
	}

	/* Password Check */
	@Override
	public Optional<UserMst> getPasswordCheck(String password) throws Exception {
		try {
			Optional<UserMst> optional = userDao.getPasswordCheck(password);
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
			LOGGER.error("Error occur while check user exist or not" + ExceptionUtils.getStackTrace(e));
		}
		return false;

	}

	/* Login ID Verify */
	@Override
	public Optional<UserMst> findLoginByLoginId(String loginId) throws Exception {
		try {
			Optional<UserMst> optional = userDao.findLoginByLoginId(loginId);
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
			LOGGER.error("Error occur while find login id " + ExceptionUtils.getStackTrace(e));
		}
		return false;

	}

	@Override
	public void resetPassword(String password, String passwordToken, String emailId) throws Exception {
		try {
			userDao.resetPassword(password, passwordToken, emailId);
		} catch (Exception e) {
			LOGGER.error("Error occur while reset password " + ExceptionUtils.getStackTrace(e));
		}

	}

	@Override
	public void setRandomOTP(int randomOTP, String emailId) throws Exception {
		try {
			userDao.setRandomOTP(randomOTP, emailId);
		} catch (Exception e) {
			LOGGER.error("Error occur while save random number " + ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public List<EmpBasicDetails> getSearchEmpByIdAndFullName(String value, String tenantId) throws Exception {
		try {
			List<EmpBasicDetails> basicDetailsList = userDao.getSearchEmpByIdAndFullName(value, tenantId);
			if (!basicDetailsList.isEmpty()) {
				return basicDetailsList;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while search employee by id and name " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	/* Two Factor Authentication By using Email Id) */
	@Override
	public void generateOneTimePassword(UserMst userMst) throws Exception {
		String OTP;
		try {
			SimpleDateFormat sdfDateTime = new SimpleDateFormat(ProdConstant.DATETIMEFORMATE);
			sdfDateTime.setTimeZone(TimeZone.getTimeZone(ProdConstant.TIMEZONE));

			Optional<UserMst> optional = userDao.findLoginByLoginId2(userMst.getLoginId());
			if (optional.isPresent()) {
				UserMst newUserMst = optional.get();
				OTP = RandomString.make(8);
				System.out.println("OTP :::: " + OTP);
				String encodedOTP = passwordEncoder.encode(OTP);
				newUserMst.setOneTimePassword(encodedOTP);
				newUserMst.setOtpRequestedTime(sdfDateTime.parse(sdfDateTime.format(new Date())));
				userDao.saveOneTimePassword(newUserMst);

				sendOTPEmail(OTP, newUserMst);
			} else {
				System.out.println("OTP NOT SAVE IN DB ::: ");
			}

		} catch (Exception e) {
			LOGGER.error("Error occur while generate One Time Password " + ExceptionUtils.getStackTrace(e));
		}
	}

//	@Override
//	public UserMst findByLoginId1(String loginId) throws Exception {
//		UserMst userMst = userDao.findByLoginId1(loginId);
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
	public void sendOTPEmail(String OTP, UserMst mst) throws Exception {
		try {
			String from = "springbootmail.2022@gmail.com";
			String to = mst.getEmailId();

			String subject = "Here's your One Time Password (OTP) - Expire in 5 minutes!";

			String content = "<p>Hello " + "Pushpendra" + " " + "Shakyawar" + "</p>"
					+ "<p>For security reason, you're required to use the following "
					+ "One Time Password to login:</p>" + "<p><b>" + OTP + "</b></p>" + "<br>"
					+ "<p>Note: this OTP is set to expire in 5 minutes.</p>";

			emailAndOTPService.emailsend(from, to, subject, content);
		} catch (Exception e) {
			LOGGER.error("Error occur while send OTP Email " + ExceptionUtils.getStackTrace(e));
		}

	}

	@Override
	public void clearOTP(UserMst userMst) throws Exception {
		try {
			Optional<UserMst> optional = userDao.findLoginByLoginId(userMst.getLoginId());
			if (optional.isPresent()) {
				UserMst newUserMst = optional.get();
				newUserMst.setOneTimePassword(null);
				newUserMst.setOtpRequestedTime(null);
				userDao.saveOrUpdateUser(newUserMst);
			} else {
				System.out.println("Error Occur in clearOTP ::: ");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while clear OTP " + ExceptionUtils.getStackTrace(e));
		}

	}

	/* Login Attempted Failed */
	@Override
	public void increaseFailedAttempts(UserMst user) {
		try {
			Integer newFailAttempts = user.getIsUserLoggedIn() + 1;
			userRepository.updateFailedAttempts(newFailAttempts, user.getLoginId());
		} catch (Exception e) {
			LOGGER.error("Error occur while increase Failed Attempts " + ExceptionUtils.getStackTrace(e));
		}

	}

	@Override
	public void resetFailedAttempts(String loginId) {
		try {
			userRepository.updateFailedAttempts(0, loginId);
		} catch (Exception e) {
			LOGGER.error("Error occur while update Failed Attempts " + ExceptionUtils.getStackTrace(e));
		}

	}

	@Override
	public void updateEnableStatus(String loginId) {
		try {
			userRepository.updateEnableStatus(1, loginId);
		} catch (Exception e) {
			LOGGER.error("Error occur while update Failed Attempts " + ExceptionUtils.getStackTrace(e));
		}

	}

	@Override
	public void lock(UserMst user) {
		try {
			SimpleDateFormat sdfDateTime = new SimpleDateFormat(ProdConstant.DATETIMEFORMATE);
			sdfDateTime.setTimeZone(TimeZone.getTimeZone(ProdConstant.TIMEZONE));

			user.setIsUserLocked(0);
			user.setLockTime(sdfDateTime.parse(sdfDateTime.format(new Date())));

			userDao.saveOrUpdateUser(user);
		} catch (Exception e) {
			LOGGER.error("Error occur while lock " + ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public UserMst saveResetPassword(UserMst user) throws Exception {
		try {
			if (Objects.nonNull(user)) {
				this.userDao.saveResetPassword(user);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while save & update user " + ExceptionUtils.getStackTrace(e));
		}
		return user;
	}

	@Override
	public UserMst getUserByToken(String passwordToken) throws Exception {
		UserMst userMst = new UserMst();
		try {
			userMst = userDao.getUserByToken(passwordToken);
		} catch (Exception e) {
			LOGGER.error("Error occur while open reset password page " + ExceptionUtils.getStackTrace(e));
		}
		return userMst;
	}

	public UserMst userEnableAndDisable(String loginId) {
		try {
			UserMst userDetails = userDao.getUserById(loginId);
			if (Objects.nonNull(userDetails)) {
				Integer checkActiveOrInActiveData = userDetails.getIsActive();

				if (checkActiveOrInActiveData.equals(ProdConstant.ISACTIVE)) {
					userDetails.setIsActive(ProdConstant.INACTIVE);
					userDao.saveOrUpdateUser(userDetails);
				} else if (checkActiveOrInActiveData.equals(ProdConstant.INACTIVE)) {
					userDetails.setIsActive(ProdConstant.ISACTIVE);
					userDao.saveOrUpdateUser(userDetails);
				}
			} else {
				System.out.println("RoleDetails object is null");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while activate and inactivate  user  " + ExceptionUtils.getStackTrace(e));
		}

		return new UserMst();

	}

}