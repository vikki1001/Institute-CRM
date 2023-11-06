package com.ksv.ktrccrm.dao.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.dao.SliderDao;
import com.ksv.ktrccrm.db1.entities.EmpBasicDetails;
import com.ksv.ktrccrm.db1.entities.Slider;
import com.ksv.ktrccrm.db1.repository.EmpBasicRepository;
import com.ksv.ktrccrm.db1.repository.SliderRepository;

@Repository
public class SliderDaoImpl implements SliderDao {
	private static final Logger LOGGER = LogManager.getLogger(SliderDaoImpl.class);

	@Autowired
	private SliderRepository sliderRepository;
	@Lazy
	@Autowired
	private CheckInCheckOutDaoImpl checkOutDaoImpl;
	@Autowired
	private EmpBasicRepository empBasicRepository;
	
	@Override
	public List<Slider> getRecordList(String isActive, String tenantId) throws Exception {
		List<Slider> sliderList = null;
		try {
			sliderList = sliderRepository.getRecordList(isActive, tenantId);
		} catch (Exception e) {
			LOGGER.error("Error occur while display slider list " + ExceptionUtils.getStackTrace(e));
		}
		return sliderList;
	}
	@Override
	public Slider saveSlider(Slider slider) throws Exception {
		String loginId = null;
		try {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		loginId = authentication.getName();

		if (Objects.nonNull(loginId)) {
			EmpBasicDetails userDetails = empBasicRepository.getCurrentUser(loginId,ProdConstant.TRUE);
			System.out.println("TenantId:::::::::"+ userDetails.getTenantId());
			String tenantId = userDetails.getTenantId();
			if(Objects.nonNull(tenantId)) {
			if (Objects.isNull(slider.getId())) {
					slider.setIsActive(ProdConstant.TRUE);
					slider.setDate(checkOutDaoImpl.getDateTime());
					slider.setCreatedDate(checkOutDaoImpl.getDateTime());
					slider.setCreatedBy(loginId);
					slider.setTenantId(tenantId);
					sliderRepository.save(slider);
				} else {
					Optional<Slider> optional = sliderRepository.findById(slider.getId());
					if (optional.isPresent()) {
						Slider newSlider = optional.get();
						newSlider.setImage(slider.getImage());
						newSlider.setImageName(slider.getImageName());
						newSlider.setImageDescription(slider.getImageDescription());
						newSlider.setDate(checkOutDaoImpl.getDateTime());
						newSlider.setLastModifiedBy(loginId);
						newSlider.setLastModifiedDate(checkOutDaoImpl.getDateTime());

						sliderRepository.save(newSlider);
					} else {
						return slider;
					}
				}
		}else {
			System.out.println("tenantId is null ");
		}
		}else {
			System.out.println("loginId is null ");
		}
		
		} catch (Exception e) {
			LOGGER.error("Error occur while save & update slider record " + ExceptionUtils.getStackTrace(e));
		}
		return slider;
	}
	
	@Override
	public Slider getById(Integer id) throws Exception {
		Slider slider = null;
		try {
			slider = sliderRepository.getById(id);
		} catch (Exception e) {
			LOGGER.error("Error occur while get record by id " + ExceptionUtils.getStackTrace(e));
		}
		return slider;
	}
}
