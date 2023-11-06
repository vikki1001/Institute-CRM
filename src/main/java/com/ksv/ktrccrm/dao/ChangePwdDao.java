package com.ksv.ktrccrm.dao;

import com.ksv.ktrccrm.db1.entities.UserMst;

public interface ChangePwdDao {

	public UserMst getUserByLoginId(String loginId) throws Exception;
	
	public UserMst changePwd(UserMst userMst) throws Exception;
	
}
