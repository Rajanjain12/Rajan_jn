package com.rsnt.web.transferobject;

import java.io.Serializable;

public class TablePositionTO implements Serializable{
	
	private String tableSeq;
	
	private String tableDataRow;
	
	private String tableDataCol;
	
	private String dataSizex;
	
	private String dataSizey;
	
	private String styleClass;
	
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
