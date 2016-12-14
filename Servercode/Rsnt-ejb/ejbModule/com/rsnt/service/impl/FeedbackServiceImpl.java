package com.rsnt.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.log.Log;

import com.rsnt.common.exception.RsntException;
import com.rsnt.common.service.impl.RsntBaseService;
import com.rsnt.entity.Feedback;
import com.rsnt.entity.FeedbackQuestionaire;
import com.rsnt.entity.FeedbackQuestionaireDetail;
import com.rsnt.entity.FeedbackQuestionaireResponse;
import com.rsnt.entity.FeedbackQuestionaireResponseDetail;
import com.rsnt.service.IFeedbackService;
import com.rsnt.util.common.Constants;
import com.rsnt.util.common.NativeQueryConstants;

@Stateless(name=IFeedbackService.JNDI_NAME)
@Scope(ScopeType.STATELESS)
@Name(value=IFeedbackService.SEAM_NAME)
public class FeedbackServiceImpl extends RsntBaseService implements IFeedbackService  {


	@Logger
	private Log log;
	
	@In
	private EntityManager entityManager;
	
	
	
	@SuppressWarnings("unchecked")
	public List<Object[]> loadFeedbackDetail(Long pOrgId)  throws RsntException{
		try{
			return entityManager.createNativeQuery(NativeQueryConstants.GET_FEEDBACK_DETAIL).
				setParameter(1, pOrgId).getResultList();
		}
		catch(Exception e){
			log.error("FeedbackServiceImpl.loadFeedbackDetail()", e);
			throw new RsntException(e);
		}
		
	}
	
	/*public void updateFeedbackEntity(FeedbackQuestionaire feedbackQuestionaire) throws RsntException {
		try{
			if(feedbackQuestionaire.getFeedbackQuestionaireId()==null){
				feedbackQuestionaire.setActive(true);
				feedbackQuestionaire.setOrganizationId(new Long(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString()));
				setCreatedAtttributes(feedbackQuestionaire);
			}
			else{
				setModifiedAtttributes(feedbackQuestionaire);
			}
			entityManager.merge(feedbackQuestionaire);
		}
		catch(Exception e){
			log.error("FeedbackServiceImpl.updateFeedbackEntity()", e);
			throw new RsntException(e);
		}
	}*/
	
	public void updateFeedbackDetailEntity(FeedbackQuestionaireDetail feedbackQuestionaireDetail) throws RsntException {
		try{
			if(feedbackQuestionaireDetail.getFeedbackQuestionaireDetailId()==null){
				setCreatedAtttributes(feedbackQuestionaireDetail);
				feedbackQuestionaireDetail.setFeedbackQuestionaireId(getFeedbackQuestionaireId(
						new Long(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString())));
			}
			else{
				setModifiedAtttributes(feedbackQuestionaireDetail);
			}
			
			entityManager.merge(feedbackQuestionaireDetail);
		}
		catch(Exception e){
			log.error("FeedbackServiceImpl.updateFeedbackEntity()", e);
			throw new RsntException(e);
		}
	}
	
	public void deleteFeedbackDetail(List<Long> feedbackIds) throws RsntException {
		try{
				entityManager.createNativeQuery(NativeQueryConstants.DELETE_FEEDBACK_DETAIL).setParameter(1, feedbackIds).executeUpdate();
			
		}
		catch(Exception e){
			log.error("FeedbackServiceImpl.deleteFeedbackDetail()", e);
			throw new RsntException(e);
		}
	}
	

	@SuppressWarnings("unchecked")
	public FeedbackQuestionaire getFeedbackEntity(long pFeedbackId) throws RsntException {
		try{
		
			return (FeedbackQuestionaire)entityManager.createNamedQuery(FeedbackQuestionaire.GET_FEEDBACK).setParameter(1, pFeedbackId).getSingleResult();
		}
		catch(Exception e){
			log.error("FeedbackServiceImpl.getFeedbackEntity()", e);
			throw new RsntException(e);
		}
		
	}
	
	public FeedbackQuestionaireDetail getFeedbackDetailEntity(long pFeedbackDetailId) throws RsntException{
		try{
			
			return (FeedbackQuestionaireDetail)entityManager.createNamedQuery(FeedbackQuestionaireDetail.GET_FEEDBACK_DETAIL).setParameter(1, pFeedbackDetailId).getSingleResult();
		}
		catch(Exception e){
			log.error("FeedbackServiceImpl.getFeedbackEntity()", e);
			throw new RsntException(e);
		}
	}
	
	public Long getFeedbackQuestionaireId(Long pOrgId) throws RsntException{
		try{
			
			return ((Integer)entityManager.createNativeQuery(NativeQueryConstants.GET_FEEDBACK_QUESTIONAIRE_ID).setParameter(1, pOrgId).getSingleResult()).longValue();
		}
		catch(Exception e){
			log.error("FeedbackServiceImpl.getFeedbackEntity()", e);
			throw new RsntException(e);
		}
	}
	
	
	
	public Long getOrganizationFeedbackType() throws RsntException{
		try{
			
			return ((Integer)entityManager.createNativeQuery(NativeQueryConstants.GET_ORGANIZATION_FEEDBACK_LOOKUP).
					setParameter(1, Contexts.getSessionContext().get(Constants.CONST_ORGID).toString()).getSingleResult()).longValue();
		}
		catch(Exception e){
			log.error("FeedbackServiceImpl.getOrganizationFeedbackType()", e);
			throw new RsntException(e);
		}
	}
	
	public void deactivateFeedback(Long pFdbkId) throws RsntException {
		try{
			entityManager.createNativeQuery(NativeQueryConstants.UPDATE_FEEDBACK_DETAIL_STATUS).setParameter(1, pFdbkId).executeUpdate();
		}
		catch(Exception e){
			log.error("FeedbackServiceImpl.deactivateFeedback()", e);
			throw new RsntException(e);
		}
	}
	

}
