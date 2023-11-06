package com.ksv.ktrccrm.dao;

import java.util.List;

import com.ksv.ktrccrm.db1.entities.Assets;

public interface AssetsDao {

	public List<Assets> getRecordList(String isActive, String tenantId) throws Exception;

	public Assets saveAssets(Assets assets) throws Exception;

	public Assets getAssetsById(Long id) throws Exception;

	public List<Assets> getEmpAssetsList(String empId) throws Exception;

	public Assets acceptAssets(Long id) throws Exception;

	public Assets rejectAssets(Long id) throws Exception;

	public void saveAll(List<Assets> assetsList) throws Exception;

	public List<Assets> findAll() throws Exception;

	public List<Assets> getEmpApprovedAssetsList(String empId) throws Exception;

	public List<Assets> getEmpRejectedAssetsList(String empId) throws Exception;

	public List<Assets> assetsNotification(String userId) throws Exception;

	public List<Assets> unreadNotification(String userId) throws Exception;

	public List<Assets> assetsNotificationRead(Long id) throws Exception;

	public List<Assets> checkDuplicateAssetsCode(String assetsCode) throws Exception;

	public boolean checkDuplicateException(String assetsCode) throws Exception;

}
