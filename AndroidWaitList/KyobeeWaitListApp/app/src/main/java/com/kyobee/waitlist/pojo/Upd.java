package com.kyobee.waitlist.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created on 3/11/2017.
 */

public class Upd{

    @SerializedName("OP")
    @Expose
    private String oP;

    @SerializedName("ORG_TOTAL_WAIT_TIME")
    @Expose
    private int oRGTOTALWAITTIME;

    @SerializedName("ORG_GUEST_COUNT")
    @Expose
    private int oRGGUESTCOUNT;

    @SerializedName("NOW_SERVING_GUEST_ID")
    @Expose
    private int nOWSERVINGGUESTID;

    public String getoP (){
        return oP;
    }

    public void setoP (String oP){
        this.oP = oP;
    }

    public int getoRGTOTALWAITTIME (){
        return oRGTOTALWAITTIME;
    }

    public void setoRGTOTALWAITTIME (int oRGTOTALWAITTIME){
        this.oRGTOTALWAITTIME = oRGTOTALWAITTIME;
    }

    public int getoRGGUESTCOUNT (){
        return oRGGUESTCOUNT;
    }

    public void setoRGGUESTCOUNT (int oRGGUESTCOUNT){
        this.oRGGUESTCOUNT = oRGGUESTCOUNT;
    }

    public int getnOWSERVINGGUESTID (){
        return nOWSERVINGGUESTID;
    }

    public void setnOWSERVINGGUESTID (int nOWSERVINGGUESTID){
        this.nOWSERVINGGUESTID = nOWSERVINGGUESTID;
    }
}
