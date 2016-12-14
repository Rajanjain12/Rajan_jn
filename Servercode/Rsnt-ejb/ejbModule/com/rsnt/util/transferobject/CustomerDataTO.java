package com.rsnt.util.transferobject;

import java.io.Serializable;

public class CustomerDataTO implements Serializable {
	 private static final long serialVersionUID = -2798260761770118907L;
	 
	 private String firstName;
	 private String contactNumber;
	 private String email;
	 private String feedbackNote;
	 private String feedbackRating;
	 private String optIn;
	 
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
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
	public String getFeedbackRating() {
		return feedbackRating;
	}
	public void setFeedbackRating(String feedbackRating) {
		this.feedbackRating = feedbackRating;
	}
	public String getOptIn() {
		return optIn;
	}
	public void setOptIn(String optIn) {
		this.optIn = optIn;
	}
	 

}
