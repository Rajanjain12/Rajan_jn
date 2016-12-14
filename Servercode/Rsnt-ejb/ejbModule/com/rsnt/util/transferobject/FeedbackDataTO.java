package com.rsnt.util.transferobject;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.jboss.seam.annotations.Name;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Name("feedbackDataTO")
public class FeedbackDataTO implements Serializable{

	@JsonProperty
	private String questionText;
	
	@JsonProperty
	private String optionTextType;
	
	@JsonProperty
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
