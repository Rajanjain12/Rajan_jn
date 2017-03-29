package com.kyobee.waitlist.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Seatpref{

    @SerializedName("prefValueId")
    @Expose
    private int prefValueId;
    @SerializedName("prefValue")
    @Expose
    private String prefValue;
    @SerializedName("guestPrefId")
    @Expose
    private int guestPrefId;
    @SerializedName("selected")
    @Expose
    private Boolean selected;

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

    public int getGuestPrefId (){
        return guestPrefId;
    }

    public void setGuestPrefId (int guestPrefId){
        this.guestPrefId = guestPrefId;
    }

    public Boolean getSelected (){
        return selected;
    }

    public void setSelected (Boolean selected){
        this.selected = selected;
    }
}
