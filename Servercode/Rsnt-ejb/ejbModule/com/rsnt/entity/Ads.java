package com.rsnt.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.jboss.seam.annotations.Name;

import com.rsnt.common.entity.BaseEntity;

@Entity
@Name("ads")
@Table(name="ADS")
@NamedQueries({@NamedQuery(name=Ads.GET_AD_DETAIL,query="Select Ad FROM Ads Ad WHERE Ad.adsId = ?1"),
		@NamedQuery(name=Ads.GET_AD_DETAIL_LIST,query="Select distinct Ad from Ads Ad left join fetch Ad.adsImageList adsImageList where Ad.adsId in ( :pAdIds) ")}
)
		
public class Ads  extends BaseEntity{

	public static final String GET_AD_DETAIL = "getAdDetail";
	
	public static final String GET_AD_DETAIL_LIST = "getAdDetailList";
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="AdsID")
	private Long adsId;
	
	@Column(name="OrganizationId")
	private Long organizationId;
	
	
	@Column(name="Title")
	private String title;

	@Column(name="Description")
	private String description;
	
	
	@Column(name="OriginalPrice")
	private BigDecimal originalPrice;
	
	@Column(name="DiscountPrice")
	private BigDecimal discountPrice;
	
	
	@Column(columnDefinition = "TINYINT",name="active", nullable=false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean activeFlag;


	@Column(name="WeekdayRun")
	private String weekdayRun;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="StartDateTime")
	private Date startDateTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="endDateTime")
	private Date endDateTime;
	
	@Column(name="StartTime")
	private String startTime;
	
	@Column(name="EndTime")
	private String endTime;
	
	@Column(columnDefinition = "TINYINT",name="TillCreditAvailable", nullable=false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean tillCreditAvailable;

/*	
	@OneToOne(mappedBy="ads", fetch=FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REMOVE })
	private AdsImage adsImage;*/
	
	
    @OneToMany(mappedBy="ads",fetch = FetchType.LAZY)
	@org.hibernate.annotations.Cascade({ org.hibernate.annotations.CascadeType.ALL,
             org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    private List<AdsImage> adsImageList = new ArrayList<AdsImage>();
	
	public Long getAdsId() {
		return adsId;
	}


	public void setAdsId(Long adsId) {
		this.adsId = adsId;
	}


	public Long getOrganizationId() {
		return organizationId;
	}


	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isActiveFlag() {
		return activeFlag;
	}


	public void setActiveFlag(boolean activeFlag) {
		this.activeFlag = activeFlag;
	}


	public String getWeekdayRun() {
		return weekdayRun;
	}


	public void setWeekdayRun(String weekdayRun) {
		this.weekdayRun = weekdayRun;
	}


	public Date getStartDateTime() {
		return startDateTime;
	}


	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}


	public Date getEndDateTime() {
		return endDateTime;
	}


	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}


	public boolean isTillCreditAvailable() {
		return tillCreditAvailable;
	}


	public void setTillCreditAvailable(boolean tillCreditAvailable) {
		this.tillCreditAvailable = tillCreditAvailable;
	}



	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}


	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}


	public BigDecimal getDiscountPrice() {
		return discountPrice;
	}


	public void setDiscountPrice(BigDecimal discountPrice) {
		this.discountPrice = discountPrice;
	}


	public List<AdsImage> getAdsImageList() {
		return adsImageList;
	}


	public void setAdsImageList(List<AdsImage> adsImageList) {
		this.adsImageList = adsImageList;
	}


	public void setStartTime(String convertDateToFormattedString) {
		// TODO Auto-generated method stub
		this.startTime = convertDateToFormattedString;
		
	}


	public void setEndTime(String convertDateToFormattedString) {
		// TODO Auto-generated method stub
		this.endTime = convertDateToFormattedString;
		
	}

	public String getStartTime() {
		// TODO Auto-generated method stub
		return startTime;
		
	}


	public String getEndTime() {
		// TODO Auto-generated method stub
		return endTime;
		
	}


	
	
}
