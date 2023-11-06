package com.ksv.ktrccrm.db1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ksv.ktrccrm.db1.entities.RoleMst;

public interface RoleRepository extends JpaRepository<RoleMst, Long> {

	/* For Duplicate Role Code Validation */
	@Query("SELECT e FROM RoleMst e WHERE e.roleCode = :roleCode")
	public Optional<RoleMst> findRoleByRoleCode(String roleCode);

	@Query("SELECT u FROM RoleMst u WHERE u.isActive=:isActive AND u.tenantId=:tenantId")
	public List<RoleMst> getRecordList(String isActive, String tenantId);

	@Query("UPDATE RoleMst s SET s.isActive=:isActive WHERE s.roleId = :roleId")
	public List<RoleMst> findByroleId(String isActive, Long roleId);

	@Query("SELECT u FROM RoleMst u WHERE u.roleId =:roleId")
	public RoleMst getDataById(Long roleId);

}
