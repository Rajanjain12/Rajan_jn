package com.rsnt.util.transferobject;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.jboss.seam.annotations.Name;

@XmlRootElement
@Name("feedbackWrapperTO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeedbackWrapperTO  implements Serializable {

	private static final long serialVersionUID = 7303552651034276308L;
	
	@JsonProperty
    private Long columnId;


    @JsonProperty
    private String columnName;
    
    @JsonProperty
    private List<FeedBackLineItemTO> feedbackLineItemTOList;

	public Long getColumnId() {
		return columnId;
	}

	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}


	public List<FeedBackLineItemTO> getFeedbackLineItemTOList() {
		return feedbackLineItemTOList;
	}

	public void setFeedbackLineItemTOList(
			List<FeedBackLineItemTO> feedbackLineItemTOList) {
		this.feedbackLineItemTOList = feedbackLineItemTOList;
	}
    
    
}
