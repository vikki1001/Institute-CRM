package com.ksv.ktrccrm.db1.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

public class LeadManagement implements Serializable {
	private static final long serialVersionUID = -8904623589024582169L;

	@Column(name = "ID")
	public long id;
	
	
	
	@Column(name = "ISACTIVE")
	public String isActive;
	
	@Column(name = "TENANTID")
	public String tenantId;
	
	@Column(name = "CREATEDDATE")
	public Date createdDate;
	
	@Column(name = "CREATEDBY")
	public String createdBy;
	
	@Column(name = "LASTMODIFIEDDATE")
	public Date lastModifiedDate;
	
	@Column(name = "LASTMODIFIEDBY")
	public String lastModifiedBy;
	
	
}
