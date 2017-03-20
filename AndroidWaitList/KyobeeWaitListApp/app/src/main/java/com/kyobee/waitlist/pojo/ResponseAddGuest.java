package com.kyobee.waitlist.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by BHARATI on 3/7/2017.
 */

public class ResponseAddGuest{

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
    private ServiceResultAdd serviceResultAdd;

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

    public ServiceResultAdd getServiceResultAdd() {
        return serviceResultAdd;
    }

    public void setServiceResultAdd(ServiceResultAdd serviceResultAdd) {
        this.serviceResultAdd = serviceResultAdd;
    }
}
