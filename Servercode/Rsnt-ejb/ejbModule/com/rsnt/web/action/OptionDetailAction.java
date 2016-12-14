package com.rsnt.web.action;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.log.Log;

import com.rsnt.common.exception.RsntException;
import com.rsnt.entity.OrganizationOption;
import com.rsnt.entity.User;
import com.rsnt.service.ILayoutService;
import com.rsnt.service.IOptionService;
import com.rsnt.service.IOrganizationService;
import com.rsnt.util.common.CommonUtil;
import com.rsnt.util.common.Constants;

@Name("optionDetailAction")
@Scope(ScopeType.CONVERSATION)
public class OptionDetailAction {

	@In
    private User loginUser;
	

	@In
    private Map messages;
	
	@In(value=ILayoutService.SEAM_NAME,create=true)
    private ILayoutService layoutService;
	
	@Logger
	private Log log;
	
	private OrganizationOption  selectedOrgOption;
	
	private String selectedOrgOptionDesc;
	
	private Long selectedOrgOptionId;
	
	@In(value=IOrganizationService.SEAM_NAME,create=true)
	private IOrganizationService orgServiceImpl;
	
	@In(value=IOptionService.SEAM_NAME,create=true)
	private IOptionService optionServiceImpl;
	
	private boolean editMode;
	
	private boolean optionUpdateValid;
	private boolean OrgPlanTypeWithAlert;

	public boolean isOrgPlanTypeWithAlert() {
		
		
		return OrgPlanTypeWithAlert;
	}

	public void setOrgPlanTypeWithAlert(boolean orgPlanTypeWithAlert) {
		OrgPlanTypeWithAlert = orgPlanTypeWithAlert;
	}
	public void initialize(){
		try{
			setOrgPlanTypeWithAlert(false);
			Long planTypeWithAlrt = layoutService.getOrgPlanType();
			if(planTypeWithAlrt.intValue() == 1)
				setOrgPlanTypeWithAlert(true);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void initializeObject() {
		initialize();
		selectedOrgOption = new OrganizationOption();
		this.setSelectedOrgOptionDesc(null);
		
	}

	public void addOptionData() {
		try {
			initialize();
			final Long orgId = (Long) Contexts.getSessionContext().get(Constants.CONST_ORGID);
			
			if(selectedOrgOption.getOrganizationOptionId()!= null) 
			{
				selectedOrgOption.setModifiedBy(loginUser.getUserName());
				
			}
			else {
				selectedOrgOption.setOrganization(orgServiceImpl.getOrganization(orgId));
				selectedOrgOption.setCreatedBy(loginUser.getUserName());
			}
			int cnt = optionServiceImpl.findDuplicateOption(this.getSelectedOrgOptionDesc(), selectedOrgOption.getOrganizationOptionId());
			if(cnt!=0){
				final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
	                    .get("add.option.duplicate").toString(), null);
	            FacesContext.getCurrentInstance().addMessage(null, message);
	            setOptionUpdateValid(false);
	            return;
			}
			setOptionUpdateValid(true);
			selectedOrgOption.setOptionDescription(this.getSelectedOrgOptionDesc());
			
			selectedOrgOption = (OrganizationOption)optionServiceImpl.merge(selectedOrgOption);
				
			
		} catch (RsntException re) {
			log.error("OptionDetailAction.addOptionData()", re);
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("add.option.failed").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	public void fetchOptionData() {
		try {
			initialize();
			if(selectedOrgOptionId != null) {
				selectedOrgOption = (OrganizationOption) optionServiceImpl.find(selectedOrgOptionId);
				this.setSelectedOrgOptionDesc(selectedOrgOption.getOptionDescription());
			}
		} catch (RsntException re) {
			log.error("OptionDetailAction.fetchOptionData()", re);
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("fetch.option.failed").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	/*public void updateOptionData() {
		try {
			if(selectedOrgOption != null) {
				optionServiceImpl.merge(selectedOrgOption);
			}
		} catch (RsntException re) {
			log.error("OptionDetailAction.updateOptionData()", re);
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("update.option.failed").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}*/
	
	public void deleteOption(){
		try {
			initialize();
			if(selectedOrgOptionId != null) {
				selectedOrgOption = (OrganizationOption) optionServiceImpl.find(selectedOrgOptionId);
				optionServiceImpl.deleteOption(selectedOrgOption);
			}
		} catch (RsntException re) {
			log.error("OptionDetailAction.fetchOptionData()", re);
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("fetch.option.failed").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
		}
		
	}
	public User getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(User loginUser) {
		this.loginUser = loginUser;
	}



	public IOrganizationService getOrgServiceImpl() {
		return orgServiceImpl;
	}

	public void setOrgServiceImpl(IOrganizationService orgServiceImpl) {
		this.orgServiceImpl = orgServiceImpl;
	}

	public IOptionService getOptionServiceImpl() {
		return optionServiceImpl;
	}

	public void setOptionServiceImpl(IOptionService optionServiceImpl) {
		this.optionServiceImpl = optionServiceImpl;
	}

	public OrganizationOption getSelectedOrgOption() {
		return selectedOrgOption;
	}

	public void setSelectedOrgOption(OrganizationOption selectedOrgOption) {
		this.selectedOrgOption = selectedOrgOption;
	}

	public Long getSelectedOrgOptionId() {
		return selectedOrgOptionId;
	}

	public void setSelectedOrgOptionId(Long selectedOrgOptionId) {
		this.selectedOrgOptionId = selectedOrgOptionId;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public String getSelectedOrgOptionDesc() {
		return selectedOrgOptionDesc;
	}

	public void setSelectedOrgOptionDesc(String selectedOrgOptionDesc) {
		this.selectedOrgOptionDesc = selectedOrgOptionDesc;
	}

	public boolean isOptionUpdateValid() {
		return optionUpdateValid;
	}

	public void setOptionUpdateValid(boolean optionUpdateValid) {
		this.optionUpdateValid = optionUpdateValid;
	}
}
