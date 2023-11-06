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
@Table(name = "mailrecordstore")
public class MailRecordStore implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "CUSTOMERID")
	private String customerId;

	@Column(name = "ORGANIZATIONID")
	private String organizationId;

	@Column(name = "FROMID")
	private String fromId;

	@Column(name = "TOID")
	private String toId;

	@Column(name = "SUBJECT")
	private String subject;

	@Column(name = "DESCRIPTION", length = 8000)
	private String description;

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

	public String getFromId() {
		return fromId;
	}

	public void setFromId(String fromId) {
		this.fromId = fromId;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
		return "MailRecordStore [id=" + id + ", customerId=" + customerId + ", organizationId=" + organizationId
				+ ", fromId=" + fromId + ", toId=" + toId + ", subject=" + subject + ", description=" + description
				+ ", category=" + category + ", subCategory=" + subCategory + ", status=" + status + ", createdDate="
				+ createdDate + ", createdBy=" + createdBy + "]";
	}
	
	

}