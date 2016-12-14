package com.rsnt.util.transferobject;

import java.io.Serializable;

public class FeedBackLineItemTO implements Serializable {
	  private static final long serialVersionUID = -2728260761770118957L;
	  
	private String questionText;
	
	private String optionTextType;
	
	private String optionTextVal;

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public String getOptionTextType() {
		return optionTextType;
	}

	public void setOptionTextType(String optionTextType) {
		this.optionTextType = optionTextType;
	}

	public String getOptionTextVal() {
		return optionTextVal;
	}

	public void setOptionTextVal(String optionTextVal) {
		this.optionTextVal = optionTextVal;
	}
}
