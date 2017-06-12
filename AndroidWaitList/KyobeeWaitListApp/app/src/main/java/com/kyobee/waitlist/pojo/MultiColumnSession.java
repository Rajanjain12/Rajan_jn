package com.kyobee.waitlist.pojo;

/**
 * Created on 4/3/2017.
 */

public class MultiColumnSession{

    int totalColumn=3;
    int totalRows=25;
    int rowPosition=1;

    String columnName1="APPROX. 30 MIN. OR LESS";
    String columnName2="APPROX. 60 MIN. OR LESS";
    String columnName3="APPROX. 90 MIN. OR LESS";
    String columnName4="APPROX. 120 MIN. OR LESS";

    boolean notPresent=true;
    boolean inComplete=true;
    boolean party=false;


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

    public boolean isParty (){
        return party;
    }

    public void setParty (boolean party){
        this.party = party;
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

    public String getColumnName4 (){
        return columnName4;
    }

    public void setColumnName4 (String columnName4){
        this.columnName4 = columnName4;
    }

    public boolean isNotPresent (){
        return notPresent;
    }

    public void setNotPresent (boolean notPresent){
        this.notPresent = notPresent;
    }

    public boolean isInComplete (){
        return inComplete;
    }

    public void setInComplete (boolean inComplete){
        this.inComplete = inComplete;
    }

    public int getRowPosition (){
        return rowPosition;
    }

    public void setRowPosition (int rowPosition){
        this.rowPosition = rowPosition;
    }
}
