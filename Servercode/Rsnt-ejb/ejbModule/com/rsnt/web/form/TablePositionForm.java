package com.rsnt.web.form;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.FormParam;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.jboss.seam.annotations.Name;

import com.rsnt.util.common.CommonUtil;


@JsonIgnoreProperties(ignoreUnknown = true)
@Name("tablePositionForm")
public class TablePositionForm implements Serializable{
	
	public TablePositionForm(List test){
		System.out.println(test);
	};
	
	public TablePositionForm(String test){
		try{
			if(test.startsWith("{"))
        	{
        		StringBuffer newData = new StringBuffer(test);
        		newData.append("]");
        		newData.insert(0, "[");
        		test = newData.toString();
        	}
			
			ObjectMapper mapper = new ObjectMapper(); 
	   		TypeReference<List<HashMap<String, String>>> typeRef = new TypeReference<List<HashMap<String,String> >>() {}; 
	   		List<HashMap<String, String>> updateData = mapper.readValue(test, typeRef);
	   		System.out.println(updateData);
	   		
	   		Map testMap = updateData.get(0);

	   		
	   		this.setTableSeq(testMap.get("dataSizex").toString());
	   		this.setTableDataRow(testMap.get("dataSizex").toString());
	   		this.setTableDataCol(testMap.get("dataSizex").toString());
	   		this.setDataSizex(testMap.get("dataSizex").toString());
	   		this.setDataSizey(testMap.get("dataSizey").toString());
	   		this.setStyleClass(CommonUtil.toStr(testMap.get("styeClass")).toString());
	   		this.setStylePosition(CommonUtil.toStr(testMap.get("stylePosition")).toString());
	   		
		}catch(Exception e){
			e.printStackTrace();
		}
   		
	}
	
	@FormParam("tablePositionForm[id]")
	private String tableSeq;
	
	@FormParam("tablePositionForm[dataRow]")
	private String tableDataRow;
	
	@FormParam("tablePositionForm[dataCol]")
	private String tableDataCol;
	
	@FormParam("tablePositionForm[dataSizex]")
	private String dataSizex;
	
	@FormParam("tablePositionForm[dataSizey]")
	private String dataSizey;
	
	@FormParam("tablePositionForm[styleClass]")
	private String styleClass;
	
	@FormParam("tablePositionForm[stylePos]")
	private String stylePosition;
	
	
	
	public String getTableSeq() {
		return tableSeq;
	}
	public void setTableSeq(String tableSeq) {
		this.tableSeq = tableSeq;
	}
	public String getTableDataRow() {
		return tableDataRow;
	}
	public void setTableDataRow(String tableDataRow) {
		this.tableDataRow = tableDataRow;
	}
	public String getTableDataCol() {
		return tableDataCol;
	}
	public void setTableDataCol(String tableDataCol) {
		this.tableDataCol = tableDataCol;
	}
	public String getDataSizex() {
		return dataSizex;
	}
	public void setDataSizex(String dataSizex) {
		this.dataSizex = dataSizex;
	}
	public String getDataSizey() {
		return dataSizey;
	}
	public void setDataSizey(String dataSizey) {
		this.dataSizey = dataSizey;
	}
	public String getStyleClass() {
		return styleClass;
	}
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
	public String getStylePosition() {
		return stylePosition;
	}
	public void setStylePosition(String stylePosition) {
		this.stylePosition = stylePosition;
	}
	

}
