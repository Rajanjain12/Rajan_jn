package com.kyobee.waitlist.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created on 3/11/2017.
 */

public class Add{

    @SerializedName("totalPartiesWaiting")
    @Expose
    private int totalPartiesWaiting;
    @SerializedName("NEXT_TO_NOTIFY_GUEST_ID")
    @Expose
    private int nEXTTONOTIFYGUESTID;
    @SerializedName("OP")
    @Expose
    private String oP;
    @SerializedName("addedGuestId")
    @Expose
    private int addedGuestId;
    @SerializedName("totalWaitTime")
    @Expose
    private int totalWaitTime;
    @SerializedName("ORG_TOTAL_WAIT_TIME")
    @Expose
    private int oRGTOTALWAITTIME;
    @SerializedName("NOW_SERVING_GUEST_ID")
    @Expose
    private int nOWSERVINGGUESTID;
    @SerializedName("guestRank")
    @Expose
    private int guestRank;
    @SerializedName("guestUUID")
    @Expose
    private String guestUUID;
    @SerializedName("nowServingParty")
    @Expose
    private int nowServingParty;
    @SerializedName("orgid")
    @Expose
    private int orgid;

    public int getTotalPartiesWaiting() {
        return totalPartiesWaiting;
    }

    public void setTotalPartiesWaiting(int totalPartiesWaiting) {
        this.totalPartiesWaiting = totalPartiesWaiting;
    }

    public int getNEXTTONOTIFYGUESTID() {
        return nEXTTONOTIFYGUESTID;
    }

    public void setNEXTTONOTIFYGUESTID(int nEXTTONOTIFYGUESTID) {
        this.nEXTTONOTIFYGUESTID = nEXTTONOTIFYGUESTID;
    }

    public String getOP() {
        return oP;
    }

    public void setOP(String oP) {
        this.oP = oP;
    }

    public int getAddedGuestId() {
        return addedGuestId;
    }

    public void setAddedGuestId(int addedGuestId) {
        this.addedGuestId = addedGuestId;
    }

    public int getTotalWaitTime() {
        return totalWaitTime;
    }

    public void setTotalWaitTime(int totalWaitTime) {
        this.totalWaitTime = totalWaitTime;
    }

    public int getORGTOTALWAITTIME() {
        return oRGTOTALWAITTIME;
    }

    public void setORGTOTALWAITTIME(int oRGTOTALWAITTIME) {
        this.oRGTOTALWAITTIME = oRGTOTALWAITTIME;
    }

    public int getNOWSERVINGGUESTID() {
        return nOWSERVINGGUESTID;
    }

    public void setNOWSERVINGGUESTID(int nOWSERVINGGUESTID) {
        this.nOWSERVINGGUESTID = nOWSERVINGGUESTID;
    }

    public int getGuestRank() {
        return guestRank;
    }

    public void setGuestRank(int guestRank) {
        this.guestRank = guestRank;
    }

    public String getGuestUUID() {
        return guestUUID;
    }

    public void setGuestUUID(String guestUUID) {
        this.guestUUID = guestUUID;
    }

    public int getNowServingParty() {
        return nowServingParty;
    }

    public void setNowServingParty(int nowServingParty) {
        this.nowServingParty = nowServingParty;
    }

    public int getOrgid() {
        return orgid;
    }

    public void setOrgid(int orgid) {
        this.orgid = orgid;
    }
}
