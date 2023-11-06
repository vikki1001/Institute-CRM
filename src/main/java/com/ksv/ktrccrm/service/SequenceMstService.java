package com.ksv.ktrccrm.service;

import java.util.List;

import com.ksv.ktrccrm.db1.entities.SequenceMst;

public interface SequenceMstService {

	public List<SequenceMst> getTenantId(String isActive) throws Exception;

}
