package com.ksv.ktrccrm.db1.entities;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rolemst")
public class RoleMst implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ROLEID", length = 10)
	private Long roleId;
	@Column(name = "ROLENAME", length = 20)
	private String roleName;
	@Column(name = "ROLECOD", length = 20)
	private String roleCode;
	@Column(name = "LEVEL", length = 10)
	private String level;
	@Column(name = "IPADD", length = 50)
	private String ipAddress;
	@Column(name = "TENANTID", length = 20)
	private String tenantId;
	@Column(name = "LASTMODDATE")
	private Date lastModifiedDate;
	@Column(name = "ISHEADROLYN", length = 10)
	private String isHeadRoleYn;
	@Column(name = "ISACTIVE", length = 10)
	private String isActive;
	@Column(name = "CREATEDDATE")
	private Date createdDate;
	@Column(name = "LASTMODBY", length = 50)
	private String lastModifiedBy;
	@Column(name = "HOSTNAME", length = 50)
	private String hostName;
	@Column(name = "DESCRIPTION", length = 100)
	private String description;
	@Column(name = "AUTHSTATUS", length = 50)
	private String authStatus;
	@Column(name = "VERSION", length = 20)
	private Integer version;
	@Column(name = "CREATEDBY", length = 50)
	private String createdBy;
	@Column(name = "ISADMFLG", length = 20)
	private Integer isAdminFlag;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getIsHeadRoleYn() {
		return isHeadRoleYn;
	}

	public void setIsHeadRoleYn(String isHeadRoleYn) {
		this.isHeadRoleYn = isHeadRoleYn;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getIsAdminFlag() {
		return isAdminFlag;
	}

	public void setIsAdminFlag(Integer isAdminFlag) {
		this.isAdminFlag = isAdminFlag;
	}

	public static class Comparators {
		public static Comparator<RoleMst> ROLECODE = new Comparator<RoleMst>() {

			@Override
			public int compare(RoleMst o1, RoleMst o2) {
				return o1.roleCode.compareTo(o2.roleCode);
			}
		};
	}
}