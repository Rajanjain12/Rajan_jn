package com.rsnt.util.transferobject;

import java.io.InputStream;
import java.io.Serializable;

public class EmailAttachment implements Serializable{
	
	 private InputStream image;
	 
	 private String fileName;
	 
	 public EmailAttachment(){
		 
	 }
	 public EmailAttachment(InputStream is, String pFilename){
		 this.image = is;
		 this.fileName = pFilename;
	 }

	public InputStream getImage() {
		return image;
	}

	public void setImage(InputStream image) {
		this.image = image;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
