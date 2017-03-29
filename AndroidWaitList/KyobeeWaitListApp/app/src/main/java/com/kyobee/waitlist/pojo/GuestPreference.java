package com.kyobee.waitlist.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GuestPreference{
    @SerializedName("prefValueId")
    @Expose
    private int prefValueId;
    @SerializedName("prefValue")
    @Expose
    private String prefValue;

    public GuestPreference (int prefValueId, String prefValue){
        this.prefValueId = prefValueId;
        this.prefValue = prefValue;
    }

    public int getPrefValueId (){
        return prefValueId;
    }

    public void setPrefValueId (int prefValueId){
        this.prefValueId = prefValueId;
    }

    public String getPrefValue (){
        return prefValue;
    }

    public void setPrefValue (String prefValue){
        this.prefValue = prefValue;
    }
}
