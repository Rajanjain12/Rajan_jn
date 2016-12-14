package com.rsnt.util.transferobject;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.jboss.seam.annotations.Name;
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Name("mapplicCatagoryDataTO")

public class MapplicCatagryDataTO implements Serializable{
	
	
	
	@JsonProperty
	private String id;
	
	@JsonProperty
	private String title;
	
	@JsonProperty
	private String color;
	
	@JsonProperty
	private String show;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getShow() {
		return show;
	}

	public void setShow(String show) {
		this.show = show;
	}
}
