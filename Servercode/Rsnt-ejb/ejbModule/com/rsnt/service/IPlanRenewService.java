package com.rsnt.service;


public interface IPlanRenewService {

	public String SEAM_NAME = "planRenewService";
	public String JNDI_NAME="PlanRenewServiceImpl";
	
	public void runRemoteScheduler();
}
