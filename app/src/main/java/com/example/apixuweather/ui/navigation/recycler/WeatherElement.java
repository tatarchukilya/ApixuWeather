package com.example.apixuweather.ui.navigation.recycler;

public class WeatherElement {

    private String mTempC;
    private String mTempF;
    private Integer isDay;
    private String mCode;

    private String mLocationId;
    private String mName;
    private String mSecondName;

    public WeatherElement(String tempC, String tempF, Integer isDay, String code, String locationId, String name, String secondName) {
        mTempC = tempC;
        mTempF = tempF;
        this.isDay = isDay;
        mCode = code;
        mLocationId = locationId;
        mName = name;
        mSecondName = secondName;
    }

    public WeatherElement() {
    }

    public Integer getIsDay() {
        return isDay;
    }

    public void setIsDay(Integer isDay) {
        this.isDay = isDay;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public String getIconName(){
        String code = "w_" + String.valueOf(getCode());
        String isDay = getIsDay() == 1 ? "_day_" : "_night_";
        return code + isDay + "anim";
    }

    public String getTempC() {
        return mTempC;
    }

    public void setTempC(String tempC) {
        mTempC = tempC;
    }

    public String getTempF() {
        return mTempF;
    }

    public void setTempF(String tempF) {
        mTempF = tempF;
    }


    public String getLocationId() {
        return mLocationId;
    }

    public void setLocationId(String locationId) {
        mLocationId = locationId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getSecondName() {
        return mSecondName;
    }

    public void setSecondName(String secondName) {
        mSecondName = secondName;
    }
}
