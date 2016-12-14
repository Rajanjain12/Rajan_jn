package com.rsnt.web.action;

import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import com.rsnt.common.exception.RsntException;
import com.rsnt.common.service.ILookupService;
import com.rsnt.entity.FeedbackQuestionaireDetail;
import com.rsnt.service.IFeedbackService;
import com.rsnt.util.common.CommonUtil;
import com.rsnt.util.common.Constants;

@Name("feedbackAction")
@Scope(ScopeType.PAGE)
public class FeedbackAction {
	
	@In(value=IFeedbackService.SEAM_NAME, create=true)
	private IFeedbackService feedbackService;

	@In(value=ILookupService.SEAM_NAME,create=true)
	private ILookupService lookupService;
	
	private Long selectedFeedbackDetailId;
	
	//private FeedbackQuestionaire selectedFeedbackQuestionaire;
	
	private FeedbackQuestionaireDetail selectedFeedbackQuestionaireDetail;
	
	@Logger
	private Log  log;
	
	private String deleteFeedbackIds;
	
	@In
	private Map messages;
	
	//private List<SelectItem> feedbackAnswerTypeList = new ArrayList<SelectItem>();
	
	private Long selectedFeedbackAnswerType;
	
	private boolean renderOptionText;
	
	private boolean advancedFeedback;
	
	private String optionTextVal;
	
	//private String selectedOptionTypeVal;
	
	public String navigateToHome(){
		initialize();
		return "success";
	}
	public void initialize(){
		try{
			
			setAdvancedFeedback(false);
			
			Long feedbackType= feedbackService.getOrganizationFeedbackType();
			if(feedbackType.intValue()== Constants.CONT_LOOKUP_FEEDBACKTYPE_ADVANCED){
				setAdvancedFeedback(true);
			}
			
			//feedbackAnswerTypeList = new ArrayList<SelectItem>();
			//List<Object[]> feedbackAnswerObjArr = lookupService.getLookupsForLookupType(new Long(Constants.CONT_LOOKUPTYPE_FEEDBACKQUESTIONTYPE));
			
			/*for(Object[] obj : feedbackAnswerObjArr ){
				BigDecimal feedbackAnswerTypeId = new BigDecimal(obj[0].toString());
				feedbackAnswerTypeList.add(new SelectItem(feedbackAnswerTypeId.longValue(),obj[1].toString()));
			}*/
			
		}
		catch(RsntException e){
			log.error("initialize()", e);
			
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
	                .get("save.plan.failed").toString(), null);
	        FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	/*public void addNewFeedback(){
		selectedFeedbackQuestionaire = new FeedbackQuestionaire();
		
	}*/
	
	public void addNewFeedbackDetail(){
		initialize();
		selectedFeedbackQuestionaireDetail = new FeedbackQuestionaireDetail();
		setSelectedFeedbackAnswerType(new Long(Constants.CONT_LOOKUP_FEEDBACKANSWERTYPEOPTION_COMMENT));
		selectedFeedbackQuestionaireDetail.setActive(true);
		setRenderOptionText(false);
	}
	
	public void renderOptionTypeData(){
		if((this.getSelectedFeedbackAnswerType().intValue() == Constants.CONT_LOOKUP_FEEDBACKANSWERTYPEOPTION_SINGLE_OPTION) || 
				(this.getSelectedFeedbackAnswerType().intValue() == Constants.CONT_LOOKUP_FEEDBACKANSWERTYPEOPTION_MULTI_OPTION))
			setRenderOptionText(true);
		else
			setRenderOptionText(false);
	}
	
	public String navigateToFeedbackDetail(){
		return "success";
	}
	
	public void deactivateFeedback(){
		try{
			if(this.getSelectedFeedbackDetailId()!=null){
				feedbackService.deactivateFeedback(this.getSelectedFeedbackDetailId());
				final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, messages
	                    .get("feedbackdetail.activate.success").toString(), null);
	            FacesContext.getCurrentInstance().addMessage(null, message);
	            
			}
		}
		catch(RsntException e){
			log.error("FeedbackAction.deactivateFeedback()", e);
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("feedbackdetail.activate.fail").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	
	/*public void loadFeedback(){
		try{
			if(this.getSelectedFeedbackId()!=null){
				selectedFeedbackQuestionaire = feedbackService.getFeedbackEntity(this.getSelectedFeedbackId());
			}
			
		}
		catch(RsntException e){
			log.error("FeedbackAction.loadFeedback()", e);
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("get.ad.failed").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}*/

	public void loadFeedbackDetail(){
		try{
			initialize();
			log.info("Getting FeedbackDetailid: "+getSelectedFeedbackDetailId());
			if(this.getSelectedFeedbackDetailId()!=null){
				selectedFeedbackQuestionaireDetail = feedbackService.getFeedbackDetailEntity(this.getSelectedFeedbackDetailId());
				setSelectedFeedbackAnswerType(selectedFeedbackQuestionaireDetail.getQuestionTypeId());
				renderOptionTypeData();
			}
			
		}
		catch(RsntException e){
			log.error("FeedbackAction.loadFeedbackDetail()", e);
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("get.ad.failed").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	
	/*public void updateFeedback(){
		try{
			feedbackService.updateFeedbackEntity(this.getSelectedFeedbackQuestionaire());
		}
		catch(RsntException e){
			log.error("FeedbackAction.updateFeedback()", e);
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("get.ad.failed").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	*/
	public void updateFeedbackDetail(){
		try{
			//this.getSelectedFeedbackQuestionaireDetail().setFeedbackQuestionaireId(this.getSelectedFeedbackId());
			this.getSelectedFeedbackQuestionaireDetail().setQuestionTypeId(this.getSelectedFeedbackAnswerType());
			if(this.getOptionTextVal()!=null && (this.getSelectedFeedbackAnswerType().intValue() == 
													Constants.CONT_LOOKUP_FEEDBACKANSWERTYPEOPTION_SINGLE_OPTION || 
					this.getSelectedFeedbackAnswerType().intValue() == Constants.CONT_LOOKUP_FEEDBACKANSWERTYPEOPTION_MULTI_OPTION) ){
				selectedFeedbackQuestionaireDetail.setOptionText(this.getOptionTextVal());
			}
			else{
				selectedFeedbackQuestionaireDetail.setOptionText(null);
			}
			feedbackService.updateFeedbackDetailEntity(this.getSelectedFeedbackQuestionaireDetail());
		}
		catch(RsntException e){
			log.error("FeedbackAction.updateFeedback()", e);
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("save.feedback.failed").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	public void deleteFeedbackDetail(){
		
		try{
			if(this.getDeleteFeedbackIds()!=null){
				List<Long> selFeedbackIds =  CommonUtil.splitStringToListOfLong(this.getDeleteFeedbackIds(),",");
				feedbackService.deleteFeedbackDetail(selFeedbackIds);
			}
		}
		catch(RsntException e){
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("get.ad.failed").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
		}
			
		
	}
	

	public IFeedbackService getFeedbackService() {
		return feedbackService;
	}

	public void setFeedbackService(IFeedbackService feedbackService) {
		this.feedbackService = feedbackService;
	}


	public FeedbackQuestionaireDetail getSelectedFeedbackQuestionaireDetail() {
		return selectedFeedbackQuestionaireDetail;
	}

	public void setSelectedFeedbackQuestionaireDetail(
			FeedbackQuestionaireDetail selectedFeedbackQuestionaireDetail) {
		this.selectedFeedbackQuestionaireDetail = selectedFeedbackQuestionaireDetail;
	}

	public Long getSelectedFeedbackDetailId() {
		return selectedFeedbackDetailId;
	}

	public void setSelectedFeedbackDetailId(Long pSelectedFeedbackDetailId) {
		log.info("setSelectedFeedbackDetailId called wuith: "+pSelectedFeedbackDetailId);
		this.selectedFeedbackDetailId = pSelectedFeedbackDetailId;
	}

	public String getDeleteFeedbackIds() {
		return deleteFeedbackIds;
	}

	public void setDeleteFeedbackIds(String deleteFeedbackIds) {
		this.deleteFeedbackIds = deleteFeedbackIds;
	}

	public ILookupService getLookupService() {
		return lookupService;
	}

	public void setLookupService(ILookupService lookupService) {
		this.lookupService = lookupService;
	}

	public Long getSelectedFeedbackAnswerType() {
		log.info("getSelectedFeedbackAnswerType() returned: "+selectedFeedbackAnswerType);
		return selectedFeedbackAnswerType;
	}

	public void setSelectedFeedbackAnswerType(Long pSelectedFeedbackAnswerType) {
		log.info("setSelectedFeedbackAnswerType() called: "+pSelectedFeedbackAnswerType);
		this.selectedFeedbackAnswerType = pSelectedFeedbackAnswerType;
	}

	public boolean isRenderOptionText() {
		return renderOptionText;
	}

	public void setRenderOptionText(boolean renderOptionText) {
		this.renderOptionText = renderOptionText;
	}
	public boolean isAdvancedFeedback() {
		return advancedFeedback;
	}
	public void setAdvancedFeedback(boolean advancedFeedback) {
		this.advancedFeedback = advancedFeedback;
	}
	public String getOptionTextVal() {
		return optionTextVal;
	}
	public void setOptionTextVal(String optionTextVal) {
		this.optionTextVal = optionTextVal;
	}
	
	
}
