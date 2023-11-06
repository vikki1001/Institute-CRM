package com.ksv.ktrccrm.db1.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ksv.ktrccrm.db1.entities.MenuMst;
import com.ksv.ktrccrm.db1.entities.RoleMst;

public interface MenuRepository extends JpaRepository<MenuMst, Integer> {

	@Query("SELECT u FROM MenuMst u WHERE u.tenantId=:tenantId AND u.menuCode IN :menuCode AND u.isActive=:isActive ")
	public List<MenuMst> getMenuMstIn(@Param("tenantId") String tenantId, @Param("menuCode") Set<String> menuCode,String isActive);

	@Query("SELECT u FROM MenuMst u WHERE u.tenantId=:tenantId AND u.menuCode=:menuCode AND u.isActive=:isActive")
	public List<MenuMst> getMenuMst(String tenantId, String menuCode, String isActive);

	@Query("SELECT u FROM MenuMst u WHERE u.tenantId=:tenantId AND u.sectionDisplayName=:sectionCode AND u.isVisible=:isVisible AND u.isActive=:isActive")
	public List<MenuMst> getMenuMstBySubSection(String tenantId, String sectionCode, Integer isVisible,String isActive);

	@Query("SELECT u FROM MenuMst u WHERE u.isActive=:isActive")
	public List<MenuMst> getIsActive(String isActive);
	
	@Query("SELECT u FROM RoleMst u WHERE u.roleCode = :roleCode")
	public RoleMst getByroleCode(@Param("roleCode") String roleCode);

	@Query("SELECT u FROM MenuMst u WHERE u.sectionCode = :roleCode AND u.isActive=:isActive")
	public List<MenuMst> getMenu(@Param("roleCode") String roleCode,String isActive);
}