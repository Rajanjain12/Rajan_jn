package com.kyobee.waitlist.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 3/8/2017.
 */

public class NotPresent{
    @SerializedName("NEXT_TO_NOTIFY_GUEST_ID")
    @Expose
    private int nEXTTONOTIFYGUESTID;
    @SerializedName("OP")
    @Expose
    private String oP;
    @SerializedName("updguest")
    @Expose
    private UpdatedTime.Updguest updguest;
    @SerializedName("totalWaitTime")
    @Expose
    private int totalWaitTime;
    @SerializedName("ORG_TOTAL_WAIT_TIME")
    @Expose
    private int oRGTOTALWAITTIME;
    @SerializedName("guestObj")
    @Expose
    private int guestObj;
    @SerializedName("ORG_GUEST_COUNT")
    @Expose
    private int oRGGUESTCOUNT;
    @SerializedName("NOW_SERVING_GUEST_ID")
    @Expose
    private int nOWSERVINGGUESTID;
    @SerializedName("FROM")
    @Expose
    private String fROM;
    @SerializedName("orgid")
    @Expose
    private int orgid;

    public int getNEXTTONOTIFYGUESTID (){
        return nEXTTONOTIFYGUESTID;
    }

    public void setNEXTTONOTIFYGUESTID (int nEXTTONOTIFYGUESTID){
        this.nEXTTONOTIFYGUESTID = nEXTTONOTIFYGUESTID;
    }

    public String getOP (){
        return oP;
    }

    public void setOP (String oP){
        this.oP = oP;
    }

    public UpdatedTime.Updguest getUpdguest (){
        return updguest;
    }

    public void setUpdguest (UpdatedTime.Updguest updguest){
        this.updguest = updguest;
    }

    public int getTotalWaitTime (){
        return totalWaitTime;
    }

    public void setTotalWaitTime (int totalWaitTime){
        this.totalWaitTime = totalWaitTime;
    }

    public int getORGTOTALWAITTIME (){
        return oRGTOTALWAITTIME;
    }

    public void setORGTOTALWAITTIME (int oRGTOTALWAITTIME){
        this.oRGTOTALWAITTIME = oRGTOTALWAITTIME;
    }

    public int getGuestObj (){
        return guestObj;
    }

    public void setGuestObj (int guestObj){
        this.guestObj = guestObj;
    }

    public int getORGGUESTCOUNT (){
        return oRGGUESTCOUNT;
    }

    public void setORGGUESTCOUNT (int oRGGUESTCOUNT){
        this.oRGGUESTCOUNT = oRGGUESTCOUNT;
    }

    public int getNOWSERVINGGUESTID (){
        return nOWSERVINGGUESTID;
    }

    public void setNOWSERVINGGUESTID (int nOWSERVINGGUESTID){
        this.nOWSERVINGGUESTID = nOWSERVINGGUESTID;
    }

    public String getFROM (){
        return fROM;
    }

    public void setFROM (String fROM){
        this.fROM = fROM;
    }

    public int getOrgid (){
        return orgid;
    }

    public void setOrgid (int orgid){
        this.orgid = orgid;
    }

    public class UpdatedTime{

        @SerializedName("date")
        @Expose
        private int date;
        @SerializedName("hours")
        @Expose
        private int hours;
        @SerializedName("seconds")
        @Expose
        private int seconds;
        @SerializedName("month")
        @Expose
        private int month;
        @SerializedName("timezoneOffset")
        @Expose
        private int timezoneOffset;
        @SerializedName("year")
        @Expose
        private int year;
        @SerializedName("minutes")
        @Expose
        private int minutes;
        @SerializedName("time")
        @Expose
        private int time;
        @SerializedName("day")
        @Expose
        private int day;

        public int getDate (){
            return date;
        }

        public void setDate (int date){
            this.date = date;
        }

        public int getHours (){
            return hours;
        }

        public void setHours (int hours){
            this.hours = hours;
        }

        public int getSeconds (){
            return seconds;
        }

        public void setSeconds (int seconds){
            this.seconds = seconds;
        }

        public int getMonth (){
            return month;
        }

        public void setMonth (int month){
            this.month = month;
        }

        public int getTimezoneOffset (){
            return timezoneOffset;
        }

        public void setTimezoneOffset (int timezoneOffset){
            this.timezoneOffset = timezoneOffset;
        }

        public int getYear (){
            return year;
        }

        public void setYear (int year){
            this.year = year;
        }

        public int getMinutes (){
            return minutes;
        }

        public void setMinutes (int minutes){
            this.minutes = minutes;
        }

        public int getTime (){
            return time;
        }

        public void setTime (int time){
            this.time = time;
        }

        public int getDay (){
            return day;
        }

        public void setDay (int day){
            this.day = day;
        }

        public class Updguest{

            @SerializedName("deviceType")
            @Expose
            private String deviceType;
            @SerializedName("note")
            @Expose
            private String note;
            @SerializedName("updatedTime")
            @Expose
            private UpdatedTime updatedTime;
            @SerializedName("checkinTime")
            @Expose
            private Object checkinTime;
            @SerializedName("optin")
            @Expose
            private boolean optin;
            @SerializedName("deviceId")
            @Expose
            private String deviceId;
            @SerializedName("uuid")
            @Expose
            private String uuid;
            @SerializedName("organizationID")
            @Expose
            private int organizationID;
            @SerializedName("prefType")
            @Expose
            private String prefType;
            @SerializedName("seatedTime")
            @Expose
            private Object seatedTime;
            @SerializedName("guestPreferences")
            @Expose
            private List<Object> guestPreferences = null;
            @SerializedName("calloutCount")
            @Expose
            private int calloutCount;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("sms")
            @Expose
            private String sms;
            @SerializedName("createdTime")
            @Expose
            private Object createdTime;
            @SerializedName("rank")
            @Expose
            private int rank;
            @SerializedName("seatingPreference")
            @Expose
            private Object seatingPreference;
            @SerializedName("resetTime")
            @Expose
            private Object resetTime;
            @SerializedName("guestID")
            @Expose
            private int guestID;
            @SerializedName("noOfPeople")
            @Expose
            private int noOfPeople;
            @SerializedName("email")
            @Expose
            private String email;
            @SerializedName("incompleteParty")
            @Expose
            private int incompleteParty;
            @SerializedName("status")
            @Expose
            private String status;

            public String getDeviceType (){
                return deviceType;
            }

            public void setDeviceType (String deviceType){
                this.deviceType = deviceType;
            }

            public String getNote (){
                return note;
            }

            public void setNote (String note){
                this.note = note;
            }

            public UpdatedTime getUpdatedTime (){
                return updatedTime;
            }

            public void setUpdatedTime (UpdatedTime updatedTime){
                this.updatedTime = updatedTime;
            }

            public Object getCheckinTime (){
                return checkinTime;
            }

            public void setCheckinTime (Object checkinTime){
                this.checkinTime = checkinTime;
            }

            public boolean isOptin (){
                return optin;
            }

            public void setOptin (boolean optin){
                this.optin = optin;
            }

            public String getDeviceId (){
                return deviceId;
            }

            public void setDeviceId (String deviceId){
                this.deviceId = deviceId;
            }

            public String getUuid (){
                return uuid;
            }

            public void setUuid (String uuid){
                this.uuid = uuid;
            }

            public int getOrganizationID (){
                return organizationID;
            }

            public void setOrganizationID (int organizationID){
                this.organizationID = organizationID;
            }

            public String getPrefType (){
                return prefType;
            }

            public void setPrefType (String prefType){
                this.prefType = prefType;
            }

            public Object getSeatedTime (){
                return seatedTime;
            }

            public void setSeatedTime (Object seatedTime){
                this.seatedTime = seatedTime;
            }

            public List<Object> getGuestPreferences (){
                return guestPreferences;
            }

            public void setGuestPreferences (List<Object> guestPreferences){
                this.guestPreferences = guestPreferences;
            }

            public int getCalloutCount (){
                return calloutCount;
            }

            public void setCalloutCount (int calloutCount){
                this.calloutCount = calloutCount;
            }

            public String getName (){
                return name;
            }

            public void setName (String name){
                this.name = name;
            }

            public String getSms (){
                return sms;
            }

            public void setSms (String sms){
                this.sms = sms;
            }

            public Object getCreatedTime (){
                return createdTime;
            }

            public void setCreatedTime (Object createdTime){
                this.createdTime = createdTime;
            }

            public int getRank (){
                return rank;
            }

            public void setRank (int rank){
                this.rank = rank;
            }

            public Object getSeatingPreference (){
                return seatingPreference;
            }

            public void setSeatingPreference (Object seatingPreference){
                this.seatingPreference = seatingPreference;
            }

            public Object getResetTime (){
                return resetTime;
            }

            public void setResetTime (Object resetTime){
                this.resetTime = resetTime;
            }

            public int getGuestID (){
                return guestID;
            }

            public void setGuestID (int guestID){
                this.guestID = guestID;
            }

            public int getNoOfPeople (){
                return noOfPeople;
            }

            public void setNoOfPeople (int noOfPeople){
                this.noOfPeople = noOfPeople;
            }

            public String getEmail (){
                return email;
            }

            public void setEmail (String email){
                this.email = email;
            }

            public int getIncompleteParty (){
                return incompleteParty;
            }

            public void setIncompleteParty (int incompleteParty){
                this.incompleteParty = incompleteParty;
            }

            public String getStatus (){
                return status;
            }

            public void setStatus (String status){
                this.status = status;
            }


        }
    }
}
