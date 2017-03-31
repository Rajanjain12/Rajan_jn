package com.kyobee.waitlist.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Login{

    @SerializedName("OrgId")
    @Expose
    private String orgId;
    @SerializedName("clientBase")
    @Expose
    private String clientBase;
    @SerializedName("seatpref")
    @Expose
    private List<Seatpref> seatpref = null;

    @SerializedName("smsRoute")
    @Expose
    private String smsRoute="";

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("logofile name")
    @Expose
    private String logofileName;

    @SerializedName("error")
    @Expose
    private String error;

    public String getOrgId (){
        return orgId;
    }

    public void setOrgId (String orgId){
        this.orgId = orgId;
    }

    public String getClientBase (){
        return clientBase;
    }

    public void setClientBase (String clientBase){
        this.clientBase = clientBase;
    }

    public List<Seatpref> getSeatpref (){
        return seatpref;
    }

    public void setSeatpref (List<Seatpref> seatpref){
        this.seatpref = seatpref;
    }

    public String getSuccess (){
        return success;
    }

    public void setSuccess (String success){
        this.success = success;
    }

    public String getLogofileName (){
        return logofileName;
    }

    public void setLogofileName (String logofileName){
        this.logofileName = logofileName;
    }

    public String getError (){
        return error;
    }

    public void setError (String error){
        this.error = error;
    }

    public String getSmsRoute() {
        return smsRoute;
    }

    public void setSmsRoute(String smsRoute) {
        this.smsRoute = smsRoute;
    }

}
