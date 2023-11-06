package com.ksv.ktrccrm.db1.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "assets")
public class Assets implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "EMPID")
	private String empId;

	@Column(name = "EMPNAME")
	private String empName;

	@Column(name = "CONFIRMATIONSTATUS")
	private String confirmationStatus;

	@Column(name = "ASSETSDESCRIPTION")
	private String assetsDescription;

	@Column(name = "ASSETSCODE", unique = true)
	private String assetsCode;

	@Column(name = "ASSETSMAKE")
	private String assetsMake;

	@Column(name = "ASSETSMODEL")
	private String assetsModel;

	@Column(name = "ASSETSSERVICETAG")
	private String assetsServiceTag;

	@DateTimeFormat(pattern = "yyyy-MM-dd hh:MM a")
	@Column(name = "ADDEDTIME")
	private String addedTime;

	@Column(name = "LASTMODIFIEDBY")
	private String lastModifiedBy;

	@Column(name = "MODIFIEDTIME")
	private Date modifiedTime;

	@Column(name = "ISACTIVE")
	private String isActive;

	@Column(name = "CREATEDDATE")
	private Date createdDate;

	@Column(name = "CREATEDBY")
	private String createdBy;

	@Column(name = "LASTMODIFIEDDATE")
	private Date lastModifiedDate;

	@Column(name = "NOTIFICATION", length = 20)
	private String notification;
	
	@Column(name = "TENANTID")
	private String tenantId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getConfirmationStatus() {
		return confirmationStatus;
	}

	public void setConfirmationStatus(String confirmationStatus) {
		this.confirmationStatus = confirmationStatus;
	}

	public String getAssetsDescription() {
		return assetsDescription;
	}

	public void setAssetsDescription(String assetsDescription) {
		this.assetsDescription = assetsDescription;
	}

	public String getAssetsCode() {
		return assetsCode;
	}

	public void setAssetsCode(String assetsCode) {
		this.assetsCode = assetsCode;
	}

	public String getAssetsMake() {
		return assetsMake;
	}

	public void setAssetsMake(String assetsMake) {
		this.assetsMake = assetsMake;
	}

	public String getAssetsModel() {
		return assetsModel;
	}

	public void setAssetsModel(String assetsModel) {
		this.assetsModel = assetsModel;
	}

	public String getAssetsServiceTag() {
		return assetsServiceTag;
	}

	public void setAssetsServiceTag(String assetsServiceTag) {
		this.assetsServiceTag = assetsServiceTag;
	}

	public String getAddedTime() {
		return addedTime;
	}

	public void setAddedTime(String addedTime) {
		this.addedTime = addedTime;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getNotification() {
		return notification;
	}

	public void setNotification(String notification) {
		this.notification = notification;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	@Override
	public String toString() {
		return "Assets [id=" + id + ", empId=" + empId + ", empName=" + empName + ", confirmationStatus="
				+ confirmationStatus + ", assetsDescription=" + assetsDescription + ", assetsCode=" + assetsCode
				+ ", assetsMake=" + assetsMake + ", assetsModel=" + assetsModel + ", assetsServiceTag="
				+ assetsServiceTag + ", addedTime=" + addedTime + ", lastModifiedBy=" + lastModifiedBy
				+ ", modifiedTime=" + modifiedTime + ", isActive=" + isActive + ", createdDate=" + createdDate
				+ ", createdBy=" + createdBy + ", lastModifiedDate=" + lastModifiedDate + "]";
	}

}
