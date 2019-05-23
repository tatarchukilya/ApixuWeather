
package com.example.apixuweather.rest.response.apixu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.core.util.Pair;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;

public class Location {

    @ColumnInfo(name = "timeZone")
    private int timeZone;

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("country")
    @Expose
    @Ignore
    private String country;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lon")
    @Expose
    private Double lon;
    @SerializedName("tz_id")
    @Expose
    @Ignore
    private String tzId;
    @SerializedName("localtime_epoch")
    @Expose
    private Integer localtimeEpoch;
    @SerializedName("localtime")
    @Expose
    @Ignore
    private String localtime;

    public int getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(int timeZone) {
        this.timeZone = timeZone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getTzId() {
        return tzId;
    }

    public void setTzId(String tzId) {
        this.tzId = tzId;
    }

    public Integer getLocaltimeEpoch() {
        return localtimeEpoch;
    }

    public void setLocaltimeEpoch(Integer localtimeEpoch) {
        this.localtimeEpoch = localtimeEpoch;
    }

    public String getLocaltime() {
        return localtime;
    }

    public void setLocaltime(String localtime) {
        this.localtime = localtime;
    }

    public Pair<String, String> getAddress(){
        return new Pair<>(name, region);
    }

    public void setLocationData(DataLocation data){
        setName(data.getFirstName());
        setRegion(data.getSecondName());
        setLat(data.getLat());
        setLon(data.getLng());
        setTimeZone(data.getTimezone());
    }
}
