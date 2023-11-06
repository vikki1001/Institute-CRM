package com.ksv.ktrccrm.service;

import com.ksv.ktrccrm.db1.entities.UserMst;

public interface ChangePwdService {
	
	public UserMst getUserByLoginId(String loginId) throws Exception;
	
	public UserMst changePwd(UserMst userMst) throws Exception;
}
