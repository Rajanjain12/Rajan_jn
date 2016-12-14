package com.rsnt.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import com.rsnt.common.exception.RsntException;
import com.rsnt.entity.Ads;
import com.rsnt.entity.AdsCreditAnalytics;
import com.rsnt.entity.Feedback;
import com.rsnt.entity.FeedbackQuestionaireResponse;
import com.rsnt.entity.FeedbackQuestionaireResponseDetail;
import com.rsnt.entity.OrganizationLayoutMarkerDeviceMap;
import com.rsnt.service.IClientAlertService;
import com.rsnt.util.common.CommonUtil;
import com.rsnt.util.common.NativeQueryConstants;
import com.rsnt.util.transferobject.AdsDataTO;
import com.rsnt.util.transferobject.CustomerDataTO;
import com.rsnt.util.transferobject.FeedbackTO;
import com.rsnt.util.transferobject.FeedbackTOList;

@Stateless(name=IClientAlertService.JNDI_NAME)
@Scope(ScopeType.STATELESS)
@Name(IClientAlertService.SEAM_NAME)
public class ClientAlertServiceImpl implements IClientAlertService{

	@In
	private EntityManager entityManager;
	
	@Logger
	private Log log;


	public void processOrgMarkerDashBoardAlert(Long pOrgMarkerId, Long pAlertId, String deviceId, String deviceMake) throws RsntException{
		
		try{
			int test = entityManager.createNativeQuery(NativeQueryConstants.INSERT_LAYOUT_ALERT_OPTION_REQUEST)
				.setParameter(1, pOrgMarkerId)
				.setParameter(2, pAlertId)
				.setParameter(3, deviceId)
				.setParameter(4, deviceMake)
				.executeUpdate();
			log.info("Insert Return "+test);
			if(test==0){
				throw new RsntException("Invalid Request For Option/Alert");
			}
		}
		catch(RsntException e){
			throw new RsntException(e);
		}
		catch(Exception e){
			log.error("ClientAlertServiceImpl.processOrgMarkerDashBoardAlert()", e);
			throw new RsntException("Unable to processAlertRequest");
		}
	}
	
	public void processDashBoardAlertRequest(Long pOrgMarkerId, Long pAlertId, Long pWaiterId) throws RsntException{
			
		try{
			int test = entityManager.createNativeQuery(NativeQueryConstants.UPDATE_DASHBOARD_PROCESS_ALERT_REQUEST)
				.setParameter(1, pOrgMarkerId)
				.setParameter(2, pAlertId)
				.setParameter(3, pWaiterId).
				executeUpdate();
			log.info("Insert Return "+test);
			if(test==0){
				throw new RsntException("Unable to Process Option/Alert Request");
			}
		}
		catch(RsntException e){
			throw new RsntException(e);
		}
		catch(Exception e){
			log.error("ClientAlertServiceImpl.processOrgMarkerDashBoardAlert()", e);
			throw new RsntException("Unable to Process Option/Alert Request");
		}
	}


	public void checkDuplicateDashBoardAlert(Long pOrgMarkerId, Long pAlertId, String deviceId) throws RsntException{
		
		try{
			BigInteger test = (BigInteger)entityManager.createNativeQuery(NativeQueryConstants.GET_DASHBOARD_UNPROCESSED_ALERT)
				.setParameter(1, pOrgMarkerId)
				.setParameter(2, pAlertId)
				.setParameter(3, deviceId)
				.getSingleResult();
			log.info("Insert Return "+test);
			if(test.intValue()!=0){
				//throw new RsntException("Duplicate Option/Alert");
				throw new RsntException("Request already sent.");

				
			}
		}
		catch(RsntException e){
			throw new RsntException(e);
		}
		catch(Exception e){
			log.error("ClientAlertServiceImpl.checkDuplicateDashBoardAlert()", e);
			throw new RsntException("ClientAlertServiceImpl.checkDuplicateDashBoardAlert()", e);
		}
	}
	
	public boolean checkActiveMarkerPresent(Long orgMarkerId)
	{
		BigInteger test = (BigInteger)entityManager.createNativeQuery(NativeQueryConstants.GET_DASHBOARD_ACTIVE_ALERT)
				.setParameter(1, orgMarkerId).getSingleResult();
		if(test.intValue()>0){
			return true;
		}
		return false;
	}
	
	public void registerDevice(OrganizationLayoutMarkerDeviceMap layoutMarkerDeviceMap)throws RsntException{
		try{
			entityManager.persist(layoutMarkerDeviceMap);
		}
		catch(Exception e){
			log.error("Exception in saving OrganizationLayoutMarkerDeviceMap" +e.getMessage());
			throw new RsntException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDeviceDetail(Long pOrgLayoutMarkerId,Long pAlertId)throws RsntException{
		try{
			return entityManager.createNativeQuery(NativeQueryConstants.GET_DEVICE_DETAIL).
				setParameter(1, pOrgLayoutMarkerId).setParameter(2, pAlertId).getResultList();
		}
		catch(Exception e){
			log.error("Exception in saving OrganizationLayoutMarkerDeviceMap" +e.getMessage());
			throw new RsntException(e);
		}
	}

	
	public List<Object[]> getOrgCords(Long pOrgMarkerId)throws RsntException{
		
		try{
			return  entityManager.createNativeQuery(NativeQueryConstants.GET_ORG_CORDS)
				.setParameter(1, pOrgMarkerId).getResultList();
		}
		catch(Exception e){
			log.error("ClientAlertServiceImpl.getOrgCords()", e);
			throw new RsntException("ClientAlertServiceImpl.getOrgCords()", e);
		}
	}
	
	public List<Object[]> getMarkerAlerts(Long pOrgMarkerId)throws RsntException{
		
		try{
			return  entityManager.createNativeQuery(NativeQueryConstants.GET_DASHBOARD_OPTION_DATA)
				.setParameter(1, pOrgMarkerId).getResultList();
		}
		catch(Exception e){
			log.error("ClientAlertServiceImpl.getMarkerAlerts()", e);
			throw new RsntException("ClientAlertServiceImpl.getMarkerAlerts()", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<AdsDataTO> getSpecialAdList(Long pOrgMarkerId, int dayOfWeek,String date, String time) throws RsntException {

		try {

			Session session = (Session) entityManager.getDelegate();
			SQLQuery query = session .createSQLQuery(NativeQueryConstants.GET_ORG_SPECIAL_AD_LIST)
					.addScalar("adsId", Hibernate.STRING)
					.addScalar("title", Hibernate.STRING)
					.addScalar("imageUrl", Hibernate.STRING);
					//.addScalar("title", Hibernate.STRING)
					//.addScalar("price", Hibernate.STRING);
			query.setParameter("1", pOrgMarkerId);
			query.setParameter("2", "%" + dayOfWeek + "%");
			query.setParameter("3", date);
			query.setParameter("4", time);
			
			query.setResultTransformer(Transformers
					.aliasToBean(AdsDataTO.class));
			List<AdsDataTO> results = query.list();

			/*List<BigInteger> adsIdList = entityManager
					.createNativeQuery(
							NativeQueryConstants.GET_ORG_SPECIAL_AD_LIST)
					.setParameter(1, pOrgMarkerId)
					.setParameter(2, "%" + dayOfWeek + "%")
					.setParameter(3, date).setParameter(4, date)
					.getResultList();*/

			return results;
		} catch (Exception e) {
			log.error("ClientAlertServiceImpl.getSpecialAdList()", e);
			throw new RsntException(
					"ClientAlertServiceImpl.getSpecialAdList()", e);
		}
	}
	
	public Ads getSpecialAdDetail(Long pAdId) throws RsntException{
		try{
			return entityManager.find(Ads.class, pAdId);
		}
		catch(Exception e){
			log.error("ClientAlertServiceImpl.getSpecialAdDetail()", e);
			throw new RsntException(
					"ClientAlertServiceImpl.getSpecialAdDetail()", e);
		}
	}
	
	public List<Ads> getAdDetails(List<Long> pAdListId) throws RsntException{
		try{
			return entityManager.createNamedQuery(Ads.GET_AD_DETAIL_LIST).setParameter("pAdIds", pAdListId).getResultList();
		}
		catch(Exception e){
			log.error("ClientAlertServiceImpl.getAdDetails()", e);
			throw new RsntException(
					"ClientAlertServiceImpl.getAdDetails()", e);
		}
	}
	
	public void saveAdAnalytics(Long orgLayoutMarkerId, Long adId, Long pOrgId) throws RsntException{
		try{
			AdsCreditAnalytics adsCreditAnalytics = new AdsCreditAnalytics();
			adsCreditAnalytics.setAdsID(adId);
			adsCreditAnalytics.setOrganizationLayoutMarkerId(orgLayoutMarkerId);
			adsCreditAnalytics.setOrganizationId(pOrgId);
			adsCreditAnalytics.setViewDate(new Date());
			
			entityManager.persist(adsCreditAnalytics);
			
		}
		catch(Exception e){
			log.error("AdsDetailServiceImpl.saveAdAnalytics()", e);
			throw new RsntException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> loadFeedbackMobileData(Long pOrgLayoutMarkerId)  throws RsntException{
		try{
			return entityManager.createNativeQuery(NativeQueryConstants.GET_FEEDBACK_MOBILE_DATA).
				setParameter(1, pOrgLayoutMarkerId).getResultList();
		}
		catch(Exception e){
			log.error("FeedbackServiceImpl.loadFeedbackMobileData()", e);
			throw new RsntException(e);
		}
	}
	

	@SuppressWarnings("unchecked")
	public List<Object[]> loadFeedbackData(Long pOrgLayoutMarkerId)  throws RsntException{
		try{
			return entityManager.createNativeQuery(NativeQueryConstants.GET_FEEDBACK_DATA).
				setParameter(1, pOrgLayoutMarkerId).getResultList();
		}
		catch(Exception e){
			log.error("ClientAlertServiceImpl.loadFeedbackData()", e);
			throw new RsntException(e);
		}
		
	}
	
	public void saveFeedbackCustomerDetail(String cusData, String qtnDetailData,Long orgLayoutMarkerId)throws RsntException{
		
		try{
    		if(cusData.startsWith("{"))
        	{
        		StringBuffer newData = new StringBuffer(cusData);
        		newData.append("]");
        		newData.insert(0, "[");
        		cusData = newData.toString();
        	}
    		if(qtnDetailData!=null && qtnDetailData.startsWith("{"))
        	{
        		StringBuffer qtnDetailDataNew = new StringBuffer(qtnDetailData);
        		qtnDetailDataNew.append("]");
        		qtnDetailDataNew.insert(0, "[");
        		qtnDetailData = qtnDetailDataNew.toString();
        	}
    		
    		ObjectMapper mapper = new ObjectMapper(); 
       		TypeReference<List<HashMap<String, String>>> typeRef = new TypeReference<List<HashMap<String,String> >>() {}; 
       		List<HashMap<String, String>> updateCustDataList = mapper.readValue(cusData, typeRef);
       		
       		List<Object[]> fdbackQtnrDataArr = entityManager.createNativeQuery(NativeQueryConstants.GET_FEEDBACK_QSTNR_ID)
       					.setParameter(1, orgLayoutMarkerId).getResultList();
       		Object[] fdbackQtnrData = fdbackQtnrDataArr.get(0);
       		HashMap<String, String> testMap = updateCustDataList.get(0);
   			Feedback feedback = new Feedback();
   			feedback.setFirstName(testMap.get("firstName"));
   			feedback.setEmail(testMap.get("email"));
   			if(testMap.get("contactNumber")!="")feedback.setContactNumber(new Long(testMap.get("contactNumber")));
   			feedback.setOrganizationId(new BigDecimal(fdbackQtnrData[1].toString()).longValue());
   			feedback.setFeedbackNote(testMap.get("feedbackNote"));
   			feedback.setFeedbackRating(new BigDecimal(testMap.get("feedbackRating")));
   			feedback.setOptIn(new Boolean(testMap.get("optIn")));
   			feedback.setOrganizationLayoutMarkerId(orgLayoutMarkerId);
       			
   			
   			
   			if(qtnDetailData!=null && !qtnDetailData.equalsIgnoreCase("")&& qtnDetailData!=""){
   				List<HashMap<String, String>> qtnDetailDataList = mapper.readValue(qtnDetailData, typeRef);
   				
   				if(qtnDetailDataList.size()>0){
   					
   					FeedbackQuestionaireResponse feedbackQuestionaireResponse = new FeedbackQuestionaireResponse();
   		   			feedbackQuestionaireResponse.setFeedback(feedback);
   		   			feedbackQuestionaireResponse.setFeedbackQuestionaireId(new BigDecimal(fdbackQtnrData[0].toString()).longValue());
   		   			
   					for(HashMap<String, String> qtnDetailDataMap : qtnDetailDataList){
   	   	   				FeedbackQuestionaireResponseDetail feedbackQuestionaireResponseDetail = new FeedbackQuestionaireResponseDetail();
   	   	   				feedbackQuestionaireResponseDetail.setFeedbackQuestionaireResponse(feedbackQuestionaireResponse);
   	   	   				feedbackQuestionaireResponseDetail.setQuestionText(qtnDetailDataMap.get("questionText"));
   	   	   				feedbackQuestionaireResponseDetail.setReplyText(qtnDetailDataMap.get("answerVal"));
   	   	   				feedbackQuestionaireResponseDetail.setOptionText(qtnDetailDataMap.get("answerVal"));
   	   	   				feedbackQuestionaireResponseDetail.setQuestionTypeId(new Long(1));
   	   	   				feedbackQuestionaireResponse.getFeedbackQuestionaireResponseDetailList().add(feedbackQuestionaireResponseDetail);
   	   	   				
   	   	   			}
   					
   					feedback.setFeedbackQuestionaireResponse(feedbackQuestionaireResponse);
   					
   				}
   	   			
   			}
       		entityManager.persist(feedback);
       		
		}
		catch(Exception e){
			log.error("FeedbackServiceImpl.saveFeedbackDetail()", e);
			throw new RsntException("FeedbackServiceImpl.saveFeedbackDetail()", e);
   		}
		
	}
	
	public void saveFeedbackCustomerDetailV2(String data,Long orgLayoutMarkerId)throws RsntException{
		
		try{
    		/*if(data.startsWith("{"))
        	{
        		StringBuffer newData = new StringBuffer(data);
        		newData.append("]");
        		newData.insert(0, "[");
        		data = newData.toString();
        	}
    		*/
    		
			ObjectMapper mapper = new ObjectMapper(); 
			FeedbackTOList feedbackList = mapper.readValue(data,FeedbackTOList.class);

			List<Object[]> fdbackQtnrDataArr = entityManager.createNativeQuery(NativeQueryConstants.GET_FEEDBACK_QSTNR_ID)
				.setParameter(1, orgLayoutMarkerId).getResultList();
			Object[] fdbackQtnrData = fdbackQtnrDataArr.get(0);
    		//Object[] fdbackQtnrData = fdbackQtnrDataArr.get(0);
       		//HashMap<String, Object> testMap = updateCustDataList.get(0);
			CustomerDataTO custDataTO = feedbackList.getCustomerData();
   			Feedback feedback = new Feedback();
   			feedback.setFirstName(custDataTO.getFirstName());
   			feedback.setEmail(custDataTO.getEmail());
   			if(!CommonUtil.isNullOrEmpty(custDataTO.getContactNumber()))feedback.setContactNumber(new Long(custDataTO.getContactNumber()));
   			feedback.setOrganizationId(new BigDecimal(fdbackQtnrData[1].toString()).longValue());
   			feedback.setFeedbackNote(custDataTO.getFeedbackNote());
   			feedback.setFeedbackRating(new BigDecimal(custDataTO.getFeedbackRating()));
   			feedback.setOptIn(new Boolean(custDataTO.getOptIn()));
   			feedback.setOrganizationLayoutMarkerId(orgLayoutMarkerId);
       			
   			
   			
   			if(feedbackList.getFeedbackList()!=null && feedbackList.getFeedbackList().size()>0){
				
				FeedbackQuestionaireResponse feedbackQuestionaireResponse = new FeedbackQuestionaireResponse();
	   			feedbackQuestionaireResponse.setFeedback(feedback);
	   			feedbackQuestionaireResponse.setFeedbackQuestionaireId(new BigDecimal(fdbackQtnrData[0].toString()).longValue());
	   			
				for(FeedbackTO feedbackTO: feedbackList.getFeedbackList()){
   	   				FeedbackQuestionaireResponseDetail feedbackQuestionaireResponseDetail = new FeedbackQuestionaireResponseDetail();
   	   				feedbackQuestionaireResponseDetail.setFeedbackQuestionaireResponse(feedbackQuestionaireResponse);
   	   				feedbackQuestionaireResponseDetail.setQuestionText(feedbackTO.getQuestionText());
   	   				feedbackQuestionaireResponseDetail.setReplyText(feedbackTO.getAnswerVal());
   	   				feedbackQuestionaireResponseDetail.setOptionText(feedbackTO.getAnswerVal());
   	   				feedbackQuestionaireResponseDetail.setQuestionTypeId(new Long(1));
   	   				feedbackQuestionaireResponse.getFeedbackQuestionaireResponseDetailList().add(feedbackQuestionaireResponseDetail);
   	   				
   	   			}
				
				feedback.setFeedbackQuestionaireResponse(feedbackQuestionaireResponse);
   	   			
   			}
       		entityManager.persist(feedback);
       		
		}
		catch(Exception e){
			log.error("FeedbackServiceImpl.saveFeedbackDetail()", e);
			throw new RsntException("FeedbackServiceImpl.saveFeedbackDetail()", e);
   		}
		
	}
	/* realtime pubish api for dashboard*/
	public List<Object[]> getDashBoardAlertData(Long pOrgLayoutId) throws RsntException{
		
		try{
			return entityManager.createNativeQuery(NativeQueryConstants.GET_DASHBOARD_ALERT_DATA).setParameter(1, pOrgLayoutId).getResultList();
		}
		catch(Exception e){
			log.error("LayoutServiceImpl.getDashBoardAlertData()", e);
			throw new RsntException("LayoutServiceImpl.getDashBoardAlertData()", e);
		}
	
	}
}
