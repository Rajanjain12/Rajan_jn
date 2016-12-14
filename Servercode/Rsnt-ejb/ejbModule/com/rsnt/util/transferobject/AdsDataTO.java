package com.rsnt.util.transferobject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.jboss.seam.annotations.Name;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Name("adsDataTO")
public class AdsDataTO  implements Serializable{

	@JsonProperty
	private String adsId;
	
	@JsonProperty
	private String description;
	
	
	@JsonProperty
	private String title;
	
	@JsonProperty
	private String imageUrl;
	
	@JsonProperty
	private String originalPrice;
	
	@JsonProperty
	private String discountPrice;
	
	@JsonProperty
	private String active;
	
	@JsonProperty
	private String weekdayRun;
	
	@JsonProperty
	private String startDate;
	
	@JsonProperty
	private String startTime;
	
	@JsonProperty
	private String endDate;
	
	@JsonProperty
	private String endTime;
	
	@JsonProperty
	private String tillCreditAvailable;
	
	@JsonProperty
	private Long access;
	
	@JsonProperty
	private String adBannerImage;
	
	
	
	@JsonProperty("imageData")
	private List<AdsImageTO> adsImageTOList = new ArrayList<AdsImageTO>();;

	public String getAdsId() {
		return adsId;
	}

	public void setAdsId(String adsId) {
		this.adsId = adsId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public List<AdsImageTO> getAdsImageTOList() {
		return adsImageTOList;
	}

	public void setAdsImageTOList(List<AdsImageTO> adsImageTOList) {
		this.adsImageTOList = adsImageTOList;
	}

	public String getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}

	public String getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(String discountPrice) {
		this.discountPrice = discountPrice;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getWeekdayRun() {
		return weekdayRun;
	}

	public void setWeekdayRun(String weekdayRun) {
		this.weekdayRun = weekdayRun;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getTillCreditAvailable() {
		return tillCreditAvailable;
	}

	public void setTillCreditAvailable(String tillCreditAvailable) {
		this.tillCreditAvailable = tillCreditAvailable;
	}

	public Long getAccess() {
		return access;
	}

	public void setAccess(Long access) {
		this.access = access;
	}

	public String getAdBannerImage() {
		return adBannerImage;
	}

	public void setAdBannerImage(String adBannerImage) {
		this.adBannerImage = adBannerImage;
	}


	
	
}
