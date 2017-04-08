package com.kyobee.waitlist.pojo;

/**
 * Created on 4/3/2017.
 */

public class MultiColumnSession{

    int totalColumn=-1;
    int totalRows=-1;

    String columnName1="";
    String columnName2="";
    String columnName3="";

    public int getTotalColumn (){
        return totalColumn;
    }

    public void setTotalColumn (int totalColumn){
        this.totalColumn = totalColumn;
    }

    public int getTotalRows (){
        return totalRows;
    }

    public void setTotalRows (int totalRows){
        this.totalRows = totalRows;
    }

    public String getColumnName1 (){
        return columnName1;
    }

    public void setColumnName1 (String columnName1){
        this.columnName1 = columnName1;
    }

    public String getColumnName2 (){
        return columnName2;
    }

    public void setColumnName2 (String columnName2){
        this.columnName2 = columnName2;
    }

    public String getColumnName3 (){
        return columnName3;
    }

    public void setColumnName3 (String columnName3){
        this.columnName3 = columnName3;
    }
}
