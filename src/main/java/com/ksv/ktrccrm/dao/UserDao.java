package com.ksv.ktrccrm.dao;

import java.util.List;
import java.util.Optional;

import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.UserMst;

public interface UserDao {

	public UserMst saveOrUpdateUser(UserMst user) throws Exception;

	public UserMst getUserById(String loginId) throws Exception;

	public List<UserMst> getRecordList(Integer isActive, String tenantId) throws Exception;

	public UserMst getUserDetails(String loginId, Integer isActive) throws Exception;

	public Optional<UserMst> getPasswordCheck(String password) throws Exception;

	public boolean userExists(String password) throws Exception;

	public Optional<UserMst> findLoginByLoginId(String loginId) throws Exception;

	public boolean loginIdExists(String loginId) throws Exception;

	public void resetPassword(String password, String passwordToken, String emailId) throws Exception;

	public void setRandomOTP(int randomOTP, String emailId) throws Exception;

	public List<EmpBasicDetails> getSearchEmpByIdAndFullName(String value, String tenantId) throws Exception;

	public void saveOneTimePassword(UserMst newUserMst) throws Exception;

	public Optional<UserMst> findLoginByLoginId2(String loginId) throws Exception;

	public UserMst saveResetPassword(UserMst user) throws Exception;

	public UserMst getUserByToken(String passwordToken) throws Exception;

}
