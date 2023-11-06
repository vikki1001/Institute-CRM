package com.ksv.ktrccrm.dao;

import java.util.List;

import com.ksv.ktrccrm.db1.entities.SequenceMst;

public interface SequenceMstDao {

	public List<SequenceMst> getTenantId(String isActive) throws Exception;
}
