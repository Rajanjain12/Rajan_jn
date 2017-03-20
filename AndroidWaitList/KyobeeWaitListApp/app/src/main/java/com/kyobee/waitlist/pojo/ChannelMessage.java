package com.kyobee.waitlist.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ChannelMessage{


    @SerializedName ("ch")
    @Expose
    String ch;

    @SerializedName ("m")
    @Expose
    String m;

    @SerializedName ("OP")
    @Expose
    String OP;

    public String getOP (){
        return OP;
    }

    public void setOP (String OP){
        this.OP = OP;
    }

    public String getCh (){
        return ch;
    }

    public void setCh (String ch){
        this.ch = ch;
    }

    public String getM (){
        return m;
    }

    public void setM (String m){
        this.m = m;
    }

}
