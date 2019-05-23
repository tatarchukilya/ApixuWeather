
package com.example.apixuweather.rest.response.apixu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "daily")
public class Forecastday {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String locationId;

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("date_epoch")
    @Expose
    private Long dateEpoch;
    @SerializedName("day")
    @Expose
    @Embedded
    private Day day;
    @SerializedName("astro")
    @Expose
    @Embedded
    private Astro astro;
    @SerializedName("hour")
    @Expose
    @Ignore
    private List<Hour> hour = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
        for (Hour hourr : hour) {
            hourr.setLocationId(locationId);
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getDateEpoch() {
        return dateEpoch;
    }

    public void setDateEpoch(Long dateEpoch) {
        this.dateEpoch = dateEpoch;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public Astro getAstro() {
        return astro;
    }

    public void setAstro(Astro astro) {
        this.astro = astro;
    }

    public List<Hour> getHour() {
        return hour;
    }

    public void setHour(List<Hour> hour) {
        this.hour = hour;
    }

    public String getTempMax(){
        return day == null ? null : day.getMaxTemp();
    }

    public String getTempMin(){
        return day == null ? null : day.getMinTemp();
    }

    public String getWind(){
        return day == null ? null : day.getWind();
    }
}
