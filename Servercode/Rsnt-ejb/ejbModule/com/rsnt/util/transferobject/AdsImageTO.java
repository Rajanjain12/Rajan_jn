package com.rsnt.util.transferobject;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;


public class AdsImageTO implements Serializable {

	@JsonProperty
	private String adsImageId;
	
	@JsonProperty
	private String image;
	
	@JsonProperty
	private String imageTypeId;

	public String getAdsImageId() {
		return adsImageId;
	}

	public void setAdsImageId(String adsImageId) {
		this.adsImageId = adsImageId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImageTypeId() {
		return imageTypeId;
	}

	public void setImageTypeId(String imageTypeId) {
		this.imageTypeId = imageTypeId;
	}
}
