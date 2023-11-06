package com.ksv.ktrccrm.service;

import java.util.List;

public interface RestrictMailService {

	List<String> getRestrictEmployees(String isActive) throws Exception;

}
