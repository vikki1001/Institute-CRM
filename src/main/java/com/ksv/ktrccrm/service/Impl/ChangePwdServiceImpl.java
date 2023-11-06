package com.ksv.ktrccrm.service.Impl;

import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ksv.ktrccrm.dao.ChangePwdDao;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.ChangePwdService;

@Service
public class ChangePwdServiceImpl implements ChangePwdService {
	private static final Logger LOGGER = LogManager.getLogger(ChangePwdServiceImpl.class);

	@Autowired
	private ChangePwdDao changePwdDao;

	@Override
	public UserMst getUserByLoginId(String loginId) throws Exception {
		UserMst userMst = changePwdDao.getUserByLoginId(loginId);
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
		try {
			if (Objects.nonNull(userMst)) {
				changePwdDao.changePwd(userMst);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while change password " + ExceptionUtils.getStackTrace(e));
		}
		return userMst;
	}

}
