package com.rsnt.web.action;

import java.io.IOException;

import javax.faces.context.FacesContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("analyticsAction")
@Scope(ScopeType.PAGE)
public class AnalyticsAction {
	
	private String startDate;
	private String endDate;
	private String reportGeneratorURL = "http://localhost:8181/birt.war/frameset?__report=AlertReport.rptdesign&&__format=xls";
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * Initialize the default Data set for Analytics
	 */
	public void initialize() {
		System.out.println("IN Action");
		
	}

	public void dummyMethod(){
		System.out.println("Dummy Method called");
	}
	public void generateExcelReport(){
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(reportGeneratorURL);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
