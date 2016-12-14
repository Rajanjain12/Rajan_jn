package com.rsnt.web.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.log.Log;

import com.rsnt.common.exception.RsntException;
import com.rsnt.entity.OrganizationLayout;
import com.rsnt.entity.OrganizationLayoutOption;
import com.rsnt.entity.OrganizationOption;
import com.rsnt.entity.User;
import com.rsnt.service.ILayoutService;
import com.rsnt.service.IOptionService;
import com.rsnt.service.IOrganizationService;
import com.rsnt.util.common.CommonUtil;
import com.rsnt.util.common.Constants;

@Scope(ScopeType.CONVERSATION)
@Name("layoutOptionAction")
public class LayoutOptionAction {

	@Logger
	private Log log;
	
	@In(create=true, required= false)
	private LayoutManagerAction layoutManagerAction;
	
	private List<String> orgLayoutOptionsList = new ArrayList<String>();
	private List<SelectItem> orgAllOptionsList = new ArrayList<SelectItem>();
	
	@In
    private User loginUser;
	

	@In(value=ILayoutService.SEAM_NAME,create=true)
    private ILayoutService layoutService;
	
	@In
    private Map messages;
	
	private boolean completeLayoutValid;
	
	private OrganizationOption  selectedOrgOption;
	
	private String selectedOrgOptionDesc;
	
	@In(value=IOptionService.SEAM_NAME,create=true)
	private IOptionService optionServiceImpl;
	
	@In(value=IOrganizationService.SEAM_NAME,create=true)
	private IOrganizationService orgServiceImpl;
	
	private boolean optionUpdateValid;
	private boolean OrgPlanTypeWithAlert;
	
	public boolean isOrgPlanTypeWithAlert() {
		return OrgPlanTypeWithAlert;
	}

	public void setOrgPlanTypeWithAlert(boolean orgPlanTypeWithAlert) {
		OrgPlanTypeWithAlert = orgPlanTypeWithAlert;
	}

	public String initializeLayoutOptions(){
		try{
			log.info("initalizeLayoutOptions(): " );
			orgAllOptionsList = new ArrayList<SelectItem>();
			orgLayoutOptionsList = new ArrayList<String>();
			
			setOrgPlanTypeWithAlert(false);
			Long planTypeWithAlrt = layoutService.getOrgPlanType();
			if(planTypeWithAlrt.intValue() == 1)
				setOrgPlanTypeWithAlert(true);
			
			List<Object[]> allOrgObjectArr= layoutService.getAllOrganizationOptions();
			
			for(Object[] obj : allOrgObjectArr){
				orgAllOptionsList.add(new SelectItem(obj[0].toString(),obj[1].toString()));
			}
			
			List<Object[]> layoutOrgObjectArr= layoutService.getLayoutOrganizationOptions(layoutManagerAction.getOrganizationLayout().getOrganizationLayoutId());
			
			for(Object[] obj : layoutOrgObjectArr){
				orgLayoutOptionsList.add(obj[0].toString());
			}
			
			return "success";
		}
		catch(RsntException e){
			e.printStackTrace();
		}
		return null;
		
	}
	
	public String associateLayoutToOptions(){
		log.info("associateLayoutToOptions(): " );
		
		try{
			OrganizationLayout layout = layoutService.fetchLayoutEntityWithOptions(layoutManagerAction.getOrganizationLayout().getOrganizationLayoutId());
			layout.getOrganizationLayoutOptionList().clear();
			
			for(String val : orgLayoutOptionsList){
				OrganizationLayoutOption option = new OrganizationLayoutOption();
				option.setOrganizationOptionId(new Long(val));
				layout.getOrganizationLayoutOptionList().add(option);
			}
			layoutService.updateOrganizationLayoutOptions(layout);
			setCompleteLayoutValid(true);
			return "success";
		}
		catch(RsntException e){
			setCompleteLayoutValid(false);
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("complete.layout.failed").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public void initializeObject() {
		selectedOrgOption = new OrganizationOption();
		this.setSelectedOrgOptionDesc(null);
	}
	
	//This is for adding new option from the Layout Options Page itself for the marker/layout.
	public void addOptionData() {
		try {
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
				
			orgAllOptionsList = new ArrayList<SelectItem>();
			
			List<Object[]> allOrgObjectArr= layoutService.getAllOrganizationOptions();
			
			for(Object[] obj : allOrgObjectArr){
				orgAllOptionsList.add(new SelectItem(obj[0].toString(),obj[1].toString()));
			}
			
		} catch (RsntException re) {
			log.error("OptionDetailAction.addOptionData()", re);
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("add.option.failed").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	public String completeLayout(){
		return "success";
	}
	public void setLayoutService(ILayoutService layoutService) {
		this.layoutService = layoutService;
	}

	public LayoutManagerAction getLayoutManagerAction() {
		return layoutManagerAction;
	}

	public void setLayoutManagerAction(LayoutManagerAction layoutManagerAction) {
		this.layoutManagerAction = layoutManagerAction;
	}

	public List<String> getOrgLayoutOptionsList() {
		return orgLayoutOptionsList;
	}

	public void setOrgLayoutOptionsList(List<String> orgLayoutOptionsList) {
		this.orgLayoutOptionsList = orgLayoutOptionsList;
	}

	public List<SelectItem> getOrgAllOptionsList() {
		return orgAllOptionsList;
	}

	public void setOrgAllOptionsList(List<SelectItem> orgAllOptionsList) {
		this.orgAllOptionsList = orgAllOptionsList;
	}

	public ILayoutService getLayoutService() {
		return layoutService;
	}

	public boolean isCompleteLayoutValid() {
		return completeLayoutValid;
	}

	public void setCompleteLayoutValid(boolean completeLayoutValid) {
		this.completeLayoutValid = completeLayoutValid;
	}

	public OrganizationOption getSelectedOrgOption() {
		return selectedOrgOption;
	}

	public void setSelectedOrgOption(OrganizationOption selectedOrgOption) {
		this.selectedOrgOption = selectedOrgOption;
	}

	public String getSelectedOrgOptionDesc() {
		return selectedOrgOptionDesc;
	}

	public void setSelectedOrgOptionDesc(String selectedOrgOptionDescVar) {
		log.info("setSelectedOrgOptionDesc() called for: "+selectedOrgOptionDescVar);
		this.selectedOrgOptionDesc = selectedOrgOptionDescVar;
	}

	public IOptionService getOptionServiceImpl() {
		return optionServiceImpl;
	}

	public void setOptionServiceImpl(IOptionService optionServiceImpl) {
		this.optionServiceImpl = optionServiceImpl;
	}

	public IOrganizationService getOrgServiceImpl() {
		return orgServiceImpl;
	}

	public void setOrgServiceImpl(IOrganizationService orgServiceImpl) {
		this.orgServiceImpl = orgServiceImpl;
	}

	public boolean isOptionUpdateValid() {
		return optionUpdateValid;
	}

	public void setOptionUpdateValid(boolean optionUpdateValid) {
		this.optionUpdateValid = optionUpdateValid;
	}

	
}
