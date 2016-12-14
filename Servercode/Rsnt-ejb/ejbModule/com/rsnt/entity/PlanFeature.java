package com.rsnt.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.rsnt.common.entity.BaseEntity;

@Table(name="PLANFEATURE")
@Entity(name="PlanFeature")
public class PlanFeature extends BaseEntity implements Comparable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="PlanFeatureId")
	private Long planFeatureId;
	
	@JoinColumn(name="PlanId")
	@ManyToOne(fetch = FetchType.LAZY)
	private Plan plan;

	@Column(name="FeatureId")
	private Long featureId;
	
	@Column(name="ChildFeatureId")
	private Long childFeatureId;

	public Long getPlanFeatureId() {
		return planFeatureId;
	}

	public void setPlanFeatureId(Long planFeatureId) {
		this.planFeatureId = planFeatureId;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public Long getFeatureId() {
		return featureId;
	}

	public void setFeatureId(Long featureId) {
		this.featureId = featureId;
	}
	
	public Long getChildFeatureId() {
		return childFeatureId;
	}

	public void setChildFeatureId(Long childFeatureId) {
		this.childFeatureId = childFeatureId;
	}

	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		if(this.getPlanFeatureId()==null) return 1;
		if(((PlanFeature)arg0).getPlanFeatureId()==null) return 1;
		if(this.getPlanFeatureId().intValue() ==((PlanFeature)arg0).getPlanFeatureId().intValue())
		return 0;
		else
			return 1;
	}
	
/*	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.getPlanFeatureId().intValue() == ((PlanFeature)obj).getPlanFeatureId().intValue();
	}

	public int compareTo(Object obj) {
		if(this.getPlanFeatureId().intValue() == ((PlanFeature)obj).getPlanFeatureId().intValue())
		return 0;
		return 1;
	}*/
	
	
}
