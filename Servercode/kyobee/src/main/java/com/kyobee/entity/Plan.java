package com.kyobee.entity;

import java.util.List;
import java.util.Set;

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

@Entity(name="Plan")
@Table(name="PLAN")
@NamedQueries({
	@NamedQuery(name=Plan.GET_PLAN_ENITY, query="Select p from Plan p where p.planId=?1"),
	@NamedQuery(name=Plan.GET_PLAN_DETAIL,query="Select p from Plan p left join fetch p.planPriceList planPriceList left join fetch p.planFeatureSet planFeatureSet where p.planId=?1 "),
	@NamedQuery(name=Plan.GET_PLAN_PRICE_DETAIL,query="Select p from Plan p left join fetch p.planPriceList planPriceList where p.planId=?1 "),
	@NamedQuery(name=Plan.GET_DUPLICATE_PLAN,query="Select p from Plan p where lower(p.planName)=?1 ")
})
public class Plan extends BaseEntity{
	
	public static final String GET_PLAN_ENITY = "getPlanEntity";

	public static final String GET_PLAN_DETAIL= "getPlanDetail";
	
	public static final String GET_PLAN_PRICE_DETAIL= "getPlanPriceDetail";
	public static final String GET_DUPLICATE_PLAN= "getDuplicatePlan";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="PlanId")
	private Long planId;
	
	@Column(name="PlanName")
	private String planName;
	
	@Column(columnDefinition = "BOOLEAN", nullable = false,name="Active")
	private boolean activeFlag;
	
	@OneToMany(mappedBy="plan", fetch = FetchType.LAZY)
	@org.hibernate.annotations.Cascade({ org.hibernate.annotations.CascadeType.ALL,
             org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	private List<PlanPrice> planPriceList;

	@OneToMany(mappedBy="plan", fetch = FetchType.LAZY)
	@org.hibernate.annotations.Cascade({ org.hibernate.annotations.CascadeType.ALL,
             org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	private Set<PlanFeature> planFeatureSet;
	
	public Long getPlanId() {
		return planId;
	}

	public void setPlanId(Long planId) {
		this.planId = planId;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public boolean isActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(boolean activeFlag) {
		this.activeFlag = activeFlag;
	}

	public List<PlanPrice> getPlanPriceList() {
		return planPriceList;
	}

	public void setPlanPriceList(List<PlanPrice> planPriceList) {
		this.planPriceList = planPriceList;
	}

	public Set<PlanFeature> getPlanFeatureSet() {
		return planFeatureSet;
	}

	public void setPlanFeatureSet(Set<PlanFeature> planFeatureSet) {
		this.planFeatureSet = planFeatureSet;
	}
	

}
