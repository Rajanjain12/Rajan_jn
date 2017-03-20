package com.kyobee.waitlist.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created on 3/8/2017.
 */

public class UpdateGuest{

    @SerializedName("ORG_TOTAL_WAIT_TIME")
    @Expose
    private int oRGTOTALWAITTIME;
    @SerializedName("totalWaitTime")
    @Expose
    private int totalWaitTime;
    @SerializedName("orgid")
    @Expose
    private int orgid;
    @SerializedName("NEXT_TO_NOTIFY_GUEST_ID")
    @Expose
    private int nEXTTONOTIFYGUESTID;
    @SerializedName("updguest")
    @Expose
    private UpdGuest updGuest;
    @SerializedName("nowServingParty")
    @Expose
    private int nowServingParty;
    @SerializedName("FROM")
    @Expose
    private String fROM;
    @SerializedName("guestObj")
    @Expose
    private int guestObj;
    @SerializedName("OP")
    @Expose
    private String oP;
    @SerializedName("NOW_SERVING_GUEST_ID")
    @Expose
    private int nOWSERVINGGUESTID;

    public int getORGTOTALWAITTIME() {
        return oRGTOTALWAITTIME;
    }

    public void setORGTOTALWAITTIME(int oRGTOTALWAITTIME) {
        this.oRGTOTALWAITTIME = oRGTOTALWAITTIME;
    }

    public int getTotalWaitTime() {
        return totalWaitTime;
    }

    public void setTotalWaitTime(int totalWaitTime) {
        this.totalWaitTime = totalWaitTime;
    }

    public int getOrgid() {
        return orgid;
    }

    public void setOrgid(int orgid) {
        this.orgid = orgid;
    }

    public int getNEXTTONOTIFYGUESTID() {
        return nEXTTONOTIFYGUESTID;
    }

    public void setNEXTTONOTIFYGUESTID(int nEXTTONOTIFYGUESTID) {
        this.nEXTTONOTIFYGUESTID = nEXTTONOTIFYGUESTID;
    }

    public UpdGuest getUpdguest() {
        return updGuest;
    }

    public void setUpdguest(UpdGuest updGuest) {
        this.updGuest = updGuest;
    }

    public int getNowServingParty() {
        return nowServingParty;
    }

    public void setNowServingParty(int nowServingParty) {
        this.nowServingParty = nowServingParty;
    }

    public String getFROM() {
        return fROM;
    }

    public void setFROM(String fROM) {
        this.fROM = fROM;
    }

    public int getGuestObj() {
        return guestObj;
    }

    public void setGuestObj(int guestObj) {
        this.guestObj = guestObj;
    }

    public String getOP() {
        return oP;
    }

    public void setOP(String oP) {
        this.oP = oP;
    }

    public int getNOWSERVINGGUESTID() {
        return nOWSERVINGGUESTID;
    }

    public void setNOWSERVINGGUESTID(int nOWSERVINGGUESTID) {
        this.nOWSERVINGGUESTID = nOWSERVINGGUESTID;
    }


}
