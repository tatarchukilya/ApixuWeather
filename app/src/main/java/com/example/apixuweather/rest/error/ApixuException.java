package com.example.apixuweather.rest.error;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApixuException extends Exception {

    @SerializedName("error")
    @Expose
    private Error error;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public class Error{
        @SerializedName("code")
        @Expose
        private Integer code;
        @SerializedName("message")
        @Expose
        private String message;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
