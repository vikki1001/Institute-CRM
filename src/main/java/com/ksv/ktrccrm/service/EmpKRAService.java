package com.ksv.ktrccrm.service;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.ksv.ktrccrm.db1.entities.EmpKRA;
import com.ksv.ktrccrm.db1.entities.EmpWorkDetails;

public interface EmpKRAService {

	public List<EmpWorkDetails> getEmpWithManger(String empId) throws Exception;

	public List<EmpKRA> getCurrentEmpAppraisal(String empId) throws Exception;

	public Optional<EmpKRA> findByempId(String empId) throws Exception;

	public EmpKRA saveTagKra(EmpKRA empKRA) throws Exception;

	public EmpKRA updateTagKra(EmpKRA empKRA) throws Exception;

	public EmpKRA getById(Long id) throws Exception;

	public EmpKRA getKRAWithDepIdAndEmpId(String depId, String empId) throws Exception;

	public EmpKRA getManagerIdWithMangerIdWithDepId(String reportingManager, String depId, String empId)
			throws Exception;

	public EmpKRA saveDraftSelfAppraisal(EmpKRA empKRA) throws Exception;

	public EmpKRA saveSelfAppraisal(EmpKRA empKRA) throws Exception;

	public EmpKRA saveRatingByReportingManager(EmpKRA empKRA) throws Exception;

	public EmpKRA saveDraftRatingByReportingManager(EmpKRA empKRA) throws Exception;

	public EmpKRA saveRatingByReportingManager2(EmpKRA empKRA) throws Exception;

	public EmpKRA saveDraftRatingByReportingManager2(EmpKRA empKRA) throws Exception;

	public List<EmpKRA> getTeammetsTeamMangerId(String empId, String depId) throws Exception;

	public List<EmpKRA> getEmpWithMangerIdOrReportingManager(String empId, String depId, List<String> empIds)
			throws Exception;

	public List<String> getEmpWithManger3(String empId) throws Exception;

	public EmpWorkDetails getEmpWithManger1(String empId, List<String> empId2) throws Exception;

	public List<EmpKRA> findByEmpIdAndDepId(String empId, String depId);

	public List<EmpKRA> appraisalCycleList(String empId) throws Exception;

	public EmpKRA getKRAWithDepIdAndEmpIdAndDate(String depId, String empId, Date date) throws Exception;

//	public Optional<EmpKRA> duplicateEmpKRACheck(String userId ,String kraI) throws Exception;
//
//	public boolean empKRAExists(String userId ,String kraI) throws Exception;

	public List<String> getTeammetsTeamMangerId2(String empId, String depId) throws Exception;

}