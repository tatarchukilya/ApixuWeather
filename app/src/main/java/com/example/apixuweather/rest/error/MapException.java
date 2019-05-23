package com.example.apixuweather.rest.error;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MapException extends Exception {

    @SerializedName("error_message")
    @Expose
    private String errorMessage;
    @SerializedName("status")
    @Expose
    private String status;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
