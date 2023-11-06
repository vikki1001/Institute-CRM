package com.ksv.ktrccrm.dao;

import java.util.List;

import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;

public interface SearchBoxDao {

	public List<EmpBasicDetails> getSearchEmployeeByFullName(String value) throws Exception;
}
