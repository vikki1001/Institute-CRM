package com.ksv.ktrccrm.service.Impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.AssetsDao;
import com.ksv.ktrccrm.db1.entities.Assets;
import com.ksv.ktrccrm.db1.entities.UserMst;
import com.ksv.ktrccrm.service.AssetsService;
import com.ksv.ktrccrm.service.UserService;

@Service
public class AssetsServiceImpl implements AssetsService {
	private static final Logger LOGGER = LogManager.getLogger(AssetsServiceImpl.class);

	@Autowired
	private AssetsDao assetsDao;
	@Autowired UserService userService;

	/* Active Assets List */
	@Override
	public List<Assets> getRecordList(String isActive, String tenantId) {
		try {
			List<Assets> assets = assetsDao.getRecordList(isActive, tenantId);
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
		try {
			if (Objects.nonNull(assets)) {
				assetsDao.saveAssets(assets);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while save & update assets record " + ExceptionUtils.getStackTrace(e));
		}
		return assets;
	}

	/* Update Assets Home */
	@Override
	public Assets getAssetsById(Long id) throws Exception {
		Assets assets = assetsDao.getAssetsById(id);
		try {
			if (Objects.nonNull(assets)) {
				return assets;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while assets get by id " + ExceptionUtils.getStackTrace(e));
		}
		return new Assets();

	}

	/* Assets Display to Pending Employee List */
	@Override
	public List<Assets> getEmpAssetsList(String empId) throws Exception {
		List<Assets> assets = null;
		try {
			assets = assetsDao.getEmpAssetsList(empId);
		} catch (Exception e) {
			LOGGER.error("Error occur while display employee assets list " + ExceptionUtils.getStackTrace(e));
		}
		return assets;
	}

	/* Assets Request Accept By Employee */
	@Override
	public Assets acceptAssets(Long id) throws Exception {
		try {
			Assets assets = assetsDao.acceptAssets(id);
			if (Objects.nonNull(assets)) {
				return assets;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while accept assets" + ExceptionUtils.getStackTrace(e));
		}
		return new Assets();
	}

	/* Assets Request Reject By Employee */
	@Override
	public Assets rejectAssets(Long id) throws Exception {
		Assets assets = null;
		try {
			assets = assetsDao.rejectAssets(id);
			if (assets != null) {
				return assets;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while reject assets" + ExceptionUtils.getStackTrace(e));
		}
		return new Assets();
	}

	/* For save/upload Assets Excel File */
	@Override
	public void saveAll(MultipartFile files) throws Exception {
		String empId = null;
		List<Assets> assetsList = new ArrayList<>();
		String tenantId = null;
		UserMst user = null;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			empId = authentication.getName();

			if(Objects.nonNull(empId)) {
				user = userService.getUserById(empId);
				tenantId = user.getTenantId();
				if(Objects.nonNull(tenantId)) {
			String assetsCode = null;

			List<Assets> assetsList1 = assetsDao.getRecordList(ProdConstant.TRUE, tenantId);
			for (Assets assets : assetsList1) {
				assetsCode = assets.getAssetsCode();
			}

			try (XSSFWorkbook workbook = new XSSFWorkbook(files.getInputStream())) {
				// Read data form excel file sheet
				XSSFSheet worksheet = workbook.getSheetAt(0);
				for (int index = 0; index < worksheet.getPhysicalNumberOfRows(); index++) {
					if (index > 0) {
						XSSFRow row = worksheet.getRow(index);

						if (assetsCode != null && !(assetsCode.isEmpty())) {
							if (!checkDuplicateException(row.getCell(2).getStringCellValue())) {
								Assets assets = new Assets();
								assets.setEmpId(row.getCell(0).getStringCellValue());
								assets.setEmpName(row.getCell(1).getStringCellValue());
								assets.setAssetsCode(row.getCell(2).getStringCellValue());
								assets.setAssetsModel(row.getCell(3).getStringCellValue());
								assets.setAssetsMake(row.getCell(4).getStringCellValue());
								assets.setAssetsServiceTag(row.getCell(5).getStringCellValue());
								assets.setCreatedBy(empId);
								assets.setCreatedDate(new Date());
								assets.setIsActive("1");

								assetsList.add(assets);
							} else {
								System.out.println("Duplicate find ::::::::");
							}
						} else {
							System.out.println("fresh records");
							Assets assets = new Assets();
							assets.setEmpId(row.getCell(0).getStringCellValue());
							assets.setEmpName(row.getCell(1).getStringCellValue());
							assets.setAssetsCode(row.getCell(2).getStringCellValue());
							assets.setAssetsModel(row.getCell(3).getStringCellValue());
							assets.setAssetsMake(row.getCell(4).getStringCellValue());
							assets.setAssetsServiceTag(row.getCell(5).getStringCellValue());
							assets.setCreatedBy(empId);
							assets.setCreatedDate(new Date());
							assets.setIsActive("1");

							assetsList.add(assets);
						}
						
						assetsDao.saveAll(assetsList);
					}
				}
			}
				}else {
					System.out.println("TenantId is null ");
				}
		}
		}catch (Exception e) {
			LOGGER.error("Error occur while upload assets excel " + ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public List<Assets> findAll() throws Exception {
		try {
			List<Assets> assets = assetsDao.findAll();
			if (assets != null) {
				return assets;
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while display all assets" + ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<Assets>();
	}

	/* Download Assets Excel Format */
	@Override
	public ByteArrayInputStream exportAssets(List<Assets> assets) throws Exception {
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Assets");

			// Header Bold
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);

			Row row = sheet.createRow(0);

			// Define header cell style
			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);

			// Creating header cells
			Cell cell = row.createCell(0);
			cell.setCellValue("EmpId");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(1);
			cell.setCellValue("EmpName");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(2);
			cell.setCellValue("AssetsCode");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(3);
			cell.setCellValue("AssetsModel");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(4);
			cell.setCellValue("AssetsMake");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(5);
			cell.setCellValue("AssetsServiceTag");
			cell.setCellStyle(headerCellStyle);

			// Creating data rows for each contact
			for (int i = 0; i < assets.size(); i++) {
				Assets rowAssets = assets.get(i);
				if (Objects.nonNull(rowAssets)) {
					Row dataRow = sheet.createRow(i + 1);
					dataRow.createCell(0).setCellValue(rowAssets.getEmpId());
					dataRow.createCell(1).setCellValue(rowAssets.getEmpName());
					dataRow.createCell(2).setCellValue(rowAssets.getAssetsCode());
					dataRow.createCell(3).setCellValue(rowAssets.getAssetsModel());
					dataRow.createCell(4).setCellValue(rowAssets.getAssetsMake());
					dataRow.createCell(5).setCellValue(rowAssets.getAssetsServiceTag());

				}
			}

			// Making size of column auto resize to fit with data
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			return new ByteArrayInputStream(outputStream.toByteArray());
		} catch (Exception e) {
			LOGGER.error("Error while download assets excel format" + ExceptionUtils.getStackTrace(e));
			throw new RuntimeException("Error Occur");
		}
	}

	/* Assets Display to Approved Employee List */
	@Override
	public List<Assets> getEmpApprovedAssetsList(String empId) throws Exception {
		List<Assets> assets = null;
		try {
			assets = assetsDao.getEmpApprovedAssetsList(empId);
		} catch (Exception e) {
			LOGGER.error("Error occur while display employee approved assets list " + ExceptionUtils.getStackTrace(e));
		}
		return assets;
	}

	/* Assets Display to Rejected Employee List */
	@Override
	public List<Assets> getEmpRejectedAssetsList(String empId) throws Exception {
		List<Assets> assets = null;
		try {
			assets = assetsDao.getEmpRejectedAssetsList(empId);
		} catch (Exception e) {
			LOGGER.error("Error occur while display employee rejected assets list " + ExceptionUtils.getStackTrace(e));
		}
		return assets;
	}

	/* For Assets Notification */
	@Override
	public List<Assets> assetsNotification(String userId) throws Exception {
		List<Assets> assets = assetsDao.assetsNotification(userId);
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
		List<Assets> assets = assetsDao.unreadNotification(userId);
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
			assetsDao.assetsNotificationRead(id);
		} catch (Exception e) {
			LOGGER.error("Error occur while assets read notification ... " + ExceptionUtils.getStackTrace(e));

		}
		return new ArrayList<>();
	}

	@Override
	public List<Assets> checkDuplicateAssetsCode(String assetsCode) throws Exception {
		List<Assets> optional = assetsDao.checkDuplicateAssetsCode(assetsCode);
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
			List<Assets> checkDuplicate = checkDuplicateAssetsCode(assetsCode);
			if (Objects.nonNull(checkDuplicate)) {
				return true;
			}

		} catch (Exception e) {
			LOGGER.error("Error occur while find duplicate assets excel" + ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public void assetsEnableAndDisable(Long id) throws Exception {
		try {
			Assets assetsDetails = assetsDao.getAssetsById(id);
			if (Objects.nonNull(assetsDetails)) {
				String checkActiveOrInActiveData = assetsDetails.getIsActive();

				if (checkActiveOrInActiveData.equals(ProdConstant.TRUE)) {
					assetsDetails.setIsActive(ProdConstant.FALSE);
					assetsDao.saveAssets(assetsDetails);
				} else if (checkActiveOrInActiveData.equals(ProdConstant.FALSE)) {
					assetsDetails.setIsActive(ProdConstant.TRUE);
					assetsDao.saveAssets(assetsDetails);
				}
			} else {
				System.out.println("docDetails object is null");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while activate and deactivate assets  " + ExceptionUtils.getStackTrace(e));
		}
	}
}
