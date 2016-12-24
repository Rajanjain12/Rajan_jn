package com.kyobee.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
@Entity
@Table(name="FEEDBACKQUESTIONAIRERESPONSE")
public class FeedbackQuestionaireResponse extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="FeedbackQuestionaireResponseID")
	private Long feedbackQuestionaireResponseId;

	@Column(name="FeedbackQuestionaireID")
	private Long feedbackQuestionaireId;
	
	@OneToOne(mappedBy="feedbackQuestionaireResponse")
	private Feedback feedback;

	
	@OneToMany(mappedBy="feedbackQuestionaireResponse",fetch = FetchType.LAZY)
	@org.hibernate.annotations.Cascade({ org.hibernate.annotations.CascadeType.ALL,
             org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	private List<FeedbackQuestionaireResponseDetail> feedbackQuestionaireResponseDetailList = new ArrayList<FeedbackQuestionaireResponseDetail>();;

	public Long getFeedbackQuestionaireResponseId() {
		return feedbackQuestionaireResponseId;
	}

	public void setFeedbackQuestionaireResponseId(
			Long feedbackQuestionaireResponseId) {
		this.feedbackQuestionaireResponseId = feedbackQuestionaireResponseId;
	}

	public Long getFeedbackQuestionaireId() {
		return feedbackQuestionaireId;
	}

	public void setFeedbackQuestionaireId(Long feedbackQuestionaireId) {
		this.feedbackQuestionaireId = feedbackQuestionaireId;
	}

	public List<FeedbackQuestionaireResponseDetail> getFeedbackQuestionaireResponseDetailList() {
		return feedbackQuestionaireResponseDetailList;
	}

	public void setFeedbackQuestionaireResponseDetailList(
			List<FeedbackQuestionaireResponseDetail> feedbackQuestionaireResponseDetailList) {
		this.feedbackQuestionaireResponseDetailList = feedbackQuestionaireResponseDetailList;
	}

	public Feedback getFeedback() {
		return feedback;
	}

	public void setFeedback(Feedback feedback) {
		this.feedback = feedback;
	}
}
	
	
