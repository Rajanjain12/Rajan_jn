package com.rsnt.service;

import java.util.List;

import javax.ejb.Local;

import com.rsnt.common.exception.RsntException;
import com.rsnt.entity.Ads;
import com.rsnt.entity.AdsImage;

@Local
public interface IAdsDetailService {
	public String SEAM_NAME = "adsService";
	public String JNDI_NAME="AdsDetailServiceImpl";
	
	
	public List<Object[]> loadAdsData(Long pActive, Long pOrgId)  throws RsntException;
	public Ads getAdEntity(long pAdId) throws RsntException ;
	public Ads updateAdEntity(Ads ad) throws RsntException ;
	public void deleteAdImage(Long adsImageId) throws RsntException;
	public AdsImage createAdImage(AdsImage image) throws RsntException;
	public void deactivateAd(Long pAdId) throws RsntException;
	public Integer deleteBannerAdImage(Long pAdId) throws RsntException;
	public AdsImage getAdImage(Long adImageId) throws RsntException;
	public void clearEM();
	
}