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

import org.jboss.seam.annotations.Name;

import com.rsnt.common.entity.BaseEntity;

@Entity
@Name("feedbackQuestionaireResponseDetail")
@Table(name="FEEDBACKQUESTIONAIRERESPONSEDETAIL")
public class FeedbackQuestionaireResponseDetail  extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="FeedbackQuestionaireResponseDetailID")
	private Long feedbackQuestionaireResponseDetailId;
	
	
	@JoinColumn(name="FeedbackQuestionaireResponseID")
	@ManyToOne(fetch=FetchType.LAZY)
	private FeedbackQuestionaireResponse feedbackQuestionaireResponse;
	
	@Column(name="QuestionTypeID")
	private Long questionTypeId;
	
	@Column(name="QuestionText")
	private String questionText;
	
	@Column(name="OptionText")
	private String optionText;
	
	@Column(name="ReplyText")
	private String replyText;

	public Long getFeedbackQuestionaireResponseDetailId() {
		return feedbackQuestionaireResponseDetailId;
	}

	public void setFeedbackQuestionaireResponseDetailId(
			Long feedbackQuestionaireResponseDetailId) {
		this.feedbackQuestionaireResponseDetailId = feedbackQuestionaireResponseDetailId;
	}

	public FeedbackQuestionaireResponse getFeedbackQuestionaireResponse() {
		return feedbackQuestionaireResponse;
	}

	public void setFeedbackQuestionaireResponse(
			FeedbackQuestionaireResponse feedbackQuestionaireResponse) {
		this.feedbackQuestionaireResponse = feedbackQuestionaireResponse;
	}

	public Long getQuestionTypeId() {
		return questionTypeId;
	}

	public void setQuestionTypeId(Long questionTypeId) {
		this.questionTypeId = questionTypeId;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public String getOptionText() {
		return optionText;
	}

	public void setOptionText(String optionText) {
		this.optionText = optionText;
	}

	public String getReplyText() {
		return replyText;
	}

	public void setReplyText(String replyText) {
		this.replyText = replyText;
	}
	
	
}
