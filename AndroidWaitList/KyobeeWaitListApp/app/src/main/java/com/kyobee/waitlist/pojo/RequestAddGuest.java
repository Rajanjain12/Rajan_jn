package com.kyobee.waitlist.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RequestAddGuest{
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("organizationID")
    @Expose
    private String organizationID;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("optin")
    @Expose
    private String optin;
    @SerializedName("guestPreferences")
    @Expose
    private List<GuestPreference> guestPreferences = new ArrayList<> ();
    @SerializedName("prefType")
    @Expose
    private String prefType;
    @SerializedName("sms")
    @Expose
    private String sms="";

    @SerializedName("email")
    @Expose
    private String email="";

    @SerializedName("noOfPeople")
    @Expose
    private String noOfPeople="";
    @SerializedName("name")
    @Expose
    private String name="";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(String organizationID) {
        this.organizationID = organizationID;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getOptin() {
        return optin;
    }

    public void setOptin(String optin) {
        this.optin = optin;
    }

    public List<GuestPreference> getGuestPreferences() {
        return guestPreferences;
    }

    public void setGuestPreferences(List<GuestPreference> guestPreferences) {
        this.guestPreferences = guestPreferences;
    }

    public String getPrefType() {
        return prefType;
    }

    public void setPrefType(String prefType) {
        this.prefType = prefType;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getEmail (){
        return email;
    }

    public void setEmail (String email){
        this.email = email;
    }

    public String getNoOfPeople() {
        return noOfPeople;
    }

    public void setNoOfPeople(String noOfPeople) {
        this.noOfPeople = noOfPeople;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
