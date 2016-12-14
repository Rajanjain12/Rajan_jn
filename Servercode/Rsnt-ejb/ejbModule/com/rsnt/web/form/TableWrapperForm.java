package com.rsnt.web.form;

import java.io.Serializable;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.jboss.seam.annotations.Name;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@Name("tableWrapperForm")
public class TableWrapperForm implements Serializable{
	
	@FormParam("test")
	private String test;
	
	@FormParam("tablePositionForm[]")
	private List<TablePositionForm> tablePositionForm;

	

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public List<TablePositionForm> getTablePositionForm() {
		return tablePositionForm;
	}

	public void setTablePositionForm(List<TablePositionForm> tablePositionForm) {
		this.tablePositionForm = tablePositionForm;
	}

	
	
	

}
