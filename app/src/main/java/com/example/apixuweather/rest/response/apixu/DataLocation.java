package com.example.apixuweather.rest.response.apixu;

import com.example.apixuweather.rest.response.maps.Prediction;
import com.example.apixuweather.rest.response.maps.ResponseDetails;

import java.util.concurrent.TimeUnit;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;

public class DataLocation {

    @ColumnInfo(name = "locationId")
    private String mLocationId;
    @ColumnInfo(name = "name")
    private String mFirstName;
    @ColumnInfo(name = "region")
    private String mSecondName;
    @ColumnInfo(name = "lat")
    private Double mLat;
    @ColumnInfo(name = "lon")
    private Double mLng;
    @ColumnInfo(name = "timeZone")
    private Integer mTimezone;

    @Ignore
    public DataLocation() {
    }

    public DataLocation(String locationId, String firstName, String secondName, Double lat, Double lng, Integer timezone) {
        mLocationId = locationId;
        mFirstName = firstName;
        mSecondName = secondName;
        mLat = lat;
        mLng = lng;
        mTimezone = timezone;
    }

    @Ignore
    public DataLocation(Prediction prediction, ResponseDetails details){
        mLocationId = prediction.getPlaceId();
        mFirstName = prediction.getStructuredFormatting().getMainText();
        mSecondName = prediction.getStructuredFormatting().getSecondaryText();
        mLat = details.getResult().getGeometry().getLocation().getLat();
        mLng = details.getResult().getGeometry().getLocation().getLng();
        mTimezone = details.getResult().getUtcOffset();
        if (details.getResult().getUtcOffset() != null){
            mTimezone = (int) TimeUnit.MINUTES.toSeconds(details.getResult().getUtcOffset());
        }

    }

    public String getLocationId() {
        return mLocationId;
    }

    public void setLocationId(String locationId) {
        mLocationId = locationId;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getSecondName() {
        return mSecondName;
    }

    public void setSecondName(String secondName) {
        mSecondName = secondName;
    }

    public Double getLat() {
        return mLat;
    }

    public void setLat(Double lat) {
        mLat = lat;
    }

    public Double getLng() {
        return mLng;
    }

    public void setLng(Double lng) {
        mLng = lng;
    }

    public Integer getTimezone() {
        return mTimezone;
    }

    public void setTimezone(Integer timezone) {
        mTimezone = timezone;
    }
}
