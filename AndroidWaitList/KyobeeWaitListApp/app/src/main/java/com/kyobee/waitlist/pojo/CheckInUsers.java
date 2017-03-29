package com.kyobee.waitlist.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckInUsers{
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("errorCode")
    @Expose
    private Object errorCode;
    @SerializedName("errorDescription")
    @Expose
    private String errorDescription;
    @SerializedName("serviceResponseDescription")
    @Expose
    private Object serviceResponseDescription;
    @SerializedName("serviceIdentifier")
    @Expose
    private Object serviceIdentifier;
    @SerializedName("serviceResult")
    @Expose
    private ServiceResultDisplay serviceResult;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Object errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public Object getServiceResponseDescription() {
        return serviceResponseDescription;
    }

    public void setServiceResponseDescription(Object serviceResponseDescription) {
        this.serviceResponseDescription = serviceResponseDescription;
    }

    public Object getServiceIdentifier() {
        return serviceIdentifier;
    }

    public void setServiceIdentifier(Object serviceIdentifier) {
        this.serviceIdentifier = serviceIdentifier;
    }

    public ServiceResultDisplay getServiceResult() {
        return serviceResult;
    }

    public void setServiceResult(ServiceResultDisplay serviceResult) {
        this.serviceResult = serviceResult;
    }
}
