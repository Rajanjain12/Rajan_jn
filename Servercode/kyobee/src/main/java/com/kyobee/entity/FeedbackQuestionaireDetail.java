package com.kyobee.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name="FEEDBACKQUESTIONAIREDETAIL")
@NamedQueries({@NamedQuery(name=FeedbackQuestionaireDetail.GET_FEEDBACK_DETAIL,query="Select Fd FROM FeedbackQuestionaireDetail Fd WHERE Fd.feedbackQuestionaireDetailId = ?1")})
public class FeedbackQuestionaireDetail extends BaseEntity{

	public static final String GET_FEEDBACK_DETAIL = "getFeedbackDetail";
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="FeedbackQuestionaireDetailID")
	private Long feedbackQuestionaireDetailId;
	
	@Column(name="FeedbackQuestionaireID")
	private Long feedbackQuestionaireId;
	
	
	@Column(name="QuestionText")
	private String questionText;
	
	@Column(name="OptionText")
	private String optionText;
	
	@Column(name="QuestionTypeID")
	private Long questionTypeId;

	@Column(columnDefinition = "TINYINT",name="Active", nullable=false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean active;

	
	public Long getFeedbackQuestionaireDetailId() {
		return feedbackQuestionaireDetailId;
	}

	public void setFeedbackQuestionaireDetailId(Long feedbackQuestionaireDetailId) {
		this.feedbackQuestionaireDetailId = feedbackQuestionaireDetailId;
	}

	public Long getFeedbackQuestionaireId() {
		return feedbackQuestionaireId;
	}

	public void setFeedbackQuestionaireId(Long feedbackQuestionaireId) {
		this.feedbackQuestionaireId = feedbackQuestionaireId;
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

	public Long getQuestionTypeId() {
		return questionTypeId;
	}

	public void setQuestionTypeId(Long questionTypeId) {
		this.questionTypeId = questionTypeId;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
}
