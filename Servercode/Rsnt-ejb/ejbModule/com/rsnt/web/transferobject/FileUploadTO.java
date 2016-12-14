package com.rsnt.web.transferobject;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import com.rsnt.entity.OrganizationLayoutMarkers;
public class FileUploadTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	 
	private String adImageId;
	private String adImageUrl;
	private String Name;
    private String mime;
    private long length;
    private byte[] data;
    private Long imageTypeId;
    
    private BufferedImage imageOut;
    
    
    public byte[] getData() {
        return data;
    }
    public void setData(byte[] data) {
        this.data = data;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
        int extDot = name.lastIndexOf('.');
        if(extDot > 0){
            String extension = name.substring(extDot +1);
            if("bmp".equals(extension)){
                mime="image/bmp";
            } else if("jpg".equals(extension)){
                mime="image/jpeg";
            } else if("gif".equals(extension)){
                mime="image/gif";
            } else if("png".equals(extension)){
                mime="image/png";
            } else {
                mime = "image/unknown";
            }
        }
    }
    public long getLength() {
        return length;
    }
    public void setLength(long length) {
        this.length = length;
    }
    
    public String getMime(){
        return mime;
    }
	public BufferedImage getImageOut() {
		return imageOut;
	}
	public void setImageOut(BufferedImage imageOut) {
		this.imageOut = imageOut;
	}
	public Long getImageTypeId() {
		return imageTypeId;
	}
	public void setImageTypeId(Long imageTypeId) {
		this.imageTypeId = imageTypeId;
	}
	public String getAdImageId() {
		return adImageId;
	}
	public void setAdImageId(String adImageId) {
		this.adImageId = adImageId;
	}
	public String getAdImageUrl() {
		return adImageUrl;
	}
	public void setAdImageUrl(String adImageUrl) {
		this.adImageUrl = adImageUrl;
	}
	
	@Override
	public boolean equals(Object obj){
		return this.getAdImageId().equals(((FileUploadTO)obj).getAdImageId());
	}
	
	@Override
	public int hashCode() {
		return this.getAdImageId().hashCode();
	
		
	}
	public void setMime(String mime) {
		this.mime = mime;
	}
}
