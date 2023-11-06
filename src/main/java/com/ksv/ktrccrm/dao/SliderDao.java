package com.ksv.ktrccrm.dao;

import java.util.List;

import com.ksv.ktrccrm.db1.entities.Slider;

public interface SliderDao {

	public List<Slider> getRecordList(String isActive, String tenantId) throws Exception;

	public Slider saveSlider(Slider slider) throws Exception;

	public Slider getById(Integer id) throws Exception;
}