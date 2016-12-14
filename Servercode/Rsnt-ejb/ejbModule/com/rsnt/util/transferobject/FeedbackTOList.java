package com.rsnt.util.transferobject;

import java.io.Serializable;
import java.util.List;

public class FeedbackTOList implements Serializable {
	 private static final long serialVersionUID = -2728260761770118907L;
	 
	  private List<FeedbackTO> feedbackList;
	  
	  private CustomerDataTO customerData;
	  
	  

	public List<FeedbackTO> getFeedbackList() {
		return feedbackList;
	}

	public void setFeedbackList(List<FeedbackTO> feedbackList) {
		this.feedbackList = feedbackList;
	}

	public CustomerDataTO getCustomerData() {
		return customerData;
	}

	public void setCustomerData(CustomerDataTO customerData) {
		this.customerData = customerData;
	}

}
