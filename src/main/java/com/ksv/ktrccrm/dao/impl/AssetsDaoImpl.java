package com.ksv.ktrccrm.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.AssetsDao;
import com.ksv.ktrccrm.db1.entities.Assets;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.db1.repository.AssetsRepository;
import com.ksv.ktrccrm.service.UserService;

@Repository
public class AssetsDaoImpl implements AssetsDao {
	private static final Logger LOGGER = LogManager.getLogger(AssetsDaoImpl.class);

	@Autowired
	private AssetsRepository assetsRepository;
	
	@Autowired
	private UserService userService;

	/* Active Assets List */
	@Override
	public List<Assets> getRecordList(String isActive, String tenantId) throws Exception {
		try {
			List<Assets> assets = assetsRepository.getRecordList(isActive, tenantId);
			if (Objects.nonNull(assets)) {
				return assets;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display assets list " + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();

	}

	/* Save Assets */
	@Override
	public Assets saveAssets(Assets assets) throws Exception {
		String empId;
		
		String tenantId = null;
		UserMst user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		empId = authentication.getName();
		user = userService.getUserById(empId);
		tenantId = user.getTenantId();
System.out.println("tenantId ------------"+tenantId);		
		try {

			if (Objects.isNull(assets.getId())) {
				assets.setCreatedDate(new Date());
				assets.setCreatedBy(empId);
				assets.setIsActive("1");
				assets.setConfirmationStatus("Pending");
				assets.setNotification("Unread");
				assets.setTenantId(tenantId);
				assetsRepository.save(assets);
			} else {

				Optional<Assets> optional = assetsRepository.findById(assets.getId());
				if (optional.isPresent()) {
					Assets newAssets = optional.get();
					newAssets.setEmpId(assets.getEmpId());
					newAssets.setEmpName(assets.getEmpName());
					newAssets.setAssetsCode(assets.getAssetsCode());
					newAssets.setAssetsModel(assets.getAssetsModel());
					newAssets.setAssetsMake(assets.getAssetsMake());
					newAssets.setConfirmationStatus("Pending");
					newAssets.setAssetsServiceTag(assets.getAssetsServiceTag());
					newAssets.setAddedTime(assets.getAddedTime());
					newAssets.setAssetsDescription(assets.getAssetsDescription());
					newAssets.setModifiedTime(assets.getModifiedTime());
					newAssets.setLastModifiedBy(empId);
					newAssets.setLastModifiedDate(new Date());
					newAssets.setTenantId(assets.getTenantId());
					assetsRepository.save(newAssets);
				} else {
					return assets;
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while save & update assets record " + ExceptionUtils.getStackTrace(e));
		}
		return assets;
	}

	/* Update Assets Home */
	@Override
	public Assets getAssetsById(Long id) throws Exception {
		Optional<Assets> optional = assetsRepository.findById(id);
		Assets assets = null;
		try {
			if (optional.isPresent()) {
				assets = optional.get();
			}
		} catch (Exception e) {
			LOGGER.error("Error while get assets by id " + ExceptionUtils.getStackTrace(e));
		}
		return assets;
	}

	/* Assets Display to Pending Employee List */
	@Override
	public List<Assets> getEmpAssetsList(String empId) throws Exception {
		List<Assets> assets = null;
		try {
			assets = assetsRepository.getEmpAssetsList(empId, ProdConstant.TRUE, ProdConstant.PENDING);
		} catch (Exception e) {
			LOGGER.error(" Error occur while display employee Assets list " + ExceptionUtils.getStackTrace(e));
		}
		return assets;
	}

	/* Assets Request Accept By Employee */
	@Override
	public Assets acceptAssets(Long id) throws Exception {
		try {
			Optional<Assets> optional = assetsRepository.findById(id);
			if (optional.isPresent()) {
				Assets newAssets = optional.get();
				newAssets.setConfirmationStatus("Approved");
				assetsRepository.save(newAssets);
			}
		} catch (Exception e) {
			LOGGER.error(" Error occur while accept assets  " + ExceptionUtils.getStackTrace(e));
		}
		return new Assets();
	}

	/* Assets Request Reject By Employee */
	@Override
	public Assets rejectAssets(Long id) throws Exception {
		try {
			Optional<Assets> optional = assetsRepository.findById(id);
			if (optional != null) {
				Assets newAssets = optional.get();
				newAssets.setConfirmationStatus("Rejected");
				assetsRepository.save(newAssets);
			}
		} catch (Exception e) {
			LOGGER.error(" Error occur while reject assets  " + ExceptionUtils.getStackTrace(e));
		}
		return new Assets();
	}

	@Override
	public void saveAll(List<Assets> assetsList) throws Exception {
		try {
			this.assetsRepository.saveAll(assetsList);
		} catch (Exception e) {
			LOGGER.error("Error while save assets list" + ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public List<Assets> findAll() throws Exception {
		try {
			List<Assets> assets = assetsRepository.findAll();
			if (assets != null) {
				return assets;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display all assets" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<Assets>();
	}

	/* Assets Display to Approved Employee List */
	@Override
	public List<Assets> getEmpApprovedAssetsList(String empId) throws Exception {
		List<Assets> assets = null;
		try {
			assets = assetsRepository.getEmpAssetsList(empId, ProdConstant.TRUE, ProdConstant.APPROVED);
		} catch (Exception e) {
			LOGGER.error(" Error occur while display employee approved Assets list " + ExceptionUtils.getStackTrace(e));
		}
		return assets;
	}

	/* Assets Display to Rejected Employee List */
	@Override
	public List<Assets> getEmpRejectedAssetsList(String empId) throws Exception {
		List<Assets> assets = null;
		try {
			assets = assetsRepository.getEmpAssetsList(empId, ProdConstant.TRUE, ProdConstant.REJECTED);
		} catch (Exception e) {
			LOGGER.error(" Error occur while display employee rejected Assets list " + ExceptionUtils.getStackTrace(e));
		}
		return assets;
	}

	/* For Assets Notification */
	@Override
	public List<Assets> assetsNotification(String userId) throws Exception {
		List<Assets> assets = assetsRepository.assetsNotification(userId, ProdConstant.TRUE);
		try {
			if (assets != null) {
				return assets;
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occur while display list of assets notification ------" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	/* For Get Unread Notification */
	@Override
	public List<Assets> unreadNotification(String userId) throws Exception {
		List<Assets> assets = assetsRepository.unreadNotification(userId, ProdConstant.TRUE, ProdConstant.UNREAD);
		try {
			if (assets != null) {
				return assets;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while unread assets notification /n" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	/* For Get Read Notification */
	@Override
	public List<Assets> assetsNotificationRead(Long id) throws Exception {
		try {

			Optional<Assets> optional = assetsRepository.findById(id);
			if (optional.isPresent()) {
				Assets assets2 = optional.get();
				assets2.setNotification("Read");
				assetsRepository.save(assets2);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while assets read notification /n" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public List<Assets> checkDuplicateAssetsCode(String assetsCode) throws Exception {
		List<Assets> optional = assetsRepository.checkDuplicateAssetsCode(assetsCode);
		try {
			if (Objects.nonNull(optional)) {
				return optional;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while check duplicate assets code" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@Override
	public boolean checkDuplicateException(String assetsCode) throws Exception {
		try {
			List<Assets> checkDiplicate = checkDuplicateAssetsCode(assetsCode);
			if (Objects.nonNull(checkDiplicate)) {
				return true;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while find duplicate assets excel" + ExceptionUtils.getStackTrace(e));
		}
		return false;
	}
}
