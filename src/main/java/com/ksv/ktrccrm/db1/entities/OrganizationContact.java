package com.ksv.ktrccrm.db1.entities;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "organizationcontact")
public class OrganizationContact implements Serializable {
	private static final long serialVersionUID = 1L;

	
	// @GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "ORGANIZATIONID")
	private String organizationId;

	@Column(name = "ORGANIZATIONNAME", length = 255)
	private String organizationName;

	@Column(name = "AFFILIATEDBY", length = 255)
	private String affiliatedBy;

	@Column(name = "EMAILID", length = 150)
	private String emailId;

	@Id
	@Column(name = "MOBILENUMBER", length = 10)
	private String mobileNumber;

	@Column(name = "ADDRESS", length = 255)
	private String address;

	@Column(name = "WEBSITELINK", length = 255)
	private String websiteLink;

	@Column(name = "ADDEDDATE")
	private Date addedDate;

	@Column(name = "ADDEDBY", length = 100)
	private String addedBy;

	@Column(name = "MODIFIEDDATE")
	private Date modifiedDate;

	@Column(name = "MODIFIEDBY", length = 100)
	private String modifiedBy;

	@Column(name = "ISACTIVE", columnDefinition = "varchar(10) default '1'")
	private String isActive;

	@Column(name = "TENANTID", length = 10)
	private String tenantId;

	@Column(name = "VERSION", length = 20)
	private Integer version;

	// @OneToOne(mappedBy = "organizationContact", cascade = CascadeType.ALL)
//	@OneToMany(mappedBy = "organizationContact", cascade = CascadeType.ALL, orphanRemoval = false)
//	@OneToMany(mappedBy = "organizationContact", cascade = CascadeType.PERSIST)
//	private List<CustomerContact> customerContact;

//	// @OneToOne(mappedBy = "organizationContact", cascade = CascadeType.ALL)
//	@OneToMany(mappedBy = "organizationContact", cascade = CascadeType.ALL, orphanRemoval = false)
//	private List<OrganizationEmployeeContact> organizationEmployeeContact;

//	public List<CustomerContact> getCustomerContact() {
//		return customerContact;
//	}
//
//	public void setCustomerContact(List<CustomerContact> customerContact) {
//		this.customerContact = customerContact;
//	}

//	public List<OrganizationEmployeeContact> getOrganizationEmployeeContact() {
//		return organizationEmployeeContact;
//	}
//
//	public void setOrganizationEmployeeContact(List<OrganizationEmployeeContact> organizationEmployeeContact) {
//		this.organizationEmployeeContact = organizationEmployeeContact;
//	}

	public String getOrganizationName() {
		return organizationName;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getAffiliatedBy() {
		return affiliatedBy;
	}

	public void setAffiliatedBy(String affiliatedBy) {
		this.affiliatedBy = affiliatedBy;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getWebsiteLink() {
		return websiteLink;
	}

	public void setWebsiteLink(String websiteLink) {
		this.websiteLink = websiteLink;
	}

	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public static class Comparators {
		public static Comparator<OrganizationContact> ORGANIZATIONNAME = new Comparator<OrganizationContact>() {

			@Override
			public int compare(OrganizationContact o1, OrganizationContact o2) {
				return o1.organizationName.compareTo(o2.organizationName);
			}
		};
	}

//	@Override
//	public String toString() {
//		return "OrganizationContact [organizationId=" + organizationId + ", organizationName=" + organizationName
//				+ ", affiliatedBy=" + affiliatedBy + ", emailId=" + emailId + ", mobileNumber=" + mobileNumber
//				+ ", address=" + address + ", websiteLink=" + websiteLink + ", addedDate=" + addedDate + ", addedBy="
//				+ addedBy + ", modifiedDate=" + modifiedDate + ", modifiedBy=" + modifiedBy + ", isActive=" + isActive
//				+ ", tenantId=" + tenantId + ", version=" + version + ", customerContact="// + customerContact
//				+ ", organizationEmployeeContact=" + organizationEmployeeContact + "]";
//	}
	
	@Override
	public String toString() {
		return "OrganizationContact [organizationId=" + organizationId + ", organizationName=" + organizationName
				+ ", affiliatedBy=" + affiliatedBy + ", emailId=" + emailId + ", mobileNumber=" + mobileNumber
				+ ", address=" + address + ", websiteLink=" + websiteLink + ", addedDate=" + addedDate + ", addedBy="
				+ addedBy + ", modifiedDate=" + modifiedDate + ", modifiedBy=" + modifiedBy + ", isActive=" + isActive
				+ ", tenantId=" + tenantId + ", version=" + version + ", customerContact="// + customerContact
				+  "]";
	}

}
