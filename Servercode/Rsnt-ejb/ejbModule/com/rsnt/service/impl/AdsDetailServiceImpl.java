package com.rsnt.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.log.Log;

import com.rsnt.common.exception.RsntException;
import com.rsnt.common.service.impl.RsntBaseService;
import com.rsnt.entity.Ads;
import com.rsnt.entity.AdsImage;
import com.rsnt.service.IAdsDetailService;
import com.rsnt.util.common.Constants;
import com.rsnt.util.common.NativeQueryConstants;

@Stateless(name=IAdsDetailService.JNDI_NAME)
@Name(IAdsDetailService.SEAM_NAME)
@Scope(ScopeType.STATELESS)
public class AdsDetailServiceImpl extends RsntBaseService implements IAdsDetailService{

	@Logger
	private Log log;
	
	@In
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<Object[]> loadAdsData(Long pActive, Long pOrgId)  throws RsntException{
		try{
			return entityManager.createNativeQuery(NativeQueryConstants.GET_ADS_DATA).
			setParameter(1, pOrgId)
			.setParameter(2, pActive).getResultList();
		}
		catch(Exception e){
			log.error("AdsDetailServiceImpl.loadAdsData()", e);
			throw new RsntException(e);
		}
		
	}
	
	
	@SuppressWarnings("unchecked")
	public Ads getAdEntity(long pAdId) throws RsntException {
		try{
		
			return (Ads)entityManager.createNamedQuery(Ads.GET_AD_DETAIL_LIST).setParameter("pAdIds", pAdId).getSingleResult();
			//return entityManager.find(Ads.class, pAdId);
		}
		catch(Exception e){
			log.error("AdsDetailServiceImpl.getAdEntity()", e);
			throw new RsntException(e);
		}
		
	}
	
	public void deleteAdImage(Long adsImageId) throws RsntException{
		try{
			
			AdsImage image = entityManager.find(AdsImage.class, adsImageId);
			entityManager.remove(image);
		}
		catch(Exception e){
			log.error("AdsDetailServiceImpl.deleteAdImage()", e);
			throw new RsntException(e);
		}
	}
	
	
	
	public AdsImage createAdImage(AdsImage image) throws RsntException{
		try{
			
			return entityManager.merge(image);
			
		}
		catch(Exception e){
			log.error("AdsDetailServiceImpl.createAdImage()", e);
			throw new RsntException(e);
		}
	}
	
	public AdsImage getAdImage(Long adImageId) throws RsntException{
		try{
			
			 return entityManager.find(AdsImage.class, adImageId);
			
		}
		catch(Exception e){
			log.error("AdsDetailServiceImpl.getAdImage()", e);
			throw new RsntException(e);
		}
	}
	
	public void clearEM(){
		this.entityManager.flush();
		this.entityManager.clear();
	}
	
	public Integer deleteBannerAdImage(Long pAdId) throws RsntException{
		try{
			
			return entityManager.createNamedQuery(AdsImage.GET_BANNER_AD).setParameter(1, pAdId).executeUpdate();
			
		}
		catch(Exception e){
			log.error("AdsDetailServiceImpl.getBannerAdImage", e);
			throw new RsntException(e);
		}
	}
	
	public Ads updateAdEntity(Ads ad) throws RsntException {
		try{
			if(ad.getAdsId()==null){
				ad.setOrganizationId(Long.valueOf(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString()));
				setCreatedAtttributes(ad);
			}else{
				setModifiedAtttributes(ad);
;			}
			
			for(AdsImage image : ad.getAdsImageList()){
				if(image.getAdsImageId()==null){
					setCreatedAtttributes(image);
				}
				else{
					setModifiedAtttributes(image);
				}
			}
			
			return entityManager.merge(ad);
		}
		catch(Exception e){
			log.error("AdsDetailServiceImpl.updateAdEntity()", e);
			throw new RsntException(e);
		}
	}
	
	public void deactivateAd(Long pAdId) throws RsntException {
		try{
			entityManager.createNativeQuery(NativeQueryConstants.UPDATE_AD_STATUS).setParameter(1, pAdId).executeUpdate();
		}
		catch(Exception e){
			log.error("AdsDetailServiceImpl.deactivateAd()", e);
			throw new RsntException(e);
		}
	}
	
}
