package com.rsnt.service;

import java.util.List;

import javax.ejb.Local;

import com.rsnt.common.exception.RsntException;

@Local
public interface IAnalyticsService {
	public String SEAM_NAME = "analyticsService";
	public String JNDI_NAME="analyticsServiceImpl";
	
	public List<Object[]> getAlertReportData(String fromDate, String toDate)  throws RsntException;
}
