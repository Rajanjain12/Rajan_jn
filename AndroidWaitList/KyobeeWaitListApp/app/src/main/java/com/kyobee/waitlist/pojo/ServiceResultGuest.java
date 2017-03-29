package com.kyobee.waitlist.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceResultGuest{

    @SerializedName("GUEST_RANK_MIN")
    @Expose
    private String gUESTRANKMIN;
    @SerializedName("OP_NOTIFYUSERCOUNT")
    @Expose
    private String oPNOTIFYUSERCOUNT;
    @SerializedName("CLIENT_BASE")
    @Expose
    private String cLIENTBASE;
    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("ORG_TOTAL_WAIT_TIME")
    @Expose
    private String oRGTOTALWAITTIME;
    @SerializedName("imageOrgPath")
    @Expose
    private String imageOrgPath;
    @SerializedName("ORG_GUEST_COUNT")
    @Expose
    private String oRGGUESTCOUNT;
    @SerializedName("OP_GUESTTOBENOTIFIED")
    @Expose
    private String oPGUESTTOBENOTIFIED;
    @SerializedName("ORG_WAIT_TIME")
    @Expose
    private String oRGWAITTIME;
    @SerializedName("OP_GUESTNOTIFIEDWAITTIME")
    @Expose
    private String oPGUESTNOTIFIEDWAITTIME;
    @SerializedName("OP_NOOFPARTIESAHEAD")
    @Expose
    private String oPNOOFPARTIESAHEAD;

    public String getGUESTRANKMIN() {
        return gUESTRANKMIN;
    }

    public void setGUESTRANKMIN(String gUESTRANKMIN) {
        this.gUESTRANKMIN = gUESTRANKMIN;
    }

    public String getOPNOTIFYUSERCOUNT() {
        return oPNOTIFYUSERCOUNT;
    }

    public void setOPNOTIFYUSERCOUNT(String oPNOTIFYUSERCOUNT) {
        this.oPNOTIFYUSERCOUNT = oPNOTIFYUSERCOUNT;
    }

    public String getCLIENTBASE() {
        return cLIENTBASE;
    }

    public void setCLIENTBASE(String cLIENTBASE) {
        this.cLIENTBASE = cLIENTBASE;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getORGTOTALWAITTIME() {
        return oRGTOTALWAITTIME;
    }

    public void setORGTOTALWAITTIME(String oRGTOTALWAITTIME) {
        this.oRGTOTALWAITTIME = oRGTOTALWAITTIME;
    }

    public String getImageOrgPath() {
        return imageOrgPath;
    }

    public void setImageOrgPath(String imageOrgPath) {
        this.imageOrgPath = imageOrgPath;
    }

    public String getORGGUESTCOUNT() {
        return oRGGUESTCOUNT;
    }

    public void setORGGUESTCOUNT(String oRGGUESTCOUNT) {
        this.oRGGUESTCOUNT = oRGGUESTCOUNT;
    }

    public String getOPGUESTTOBENOTIFIED() {
        return oPGUESTTOBENOTIFIED;
    }

    public void setOPGUESTTOBENOTIFIED(String oPGUESTTOBENOTIFIED) {
        this.oPGUESTTOBENOTIFIED = oPGUESTTOBENOTIFIED;
    }

    public String getORGWAITTIME() {
        return oRGWAITTIME;
    }

    public void setORGWAITTIME(String oRGWAITTIME) {
        this.oRGWAITTIME = oRGWAITTIME;
    }

    public String getOPGUESTNOTIFIEDWAITTIME() {
        return oPGUESTNOTIFIEDWAITTIME;
    }

    public void setOPGUESTNOTIFIEDWAITTIME(String oPGUESTNOTIFIEDWAITTIME) {
        this.oPGUESTNOTIFIEDWAITTIME = oPGUESTNOTIFIEDWAITTIME;
    }

    public String getOPNOOFPARTIESAHEAD() {
        return oPNOOFPARTIESAHEAD;
    }

    public void setOPNOOFPARTIESAHEAD(String oPNOOFPARTIESAHEAD) {
        this.oPNOOFPARTIESAHEAD = oPNOOFPARTIESAHEAD;
    }
}
