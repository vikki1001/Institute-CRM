package com.ksv.ktrccrm.service;

import java.util.List;

import com.ksv.ktrccrm.db1.entities.Slider;

public interface SliderService {

	public List<Slider> getRecordList(String isActive, String tenantId) throws Exception;
	
	public Slider saveSlider(Slider slider) throws Exception;	

	public Slider getById(Integer id) throws Exception;

	public void sliderEnableAndDisable(Integer id) throws Exception;
}
