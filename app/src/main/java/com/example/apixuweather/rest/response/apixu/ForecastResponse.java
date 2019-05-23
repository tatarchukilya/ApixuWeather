
package com.example.apixuweather.rest.response.apixu;

import com.example.apixuweather.rest.response.maps.ResponseAddress;
import com.example.apixuweather.rest.response.maps.ResponseTimeZone;
import com.example.apixuweather.ui.base.BaseItem;
import com.example.apixuweather.ui.navigation.recycler.SearchViewHolder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.core.util.Pair;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static com.example.apixuweather.utils.Const.CURRENT_LOCATION_ID;

@Entity(tableName = "current")
public class ForecastResponse implements BaseItem {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String locationId;

    @SerializedName("location")
    @Expose
    @Embedded
    private Location location;
    @SerializedName("current")
    @Expose
    @Embedded
    private Current current;
    @SerializedName("forecast")
    @Expose
    @Ignore
    private Forecast forecast;

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
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }

    public void setLocId(String id) {
        for (Forecastday forecastday : forecast.getForecastday()) {
            forecastday.setLocationId(id);
        }
    }

    public void setDataLocation(android.location.Location location, ResponseAddress responseAddress, ResponseTimeZone timeZone) {
        locationId = CURRENT_LOCATION_ID;
        Pair<String, String> address = responseAddress.getAddress();
        this.location.setName(address.first);
        this.location.setRegion(address.second);
        this.location.setLat(location.getLatitude());
        this.location.setLon(location.getLongitude());
        this.location.setTimeZone(timeZone.getRawOffset());
        setLocId(locationId);
    }

    public void setLocationData(DataLocation location) {
        setLocationId(location.getLocationId());
        this.location.setLocationData(location);
        setLocId(location.getLocationId());
    }

    public DataLocation getDataLocation(){
        return new DataLocation(locationId, location.getName(), location.getRegion(), location.getLat(), location.getLon(), location.getTimeZone());
    }

    @Override
    public int getLayoutType() {
        return SearchViewHolder.LAYOUT;
    }

    public void setForecast(ForecastResponse response) {
        setCurrent(response.getCurrent());
        setForecast(response.getForecast());
    }

    public boolean isNotCurrent(){
        return !locationId.equals(CURRENT_LOCATION_ID);
    }

    public boolean isNotFresh(){
        return !getCurrent().isFresh() && !locationId.equals(CURRENT_LOCATION_ID);
    }

    public boolean isFresh(){
        return current.isFresh();
    }

    public boolean isValidate(){
        return getCurrent().isValidate();
    }

    public boolean isRelevance(){
        return getCurrent().isRelevance();
    }
}
