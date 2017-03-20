package com.kyobee.waitlist.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Record{
    @SerializedName("guestID")
    @Expose
    private int guestID;
    @SerializedName("organizationID")
    @Expose
    private int organizationID;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("noOfPeople")
    @Expose
    private int noOfPeople;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("sms")
    @Expose
    private String sms;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("rank")
    @Expose
    private int rank;
    @SerializedName("prefType")
    @Expose
    private String prefType;
    @SerializedName("guestPreferences")
    @Expose
    private Object guestPreferences;
    @SerializedName("optin")
    @Expose
    private Boolean optin;
    @SerializedName("calloutCount")
    @Expose
    private String calloutCount;
    @SerializedName("checkinTime")
    @Expose
    private Object checkinTime;
    @SerializedName("seatedTime")
    @Expose
    private Object seatedTime;
    @SerializedName("createdTime")
    @Expose
    private Object createdTime;
    @SerializedName("updatedTime")
    @Expose
    private String updatedTime;
    @SerializedName("incompleteParty")
    @Expose
    private String incompleteParty;
    @SerializedName("seatingPreference")
    @Expose
    private String seatingPreference;
    @SerializedName("deviceType")
    @Expose
    private Object deviceType;
    @SerializedName("deviceId")
    @Expose
    private Object deviceId;

    public Record (int guestID, String name, String status){
        this.guestID = guestID;
        this.name = name;
        this.status = status;
    }

    public int getGuestID (){
        return guestID;
    }

    public void setGuestID (int guestID){
        this.guestID = guestID;
    }

    public int getOrganizationID (){
        return organizationID;
    }

    public void setOrganizationID (int organizationID){
        this.organizationID = organizationID;
    }

    public String getName (){
        return name;
    }

    public void setName (String name){
        this.name = name;
    }

    public String getNote (){
        return note;
    }

    public void setNote (String note){
        this.note = note;
    }

    public String getUuid (){
        return uuid;
    }

    public void setUuid (String uuid){
        this.uuid = uuid;
    }

    public int getNoOfPeople (){
        return noOfPeople;
    }

    public void setNoOfPeople (int noOfPeople){
        this.noOfPeople = noOfPeople;
    }

    public String getEmail (){
        return email;
    }

    public void setEmail (String email){
        this.email = email;
    }

    public String getSms (){
        return sms;
    }

    public void setSms (String sms){
        this.sms = sms;
    }

    public String getStatus (){
        return status;
    }

    public void setStatus (String status){
        this.status = status;
    }

    public int getRank (){
        return rank;
    }

    public void setRank (int rank){
        this.rank = rank;
    }

    public String getPrefType (){
        return prefType;
    }

    public void setPrefType (String prefType){
        this.prefType = prefType;
    }

    public Object getGuestPreferences (){
        return guestPreferences;
    }

    public void setGuestPreferences (Object guestPreferences){
        this.guestPreferences = guestPreferences;
    }

    public Boolean getOptin (){
        return optin;
    }

    public void setOptin (Boolean optin){
        this.optin = optin;
    }

    public String getCalloutCount (){
        return calloutCount;
    }

    public void setCalloutCount (String calloutCount){
        this.calloutCount = calloutCount;
    }

    public String getIncompleteParty (){
        return incompleteParty;
    }

    public void setIncompleteParty (String incompleteParty){
        this.incompleteParty = incompleteParty;
    }

    public Object getCheckinTime (){
        return checkinTime;
    }

    public void setCheckinTime (Object checkinTime){
        this.checkinTime = checkinTime;
    }

    public Object getSeatedTime (){
        return seatedTime;
    }

    public void setSeatedTime (Object seatedTime){
        this.seatedTime = seatedTime;
    }

    public Object getCreatedTime (){
        return createdTime;
    }

    public void setCreatedTime (Object createdTime){
        this.createdTime = createdTime;
    }

    public String getUpdatedTime (){
        return updatedTime;
    }

    public void setUpdatedTime (String updatedTime){
        this.updatedTime = updatedTime;
    }

    public String getSeatingPreference (){
        return seatingPreference;
    }

    public void setSeatingPreference (String seatingPreference){
        this.seatingPreference = seatingPreference;
    }

    public Object getDeviceType (){
        return deviceType;
    }

    public void setDeviceType (Object deviceType){
        this.deviceType = deviceType;
    }

    public Object getDeviceId (){
        return deviceId;
    }

    public void setDeviceId (Object deviceId){
        this.deviceId = deviceId;
    }
}
