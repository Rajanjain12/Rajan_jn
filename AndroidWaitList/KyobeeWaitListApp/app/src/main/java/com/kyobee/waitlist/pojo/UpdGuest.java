package com.kyobee.waitlist.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpdGuest{
    @SerializedName("incompleteParty")
    @Expose
    private int incompleteParty;
    @SerializedName("prefType")
    @Expose
    private String prefType;
    @SerializedName("guestID")
    @Expose
    private int guestID;
    @SerializedName("seatingPreference")
    @Expose
    private String seatingPreference;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("createdTime")
    @Expose
    private Object createdTime;
    @SerializedName("organizationID")
    @Expose
    private int organizationID;
    @SerializedName("optin")
    @Expose
    private boolean optin;
    @SerializedName("sms")
    @Expose
    private String sms;
    @SerializedName("seatedTime")
    @Expose
    private Object seatedTime;
    @SerializedName("rank")
    @Expose
    private int rank;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("deviceType")
    @Expose
    private String deviceType;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("noOfPeople")
    @Expose
    private int noOfPeople;
    @SerializedName("calloutCount")
    @Expose
    private int calloutCount;
    @SerializedName("checkinTime")
    @Expose
    private Object checkinTime;
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("resetTime")
    @Expose
    private Object resetTime;
    @SerializedName("deviceId")
    @Expose
    private String deviceId;
    /*@SerializedName("updatedTime")
    @Expose
    private UpdatedTime updatedTime;*/
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("guestPreferences")
    @Expose
    private List<Object> guestPreferences = null;

    public int getIncompleteParty() {
        return incompleteParty;
    }

    public void setIncompleteParty(int incompleteParty) {
        this.incompleteParty = incompleteParty;
    }

    public String getPrefType() {
        return prefType;
    }

    public void setPrefType(String prefType) {
        this.prefType = prefType;
    }

    public int getGuestID() {
        return guestID;
    }

    public void setGuestID(int guestID) {
        this.guestID = guestID;
    }

    public String getSeatingPreference() {
        return seatingPreference;
    }

    public void setSeatingPreference(String seatingPreference) {
        this.seatingPreference = seatingPreference;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Object createdTime) {
        this.createdTime = createdTime;
    }

    public int getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(int organizationID) {
        this.organizationID = organizationID;
    }

    public boolean isOptin() {
        return optin;
    }

    public void setOptin(boolean optin) {
        this.optin = optin;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public Object getSeatedTime() {
        return seatedTime;
    }

    public void setSeatedTime(Object seatedTime) {
        this.seatedTime = seatedTime;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNoOfPeople() {
        return noOfPeople;
    }

    public void setNoOfPeople(int noOfPeople) {
        this.noOfPeople = noOfPeople;
    }

    public int getCalloutCount() {
        return calloutCount;
    }

    public void setCalloutCount(int calloutCount) {
        this.calloutCount = calloutCount;
    }

    public Object getCheckinTime() {
        return checkinTime;
    }

    public void setCheckinTime(Object checkinTime) {
        this.checkinTime = checkinTime;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Object getResetTime() {
        return resetTime;
    }

    public void setResetTime(Object resetTime) {
        this.resetTime = resetTime;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /*public UpdatedTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(UpdatedTime updatedTime) {
        this.updatedTime = updatedTime;
    }*/

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Object> getGuestPreferences() {
        return guestPreferences;
    }

    public void setGuestPreferences(List<Object> guestPreferences) {
        this.guestPreferences = guestPreferences;
    }
}
