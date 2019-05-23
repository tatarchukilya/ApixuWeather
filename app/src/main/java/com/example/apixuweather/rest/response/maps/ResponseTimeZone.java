package com.example.apixuweather.rest.response.maps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseTimeZone extends ResponseBaseMap{

    @SerializedName("dstOffset")
    @Expose
    private Integer dstOffset;
    @SerializedName("rawOffset")
    @Expose
    private Integer rawOffset;
    @SerializedName("timeZoneId")
    @Expose
    private String timeZoneId;
    @SerializedName("timeZoneName")
    @Expose
    private String timeZoneName;

    public Integer getDstOffset() {
        return dstOffset;
    }

    public void setDstOffset(Integer dstOffset) {
        this.dstOffset = dstOffset;
    }

    public Integer getRawOffset() {
        return rawOffset;
    }

    public void setRawOffset(Integer rawOffset) {
        this.rawOffset = rawOffset;
    }

    public String getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    public String getTimeZoneName() {
        return timeZoneName;
    }

    public void setTimeZoneName(String timeZoneName) {
        this.timeZoneName = timeZoneName;
    }
}
