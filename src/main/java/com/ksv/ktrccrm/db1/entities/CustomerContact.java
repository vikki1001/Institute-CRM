package com.ksv.ktrccrm.db1.entities;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "customercontact")
public class CustomerContact implements Serializable {
	private static final long serialVersionUID = 1L;

	
	@Column(name = "CUSTOMERID", unique = true)
	private String customerId;

	@Column(name = "FIRSTNAME", length = 50)
	private String firstName;

	@Column(name = "LASTNAME", length = 50)
	private String lastName;

	@Column(name = "ORGANIZATION", length = 255)
	private String organization;

	@Column(name = "BRANCH")
	private String branch;

	@Column(name = "YEAR", length = 10)
	private String year;

	@Column(name = "EMAILID", length = 150)
	private String emailId;

	@Id
	@Column(name = "MOBILENUMBER", length = 10)
	private String mobileNumber;

	@Column(name = "LINKEDINURL", length = 255)
	private String linkedinURL;

	@Column(name = "TWITTERURL", length = 255)
	private String twitterURL;

	@Column(name = "FACEBOOKURL", length = 255)
	private String facebookURL;

	@Column(name = "ADDRESS", length = 255)
	private String address;

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

//	//@OneToOne(cascade = CascadeType.ALL)
//	@ManyToOne(fetch = FetchType.LAZY, optional = false)
//	@JoinColumn(name = "ORGANIZATIONID", nullable = false)
//	private OrganizationContact organizationContact;

	

//	public OrganizationContact getOrganizationContact() {
//		return organizationContact;
//	}
//
//	public void setOrganizationContact(OrganizationContact organizationContact) {
//		this.organizationContact = organizationContact;
//	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
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

	public String getLinkedinURL() {
		return linkedinURL;
	}

	public void setLinkedinURL(String linkedinURL) {
		this.linkedinURL = linkedinURL;
	}

	public String getTwitterURL() {
		return twitterURL;
	}

	public void setTwitterURL(String twitterURL) {
		this.twitterURL = twitterURL;
	}

	public String getFacebookURL() {
		return facebookURL;
	}

	public void setFacebookURL(String facebookURL) {
		this.facebookURL = facebookURL;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
	
	public String getFirstLastName() {
		return firstName + " " + lastName;
	}

	public static class Comparators {
		public static Comparator<CustomerContact> CUSTOMERCONTACTNAME = new Comparator<CustomerContact>() {

			@Override
			public int compare(CustomerContact o1, CustomerContact o2) {
				return o1.firstName.compareTo(o2.firstName);
			}
		};
	}
	
	@Override
	public String toString() {
		return "CustomerContact [customerId=" + customerId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", organization=" + organization + ", branch=" + branch + ", year=" + year + ", emailId=" + emailId
				+ ", mobileNumber=" + mobileNumber + ", linkedinURL=" + linkedinURL + ", twitterURL=" + twitterURL
				+ ", facebookURL=" + facebookURL + ", address=" + address + ", addedDate=" + addedDate + ", addedBy="
				+ addedBy + ", modifiedDate=" + modifiedDate + ", modifiedBy=" + modifiedBy + ", isActive=" + isActive
				+ ", tenantId=" + tenantId + ", version=" + version + ", organizationContact=" //+ organizationContact
				+ "]";
	}

}
