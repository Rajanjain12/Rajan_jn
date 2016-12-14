package com.rsnt.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PostPersist;
import javax.persistence.Table;

import org.jboss.seam.annotations.Name;

import com.rsnt.common.entity.BaseEntity;
import com.rsnt.util.common.AppInitializer;


@Entity
@Name("adsImage")
@Table(name="ADSIMAGE")
@NamedQueries({@NamedQuery(name=AdsImage.GET_BANNER_AD,query="Delete FROM AdsImage Adim WHERE Adim.ads.adsId = ?1 and Adim.imageTypeId = 33")
})
		
public class AdsImage extends BaseEntity{
	
	public static final String GET_BANNER_AD= "getBannerAd";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="AdsImageID")
	private Long adsImageId;
	
	@JoinColumn(name="AdsID")
	@ManyToOne(fetch=FetchType.LAZY)
	private Ads ads;
	
	@Column(name="Image")
	private String image;
	
	@Column(name="ImageExt")
	private String imageExt;
	
	@Column(name="ImageTypeID")
	private Long imageTypeId;

	public Long getAdsImageId() {
		return adsImageId;
	}

	public void setAdsImageId(Long adsImageId) {
		this.adsImageId = adsImageId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	
	public Ads getAds() {
		return ads;
	}

	public void setAds(Ads ads) {
		this.ads = ads;
	}
	
	@PostPersist
    protected void postCreate() {
		image = AppInitializer.adImagePath+this.getAdsImageId()+"."+this.getImageExt();
    }

	public Long getImageTypeId() {
		return imageTypeId;
	}

	public void setImageTypeId(Long imageTypeId) {
		this.imageTypeId = imageTypeId;
	}

	public String getImageExt() {
		return imageExt;
	}

	public void setImageExt(String imageExt) {
		this.imageExt = imageExt;
	}

	
}
