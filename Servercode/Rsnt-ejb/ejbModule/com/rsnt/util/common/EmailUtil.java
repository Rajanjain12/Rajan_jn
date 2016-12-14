package com.rsnt.util.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.Renderer;
import org.jboss.seam.log.Log;

import com.rsnt.common.exception.RsntException;
import com.rsnt.util.transferobject.EmailAttachment;

@Name("emailUtil")
public class EmailUtil implements Serializable {

    private static final long serialVersionUID = -6276150078066400946L;

    @In(create = true)
    private Renderer renderer;
    
    @Logger
    private Log log;

    private List<EmailRecepient> addressedTo;
    
    private List<EmailRecepient> addressCC;

    private EmailRecepient addressedFrom;
    
    private EmailRecepient bccRcpnt;

    private Date sendDate;

    private String subject;

    private String messageBody;

    private InputStream fileStream;

    private String attachmentName;

    private String attachmentType;
    
    //private byte[] inlineAttachment;
    
    private List<EmailAttachment> emailAttachmentList = new ArrayList<EmailAttachment>();
    
    private EmailAttachment pdfAttachment;
    
    
    public void prepareMessageAndSend(String emailId,String subject,String message) throws RsntException
    {
        addressedTo = new ArrayList<EmailRecepient>();
        List<String> emailList = Arrays.asList(emailId.split(","));
        for(String eachEmail: emailList){
        	addressedTo.add(new EmailRecepient("No Reply", eachEmail));
        }
        
    	
    	this.addressedFrom = new EmailRecepient("Support Team", AppInitializer.supportEmail);
        this.subject = subject;
        this.messageBody = message;
        
      //  InputStream inStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("/logo_light.png");
        /*InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("logo/logo.png");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            int i;
            while ((i = inStream.read()) > 0) {
                bos.write(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.inlineAttachment = bos.toByteArray();*/

        if(!(AppInitializer.bccRcpntEmailId.equalsIgnoreCase("")))
        bccRcpnt = new EmailRecepient("No Reply",AppInitializer.bccRcpntEmailId);
        //this.attachmentName = "Test XLS";
        //this.attachmentType = "application/vnd.ms-excel";
        //fileStream = prepareExcelSheet();
        sendMail();
    }
    
    public void prepareMessageAndSendWithAttachment(String emailId,String subject,String message, List<EmailAttachment> emailAttachmentListVar) throws RsntException
    {
        addressedTo = new ArrayList<EmailRecepient>();
        List<String> emailList = Arrays.asList(emailId.split(","));
        for(String eachEmail: emailList){
        	addressedTo.add(new EmailRecepient("No Reply", eachEmail));
        }        
    	
    	this.addressedFrom = new EmailRecepient("Support Team", AppInitializer.supportEmail);
        this.subject = subject;
        this.messageBody = message;
        this.emailAttachmentList = emailAttachmentListVar;
        
     

        if(!(AppInitializer.bccRcpntEmailId.equalsIgnoreCase("")))
        bccRcpnt = new EmailRecepient("No Reply",AppInitializer.bccRcpntEmailId);
        //this.attachmentName = "Test XLS";
        //this.attachmentType = "application/vnd.ms-excel";
        //fileStream = prepareExcelSheet();
        sendMail();
    }
    
    public void prepareMessageAndSendWithAttachmentCC(String toEmailId, String ccEmailId, String subject,String message, List<EmailAttachment> emailAttachmentListVar) throws RsntException
    {
        addressedTo = new ArrayList<EmailRecepient>();
        List<String> emailList = Arrays.asList(toEmailId.split(","));
        for(String eachEmail: emailList){
        	addressedTo.add(new EmailRecepient("No Reply", eachEmail));
        }      
        
        addressCC = new ArrayList<EmailRecepient>();
        List<String> emailListCC = Arrays.asList(ccEmailId.split(","));

        for(String eachEmail: emailListCC){
        	addressCC.add(new EmailRecepient("No Reply", eachEmail));
        } 
        
    	
    	this.addressedFrom = new EmailRecepient("Support Team", AppInitializer.supportEmail);
        this.subject = subject;
        this.messageBody = message;
        this.emailAttachmentList = emailAttachmentListVar;
        
     

        if(!(AppInitializer.bccRcpntEmailId.equalsIgnoreCase("")))
        bccRcpnt = new EmailRecepient("No Reply",AppInitializer.bccRcpntEmailId);
        //this.attachmentName = "Test XLS";
        //this.attachmentType = "application/vnd.ms-excel";
        //fileStream = prepareExcelSheet();
        sendMail();
    }

    public void prepareMessageAndSendWithPdfAttachment(String emailId,String subject,String message, EmailAttachment pdfAttachmentVar) throws RsntException
    {
        addressedTo = new ArrayList<EmailRecepient>();
        List<String> emailList = Arrays.asList(emailId.split(","));
        for(String eachEmail: emailList){
        	addressedTo.add(new EmailRecepient("No Reply", eachEmail));
        }        
    	
    	this.addressedFrom = new EmailRecepient("Support Team", AppInitializer.supportEmail);
        this.subject = subject;
        this.messageBody = message;
        this.pdfAttachment = pdfAttachmentVar;
        
     

        if(!(AppInitializer.bccRcpntEmailId.equalsIgnoreCase("")))
        bccRcpnt = new EmailRecepient("No Reply",AppInitializer.bccRcpntEmailId);
        //this.attachmentName = "Test XLS";
        //this.attachmentType = "application/vnd.ms-excel";
        //fileStream = prepareExcelSheet();
        sendMail();
    }

    
   /* public void prepareTestMailAndSend() {
    	System.out.println("Sending Test Mail");
    	addressedTo = new ArrayList<EmailRecepient>();
    	addressedTo.add(new EmailRecepient("Rohit", "Rohit_Mehta@spe.sony.com"));
    	addressedTo.add(new EmailRecepient("Adam", "Tarshis_Adam@spe.sony.com"));
        this.addressedFrom = new EmailRecepient("RPM Support Team", "rpmSupport@spe.sony.com");
        this.subject = "Welcome To RPM";
        this.messageBody = "<p>Welcome to RPM. <br/> This is the test mail. Please do not reply to it. <br/><br/>. Thanks<br/>RPM Support Team";
        //this.attachmentName = "Test XLS";
        //this.attachmentType = "application/vnd.ms-excel";
        //fileStream = prepareExcelSheet();
        sendMail();
    }*/
    
  /*  public void prepareAvailsRunMailAndSend(List<EmailRecepient> addressedTo, String availsSessionName, long availsSessionId, InputStream xlsFileStream){
        this.addressedTo = addressedTo;
        this.addressedFrom = new EmailRecepient("RPM Support Team", "rpmSupport@spe.sony.com");
        this.subject = "Avails Report " + availsSessionName;
        this.messageBody = "<p>Welcome to RPM. <br/> Please find attached the results of the avails Run. <br/><br/>. Thanks<br/>RPM Support Team";
        this.attachmentName = availsSessionName + "_" + availsSessionId+".xls";
        this.attachmentType = "application/vnd.ms-excel";
        fileStream = xlsFileStream;
        sendMail();
    }*/
    
    /*public void prepareDefectReportMailAndSend(final Long logDefectId, final String errorDesc, final InputStream attachementIStream,
            String attachmentType, final String attachementName, final SecurityUser loginUser) {

        String sendToEmails = CommonUtil.isNullOrEmpty(AppInitializer.RPM_PROPERTIES_MAP
                    .get(RPMConstants.RPMPropsKey.SEND_TO_DEFECT_REPORT)) ? "Adam_Tarshis@spe.sony.com"
                    : AppInitializer.RPM_PROPERTIES_MAP.get(RPMConstants.RPMPropsKey.SEND_TO_DEFECT_REPORT);
        this.addressedTo = new ArrayList<EmailRecepient>();
        for (String toAdd : sendToEmails.split(";")) {
            addressedTo.add(new EmailRecepient("", toAdd));
        }
        
        final String fromAddress = CommonUtil.isNullOrEmpty(loginUser.getEmailAddress()) ? "rpmSupport@spe.sony.com"
                : loginUser.getEmailAddress();

        this.addressedFrom = new EmailRecepient("RPM Support", fromAddress);
        this.subject = "New Defect Reported";
        String msgBody = "Deal Team, <br/><br/>";
        msgBody = "Logged Defect ID : " + logDefectId + "<br/>";
        msgBody = msgBody + "<p>Defect Description : <br/> " + errorDesc + "<br/><br/></p>"
                + "Thanks<br/>" + loginUser.getFirstName() + " " + loginUser.getLastName();
        this.messageBody = msgBody;
        if (!CommonUtil.isNullOrEmpty(attachementIStream)) {
            this.attachmentType = CommonUtil.getContentTypeForFileExtenstion(attachmentType);
            this.attachmentName = attachementName + "." + attachmentType;
            this.fileStream = attachementIStream;
        }
        sendMail();
    }*/

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

   /* public InputStream prepareExcelSheet() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {

            Workbook workBook = new HSSFWorkbook();
            workBook.createSheet("Demo");
            workBook.write(out);
            InputStream in = new ByteArrayInputStream(out.toByteArray());
            return in;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage(),e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
        }
        return null;
    }*/

    private boolean sendMail() throws RsntException{
        try {
            renderer.render("/views/emailTemplate.xhtml");
        } catch (final Exception e) {
        	e.printStackTrace();
            log.error(e.getMessage(),e);
            throw new RsntException(e);
        } finally {
            if (fileStream != null) {
                try {
                    fileStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                    throw new RsntException(e);
                }
            }
        }
        return true;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public InputStream getFileStream() {
        return fileStream;
    }

    public void setFileStream(InputStream fileStream) {
        this.fileStream = fileStream;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public List<EmailRecepient> getAddressedTo() {
        return addressedTo;
    }

    public void setAddressedTo(List<EmailRecepient> addressedTo) {
        this.addressedTo = addressedTo;
    }
    
    public List<EmailRecepient> getAddressCC() {
        return addressCC;
    }

    public void setAddressCC(List<EmailRecepient> addressCC) {
        this.addressCC = addressCC;
    }

    public EmailRecepient getAddressedFrom() {
        return addressedFrom;
    }

    public void setAddressedFrom(EmailRecepient addressedFrom) {
        this.addressedFrom = addressedFrom;
    }

	public EmailRecepient getBccRcpnt() {
		return bccRcpnt;
	}

	public void setBccRcpnt(EmailRecepient bccRcpnt) {
		this.bccRcpnt = bccRcpnt;
	}

	public List<EmailAttachment> getEmailAttachmentList() {
		return emailAttachmentList;
	}

	public void setEmailAttachmentList(List<EmailAttachment> emailAttachmentList) {
		this.emailAttachmentList = emailAttachmentList;
	}

	public EmailAttachment getPdfAttachment() {
		return pdfAttachment;
	}

	public void setPdfAttachment(EmailAttachment pdfAttachment) {
		this.pdfAttachment = pdfAttachment;
	}


}
