
package com.ksv.ktrccrm.db1.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "sequencemstcrm")
public class SequenceMstCRM implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "SEQUENCENO")
	private String sequenceNo;

	@Column(name = "LASTSEQUENCENO")
	private String lastSequenceNo;

	@Column(name = "SEQUENCEKEY")
	private String sequenceKey;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATEDDATE")
	private Date createdDate;

	@Column(name = "TENANTID")
	private String tenantId;

	@Column(name = "ISACTIVE")
	private String isActive;

	@Column(name = "NAME")
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public String getLastSequenceNo() {
		return lastSequenceNo;
	}

	public void setLastSequenceNo(String lastSequenceNo) {
		this.lastSequenceNo = lastSequenceNo;
	}

	public String getSequenceKey() {
		return sequenceKey;
	}

	public void setSequenceKey(String sequenceKey) {
		this.sequenceKey = sequenceKey;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
