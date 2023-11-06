package com.ksv.ktrccrm.db1.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "smsrecordstore")
public class SMSRecordStore implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "CUSTOMERID")
	private String customerId;

	@Column(name = "ORGANIZATIONID")
	private String organizationId;

	@Column(name = "FROMNUMBER")
	private String fromNumber;

	@Column(name = "TONUMBER")
	private String toNumber;

	@Column(name = "MESSAGE")
	private String message;

	@Column(name = "CATEGORY")
	private String category;

	@Column(name = "SUBCATEGORY")
	private String subCategory;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "CREATEDDATE")
	private Date createdDate;

	@Column(name = "CREATEDBY")
	private String createdBy;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getFromNumber() {
		return fromNumber;
	}

	public void setFromNumber(String fromNumber) {
		this.fromNumber = fromNumber;
	}

	public String getToNumber() {
		return toNumber;
	}

	public void setToNumber(String toNumber) {
		this.toNumber = toNumber;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	@Override
	public String toString() {
		return "SMSRecordStore [id=" + id + ", customerId=" + customerId + ", organizationId=" + organizationId
				+ ", fromNumber=" + fromNumber + ", toNumber=" + toNumber + ", message=" + message + ", category="
				+ category + ", subCategory=" + subCategory + ", status=" + status + ", createdDate=" + createdDate
				+ ", createdBy=" + createdBy + "]";
	}

}