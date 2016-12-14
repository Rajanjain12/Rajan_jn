package com.rsnt.service;

import java.util.List;

import javax.ejb.Local;

import com.rsnt.common.exception.RsntException;
import com.rsnt.entity.FeedbackQuestionaire;
import com.rsnt.entity.FeedbackQuestionaireDetail;

@Local
public interface IFeedbackService {
	public String SEAM_NAME = "feedbackService";
	public String JNDI_NAME="FeedbackServiceImpl";
	
	
	public FeedbackQuestionaire getFeedbackEntity(long pFeedbackId) throws RsntException;
	public FeedbackQuestionaireDetail getFeedbackDetailEntity(long pFeedbackDetailId) throws RsntException;
	public List<Object[]> loadFeedbackDetail(Long pOrgId)  throws RsntException;
	//public void updateFeedbackEntity(FeedbackQuestionaire feedbackQuestionaire) throws RsntException;
	public void updateFeedbackDetailEntity(FeedbackQuestionaireDetail feedbackQuestionaireDetail) throws RsntException;
	public void deleteFeedbackDetail(List<Long> feedbackIds) throws RsntException;
	public Long getFeedbackQuestionaireId(Long pOrgId) throws RsntException;
	public Long getOrganizationFeedbackType() throws RsntException;
	public void deactivateFeedback(Long pFdbkId) throws RsntException;
	
}
