package com.rsnt.service;

public interface IWaitListScheduler {
	public String SEAM_NAME = "waitListSchedular";
	public String JNDI_NAME="waitListResetImpl";
	
	public void runRemoteScheduler();
	
}
