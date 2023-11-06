package com.ksv.ktrccrm.db1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ksv.ktrccrm.db1.entities.UserMst;

@Transactional
public interface UserRepository extends JpaRepository<UserMst, String> {

	@Query("SELECT e FROM UserMst e WHERE e.loginId = :loginId")
	public UserMst findByLoginId(String loginId);

	@Query("SELECT u FROM UserMst u WHERE u.isActive=:isActive AND u.tenantId=:tenantId")
	public List<UserMst> getRecordList(Integer isActive , String tenantId);

	@Query("SELECT u FROM UserMst u WHERE u.loginId = :loginId AND u.isActive=:isActive")
	public UserMst getUserDetails(String loginId,Integer isActive);
	
	/* Duplicate Login Id Check */
	@Query("SELECT e FROM UserMst e WHERE e.loginId = :loginId")
	public Optional<UserMst> findLoginByLoginId(String loginId);
	
	@Query("SELECT e FROM UserMst e WHERE e.password = :password")
	public Optional<UserMst> getPasswordCheck(String password);

	@Query("SELECT u FROM UserMst u WHERE u.loginId = :loginId AND u.isActive=:isActive AND u.tenantId=:tenantId")
	public UserMst getLoginAndTenantId(String loginId, String tenantId,Integer isActive);
	
	@Modifying
	@Query("UPDATE UserMst SET isUserLocked = :isUserLocked WHERE loginId = :loginId")
    public void updateEnableStatus(@Param("isUserLocked") Integer isUserLocked, @Param("loginId") String loginId);
	
	@Modifying
	@Query("UPDATE UserMst SET isUserLoggedIn = :isUserLoggedIn WHERE loginId = :loginId")
    public void updateFailedAttempts(@Param("isUserLoggedIn") Integer isUserLoggedIn, @Param("loginId") String loginId);

	@Modifying
	@Query("update UserMst set randomOTP = :randomOTP where emailId = :emailId")
	void setRandomOTP(Integer randomOTP, String emailId);
	
	/* Reset Password */
	@Modifying
	@Query("update UserMst set password = :password, passwordToken = :passwordToken  where emailId = :emailId")
	void resetPassword(String password, String passwordToken, String emailId);
	
	@Query("SELECT u FROM UserMst u WHERE u.passwordToken=:passwordToken")
	public UserMst getPasswordPolicy(String passwordToken);
}