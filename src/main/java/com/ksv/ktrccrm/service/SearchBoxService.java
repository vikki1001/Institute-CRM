package com.ksv.ktrccrm.service;

import java.util.List;

import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;

public interface SearchBoxService {

	public List<EmpBasicDetails> getSearchEmployeeByFullName(String value) throws Exception;
}
