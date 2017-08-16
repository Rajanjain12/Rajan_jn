package com.kyobee.waitlist.pojo.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 3/30/2017.
 */

public class ResponseGen{
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("errorCode")
    @Expose
    private String errorCode;
    @SerializedName("errorDescription")
    @Expose
    private String errorDescription;
    @SerializedName("serviceResponseDescription")
    @Expose
    private String serviceResponseDescription;
    @SerializedName("serviceIdentifier")
    @Expose
    private String serviceIdentifier;
    @SerializedName("serviceResult")
    @Expose
    private ServiceResult serviceResult;

    public String getStatus (){
        return status;
    }

    public void setStatus (String status){
        this.status = status;
    }


    public String getErrorDescription (){
        return errorDescription;
    }

    public void setErrorDescription (String errorDescription){
        this.errorDescription = errorDescription;
    }



    public ServiceResult getServiceResult (){
        return serviceResult;
    }

    public void setServiceResult (ServiceResult serviceResult){
        this.serviceResult = serviceResult;
    }

    public String getErrorCode (){
        return errorCode;
    }

    public void setErrorCode (String errorCode){
        this.errorCode = errorCode;
    }

    public String getServiceResponseDescription (){
        return serviceResponseDescription;
    }

    public void setServiceResponseDescription (String serviceResponseDescription){
        this.serviceResponseDescription = serviceResponseDescription;
    }

    public String getServiceIdentifier (){
        return serviceIdentifier;
    }

    public void setServiceIdentifier (String serviceIdentifier){
        this.serviceIdentifier = serviceIdentifier;
    }

    public class Record{

        @SerializedName("note")
        @Expose
        private String note;
        @SerializedName("optin")
        @Expose
        private boolean optin;
        @SerializedName("uuid")
        @Expose
        private String uuid;
        @SerializedName("deviceId")
        @Expose
        private String deviceId;
        @SerializedName("prefType")
        @Expose
        private String prefType;
        @SerializedName("sms")
        @Expose
        private String sms;
        @SerializedName("calloutCount")
        @Expose
        private String calloutCount;
        @SerializedName("rank")
        @Expose
        private int rank;
        @SerializedName("createdTime")
        @Expose
        private String createdTime;
        @SerializedName("recvLeveltwo")
        @Expose
        private String recvLeveltwo;
        @SerializedName("row")
        @Expose
        private int row;
        @SerializedName("seatingPreference")
        @Expose
        private String seatingPreference;
        @SerializedName("resetTime")
        @Expose
        private String resetTime;
        @SerializedName("noOfPeople")
        @Expose
        private String noOfPeople;
        @SerializedName("partyType")
        @Expose
        private int partyType;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("incompleteParty")
        @Expose
        private String incompleteParty;
        @SerializedName("deviceType")
        @Expose
        private String deviceType;
        @SerializedName("updatedTime")
        @Expose
        private String updatedTime;
        @SerializedName("checkinTime")
        @Expose
        private String checkinTime;
        @SerializedName("OrganizationID")
        @Expose
        private int organizationID;
        @SerializedName("seatedTime")
        @Expose
        private String seatedTime;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("WaitTime")
        @Expose
        private int waitTime;
        @SerializedName("guestID")
        @Expose
        private int guestID;
        @SerializedName("status")
        @Expose
        private String status;

        public String getDeviceId (){
            return deviceId;
        }

        public void setDeviceId (String deviceId){
            this.deviceId = deviceId;
        }

        public String getSms (){
            return sms;
        }

        public void setSms (String sms){
            this.sms = sms;
        }

        public void setCalloutCount (String calloutCount){
            this.calloutCount = calloutCount;
        }

        public String getCreatedTime (){
            return createdTime;
        }

        public void setCreatedTime (String createdTime){
            this.createdTime = createdTime;
        }

        public String getRecvLeveltwo (){
            return recvLeveltwo;
        }

        public void setRecvLeveltwo (String recvLeveltwo){
            this.recvLeveltwo = recvLeveltwo;
        }

        public String getResetTime (){
            return resetTime;
        }

        public void setResetTime (String resetTime){
            this.resetTime = resetTime;
        }

        public String getIncompleteParty (){
            return incompleteParty;
        }

        public void setIncompleteParty (String incompleteParty){
            this.incompleteParty = incompleteParty;
        }

        public String getDeviceType (){
            return deviceType;
        }

        public void setDeviceType (String deviceType){
            this.deviceType = deviceType;
        }

        public String getUpdatedTime (){
            return updatedTime;
        }

        public void setUpdatedTime (String updatedTime){
            this.updatedTime = updatedTime;
        }

        public String getCheckinTime (){
            return checkinTime;
        }

        public void setCheckinTime (String checkinTime){
            this.checkinTime = checkinTime;
        }

        public String getNote (){
            return note;
        }

        public void setNote (String note){
            this.note = note;
        }

      /*  public int getOptin (){
            return optin;
        }

        public void setOptin (int optin){
            this.optin = optin;
        }
*/

        public boolean isOptin (){
            return optin;
        }

        public void setOptin (boolean optin){
            this.optin = optin;
        }

        public String getUuid (){
            return uuid;
        }

        public void setUuid (String uuid){
            this.uuid = uuid;
        }

        public String getPrefType (){
            return prefType;
        }

        public void setPrefType (String prefType){
            this.prefType = prefType;
        }

        public String getCalloutCount (){
            return calloutCount;
        }

        public int getRank (){
            return rank;
        }

        public void setRank (int rank){
            this.rank = rank;
        }

        public int getRow (){
            return row;
        }

        public void setRow (int row){
            this.row = row;
        }

        public String getSeatingPreference (){
            return seatingPreference;
        }

        public void setSeatingPreference (String seatingPreference){
            this.seatingPreference = seatingPreference;
        }

        public String getNoOfPeople (){
            return noOfPeople;
        }

        public void setNoOfPeople (String noOfPeople){
            this.noOfPeople = noOfPeople;
        }

        public int getPartyType() {
            return partyType;
        }

        public void setPartyType(int partyType) {
            this.partyType = partyType;
        }

        public String getEmail (){
            return email;
        }

        public void setEmail (String email){
            this.email = email;
        }

        public int getOrganizationID (){
            return organizationID;
        }

        public void setOrganizationID (int organizationID){
            this.organizationID = organizationID;
        }


        public String getName (){
            return name;
        }

        public void setName (String name){
            this.name = name;
        }

        public int getWaitTime (){
            return waitTime;
        }

        public void setWaitTime (int waitTime){
            this.waitTime = waitTime;
        }

        public int getGuestID (){
            return guestID;
        }

        public void setGuestID (int guestID){
            this.guestID = guestID;
        }

        public String getStatus (){
            return status;
        }

        public void setStatus (String status){
            this.status = status;
        }
    }

    public class ServiceResult{

        @SerializedName("totalRecords")
        @Expose
        private int totalRecords;
        @SerializedName("pageNo")
        @Expose
        private int pageNo;
        @SerializedName("records")
        @Expose
        private List<Record> records = null;

        public int getTotalRecords (){
            return totalRecords;
        }

        public void setTotalRecords (int totalRecords){
            this.totalRecords = totalRecords;
        }

        public int getPageNo (){
            return pageNo;
        }

        public void setPageNo (int pageNo){
            this.pageNo = pageNo;
        }

        public List<Record> getRecords (){
            return records;
        }

        public void setRecords (List<Record> records){
            this.records = records;
        }
    }
}
