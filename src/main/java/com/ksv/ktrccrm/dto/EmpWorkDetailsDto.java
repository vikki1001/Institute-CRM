package com.ksv.ktrccrm.dto;

public class EmpWorkDetailsDto {

	private String allEmpIds;
	private String allEmpFullName;
	private String allEmpDoj;
	private byte[] allEmpImage;

	public String getAllEmpIds() {
		return allEmpIds;
	}

	public void setAllEmpIds(String allEmpIds) {
		this.allEmpIds = allEmpIds;
	}

	public String getAllEmpFullName() {
		return allEmpFullName;
	}

	public void setAllEmpFullName(String allEmpFullName) {
		this.allEmpFullName = allEmpFullName;
	}

	public String getAllEmpDoj() {
		return allEmpDoj;
	}

	public void setAllEmpDoj(String allEmpDoj) {
		this.allEmpDoj = allEmpDoj;
	}

	public byte[] getAllEmpImage() {
		return allEmpImage;
	}

	public void setAllEmpImage(byte[] allEmpImage) {
		this.allEmpImage = allEmpImage;
	}

	@Override
	public String toString() {
		return "EmpWorkDetailsDto [allEmpIds=" + allEmpIds + ", allEmpFullName=" + allEmpFullName + ", allEmpDoj="
				+ allEmpDoj + "]";
	}

}
