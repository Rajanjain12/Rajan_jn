package com.rsnt.util.transferobject;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.jboss.seam.annotations.Name;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Name("genericReportDTO")
public class GenericReportDTO {
	
	@JsonProperty
	private Long id;
	
	@JsonProperty
	private String description;
	
	@JsonProperty
	private String xAxisValue;
	
	@JsonProperty
	private int yAxisValue;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getxAxisValue() {
		return xAxisValue;
	}

	public void setxAxisValue(String xAxisValue) {
		this.xAxisValue = xAxisValue;
	}

	public int getyAxisValue() {
		return yAxisValue;
	}

	public void setyAxisValue(int yAxisValue) {
		this.yAxisValue = yAxisValue;
	}
	
	

}
