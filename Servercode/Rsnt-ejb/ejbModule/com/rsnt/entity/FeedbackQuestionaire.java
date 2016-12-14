package com.rsnt.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.rsnt.common.entity.BaseEntity;

@Entity
@Table(name="FEEDBACKQUESTIONAIRE")
@NamedQueries({@NamedQuery(name=FeedbackQuestionaire.GET_FEEDBACK,query="Select Fd FROM FeedbackQuestionaire Fd WHERE Fd.feedbackQuestionaireId = ?1")})
public class FeedbackQuestionaire extends BaseEntity {
	
	public static final String GET_FEEDBACK = "getFeedback";

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="FeedbackQuestionaireID")
	private Long feedbackQuestionaireId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="OrganizationId")
	private Organization organization;
	

	@Column(name="FeedbackQuestionaireTitle")
	private String feedbackQuestionaireTitle;
	
	@Column(columnDefinition = "TINYINT",name="Active", nullable=false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean active;

	public Long getFeedbackQuestionaireId() {
		return feedbackQuestionaireId;
	}

	public void setFeedbackQuestionaireId(Long feedbackQuestionaireId) {
		this.feedbackQuestionaireId = feedbackQuestionaireId;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public String getFeedbackQuestionaireTitle() {
		return feedbackQuestionaireTitle;
	}

	public void setFeedbackQuestionaireTitle(String feedbackQuestionaireTitle) {
		this.feedbackQuestionaireTitle = feedbackQuestionaireTitle;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}