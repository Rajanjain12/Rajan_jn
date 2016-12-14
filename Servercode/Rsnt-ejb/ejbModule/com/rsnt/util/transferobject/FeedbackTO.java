package com.rsnt.util.transferobject;

import java.io.Serializable;

public class FeedbackTO implements Serializable {
	 private static final long serialVersionUID = -2728260761770118957L;
	
	 private String questionText;
	 
	 private String answerVal;

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public String getAnswerVal() {
		return answerVal;
	}

	public void setAnswerVal(String answerVal) {
		this.answerVal = answerVal;
	}

}
