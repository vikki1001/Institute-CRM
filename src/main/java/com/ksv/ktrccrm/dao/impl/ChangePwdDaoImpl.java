package com.ksv.ktrccrm.dao.impl;

import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.ChangePwdDao;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.db1.repository.UserRepository;

@Repository
public class ChangePwdDaoImpl implements ChangePwdDao {
	private static final Logger LOGGER = LogManager.getLogger(ChangePwdDaoImpl.class);

	@Autowired
	private UserRepository userRepository;
	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;

	@Override
	public UserMst getUserByLoginId(String loginId) throws Exception {
		UserMst userMst = userRepository.getUserDetails(loginId,ProdConstant.ISACTIVE);
		try {
			if (Objects.nonNull(userMst)) {
				return userMst;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while open change password page " + ExceptionUtils.getStackTrace(e));
		}
		return new UserMst();
	}

	@Override
	public UserMst changePwd(UserMst userMst) throws Exception {
		String loginId;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			loginId = authentication.getName();
			userMst.setLastModifieDate(checkOutDaoImpl.getDateTime());
			userMst.setLastModifiedBy(loginId);
			userRepository.save(userMst);
		} catch (Exception e) {
			LOGGER.error("Error occur while change password " + ExceptionUtils.getStackTrace(e));
		}
		return userMst;
	}
}