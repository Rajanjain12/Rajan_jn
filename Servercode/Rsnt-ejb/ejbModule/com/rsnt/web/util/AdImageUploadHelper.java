package com.rsnt.web.util;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import com.rsnt.util.common.Constants;
import com.rsnt.web.transferobject.FileUploadTO;

@Scope(ScopeType.SESSION)
@Name("adImageUploadHelper")
public class AdImageUploadHelper {

	private UploadItem uploadItem;

	private Long activeUploadImageTypeId;
	
	private String mime;
	@Logger
	private Log log;
	
	private String cx;
	private String cy;
	private String cx2;
	private String cy2;
	private String ch;
	private String cw;
	private int tmpDelImageCount;
	
	private String previewCh;
	private String previewCw;
	
	
	private List<FileUploadTO> adImageTOList = new ArrayList<FileUploadTO>();
	
	private List<FileUploadTO> adAddonImageTOList = new ArrayList<FileUploadTO>();
	
	private List<SelectItem> imageTypeList = new ArrayList<SelectItem>();

	public void paintCurrent(OutputStream stream, Object object) throws IOException {
		log.info("paintCurrent called");
		
		if(this.getUploadItem()!=null){
			FileInputStream fis = new FileInputStream(this.getUploadItem().getFile().getAbsolutePath());
			BufferedImage imgup = ImageIO.read(fis);
		    //ImageIO.write(imgup, "jpeg", stream);
			ImageIO.write(imgup, this.getMime(), stream);
		    
		}
		    
	    
	}
	
	public void paint(OutputStream stream, Object object) throws IOException {
		log.info("paint called");
		
		if(this.getAdImageTOList()!=null && this.getAdImageTOList().size()>0){
			BufferedImage imgup = this.getAdImageTOList().get((Integer)object).getImageOut();
		   // ImageIO.write(imgup, "jpeg", stream);
			 ImageIO.write(imgup, this.getAdImageTOList().get((Integer)object).getMime(), stream);
		    
		}
		    
	    
	}
	
	/*public void paint(OutputStream stream, Object object) throws IOException {
		log.info("paint called");
		
		if(uploadItem!=null){
			log.info("paint called after bool check");
			FileInputStream fis = new FileInputStream(uploadItem.getFile().getAbsolutePath());
			BufferedImage imgup = ImageIO.read(fis);
		    ImageIO.write(imgup, "jpeg", stream);
		    
		}
		    
	    
	}*/

	public void listener(UploadEvent event) throws Exception {
		log.info("listener called");
		clearData();
		
		uploadItem = event.getUploadItem();
		int dotLocation= event.getUploadItem().getFileName().lastIndexOf(".");
		String extension = event.getUploadItem().getFileName().substring(dotLocation+1, event.getUploadItem().getFileName().length());
		setMime(extension);
		log.info("listener done");
		
		//writeExcelFileToServer(event);
	}
	
	public void clearImageList(){
		 adImageTOList = new ArrayList<FileUploadTO>();
		 setTmpDelImageCount(0);
	}
	
	public void addUploadTO(FileUploadTO uploadTO){
		adImageTOList.add(uploadTO);
	}
	
	public FileUploadTO getUploadTO(String pAdImageId){
		for (FileUploadTO uploadTO : adImageTOList){
    		
    		if(uploadTO.getAdImageId().equals(pAdImageId)){
    			return uploadTO;
    		}
		}
		return null;
	}
	
	public void removeUploadTO(int index){
		adImageTOList.remove(index);
	}
	
	public void removeUploadTO(FileUploadTO uploadTO){
		adImageTOList.remove(uploadTO);
	}
	
	public  void clearData(){
		this.setUploadItem(null);
		setMime(null);
		
		
		//adImageTOList = new ArrayList<FileUploadTO>();
		
		setCx(null);
		 setCy(null);
		 setCx2(null);
		 setCy2(null);
		 setCh(null);
		 setCw(null);
		
		
		
	}
	
	public void resetImageTypeList(){
		imageTypeList  = new ArrayList<SelectItem>();
		imageTypeList.add(new SelectItem(new  Long(Constants.CONT_LOOKUP_IMAGE_TYPE_ADDON),"ADDON"));
		imageTypeList.add(new SelectItem(new  Long(Constants.CONT_LOOKUP_IMAGE_TYPE_BANNER),"BANNER"));
		
		setActiveUploadImageTypeId(new Long(Constants.CONT_LOOKUP_IMAGE_TYPE_BANNER));
	}

	/*public void resizeImage() throws Exception{

		if (this.getUploadItem() != null) {
			FileInputStream fis = new FileInputStream(uploadItem.getFile().getAbsolutePath());
			BufferedImage imgup = ImageIO.read(fis);

			BufferedImage resizedImage = new BufferedImage(new Integer(getCw()),new Integer(getCh()), BufferedImage.TYPE_INT_RGB);
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(imgup, new Integer(getCx2()), new Integer(getCy2()),new Integer(getCw()),new Integer(getCh()), null);
			g.dispose();

			int dotLocation= this.getUploadItem().getFileName().lastIndexOf(".");
			String extension = this.getUploadItem().getFileName().substring(dotLocation+1, this.getUploadItem().getFileName().length());
			final String filePath = AppInitializer.importPath + "/resize.jpg";
			
			File outputfile = new File(filePath);
			ImageIO.write(resizedImage, "jpg", outputfile);
			
			//return resizedImage;
		}

	}*/
	
	public void resizeImage() throws Exception{

		log.info("ResizeImage called for "+this.getActiveUploadImageTypeId());
		if (this.getUploadItem() != null) {
			FileInputStream fis = new FileInputStream(uploadItem.getFile().getAbsolutePath());
			BufferedImage imgup = ImageIO.read(fis);

			//Get the sub image
			log.info("Printing cx:"+getCx());
			log.info("Printing cy:"+getCy());
			log.info("Printing cw:"+getCw());
			log.info("Printing ch:"+getCh());
			
		    BufferedImage out=imgup.getSubimage(new Integer(getCx()),new Integer(getCy()),new Integer(getCw()),new Integer(getCh()));

		    //Store the image to a new file
		    //int dotLocation= this.getUploadItem().getFileName().lastIndexOf(".");
			//String extension = this.getUploadItem().getFileName().substring(dotLocation, this.getUploadItem().getFileName().length());
			//final String filePath = AppInitializer.importPath + "/"+ pAdsImageId + ".jpg";
			
			//ImageIO.write(out,"jpg",new File(filePath));
		    
		    FileUploadTO imageTO = new  FileUploadTO();
		    imageTO.setImageOut(out);
		    imageTO.setAdImageId("tmp"+getTmpDelImageCount());
		    imageTO.setImageTypeId(this.getActiveUploadImageTypeId());
		    imageTO.setMime(this.getMime());
		    
		    Integer bannerImageIndex= null;
		    Integer pCount = 0;
		    if(this.getActiveUploadImageTypeId().intValue()==Constants.CONT_LOOKUP_IMAGE_TYPE_BANNER){
		    	for (FileUploadTO uploadTO : adImageTOList){
		    		
		    		if(uploadTO.getImageTypeId().intValue()==Constants.CONT_LOOKUP_IMAGE_TYPE_BANNER){
		    			bannerImageIndex = pCount;
		    		}
		    		pCount++;
		    	}
		    	if(bannerImageIndex!=null){
		    		adImageTOList.remove(bannerImageIndex.intValue());
		    	}
		    }
		    
		    if(this.getActiveUploadImageTypeId().intValue()==Constants.CONT_LOOKUP_IMAGE_TYPE_BANNER){
		    	if((Long.valueOf(this.getCh()).compareTo(new Long(300))>0))
					setPreviewCh("300"); 
				else 
					setPreviewCh(this.getCh());
		    	
		    	if((Long.valueOf(this.getCw()).compareTo(new Long(600))>0))
		    		setPreviewCw("600"); 
		    	else 
		    		setPreviewCw(this.getCw());
		    	
		    }
		    
		    adImageTOList.add(imageTO);
			
		    log.info("ResizeImage Done for "+this.getActiveUploadImageTypeId());
			//return resizedImage;
		}

	}
	

	public long getTimeStamp() {
		return System.currentTimeMillis();
	}

	public UploadItem getUploadItem() {
		return uploadItem;
	}

	public void setUploadItem(UploadItem uploadItem) {
		this.uploadItem = uploadItem;
	}

	public String getMime() {
		log.info("getMime called for; "+mime);
		return mime;
	}

	public void setMime(String mime) {
		this.mime = mime;
	}

	public String getCx() {
		return cx;
	}

	public void setCx(String cx) {
		this.cx = cx;
	}

	public String getCy() {
		return cy;
	}

	public void setCy(String cy) {
		this.cy = cy;
	}

	public String getCx2() {
		return cx2;
	}

	public void setCx2(String cx2) {
		this.cx2 = cx2;
	}

	public String getCy2() {
		return cy2;
	}

	public void setCy2(String cy2) {
		this.cy2 = cy2;
	}

	public String getCh() {
		return ch;
	}

	public void setCh(String ch) {
		this.ch = ch;
	}

	public String getCw() {
		return cw;
	}

	public void setCw(String cw) {
		this.cw = cw;
	}

	public List<FileUploadTO> getAdImageTOList() {
		return adImageTOList;
	}

	public void setAdImageTOList(List<FileUploadTO> adImageTOList) {
		this.adImageTOList = adImageTOList;
	}

	public Long getActiveUploadImageTypeId() {
		return activeUploadImageTypeId;
	}

	public void setActiveUploadImageTypeId(Long pActiveUploadImageTypeId) {
		this.activeUploadImageTypeId = pActiveUploadImageTypeId;
	}

	public int getTmpDelImageCount() {
		return tmpDelImageCount++;
	}

	public void setTmpDelImageCount(int tmpDelImageCount) {
		this.tmpDelImageCount = tmpDelImageCount;
	}

	public List<SelectItem> getImageTypeList() {
		return imageTypeList;
	}

	public void setImageTypeList(List<SelectItem> imageTypeList) {
		this.imageTypeList = imageTypeList;
	}

	public List<FileUploadTO> getAdAddonImageTOList() {
		return adAddonImageTOList;
	}

	public void setAdAddonImageTOList(List<FileUploadTO> adAddonImageTOList) {
		this.adAddonImageTOList = adAddonImageTOList;
	}

	public String getPreviewCh() {
		/*if((Long.valueOf(this.getCh()).compareTo(new Long(300))>0))
			return "300"; 
		else 
			return this.getCh();*/
		return previewCh;
	}

	public void setPreviewCh(String previewCh) {
		this.previewCh = previewCh;
	}

	public String getPreviewCw() {
		/*if((Long.valueOf(this.getCw()).compareTo(new Long(600))>0))
				return "600"; 
		else 
			return this.getCw();*/
		return previewCw;
	}

	public void setPreviewCw(String previewCw) {
		this.previewCw = previewCw;
	}

	
	

	
}
