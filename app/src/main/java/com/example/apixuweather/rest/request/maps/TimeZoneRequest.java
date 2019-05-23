package com.example.apixuweather.rest.request.maps;

import java.util.Map;

public class TimeZoneRequest extends MapsRequest {

    private Double mLat;
    private Double mLng;
    private final String mTimeStamp = "0";

    public TimeZoneRequest(Double lat, Double lng) {
        mLat = lat;
        mLng = lng;
    }

    @Override
    public void onMapCreate(Map<String, String> map) {
        super.onMapCreate(map);
        map.put("location", String.valueOf(mLat) + "," + mLng);
        map.put("timestamp", mTimeStamp);
    }
}
