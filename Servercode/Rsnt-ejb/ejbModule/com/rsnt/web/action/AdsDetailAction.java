package com.rsnt.web.action;

import java.awt.image.BufferedImage;import java.io.File;import java.io.FileInputStream;import java.sql.Date;
import java.text.ParseException;import java.text.SimpleDateFormat;
import java.util.ArrayList;import java.util.Arrays;import java.util.Iterator;import java.util.List;import java.util.Map;

import javax.faces.application.FacesMessage;import javax.faces.context.FacesContext;import javax.faces.model.SelectItem;import javax.imageio.ImageIO;

import org.jboss.seam.ScopeType;import org.jboss.seam.annotations.In;import org.jboss.seam.annotations.Logger;import org.jboss.seam.annotations.Name;import org.jboss.seam.annotations.Scope;import org.jboss.seam.log.Log;

import com.rsnt.common.exception.RsntException;import com.rsnt.entity.Ads;import com.rsnt.entity.AdsImage;import com.rsnt.service.IAdsDetailService;import com.rsnt.util.common.AppInitializer;import com.rsnt.util.common.CommonUtil;import com.rsnt.util.common.Constants;import com.rsnt.web.transferobject.FileUploadTO;import com.rsnt.web.util.AdImageUploadHelper;

@Scope(ScopeType.CONVERSATION)
@Name("adsDetailAction")
public class AdsDetailAction {

	private Ads selectedAd;
	
	
	private Long selectedAdId;
	
	@In(value="adImageUploadHelper", create=true)
	private AdImageUploadHelper adImageUploadHelper;
	
	@Logger
	private Log log;
	
	@In(value=IAdsDetailService.SEAM_NAME,create=true)
	private IAdsDetailService adsDetailService;
		
	@In
	private Map messages;
	
	private List<SelectItem> weekdayList1 = new ArrayList<SelectItem>();
	private List<SelectItem> weekdayList2 = new ArrayList<SelectItem>();
	
	private List<String> weeklyRun1 = new ArrayList<String>();
	private List<String> weeklyRun2 = new ArrayList<String>();
	
	private boolean runTillCredit;
	
	private String startTime;
	private String endTime;
	
	private String startDate;
	private String endDate;
	

	private String selectedDelAdImgId;

	
	private boolean updateSuccessFlag;
	
	List<String> deleteImgPathList = new ArrayList<String>();
	
	
	
	private void initialize(){
		//setUploadPrimaryFileName("");
		//setUploadPrimaryImageLinkUrl(null);
		adImageUploadHelper.clearData();
		adImageUploadHelper.clearImageList();
		adImageUploadHelper.resetImageTypeList();
		deleteImgPathList = new ArrayList<String>();
		 
		weekdayList1 = new ArrayList<SelectItem>();
		weekdayList2 = new ArrayList<SelectItem>();
		weekdayList1.add(new SelectItem(2,"Monday"));
		weekdayList1.add(new SelectItem(3,"Tuesday"));
		weekdayList1.add(new SelectItem(4,"Wednesday"));
		weekdayList1.add(new SelectItem(5,"Thursday"));
		weekdayList2.add(new SelectItem(6,"Friday"));
		weekdayList2.add(new SelectItem(7,"Saturday"));
		weekdayList2.add(new SelectItem(1,"Sunday"));
		
		weeklyRun1 = new ArrayList<String>();
		weeklyRun2 = new ArrayList<String>();
		this.setStartTime(null);
		this.setEndTime(null);
		this.setStartDate(null);
		this.setEndDate(null);
		
		this.setRunTillCredit(false);
		
	}
	
	public void dummyMethod(){
		log.info("Dummy Method called");
	}
	
	/*public void uploadPrimaryImageFile(UploadEvent pUploadEvent) {
		log.info("uploadPrimaryImageFile() called");
        setUploadPrimaryItem(pUploadEvent.getUploadItem());
    	setUploadPrimaryFileName(pUploadEvent.getUploadItem().getFileName());
        log.info("setUploadPrimaryFileUpdated True");
    }*/

	
	

	public String  addNewAd(){
		initialize();
		this.selectedAd = new Ads();
		return "success";
	}

	public String loadAdDetail(){
	
		log.info("loadAdDetail called");
		initialize();
	
		//adImageUploadHelper.clearImageList();
		
		try{
			if(this.getSelectedAdId()!=null){
				log.info("loadAdDetail called for "+this.getSelectedAdId());
				selectedAd = adsDetailService.getAdEntity(this.getSelectedAdId());
				//this.setStartTime(CommonUtil.convertDateToFormattedString(selectedAd.getStartDateTime(),"MM/dd/yyyy HH:mm"));
				//this.setEndTime(CommonUtil.convertDateToFormattedString(selectedAd.getEndDateTime(),"MM/dd/yyyy HH:mm"));
				this.setStartDate(CommonUtil.convertDateToFormattedString(selectedAd.getStartDateTime(),"MM/dd/yyyy"));
				this.setEndDate(CommonUtil.convertDateToFormattedString(selectedAd.getEndDateTime(),"MM/dd/yyyy"));
				//this.setStartTime(CommonUtil.convertDateToFormattedString(selectedAd.getStartDateTime(),"hh:mm a"));
				//this.setEndTime(CommonUtil.convertDateToFormattedString(selectedAd.getEndDateTime(),"hh:mm a"));
				
				this.setStartTime(CommonUtil.convertDateToFormattedString(selectedAd.getStartDateTime(),"HH:mm"));
				this.setEndTime(CommonUtil.convertDateToFormattedString(selectedAd.getEndDateTime(),"HH:mm"));
				
				if(selectedAd.getWeekdayRun()!=null){
					this.setWeeklyRun1(CommonUtil.splitStringToList(selectedAd.getWeekdayRun(), "|"));
				}
				if(selectedAd.getWeekdayRun()!=null){
					this.setWeeklyRun2(CommonUtil.splitStringToList(selectedAd.getWeekdayRun(), "|"));
				}
				this.setRunTillCredit(selectedAd.isTillCreditAvailable());
				
				Iterator adsImageIterator = selectedAd.getAdsImageList().iterator();
				while(adsImageIterator.hasNext()){
					AdsImage adsImage =  (AdsImage) adsImageIterator.next();
					
					FileUploadTO uploadTO = new FileUploadTO();
					FileInputStream fis = new FileInputStream(adsImage.getImage());
					BufferedImage imgup = ImageIO.read(fis);
					
					uploadTO.setAdImageId("tmp"+adImageUploadHelper.getTmpDelImageCount());
					uploadTO.setImageOut(imgup);
					uploadTO.setImageTypeId(adsImage.getImageTypeId());
					uploadTO.setAdImageUrl(adsImage.getImage());
					int dotLocation= adsImage.getImage().lastIndexOf(".");
					String extension = adsImage.getImage().substring(dotLocation+1, adsImage.getImage().length());
					uploadTO.setMime(extension);
					if(adsImage.getImageTypeId().intValue() == Constants.CONT_LOOKUP_IMAGE_TYPE_BANNER){
						
						if((Long.valueOf(imgup.getHeight()).compareTo(new Long(300))>0))
							adImageUploadHelper.setPreviewCh("300"); 
						else 
							adImageUploadHelper.setPreviewCh(String.valueOf(imgup.getHeight()));
				    	
				    	if((Long.valueOf(imgup.getWidth()).compareTo(new Long(600))>0))
				    		adImageUploadHelper.setPreviewCw("600"); 
				    	else 
				    		adImageUploadHelper.setPreviewCw(String.valueOf(imgup.getWidth()));
				    	
						
						
						//adImageUploadHelper.setCw(String.valueOf(imgup.getWidth()));
						//adImageUploadHelper.setCh(String.valueOf(imgup.getHeight()));
					}
					//adImageUploadHelper.getAdImageTOList().add(uploadTO);
					adImageUploadHelper.addUploadTO(uploadTO);
				}
				
			}
			return "success";
		}
		catch(RsntException e) 
		{		
			log.error("AdsDetailAction.loadAdDetail()", e);
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("get.ad.failed").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
		}
		catch(Exception e) 
		{		
			log.error("AdsDetailAction.loadAdDetail()", e);
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("get.ad.failed").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
		}
		return null;
	}
	
	public void deactivateAd(){
		try{
			if(this.getSelectedAdId()!=null){
				adsDetailService.deactivateAd(this.getSelectedAdId());
			}
		}
		catch(RsntException e){
			log.error("AdsDetailAction.loadAdDetail()", e);
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("get.ad.failed").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	//This function deletes the images and updates the Ad. In the first version. 
	//Delete was followed by saving the Ad itself.Removing the update now. 
	public String deleteAddonImages(){
		try{
			if(this.getSelectedDelAdImgId()!=null){
				String[] imgIdArr = this.getSelectedDelAdImgId().split(",");
				List<String> imgIdList = Arrays.asList(imgIdArr);
				
				for(String adImageIdVar: imgIdList){
					if(adImageIdVar!=""){
						FileUploadTO delUploadTO = adImageUploadHelper.getUploadTO(adImageIdVar);
						if(delUploadTO!=null){
							String filePath = delUploadTO.getAdImageUrl();
							if(filePath!=null){
								//adsDetailService.deleteAdImage(new Long(delUploadTO.getAdImageId()));
								//deleteFileOnServer(filePath);//
								//Vish: Physical Deletion will happen once the user updates the Ad. Else on get Ad the images will be retrieved back.
								deleteImgPathList.add(filePath);
							}
							//adImageUploadHelper.getAdImageTOList().remove(new Integer(str).intValue());
							adImageUploadHelper.removeUploadTO(delUploadTO);
						}
					}
				}
				//updateAdd();
				
			}
			return "success";
		}
		catch(Exception e){
			log.error("AdsDetailAction.deleteAddonImages()", e);
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("get.ad.failed").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
		}
		return null;
	}
	
	public String cancelEdit(){
		return "success";
	}
	
	
	public String updateAdd(){
		try{
			
			setUpdateSuccessFlag(false);
			
			//Removing Check Image exists only for New Ad creation
			//if(this.getSelectedAd().getAdsId()==null){
			boolean bannerImageExists = false;
			List<FileUploadTO> imageTOListCheck = adImageUploadHelper.getAdImageTOList();
			for (FileUploadTO uploadTO : imageTOListCheck){
				if(uploadTO.getImageTypeId().intValue()==Constants.CONT_LOOKUP_IMAGE_TYPE_BANNER)//
					bannerImageExists = true;;
			}
			
			if(!bannerImageExists){
				final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
	                    .get("ad.bannerimage.error").toString(), null);
	            FacesContext.getCurrentInstance().addMessage(null, message);
				return null;
			}
			//}
			
			if(this.getSelectedAd()!=null){
				
				if(!this.isRunTillCredit()){
					this.getSelectedAd().setTillCreditAvailable(false);
					//CommonUtil.convertLongListToString(this.getWeeklyRun1());
					this.getSelectedAd().setWeekdayRun(CommonUtil.convertLongListToString(this.getWeeklyRun1(),this.getWeeklyRun2()));
					log.info("24 hour:::end"+CommonUtil.convertStringToDate(this.getEndDate()+" "+this.getEndTime(), "MM/dd/yyyy HH:mm"));
					//log.info("12 hour:::end"+CommonUtil.convertStringToDate(this.getEndDate()+" "+this.getEndTime(), "MM/dd/yyyy hh:mm a"));
					log.info("24 hour:::start::"+CommonUtil.convertStringToDate(this.getStartDate()+" "+this.getStartTime(), "MM/dd/yyyy HH:mm"));
					//log.info("12 hour:::start::"+CommonUtil.convertStringToDate(this.getStartDate()+" "+this.getStartTime(), "MM/dd/yyyy hh:mm a"));


					this.getSelectedAd().setStartDateTime(CommonUtil.convertStringToDate(this.getStartDate()+" "+this.getStartTime(), "MM/dd/yyyy HH:mm"));
					this.getSelectedAd().setEndDateTime(CommonUtil.convertStringToDate(this.getEndDate()+" "+this.getEndTime(), "MM/dd/yyyy HH:mm"));
					//this.getSelectedAd().setStartDateTime(CommonUtil.convertStringToDate(this.getStartDate()+" "+this.getStartTime(), "MM/dd/yyyy hh:mm a"));
					//this.getSelectedAd().setEndDateTime(CommonUtil.convertStringToDate(this.getEndDate()+" "+this.getEndTime(), "MM/dd/yyyy hh:mm a"));                   
					this.getSelectedAd().setStartTime(this.getStartTime());						
					this.getSelectedAd().setEndTime(this.getEndTime());					
				    
				    
					
				}
				else{
					this.getSelectedAd().setWeekdayRun(null);
					this.getSelectedAd().setTillCreditAvailable(true);
					this.getSelectedAd().setStartDateTime(null);
					this.getSelectedAd().setEndDateTime(null);
				}
				
				this.getSelectedAd().setActiveFlag(true);
				this.getSelectedAd().getAdsImageList().clear();
				Ads adUpdated = adsDetailService.updateAdEntity(this.getSelectedAd());
				
				
				List<FileUploadTO> imageTOList = adImageUploadHelper.getAdImageTOList();
				/*int success = 0;
				for (FileUploadTO uploadTO : imageTOList){
					if(uploadTO.getImageTypeId().intValue()==Constants.CONT_LOOKUP_IMAGE_TYPE_BANNER && this.getSelectedAd().getAdsId()!=null)//Only need to delete in case of update 
						success = adsDetailService.deleteBannerAdImage(this.getSelectedAd().getAdsId());
					else 
						success = 1;
					
					if(success==1){
							AdsImage adsImageUpdated = new AdsImage();
							adsImageUpdated.setAds(adUpdated);
							adsImageUpdated.setImageTypeId(uploadTO.getImageTypeId());
							adsImageUpdated.setImage("TEMP");
							
							AdsImage adsImageReturn = adsDetailService.createAdImage(adsImageUpdated);
							writeImageFile(uploadTO.getImageOut(), adsImageReturn.getAdsImageId());
						}
					}*/
				for (FileUploadTO uploadTO : imageTOList){
					AdsImage adsImageUpdated = new AdsImage();
					adsImageUpdated.setAds(adUpdated);
					adsImageUpdated.setImageTypeId(uploadTO.getImageTypeId());
					adsImageUpdated.setImage("TEMP");
					adsImageUpdated.setImageExt(uploadTO.getMime());
					
					AdsImage adsImageReturn = adsDetailService.createAdImage(adsImageUpdated);
					writeImageFile(uploadTO.getImageOut(), adsImageReturn.getAdsImageId(), uploadTO.getMime());
				}
				
					
				}
				
				/*Iterator adsImageIterator = this.getSelectedAd().getAdsImageList().iterator();
				while(adsImageIterator.hasNext()){
					AdsImage adsImage =  (AdsImage) adsImageIterator.next();
					if(adsImage.getImageTypeId().intValue() == Constants.CONT_LOOKUP_IMAGE_TYPE_BANNER){
						this.getSelectedAd().getAdsImageList().remove(adsImage);
					}
				}
				
				AdsImage adsImageUpdated = new AdsImage();
				adsImageUpdated.setAds(this.getSelectedAd());
				adsImageUpdated.setImageTypeId(new Long(Constants.CONT_LOOKUP_IMAGE_TYPE_BANNER));
				adsImageUpdated.setImage("TEMP");
				
				//AdsImage adsImageReturn = adsDetailService.createAdImage(image);
				
				this.getSelectedAd().getAdsImageList().add(adsImageUpdated);
				
				Ads adsReturn = adsDetailService.updateAdEntity(this.getSelectedAd());
				
				
				Iterator adsImageIterator2 = this.getSelectedAd().getAdsImageList().iterator();
				while(adsImageIterator2.hasNext()){
					AdsImage adsImage =  (AdsImage) adsImageIterator2.next();
					if(adsImage.getImageTypeId().intValue() == Constants.CONT_LOOKUP_IMAGE_TYPE_BANNER){
						writeExcelFileToServer( adsImage.getAdsImageId());
					}
				}*/
			
			for(String str : deleteImgPathList){
				deleteFileOnServer(str);
			}
			
			adsDetailService.clearEM();
			setUpdateSuccessFlag(true);
			return "success";
		}
		catch(RsntException e) 
		{		
			log.error("AdsDetailAction.loadAdDetail()", e);
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("get.ad.failed").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
		}
		
		return null;
	}
	

	/*public void saveCropImage(){
		try{
			adImageUploadHelper.resizeImage(this.getActiveUploadImageTypeId());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
	}*/
	/*public void updateBannerImage(){
	
		try{
			if(this.getSelectedAdId()!=null){
				int success = adsDetailService.deleteBannerAdImage(this.getSelectedAdId());
				if(success==1){
					AdsImage adsImageUpdated = new AdsImage();
					adsImageUpdated.setAds(this.getSelectedAd());
					adsImageUpdated.setImageTypeId(new Long(Constants.CONT_LOOKUP_IMAGE_TYPE_BANNER));
					adsImageUpdated.setImage("TEMP");
					
					AdsImage adsImageReturn = adsDetailService.createAdImage(adsImageUpdated);
					
					adImageUploadHelper.resizeImage(adsImageReturn.getAdsImageId());
				}
				
			}
			
		}
		catch(RsntException e){
			e.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}*/
	

	/*private void writeExcelFileToServer(Long pAdsImageId) throws RsntException {

		FileOutputStream fos = null;
		try {

			FileInputStream fis = new FileInputStream(this.getUploadPrimaryItem().getFile().getAbsolutePath());
			int dotLocation= this.getUploadPrimaryFileName().lastIndexOf(".");
			String extension = this.getUploadPrimaryFileName().substring(dotLocation, this.getUploadPrimaryFileName().length());
			
			byte[] readData = new byte[1024];

			final String filePath = AppInitializer.importPath + "/"+ pAdsImageId + extension;

			fos = new FileOutputStream(filePath);
			int i = fis.read(readData);

			while (i != -1) {
				fos.write(readData, 0, i);
				i = fis.read(readData);
			}
			fis.close();
			fos.close();

		} catch (final Exception rpmException) {
			throw new RsntException(
					"Error while creating Excel File on Server", rpmException);
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
	}*/
	
	private void writeImageFile(BufferedImage out, Long pAdsImageId, String extension) throws RsntException{
		
		try{
			 //Store the image to a new file
			final String filePath = AppInitializer.importPath + "/"+ pAdsImageId + "."+extension;
			
			ImageIO.write(out,extension,new File(filePath));
		}
		catch(Exception e){
			throw new RsntException(e.getMessage());
		}
		
	}
	
	private void deleteFileOnServer(String filePath) throws RsntException{
		try {
			int nameLocation= filePath.lastIndexOf("/");
			//String fileName= AppInitializer.importPath+"/"+ filePath.substring(nameLocation+1, filePath.length());
			
	         boolean success = (new File(filePath)).delete();
         }
         catch (Exception e) {
        	 throw new RsntException(e.getMessage());
         }
      }
	
	 
	public String cancel(){
		return "sucess";
	}
	public Ads getSelectedAd() {
		return selectedAd;
	}

	public void setSelectedAd(Ads selectedAd) {
		this.selectedAd = selectedAd;
	}

	public Long getSelectedAdId() {
		return selectedAdId;
	}

	public void setSelectedAdId(Long selectedAdId) {
		log.info("Setting selectedAdId "+selectedAdId);
		this.selectedAdId = selectedAdId;
	}

	public IAdsDetailService getAdsDetailService() {
		return adsDetailService;
	}

	public void setAdsDetailService(IAdsDetailService adsDetailService) {
		this.adsDetailService = adsDetailService;
	}
	
	public boolean isRunTillCredit() {
		return runTillCredit;
	}

	public void setRunTillCredit(boolean runTillCredit) {
		this.runTillCredit = runTillCredit;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		log.info("Setting startTime"+startTime);
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		log.info("Setting endTime"+endTime);
		this.endTime = endTime;
	}

	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		log.info("Setting StartDate"+startDate);
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		log.info("Setting EndDate"+endDate);
		this.endDate = endDate;
	}

	public List<SelectItem> getWeekdayList1() {
		return weekdayList1;
	}

	public void setWeekdayList1(List<SelectItem> weekdayList1) {
		this.weekdayList1 = weekdayList1;
	}

	public List<SelectItem> getWeekdayList2() {
		return weekdayList2;
	}

	public void setWeekdayList2(List<SelectItem> weekdayList2) {
		this.weekdayList2 = weekdayList2;
	}

	public List<String> getWeeklyRun1() {
		return weeklyRun1;
	}

	public void setWeeklyRun1(List<String> weeklyRun1) {
		this.weeklyRun1 = weeklyRun1;
	}

	public List<String> getWeeklyRun2() {
		return weeklyRun2;
	}

	public void setWeeklyRun2(List<String> weeklyRun2) {
		this.weeklyRun2 = weeklyRun2;
	}

	

	public AdImageUploadHelper getAdImageUploadHelper() {
		return adImageUploadHelper;
	}

	public void setAdImageUploadHelper(AdImageUploadHelper adImageUploadHelper) {
		this.adImageUploadHelper = adImageUploadHelper;
	}

	public String getSelectedDelAdImgId() {
		return selectedDelAdImgId;
	}

	public void setSelectedDelAdImgId(String selectedDelAdImgId) {
		this.selectedDelAdImgId = selectedDelAdImgId;
	}

	public boolean isUpdateSuccessFlag() {
		return updateSuccessFlag;
	}

	public void setUpdateSuccessFlag(boolean updateSuccessFlag) {
		this.updateSuccessFlag = updateSuccessFlag;
	}

	
}
