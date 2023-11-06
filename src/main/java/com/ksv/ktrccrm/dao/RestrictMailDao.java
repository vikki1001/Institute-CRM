package com.ksv.ktrccrm.dao;

import java.util.List;

public interface RestrictMailDao {

	List<String> getRestrictEmployees(String isActive) throws Exception;

}
