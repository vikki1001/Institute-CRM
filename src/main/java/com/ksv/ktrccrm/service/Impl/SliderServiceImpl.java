package com.ksv.ktrccrm.service.Impl;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.SliderDao;
import com.ksv.ktrccrm.db1.entities.Slider;
import com.ksv.ktrccrm.service.SliderService;

@Service
public class SliderServiceImpl implements SliderService {
	private static final Logger LOGGER = LogManager.getLogger(SliderServiceImpl.class);

	@Autowired
	private SliderDao sliderDao;

	@Override
	public List<Slider> getRecordList(String isActive, String tenantId) throws Exception {
		List<Slider> sliderList = null;
		try {
			sliderList = sliderDao.getRecordList(isActive, tenantId);
		} catch (Exception e) {
			LOGGER.error("Error occur while display slider list " + ExceptionUtils.getStackTrace(e));
		}
		return sliderList;
	}

	@Override
	public Slider saveSlider(Slider slider) throws Exception {
		try {
			if (Objects.nonNull(slider)) {
				sliderDao.saveSlider(slider);
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while save slider record " + ExceptionUtils.getStackTrace(e));
		}
		return slider;
	}

	@Override
	public Slider getById(Integer id) throws Exception {
		Slider slider = null;
		try {
			return sliderDao.getById(id);
		} catch (Exception e) {
			LOGGER.error("Error occur while get record by id " + ExceptionUtils.getStackTrace(e));
		}
		return slider;
	}

	public void sliderEnableAndDisable(Integer id) {
		try {
			Slider sliderDetails = sliderDao.getById(id);
			if (Objects.nonNull(sliderDetails)) {
				String checkActiveOrInActiveData = sliderDetails.getIsActive();
				if (checkActiveOrInActiveData.equals(ProdConstant.TRUE)) {
					sliderDetails.setIsActive(ProdConstant.FALSE);
					sliderDao.saveSlider(sliderDetails);
				} else if (checkActiveOrInActiveData.equals(ProdConstant.FALSE)) {
					sliderDetails.setIsActive(ProdConstant.TRUE);
					sliderDao.saveSlider(sliderDetails);
				}
			} else {
				System.out.println("docDetails object is null");
			}
		} catch (Exception e) {
			LOGGER.error("Error occur while activate and deactivate doc  " + ExceptionUtils.getStackTrace(e));
		}

	}
}
