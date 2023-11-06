package com.ksv.ktrccrm.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ksv.ktrccrm.db1.entities.MenuMst;

public interface MenuService {

	public List<MenuMst> getMenuMstIn(String tenantId, Set<String> menuCodeSet) throws Exception;
	
	public Map<String, List<MenuMst>> getAccessibleMenu(String tenantId, String languageCode, Set<String> userMenuCodeSet)throws Exception;
	
	public List<MenuMst> getAllIsActive() throws Exception;

	public List<MenuMst> getMenu(String roleCode) throws Exception;
		
}
