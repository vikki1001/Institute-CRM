package com.ksv.ktrccrm.service;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ksv.ktrccrm.db1.entities.Assets;

public interface AssetsService {

	public List<Assets> getRecordList(String isActive, String tenantId) throws Exception;

	public Assets saveAssets(Assets assets) throws Exception;

	public Assets getAssetsById(Long id) throws Exception;

	public List<Assets> getEmpAssetsList(String empId) throws Exception;

	public Assets acceptAssets(Long id) throws Exception;

	public Assets rejectAssets(Long id) throws Exception;

	public void saveAll(MultipartFile files) throws Exception;

	public List<Assets> findAll() throws Exception;

	public ByteArrayInputStream exportAssets(List<Assets> assets) throws Exception;

	public List<Assets> getEmpApprovedAssetsList(String empId) throws Exception;

	public List<Assets> getEmpRejectedAssetsList(String empId) throws Exception;

	public List<Assets> assetsNotification(String userId)throws Exception;

	public List<Assets> unreadNotification(String userId)throws Exception;

	public List<Assets> assetsNotificationRead(Long id)throws Exception;

	public List<Assets> checkDuplicateAssetsCode(String assetsCode) throws Exception;        

	public boolean checkDuplicateException(String assetsCode) throws Exception;

	public void assetsEnableAndDisable(Long id) throws Exception;

}
