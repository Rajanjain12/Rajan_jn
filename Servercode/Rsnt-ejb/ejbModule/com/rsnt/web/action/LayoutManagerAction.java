package com.rsnt.web.action;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.log.Log;

import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.rsnt.common.exception.RsntException;
import com.rsnt.entity.Address;
import com.rsnt.entity.Organization;
import com.rsnt.entity.OrganizationLayout;
import com.rsnt.entity.OrganizationLayoutMarkers;
import com.rsnt.entity.User;
import com.rsnt.service.ILayoutService;
import com.rsnt.service.IOrganizationService;
import com.rsnt.util.common.AppInitializer;
import com.rsnt.util.common.CommonUtil;
import com.rsnt.util.common.Constants;
import com.rsnt.util.common.EmailUtil;
import com.rsnt.util.transferobject.EmailAttachment;

@Scope(ScopeType.CONVERSATION)
@Name("layoutManagerAction")
public class LayoutManagerAction {
	
	private Long numberofTables;
	
	@Logger
	private Log log;
	
	@In(value=ILayoutService.SEAM_NAME,create=true)
    private ILayoutService layoutService;
	
	private OrganizationLayout organizationLayout;
	
	private Long selectedOrgLayoutId;
	
	private List<OrganizationLayoutMarkers> organizationLayoutMarkerList = new ArrayList<OrganizationLayoutMarkers>();
	
	private OrganizationLayoutMarkers addEditOrganizationMarker;
	
	private String addEditOrganizationMarkerId;
	
	private boolean dashboardMode;
	
	private Long selectedOrgMarkerId;
	
	private Long displayGenerateQRCodeBtn;
	
	private boolean createLayoutValid;

	private String addEditOrgMarkerName;
	
	private String addEditOrgLayoutName;
	
	private String addEditOrganizationLayoutId;
	
	private boolean editLayoutMode;
	
	private boolean layoutDefaultFlag;
	
	private boolean layoutActiveFlag;
	
	@In
    private User loginUser;
	
	@In
    private Map messages;
	
	@In(value=IOrganizationService.SEAM_NAME, create=true)
	private IOrganizationService organizationService;
	
	public void initialize(){
		log.info("initialize() called ");
		try{
			setEditLayoutMode(false);
			setAddEditOrgLayoutName(null);
			setLayoutDefaultFlag(false);
			setLayoutActiveFlag(true);
			organizationLayout = new OrganizationLayout();
			
			/*if(layoutService.getDefaultOrgLayoutCheck(new Long(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString()))== 0)
				organizationLayout.setDefaultFlag(true);*/
			setNumberofTables(null);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void dummyMethod(){
		
	}
	

	public String saveLayoutHeader(){
		try{
			log.info("saveLayoutHeader() called ");
			List<OrganizationLayout> layoutDupList = layoutService.checkDuplicateLayout(this.getAddEditOrgLayoutName(),organizationLayout.getOrganizationLayoutId());
			if(layoutDupList!=null && layoutDupList.size()>0 ){
				
				final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
	                    .get("organization.layout.duplicate").toString(), null);
	            FacesContext.getCurrentInstance().addMessage(null, message);
	            setCreateLayoutValid(false);
				return null;
			}
			
			AppInitializer appInitializer = (AppInitializer) Component.getInstance("appInitializer");
			log.info("ApInitializer Instance: "+appInitializer);
			log.info("Number of Tables" + this.getNumberofTables());
			
			if(this.getNumberofTables() > appInitializer.getMaxLayoutMarkers()){
				final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
	                    .get("layout.markers.max").toString(), String.valueOf(appInitializer.getMaxLayoutMarkers()));
	            FacesContext.getCurrentInstance().addMessage(null, message);
	            setCreateLayoutValid(false);
				return null;
			}
			
			setCreateLayoutValid(true);
			if(organizationLayout.getOrganizationLayoutId()==null){
				setDisplayGenerateQRCodeBtn(new Long(0));
				organizationLayout.setOrganizationLayoutMarkerList(populateDefaultTableColumnsLayout());
				organizationLayout.setLayoutCapacity(this.getNumberofTables());
				//organizationLayout.setActiveFlag(false);
			}
			
			organizationLayout.setLayoutIdentificationName(this.getAddEditOrgLayoutName());
			organizationLayout.setDefaultFlag(this.isLayoutDefaultFlag());
			organizationLayout.setActiveFlag(this.layoutActiveFlag);
			organizationLayout = layoutService.saveOrUpdateOrganizationLayoutEntity(organizationLayout);
			this.setSelectedOrgLayoutId(organizationLayout.getOrganizationLayoutId());
			
			if(organizationLayout.isDefaultFlag())layoutService.resetOrgLayoutDefaultFlags(organizationLayout.getOrganizationLayoutId());
			
			return "success";
		}
		catch(RsntException e){
			e.printStackTrace();
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("organization.layout.error").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            setCreateLayoutValid(false);
			return null;
		}
	
	}
	
	public String  loadLayoutData(){
		
		try{
			log.info("loadLayoutTableData(): " );
			organizationLayout = layoutService.fetchLayoutEntity(this.getSelectedOrgLayoutId());
			this.setDisplayGenerateQRCodeBtn(layoutService.getGenerateQRCodeBtnDisplay(this.getSelectedOrgLayoutId()));
			return "success";
		}
		catch(RsntException e){
			
		}
		return null;
	}
	
	public void cancelEditOrgLayout(){
		this.setOrganizationLayout(null);
	}
	
	/*public String loadDashBoardLayout(){
		try{
			setDashboardMode(false);
			if(this.getSelectedOrgLayoutId()!=null){
				organizationLayoutMarkerList = new ArrayList<OrganizationLayoutMarkers>();
				organizationLayoutMarkerList = layoutService.loadLayoutMarkerData(this.getSelectedOrgLayoutId(),true);
			}
			
			optionsList.add(new SelectItem("cs","Call Server"));
			optionsList.add(new SelectItem("cc","Call Check"));
		}
		catch(RsntException e){
			e.printStackTrace();
		}
		return "success";
		
	}*/
		
	public List<OrganizationLayoutMarkers> populateDefaultTableColumnsLayout(){
		
		List<OrganizationLayoutMarkers> organizationLayoutMarkerList = new ArrayList<OrganizationLayoutMarkers>();
		OrganizationLayoutMarkers organizationLayoutMarker = null;
		
		Integer rows = this.getNumberOfRows();
		Integer cols = this.getNumberOfColumns();
		int  tableCount = 1;
		if(rows!=null && cols!=null){
			for(int i=1; i<=rows; i++){
				for(int j=1; j<=cols; j++){
					if(tableCount<=this.getNumberofTables().intValue()){
					
						organizationLayoutMarker =  new OrganizationLayoutMarkers();
						organizationLayoutMarker.setLayoutMarkerPosRowData(Long.valueOf(i));
						organizationLayoutMarker.setLayoutMarkerPosColData(Long.valueOf(j));
						organizationLayoutMarker.setLayoutMarkerDataXSize(Long.valueOf(1));
						organizationLayoutMarker.setLayoutMarkerDataYSize(Long.valueOf(1));
						organizationLayoutMarker.setLayoutMarkerStyleClass("gs_w");
						
						organizationLayoutMarker.setLayoutMarkerCode("Table" +tableCount);
						organizationLayoutMarker.setLayoutMarkerQRCodeGenerated(false);
						organizationLayoutMarker.setActive(true);
						organizationLayoutMarkerList.add(organizationLayoutMarker);
						
						
						tableCount++;
					}
				}
			}
		}
		
		return organizationLayoutMarkerList;
	}
	
	/*private void updateDefaultColumnsData(){
		
		List<OrganizationLayoutMarkers> organizationLayoutMarkerList = this.getOrganizationLayout().getOrganizationLayoutMarkerList();
		OrganizationLayoutMarkers organizationLayoutMarker = null;
		
		Integer rows = this.getNumberOfRows();
		Integer cols = this.getNumberOfColumns();
		int  tableCount = 0;
		if(rows!=null && cols!=null){
			for(int i=1; i<=rows; i++){
				for(int j=1; j<=cols; j++){
					if(tableCount<this.getNumberofTables().intValue()){
					
						organizationLayoutMarker = organizationLayoutMarkerList.get(tableCount);
						organizationLayoutMarker.setLayoutMarkerPosRowData(Long.valueOf(i));
						organizationLayoutMarker.setLayoutMarkerPosColData(Long.valueOf(j));
						organizationLayoutMarker.setLayoutMarkerDataXSize(Long.valueOf(1));
						organizationLayoutMarker.setLayoutMarkerDataYSize(Long.valueOf(1));
						organizationLayoutMarker.setLayoutMarkerStyleClass("gs_w");
						
						tableCount++;
					}
				}
			}
		}
	}*/
	
	
	private List<OrganizationLayoutMarkers> generateQRCodeForMarkers(String layoutId){
		try{
			List<OrganizationLayoutMarkers> layoutMarkerList =  layoutService.loadLayoutMarkerData(new Long(layoutId),true);
			
			for(OrganizationLayoutMarkers markers : layoutMarkerList){
				
				markers.setLayoutMarkerQRCodeImage(generateQRCodeImage(markers.getOrganizationLayoutMarkerId()));
				markers.setLayoutMarkerQRCodeGenerated(true);
				layoutService.saveLayoutMarkerData(markers);
				
			}
			//this.getOrganizationLayout().setActiveFlag(true);
			//layoutService.saveOrUpdateOrganizationLayoutEntity(this.getOrganizationLayout());
			//this.setDisplayGenerateQRCodeBtn(layoutService.getGenerateQRCodeBtnDisplay(this.getOrganizationLayout().getOrganizationLayoutId()));
			return layoutMarkerList;
			/*final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("marker.layoutcode.success").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            
			return "success";*/
		}
		catch(RsntException e){
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("marker.layoutcode.failure").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	public void generateQRCodeForMarkersAndEmail(){
		try{
			log.info("generateQRCodeForMarkersAndEmail called for OrgLayoutId: "+this.getSelectedOrgLayoutId());
			if(this.getSelectedOrgLayoutId()!=null){
				
				Organization org = organizationService.getOrganizationDetail(new Long(Contexts.getSessionContext().get(Constants.CONST_ORGID).toString()));
				Address add = org.getAddress();
				
				List<OrganizationLayoutMarkers> layoutMarkerList =  layoutService.loadLayoutMarkerData(new Long(this.getSelectedOrgLayoutId()),true);
				List<EmailAttachment> emailAttachmentList = new ArrayList<EmailAttachment>();
				
				for(OrganizationLayoutMarkers markers : layoutMarkerList){
					
					markers.setLayoutMarkerQRCodeImage(generateQRCodeImageWithDesc(markers.getOrganizationLayoutMarkerId(),markers.getLayoutMarkerCode()));
					markers.setLayoutMarkerQRCodeGenerated(true);
					layoutService.saveLayoutMarkerData(markers);
					
					
					//InputStream in = new ByteArrayInputStream(markers.getLayoutMarkerQRCodeImage());
					//BufferedImage bImageFromConvert = ImageIO.read(in);
					
					//final BufferedImage bImageFromConvert = ImageIO.read(new File("C://Vish//test.png"));
					
				/*	Graphics g = bImageFromConvert.getGraphics();
				    g.setFont(g.getFont().deriveFont(15f));
				    g.drawString("Hello World!", 20, 20);
				    g.setColor(Color.BLACK);
				    g.dispose();*/

					/*String key = markers.getLayoutMarkerCode();
			        BufferedImage bufferedImage = new BufferedImage(170, 30,
			                BufferedImage.TYPE_INT_RGB);
				    //BufferedImage bufferedImage = ImageIO.read(new File("C://Vish//test.png"));
					InputStream in = new ByteArrayInputStream(markers.getLayoutMarkerQRCodeImage());
					BufferedImage bufferedImage = ImageIO.read(in);
			        Graphics graphics = bufferedImage.getGraphics();
			        //graphics.setColor(Color.LIGHT_GRAY);
			       // graphics.fillRect(0, 0, 200, 50);
			        graphics.setColor(Color.BLACK);
			        graphics.setFont(new Font("Arial Black", Font.BOLD, 10));
			        graphics.drawString(key, 35, 180);
					
				    ImageIO.write(bufferedImage, "png", new File("C://Vish//test22_new.png"));
				    break;*/
				    
					
					EmailAttachment attachment = 
						new EmailAttachment(new ByteArrayInputStream(markers.getLayoutMarkerQRCodeImage()), markers.getLayoutMarkerCode());
					emailAttachmentList.add(attachment);
					
				}
				
				StringBuffer emailQRCodeHdr = new StringBuffer("Hi "+loginUser.getFirstName()+", <br/>");
				emailQRCodeHdr.append(AppInitializer.qrCodeEmailText+"<br/>"); 

				emailQRCodeHdr.append("<br/>Registered Address:<br/>"); 
				emailQRCodeHdr.append(add.getAddressLineOne() +"<br/>");
				emailQRCodeHdr.append((add.getAddressLineTwo()!="" && add.getAddressLineTwo()!= null && add.getAddressLineTwo().trim().length() != 0)?add.getAddressLineTwo()+"<br/>":"");
				emailQRCodeHdr.append(add.getCity()+"<br/>"+add.getState()+", "+add.getZipCode()+"<br/>"+add.getCountry()+"<br/>");
								
				
				/*String appLinkAppleUrl = AppInitializer.appLinkAppleText+" "+"<a href='"+AppInitializer.appLinkApple+"'  target='_blank'>Click Here</a>";
				 String appLinkAndroidUrl = AppInitializer.appLinkAndroidText+" "+"<a href='"+AppInitializer.appLinkAndroid+"'  target='_blank'>Click Here</a>";
				 
				 String residualIncUrl = AppInitializer.residualIncText+" "+"<a href='"+AppInitializer.residualInc+"'  target='_blank'>Click Here</a>";*/
				
				 String appLinkAppleUrl = "<a href='"+AppInitializer.appLinkApple+"'  target='_blank'><img src=\""+AppInitializer.domainNameUrl+AppInitializer.appImangeExtPath+"/"+AppInitializer.appLinkImg+"\"/></a>";
				 String appLinkAndroidUrl = "<a href='"+AppInitializer.appLinkAndroid+"'  target='_blank'><img src=\""+AppInitializer.domainNameUrl+AppInitializer.appImangeExtPath+"/"+AppInitializer.androidLinkImg+"\"/></a>";
			
				 
				 String msgBody = 	emailQRCodeHdr.toString() +"<br/><br/>" + appLinkAppleUrl + "<br/><br/>"
				 			+ appLinkAndroidUrl +"<br/><br/>" + AppInitializer.rmsThankYou;
				 
				 String userEmail = loginUser.getEmail();
				 String ccUserEmail = AppInitializer.qrCodeGroupEmail;
				 //String userEmail = !(AppInitializer.qrCodeGroupEmail.equalsIgnoreCase(""))?AppInitializer.qrCodeGroupEmail:loginUser.getEmail();
				 //String userEmail ="vish.riyer@gmail.com";
				 log.info("Printing msgBody "+msgBody+" to be sent to "+userEmail);	
						
				 EmailUtil emailUtil = (EmailUtil) Component.getInstance(EmailUtil.class);
					try {
						emailUtil.prepareMessageAndSendWithAttachmentCC
								(userEmail,ccUserEmail,AppInitializer.defaultEmailSubject+" QR Code Images", msgBody, emailAttachmentList);
					} catch (RsntException e) {
					}catch (Exception e) {
						e.printStackTrace();
					}
			}
			
			//this.getOrganizationLayout().setActiveFlag(true);
			//layoutService.saveOrUpdateOrganizationLayoutEntity(this.getOrganizationLayout());
			//this.setDisplayGenerateQRCodeBtn(layoutService.getGenerateQRCodeBtnDisplay(this.getOrganizationLayout().getOrganizationLayoutId()));
			//return layoutMarkerList;
			/*final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("marker.layoutcode.success").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            
			return "success";*/
		}
		catch(RsntException e){
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("marker.layoutcode.failure").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
			e.printStackTrace();
		}
		catch(Exception e){
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("marker.layoutcode.failure").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
			e.printStackTrace();
		}
		
	}
	
	public  OrganizationLayoutMarkers generateQRCodeForSelectedMarker(Long pOrgLayoutMarkerId){
		try{
			OrganizationLayoutMarkers layoutMarker =  layoutService.fetchLayoutMarkerEntity(pOrgLayoutMarkerId);
			
				
			layoutMarker.setLayoutMarkerQRCodeImage(generateQRCodeImage(layoutMarker.getOrganizationLayoutMarkerId()));
			layoutMarker.setLayoutMarkerQRCodeGenerated(true);
			layoutService.saveLayoutMarkerData(layoutMarker);
			
			return layoutMarker;
			//this.getOrganizationLayout().setActiveFlag(true);
			//layoutService.saveOrUpdateOrganizationLayoutEntity(this.getOrganizationLayout());
			/*final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("marker.layoutcode.success").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            
			return "success";*/
		}
		catch(RsntException e){
			e.printStackTrace();
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("marker.layoutcode.failure").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
		}
		return null;
		
	}
	
	
	
	private byte[] generateQRCodeImage(Long markerId){
		 ByteArrayOutputStream boe = null;
		Charset charset = Charset.forName("UTF-8");
	    CharsetEncoder encoder = charset.newEncoder();
	    byte[] b = null;
	    try {
	        // Convert a string to UTF-8 bytes in a ByteBuffer
	        //ByteBuffer bbuf = encoder.encode(CharBuffer.wrap("MECARD:N:test;ORG:test;TEL:2222;URL:www.google.com;EMAIL:ss@ss.com;ADR:r r;NOTE:rtest;;"));
	    	ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(String.valueOf(+markerId)));
	        b = bbuf.array();
	    } catch (CharacterCodingException e) {
	        System.out.println(e.getMessage());
	    }

	    String data;
	    try {
	        data = new String(b, "UTF-8");
	        // get a byte matrix for the data
	        BitMatrix matrix = null;
	        int h = 200;
	        int w = 200;
	        com.google.zxing.Writer writer = new MultiFormatWriter();
	        try {
	            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>(2);
	            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
	            matrix = writer.encode(data,
	            com.google.zxing.BarcodeFormat.QR_CODE, w, h, hints);
	        } catch (com.google.zxing.WriterException e) {
	            System.out.println(e.getMessage());
	        }

	        // change this path to match yours (this is my mac home folder, you can use: c:\\qr_png.png if you are on windows)
	                //String filePath = "C:/Vish/qr_png.png";
	       // File file = new File(filePath);
	        try {
	            //MatrixToImageWriter.writeToFile(matrix, "PNG", file);
	            boe = new ByteArrayOutputStream();      
	            MatrixToImageWriter.writeToStream(matrix, "PNG", boe);
	            //System.out.println("printing to " + file.getAbsolutePath());
	        } catch (IOException e) {
	            System.out.println(e.getMessage());
	        }
	    } catch (UnsupportedEncodingException e) {
	        System.out.println(e.getMessage());
	    }
	    return boe.toByteArray();
	}
	
	private byte[] generateQRCodeImageWithDesc(Long markerId, String markerCode){
		 ByteArrayOutputStream boe = null;
		 ByteArrayOutputStream boeOutput = null;
		Charset charset = Charset.forName("UTF-8");
	    CharsetEncoder encoder = charset.newEncoder();
	    byte[] b = null;
	    try {
	        // Convert a string to UTF-8 bytes in a ByteBuffer
	        //ByteBuffer bbuf = encoder.encode(CharBuffer.wrap("MECARD:N:test;ORG:test;TEL:2222;URL:www.google.com;EMAIL:ss@ss.com;ADR:r r;NOTE:rtest;;"));
	    	ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(String.valueOf(+markerId)));
	        b = bbuf.array();
	    } catch (CharacterCodingException e) {
	        System.out.println(e.getMessage());
	    }

	    String data;
	    try {
	        data = new String(b, "UTF-8");
	        // get a byte matrix for the data
	        BitMatrix matrix = null;
	        int h = 144;
	        int w = 144;
	        com.google.zxing.Writer writer = new MultiFormatWriter();
	        try {
	            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>(2);
	            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
	            hints.put(EncodeHintType.MARGIN, 1);
	            matrix = writer.encode(data,
	            com.google.zxing.BarcodeFormat.QR_CODE, w, h, hints);
	        } catch (com.google.zxing.WriterException e) {
	            System.out.println(e.getMessage());
	        }

	        try {
	            //MatrixToImageWriter.writeToFile(matrix, "PNG", file);
	            boe = new ByteArrayOutputStream();      
	            MatrixToImageWriter.writeToStream(matrix, "PNG", boe);
	            InputStream in = new ByteArrayInputStream(boe.toByteArray());
				BufferedImage bufferedImage = ImageIO.read(in);
				//Graphics graphics = bufferedImage.getGraphics();
				//String key = markers.getLayoutMarkerCode();
		        //graphics.setColor(Color.LIGHT_GRAY);
		       // graphics.fillRect(0, 0, 200, 50);
		        //graphics.setColor(Color.BLACK);
		        //graphics.setFont(new Font("Arial Black", com.lowagie.text.Font.NORMAL, 11));
		        //graphics.drawString(markerCode.toUpperCase(), xPos, 144);

				
		        boeOutput = new ByteArrayOutputStream();  
		        ImageIO.write(CommonUtil.drawTextOnImage(markerCode, bufferedImage, 2), "png", boeOutput);
		        //ImageIO.write(bufferedImage, "png", new File("C://Vish//test22_new.png"));
	            //System.out.println("printing to " + file.getAbsolutePath());
	        } catch (IOException e) {
	            System.out.println(e.getMessage());
	        }
	    } catch (UnsupportedEncodingException e) {
	        System.out.println(e.getMessage());
	    }
	    return boeOutput.toByteArray();
	}
	
	
	
	public Long getNumberofTables() {
		return numberofTables;
	}
	public void setNumberofTables(Long numberofTables) {
		this.numberofTables = numberofTables;
	}

	public String initializeAddEditMarker(){
	
		try{
			if(this.getAddEditOrganizationMarkerId()!=null && !this.getAddEditOrganizationMarkerId().equals("undefined")){
				log.info("Loading addEditOrgMarker with Id "+this.getAddEditOrganizationMarkerId());
				addEditOrganizationMarker = layoutService.fetchLayoutMarkerEntity(Long.valueOf(this.getAddEditOrganizationMarkerId()));
				this.setAddEditOrgMarkerName(addEditOrganizationMarker.getLayoutMarkerCode());
			}
			else
			{	log.info("Loading NEW addEditOrgMarker with Id "+this.getAddEditOrganizationMarkerId());
				addEditOrganizationMarker = new OrganizationLayoutMarkers();
				addEditOrganizationMarker.setActive(true);
				this.setAddEditOrgMarkerName(null);
			}
		}
		catch(RsntException e)
		{
			e.printStackTrace();
		}
		return "success";
		
	}
	
	public String initializeEditLayout(){
		
		try{
			if(this.getAddEditOrganizationLayoutId()!=null && !this.getAddEditOrganizationLayoutId().equals("undefined")){
				setEditLayoutMode(true);
				log.info("Loading addEditOrgLayout with Id "+this.getAddEditOrganizationLayoutId());
				organizationLayout = layoutService.fetchLayoutEntity(Long.valueOf(this.getAddEditOrganizationLayoutId()));
				this.setAddEditOrgLayoutName(organizationLayout.getLayoutIdentificationName());
				this.setLayoutDefaultFlag(organizationLayout.isDefaultFlag());
				this.setNumberofTables(new Long(organizationLayout.getOrganizationLayoutMarkerList().size()));
				this.setLayoutActiveFlag(organizationLayout.isActiveFlag());
				//this.setAddEditOrgMarkerName(addEditOrganizationMarker.getLayoutMarkerCode());
			}
			
		}
		catch(RsntException e)
		{
			e.printStackTrace();
		}
		return "success";
		
	}
	public String addEditLayoutMarker(){
		
		try{
			
	       List<OrganizationLayoutMarkers> layoutMarkerList= layoutService.checkDuplicateMarker(this.getAddEditOrgMarkerName(),this.getAddEditOrganizationMarker().getOrganizationLayoutMarkerId(), this.getOrganizationLayout().getOrganizationLayoutId() );
			if(layoutMarkerList!=null && layoutMarkerList.size()>0){
				
				final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
	                    .get("organization.marker.duplicate").toString(), null);
	            FacesContext.getCurrentInstance().addMessage(null, message);
	            setCreateLayoutValid(false);
				return null;
			}
			
			
			setCreateLayoutValid(true);
			
			this.getAddEditOrganizationMarker().setLayoutMarkerCode(this.getAddEditOrgMarkerName());
			this.getAddEditOrganizationMarker().setLayoutIdentificationName(this.getOrganizationLayout().getLayoutIdentificationName());
			this.getAddEditOrganizationMarker().setOrganizationLayout(this.getOrganizationLayout());
			this.getAddEditOrganizationMarker().setOrganizationId(this.getOrganizationLayout().getOrganizationId());
			
			
			//Reset All layout positions if only new marker is added.
			if(this.getAddEditOrganizationMarker().getOrganizationLayoutMarkerId()==null){
				
				//updateDefaultColumnsData(); 
				addEditOrganizationMarker.setLayoutMarkerPosRowData(Long.valueOf(0));
				addEditOrganizationMarker.setLayoutMarkerPosColData(Long.valueOf(0));
				addEditOrganizationMarker.setLayoutMarkerDataXSize(Long.valueOf(1));
				addEditOrganizationMarker.setLayoutMarkerDataYSize(Long.valueOf(1));
				addEditOrganizationMarker.setLayoutMarkerStyleClass("gs_w");
				
				
				//If new Marker Added then only add to the list
				this.getOrganizationLayout().getOrganizationLayoutMarkerList().add(this.getAddEditOrganizationMarker());
				this.setNumberofTables(Long.valueOf(this.getOrganizationLayout().getOrganizationLayoutMarkerList().size()));
				this.getOrganizationLayout().setLayoutCapacity(Long.valueOf(this.getOrganizationLayout().getOrganizationLayoutMarkerList().size()));
			}
			
			layoutService.saveOrUpdateOrganizationLayoutEntity(organizationLayout);
			return "success";
		}
		catch(RsntException e){
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("organization.marker.error").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            setCreateLayoutValid(false);
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	public void deactivateLayoutMarker(){
		
		try{
			if(this.getAddEditOrganizationMarkerId()!=null && !this.getAddEditOrganizationMarkerId().equals("undefined")){
				log.info("Loading addEditOrgMarker with Id "+this.getAddEditOrganizationMarkerId());
				addEditOrganizationMarker = layoutService.fetchLayoutMarkerEntity(Long.valueOf(this.getAddEditOrganizationMarkerId()));
			}
			
			addEditOrganizationMarker.setActive(!addEditOrganizationMarker.isActive());
			//this.getOrganizationLayout().getOrganizationLayoutMarkerList().remove(addEditOrganizationMarker);
			
			//Reset All layout positions if marker is deleted.
		/*	if(this.getAddEditOrganizationMarker().getOrganizationLayoutMarkerId()==null){
				this.setNumberofTables(Long.valueOf(this.getOrganizationLayout().getOrganizationLayoutMarkerList().size()));
				updateDefaultColumnsData(); 
				this.getOrganizationLayout().setLayoutCapacity(Long.valueOf(this.getOrganizationLayout().getOrganizationLayoutMarkerList().size()));
			}
			
			layoutService.saveLayoutMarkerData(layoutMarker);*/
			
			//this.getOrganizationLayout().setActiveFlag(true);
			//layoutService.saveOrUpdateOrganizationLayoutEntity(this.getOrganizationLayout());
			layoutService.saveLayoutMarkerData(addEditOrganizationMarker);
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, messages
                    .get("marker.layoutactivate.success").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            
            
			
			//layoutService.saveOrUpdateOrganizationLayoutEntity(organizationLayout);
		}
		catch(RsntException e){
			final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
                    .get("marker.layoutactivate.fail").toString(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
			e.printStackTrace();
		}
		
		
	}
	
	/*public void startDownload() throws RsntException {
		
		Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext()
         .getRequestParameterMap();
		   final String addEditMarkerId = requestParameterMap.get("addEditMarkerId");
		   
		   
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		final HttpServletRequest request = (HttpServletRequest) facesContext
				.getExternalContext().getRequest();
		final HttpServletResponse response = (HttpServletResponse) facesContext
				.getExternalContext().getResponse();
		addEditOrganizationMarker = layoutService.fetchLayoutMarkerEntity(Long.valueOf(addEditMarkerId));
		if(addEditOrganizationMarker.getLayoutMarkerQRCodeImage()!=null){
			response.setContentType("image/png");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ addEditOrganizationMarker.getLayoutMarkerCode() + "\"");
			response.setHeader("cache-control", "no-cache");
			ServletOutputStream outs;
			try {
				outs = response.getOutputStream();
				outs.write(addEditOrganizationMarker.getLayoutMarkerQRCodeImage());
				outs.flush();
				outs.close();
				facesContext.responseComplete();
			} catch (IOException e) {
				throw new RsntException(
						"Error while downloading the defect report file", e);
			}
		}
		else{
			
		}
		
		
	}*/

	
	public void startDownloadPdf(boolean generateForLayout){
		try{
			
			Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext()
	         .getRequestParameterMap();
			   final String addEditMarkerId = requestParameterMap.get("addEditMarkerId");
			   final String layoutId = requestParameterMap.get("layoutId");
			   
			   String fileName=null;
			final FacesContext facesContext = FacesContext.getCurrentInstance();
			final HttpServletRequest request = (HttpServletRequest) facesContext
					.getExternalContext().getRequest();
			final HttpServletResponse response = (HttpServletResponse) facesContext
					.getExternalContext().getResponse();
			
			List<OrganizationLayoutMarkers> layoutMarkersList = new ArrayList<OrganizationLayoutMarkers>();
			
			if(generateForLayout && layoutId!=null && layoutId!=""){
				layoutMarkersList = generateQRCodeForMarkers(layoutId);
				fileName = "All Markers";
			}
			else{
				OrganizationLayoutMarkers selectedMarker = generateQRCodeForSelectedMarker(new Long(addEditMarkerId));
				layoutMarkersList.add(selectedMarker);
				fileName = selectedMarker.getLayoutMarkerCode();
			}
			
			ByteArrayOutputStream  outputStream = new ByteArrayOutputStream();
			  
			 writePdf(outputStream, layoutMarkersList);
			 byte[] bytes = outputStream.toByteArray();
			
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ fileName + "\".pdf");
			response.setHeader("cache-control", "no-cache");
			ServletOutputStream outs;
			
			outs = response.getOutputStream();
			outs.write(bytes);
			outs.flush();
			outs.close();
			facesContext.responseComplete();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	public void writePdf(OutputStream outputStream, List<OrganizationLayoutMarkers> markersList) throws Exception {
		Document document = new Document(PageSize.A4, 25, 25, 25, 25);
		PdfWriter.getInstance(document, outputStream);
		document.open();
		
		for(OrganizationLayoutMarkers marker : markersList){
			 
	        Image hoja = Image.getInstance(marker.getLayoutMarkerQRCodeImage());
	        //hoja.scaleToFit(25,25);
	        hoja.setAbsolutePosition(200f, 650f);
	        document.add(hoja);

	        /*	document.addTitle("Test PDF");
	        document.addSubject("Testing email PDF");
	        document.addKeywords("iText, email");
	        document.addAuthor("Jee Vang");
	        document.addCreator("Jee Vang");*/
			
	        Paragraph paragraph = new Paragraph();
			paragraph.add(new Chunk( marker.getLayoutMarkerCode()));
			document.add(paragraph);
			
	        document.newPage();
		}
       
		
        document.close();
	}

	public ILayoutService getLayoutService() {
		return layoutService;
	}



	public void setLayoutService(ILayoutService layoutService) {
		this.layoutService = layoutService;
	}




	public Integer getNumberOfColumns() {
		/*if(this.getNumberofTables().intValue()<=6)
			return 5;
		else if(this.getNumberofTables().intValue()<=12)
			return 5;
		else if(this.getNumberofTables().intValue()<=20)
			return 5;
		else if(this.getNumberofTables().intValue()<=30)
			return 6;
		else if(this.getNumberofTables().intValue()<=42)
			return 6;
		else if(this.getNumberofTables().intValue()<=56)
			return 6;
		else if(this.getNumberofTables().intValue()<=72)
			return 6;
		else if(this.getNumberofTables().intValue()<=90)
			return 6;
		else if(this.getNumberofTables().intValue()<=110)
			return 6;
		else if(this.getNumberofTables().intValue()>20)*/
			return 6;
		
		//return null;
	}

	public Integer getNumberOfRows() {
		if(this.getNumberofTables().intValue()<=6)
			return 1;
		else if(this.getNumberofTables().intValue()<=12)
			return 3;
		else if(this.getNumberofTables().intValue()<=20)
			return 4;
		else if(this.getNumberofTables().intValue()<=30)
			return 5;
		/*else if(this.getNumberofTables().intValue()<=42)
			return 6;
		else if(this.getNumberofTables().intValue()<=56)
			return 7;
		else if(this.getNumberofTables().intValue()<=72)
			return 8;
		else if(this.getNumberofTables().intValue()<=90)
			return 9;
		else if(this.getNumberofTables().intValue()<=110)
			return 10;*/
		else if(this.getNumberofTables().intValue()>30)
		{
			Float val1 = new Float(this.getNumberofTables());
			Float val2 = new Float(6);
			Float newVal = val1/val2;
			return (int)Math.ceil(newVal);
		}
		
		return null;
	}

	

	public String loadDashBoardForOrgLayoutId(){
		/*try{
			
			log.info("Layout Dashboard called for: "+this.getSelectedOrgLayoutId());
			organizationLayoutMarkerList = new ArrayList<OrganizationLayoutMarkers>();
			//orgLayoutOptionList = new ArrayList<AdsDataTO>();
			
			if(this.getSelectedOrgLayoutId()==null || this.getSelectedOrgLayoutId().intValue()==0){
				final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
	                    .get("organization.layout.selectOrgLayoutId").toString(), null);
	            FacesContext.getCurrentInstance().addMessage(null, message);
			}else{
				organizationLayoutMarkerList = layoutService.loadLayoutMarkerData(Long.valueOf(this.getSelectedOrgLayoutId()), true);
				
				if(organizationLayoutMarkerList.size()>0){
					//setSelectedLayoutName(organizationLayoutMarkerList.get(0).getOrganizationLayout().getLayoutIdentificationName());
					//setSelectedOrgLayoutId((organizationLayoutMarkerList.get(0).getOrganizationLayout().getOrganizationLayoutId().toString()));
					
					List<Object[]> layoutOrgObjectArr= layoutService.getLayoutOrganizationOptions(organizationLayoutMarkerList.get(0).getOrganizationLayout().getOrganizationLayoutId());
					for(Object[] obj : layoutOrgObjectArr){
						orgLayoutOptionList.add(new SelectItem(obj[0].toString(),obj[1].toString()));
					}
					AdsDataTO dataTO = null;  
					for(Object[] obj : layoutOrgObjectArr){
						dataTO = new AdsDataTO();
						Long lookupTypeId = null;
						if(obj[2]!=null) lookupTypeId = new Long(obj[2].toString());
						dataTO.setAdsId(obj[0].toString());
						dataTO.setDescription(obj[1].toString());
						if(lookupTypeId!=null && lookupTypeId.intValue()==1)
							dataTO.setImageUrl("http://ringmyserver.com/dboardImg/"+obj[0].toString()+".png");
						else
							dataTO.setImageUrl("http://ringmyserver.com/dboardImg/99.png");
						
						//orgLayoutOptionList.add(new SelectItem(obj[0].toString(),obj[1].toString()));
						orgLayoutOptionList.add(dataTO);
					}
				}
				else{
					//setSelectedOrgLayoutId("0");
					final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messages
		                    .get("organization.layout.nomarker").toString(), null);
		            FacesContext.getCurrentInstance().addMessage(null, message);
				}
			}
			
			
			//setSelectedOrgLayoutId((organizationLayoutMarkerList.get(0).getOrganizationLayout().getOrganizationLayoutId().toString()));
			optionsList.add(new SelectItem("cs","Call Server"));
			optionsList.add(new SelectItem("cc","Call Check"));
		}
		catch(RsntException e){
			e.printStackTrace();
		}*/
		return "success";
		
	}



	public List<OrganizationLayoutMarkers> getOrganizationLayoutMarkerList() {
		return organizationLayoutMarkerList;
	}


	public void setOrganizationLayoutMarkerList(
			List<OrganizationLayoutMarkers> organizationLayoutMarkerList) {
		this.organizationLayoutMarkerList = organizationLayoutMarkerList;
	}


	public Long getSelectedOrgLayoutId() {
		return selectedOrgLayoutId;
	}


	public void setSelectedOrgLayoutId(Long selectedOrgLayoutId) {
		this.selectedOrgLayoutId = selectedOrgLayoutId;
	}


	public OrganizationLayout getOrganizationLayout() {
		return organizationLayout;
	}


	public void setOrganizationLayout(OrganizationLayout organizationLayout) {
		this.organizationLayout = organizationLayout;
	}


	

	public OrganizationLayoutMarkers getAddEditOrganizationMarker() {
		return addEditOrganizationMarker;
	}


	public void setAddEditOrganizationMarker(
			OrganizationLayoutMarkers addEditOrganizationMarker) {
		this.addEditOrganizationMarker = addEditOrganizationMarker;
	}


	public String getAddEditOrganizationMarkerId() {
		return addEditOrganizationMarkerId;
	}


	public void setAddEditOrganizationMarkerId(String addEditOrganizationMarkerId) {
		log.info("setAddEditMarkerId called for "+addEditOrganizationMarkerId);
		this.addEditOrganizationMarkerId = addEditOrganizationMarkerId;
	}


	public boolean isDashboardMode() {
		return dashboardMode;
	}


	public void setDashboardMode(boolean dashboardMode) {
		this.dashboardMode = dashboardMode;
	}


	public Long getSelectedOrgMarkerId() {
		return selectedOrgMarkerId;
	}


	public void setSelectedOrgMarkerId(Long selectedOrgMarkerId) {
		this.selectedOrgMarkerId = selectedOrgMarkerId;
	}


	public Long getDisplayGenerateQRCodeBtn() {
		return displayGenerateQRCodeBtn;
	}


	public void setDisplayGenerateQRCodeBtn(Long displayGenerateQRCodeBtn) {
		this.displayGenerateQRCodeBtn = displayGenerateQRCodeBtn;
	}


	public boolean isCreateLayoutValid() {
		return createLayoutValid;
	}


	public void setCreateLayoutValid(boolean createLayoutValid) {
		this.createLayoutValid = createLayoutValid;
	}


	public String getAddEditOrgMarkerName() {
		return addEditOrgMarkerName;
	}


	public void setAddEditOrgMarkerName(String addEditOrgMarkerName) {
		this.addEditOrgMarkerName = addEditOrgMarkerName;
	}

	public String getAddEditOrganizationLayoutId() {
		return addEditOrganizationLayoutId;
	}

	public void setAddEditOrganizationLayoutId(String addEditOrganizationLayoutId) {
		this.addEditOrganizationLayoutId = addEditOrganizationLayoutId;
	}

	public boolean isEditLayoutMode() {
		return editLayoutMode;
	}

	public void setEditLayoutMode(boolean editLayoutMode) {
		this.editLayoutMode = editLayoutMode;
	}

	public String getAddEditOrgLayoutName() {
		return addEditOrgLayoutName;
	}

	public void setAddEditOrgLayoutName(String addEditOrgLayoutName) {
		this.addEditOrgLayoutName = addEditOrgLayoutName;
	}

	public boolean isLayoutDefaultFlag() {
		return layoutDefaultFlag;
	}

	public void setLayoutDefaultFlag(boolean layoutDefaultFlag) {
		this.layoutDefaultFlag = layoutDefaultFlag;
	}

	public boolean isLayoutActiveFlag() {
		return layoutActiveFlag;
	}

	public void setLayoutActiveFlag(boolean layoutActiveFlag) {
		this.layoutActiveFlag = layoutActiveFlag;
	}

	public IOrganizationService getOrganizationService() {
		return organizationService;
	}

	public void setOrganizationService(IOrganizationService organizationService) {
		this.organizationService = organizationService;
	}


}
