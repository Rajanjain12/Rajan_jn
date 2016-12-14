package com.rsnt.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.jboss.seam.annotations.Name;

import com.rsnt.common.entity.BaseEntity;
@Entity
@Name("feedback")
@Table(name="FEEDBACK")
public class Feedback extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="FeedbackID")
	private Long feedbackId;
	
	@Column(name="OrganizationID")
	private Long organizationId;
	
	@Column(name="OrganizationLayoutMarkerID")
	private Long organizationLayoutMarkerId;
	
	@Column(name="FirstName")
	private String firstName;
	
	@Column(name="ContactNumber")
	private Long contactNumber;
	
	@Column(name="Email")
	private String email;
	
	@Column(name="FeedbackNote")
	private String feedbackNote;
	
	@Column(name="FeedbackRating")
	private BigDecimal feedbackRating;
	
	@Column(columnDefinition = "TINYINT",name="OptIn", nullable=false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean optIn;
	
	
	@JoinColumn(name="FeedbackQuestionaireResponseID")
	@OneToOne(fetch=FetchType.LAZY)
	@org.hibernate.annotations.Cascade({ org.hibernate.annotations.CascadeType.ALL,
             org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	private FeedbackQuestionaireResponse feedbackQuestionaireResponse;


	public Long getFeedbackId() {
		return feedbackId;
	}


	public void setFeedbackId(Long feedbackId) {
		this.feedbackId = feedbackId;
	}


	public Long getOrganizationId() {
		return organizationId;
	}


	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}


	public Long getOrganizationLayoutMarkerId() {
		return organizationLayoutMarkerId;
	}


	public void setOrganizationLayoutMarkerId(Long organizationLayoutMarkerId) {
		this.organizationLayoutMarkerId = organizationLayoutMarkerId;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public Long getContactNumber() {
		return contactNumber;
	}


	public void setContactNumber(Long contactNumber) {
		this.contactNumber = contactNumber;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getFeedbackNote() {
		return feedbackNote;
	}


	public void setFeedbackNote(String feedbackNote) {
		this.feedbackNote = feedbackNote;
	}

	public boolean isOptIn() {
		return optIn;
	}


	public void setOptIn(boolean optIn) {
		this.optIn = optIn;
	}


	public FeedbackQuestionaireResponse getFeedbackQuestionaireResponse() {
		return feedbackQuestionaireResponse;
	}


	public void setFeedbackQuestionaireResponse(
			FeedbackQuestionaireResponse feedbackQuestionaireResponse) {
		this.feedbackQuestionaireResponse = feedbackQuestionaireResponse;
	}


	public BigDecimal getFeedbackRating() {
		return feedbackRating;
	}


	public void setFeedbackRating(BigDecimal feedbackRating) {
		this.feedbackRating = feedbackRating;
	}
	
	
	

	
}
